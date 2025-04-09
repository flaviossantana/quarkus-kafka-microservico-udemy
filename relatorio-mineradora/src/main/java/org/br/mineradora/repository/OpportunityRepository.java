package org.br.mineradora.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.entity.OpportunityEntity;

@Slf4j
@ApplicationScoped
public class OpportunityRepository implements PanacheRepository<OpportunityEntity> {
}
