package mate.academy.intro.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.cartitem.CartItemRequestDto;
import mate.academy.intro.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.intro.service.CartItemService;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemService cartItemService;

    @Override
    public void createShoppingCart(User user) {
        shoppingCartRepository.save(new ShoppingCart().setUser(user));
    }

    public ShoppingCartResponseDto addNewCartItemToShoppingCart(
            CartItemRequestDto cartItemRequestDto, Long userId
    ) {
        CartItem cartItem = cartItemService.createCartItem(cartItemRequestDto, userId);
        ShoppingCart shoppingCart
                = cartItem.getShoppingCart();

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart by user Id " + userId)
                );
        return shoppingCartMapper.toDto(shoppingCart);
    }
}
