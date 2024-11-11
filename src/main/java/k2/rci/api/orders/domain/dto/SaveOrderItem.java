package k2.rci.api.orders.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SaveOrderItem{
    private UUID productId;
    private Integer quantity;
}
