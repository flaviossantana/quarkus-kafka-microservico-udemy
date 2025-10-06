package org.br.mineradora.controller;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.dto.OpportunityDTO;
import org.br.mineradora.exception.ReportException;
import org.br.mineradora.service.OpportunityService;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Slf4j
@RequestScoped
@Authenticated
@Path("/api/opportunity")
public class OpportunityController {

    @Inject
    OpportunityService opportunityService;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/data")
    @RolesAllowed({"user","manager"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<OpportunityDTO> generateReport() {
        try {
            return opportunityService.generateOpportunitesData();
        } catch (Exception e) {
            log.error("[OPPORTUNITYCONTROLLER][EXCEPTION]", e);
            throw new ReportException(e.getMessage());
        }
    }

}
