package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.br.mineradora.dto.ProposalDetailDTO;

@ApplicationScoped
public class ProposalServiceImpl implements ProposalService {

    @Override
    public ProposalDetailDTO findProposalDetailById(long proposalId) {
        return null;
    }

    @Override
    public Response createProposal(ProposalDetailDTO proposalDetailDTO) {
        return null;
    }

    @Override
    public Response deleteProposal(long proposalId) {
        return null;
    }
}
