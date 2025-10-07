package org.br.mineradora.client;

import io.quarkus.oidc.token.propagation.reactive.AccessTokenRequestReactiveFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.br.mineradora.dto.ProposalDetailDTO;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@Path("/api/proposal")
@RegisterRestClient(configKey = "proposal-client-api")
@RegisterProvider(AccessTokenRequestReactiveFilter.class)
public interface ProposalClient {

    @GET
    @Path("/{id}")
    ProposalDetailDTO findProposalDetailById(@PathParam("id") long proposalId);

    @POST
    Response createProposal(ProposalDetailDTO proposalDetailDTO);

    @DELETE
    @Path("/{id}")
    Response removeProposal(@PathParam("id") long id);

}
