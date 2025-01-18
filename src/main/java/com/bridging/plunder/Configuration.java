package com.bridging.plunder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Configuration {
    private Properties properties = new Properties();
    private final Path configFilePath = Paths.get("mods/plunder/configuration.properties");
    private static final Logger LOGGER = LogManager.getLogger();

    public Configuration() {
        try{
            if (Files.notExists(configFilePath)){
                generateDefaultConfig();
            }
            loadConfig();
        } catch (IOException e){
            LOGGER.error(Plunder.messagePrefix + "Failed to load configuration.", e);
        } catch (IllegalArgumentException e){
            LOGGER.error(Plunder.messagePrefix + "Invalid configuration.", e);
        }
    }

    private void generateDefaultConfig() throws IOException{
        try (InputStream defaultConfigStream = getClass().getClassLoader().getResourceAsStream(("assets/plunder/default_configuration.properties"))){
            if (defaultConfigStream == null){
                throw new FileNotFoundException(Plunder.messagePrefix + "Default configuration was not found in the JAR.");
            }
            Files.createDirectories(configFilePath.getParent());
            Files.copy(defaultConfigStream, configFilePath);
            System.out.println(Plunder.messagePrefix + "Default configuration has been generated.");
        }
    }

    public void loadConfig() throws IOException{
        try (InputStream configStream = Files.newInputStream(configFilePath)){
            properties.load(configStream);
        }
        validateConfig();
    }

    private void validateConfig(){
        if (!properties.containsKey("armorDropChance")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing armorDropChance.");
        }
        if (!properties.containsKey("toolDropChance")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing toolDropChance.");
        }
        if (!properties.containsKey("itemDropChance")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing itemDropChance.");
        }
        if (!properties.containsKey("pickUpTime")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing pickUpTime.");
        }
        if (!properties.containsKey("bypassKeepInventory")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing bypassKeepInventory.");
        }
        if (!properties.containsKey("pvpDamage")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing pvpDamage.");
        }
        if (!properties.containsKey("cactusDamage")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing cactusDamage.");
        }
        if (!properties.containsKey("explosionDamage")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing explosionDamage.");
        }
        if (!properties.containsKey("fallDamage")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing fallDamage.");
        }
        if (!properties.containsKey("fireDamage")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing fireDamage.");
        }
        if (!properties.containsKey("mobDamage")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing mobDamage.");
        }
        if (!properties.containsKey("drowningDamage")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing drowningDamage.");
        }
        if (!properties.containsKey("suffocationDamage")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing suffocationDamage.");
        }
        if (!properties.containsKey("otherDamage")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing otherDamage.");
        }
        if (!properties.containsKey("enableParticles")){
            throw new IllegalArgumentException(Plunder.messagePrefix + "Configuration is missing enableParticles.");
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
