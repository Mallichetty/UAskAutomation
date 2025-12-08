package steps;

import core.DriverFactory;
import io.cucumber.java.*;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import utils.ConfigReader;
import utils.ScreenshotUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

public class Hooks {

    protected WebDriver driver;
    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected Scenario scenario;

    @Before
    public void setUp(Scenario scenario) {
        this.scenario = scenario;
        driver = DriverFactory.getDriver();
        String url = ConfigReader.get("url");
        driver.get(url);
        logger.info("Driver started and navigated to: {}", url);
    }

    @After
    public void tearDown() {
        if (scenario.isFailed()) {
            try {
                String path = ScreenshotUtil.takeScreenshot(driver, scenario.getName().replaceAll("[\\s/]+", "_"));
                if (path != null) {

                    File screenshotFile = new File(path);
                    byte[] content = Files.readAllBytes(screenshotFile.toPath());
                    Allure.addAttachment("Failed Screenshot", new ByteArrayInputStream(content));
                    logger.error("Screenshot saved and attached to Allure report: {}", path);
                }
            } catch (Exception e) {
                logger.error("Failed to capture screenshot for scenario: {}", scenario.getName(), e);
            }
        }

        DriverFactory.quitDriver();
        logger.info("Driver quit after scenario: {}", scenario.getName());
    }
}
