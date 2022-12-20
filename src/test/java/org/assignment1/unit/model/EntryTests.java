package org.assignment1.unit.model;

import org.assignment1.models.Entry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntryTests {

    Entry entry;

    @BeforeEach
    public void setup() {
        entry = new Entry(Integer.toString(0), "testHeading", "testDescription", "testUrl", "testImageUrl", "testPrice");
    }

    @AfterEach
    public void teardown() {
        entry = null;
    }

    @Test
    public void getTypeTest() {
        Assertions.assertEquals("0", entry.getType());
    }

    @Test
    public void getHeadingTest() {
        Assertions.assertEquals("testHeading", entry.getHeading());
    }

    @Test
    public void getDescriptionTest() {
        Assertions.assertEquals("testDescription", entry.getDescription());
    }

    @Test
    public void getUrlTest() {
        Assertions.assertEquals("testUrl", entry.getUrl());
    }

    @Test
    public void getImageUrlTest() {
        Assertions.assertEquals("testImageUrl", entry.getImageUrl());
    }

    @Test
    public void getPriceTest() {
        Assertions.assertEquals("testPrice", entry.getPriceInCents());
    }
}
