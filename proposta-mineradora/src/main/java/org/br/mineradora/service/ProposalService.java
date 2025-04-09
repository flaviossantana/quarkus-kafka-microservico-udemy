package org.br.mineradora.service;

import org.br.mineradora.dto.ProposalDetailDTO;


public interface ProposalService {

    ProposalDetailDTO findFullProposal(Long id);

    void createAndSendProposal(ProposalDetailDTO proposalDetailDTO);

    void removeProposal(Long id);

}
