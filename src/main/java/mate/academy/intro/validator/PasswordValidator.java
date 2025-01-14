package mate.academy.intro.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mate.academy.intro.dto.user.UserRegistrationRequestDto;

public class PasswordValidator
        implements ConstraintValidator<PasswordMatch, UserRegistrationRequestDto> {
    @Override
    public boolean isValid(UserRegistrationRequestDto user, ConstraintValidatorContext context) {
        String password = user.getPassword();
        String repeatPassword = user.getRepeatPassword();
        return password != null && password.equals(repeatPassword);
    }
}
