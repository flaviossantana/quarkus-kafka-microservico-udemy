package org.br.mineradora.message;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.dto.ProposalDTO;
import org.br.mineradora.dto.QuotationDTO;
import org.br.mineradora.service.OpportunityService;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@Slf4j
@ApplicationScoped
public class MessageEvents {

    @Inject
    OpportunityService opportunityService;

    @Transactional
    @Incoming("proposal-receive")
    public void receivePropostal(ProposalDTO proposal) {
        log.info("Proposal received: {}", proposal);
        opportunityService.buildOpportunity(proposal);
    }

    @Transactional
    @Incoming("quotation-receive")
    public void receiveQuotationl(QuotationDTO quotation) {
        log.info("Quotation received: {}", quotation);
        opportunityService.saveQuotation(quotation);
    }


}
