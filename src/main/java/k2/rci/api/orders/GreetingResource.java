package k2.rci.api.orders;

import jakarta.inject.Inject;
import jakarta.resource.spi.ConfigProperty;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import k2.rci.api.orders.infra.shared.config.AppConfig;

@Path("/health")
public class GreetingResource {

    @Inject
    AppConfig appConfig;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        System.out.println("apiProductsUrl: " + appConfig.getApiProductUrl());
        return "Hello from Quarkus REST";
    }
}
