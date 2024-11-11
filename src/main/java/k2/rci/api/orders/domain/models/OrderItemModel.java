package k2.rci.api.orders.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderItemModel {
    private Integer id;
    private UUID orderId;
    private UUID productId;
    private Double productUnityValue;
    private Integer quantity;
}
