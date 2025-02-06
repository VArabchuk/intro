package mate.academy.intro.service;

import mate.academy.intro.dto.cartitem.CartItemRequestDto;
import mate.academy.intro.dto.cartitem.CartItemRequestDtoForUpdate;
import mate.academy.intro.model.CartItem;

public interface CartItemService {

    CartItem createCartItem(CartItemRequestDto cartItemRequestDto, Long userId);

    void deleteCartItem(Long id, Long userId);

    CartItem updateCartItem(
            Long id,
            CartItemRequestDtoForUpdate dtoForUpdate,
            Long userId
    );
}
