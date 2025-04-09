package org.br.mineradora.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.dto.ProposalDetailDTO;
import org.br.mineradora.service.ProposalService;

@Slf4j
@RequestScoped
@Path("/api/proposal")
public class ProposalController {

    @Inject
    ProposalService proposalService;

    @GET
    @Path("/{id}")
    public ProposalDetailDTO findDetailsProposal(@PathParam("id") long id) {
        return proposalService.findFullProposal(id);
    }

    @POST
    public Response createProposal(ProposalDetailDTO proposalDetailDTO) {
        try {
            log.info("Creating proposal {}", proposalDetailDTO);
            proposalService.createAndSendProposal(proposalDetailDTO);
            return Response.ok().build();
        } catch (Exception e) {
            log.error("Error creating proposal {}", proposalDetailDTO, e);
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response removeProposal(@PathParam("id") long id) {
        try {
            log.info("Removing proposal {}", id);
            proposalService.removeProposal(id);
            return Response.ok().build();
        }catch (Exception e) {
            log.error("Error removing proposal {}", id, e);
            return Response.serverError().build();
        }
    }


}
