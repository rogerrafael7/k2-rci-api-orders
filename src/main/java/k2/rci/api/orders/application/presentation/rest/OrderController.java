package k2.rci.api.orders.application.presentation.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import k2.rci.api.orders.domain.dto.SaveOrderRequest;
import k2.rci.api.orders.domain.dto.UpdateOrderStatusRequest;
import k2.rci.api.orders.domain.models.OrderModel;
import k2.rci.api.orders.domain.services.OrderService;
import k2.rci.api.orders.infra.shared.types.DefaultPaginationRequest;
import k2.rci.api.orders.infra.shared.types.PaginationResponse;

import java.util.UUID;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    OrderService orderService;

    @GET
    public PaginationResponse<OrderModel> getAllOrders(
            @BeanParam
            DefaultPaginationRequest paginationParams
    ) {
        return orderService.getAllOrders(paginationParams);
    }

    @GET
    @Path("/{id}")
    public OrderModel getOrderById(@PathParam("id") UUID id) {
        return orderService.getOrderByIdOrFail(id);
    }

    @POST
    public OrderModel createOrder(SaveOrderRequest request) {
        return orderService.createOrder(request);
    }

    @DELETE
    @Path("/{id}")
    public void deleteOrder(@PathParam("id") UUID id) {
        orderService.deleteOrder(id);
    }

    @PATCH
    @Path("/{id}/items")
    public OrderModel updateOrder(@PathParam("id") UUID id, SaveOrderRequest request) {
        return orderService.updateOrderItems(id, request);
    }

    @PATCH
    @Path("/{id}/status")
    public void updateOrderStatus(@PathParam("id") UUID id, UpdateOrderStatusRequest request) {
        orderService.updateOrderStatus(id, request.status());
    }

}
