package mate.academy.intro.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDto {
    @NotBlank
    @Size(min = 3, max = 25)
    private String name;
    @NotBlank
    private String description;
}
