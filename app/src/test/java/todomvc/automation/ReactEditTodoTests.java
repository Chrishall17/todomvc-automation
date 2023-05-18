package todomvc.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ReactEditTodoTests {
    private static WebDriver driver;
    public Actions act;
    @BeforeEach
    public void launchDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver();
        act = new Actions(driver);
    }

    @Test
    void modifyTodoByDoubleClicking() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        WebElement todoTemplate = driver.findElement(By.xpath("/html/body/section/div/section/ul/li/div/label"));
        act.doubleClick(todoTemplate).perform();
        WebElement todoEdit = driver.findElement(By.xpath("/html/body/section/div/section/ul/li/input"));
        todoEdit.sendKeys(Keys.BACK_SPACE);
        todoEdit.sendKeys(Keys.RETURN);
        assertEquals("Test Tod", todoTemplate.getText());
    }

    @Test
    void escapeModifyTodo() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        WebElement todoTemplate = driver.findElement(By.xpath("/html/body/section/div/section/ul/li/div/label"));
        act.doubleClick(todoTemplate).perform();
        WebElement todoEdit = driver.findElement(By.xpath("/html/body/section/div/section/ul/li/input"));
        todoEdit.sendKeys(Keys.ESCAPE);
        assertEquals("Test Todo", todoTemplate.getText());
        System.out.println(todoTemplate.getCssValue("text-decoration"));
    }

    @Test
    void checkTodoMarkedAsComplete() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        WebElement todoTemplate = driver.findElement(By.xpath("/html/body/section/div/section/ul/li/div/label"));
        WebElement toggleComplete = driver.findElement(By.xpath("/html/body/section/div/section/ul/li/div/input"));
        toggleComplete.click();
        String todoCSSValue = todoTemplate.getCssValue("text-decoration-line");
        assertEquals("line-through", todoCSSValue);
    }

    @Test
    void checkTodoMarkedAsCompleteThenUncheck() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        WebElement todoTemplate = driver.findElement(By.xpath("/html/body/section/div/section/ul/li/div/label"));
        WebElement toggleComplete = driver.findElement(By.xpath("/html/body/section/div/section/ul/li/div/input"));
        toggleComplete.click();
        toggleComplete.click();
        String todoCSSValue = todoTemplate.getCssValue("text-decoration-line");
        assertEquals("none", todoCSSValue);
    }

    @Test
    void deleteIncompleteTodo() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        WebElement todoTemplate = driver.findElement(By.xpath("/html/body/section/div/section/ul/li/div/label"));
        act.moveToElement(todoTemplate).perform();
        WebElement destroyTodo = driver.findElement(By.xpath("/html/body/section/div/section/ul/li/div/button"));
        destroyTodo.click();
        List<WebElement> todoTemplateList = driver.findElements(By.xpath("/html/body/section/div/section/ul/li/div/label"));
        assertEquals(0, todoTemplateList.size());
    }

    @Test// METHOD 2 OF VERIFYING AN ELEMENT IS ABSENT - ASSERT EXCEPTION IS THROWN
    public void canDeleteIncompleteToDoRaiseException() throws NoSuchElementException {

        // Get Template Page with 1 To-do Created
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();

        // Mouse over To-do to Make Destroy Button Visible
        WebElement toDoTemplate = driver.findElement(page.newlyCreatedTodo);
        act.moveToElement(toDoTemplate).perform();

        // Click Destroy Button
        WebElement destroy = driver.findElement(page.destroyTodoButton);
        destroy.click();

        // Try to Get Absent Web Element, Assert Exception is Thrown
        assertThrows(NoSuchElementException.class, () ->{

            WebElement updatedToDo = driver.findElement(page.newlyCreatedTodo);

        });
    }



    @Test
    public void canSwitchBetweenActiveInactiveCompleted(){
        // Get Template Page with 1 To-do Created
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();

        // Create another to-do and mark complete
        WebElement searchBox = driver.findElement(page.todoSearchBox);
        searchBox.click();
        searchBox.sendKeys("new todo");
        searchBox.sendKeys(Keys.RETURN);

        // Strike Through to complete to-do
        WebElement toggleComplete = driver.findElement(page.toggleTodoCompleteButton);
        toggleComplete.click();

        // Click to Active
        WebElement activeTab = driver.findElement(page.activeTab);
        activeTab.click();


        // Check To-Do is Visible with 'Test Input'
        WebElement createdTodo = driver.findElement(By.xpath("/html/body/section/div/section/ul/li[1]/div/label"));
        assertEquals("new todo", createdTodo.getText());

        // Click to Completed
        WebElement completedTab = driver.findElement(page.completedTab);
        completedTab.click();
        WebElement secondTodo = driver.findElement(page.newlyCreatedTodo);
        assertEquals("Test Todo", secondTodo.getText());
    }

    @AfterEach
    public void exitDriver() {
        driver.quit();
    }
}
