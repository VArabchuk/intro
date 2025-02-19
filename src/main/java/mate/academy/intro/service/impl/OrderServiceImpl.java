package mate.academy.intro.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.order.CreateOrderRequestDto;
import mate.academy.intro.dto.order.OrderDtoForUpdate;
import mate.academy.intro.dto.order.OrderItemResponseDto;
import mate.academy.intro.dto.order.OrderResponseDto;
import mate.academy.intro.exception.EmptyCartException;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.OrderItemMapper;
import mate.academy.intro.mapper.OrderMapper;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.OrderItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.order.OrderRepository;
import mate.academy.intro.repository.orderitem.OrderItemRepository;
import mate.academy.intro.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.intro.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto orderRequestDto, User user) {
        ShoppingCart cart = getShoppingCartByUser(user);
        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new EmptyCartException(
                    "Your shopping cart is empty. Add items before placing an order."
            );
        }
        Order order = createAndSaveOrder(orderRequestDto.getShippingAddress(), user, cartItems);
        cart.getCartItems().clear();
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> getOrders(User user, Pageable pageable) {
        return orderRepository.findAllByUserId(user.getId(), pageable).stream()
                .map(orderMapper::toDto).toList();
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, Long orderItemId, User user) {
        Order order = getUserOrderById(orderId, user);
        return orderItemMapper.toDto(orderItemRepository
                .findByIdAndOrderId(orderItemId, order.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find order item by id '"
                        + orderItemId
                        + "' for user with email '" + user.getEmail() + "'")));
    }

    @Override
    public void changeOrderStatus(Long orderId, OrderDtoForUpdate dtoForUpdate, User user) {
        Order order = getUserOrderById(orderId, user);
        order.setStatus(dtoForUpdate.getStatus());
        orderRepository.save(order);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItems(Long orderId, User user, Pageable pageable) {
        Order order = getUserOrderById(orderId, user);
        return orderItemRepository.findAllByOrder(order, pageable).stream()
                .map(orderItemMapper::toDto).toList();
    }

    private ShoppingCart getShoppingCartByUser(User user) {
        return shoppingCartRepository.findShoppingCartByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart for user with email '"
                        + user.getEmail()
                        + "' is empty"));
    }

    private Order createAndSaveOrder(String shippingAddress, User user, Set<CartItem> cartItems) {
        Order order = new Order()
                .setShippingAddress(shippingAddress)
                .setUser(user)
                .setStatus(Order.Status.PENDING)
                .setTotal(getTotalBalanceForCartItems(cartItems));
        Set<OrderItem> orderItems = createOrderItemsSet(cartItems, order);
        order.setOrderItems(orderItems);
        return orderRepository.save(order);
    }

    private Set<OrderItem> createOrderItemsSet(Set<CartItem> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem -> new OrderItem()
                        .setBook(cartItem.getBook())
                        .setQuantity(cartItem.getQuantity())
                        .setOrder(order)
                        .setPrice(cartItem.getBook().getPrice()))
                .collect(Collectors.toSet());
    }

    private BigDecimal getTotalBalanceForCartItems(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(orderItem -> orderItem.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order getUserOrderById(Long orderId, User user) {
        return orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order by id '"
                        + orderId
                        + "' for user with email '"
                        + user.getEmail() + "'"));
    }
}
