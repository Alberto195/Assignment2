package org.assignment1.web_automation;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/org/assignment1/resources/cucumber.feature"
)
public class TestRunner {
}
