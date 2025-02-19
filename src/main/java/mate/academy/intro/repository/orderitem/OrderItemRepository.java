package mate.academy.intro.repository.orderitem;

import java.util.List;
import java.util.Optional;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndOrderId(Long orderItemId, Long orderId);

    List<OrderItem> findAllByOrder(Order order, Pageable pageable);
}
