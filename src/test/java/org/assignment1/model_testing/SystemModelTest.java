package org.assignment1.model_testing;

import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import nz.ac.waikato.modeljunit.GreedyTester;
import nz.ac.waikato.modeljunit.StopOnFailureListener;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import org.assignment1.model_testing.enums.SystemStates;
import org.example.System;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class SystemModelTest implements FsmModel {

    private final System systemUnderTest;

    private SystemStates systemState = SystemStates.LOG_OUT_PURGED;

    private boolean logged = false;
    private int alerts = 0;

    public SystemModelTest() {
        java.lang.System.setProperty("webdriver.chrome.driver", "/home/albert/Downloads/chromedriver_linux64/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--log-level=3");
        chromeOptions.addArguments("--silent");
        WebDriver driver = new ChromeDriver(chromeOptions);
        this.systemUnderTest = new System(driver);
    }

    @Override
    public SystemStates getState() {
        return systemState;
    }

    @Override
    public void reset(boolean b) {
        if (b) {
            systemUnderTest.reset();
        }
        systemState = SystemStates.LOG_OUT_PURGED;
        logged = false;
        alerts = 0;
    }

    public @Action void addAlert() {
        systemUnderTest.addAlert();
        alerts += 1;
        if (getState().equals(SystemStates.LOG_OUT_PURGED) || getState().equals(SystemStates.LOG_OUT_ADDED)) {
            systemState = SystemStates.LOG_OUT_ADDED;
        } else  {
            systemState = SystemStates.LOG_IN_ADDED;
        }
        assertEquals("The SUT's alert amount does not match the model's alert amount after new alert", alerts, systemUnderTest.getAlertsAmount());
    }

    public @Action void purgeAlerts() {
        systemUnderTest.purgeAlerts();
        alerts = 0;
        if (getState().equals(SystemStates.LOG_OUT_ADDED) || getState().equals(SystemStates.LOG_OUT_PURGED)) {
            systemState = SystemStates.LOG_OUT_PURGED;
        } else {
            systemState = SystemStates.LOG_IN_PURGED;
        }
        assertEquals("The SUT's alert amount does not match the model's alert amount after alert purge", alerts, systemUnderTest.getAlertsAmount());
    }

    public boolean validLogInGuard() {
        return getState().equals(SystemStates.LOG_OUT_ADDED) || getState().equals(SystemStates.LOG_OUT_PURGED);
    }
    public @Action void validLogIn() throws InterruptedException {
        systemUnderTest.validLogIn();
        logged = true;
        if (getState().equals(SystemStates.LOG_OUT_PURGED)) {
            systemState = SystemStates.LOG_IN_PURGED;
        } else if (getState().equals(SystemStates.LOG_OUT_ADDED)) {
            systemState = SystemStates.LOG_IN_ADDED;
        }
        assertEquals("The SUT's logged in state does not match the model's logged in state after log in", logged, systemUnderTest.isLogged());
    }

    public boolean validLogOutGuard() {
        return getState().equals(SystemStates.LOG_IN_PURGED) || getState().equals(SystemStates.SEE_ZERO_ALERTS)
                || getState().equals(SystemStates.LOG_IN_ADDED) || getState().equals(SystemStates.SEE_LAST_5_ALERTS);
    }
    public @Action void validLogOut() {
        systemUnderTest.logOut();
        logged = false;
        if (getState().equals(SystemStates.LOG_IN_PURGED) || getState().equals(SystemStates.SEE_ZERO_ALERTS)) {
            systemState = SystemStates.LOG_OUT_PURGED;
        } else if (getState().equals(SystemStates.LOG_IN_ADDED) || getState().equals(SystemStates.SEE_LAST_5_ALERTS)) {
            systemState = SystemStates.LOG_OUT_ADDED;
        }
        assertEquals("The SUT's logged in state does not match the model's logged in state after log out", logged, systemUnderTest.isLogged());
    }

    public boolean invalidLogInGuard() {
        return getState().equals(SystemStates.LOG_OUT_ADDED) || getState().equals(SystemStates.LOG_OUT_PURGED);
    }
    public @Action void invalidLogIn() throws InterruptedException {
        systemUnderTest.invalidLogIn();
        assertEquals("The SUT's logged in state does not match the model's logged in state after false log in", logged, systemUnderTest.isLogged());
    }

    public boolean seeLastAlertsGuard() {
        return getState().equals(SystemStates.LOG_IN_PURGED) || getState().equals(SystemStates.LOG_IN_ADDED)
                || getState().equals(SystemStates.SEE_ZERO_ALERTS) || getState().equals(SystemStates.SEE_LAST_5_ALERTS);
    }
    public @Action void seeLastAlerts() {
        systemUnderTest.getAlerts();
        if (getState().equals(SystemStates.LOG_IN_PURGED)) {
            systemState = SystemStates.SEE_ZERO_ALERTS;
        } else if (getState().equals(SystemStates.LOG_IN_ADDED)) {
            systemState = SystemStates.SEE_LAST_5_ALERTS;
        }
    }

    @Test
    public void SystemModelTestRunner() {
        final GreedyTester tester = new GreedyTester(new SystemModelTest()); //Creates a test generator that can generate random walks. A greedy random walk gives preference to transitions that have never been taken before. Once all transitions out of a state have been taken, it behaves the same as a random walk.
        tester.setRandom(new Random()); //Allows for a random path each time the model is run.
        tester.buildGraph(); //Builds a model of our FSM to ensure that the coverage metrics are correct.
        tester.addListener(new StopOnFailureListener()); //This listener forces the test class to stop running as soon as a failure is encountered in the model.
        tester.addListener("verbose"); //This gives you printed statements of the transitions being performed along with the source and destination states.
        tester.addCoverageMetric(new TransitionPairCoverage()); //Records the transition pair coverage i.e. the number of paired transitions traversed during the execution of the test.
        tester.addCoverageMetric(new StateCoverage()); //Records the state coverage i.e. the number of states which have been visited during the execution of the test.
        tester.addCoverageMetric(new ActionCoverage()); //Records the number of @Action methods which have been executed during the execution of the test.
        tester.generate(100); //Generates 100 transitions
        tester.printCoverage(); //Prints the coverage metrics specified above.
    }
}
