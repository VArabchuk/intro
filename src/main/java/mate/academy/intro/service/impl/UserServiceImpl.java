package mate.academy.intro.service.impl;

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
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        String email = requestDto.getEmail();

        checkUserExistsByEmail(email);

        User newUser = userMapper.toUser(requestDto);

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Role roleForNewUser = getRoleForNewUser(email);

        newUser.setPassword(encodedPassword);
        newUser.getRoles().add(roleForNewUser);

        return userMapper.toUserResponse(userRepository.save(newUser));
    }

    private void checkUserExistsByEmail(String email) throws RegistrationException {
        if (userRepository.existsUserByEmail(email)) {
            throw new RegistrationException("The user with email '"
                    + email
                    + "' is already registered");
        }
    }

    private Role getRoleForNewUser(String email) {
        return rolesRepository.findRoleByRoleName(email.toLowerCase().contains("admin@")
                ? Role.RoleName.ROLE_ADMIN
                : Role.RoleName.ROLE_USER);
    }
}
