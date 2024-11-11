package k2.rci.api.orders.infra.gateway.productsApi.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductAPIGetProductsByIdsRequest {
    @QueryParam("productIds")
    private String productIds;
}
