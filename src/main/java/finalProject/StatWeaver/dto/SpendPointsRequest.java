package finalProject.StatWeaver.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class SpendPointsRequest {
    @Min(0)
    private int intelligence;

    @Min(0)
    private int strength;

    @Min(0)
    private int defense;

    @Min(0)
    private int agility;

    @Min(0)
    private int magic;
}

