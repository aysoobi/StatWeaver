package finalProject.StatWeaver.dto;

import lombok.Data;

import java.util.List;

@Data
public class StartGenerationResponse {
    //для того чтобы кол-во вопросов выбирать
    private List<QuestionDto> questions;
    private int questionCount;
}

