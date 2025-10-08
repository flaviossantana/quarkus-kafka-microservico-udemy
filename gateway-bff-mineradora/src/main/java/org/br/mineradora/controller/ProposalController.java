package org.br.mineradora.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.dto.ProposalDetailDTO;
import org.br.mineradora.service.ProposalService;

import static org.jboss.resteasy.reactive.RestResponse.StatusCode.OK;

@Slf4j
@RequestScoped
@Path("/api/trade")
public class ProposalController {

    @Inject
    ProposalService proposalService;

    @GET
    @Path("/{id}")
    @RolesAllowed({"user", "manager"})
    public Response findProposalDetailById(@PathParam("id") long id) {
        try {
            return Response
                    .ok(proposalService.findProposalDetailById(id), MediaType.APPLICATION_JSON)
                    .build();
        } catch (ServerErrorException see) {
            return Response.serverError().build();
        }
    }


    @POST
    @RolesAllowed("proposal-customer")
    public Response createProposal(ProposalDetailDTO proposalDetailDTO) {
        log.info("Creating proposal {}", proposalDetailDTO);
        try (Response response = proposalService.createProposal(proposalDetailDTO)) {
            int status = response.getStatus();
            return status == OK ?
                    Response.ok().build() :
                    Response.status(status).build();
        } catch (Exception e) {
            log.error("Error creating proposal {}", proposalDetailDTO, e);
            return Response.serverError().build();
        }
    }


    @DELETE
    @Path("/remove/{id}")
    @RolesAllowed("manager")
    public Response removeProposal(@PathParam("id") long id) {
        log.info("Removing proposal {}", id);
        try (Response response = proposalService.removeProposal(id)) {
            int status = response.getStatus();
            return status == OK ?
                    Response.ok().build() :
                    Response.status(status).build();
        }catch (Exception e) {
            log.error("Error removing proposal {}", id, e);
            return Response.serverError().build();
        }
    }

}
