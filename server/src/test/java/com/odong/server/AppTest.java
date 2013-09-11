package com.odong.server;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppTest
{
    @Test
    public void test1(){

    }

    public void test0(){
        try{
            App app = new App();
            app.init(null);
            app.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
   @BeforeTest
    public void init(){

   }
}
