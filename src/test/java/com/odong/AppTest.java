package com.odong;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Unit test for  App.
 */
@Test
public class AppTest{
    @Test
    public void test(){
    }
    private void log(Object...objects){
        for(Object o : objects){
            System.out.print(o==null?" " : o.toString());
        }
        System.out.println();
    }
    @BeforeTest
    public void setup(){
    }
}
