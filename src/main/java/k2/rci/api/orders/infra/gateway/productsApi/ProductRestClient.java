package k2.rci.api.orders.infra.gateway.productsApi;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import k2.rci.api.orders.infra.gateway.productsApi.dto.ProductAPIGetProductsByIdsRequest;
import k2.rci.api.orders.infra.gateway.productsApi.dto.ProductAPIGetProductsByIdsResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/products")
@RegisterRestClient
public interface ProductRestClient {

    @GET
    @Path("/by-ids")
    public ProductAPIGetProductsByIdsResponse getProductsByIds(ProductAPIGetProductsByIdsRequest productIds);
}