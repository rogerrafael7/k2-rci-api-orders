package k2.rci.api.orders.domain.repositories;

import k2.rci.api.orders.domain.models.OrderModel;
import k2.rci.api.orders.domain.models.OrderStatusEnum;
import k2.rci.api.orders.domain.repositories.dto.SaveOrderModelRequest;
import k2.rci.api.orders.infra.shared.types.PaginationRequest;
import k2.rci.api.orders.infra.shared.types.PaginationResponse;

import java.util.UUID;

public abstract class OrderRepository {
    public abstract OrderModel createOrder(SaveOrderModelRequest payload);

    public abstract OrderModel getOrderByIdOrFail(UUID orderId);

    public abstract PaginationResponse<OrderModel> getAllOrders(PaginationRequest paginationRequest);

    public abstract OrderModel updateOrderItems(UUID orderId, SaveOrderModelRequest payload);

    public abstract void deleteOrder(UUID orderId);

    public abstract void updateOrderStatus(UUID orderId, OrderStatusEnum status);
}
