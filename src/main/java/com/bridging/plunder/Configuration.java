package com.bridging.plunder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    // read and store the assets.plunder/configuration.properties values
    private Properties properties = new Properties();

    public Configuration() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("assets/plunder/configuration.properties")) {
            if (input == null) {
                System.out.println("[Plunder v1.1] - Could not find configuration file.");
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
    }
}
