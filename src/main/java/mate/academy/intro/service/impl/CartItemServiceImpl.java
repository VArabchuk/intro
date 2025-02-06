package mate.academy.intro.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.cartitem.CartItemRequestDto;
import mate.academy.intro.dto.cartitem.CartItemRequestDtoForUpdate;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.repository.book.BookRepository;
import mate.academy.intro.repository.cartitem.CartItemRepository;
import mate.academy.intro.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.intro.service.CartItemService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public CartItem createCartItem(CartItemRequestDto cartItemRequestDto, Long userId) {
        Long bookId = cartItemRequestDto.getBookId();

        Book book = bookRepository.getBookById(bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find book by Id: " + bookId));

        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);

        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findAny()
                .orElseGet(() -> {
                    CartItem item = new CartItem();
                    shoppingCart.getCartItems().add(item);
                    return item;
                });

        cartItem
                .setShoppingCart(shoppingCart)
                .setBook(book)
                .setQuantity(cartItem.getQuantity() + cartItemRequestDto.getQuantity());

        return cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteCartItem(Long id, Long userId) {
        getShoppingCartByUserId(userId)
                .getCartItems().stream()
                .filter(item -> item.getId().equals(id))
                .findAny()
                .orElseThrow(()
                        -> new EntityNotFoundException("Can't find cart item by Id "
                        + id
                        + " for user by Id "
                        + userId));
        cartItemRepository.deleteById(id);
    }

    @Override
    public CartItem updateCartItem(
            Long id,
            CartItemRequestDtoForUpdate dtoForUpdate,
            Long userId
    ) {
        CartItem cartItem = getShoppingCartByUserId(userId).getCartItems().stream()
                .filter(item -> item.getId().equals(id))
                .findAny()
                .orElseThrow(()
                        -> new EntityNotFoundException("Can't find cart item by Id "
                        + id
                        + " for user by Id "
                        + userId))
                .setQuantity(dtoForUpdate.quantity());
        return cartItemRepository.save(cartItem);
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart by user Id " + userId)
                );
    }
}
