package mate.academy.intro.service;

import mate.academy.intro.dto.cartitem.CartItemRequestDto;
import mate.academy.intro.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.intro.model.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    ShoppingCartResponseDto getShoppingCart(Long userId);

    ShoppingCartResponseDto addNewCartItemToShoppingCart(
            CartItemRequestDto cartItemRequestDto, Long userId
    );
}
