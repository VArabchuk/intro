package mate.academy.intro.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import mate.academy.intro.exception.FieldValidationException;

public class PasswordValidator
        implements ConstraintValidator<FieldMatch, Object> {
    private String passwordFieldName;
    private String repeatPasswordFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.passwordFieldName = constraintAnnotation.passwordFieldName();
        this.repeatPasswordFieldName = constraintAnnotation.repeatPasswordFieldName();
    }

    @Override
    public boolean isValid(Object requestDto, ConstraintValidatorContext context) {
        try {
            Field passwordField
                    = requestDto.getClass().getDeclaredField(passwordFieldName);
            Field repeatPasswordField
                    = requestDto.getClass().getDeclaredField(repeatPasswordFieldName);

            passwordField.setAccessible(true);
            repeatPasswordField.setAccessible(true);

            Object password = passwordField.get(requestDto);
            Object repeatPassword = repeatPasswordField.get(requestDto);
            if (password != null && password.equals(repeatPassword)) {
                return true;
            } else {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Fields "
                                + passwordFieldName
                                + " and "
                                + repeatPasswordFieldName
                                + " do not match!").addConstraintViolation();
                return false;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FieldValidationException("Error accessing fields "
                    + passwordFieldName
                    + " and "
                    + repeatPasswordFieldName
                    + " for validation", e);
        }
    }
}
