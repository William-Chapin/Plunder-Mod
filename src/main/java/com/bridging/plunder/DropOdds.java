package com.bridging.plunder;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class DropOdds {
    private List<DropChance> dropOdds;
    private final Path configFilePath = Paths.get("mods/plunder/odds.json");
    private static final Logger LOGGER = LogManager.getLogger();

    public DropOdds() {
        try {
            if (Files.notExists(configFilePath)) {
                generateDefaultConfig();
            }
            loadConfig();
        } catch (IOException e){
            LOGGER.error(Plunder.messagePrefix + "Failed to load drop odds configuration.", e);
        } catch (JsonSyntaxException e){
            LOGGER.error(Plunder.messagePrefix + "Invalid JSON in drop odds configuration.", e);
        } catch (IllegalArgumentException e){
            LOGGER.error(Plunder.messagePrefix + "Invalid drop odds configuration.", e);
        }
    }

    private void generateDefaultConfig() throws IOException{
        try (InputStream defaultConfigStream = getClass().getClassLoader().getResourceAsStream(("assets/plunder/default_odds.json"))){
            if (defaultConfigStream == null){
                throw new IOException(Plunder.messagePrefix + "Default drop odds were not found in the JAR.");
            }
            Files.createDirectories(configFilePath.getParent());
            Files.copy(defaultConfigStream, configFilePath);
            System.out.println(Plunder.messagePrefix + "Default drop odds file has been generated.");
        }
    }

    public void loadConfig() throws IOException{
        try (InputStream configStream = Files.newInputStream(configFilePath)){
            InputStreamReader reader = new InputStreamReader(configStream);
            Type listType = new TypeToken<DropOddsWrapper>() {}.getType();
            DropOddsWrapper wrapper = new Gson().fromJson(reader, listType);
            dropOdds = wrapper.dropOdds;
        }
        validateConfig();
    }

    private void validateConfig(){
        if (dropOdds == null || dropOdds.isEmpty()){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Drop odds configuration is empty or invalid.");
        }
        for (DropChance dropChance : dropOdds){
            if (dropChance.item == null || dropChance.item.isEmpty()){
                throw new IllegalStateException(Plunder.messagePrefix + "Drop odds configuration has an item without a name.");
            }
            if (dropChance.chance < 0.0 || dropChance.chance > 1.0){
                throw new IllegalStateException(Plunder.messagePrefix + "Drop odds configuration has an item with an invalid drop chance.");
            }
        }
    }

    public Optional<Double> getDropChance(String item) {
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