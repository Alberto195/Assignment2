package org.assignment1.web_automation.page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class AlertPage {

    protected WebDriver driver;

    private final By alerts = By.xpath("/html/body/div/main");
    private final By alertTable = By.tagName("table");
    private final By iconPath = By.xpath("/html/body/div/main/table/tbody/tr[1]/td/h4/img");
    private final By headingPath = By.xpath("/html/body/div/main/table/tbody/tr[1]/td/h4");
    private final By descriptionPath = By.xpath("/html/body/div/main/table/tbody/tr[3]/td");
    private final By imagePath = By.xpath("/html/body/div/main/table/tbody/tr[2]/td/img");
    private final By pricePath = By.xpath("/html/body/div/main/table/tbody/tr[4]/td/b");
    private final By linkPath = By.xpath("/html/body/div/main/table/tbody/tr[5]/td/a");

    public AlertPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean seeAlerts() {
        return !driver.findElements(alerts).isEmpty();
    }

    public List<WebElement> getAlerts() {
        return driver.findElements(alertTable);
    }

    public boolean iconPresent(WebElement alert) {
        return !alert.findElements(iconPath).isEmpty();
    }

    public String headingPresent(WebElement alert) {
        return alert.findElement(headingPath).getText();
    }

    public boolean descriptionPresent(WebElement alert) {
        return !alert.findElements(descriptionPath).isEmpty();
    }

    public boolean imagePresent(WebElement alert) {
        return !alert.findElements(imagePath).isEmpty();
    }

    public boolean pricePresent(WebElement alert) {
        return !alert.findElements(pricePath).isEmpty();
    }

    public boolean linkPresent(WebElement alert) {
        return !alert.findElements(linkPath).isEmpty();
    }

    public String iconIsElectronic(WebElement alert) {
        return alert.findElement(iconPath).getAttribute("src");
    }
}
