package finalProject.StatWeaver.dto;

import lombok.Data;

@Data
public class CharacterDto {
    private Long id;
    private String name;

    private int intelligence;
    private int strength;
    private int defense;
    private int agility;
    private int magic;

    private int elfPercent;
    private int dwarfPercent;
    private int dragonPercent;

    private int totalPoints;
    private int availablePoints;

    private String mainType;
    private double powerMultiplier;
}

