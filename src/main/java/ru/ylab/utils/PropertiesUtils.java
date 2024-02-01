package ru.ylab.utils;

import lombok.experimental.UtilityClass;
import ru.ylab.exception.PropertiesNotLoadedException;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

@UtilityClass
public class PropertiesUtils {
    private final Map<PropertiesType, Properties> properties = new EnumMap<>(PropertiesType.class);

    public Properties getProperties(PropertiesType type) {
        if (properties.containsKey(type)) {
            return properties.get(type);
        }

        Properties props = new Properties();
        var fileName = getPropertiesFileNameByPropertiesType(type);
        try (var inputStream = ClassLoader.getSystemResourceAsStream(fileName)) {
            props.load(inputStream);
            properties.put(type, props);
        } catch (IOException e) {
            throw new PropertiesNotLoadedException(e);
        }
        return props;
    }

    private String getPropertiesFileNameByPropertiesType(PropertiesType type) {
        return switch (type) {
            case DATABASE -> "database.properties";
            case MIGRATIONS -> "liquibase.properties";
            default -> throw new IllegalArgumentException(String.format("Property not found %s", type));
        };
    }

    public enum PropertiesType {
        DATABASE,
        MIGRATIONS
    }
}
