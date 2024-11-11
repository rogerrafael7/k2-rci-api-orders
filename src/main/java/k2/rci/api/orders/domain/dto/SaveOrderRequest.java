package k2.rci.api.orders.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SaveOrderRequest {
    private UUID customerId;
    private List<SaveOrderItem> items;
}
