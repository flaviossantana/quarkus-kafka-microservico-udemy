package org.br.mineradora.service;


import org.br.mineradora.dto.OpportunityDTO;
import org.br.mineradora.dto.ProposalDTO;
import org.br.mineradora.dto.QuotationDTO;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface OpportunityService {

    void buildOpportunity(ProposalDTO proposalDTO);

    void saveQuotation(QuotationDTO quotationDTO);

    List<OpportunityDTO> generateOpportunitesData();

    ByteArrayInputStream generateCSVOpportunityReport();

}
