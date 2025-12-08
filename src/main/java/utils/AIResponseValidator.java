package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AIResponseValidator {

    private static final Logger logger = LogManager.getLogger(AIResponseValidator.class);
    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-001:embedContent?key="
                    + ConfigReader.get("GEMINI_API_KEY");

    private static final double SIMILARITY_THRESHOLD = 0.75;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();

    public static List<Double> getEmbeddings(String text) {
        try {
            String requestBody = """
                    {
                      "model": "models/gemini-embedding-001",
                      "content": {
                        "parts": [
                          { "text": "%s" }
                        ]
                      }
                    }
                    """.formatted(text.replace("\"", "\\\""));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(GEMINI_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Gemini API Error: " + response.body());
            }

            JsonNode json = mapper.readTree(response.body());

            JsonNode embeddingArray =
                    json.get("embedding").get("values");

            List<Double> vector = new ArrayList<>();
            embeddingArray.forEach(node -> vector.add(node.asDouble()));

            return vector;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch embeddings from Gemini", e);
        }
    }

    public static double cosineSimilarity(List<Double> v1, List<Double> v2) {

        double dot = 0.0, mag1 = 0.0, mag2 = 0.0;

        for (int i = 0; i < v1.size(); i++) {
            dot += v1.get(i) * v2.get(i);
            mag1 += v1.get(i) * v1.get(i);
            mag2 += v2.get(i) * v2.get(i);
        }

        return dot / (Math.sqrt(mag1) * Math.sqrt(mag2));
    }

    public static boolean isSemanticallyValid(String actualResponse,
                                              String expectedResponse) {
        try {

            System.out.println("EXPECTED: " + expectedResponse);
            System.out.println("ACTUAL: " + actualResponse);

            List<Double> expectedVec = getEmbeddings(expectedResponse);
            List<Double> actualVec = getEmbeddings(actualResponse);

            double score = cosineSimilarity(expectedVec, actualVec);
            logger.info("Semantic Similarity Score: " + score);

            return score >= SIMILARITY_THRESHOLD;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValidHtml(String text) {
        try {
            Document doc = Jsoup.parse(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isResponseComplete(String text) {
        if (text == null || text.trim().isEmpty()) return false;

        String trimmed = text.trim();

        if (!trimmed.matches(".*[.!?]$")) return false;

        String[] badEndings = {
                "and", "but", "so", "because", "therefore", "however", "for example"
        };

        for (String ending : badEndings) {
            if (trimmed.toLowerCase().endsWith(" " + ending)) {
                return false;
            }
        }

        if (trimmed.endsWith("**") || trimmed.endsWith("*") ||
                trimmed.endsWith("<b>") || trimmed.endsWith("</") ||
                trimmed.endsWith("<")) {
            return false;
        }

        return true;
    }

}
