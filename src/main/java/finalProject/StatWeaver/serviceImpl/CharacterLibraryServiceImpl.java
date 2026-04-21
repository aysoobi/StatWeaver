package finalProject.StatWeaver.serviceImpl;

import finalProject.StatWeaver.dto.CharacterDto;
import finalProject.StatWeaver.dto.SpendPointsRequest;
import finalProject.StatWeaver.mapper.CharacterMapper;
import finalProject.StatWeaver.model.Character;
import finalProject.StatWeaver.repository.CharacterRepository;
import finalProject.StatWeaver.service.CharacterClassifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
    @RequiredArgsConstructor

public class CharacterLibraryServiceImpl implements finalProject.StatWeaver.service.CharacterLibraryService {

    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;

    private final CharacterClassifier classifier = new CharacterClassifier();



    @Override
    public List<CharacterDto> listCharacters() {
        return characterRepository.findAllByOrderByIdDesc()
                .stream()
                .map(characterMapper::toDto)
                .toList();
    }

    @Override
    public CharacterDto getCharacter(long id) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Character not found: id=" + id));
        return characterMapper.toDto(character);
    }

    @Override
    @Transactional
    public CharacterDto renameCharacter(long id, String newName) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Character not found: id=" + id));
        character.setName(newName);
        Character saved = characterRepository.save(character);
        return characterMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CharacterDto spendPoints(long id, SpendPointsRequest request) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Character not found: id=" + id));

        int spendTotal = request.getIntelligence()
                + request.getStrength()
                + request.getDefense()
                + request.getAgility()
                + request.getMagic();

        if (spendTotal <= 0) {
            return characterMapper.toDto(character);
        }

        if (spendTotal > character.getAvailablePoints()) {
            throw new IllegalArgumentException("Not enough availablePoints. Requested=" + spendTotal + ", available=" + character.getAvailablePoints());
        }

        character.setIntelligence(character.getIntelligence() + request.getIntelligence());
        character.setStrength(character.getStrength() + request.getStrength());
        character.setDefense(character.getDefense() + request.getDefense());
        character.setAgility(character.getAgility() + request.getAgility());
        character.setMagic(character.getMagic() + request.getMagic());

        character.setAvailablePoints(character.getAvailablePoints() - spendTotal);

        CharacterClassifier.Percentages p = classifier.classify(
                character.getIntelligence(),
                character.getStrength(),
                character.getDefense(),
                character.getAgility(),
                character.getMagic()
        );

        character.setElfPercent(p.elf());
        character.setDwarfPercent(p.dwarf());
        character.setDragonPercent(p.dragon());

        Character saved = characterRepository.save(character);
        return characterMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteCharacter(long id) {
        if (!characterRepository.existsById(id)) {
            throw new IllegalArgumentException("Character not found: id=" + id);
        }
        characterRepository.deleteById(id);
    }
}

