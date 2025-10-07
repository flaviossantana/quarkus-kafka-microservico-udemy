package org.br.mineradora.service;

import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.br.mineradora.dto.ProposalDetailDTO;


public interface ProposalService {
    ProposalDetailDTO findProposalDetailById(@PathParam("id") long proposalId);
    Response createProposal(ProposalDetailDTO proposalDetailDTO);
    Response removeProposal(long proposalId);
}
