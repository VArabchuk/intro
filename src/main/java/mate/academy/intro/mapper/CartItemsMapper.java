package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.cartitem.CartItemResponseDto;
import mate.academy.intro.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemsMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "title")
    CartItemResponseDto toDto(CartItem cartItem);
}
