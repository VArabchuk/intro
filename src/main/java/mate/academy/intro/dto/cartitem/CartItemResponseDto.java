package mate.academy.intro.dto.cartitem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponseDto {
    private Long id;
    private Long bookId;
    private String title;
    private int quantity;
}
