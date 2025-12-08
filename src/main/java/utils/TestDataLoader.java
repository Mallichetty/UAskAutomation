package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class TestDataLoader {
    private static JsonNode root;
    static {
        try (InputStream is = TestDataLoader.class.getClassLoader().getResourceAsStream("test-data.json")) {
            ObjectMapper mapper = new ObjectMapper();
            root = mapper.readTree(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static JsonNode get() { return root; }
}
