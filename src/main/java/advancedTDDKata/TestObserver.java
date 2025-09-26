package advancedTDDKata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestObserver {

    private static final List<String> logs = new ArrayList<>();
    private static final HashMap<String, Object> capturedData = new HashMap<>();

    public static void log(String message) {
        logs.add(message);
    }

    public static String getLogs() {
        return String.join("\n", logs);
    }

    public static void captureValue(String key, Object value) {
        capturedData.put(key, value);
    }

    public static Object getCapturedValue(String key) {
        return capturedData.get(key);
    }

    public static void reset() {
        logs.clear();
        capturedData.clear();
    }
}
