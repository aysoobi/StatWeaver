package finalProject.StatWeaver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private int availablePoints;

    private int totalPoints;

}
