package org.example.util;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static Properties props = new Properties();

    static {
        try (InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) props.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}