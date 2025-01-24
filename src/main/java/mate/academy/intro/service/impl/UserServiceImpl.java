package mate.academy.intro.service.impl;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.user.UserRegistrationRequestDto;
import mate.academy.intro.dto.user.UserResponseDto;
import mate.academy.intro.exception.RegistrationException;
import mate.academy.intro.mapper.UserMapper;
import mate.academy.intro.model.Role;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.user.UserRepository;
import mate.academy.intro.service.RolesService;
import mate.academy.intro.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RolesService rolesService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        String email = requestDto.getEmail();
        if (userRepository.existsUserByEmail(email)) {
            throw new RegistrationException("The user with email: "
                    + email
                    + " is already registered");
        }
        Set<Role> roles = new HashSet<>();

        Role savedRole = rolesService.save(getRoleByEmail(email));
        roles.add(savedRole);
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User savedUser = userRepository.save(userMapper.toUser(requestDto, encodedPassword, roles));
        return userMapper.toUserResponse(savedUser);
    }

    private Role getRoleByEmail(String email) {
        Role role = new Role();
        if (email.toLowerCase().startsWith("admin@")) {
            role.setRole(Role.RoleName.ROLE_ADMIN);
        } else {
            role.setRole(Role.RoleName.ROLE_USER);
        }
        return role;
    }
}
