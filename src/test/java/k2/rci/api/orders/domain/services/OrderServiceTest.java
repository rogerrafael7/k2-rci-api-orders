package k2.rci.api.orders.domain.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import k2.rci.api.orders.domain.dto.SaveOrderItem;
import k2.rci.api.orders.domain.dto.SaveOrderRequest;
import k2.rci.api.orders.domain.gateway.ProductGateway;
import k2.rci.api.orders.domain.gateway.dto.GetProductsByIdsResponse;
import k2.rci.api.orders.domain.gateway.dto.ProductModel;
import k2.rci.api.orders.domain.models.OrderModel;
import k2.rci.api.orders.domain.repositories.OrderRepository;
import k2.rci.api.orders.domain.repositories.dto.SaveOrderModelRequest;
import k2.rci.api.orders.infra.shared.types.DefaultPaginationRequest;
import k2.rci.api.orders.infra.shared.types.PaginationRequest;
import k2.rci.api.orders.infra.shared.types.PaginationResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@QuarkusTest
public class OrderServiceTest {

    @Inject
    OrderService orderService;

    @InjectMock
    ProductGateway productGateway;

    @InjectMock
    OrderRepository orderRepository;

    @Inject
    ObjectMapper objectMapper;

    private static String loadJson(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(STR."src/test/resources/\{path}")));
    }

    @Test
    public void testCreateOrder() throws IOException {
        String jsonContent = loadJson("mocks/products.json");
        PaginationResponse<HashMap> mockResponse = objectMapper.readValue(jsonContent, PaginationResponse.class);

        PaginationRequest paginationRequest = new DefaultPaginationRequest();
        paginationRequest.setPage(1);
        paginationRequest.setLimit(10);

        SaveOrderRequest request = new SaveOrderRequest();
        request.setCustomerId(UUID.randomUUID());

        List<SaveOrderItem> saveOrderItems = new ArrayList<>();
        GetProductsByIdsResponse productsResponse = new GetProductsByIdsResponse();
        productsResponse.setProductsFound(
                mockResponse.data().stream()
                        .map(product -> {
                            var productModel = new ProductModel();
                            productModel.setId(UUID.fromString((String) product.get("id")));
                            productModel.setPrice((Double) product.get("price"));

                            var item = new SaveOrderItem();
                            item.setProductId(UUID.fromString((String) product.get("id")));
                            item.setQuantity(2);
                            saveOrderItems.add(item);

                            return productModel;
                        })
                        .toList()
        );
        request.setItems(saveOrderItems);
        productsResponse.setProductIdsNotFound(List.of());

        List<UUID> productIds = request.getItems().stream()
                .map(SaveOrderItem::getProductId)
                .toList();

        when(productGateway.getProductsByIds(productIds)).thenReturn(productsResponse);
        OrderModel mockOrder = new OrderModel();
        when(orderRepository.createOrder(any(SaveOrderModelRequest.class))).thenReturn(mockOrder);

        orderService.createOrder(request);

        verify(orderRepository, times(1)).createOrder(any(SaveOrderModelRequest.class));
    }
}