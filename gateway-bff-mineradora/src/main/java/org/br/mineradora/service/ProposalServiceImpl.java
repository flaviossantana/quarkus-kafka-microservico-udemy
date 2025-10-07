package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.br.mineradora.client.ProposalClient;
import org.br.mineradora.dto.ProposalDetailDTO;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class ProposalServiceImpl implements ProposalService {

    @Inject
    @RestClient
    ProposalClient proposalClient;

    @Override
    public ProposalDetailDTO findProposalDetailById(long proposalId) {
        return proposalClient.findProposalDetailById(proposalId);
    }

    @Override
    public Response createProposal(ProposalDetailDTO proposalDetailDTO) {
        return proposalClient.createProposal(proposalDetailDTO);
    }

    @Override
    public Response removeProposal(long proposalId) {
        return proposalClient.removeProposal(proposalId);
    }
}
