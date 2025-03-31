package queue.utils;

public class Utils {
    public static final String SUCCESS = "Успешно";

    public static Exception error(String message) {
        return new Exception(message);
    }

    public static Exception authError() {
        return error("Ошибка аутентификации");
    }
}
