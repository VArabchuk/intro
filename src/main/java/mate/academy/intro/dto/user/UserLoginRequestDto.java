package mate.academy.intro.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotEmpty
    @Email
    @Size(min = 8, max = 25)
    private String email;
    @NotEmpty
    @Size(min = 8, max = 25)
    private String password;
}
