package k2.rci.api.orders.infra.gateway.productsApi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductAPIProductModel {
    private UUID id;
    private String name;
    private Double price;
    private String description;
    private String type;
}