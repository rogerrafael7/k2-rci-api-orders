package k2.rci.api.orders.domain.gateway;

import k2.rci.api.orders.domain.gateway.dto.GetProductsByIdsResponse;

import java.util.List;
import java.util.UUID;

public interface ProductGateway {
    GetProductsByIdsResponse getProductsByIds(List<UUID> productIds);
}
