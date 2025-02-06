package mate.academy.intro.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequestDto {
    @NotNull
    @Min(1)
    private Long bookId;
    @Min(1)
    private int quantity;
}
