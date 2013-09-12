package com.odong;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppTest {
    //@Test
    public void test0(){
        try {
            App app = new App();
            app.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void test1(){

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
