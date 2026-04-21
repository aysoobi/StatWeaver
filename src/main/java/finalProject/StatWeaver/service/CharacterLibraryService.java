package finalProject.StatWeaver.service;

import finalProject.StatWeaver.dto.CharacterDto;
import finalProject.StatWeaver.dto.SpendPointsRequest;

import java.util.List;

public interface CharacterLibraryService {
    List<CharacterDto> listCharacters();

    CharacterDto getCharacter(long id);

    CharacterDto renameCharacter(long id, String newName);

    CharacterDto spendPoints(long id, SpendPointsRequest request);

    void deleteCharacter(long id);
}

