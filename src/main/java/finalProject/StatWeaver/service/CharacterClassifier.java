package finalProject.StatWeaver.service;

import java.util.LinkedHashMap;
import java.util.Map;


public class CharacterClassifier {

    public record Percentages(int elf, int dwarf, int dragon) {
    }

    public Percentages classify(int intelligence, int strength, int defense, int agility, int magic) {
        double elfScore = intelligence * 1.0 + agility * 2.0 + magic * 2.0;
        double dwarfScore = strength * 2.0 + defense * 2.0 + intelligence * 0.5;
        double dragonScore = magic * 1.5 + strength * 1.5 + defense * 1.2;

        double total = elfScore + dwarfScore + dragonScore;
        if (total <= 0.0) {
            return new Percentages(34, 33, 33);
        }

        double elfFrac = elfScore / total * 100.0;
        double dwarfFrac = dwarfScore / total * 100.0;
        double dragonFrac = dragonScore / total * 100.0;

        Map<String, Double> fracs = new LinkedHashMap<>();
        fracs.put("elf", elfFrac);
        fracs.put("dwarf", dwarfFrac);
        fracs.put("dragon", dragonFrac);

        int elf = (int) Math.floor(elfFrac);
        int dwarf = (int) Math.floor(dwarfFrac);
        int dragon = (int) Math.floor(dragonFrac);

        int used = elf + dwarf + dragon;
        int remaining = 100 - used;

        if (remaining > 0) {
            for (int i = 0; i < remaining; i++) {
                String best = fracs.entrySet().stream()
                        .max((a, b)
                                -> Double.compare(a.getValue() - Math.floor(a.getValue()),
                                b.getValue() - Math.floor(b.getValue())))
                        .map(Map.Entry::getKey)
                        .orElse("elf");
                fracs.put(best, fracs.get(best) + 1e-9);
                if ("elf".equals(best)) elf++;
                if ("dwarf".equals(best)) dwarf++;
                if ("dragon".equals(best)) dragon++;
            }
        }


        int sum = elf + dwarf + dragon;
        if (sum != 100) {
            elf += 100 - sum;
        }

        return new Percentages(elf, dwarf, dragon);
    }
}

