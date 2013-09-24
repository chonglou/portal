package com.odong;

import org.snmp4j.TransportMapping;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


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
