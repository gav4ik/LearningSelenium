package ru.ibs.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class FirstTest {
    WebDriver driver;
    WebDriverWait wait;

    public FirstTest() {
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 20);
    }

    @BeforeEach
    public void before() {

        driver.get("http://training.appline.ru/user/login");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();


    }

    @Test
    public void test() throws InterruptedException {
        // Шаг 1: Авторизация
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//form[@id='login-form']"))));
        driver.findElement(By.xpath("//input[@name='_username']")).sendKeys("Irina Filippova");
        driver.findElement(By.xpath("//input[@name='_password']")).sendKeys("testing");
        driver.findElement(By.xpath("//button[@id='_submit']")).click();
        WebElement title = driver.findElement(By.xpath("//h1[@class='oro-subtitle']"));
        wait.until(ExpectedConditions.visibilityOf(title));
        Assertions.assertEquals("Панель быстрого запуска", title.getText(),
                "Не удалось перейти на страницу портала");

        // Шаг 2: Переход в командировки
        WebElement costs = driver.findElement(By.xpath
                ("//ul[contains(@class, 'main-menu')]/li/a/span[text()='Расходы']"));
        costs.click();
        wait.until(ExpectedConditions.visibilityOf(costs.findElement(By.xpath
                ("./ancestor::li//ul[@class='dropdown-menu menu_level_1']"))));
        driver.findElement(By.xpath("//span[contains(text(),'Командировки')]")).click();
        loading();

        // Шаг 3: Создание командировки
        driver.findElement(By.xpath("//div[@class='pull-left btn-group icons-holder']")).click();
        loading();
        WebElement pageTitle = driver.findElement(By.xpath("//h1[@class='user-name']"));
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        Assertions.assertEquals("Создать командировку", pageTitle.getText(),
                "Не удалось перейти на страницу создания командировки");

        //Подразделение - выбрать отдел внутренней разработки

        WebElement devision = driver.findElement(By.xpath("//select[@name='crm_business_trip[businessUnit]']"));
        devision.click();
        WebElement myDevision = devision.findElement(By.xpath
                ("./option[text()='Отдел внутренней разработки']"));
        myDevision.click();
        Assertions.assertEquals("Отдел внутренней разработки", myDevision.getText(),
                "Не удалось выбрать нужный отдел");

        // Выбор принимающей организации

        WebElement chooseCompany = driver.findElement(By.xpath("//a[@id='company-selector-show']"));
        chooseCompany.click();
        WebElement company = driver.findElement(By.xpath("//span[@class='select2-chosen']"));
        wait.until(ExpectedConditions.visibilityOf(company));
        company.click();
        WebElement myCompany = driver.findElement(By.xpath("//li/div[text()='(Хром) Призрачная Организация Охотников']"));
        myCompany.click();
        Assertions.assertEquals("(Хром) Призрачная Организация Охотников", company.getText(),
                "Не удалось выбрать нужную компанию");

        //Проставить чекбокс "Заказ билетов"

        WebElement tickets = driver.findElement(By.xpath("//input[@type='checkbox'][@value='1']"));
        tickets.click();
        Assertions.assertTrue(tickets.isSelected(), "Чекбокс заказа билетов не выбран");

         //Указать города выбытия и прибытия
        WebElement townDeparture = driver.findElement(By.xpath("//input[@data-name='field__departure-city']"));
        townDeparture.click();
        townDeparture.clear();
        townDeparture.sendKeys("Россия, Москва");
        Assertions.assertEquals("Россия, Москва",
                townDeparture.getAttribute("value"),
                "Город выбытия не указан");

        WebElement townArrival = driver.findElement(By.xpath("//input[@data-name='field__arrival-city']"));
        townArrival.click();
        townArrival.clear();
        townArrival.sendKeys("Белоруссия, Минск");
        Assertions.assertEquals("Белоруссия, Минск", townArrival.getAttribute("value"),
                "Город прибытия не указан");

        //Указать даты выезда и возращения

        WebElement departureDate = driver.findElement(By.xpath
                ("//input[@class='datepicker-input  hasDatepicker'][contains(@id, 'departureDatePlan')]"));
        wait.until(ExpectedConditions.elementToBeClickable(departureDate));
        departureDate.sendKeys(Keys.ENTER);
        departureDate.click();
        departureDate.clear();
        departureDate.sendKeys("12.04.2022");
        Thread.sleep(3000);


        WebElement returnDate = driver.findElement(By.xpath
                ("//input[@class='datepicker-input  hasDatepicker'][contains(@id, 'returnDatePlan')]"));
        wait.until(ExpectedConditions.elementToBeClickable(returnDate));
        returnDate.sendKeys(Keys.ENTER);
        returnDate.click();
        returnDate.clear();
        returnDate.sendKeys("19.04.2022");
        returnDate.sendKeys(Keys.TAB);
        Thread.sleep(3000);


        Assertions.assertEquals("12.04.2022",
                departureDate.getAttribute("value"),
                "Дата выезда неверная");
        Assertions.assertEquals("19.04.2022",
                returnDate.getAttribute("value"),
                "Дата возвращения неверная");

        //Нажатие "Сохранить и закрыть"
        WebElement btn = driver.findElement(By.xpath("//button[@class='btn btn-success action-button']"));
        btn.click();
        loading();
        WebElement mistake= driver.findElement
                (By.xpath("//span[@class='validation-failed']" +
                        "[text()='Список командируемых сотрудников не может быть пустым'][1]"));
        Assertions.assertEquals("Список командируемых сотрудников не может быть пустым", mistake.getText(),
                "Валидация поля 'Командированные сотрудники' не работает");

    }

    @AfterEach
    public void after() {
        driver.quit();
    }

    public void loading() {
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("//div[@class='loader-content']"))));
    }
}
