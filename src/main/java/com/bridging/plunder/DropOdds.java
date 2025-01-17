package com.bridging.plunder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class DropOdds {
    private List<DropChance> dropOdds;

    public DropOdds() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("assets/plunder/odds.json")) {
            if (input == null) {
                System.out.println("[Plunder v1.1] - Could not find odds.json file.");
                return;
            }
            InputStreamReader reader = new InputStreamReader(input);
            Type listType = new TypeToken<DropOddsWrapper>() {}.getType();
            DropOddsWrapper wrapper = new Gson().fromJson(reader, listType);
            dropOdds = wrapper.dropOdds;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Optional<Double> getDropChance(String item) {
        dropOdds.forEach(dropChance -> {
            System.out.println("Item: " + dropChance.item + " Chance: " + dropChance.chance);
        });
        return dropOdds.stream()
                .filter(dropChance -> dropChance.item.equals(item))
                .map(dropChance -> dropChance.chance)
                .findFirst();
    }

    public static class DropChance {
        public String item;
        public double chance;
    }

    private static class DropOddsWrapper {
        List<DropChance> dropOdds;
    }
}