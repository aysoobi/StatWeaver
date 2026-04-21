package finalProject.StatWeaver.controller;

import finalProject.StatWeaver.dto.CharacterDto;
import finalProject.StatWeaver.dto.CreateCharacterFromGenerationRequest;
import finalProject.StatWeaver.dto.StartGenerationResponse;
import finalProject.StatWeaver.dto.SpendPointsRequest;
import finalProject.StatWeaver.dto.UpdateCharacterNameRequest;
import finalProject.StatWeaver.service.CharacterGenerationService;
import finalProject.StatWeaver.service.CharacterLibraryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CharacterController {

    private final CharacterGenerationService generationService;
    private final CharacterLibraryService libraryService;

    public CharacterController(CharacterGenerationService generationService, CharacterLibraryService libraryService) {
        this.generationService = generationService;
        this.libraryService = libraryService;
    }

    @RequestMapping(value = "/generation/start", method = {RequestMethod.GET, RequestMethod.POST})
    public StartGenerationResponse startGenerationAny(
            @RequestParam(name = "questionCount", defaultValue = "10") int questionCount
    ) {
        return generationService.getQuestionsForGeneration(questionCount);
    }

    @PostMapping("/characters/from-generation")
    public CharacterDto createFromGeneration(@Valid @RequestBody CreateCharacterFromGenerationRequest request) {
        return generationService.createCharacter(request);
    }

    @GetMapping("/characters")
    public List<CharacterDto> listCharacters() {
        return libraryService.listCharacters();
    }

    @GetMapping("/characters/{id}")
    public CharacterDto getCharacter(@PathVariable long id) {
        return libraryService.getCharacter(id);
    }

    @PutMapping("/characters/{id}")
    public CharacterDto renameCharacter(@PathVariable long id, @Valid @RequestBody UpdateCharacterNameRequest request) {
        return libraryService.renameCharacter(id, request.getName());
    }

    @PutMapping("/characters/{id}/spend")
    public CharacterDto spendPoints(@PathVariable long id, @Valid @RequestBody SpendPointsRequest request) {
        return libraryService.spendPoints(id, request);
    }

    @DeleteMapping("/characters/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable long id) {
        libraryService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}
}

