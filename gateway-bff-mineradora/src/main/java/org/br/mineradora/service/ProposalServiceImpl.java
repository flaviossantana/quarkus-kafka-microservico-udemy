package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.br.mineradora.client.ProposalRestClient;
import org.br.mineradora.dto.ProposalDetailDTO;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class ProposalServiceImpl implements ProposalService {

    @Inject
    @RestClient
    ProposalRestClient proposalRestClient;

    @Override
    public ProposalDetailDTO findProposalDetailById(long proposalId) {
        return proposalRestClient.findProposalDetailById(proposalId);
    }

    @Override
    public Response createProposal(ProposalDetailDTO proposalDetailDTO) {
        return proposalRestClient.createProposal(proposalDetailDTO);
    }

    @Override
    public Response removeProposal(long proposalId) {
        return proposalRestClient.removeProposal(proposalId);
    }
}
