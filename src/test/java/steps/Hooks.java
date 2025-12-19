package steps;

import core.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import utils.ConfigReader;
import utils.ScreenshotUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
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

    @AfterStep
    public void afterStep(Scenario scenario) {
        try {
            String screenshotPath = ScreenshotUtil.takeScreenshot(
                    driver,
                    scenario.getName().replaceAll("[\\s/]+", "_") + "_" + System.currentTimeMillis()
            );

            if (screenshotPath != null) {
                File screenshotFile = new File(screenshotPath);

                if (screenshotFile.exists()) {
                    byte[] content = Files.readAllBytes(screenshotFile.toPath());

                    Allure.addAttachment(
                            "Step Screenshot - " + scenario.getName(),
                            "image/png",
                            new ByteArrayInputStream(content),
                            ".png"
                    );

                    logger.info("Step screenshot attached for scenario: {}", scenario.getName());
                }
            }
        } catch (Exception e) {
            logger.error("Failed to capture step screenshot", e);
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                String path = ScreenshotUtil.takeScreenshot(
                        driver,
                        scenario.getName().replaceAll("[\\s/]+", "_") + "_FAILED"
                );

                if (path != null) {
                    byte[] content = Files.readAllBytes(new File(path).toPath());
                    Allure.addAttachment(
                            "Failed Screenshot",
                            "image/png",
                            new ByteArrayInputStream(content),
                            ".png"
                    );
                }

                String failureInfo =
                        "Scenario: " + scenario.getName() +
                                "\nStatus: " + scenario.getStatus();

                Allure.addAttachment(
                        "Failure Summary",
                        "text/plain",
                        new ByteArrayInputStream(failureInfo.getBytes(StandardCharsets.UTF_8)),
                        ".txt"
                );
            }
        } catch (Exception e) {
            logger.error("Error during failure handling", e);
        }

        DriverFactory.quitDriver();
        logger.info("Driver quit after scenario: {}", scenario.getName());
    }
}
