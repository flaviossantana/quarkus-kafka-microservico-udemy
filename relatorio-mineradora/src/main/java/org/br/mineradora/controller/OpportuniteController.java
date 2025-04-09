package org.br.mineradora.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@Path("/api/opportunity")
public class OpportuniteController {

    @GET
    @Path("/{id}")
    public Response find(@PathParam("id") long id) {
        return Response.ok(id).build();
    }

}
