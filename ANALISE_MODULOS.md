# Análise Completa dos Módulos - Sistema de Mineração

## Visão Geral do Sistema

Este é um sistema de microserviços baseado em **Quarkus** que implementa uma solução para gestão de cotações e propostas de mineração. O sistema utiliza **Apache Kafka** para comunicação assíncrona entre os serviços e **Keycloak** para autenticação/autorização.

---

## 1. MÓDULO: cotacao-mineradora

### Tecnologias
- **Framework**: Quarkus 3.2.12.Final
- **Linguagem**: Java 17
- **Banco de Dados**: MariaDB
- **ORM**: Hibernate ORM com Panache
- **Mensageria**: Apache Kafka (SmallRye Reactive Messaging)
- **Agendamento**: Quartz Scheduler
- **REST Client**: MicroProfile REST Client (Reactive)
- **Observabilidade**: OpenTelemetry (Jaeger)
- **Build Tool**: Maven
- **Bibliotecas**:
  - Lombok 1.18.30
  - SLF4J para logging

### Porta
- **8081**

### Informações de Negócio

**Propósito**: Serviço responsável por buscar e monitorar cotações de moedas (especificamente USD-BRL) de uma API externa e publicar eventos quando há variação de preço.

**Funcionalidades**:
1. **Busca Automática de Cotação**: 
   - Executa a cada 35 segundos (via Quartz Scheduler)
   - Consulta a API externa `https://economia.awesomeapi.com.br` para obter a cotação USD-BRL
   - Armazena cotações no banco de dados MariaDB

2. **Lógica de Negócio**:
   - Salva a primeira cotação encontrada
   - Para cotações subsequentes, só salva e publica evento se o preço atual for **maior** que o último preço armazenado
   - Publica eventos no tópico Kafka `quotation` quando há nova cotação

3. **Estrutura de Dados**:
   - **QuotationEntity**: Armazena id, data, preço da moeda, percentual de mudança, par de moedas
   - **QuotationDTO**: DTO para comunicação via Kafka (data, currencyPrice)

4. **Integrações**:
   - **API Externa**: AwesomeAPI (economia.awesomeapi.com.br) - REST Client
   - **Kafka**: Publica no tópico `quotation`
   - **Banco**: MariaDB (schema: `cotacao-mineradora`)

**Fluxo de Dados**:
```
Scheduler (35s) → QuotationService → API Externa → Validação de Preço → 
Salva no BD → Publica no Kafka (se preço aumentou)
```

---

## 2. MÓDULO: proposta-mineradora

### Tecnologias
- **Framework**: Quarkus 3.2.12.Final
- **Linguagem**: Java 17
- **Banco de Dados**: MariaDB
- **ORM**: Hibernate ORM com Panache
- **Mensageria**: Apache Kafka (SmallRye Reactive Messaging)
- **Segurança**: Keycloak (OIDC) com Token Propagation
- **Observabilidade**: OpenTelemetry (Jaeger)
- **Build Tool**: Maven
- **Bibliotecas**:
  - Lombok 1.18.30
  - SLF4J para logging

### Porta
- **8082**

### Informações de Negócio

**Propósito**: Serviço responsável por gerenciar propostas comerciais de mineração, incluindo criação, consulta e remoção de propostas.

**Funcionalidades**:
1. **Gestão de Propostas**:
   - **Criar Proposta**: Recebe proposta via REST API, salva no banco e publica evento no Kafka
   - **Consultar Proposta**: Busca detalhes completos de uma proposta por ID
   - **Remover Proposta**: Deleta uma proposta do sistema

2. **Estrutura de Dados**:
   - **ProposalEntity**: 
     - ID, preço por tonelada, dias de validade da proposta
     - Data de criação, cliente, quantidade em toneladas, país
   - **ProposalDetailDTO**: DTO completo para API (inclui todos os campos)
   - **ProposalDTO**: DTO simplificado para Kafka (proposalId, costumer, priceTonne)

3. **Segurança**:
   - **Autenticação**: Obrigatória via Keycloak
   - **Autorização por Role**:
     - `proposal-customer`: Pode criar propostas
     - `user`, `manager`: Podem consultar propostas
     - `manager`: Pode remover propostas

4. **Integrações**:
   - **Kafka**: Publica no tópico `proposal` quando uma proposta é criada
   - **Banco**: MariaDB (schema: `proposta-mineradora`)

**Fluxo de Dados**:
```
Cliente → REST API (autenticado) → ProposalService → 
Salva no BD → Publica no Kafka (tópico: proposal)
```

