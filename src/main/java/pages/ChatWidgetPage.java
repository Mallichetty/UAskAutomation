package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WebDriverUtils;

public class ChatWidgetPage {

    private WebDriver driver;
    private WebDriverUtils utils;

    public ChatWidgetPage(WebDriver driver) {
        this.driver = driver;
        this.utils = new WebDriverUtils(driver, 15); // 10 sec timeout
    }

    private By chatInputBox = By.xpath("//*[@id='chat-input']/p");
    private By sendButton = By.xpath("//*[@id='send-message-button']");
    private By lastUserMessage = By.xpath("//*[contains(@class,'user-message')]/div/div[2]/div/div[1]/div/p");
    private By lastChatbotResponse = By.xpath("//*[@id='response-content-container']/div");
    private By shimmerInvisibility = By.xpath("//*[@id='response-content-container']/div/div/span");
    private By inputField = By.id("chat-input");

    public void enterMessage(String message) {
        utils.clearAndType(chatInputBox, message);
    }

    public void clickSend() {
        utils.click(sendButton);
    }

    public void sendMessage(String message) {
        enterMessage(message);
        clickSend();
    }

    public String getLastUserMessage() {
        return utils.getText(lastUserMessage);
    }

    public String getLastChatbotResponse() {
        utils.waitForElementInvisibility(shimmerInvisibility);
        return utils.getText(lastChatbotResponse);
    }

    public boolean isChatWidgetDisplayed() {
        return utils.isDisplayed(chatInputBox);
    }

    public void scrollToChatWidget() {
        utils.scrollIntoView(chatInputBox);
    }

    public String getInputFieldValue() {
        return utils.getAttribute(inputField);
    }

}
