package k2.rci.api.orders.domain.gateway.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductModel {
    private UUID id;
    private Double price;
}
