package finalProject.StatWeaver.mapper;

import finalProject.StatWeaver.dto.CharacterDto;
import finalProject.StatWeaver.model.Character;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {

    public CharacterDto toDto(Character character) {
        CharacterDto dto = new CharacterDto();
        dto.setId(character.getId());
        dto.setName(character.getName());

        dto.setIntelligence(character.getIntelligence());
        dto.setStrength(character.getStrength());
        dto.setDefense(character.getDefense());
        dto.setAgility(character.getAgility());
        dto.setMagic(character.getMagic());

        dto.setElfPercent(character.getElfPercent());
        dto.setDwarfPercent(character.getDwarfPercent());
        dto.setDragonPercent(character.getDragonPercent());

        dto.setTotalPoints(character.getTotalPoints());
        dto.setAvailablePoints(character.getAvailablePoints());

        int elf = character.getElfPercent();
        int dwarf = character.getDwarfPercent();
        int dragon = character.getDragonPercent();

        //чтобы выяснить кто доминирует
    

    int max = Math.max(elf, Math.max(dwarf, dragon));
 
     if (max == elf) {
    dto.setMainType("Elf");
    } else if (max == dwarf) {
    dto.setMainType("Dwarf");
    } else {
    dto.setMainType("Dragon");
      }

        dto.setPowerMultiplier(max / 100.0);


        return dto;
    }
}

