package org.example;

import okhttp3.OkHttpClient;
import org.example.api.HttpClient;
import org.example.models.Entry;
import org.example.models.LogEvent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        java.lang.System.setProperty("webdriver.chrome.driver", "/home/albert/Downloads/chromedriver_linux64/chromedriver");
        WebDriver driver = new ChromeDriver();
        Entry alert = new Entry(
                "6",
                "ps5",
                "a playstation",
                "https://smth.com",
                "image url",
                "200000"
        );
        HttpClient client = new HttpClient(new OkHttpClient());
        System logger = new System(driver);
        List<LogEvent> events = client.eventLog();
        client.purgeAlerts();
        events.isEmpty();
    }
}

