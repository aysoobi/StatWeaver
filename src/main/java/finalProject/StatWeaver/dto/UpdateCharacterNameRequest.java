package finalProject.StatWeaver.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCharacterNameRequest {
    @NotBlank
    private String name;
}

