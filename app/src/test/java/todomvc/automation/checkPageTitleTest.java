package todomvc.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class checkPageTitleTest {
    private static WebDriver driver;
    @BeforeAll
    static void launchBrowser() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver();
    }

    @Test
    void assertPageTitleTodo() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get("https://todomvc.com");
    }



    @AfterAll
    static void closeBrowser() {
        driver.quit();
    }
}

