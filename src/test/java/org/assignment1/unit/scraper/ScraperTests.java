package org.assignment1.unit.scraper;

import org.assignment1.models.Entry;
import org.assignment1.scraper.Scraper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;

public class ScraperTests {
    static final String searchText = "ps5";
    static final int alertType = 6;

    @Mock
    private WebDriver driver;

    @Mock
    private WebDriverWait wait;

    Scraper scraper;

    @BeforeEach
    public void setup() {
        driver = Mockito.mock(WebDriver.class);
        wait = Mockito.mock(WebDriverWait.class);
        scraper = new Scraper(driver, wait);
    }

    @AfterEach
    public void teardown() {
        scraper = null;
    }

    @Test
    public void testTransformPrice() {
        String cleanPrice = scraper.transformPrice("GBP 889.99 test");
        Assertions.assertEquals("88999", cleanPrice);
    }

    @Test
    public void testTransformPriceEmpty() {
        String cleanPrice = scraper.transformPrice("");
        Assertions.assertEquals("", cleanPrice);
    }

    @Test
    public void testTransformPriceLetters() {
        String cleanPrice = scraper.transformPrice("test");
        Assertions.assertEquals("", cleanPrice);
    }

    @Test
    public void testGetEntriesSuccess() {
        WebElement element = Mockito.mock(WebElement.class);
        WebDriver.TargetLocator locator = Mockito.mock(WebDriver.TargetLocator.class);
        Mockito.when(
                driver.findElement(Mockito.any())
        ).thenReturn(element);
        Mockito.when(
                driver.findElement(Mockito.any()).getAttribute(Mockito.any())
        ).thenReturn("attribute");
        Mockito.when(
                driver.findElement(Mockito.any()).getText()
        ).thenReturn("heading");
        Mockito.when(
                wait.until(Mockito.any())
        ).thenReturn(element);
        Mockito.when(
                driver.switchTo()
        ).thenReturn(locator);
        Mockito.when(
                locator.window(Mockito.any())
        ).thenReturn(driver);

        ArrayList<Entry> list = scraper.getEntries(1, searchText, alertType);
        Assertions.assertEquals(1, list.size());
        Entry test = list.get(0);
        Assertions.assertEquals("heading", test.getHeading());
        Assertions.assertEquals("", test.getDescription());
        Assertions.assertEquals("attribute", test.getImageUrl());
        Assertions.assertEquals("attribute", test.getUrl());
        Assertions.assertEquals("", test.getPriceInCents());
    }

    @Test
    public void testGetEntriesException() {
        WebElement element = Mockito.mock(WebElement.class);
        Mockito.when(
                driver.findElement(Mockito.any())
        ).thenThrow(new RuntimeException());
        Mockito.when(
                wait.until(Mockito.any())
        ).thenReturn(element);

        ArrayList<Entry> list = scraper.getEntries(1, searchText, alertType);
        Assertions.assertEquals(0, list.size());
    }
}
