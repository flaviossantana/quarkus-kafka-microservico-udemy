package org.br.mineradora.client;

import io.quarkus.oidc.token.propagation.reactive.AccessTokenRequestReactiveFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.br.mineradora.dto.OpportunityDTO;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@ApplicationScoped
@RegisterRestClient
@Path("/api/opportunity")
@RegisterProvider(AccessTokenRequestReactiveFilter.class)
public interface ReportClient {

    @GET
    @Path("/data")
    List<OpportunityDTO> generateDataReport();

}
