package com.odong;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppTest {
    @Test
    public void test0(){
        log(System.getProperty("user.dir"));
    }
    @BeforeTest
    public void init(){

    }
    private void log(Object...objects){
        for(Object obj : objects){
            System.out.println(obj);
        }
    }
}
