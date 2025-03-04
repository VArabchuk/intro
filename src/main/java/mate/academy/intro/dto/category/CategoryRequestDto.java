package mate.academy.intro.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CategoryRequestDto {
    @NotBlank
    @Size(min = 3, max = 25)
    private String name;
    @NotBlank
    private String description;
}
