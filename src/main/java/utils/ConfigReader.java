package utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    public static void loadConfig() {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load config.properties: " + e.getMessage());
        }
    }

    public static String get(String key) {
        if (properties == null) loadConfig();
        return properties.getProperty(key);
    }
}
