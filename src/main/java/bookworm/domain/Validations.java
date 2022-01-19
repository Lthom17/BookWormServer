package bookworm.domain;

import java.util.UUID;

public class Validations {
    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    public static boolean isNullOrBlank(UUID value) {
        return value == null;
    }
}