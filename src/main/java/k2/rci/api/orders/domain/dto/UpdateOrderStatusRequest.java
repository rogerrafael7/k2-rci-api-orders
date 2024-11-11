package k2.rci.api.orders.domain.dto;

import k2.rci.api.orders.domain.models.OrderStatusEnum;

public record UpdateOrderStatusRequest(
        OrderStatusEnum status
) {
}
