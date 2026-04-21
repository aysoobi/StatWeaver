package finalProject.StatWeaver.dto;

import lombok.Data;

import java.util.List;

@Data
public class StartGenerationResponse {
    private List<QuestionDto> questions;
    private int questionCount;
}

