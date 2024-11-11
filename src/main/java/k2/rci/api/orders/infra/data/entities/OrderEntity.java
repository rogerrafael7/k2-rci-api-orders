package k2.rci.api.orders.infra.data.entities;

import jakarta.persistence.*;
import k2.rci.api.orders.domain.models.OrderStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "status", columnDefinition = "order_status")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private OrderStatusEnum status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItemEntity> items;
}
