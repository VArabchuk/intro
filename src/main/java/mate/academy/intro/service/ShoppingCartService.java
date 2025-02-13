package mate.academy.intro.service;

import mate.academy.intro.dto.cartitem.CartItemRequestDto;
import mate.academy.intro.dto.cartitem.CartItemRequestDtoForUpdate;
import mate.academy.intro.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.intro.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto createCartItem(CartItemRequestDto cartItemRequestDto, User user);

    ShoppingCartResponseDto getShoppingCart(Long userId);

    void deleteCartItem(Long cartItemId, Long userId);

    ShoppingCartResponseDto updateCartItem(
            Long cartItemId,
            CartItemRequestDtoForUpdate dtoForUpdate,
            Long userId
    );

    void createShoppingCart(User newUser);
}
