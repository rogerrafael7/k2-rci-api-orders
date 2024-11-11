package k2.rci.api.orders.infra.data.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import k2.rci.api.orders.domain.models.OrderItemModel;
import k2.rci.api.orders.domain.models.OrderModel;
import k2.rci.api.orders.domain.models.OrderStatusEnum;
import k2.rci.api.orders.domain.repositories.OrderRepository;
import k2.rci.api.orders.domain.repositories.dto.SaveOrderModelRequest;
import k2.rci.api.orders.infra.data.entities.OrderEntity;
import k2.rci.api.orders.infra.data.entities.OrderItemEntity;
import k2.rci.api.orders.infra.shared.exceptions.SERVER_EXCEPTION_CAUSE;
import k2.rci.api.orders.infra.shared.exceptions.ServerException;
import k2.rci.api.orders.infra.shared.types.PaginationRequest;
import k2.rci.api.orders.infra.shared.types.PaginationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderRepositoryImpl extends OrderRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    @Override
    public OrderModel createOrder(SaveOrderModelRequest payload) {
        OrderEntity order = new OrderEntity();
        order.setCustomerId(payload.getCustomerId());
        order.setStatus(OrderStatusEnum.PENDING);
        prepareItemsToSave(order, payload);
        entityManager.persist(order);
        return remapToOrderModel(order);
    }

    @Override
    public OrderModel getOrderByIdOrFail(UUID orderId) {
        var order = getOrderEntityByIdOrFail(orderId);
        return remapToOrderModel(order);
    }

    @Override
    public PaginationResponse<OrderModel> getAllOrders(PaginationRequest paginationRequest) {
        var query = entityManager.createQuery("SELECT p FROM orders p", OrderEntity.class);
        var page = paginationRequest.getPage();
        var limit = paginationRequest.getLimit();
        int offset = (page - 1) * limit;

        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<OrderEntity> orders = query.getResultList();

        Long count = entityManager.createQuery("SELECT COUNT(p) FROM orders p", Long.class).getSingleResult();

        var totalItems = count.intValue();
        var totalPages = (int) Math.ceil((double) totalItems / limit);
        var hasNextPage = page < totalPages;
        var hasPreviousPage = page > 1;
        var ordersModel = new ArrayList<OrderModel>();
        orders.forEach(order -> ordersModel.add(remapToOrderModel(order)));

        return new PaginationResponse<>(
                page,
                limit,
                totalItems,
                totalPages,
                hasNextPage,
                hasPreviousPage,
                ordersModel
        );
    }

    @Transactional
    @Override
    public OrderModel updateOrderItems(UUID orderId, SaveOrderModelRequest payload) {
        var order = getOrderEntityByIdOrFail(orderId);
        if (order.getStatus() != OrderStatusEnum.PENDING) {
            throw new ServerException("Order status is not PENDING, items cannot be changed after order is in progress", SERVER_EXCEPTION_CAUSE.BAD_REQUEST);
        }
        var itemsToRemove = order.getItems();
        itemsToRemove.forEach(entityManager::remove);
        prepareItemsToSave(order, payload);
        entityManager.persist(order);
        return remapToOrderModel(order);
    }

    @Transactional
    @Override
    public void deleteOrder(UUID orderId) {
        var order = getOrderEntityByIdOrFail(orderId);
        entityManager.remove(order);
    }

    @Transactional
    @Override
    public void updateOrderStatus(UUID orderId, OrderStatusEnum status) {
        var order = getOrderEntityByIdOrFail(orderId);
        order.setStatus(OrderStatusEnum.valueOf(status.name()));
        entityManager.persist(order);
    }

    private void prepareItemsToSave(OrderEntity order, SaveOrderModelRequest payload) {
        List<OrderItemEntity> items = new ArrayList<>();
        for (var item : payload.getItems()) {
            var orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setProductId(item.getProductId());
            orderItem.setProductUnityValue(item.getProductUnityValue());
            orderItem.setQuantity(item.getQuantity());
            items.add(orderItem);
        }
        order.setItems(items);
    }

    private OrderEntity getOrderEntityByIdOrFail(UUID orderId) {
        OrderEntity order = entityManager.find(OrderEntity.class, orderId);
        if (order == null) {
            throw new ServerException("Order not found", SERVER_EXCEPTION_CAUSE.BAD_REQUEST);
        }
        return order;
    }

    private OrderModel remapToOrderModel(OrderEntity orderEntity) {
        OrderModel orderModel = new OrderModel();
        orderModel.setId(orderEntity.getId());
        orderModel.setCustomerId(orderEntity.getCustomerId());
        orderModel.setStatus(orderEntity.getStatus());
        double totalPrice = 0.0;
        List<OrderItemModel> items = new ArrayList<>();
        for (var item : orderEntity.getItems()) {
            totalPrice += item.getProductUnityValue() * item.getQuantity();
            items.add(remapToOrderItemModel(item));
        }
        orderModel.setTotalPrice(totalPrice);
        orderModel.setItems(items);
        return orderModel;
    }

    private OrderItemModel remapToOrderItemModel(OrderItemEntity orderItemEntity) {
        OrderItemModel orderItemModel = new OrderItemModel();
        orderItemModel.setId(orderItemEntity.getId());
        orderItemModel.setProductId(orderItemEntity.getProductId());
        orderItemModel.setProductUnityValue(orderItemEntity.getProductUnityValue());
        orderItemModel.setQuantity(orderItemEntity.getQuantity());
        return orderItemModel;
    }
}
