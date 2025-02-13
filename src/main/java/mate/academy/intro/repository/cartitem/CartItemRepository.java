package mate.academy.intro.repository.cartitem;

import java.util.Optional;
import mate.academy.intro.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findCartItemByIdAndShoppingCartId(
            Long id,
            Long shoppingCardId);
}
