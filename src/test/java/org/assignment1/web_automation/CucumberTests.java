package org.assignment1.web_automation;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import okhttp3.OkHttpClient;
import org.assignment1.api.HttpClient;
import org.assignment1.models.Entry;
import org.assignment1.scraper.Scraper;
import org.assignment1.web_automation.page_objects.AlertPage;
import org.assignment1.web_automation.page_objects.EntryMarketAlertUmPage;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CucumberTests {
    static final String pathToChrome = "/home/albert/Downloads/chromedriver_linux64/chromedriver";
    static final String searchText = "ps5";
    static final int alertType = 6;

    WebDriver driver;

    EntryMarketAlertUmPage entryPage;

    AlertPage alertPage;

    WebDriver scraperDriver;
    Scraper scraper;
    HttpClient client;
    List<WebElement> alerts;
    Map<Integer, String> searchMap;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", pathToChrome);
        driver = new ChromeDriver();
        scraperDriver = new ChromeDriver();
        client = new HttpClient(new OkHttpClient());
        entryPage = new EntryMarketAlertUmPage(driver);
        alertPage = new AlertPage(driver);
        searchMap = new HashMap<>(10);
        searchMap.put(1, "Car");
        searchMap.put(2, "Boat");
        searchMap.put(3, "PropertyForRent");
        searchMap.put(4, "PropertyForSale");
        searchMap.put(5, "Toys");
        searchMap.put(6, "Ps5");
    }

    @After
    public void tearDown() {
        client.purgeAlerts();
        client = null;
        driver.quit();
        driver = null;
        scraperDriver.quit();
        scraperDriver = null;
        entryPage = null;
        alertPage = null;
        searchMap = null;
    }

    @Given("I am a user of marketalertum")
    public void iAmAUserOfMarketalertum() {
        entryPage.goToMarketAlertUm();
    }

    @When("I login using valid credentials")
    public void iLoginUsingValidCredentials() {
        entryPage.login("a338083d-1742-421a-be40-5978848942df");
    }

    @Then("I should see my alerts")
    public void iShouldSeeMyAlerts() {
        boolean main = alertPage.seeAlerts();
        Assertions.assertTrue(main);
    }

    @When("I login using invalid credentials")
    public void iLoginUsingInvalidCredentials() {
        entryPage.login("invalid-login");
    }

    @Then("I should see the login screen again")
    public void iShouldSeeTheLoginScreenAgain() {
        Assertions.assertEquals("User ID:", entryPage.getHeader());
    }

    @Given("I am an administrator of the website and I upload {int} alerts")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadAlerts(int arg0) {
        scraper = new Scraper(scraperDriver, new WebDriverWait(scraperDriver, Duration.ofSeconds(1)));
        ArrayList<Entry> entries = scraper.getEntries(arg0, searchText, alertType);
        for (Entry ent : entries) {
            client.addAlert(ent);
        }
    }

    @When("I view a list of alerts")
    public void iViewAListOfAlerts() {
        iLoginUsingValidCredentials();
        iShouldSeeMyAlerts();
        alerts = alertPage.getAlerts();
    }

    @Then("each alert should contain an icon")
    public void eachAlertShouldContainAnIcon() {
        for (WebElement alert : alerts) {
            Assertions.assertTrue(alertPage.iconPresent(alert));
        }
    }

    @And("each alert should contain a heading")
    public void eachAlertShouldContainAHeading() {
        for (WebElement alert : alerts) {
            Assertions.assertNotEquals("", alertPage.headingPresent(alert));
        }
    }

    @And("each alert should contain a description")
    public void eachAlertShouldContainADescription() {
        for (WebElement alert : alerts) {
            Assertions.assertTrue(alertPage.descriptionPresent(alert));
        }
    }

    @And("each alert should contain an image")
    public void eachAlertShouldContainAnImage() {
        for (WebElement alert : alerts) {
            Assertions.assertTrue(alertPage.imagePresent(alert));
        }
    }

    @And("each alert should contain a price")
    public void eachAlertShouldContainAPrice() {
        for (WebElement alert : alerts) {
            Assertions.assertTrue(alertPage.pricePresent(alert));
        }
    }

    @And("each alert should contain a link to the original product website")
    public void eachAlertShouldContainALinkToTheOriginalProductWebsite() {
        for (WebElement alert : alerts) {
            Assertions.assertTrue(alertPage.linkPresent(alert));
        }
    }

    @Given("I am an administrator of the website and I upload more than {int} alerts")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadMoreThanAlerts(int arg0) {
        scraper = new Scraper(scraperDriver, new WebDriverWait(scraperDriver, Duration.ofSeconds(1)));
        ArrayList<Entry> entries = scraper.getEntries(arg0+1, searchText, alertType);
        for (Entry ent : entries) {
            client.addAlert(ent);
        }
    }

    @Then("I should see {int} alerts")
    public void iShouldSeeAlerts(int arg0) {
        alerts = alertPage.getAlerts();
        Assertions.assertEquals(arg0, alerts.size());
    }

    @Given("I am an administrator of the website and I upload an alert of type {int}")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadAnAlertOfType(int arg0) {
        scraper = new Scraper(scraperDriver, new WebDriverWait(scraperDriver, Duration.ofSeconds(1)));
        ArrayList<Entry> entries = scraper.getEntries(1, searchMap.get(arg0), arg0);
        for (Entry ent : entries) {
            client.addAlert(ent);
        }
    }

    @And("the icon displayed should be {string}")
    public void theIconDisplayedShouldBe(String arg0) {
        for (WebElement alert : alerts) {
            Assertions.assertTrue(alertPage.iconIsElectronic(alert).contains(arg0));
        }
    }
}
