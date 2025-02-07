package mate.academy.intro.dto.cartitem;

import jakarta.validation.constraints.Positive;

public record CartItemRequestDtoForUpdate(
        @Positive
        int quantity
) {
}
