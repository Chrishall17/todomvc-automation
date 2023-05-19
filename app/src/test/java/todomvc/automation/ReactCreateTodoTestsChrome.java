package todomvc.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class ReactCreateTodoTestsChrome {
    private static WebDriver driver;
    @BeforeEach
    public void launchDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver();
    }

    @Test
    void addNewTodo() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        String testString = "a";
        ReactPage page = new ReactPage(driver);
        String result = page.createToDoWithGivenString(testString);
        assertEquals(testString, result);
    }

    @Test
    void CantAddEmptyString() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        String testString = "";
        ReactPage page = new ReactPage(driver);
        String result = page.createToDoWithGivenString(testString);
        assertEquals("Locator not found", result);
    }

    @Test
    void addMultiStringTodo() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        String testString = "a quick brown fox";
        ReactPage page = new ReactPage(driver);
        page.createToDoWithGivenString(testString);
        WebElement newTodo = driver.findElement(By.xpath("//label[contains(.,'"+testString+"')]"));
        assertEquals(testString, newTodo.getText());
    }


    @Test
    void checkBasicPunctuationSupported() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        String testString = "Hello, do you like my shoes? I think they're great!";
        ReactPage page = new ReactPage(driver);
        String result = page.createToDoWithGivenString(testString);
        assertEquals(testString, result);
    }
    // Locator used in other tests, struggled with basic punctuation so had to change to a different locator

    @Test
    void checkComplexCharactersSupported() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        String testString = "±§!@£$%^&*();',./?";
        ReactPage page = new ReactPage(driver);
        String result = page.createToDoWithGivenString(testString);
        assertEquals(testString, result);
    }

    @Test
    void checkLanguageNativeCharactersSupported() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        String testString = "ẞ大本éñ";
        ReactPage page = new ReactPage(driver);
        String result = page.createToDoWithGivenString(testString);
        assertEquals(testString, result);
    }


    @Test
    public void toDoMaintainsCharacterCountOf280(){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        String testString = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat mas";
        ReactPage page = new ReactPage(driver);
        String result = page.createToDoWithGivenString(testString);
        assertEquals(testString, result);
        assertEquals(280, result.length());
    }

    @Test
    public void toDoMaintainsCharacterCountOfX(){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        String baseString = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean m";
        String testString = baseString.repeat(10);
        ReactPage page = new ReactPage(driver);
        String result = page.createToDoWithGivenString(testString);
        assertEquals(testString, result);
        assertEquals(1000, result.length());
    }


    @AfterEach
    public void exitDriver() {
        driver.quit();
    }
}