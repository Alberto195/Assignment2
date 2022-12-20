package org.example.scraper;

import org.example.models.Entry;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

public class Scraper {

    protected WebDriver driver;

    protected WebDriverWait wait;

    protected ArrayList<Entry> entries;

    public Scraper(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.entries = new ArrayList<>();
    }

    public ArrayList<Entry> getEntries(int amount, String searchText, int type) {
        try {
            driver.get("https://www.ebay.com/");
            String findButtonXpath = "//*[@id=\"gh-ac\"]";
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(findButtonXpath)));

            WebElement searchField = driver.findElement(By.xpath(findButtonXpath));
            searchField.sendKeys(searchText);
            WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"gh-btn\"]"));
            searchButton.submit();
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"mainContent\"]"))));

            String originalWindow = driver.getWindowHandle();
            for (int i = 1; i < amount+1; i++) {
                String productUrl = driver.findElement(By.xpath("//*[@id=\"srp-river-results\"]/ul/li[" + i + "]/div/div[1]/div/a")).getAttribute("href");
                String imageUrl = driver.findElement(By.xpath("//*[@id=\"srp-river-results\"]/ul/li[" + i + "]/div/div[1]/div/a/div/img")).getAttribute("src");
                String heading = driver.findElement(By.xpath("//*[@id=\"srp-river-results\"]/ul/li[" + i + "]/div/div[2]/a/div/span")).getText();
                driver.findElement(By.xpath("//*[@id=\"srp-river-results\"]/ul/li[" + i + "]/div/div[2]/a/div/span")).click();
                wait.until(numberOfWindowsToBe(2));

                String windowToCloseHandle = "";
                for (String windowHandle : driver.getWindowHandles()) {
                    if (!originalWindow.contentEquals(windowHandle)) {
                        driver.switchTo().window(windowHandle);
                        windowToCloseHandle = windowHandle;
                        break;
                    }
                }

                String price = driver.findElement(By.xpath("//*[@id=\"mainContent\"]/form/div[2]/div/div[1]/div[1]/div/div[2]/div[1]/span[1]/span")).getText();
                String newPrice = transformPrice(price);

                String descriptionLink = driver.findElement(By.xpath("//*[@id=\"desc_ifr\"]")).getAttribute("src");
                String html = this.getHtml(descriptionLink);
                Document doc = Jsoup.parse(html);
                String description = doc.body().text();

                entries.add(new Entry(Integer.toString(type), heading, description, productUrl, imageUrl, newPrice));
                driver.switchTo().window(windowToCloseHandle).close();
                driver.switchTo().window(originalWindow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.quit();

        return entries;
    }

    public String transformPrice(String price) {
        String newPrice = price.replaceAll("[\\D]", "");
        if (!newPrice.equals("")) {
            return newPrice;
        }

        return "";
    }

    private String getHtml(String urlPath) {
        StringBuilder description = new StringBuilder();
        try {
            URL url = new URL(urlPath);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String input;
            while ((input = br.readLine()) != null) {
                description.append(input);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return description.toString();
    }
}
