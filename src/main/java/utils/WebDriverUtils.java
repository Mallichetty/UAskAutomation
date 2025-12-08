package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WebDriverUtils {

    private WebDriver driver;
    private WebDriverWait wait;

    public WebDriverUtils(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForElementInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void click(By locator) {
        waitForElementClickable(locator).click();
    }

    public void type(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    public void sendKeysWithEnter(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text, Keys.ENTER);
    }

    public String getText(By locator) {
        return waitForElementVisible(locator).getText();
    }

    public String getAttribute(By locator) {
        return waitForElementVisible(locator).getAttribute("value");
    }

    public boolean isDisplayed(By locator) {
        try {
            return waitForElementVisible(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void scrollIntoView(By locator) {
        WebElement element = waitForElementVisible(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void waitForPageLoad() {
        wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }

    public void hoverOverElement(By locator) {
        WebElement element = waitForElementVisible(locator);
        new Actions(driver).moveToElement(element).perform();
    }

    public void doubleClick(By locator) {
        WebElement element = waitForElementVisible(locator);
        new Actions(driver).doubleClick(element).perform();
    }

    public void rightClick(By locator) {
        WebElement element = waitForElementVisible(locator);
        new Actions(driver).contextClick(element).perform();
    }

    public void dragAndDrop(By sourceLocator, By targetLocator) {
        WebElement source = waitForElementVisible(sourceLocator);
        WebElement target = waitForElementVisible(targetLocator);
        new Actions(driver).dragAndDrop(source, target).perform();
    }

    public void clearAndType(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    public List<WebElement> getElements(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public void clickJS(By locator) {
        WebElement element = waitForElementVisible(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void scrollBy(int x, int y) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    public void refreshPage() {
        driver.navigate().refresh();
        waitForPageLoad();
    }

    public void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    public void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).dismiss();
    }

    public String getAlertText() {
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }
}
