package mate.academy.intro.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import mate.academy.intro.validator.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@FieldMatch(passwordFieldName = "password", repeatPasswordFieldName = "repeatPassword")
public class UserRegistrationRequestDto {
    @NotBlank
    private String email;
    @NotBlank
    @Length(min = 8, max = 25)
    private String password;
    @NotBlank
    @Length(min = 8, max = 25)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
