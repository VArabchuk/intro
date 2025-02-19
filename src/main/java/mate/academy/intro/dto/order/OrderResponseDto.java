package mate.academy.intro.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import mate.academy.intro.model.Order;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Set<OrderItemResponseDto> orderItems;
    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Order.Status status;
}
