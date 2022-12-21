package org.example;

import okhttp3.OkHttpClient;
import org.apache.commons.collections15.functors.FalsePredicate;
import org.example.api.HttpClient;
import org.example.models.Entry;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class System {

    WebDriver driver;
    AlertPage alertPage;
    HttpClient client;

    private int alerts = 0;

    private boolean logged = false;

    private final By loginButton = By.xpath("/html/body/header/nav/div/div/ul/li[3]/a");
    private final By logoutButton = By.xpath("/html/body/header/nav/div/div/ul/li[3]/a");
    private final By loginTextField = By.xpath("//*[@id=\"UserId\"]");
    private final By submitButton = By.xpath("/html/body/div/main/form/input[2]");

    public System(WebDriver driver) {
        this.client = new HttpClient(new OkHttpClient());
        driver.get("https://www.marketalertum.com");
        this.driver = driver;
        this.alertPage = new AlertPage(this.driver);
    }

    public void addAlert() {
        client.addAlert(new Entry(
                "6",
                "ps5",
                "a playstation",
                "https://smth.com",
                "image url",
                "200000"
        ));
        alerts += 1;
    }

    public void getAlerts() {
    }

    public void purgeAlerts() {
        client.purgeAlerts();
        alerts = 0;
    }

    public void validLogIn() throws InterruptedException {
        driver.get("https://www.marketalertum.com/Alerts/Login");
        Thread.sleep(100);
        WebElement field = driver.findElement(loginTextField);
        String userId = "a338083d-1742-421a-be40-5978848942df";
        field.sendKeys(userId);
        driver.findElement(submitButton).submit();
        logged = true;
    }

    public void invalidLogIn() throws InterruptedException {
        driver.get("https://www.marketalertum.com/Alerts/Login");
        Thread.sleep(100);
        WebElement field = driver.findElement(loginTextField);
        field.sendKeys("invalidLogIn");
        driver.findElement(submitButton).submit();
        logged = false;
    }

    public void logOut() {
        properLogOut();
        this.driver.quit();
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.marketalertum.com");
        this.driver = driver;
        this.alertPage = new AlertPage(this.driver);
        logged = false;
    }

    public void reset() {
        this.driver.quit();
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.marketalertum.com");
        this.driver = driver;
        this.alertPage = new AlertPage(this.driver);
        logged = false;
    }

    public void properLogOut() {
        driver.findElement(logoutButton).click();
    }

    public boolean isLogged() {
        return logged;
    }

    public int getAlertsAmount() {
        return alerts;
    }
}