**Endpoints**:
- `GET /api/proposal/{id}` - Consultar proposta
- `POST /api/proposal` - Criar proposta (role: proposal-customer)
- `DELETE /api/proposal/{id}` - Remover proposta (role: manager)

---

## 3. MÓDULO: relatorio-mineradora

### Tecnologias
- **Framework**: Quarkus 3.2.12.Final
- **Linguagem**: Java 17
- **Banco de Dados**: MariaDB
- **ORM**: Hibernate ORM com Panache
- **Mensageria**: Apache Kafka (SmallRye Reactive Messaging) - Consumer
- **Segurança**: Keycloak (OIDC) com Token Propagation
- **Observabilidade**: OpenTelemetry (Jaeger)
- **Build Tool**: Maven
- **Bibliotecas**:
  - Lombok 1.18.30
  - Apache Commons CSV 1.8
  - SLF4J para logging

### Porta
- **8083**

### Informações de Negócio

**Propósito**: Serviço responsável por processar eventos de propostas e cotações para gerar oportunidades de negócio e relatórios.

**Funcionalidades**:
1. **Processamento de Eventos Kafka**:
   - **Consumer de Propostas**: Recebe eventos do tópico `proposal`
   - **Consumer de Cotações**: Recebe eventos do tópico `quotation`
   - Cria oportunidades de negócio combinando propostas com a última cotação disponível

2. **Geração de Oportunidades**:
   - Quando recebe uma proposta, busca a última cotação de dólar
   - Cria uma **OpportunityEntity** combinando:
     - Dados da proposta (ID, cliente, preço por tonelada)
     - Última cotação de dólar disponível
     - Data da oportunidade

3. **Relatórios**:
   - **JSON**: Retorna lista de oportunidades em formato JSON
   - **CSV**: Gera relatório CSV com todas as oportunidades para download

4. **Estrutura de Dados**:
   - **OpportunityEntity**: 
     - ID, data, proposalId, cliente, preço por tonelada, última cotação dólar
   - **QuotationEntity**: Armazena cotações recebidas via Kafka
   - **OpportunityDTO**: DTO para API (proposalId, costumer, priceTonne, lastDollarQuotation)

5. **Segurança**:
   - **Autenticação**: Obrigatória via Keycloak
   - **Autorização**: Roles `user` e `manager` podem acessar relatórios

6. **Integrações**:
   - **Kafka**: Consome dos tópicos `proposal` e `quotation`
   - **Banco**: MariaDB (schema: `relatorio-mineradora`)

**Fluxo de Dados**:
```
Kafka (proposal) → MessageEvents → OpportunityService → 
Busca última cotação → Cria Opportunity → Salva no BD

Kafka (quotation) → MessageEvents → OpportunityService → 
Salva cotação no BD

Cliente → REST API → OpportunityService → 
Gera relatório (JSON ou CSV)
```

**Endpoints**:
- `GET /api/opportunity/data` - Lista oportunidades em JSON (roles: user, manager)

---

## 4. MÓDULO: gateway-bff-mineradora

### Tecnologias
- **Framework**: Quarkus 3.2.12.Final
- **Linguagem**: Java 17
- **Segurança**: Keycloak (OIDC) com Token Propagation
- **REST Client**: MicroProfile REST Client (Reactive)
- **Observabilidade**: OpenTelemetry (Jaeger)
- **Build Tool**: Maven
- **Bibliotecas**:
  - Lombok 1.18.30
  - Apache Commons CSV 1.8
  - SLF4J para logging

### Porta
- **8084**

### Informações de Negócio

**Propósito**: Backend for Frontend (BFF) que atua como gateway único para o frontend, agregando chamadas aos outros microserviços e fornecendo uma API unificada.

**Funcionalidades**:
1. **Gateway de Propostas**:
   - **Consultar Proposta**: Proxy para `proposta-mineradora`
   - **Criar Proposta**: Proxy para `proposta-mineradora`
   - **Remover Proposta**: Proxy para `proposta-mineradora`

2. **Gateway de Relatórios**:
   - **Relatório CSV**: Obtém dados de `relatorio-mineradora` e gera CSV
   - **Relatório JSON**: Obtém dados de `relatorio-mineradora` e retorna JSON

3. **Segurança**:
   - **Autenticação**: Obrigatória via Keycloak
   - **Token Propagation**: Propaga token JWT para os serviços backend
   - **Autorização por Role**:
     - `proposal-customer`: Pode criar propostas
     - `user`, `manager`: Podem consultar propostas e relatórios
     - `manager`: Pode remover propostas

4. **Integrações**:
   - **REST Client para proposta-mineradora** (porta 8082)
   - **REST Client para relatorio-mineradora** (porta 8083)

