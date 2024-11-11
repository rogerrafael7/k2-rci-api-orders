package k2.rci.api.orders.infra.gateway.productsApi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import k2.rci.api.orders.domain.gateway.ProductGateway;
import k2.rci.api.orders.domain.gateway.dto.GetProductsByIdsResponse;
import k2.rci.api.orders.domain.gateway.dto.ProductModel;
import k2.rci.api.orders.infra.gateway.productsApi.dto.ProductAPIGetProductsByIdsRequest;
import k2.rci.api.orders.infra.gateway.productsApi.dto.ProductAPIGetProductsByIdsResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProductGatewayImpl implements ProductGateway {
    @Inject
    @RestClient
    ProductRestClient productRestClient;

    @Override
    public GetProductsByIdsResponse getProductsByIds(List<UUID> productIds) {
        var payload = new ProductAPIGetProductsByIdsRequest();
        payload.setProductIds(
                String.join(",", productIds.stream()
                        .map(UUID::toString)
                        .toList())
        );
        var result = productRestClient.getProductsByIds(payload);
        var response = remapToDomain(result);
        return response;
    }

    private static GetProductsByIdsResponse remapToDomain(ProductAPIGetProductsByIdsResponse result) {
        var response = new GetProductsByIdsResponse();
        response.setProductIdsNotFound(result.productIdsNotFound());
        var listProductModels = result.productsFound()
                .stream().map(product -> {
                    var productModel = new ProductModel();
                    productModel.setId(product.getId());
                    productModel.setPrice(product.getPrice());
                    return productModel;
                }).toList();
        response.setProductsFound(listProductModels);
        return response;
    }
}
