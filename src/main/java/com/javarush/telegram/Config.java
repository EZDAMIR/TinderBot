package com.javarush.telegram;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception properly in production code
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
