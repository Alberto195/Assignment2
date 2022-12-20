package org.assignment1.web_automation.page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EntryMarketAlertUmPage {

    protected WebDriver driver;

    private final By loginButton = By.xpath("/html/body/header/nav/div/div/ul/li[3]/a");
    private final By loginTextField = By.xpath("//*[@id=\"UserId\"]");
    private final By submitButton = By.xpath("/html/body/div/main/form/input[2]");

    private final By header = By.xpath("/html/body/div/main/form/b");

    public EntryMarketAlertUmPage(WebDriver driver) {
        this.driver = driver;
    }

    public void goToMarketAlertUm() {
        driver.get("https://www.marketalertum.com");
    }

    public void login(String login) {
        driver.findElement(loginButton).click();
        WebElement field = driver.findElement(loginTextField);
        field.sendKeys(login);
        driver.findElement(submitButton).submit();
    }

    public String getHeader() {
        return driver.findElement(header).getText();
    }
}
