package todomvc.automation;

import com.google.gson.Gson;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class ReactEditTodoTestsChrome {
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

    @Test
    void clearCompletedAppearsWhenTodoMarkedAsComplete() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        WebElement toggleComplete = driver.findElement(page.toggleTodoCompleteButton);
        toggleComplete.click();
        WebElement clearCompletedLink = driver.findElement(page.clearCompleted);
        assertTrue(clearCompletedLink.isDisplayed());

    }

    @Test
    void clearCompletedClearsOneTodoWhichIsMarkedComplete() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        WebElement toggleComplete = driver.findElement(page.toggleTodoCompleteButton);
        toggleComplete.click();
        WebElement clearCompletedLink = driver.findElement(page.clearCompleted);
        clearCompletedLink.click();
        assertThrows(NoSuchElementException.class, () ->{

            WebElement updatedToDo = driver.findElement(page.newlyCreatedTodo);

        });
    }

    @Test
    void clearCompletedOnlyClearsTodosMarkedComplete() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        page.createTodoTemplate();
        WebElement toggleComplete = driver.findElement(page.toggleTodoCompleteButton);
        toggleComplete.click();
        WebElement clearCompletedLink = driver.findElement(page.clearCompleted);
        clearCompletedLink.click();
        WebElement createdTodo = driver.findElement(page.newlyCreatedTodo);
        assertEquals("Test Todo",createdTodo.getText());
    }

    @Test
    void arrowSymbolClickedOnceMarksAllTodosComplete() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        page.createTodoTemplate();
        WebElement toggleAllComplete = driver.findElement(page.arrowToggle);
        toggleAllComplete.click();
        WebElement firstTodo = driver.findElement(page.firstTodo);
        WebElement secondTodo = driver.findElement(page.secondTodo);
        String firstTodoCSSValue = firstTodo.getCssValue("text-decoration-line");
        assertEquals("line-through", firstTodoCSSValue);
        String secondTodoCSSValue = secondTodo.getCssValue("text-decoration-line");
        assertEquals("line-through", secondTodoCSSValue);
    }

    @Test
    void arrowSymbolClickedTwiceMarksAllTodosCompleteThenIncomplete() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        page.createTodoTemplate();
        WebElement toggleAllComplete = driver.findElement(page.arrowToggle);
        toggleAllComplete.click();
        WebElement firstTodo = driver.findElement(page.firstTodo);
        WebElement secondTodo = driver.findElement(page.secondTodo);
        String firstTodoInitialCSSValue = firstTodo.getCssValue("text-decoration-line");
        String secondTodoInitialCSSValue = secondTodo.getCssValue("text-decoration-line");
        assertEquals("line-through", firstTodoInitialCSSValue);
        assertEquals("line-through", secondTodoInitialCSSValue);
        toggleAllComplete.click();
        String firstTodoAfterCSSValue = firstTodo.getCssValue("text-decoration-line");
        String secondTodoAfterCSSValue = secondTodo.getCssValue("text-decoration-line");
        assertEquals("none", firstTodoAfterCSSValue);
        assertEquals("none", secondTodoAfterCSSValue);
    }

    @Test
    public void reactMaintainsXNumberOfTodos() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        for (int i = 1; i < 20; i++ ) {
            page.createTodoTemplate();
            if (i == 1) {
                WebElement todoItem = driver.findElement(page.firstTodo);
                assertEquals("Test Todo", todoItem.getText());
            } else {
                WebElement todoItem = driver.findElement(By.xpath(page.newTodoListValue.replace("[1]", "["+i+"]")));
                assertEquals("Test Todo", todoItem.getText());
            }
        }
    }

    @Test
    public void addingTodoInReactDoesNotBleedOverToBackbone(){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        page.createTodoTemplate();
        BackbonePage page2 = new BackbonePage(driver);
        page2.navigateBackbone();
        assertThrows(NoSuchElementException.class, () ->{

            WebElement updatedToDo = driver.findElement(page2.newlyCreatedTodo);

        });
    }

    @Test
    public void refreshingPageDoesNotClearTodo() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        page.navigateReact();
        WebElement todoItem = driver.findElement(page.firstTodo);
        assertEquals("Test Todo", todoItem.getText());
    }

    @Test
    public void testReactLocalStorageContainsMethod() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        WebElement todoItem = driver.findElement(page.firstTodo);
        LocalStorage local = ((WebStorage) driver).getLocalStorage();
        String JSONData = local.getItem("react-todos");
        assertTrue(JSONData.contains("\"title\":\"Test Todo\""));
        assertTrue(JSONData.contains("\"completed\":false"));
    }

    @Test
    public void testReactLocalStorageHashMapMethod() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        // Get React Todos From Local Storage
        LocalStorage local = ((WebStorage) driver).getLocalStorage();
        String reactTodos = local.getItem("react-todos");
        // Get rid of the Brackets from Array
        String extractedString = reactTodos.substring(1, reactTodos.length() - 1);
        // use Gson to Parse the String into a Hashmap
        Gson parser= new Gson();
        HashMap<String, Object> reactMap= parser.fromJson(extractedString, HashMap.class);
        // Step 3: Access the hashmap
        String id = reactMap.get("id").toString();
        String title = reactMap.get("title").toString();
        String completed = reactMap.get("completed").toString();
        assertEquals("Test Todo", title);
    }

    @Test
    public void jsonStructureIsAsSpecified(){

        // Create Template To-do and another To-do with the text "hello"
        ReactPage page = new ReactPage(driver);
        page.createTodoTemplate();
        page.createToDoWithGivenString("hello");

        // Complete One "Test Input" to-do
        WebElement toggleComplete = driver.findElement(page.toggleTodoCompleteButton);
        toggleComplete.click();

        // Get React Todos Array of Jsons From Local Storage
        LocalStorage local = ((WebStorage) driver).getLocalStorage();
        String reactTodos = local.getItem("react-todos");

        // Create an Array of Jsons (One for Each To-do)
        JSONArray todosArray = new JSONArray(reactTodos);

        // Get Individual Jsons from the Array
        JSONObject todoOne = todosArray.getJSONObject(0);
        JSONObject todoTwo = todosArray.getJSONObject(1);

        // Store the Values of Keys from the respective Jsons
        String todoOneTitle = todoOne.get("title").toString();
        String todoOneId = todoOne.get("id").toString();
        String todoOneCompleted = todoOne.get("completed").toString();
        String todoTwoTitle = todoTwo.get("title").toString();
        String todoTwoId = todoTwo.get("id").toString();
        String todoTwoCompleted = todoTwo.get("completed").toString();

        // Assert that the inputs are correct under key title
        assertEquals("Test Todo", todoOneTitle);
        assertEquals("hello", todoTwoTitle);

        // Assert that the correct to-dos are completed and not completed respectively
        assertEquals("true", todoOneCompleted);
        assertEquals("false", todoTwoCompleted);

        // Assert that the IDs are unique from one another
        assertNotEquals(todoOneId, todoTwoId);

        // UPDATE SELENIUM NOTES / JAVA NOTES & CHEATSHEETS
    }

    @AfterEach
    public void exitDriver() {
        driver.quit();
    }
}
