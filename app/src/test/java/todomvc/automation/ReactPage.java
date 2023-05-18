package todomvc.automation;

import org.openqa.selenium.*;

public class ReactPage {
    protected WebDriver driver;

    public By todoSearchBox = By.className("new-todo");
    public By todoLocator = By.xpath("/html/body/section/div/section/ul/li/div/label");
    public String newTodoListValue = "/html/body/section/div/section/ul/li[1]/div/label";
    public By newlyCreatedTodo = By.xpath(newTodoListValue);
    public By firstTodo = By.xpath("/html/body/section/div/section/ul/li[1]/div/label");
    public By secondTodo = By.xpath("/html/body/section/div/section/ul/li[2]/div/label");
    public By destroyTodoButton = By.xpath("/html/body/section/div/section/ul/li/div/button");
    public By todoCounter = By.xpath("//span[@class='todo-count']");
    public By toggleTodoCompleteButton = By.xpath("/html/body/section/div/section/ul/li/div/input");
    public By allTab = By.linkText("All");
    public By activeTab = By.linkText("Active");
    public By completedTab = By.linkText("Completed");
    public By clearCompleted = By.xpath("/html/body/section/div/footer/button");
    public By arrowToggle = By.xpath("/html/body/section/div/section/label");
    public String pageURL = "https://todomvc.com/examples/react/#/";


    public ReactPage(WebDriver driver){
        this.driver = driver;}


    public void navigateReact() {
        driver.get(pageURL);
    }

    public String createToDoWithGivenString(String input){
        navigateReact();
        WebElement searchBox = driver.findElement(todoSearchBox);
        searchBox.click();
        searchBox.sendKeys(input);
        searchBox.sendKeys(Keys.RETURN);
        try {
            WebElement newTodo = driver.findElement(todoLocator);
            return newTodo.getText();
        } catch (NoSuchElementException noLocator) {
        return "Locator not found";
        }
    }

    public void createTodoTemplate() {
        navigateReact();
        WebElement searchBox = driver.findElement(todoSearchBox);
        searchBox.click();
        searchBox.sendKeys("Test Todo");
        searchBox.sendKeys(Keys.RETURN);
    }

    public String ElementException() {
        try {
            WebElement newTodo = driver.findElement(todoLocator);
            return newTodo.getText();
        } catch (NoSuchElementException noLocator) {
            return "Locator not found";
        }
    }

    public String getTodoCount(){
        WebElement todoCount = driver.findElement(todoCounter);
        return todoCount.getText();
    }

}
