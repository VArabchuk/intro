package mate.academy.intro.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import mate.academy.intro.model.Order;

@Getter
@Setter
public class OrderDtoForUpdate {
    @NotNull
    private Order.Status status;
}
