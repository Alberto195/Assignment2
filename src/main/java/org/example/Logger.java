package org.example;

import org.example.api.HttpClient;
import org.example.models.Entry;
import org.example.models.LogEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    WebDriver driver;
    AlertPage alertPage;
    private final By loginButton = By.xpath("/html/body/header/nav/div/div/ul/li[3]/a");
    private final By logoutButton = By.xpath("/html/body/header/nav/div/div/ul/li[3]/a");
    private final By loginTextField = By.xpath("//*[@id=\"UserId\"]");
    private final By submitButton = By.xpath("/html/body/div/main/form/input[2]");

    public Logger(WebDriver driver) {
        this.driver = driver;
        this.alertPage = new AlertPage(this.driver);
    }

    public void testCase_1(HttpClient client, Entry entry) {
        logOut();
        navigateToAlerts();
        List<WebElement> elements = alertPage.getAlerts();
        if (elements.isEmpty()) {
            System.out.println("Yeah");
        }
        invalidLogIn();
        validLogIn();
        client.addAlert(entry);
        navigateToAlerts();
        elements = alertPage.getAlerts();
        if (!elements.isEmpty()) {
            System.out.println("Yeah");
        }
        logOut();
        navigateToAlerts();
        elements = alertPage.getAlerts();
        if (elements.isEmpty()) {
            System.out.println("Yep, no alerts");
        }
        validLogIn();
        List<LogEvent> events = client.eventLog();
        events.isEmpty();
    }

    public void validLogIn() {
        driver.findElement(loginButton).click();
        WebElement field = driver.findElement(loginTextField);
        String userId = "a338083d-1742-421a-be40-5978848942df";
        field.sendKeys(userId);
        driver.findElement(submitButton).submit();
    }

    public void invalidLogIn() {
        driver.findElement(loginButton).click();
        WebElement field = driver.findElement(loginTextField);
        field.sendKeys("invalidLogIn");
        driver.findElement(submitButton).submit();
    }

    public void logOut() {
        properLogOut();
        this.driver.quit();
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.marketalertum.com");
        this.driver = driver;
        this.alertPage = new AlertPage(this.driver);
    }

    public void properLogOut() {
        driver.findElement(logoutButton).click();
    }

    public void navigateToAlerts() {
        driver.get("https://www.marketalertum.com/Alerts/List");
    }

}
