package k2.rci.api.orders.domain.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import k2.rci.api.orders.domain.dto.SaveOrderItem;
import k2.rci.api.orders.domain.dto.SaveOrderRequest;
import k2.rci.api.orders.domain.gateway.ProductGateway;
import k2.rci.api.orders.domain.gateway.dto.GetProductsByIdsResponse;
import k2.rci.api.orders.domain.models.OrderItemModel;
import k2.rci.api.orders.domain.models.OrderModel;
import k2.rci.api.orders.domain.models.OrderStatusEnum;
import k2.rci.api.orders.domain.repositories.OrderRepository;
import k2.rci.api.orders.domain.repositories.dto.SaveOrderModelRequest;
import k2.rci.api.orders.infra.shared.exceptions.SERVER_EXCEPTION_CAUSE;
import k2.rci.api.orders.infra.shared.exceptions.ServerException;
import k2.rci.api.orders.infra.shared.types.PaginationRequest;
import k2.rci.api.orders.infra.shared.types.PaginationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderService {
    @Inject
    ProductGateway productGateway;

    @Inject
    OrderRepository orderRepository;

    public OrderModel createOrder(SaveOrderRequest payload) {
        List<OrderItemModel> populatedItems = populateItems(payload.getItems());
        var saveOrderModelRequest = new SaveOrderModelRequest();
        saveOrderModelRequest.setItems(populatedItems);
        saveOrderModelRequest.setCustomerId(payload.getCustomerId());
        return orderRepository.createOrder(saveOrderModelRequest);
    }

    public OrderModel getOrderByIdOrFail(UUID orderId) {
        return orderRepository.getOrderByIdOrFail(orderId);
    }

    public PaginationResponse<OrderModel> getAllOrders(PaginationRequest paginationRequest) {
        return orderRepository.getAllOrders(paginationRequest);
    }

    public OrderModel updateOrderItems(UUID orderId, SaveOrderRequest payload) {
        List<OrderItemModel> populatedItems = populateItems(payload.getItems());
        var saveOrderModelRequest = new SaveOrderModelRequest();
        saveOrderModelRequest.setItems(populatedItems);
        return orderRepository.updateOrderItems(orderId, saveOrderModelRequest);
    }

    public void deleteOrder(UUID orderId) {
        orderRepository.deleteOrder(orderId);
    }

    public void updateOrderStatus(UUID orderId, OrderStatusEnum status) {
        orderRepository.updateOrderStatus(orderId, status);
    }

    private List<OrderItemModel> populateItems(List<SaveOrderItem> items) {
        List<UUID> productIds = items.stream()
                .map(SaveOrderItem::getProductId)
                .toList();

        GetProductsByIdsResponse responseProducts = productGateway.getProductsByIds(productIds);

        if (!responseProducts.getProductIdsNotFound().isEmpty()) {
            throw new ServerException("Some products were not found", SERVER_EXCEPTION_CAUSE.NOT_FOUND);
        }
        List<OrderItemModel> populatedItems = new ArrayList<>();
        var productsFound = responseProducts.getProductsFound();

        for (var product : productsFound) {
            var orderItemModel = new OrderItemModel();
            orderItemModel.setProductId(product.getId());
            orderItemModel.setProductUnityValue(product.getPrice());
            var item = items.stream()
                    .filter(itemPayload -> itemPayload.getProductId().equals(product.getId()))
                    .findFirst()
                    .orElseThrow();
            orderItemModel.setQuantity(item.getQuantity());
            populatedItems.add(orderItemModel);
        }
        return populatedItems;
    }
}
