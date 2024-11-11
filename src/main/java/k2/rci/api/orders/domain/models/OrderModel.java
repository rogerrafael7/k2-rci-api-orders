package k2.rci.api.orders.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderModel {
    private UUID id;
    private UUID customerId;
    private Double totalPrice;
    private OrderStatusEnum status;
    private List<OrderItemModel> items;
}
