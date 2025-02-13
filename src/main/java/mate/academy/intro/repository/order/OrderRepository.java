package mate.academy.intro.repository.order;

import java.util.List;
import java.util.Optional;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserId(Long userId, Pageable pageable);

    Optional<Order> findByIdAndUser(Long orderId, User user);
}
