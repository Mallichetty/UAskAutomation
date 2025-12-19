package steps;

import com.fasterxml.jackson.databind.JsonNode;
import core.DriverFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import pages.ChatWidgetPage;
import utils.AIResponseValidator;
import utils.SemanticResult;
import utils.TestDataLoader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
        chatWidget.isChatWidgetDisplayed();
        chatWidget.sendMessage(userInput);
        logger.info("Sent user input: {}", userInput);
    }

    @Then("the chatbot response should match the expected result from JSON")
    public void validate_response_with_json() {

        aiResponse = chatWidget.getLastChatbotResponse();
        logger.info("AI Response: {}", aiResponse);

        List<String> expectedResponses = new ArrayList<>();

        Iterator<JsonNode> iter = messagesJson.elements();
        while (iter.hasNext()) {
            JsonNode node = iter.next();

            if (node.get("userMessage").asText()
                    .equalsIgnoreCase(lastUserMessage)) {

                JsonNode expectedArray = node.get("expectedResponse");

                if (expectedArray == null || !expectedArray.isArray()) {
                    throw new RuntimeException(
                            "'expectedResponse' missing or invalid for question: "
                                    + lastUserMessage
                    );
                }

                for (JsonNode ansNode : expectedArray) {
                    String text = ansNode.asText();
                    if (text != null && !text.isBlank()) {
                        expectedResponses.add(text);
                    }
                }
                break;
            }
        }

        if (expectedResponses.isEmpty()) {
            throw new RuntimeException(
                    "No valid expected responses found for input: "
                            + lastUserMessage
            );
        }

        SemanticResult result =
                AIResponseValidator.isSemanticallyValid(aiResponse, expectedResponses);

        Allure.addAttachment("User Question", lastUserMessage);
        Allure.addAttachment("Actual Response", aiResponse);
        Allure.addAttachment("Best Matched Expected Answer",
                result.matchedExpected == null ? "NONE" : result.matchedExpected);
        Allure.addAttachment("Cosine Similarity Score",
                String.valueOf(result.cosineScore));

        assertTrue(
                "AI response failed semantic validation. Best cosine score: "
                        + result.cosineScore,
                result.isValid
        );
    }

    @Then("the chatbot should display the sent message {string}")
    public void the_chatbot_should_display_the_sent_message(String sentMessage) {
        String actualMessage = chatWidget.getLastUserMessage();
        assertEquals("Chatbot did not display the correct sent message!", sentMessage.trim(), actualMessage.trim());
        logger.info("The chatbot displays the sent message");
    }

    @Then("the input field should be cleared after sending the message")
    public void verifyInputClearedAfterSending() {
        String inputValue = chatWidget.getInputFieldValue();
        boolean isCleared = (inputValue == null || inputValue.isBlank());

        assertTrue("Input field is not cleared after sending the message.", isCleared);
        logger.info("Input field is cleared");
    }

    @Then("the chatbot response should have clean formatting")
    public void verifyResponseFormatting() {

        String response = chatWidget.getLastChatbotResponse();

        boolean htmlOK = AIResponseValidator.isValidHtml(response);
        boolean complete = AIResponseValidator.isResponseComplete(response);

        assertTrue("Chatbot returned broken HTML:\n" + response, htmlOK);
        assertTrue("Chatbot response seems incomplete:\n" + response, complete);
    }


}
