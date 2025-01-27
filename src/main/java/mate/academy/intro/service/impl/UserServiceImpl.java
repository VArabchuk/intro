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
import mate.academy.intro.repository.role.RolesRepository;
import mate.academy.intro.repository.user.UserRepository;
import mate.academy.intro.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String ADMIN_IDENTIFIER = "admin@";
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        String email = requestDto.getEmail();

        validateEmailNotRegistered(email);

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User newUser = userMapper.toUser(requestDto, encodedPassword, getRolesForNewUser(email));

        return userMapper.toUserResponse(userRepository.save(newUser));
    }

    private void validateEmailNotRegistered(String email) throws RegistrationException {
        if (userRepository.existsUserByEmail(email)) {
            throw new RegistrationException("The user with email: '"
                    + email
                    + "' is already registered");
        }
    }

    private Set<Role> getRolesForNewUser(String email) {
        Set<Role> roles = new HashSet<>();
        roles.add(rolesRepository.findRoleByRoleName(email.toLowerCase().contains(ADMIN_IDENTIFIER)
                ? Role.RoleName.ROLE_ADMIN
                : Role.RoleName.ROLE_USER));
        return roles;
    }
}
