package queue.utils;

import java.util.UUID;

public class Utils {
    public static final String SUCCESS = "Успешно";
    public static final UUID ZERO_UUID = UUID.fromString("00000000-00000000-00000000-00000000");

    public static Exception error(String message) {
        return new Exception(message);
    }

    public static Exception authError() {
        return error("Ошибка аутентификации");
    }
}
