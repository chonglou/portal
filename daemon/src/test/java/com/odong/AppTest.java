package com.odong;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppTest
{
    @Test
    public void test0(){
        System.out.println("OK");
    }
    @BeforeTest
    private void init(){

    }
}
