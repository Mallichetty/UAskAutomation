package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WebDriverUtils;

public class LoginPage {

    private WebDriver driver;
    private WebDriverUtils utils;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.utils = new WebDriverUtils(driver, 10);
    }

    private By splashScreenLoginButton = By.xpath("//*[@id='splash-screen']/div[2]/div/button");
    private By emailInput = By.id("email");
    private By passwordInput = By.id("password");
    private By loginButton = By.xpath("//*[@type='submit']");

    public void clickSplashLoginBtn() {
        utils.click(splashScreenLoginButton);
    }

    public void enterEmail(String email) {
        utils.clearAndType(emailInput, email);
    }

    public void enterPassword(String password) {
        utils.clearAndType(passwordInput, password);
    }

    public void clickLogin() {
        utils.click(loginButton);
    }

    public void login(String email, String password) {
        clickSplashLoginBtn();
        enterEmail(email);
        enterPassword(password);
        clickLogin();
        utils.waitForPageLoad();
    }
}
