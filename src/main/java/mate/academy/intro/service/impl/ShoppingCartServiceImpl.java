package mate.academy.intro.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.cartitem.CartItemRequestDto;
import mate.academy.intro.dto.cartitem.CartItemRequestDtoForUpdate;
import mate.academy.intro.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.book.BookRepository;
import mate.academy.intro.repository.cartitem.CartItemRepository;
import mate.academy.intro.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public ShoppingCartResponseDto createCartItem(
            CartItemRequestDto cartItemRequestDto,
            User user
    ) {
        Long bookId = cartItemRequestDto.getBookId();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find book by Id: " + bookId));
        ShoppingCart shoppingCart = getShoppingCartByUserId(user.getId());
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
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCart(Long userId) {
        return shoppingCartMapper.toDto(getShoppingCartByUserId(userId));
    }

    @Override
    public void deleteCartItem(Long cartItemId, Long userId) {
        CartItem cartItem = findCartItemBelongsToUser(cartItemId, userId);
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateCartItem(
            Long cartItemId,
            CartItemRequestDtoForUpdate dtoForUpdate,
            Long userId
    ) {
        CartItem cartItem = findCartItemBelongsToUser(cartItemId, userId);
        cartItem.setQuantity(dtoForUpdate.quantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(getShoppingCartByUserId(userId));
    }

    @Override
    public void createShoppingCart(User newUser) {
        ShoppingCart shoppingCart = new ShoppingCart().setUser(newUser);
        shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart by user Id " + userId)
                );
    }

    private CartItem findCartItemBelongsToUser(
            Long cartItemId,
            Long userId
    ) {
        return cartItemRepository
                .findCartItemByIdAndShoppingCartId(cartItemId, userId)
                .orElseThrow(()
                        -> new EntityNotFoundException("Can't find cart item by Id "
                        + cartItemId
                        + " for user by Id "
                        + userId));
    }
}
