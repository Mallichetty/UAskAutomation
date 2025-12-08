package steps;

import com.fasterxml.jackson.databind.JsonNode;
import core.DriverFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import pages.ChatWidgetPage;
import utils.AIResponseValidator;
import utils.ScreenshotUtil;
import utils.TestDataLoader;

import java.util.Iterator;

import static org.junit.Assert.assertTrue;

public class UAskChatbotSteps {

    private static final Logger logger = LogManager.getLogger(UAskChatbotSteps.class);
    private WebDriver driver = DriverFactory.getDriver();
    private ChatWidgetPage chatWidget = new ChatWidgetPage(driver);

    private final JsonNode messagesJson = TestDataLoader.get().get("messages");

    private String lastUserMessage;
    private String aiResponse;

    @When("I send the user input {string}")
    public void send_user_input(String userInput) {
        lastUserMessage = userInput;
        chatWidget.sendMessage(userInput);
        logger.info("Sent user input: {}", userInput);
    }

    @Then("the chatbot response should match the expected result from JSON")
    public void validate_response_with_json() throws Exception {
        aiResponse = chatWidget.getLastChatbotResponse();
        logger.info("AI Response: {}", aiResponse);

        String expectedResponse = null;
        Iterator<JsonNode> iter = messagesJson.elements();
        while (iter.hasNext()) {
            JsonNode node = iter.next();
            if (node.get("userMessage").asText().equalsIgnoreCase(lastUserMessage)) {
                expectedResponse = node.get("expectedResponse").asText();
                break;
            }
        }

        if (expectedResponse == null) {
            throw new RuntimeException("Expected response not found in JSON for input: " + lastUserMessage);
        }

        boolean isValid = AIResponseValidator.isSemanticallyValid(aiResponse, expectedResponse);

        if (!isValid) {
            ScreenshotUtil.takeScreenshotAs("SemanticValidationFailed");
        }

        assertTrue("AI response is semantically invalid for input: " + lastUserMessage, isValid);
    }

}
