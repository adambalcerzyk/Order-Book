import java.util.*;
// If using an IDE or javac, ensure the following JARs are in your classpath:
//   lib/jackson-annotations-2.19.0.jar
//   lib/jackson-core-2.19.0.jar
//   lib/jackson-databind-2.19.0.jar
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> parseJson(String json) {
        try {
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "{}";
        }
    }
}
