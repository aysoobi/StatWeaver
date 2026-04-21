package finalProject.StatWeaver.service;

import finalProject.StatWeaver.dto.CharacterDto;
import finalProject.StatWeaver.dto.CreateCharacterFromGenerationRequest;
import finalProject.StatWeaver.dto.StartGenerationResponse;

public interface CharacterGenerationService {
    StartGenerationResponse getQuestionsForGeneration(int questionCount);
    CharacterDto createCharacter(CreateCharacterFromGenerationRequest request);
}

