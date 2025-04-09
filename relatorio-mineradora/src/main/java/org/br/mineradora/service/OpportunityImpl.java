package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.dto.OpportunityDTO;
import org.br.mineradora.dto.ProposalDTO;
import org.br.mineradora.dto.QuotationDTO;
import org.br.mineradora.entity.OpportunityEntity;
import org.br.mineradora.entity.QuotationEntity;
import org.br.mineradora.repository.OpportunityRepository;
import org.br.mineradora.repository.QuotationRepository;
import org.br.mineradora.utils.CSVHelper;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@ApplicationScoped
public class OpportunityImpl implements OpportunityService {

    @Inject
    OpportunityRepository opportunityRepository;

    @Inject
    QuotationRepository quotationRepository;

    @Override
    public void buildOpportunity(ProposalDTO proposalDTO) {

        List<QuotationEntity> quotations = quotationRepository.findAll().list();
        Collections.reverse(quotations);

        opportunityRepository.persist(
                OpportunityEntity
                        .builder()
                        .data(new Date())
                        .proposalId(proposalDTO.getProposalId())
                        .costumer(proposalDTO.getCostumer())
                        .priceTonne(proposalDTO.getPriceTonne())
                        .lastDollarQuotation(quotations.stream().findFirst().orElseThrow().getCurrencyPrice())
                        .build()
        );

    }

    @Override
    public void saveQuotation(QuotationDTO quotationDTO) {
        log.info("Salvando quotation: {}", quotationDTO);
        quotationRepository.persist(
                QuotationEntity
                        .builder()
                        .data(new Date())
                        .currencyPrice(quotationDTO.getCurrencyPrice())
                        .build());
    }

    @Override
    public List<OpportunityDTO> generateOpportunitesData() {
        return List.of();
    }

    @Override
    public ByteArrayInputStream generateCSVOpportunityReport() {

        List<OpportunityDTO> opportunities = opportunityRepository
                .findAll()
                .list()
                .parallelStream()
                .map(o -> OpportunityDTO
                        .builder()
                        .proposalId(o.getProposalId())
                        .costumer(o.getCostumer())
                        .priceTonne(o.getPriceTonne())
                        .lastDollarQuotation(o.getLastDollarQuotation())
                        .build())
                .toList();

        return CSVHelper.opportunitiesCSV(opportunities);
    }
}
