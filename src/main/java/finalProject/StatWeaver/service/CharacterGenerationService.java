package finalProject.StatWeaver.service;

import finalProject.StatWeaver.dto.CharacterDto;
import finalProject.StatWeaver.dto.CreateCharacterFromGenerationRequest;
import finalProject.StatWeaver.dto.StartGenerationResponse;

public interface CharacterGenerationService {
    //берет кол-во вопросов
    StartGenerationResponse getQuestionsForGeneration(int questionCount);
    //для создание героя
    CharacterDto createCharacter(CreateCharacterFromGenerationRequest request);
}

