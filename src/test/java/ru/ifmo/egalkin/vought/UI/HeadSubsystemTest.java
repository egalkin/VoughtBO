package ru.ifmo.egalkin.vought.UI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@Sql(value = {"/functional/create-head-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/functional/create-head-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class HeadSubsystemTest {

    private static final String DRIVER_PATH = "C:/Users/e.eschenko/Downloads/chromedriver_win32(3)/chromedriver.exe";
    private static final String KEY_PROPERTY = "webdriver.chrome.driver";
    private static final String URL = "http://localhost:8082";
    WebDriver driver;
    WebDriverWait wait;

    @AfterEach
    public void setUp() {
        driver.quit();
    }

    @BeforeEach
    public void headSubsystemLoginTest() {
        System.setProperty(KEY_PROPERTY, DRIVER_PATH);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
        driver.manage().window().maximize();
        driver.get(URL);
        driver.findElement(By.id("log")).sendKeys("edgar@vought.com");
        driver.findElement(By.id("pass")).sendKeys("test");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btn")));
        driver.findElement(By.id("btn")).click();

    }

    @Test
    public void headSubsystemEmployeeTest() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("emp")));
        driver.findElement(By.id("emp")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("newEmp")));
        driver.findElement(By.id("newEmp")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("selDep")));
        new Select(driver.findElement(By.id("selDep"))).selectByIndex(2);
        driver.findElement(By.id("fn")).sendKeys("Никита");
        driver.findElement(By.id("ln")).sendKeys("Никитов");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add")));
        driver.findElement(By.id("add")).click();

        driver.findElement(By.id("3")).click();
        driver.findElement(By.id("fn")).sendKeys("Анатолий");
        driver.findElement(By.id("save")).click();

        driver.findElement(By.id("3")).click();
        driver.findElement(By.id("del")).click();
        driver.findElement(By.id("yes")).click();
    }

    @Test
    @WithUserDetails(value = "edgar@vought.com")
    public void headSubsystemEmployeeSortedTest() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("emp")));
        driver.findElement(By.id("emp")).click();
        WebElement sort;
        sort = driver.findElement(By.xpath("/html/body/div/section/table/tbody/tr[1]/td[3]/p"));
        assertEquals(sort.getText(), "Отдел: PR служба");
        new Select(driver.findElement(By.id("sortingType"))).selectByIndex(1);
        sort = driver.findElement(By.xpath("/html/body/div/section/table/tbody/tr[1]/td[3]/p"));
        assertEquals(sort.getText(), "Отдел: Служба охраны");
        new Select(driver.findElement(By.id("sortingType"))).selectByIndex(2);
        sort = driver.findElement(By.xpath("/html/body/div/section/table/tbody/tr[1]/td[3]/p"));
        assertEquals(sort.getText(), "Отдел: Лаборатория");
        new Select(driver.findElement(By.id("sortingType"))).selectByIndex(3);
        sort = driver.findElement(By.xpath("/html/body/div/section/table/tbody/tr[1]/td[3]/p"));
        assertEquals(sort.getText(), "Отдел: Герои");
    }

    @Test
    @WithUserDetails(value = "edgar@vought.com")
    public void headSubsystemSensorsTest() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("sen")));
        driver.findElement(By.id("sen")).click();
        WebElement title = driver.findElement(By.xpath("/html/body/div/div/H1"));
        assertEquals(title.getText(), "Состояние системы");
    }

    @Test
    public void headSubsystemApplicationApproveAndRejectedTest() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("app")));
        driver.findElement(By.id("app")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("3")));
        driver.findElement(By.id("3")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("approve")));
        driver.findElement(By.id("approve")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("4")));
        driver.findElement(By.id("4")).click();
        driver.findElement(By.id("reject")).click();
        driver.findElement(By.id("reason")).sendKeys("Причина отказа");
        driver.findElement(By.id("reject")).click();
    }

    @Test
    @WithUserDetails(value = "edgar@vought.com")
    public void headSubsystemApplicationSortedTest() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("app")));
        driver.findElement(By.id("app")).click();
        WebElement sort;
        sort = driver.findElement(By.xpath("/html/body/div/section/table/tbody/tr[1]/td[4]/div/p"));
        assertEquals(sort.getText(), "2022-01-20");
        new Select(driver.findElement(By.id("sortingType"))).selectByIndex(1);
        sort = driver.findElement(By.xpath("/html/body/div/section/table/tbody/tr[1]/td[4]/div/p"));
        assertEquals(sort.getText(), "2022-01-23");
        new Select(driver.findElement(By.id("sortingType"))).selectByIndex(2);
        sort = driver.findElement(By.xpath("/html/body/div/section/table/tbody/tr[1]/td[2]/p[2]"));
        assertEquals(sort.getText(), "От: Антонина Свонс");
        new Select(driver.findElement(By.id("sortingType"))).selectByIndex(3);
        sort = driver.findElement(By.xpath("/html/body/div/section/table/tbody/tr[1]/td[2]/p[2]"));
        assertEquals(sort.getText(), "От: Василий Свонс");
        new Select(driver.findElement(By.id("sortingType"))).selectByIndex(4);
        sort = driver.findElement(By.xpath("/html/body/div/section/table/tbody/tr[1]/td[3]/div/p"));
        assertEquals(sort.getText(), "На рассмотрении");
        new Select(driver.findElement(By.id("sortingType"))).selectByIndex(5);
        sort = driver.findElement(By.xpath("/html/body/div/section/table/tbody/tr[1]/td[3]/div/p"));
        assertEquals(sort.getText(), "Согласовано");

    }

}
