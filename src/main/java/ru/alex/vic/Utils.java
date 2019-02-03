package ru.alex.vic;

import java.io.InputStream;
import java.util.Properties;

public class Utils {


    public static Properties createProperties(String propertyFileName) {
        try (final InputStream stream = Utils.class.getClassLoader().getResourceAsStream(propertyFileName)) {
            Properties properties = new Properties();
            properties.load(stream);
            return properties;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
