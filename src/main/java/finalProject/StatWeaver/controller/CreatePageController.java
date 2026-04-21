package finalProject.StatWeaver.controller;

import finalProject.StatWeaver.dto.CharacterDto;
import finalProject.StatWeaver.dto.CreateCharacterFromGenerationRequest;
import finalProject.StatWeaver.dto.StartGenerationResponse;
import finalProject.StatWeaver.service.CharacterGenerationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
    @RequiredArgsConstructor
public class CreatePageController {

    private final CharacterGenerationService characterGenerationService;


    @GetMapping("/create")
    public String createStartPage(
            @RequestParam(name = "error", required = false) String error,
            Model model
    ) {
        model.addAttribute("error", error);
        model.addAttribute("defaultQuestionCount", 10);
        return "create";
    }

    @PostMapping("/create/start")
    public String startQuestionnaire(
            @RequestParam(name = "characterName") String characterName,
            @RequestParam(name = "questionCount", defaultValue = "10") int questionCount,
            Model model
    ) {
        try {
            StartGenerationResponse response = characterGenerationService.getQuestionsForGeneration(questionCount);
            model.addAttribute("characterName", characterName);
            model.addAttribute("questions", response.getQuestions());
            return "create-quiz";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("defaultQuestionCount", questionCount);
            return "create";
        }
    }

    @PostMapping("/create/submit")
    public String submitQuestionnaire(
            @RequestParam(name = "characterName") String characterName,
            @RequestParam Map<String, String> params,
            Model model
    ) {
        try {
            Map<Long, Long> selectedAnswers = new HashMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                if (!key.startsWith("answer_")) {
                    continue;
                }
                long questionId = Long.parseLong(key.substring("answer_".length()));
            
                long answerId = Long.parseLong(entry.getValue());
                selectedAnswers.put(questionId, answerId);
            }

            CreateCharacterFromGenerationRequest request = new CreateCharacterFromGenerationRequest();
            request.setCharacterName(characterName);
            request.setSelectedAnswersByQuestionId(selectedAnswers);

            CharacterDto created = characterGenerationService.createCharacter(request);
            model.addAttribute("character", created);
            return "create-result";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("defaultQuestionCount", 10);
            return "create";
        }
    }
}

