package mate.academy.intro.mapper;

import java.util.Set;
import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.user.UserRegistrationRequestDto;
import mate.academy.intro.dto.user.UserResponseDto;
import mate.academy.intro.model.Role;
import mate.academy.intro.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toUserResponse(User user);

    @Mapping(source = "encodedPassword", target = "password")
    @Mapping(source = "savedRoles", target = "roles")
    User toUser(
            UserRegistrationRequestDto requestDto, String encodedPassword, Set<Role> savedRoles);
}
