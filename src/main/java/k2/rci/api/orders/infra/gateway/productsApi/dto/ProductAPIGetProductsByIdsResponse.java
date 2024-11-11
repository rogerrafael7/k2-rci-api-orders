package k2.rci.api.orders.infra.gateway.productsApi.dto;

import java.util.List;
import java.util.UUID;

public record ProductAPIGetProductsByIdsResponse(
        List<ProductAPIProductModel> productsFound,
        List<UUID> productIdsNotFound
) {
}