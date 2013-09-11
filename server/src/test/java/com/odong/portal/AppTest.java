package com.odong.portal;

import org.testng.annotations.BeforeTest;

public class AppTest {



    private void log(Object... objects) {
        for (Object o : objects) {
            System.out.println(o);
        }
    }
    //@Test
    public void test0() {
        try{
       App.main(new String[]{});
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @BeforeTest
    public void init() {

    }
}