**Fluxo de Dados**:
```
Frontend → Gateway BFF (autenticado) → 
  → REST Client → proposta-mineradora
  → REST Client → relatorio-mineradora
  → Retorna resposta agregada ao Frontend
```

**Endpoints**:
- `GET /api/trade/{id}` - Consultar proposta
- `POST /api/trade` - Criar proposta (role: proposal-customer)
- `DELETE /api/trade/remove/{id}` - Remover proposta (role: manager)
- `GET /api/opportunity/report` - Download relatório CSV (roles: user, manager)
- `GET /api/opportunity/data` - Relatório JSON (roles: user, manager)

---

## 5. INFRAESTRUTURA E SERVIÇOS AUXILIARES

### Kafka (Docker Compose)
- **Zookeeper**: Porta 2181
- **Kafka**: Porta 9092
- **Tópicos**:
  - `quotation`: Cotações de moedas
  - `proposal`: Propostas comerciais

### Keycloak (Docker Compose)
- **Porta**: 7080 (HTTP), 7443 (HTTPS)
- **Realm**: `quarkus`
- **Client**: `backend-service`
- **Roles**:
  - `user`: Usuário comum
  - `manager`: Gerente/administrador
  - `proposal-customer`: Cliente que pode criar propostas
  - `admin`: Administrador do sistema

### Jaeger (Docker Compose)
- **Porta UI**: 16686
- **OTLP gRPC**: 4317
- **OTLP HTTP**: 4318
- **Propósito**: Rastreamento distribuído (OpenTelemetry)

---

## ARQUITETURA DO SISTEMA

```
┌─────────────┐
│   Frontend  │
└──────┬──────┘
       │
       ▼
┌─────────────────────────┐
│  gateway-bff-mineradora │ (Porta 8084)
│  (BFF/Gateway)          │
└──────┬──────────┬────────┘
       │          │
       │          │
       ▼          ▼
┌──────────────┐  ┌──────────────────┐
│proposta-     │  │relatorio-         │
│mineradora    │  │mineradora         │
│(Porta 8082)  │  │(Porta 8083)       │
└──────┬───────┘  └───────┬───────────┘
       │                  │
       │                  │
       └──────────┬────────┘
                  │
                  ▼
            ┌──────────┐
            │  Kafka   │
            └────┬─────┘
                 │
                 ▼
         ┌──────────────────┐
         │cotacao-mineradora │ (Porta 8081)
         │(Producer)         │
         └───────────────────┘
                 │
                 ▼
         ┌──────────────────┐
         │  API Externa      │
         │(AwesomeAPI)       │
         └───────────────────┘
```

## FLUXO DE DADOS COMPLETO

1. **Cotação**:
   - `cotacao-mineradora` busca cotação a cada 35s
   - Se preço aumentou, publica no Kafka (`quotation`)
   - `relatorio-mineradora` consome e salva cotação

2. **Proposta**:
   - Frontend → `gateway-bff-mineradora` → `proposta-mineradora`
   - `proposta-mineradora` salva e publica no Kafka (`proposal`)
   - `relatorio-mineradora` consome e cria oportunidade

3. **Relatório**:
   - Frontend → `gateway-bff-mineradora` → `relatorio-mineradora`
   - Retorna oportunidades (JSON ou CSV)

## BANCOS DE DADOS

1. **cotacao-mineradora** (MariaDB)
   - Tabela: `quotation`
   - Armazena histórico de cotações USD-BRL

2. **proposta-mineradora** (MariaDB)
   - Tabela: `proposal`
   - Armazena propostas comerciais

3. **relatorio-mineradora** (MariaDB)
   - Tabelas: `opportunity`, `quotation`
   - Armazena oportunidades de negócio e cotações recebidas

## SEGURANÇA

- **Autenticação**: Keycloak (OIDC)
- **Autorização**: Baseada em roles (RBAC)
- **Token Propagation**: JWT propagado entre serviços
- **Proteção de Endpoints**: Anotações `@Authenticated` e `@RolesAllowed`

## OBSERVABILIDADE

- **OpenTelemetry**: Rastreamento distribuído
- **Jaeger**: Visualização de traces
- **Logging**: SLF4J com formato estruturado incluindo traceId, spanId

## TECNOLOGIAS COMUNS

- **Quarkus 3.2.12.Final**: Framework reativo
- **Java 17**: Linguagem de programação
- **Maven**: Gerenciamento de dependências
- **Lombok**: Redução de boilerplate
- **Docker**: Containerização (Dockerfiles incluídos)
- **OpenTelemetry**: Observabilidade distribuída

