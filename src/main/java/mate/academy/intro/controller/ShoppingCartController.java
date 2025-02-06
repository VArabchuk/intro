package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.cartitem.CartItemRequestDto;
import mate.academy.intro.dto.cartitem.CartItemRequestDtoForUpdate;
import mate.academy.intro.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.User;
import mate.academy.intro.service.CartItemService;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Shopping Cart Management",
        description = "Endpoints for managing shopping cart and cart items")
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;
    private final ShoppingCartMapper shoppingCartMapper;

    @GetMapping
    @Operation(summary = "Get user's shopping cart",
            description = "Retrieves the shopping cart of the authenticated user")
    public ShoppingCartResponseDto getUsersShoppingCart(@AuthenticationPrincipal User user) {
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @PostMapping
    @Operation(summary = "Add an item to the cart",
            description = "Adds a new item to the authenticated user's shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartResponseDto createCartItem(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto,
            @AuthenticationPrincipal User user
    ) {
        return shoppingCartMapper.toDto(
                cartItemService.createCartItem(cartItemRequestDto, user.getId())
                        .getShoppingCart()
        );
    }

    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove an item from the cart",
            description = "Deletes an item from the authenticated user's shopping cart by its ID")
    public void deleteCartItem(@PathVariable Long cartItemId, @AuthenticationPrincipal User user) {
        cartItemService.deleteCartItem(cartItemId, user.getId());
    }

    @PutMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an item in the cart",
            description = "Updates the quantity of a cart item "
                    + "in the authenticated user's shopping cart")
    public ShoppingCartResponseDto updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemRequestDtoForUpdate dtoForUpdate,
            @AuthenticationPrincipal User user
    ) {
        return shoppingCartMapper.toDto(
                cartItemService.updateCartItem(cartItemId, dtoForUpdate, user.getId())
                        .getShoppingCart());
    }
}
