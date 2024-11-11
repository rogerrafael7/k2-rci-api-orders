package k2.rci.api.orders.domain.repositories.dto;

import k2.rci.api.orders.domain.models.OrderItemModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SaveOrderModelRequest {
    private UUID customerId;
    private List<OrderItemModel> items;
}
