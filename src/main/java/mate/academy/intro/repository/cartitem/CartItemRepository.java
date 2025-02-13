package mate.academy.intro.repository.cartitem;

import java.util.Optional;
import mate.academy.intro.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findCartItemByIdAndShoppingCartId(
            Long id,
            Long shoppingCardId);

    @Modifying
    @Query("UPDATE CartItem c SET c.isDeleted = true WHERE c.shoppingCart.id = :userId")
    void deleteByShoppingCartAndId(Long userId);
}
