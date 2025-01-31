package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.user.UserLoginRequestDto;
import mate.academy.intro.dto.user.UserLoginResponseDto;
import mate.academy.intro.dto.user.UserRegistrationRequestDto;
import mate.academy.intro.dto.user.UserResponseDto;
import mate.academy.intro.exception.RegistrationException;
import mate.academy.intro.security.AuthenticationService;
import mate.academy.intro.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Registration Controller",
        description = "Controller for user registration processing")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary
            = "Register a new user", description
            = "Accepts user data, validates it, and saves the new user to the database"
    )
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto requestDto
    ) throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Accepts user credentials, validates them, "
                    + "and returns a JWT token if authentication is successful."
    )
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
