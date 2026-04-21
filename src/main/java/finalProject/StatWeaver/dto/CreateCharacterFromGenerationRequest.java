package finalProject.StatWeaver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Map;

@Data
public class CreateCharacterFromGenerationRequest {
    //для создание героя
    @NotBlank
    private String characterName;

    @NotEmpty
    private Map<Long, Long> selectedAnswersByQuestionId;
    //от ответа клиента беред id ответа и вопроса
}

