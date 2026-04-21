package finalProject.StatWeaver.mapper;

import finalProject.StatWeaver.dto.AnswerOptionDto;
import finalProject.StatWeaver.dto.QuestionDto;
import finalProject.StatWeaver.model.Answer;
import finalProject.StatWeaver.model.Question;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionMapper {

    public QuestionDto toDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setText(question.getQuestion());
        dto.setOptions(mapOptions(question.getAnswerList()));
        return dto;
    }

    private List<AnswerOptionDto> mapOptions(List<Answer> answers) {
        return answers.stream().map(a -> {
            AnswerOptionDto dto = new AnswerOptionDto();
            dto.setId(a.getId());
            dto.setText(a.getAnswer());
            return dto;
        }).toList();
    }
}

