package mate.academy.intro.service;

import java.util.List;
import mate.academy.intro.dto.order.CreateOrderRequestDto;
import mate.academy.intro.dto.order.OrderDtoForUpdate;
import mate.academy.intro.dto.order.OrderItemResponseDto;
import mate.academy.intro.dto.order.OrderResponseDto;
import mate.academy.intro.model.User;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto createOrder(CreateOrderRequestDto orderRequestDto, User user);

    List<OrderResponseDto> getOrders(User user, Pageable pageable);

    OrderItemResponseDto getOrderItem(Long orderId, Long orderItemId, User user);

    void changeOrderStatus(Long orderId, OrderDtoForUpdate dtoForUpdate, User user);

    List<OrderItemResponseDto> getOrderItems(Long orderId, User user, Pageable pageable);
}
