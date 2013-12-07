package com.odong;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;


public class AppTest {
    @Test
    public void test0() {

    }

    @BeforeTest
    public void init() {

    }

    private void log(Object... objects) {
        for (Object obj : objects) {
            System.out.println(obj);
        }
    }
}
