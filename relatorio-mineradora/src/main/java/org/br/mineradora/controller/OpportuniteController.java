package org.br.mineradora.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.service.OpportunityService;

import java.util.Date;

@Slf4j
@RequestScoped
@Path("/api/opportunity")
public class OpportuniteController {

    @Inject
    OpportunityService opportunityService;

    @GET
    @Path("/report")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response generateReport() {
        try {
            return Response
                    .ok(opportunityService.generateCSVOpportunityReport(), MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", String.format("attachment; filename=%s-opportunities-sales.txt", new Date()))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.serverError().build();
        }
    }

}
