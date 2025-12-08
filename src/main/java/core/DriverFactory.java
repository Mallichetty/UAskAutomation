package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.ConfigReader;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {

        if (driver.get() == null) {

            String browser = System.getProperty(
                    "browser",
                    ConfigReader.get("browser")
            );

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");

            if (browser.equalsIgnoreCase("mobile")) {
                System.out.println("Launching Chrome in mobile responsive mode");

                Map<String, Object> deviceMetrics = new HashMap<>();
                deviceMetrics.put("width", Integer.parseInt(ConfigReader.get("mobile.device.width")));
                deviceMetrics.put("height", Integer.parseInt(ConfigReader.get("mobile.device.height")));
                deviceMetrics.put("pixelRatio", Double.parseDouble(ConfigReader.get("mobile.device.pixelRatio")));

                Map<String, Object> mobile = new HashMap<>();
                mobile.put("deviceMetrics", deviceMetrics);

                options.setExperimentalOption("mobileEmulation", mobile);
            } else {
                System.out.println("Launching Chrome in desktop mode");
            }

            WebDriver wd = new ChromeDriver(options);
            wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.set(wd);
        }

        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
