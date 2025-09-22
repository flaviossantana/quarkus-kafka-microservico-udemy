package org.br.mineradora.scheduler;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.service.QuotationService;

@Slf4j
@ApplicationScoped
public class QuotationScheduler {

    @Inject
    QuotationService quotationService;

    @Transactional(Transactional.TxType.REQUIRED)
    @Scheduled(every = "35s", identity = "task-quotation-scheduler")
    public void schedule() {

        log.info("INICIANDO A EXECUÇÃO DE BUSCA DE COTAÇÃO USD-BRL");

        quotationService.getCurrencyPrice();

        log.info("FINALIZANDO A EXECUÇÃO DE BUSCA DE COTAÇÃO USD-BRL");
    }


}
