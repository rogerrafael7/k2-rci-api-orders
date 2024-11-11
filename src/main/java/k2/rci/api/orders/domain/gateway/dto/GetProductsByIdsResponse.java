package k2.rci.api.orders.domain.gateway.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetProductsByIdsResponse {
    private List<ProductModel> productsFound;
    private List<UUID> productIdsNotFound;
}
