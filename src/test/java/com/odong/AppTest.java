package com.odong;

import com.odong.portal.entity.User;
import org.jasypt.util.text.StrongTextEncryptor;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Unit test for  App.
 */
public class AppTest{
    @Test
    public void testName(){
        Class<User> clazz = User.class;
        log(clazz.toString(), clazz.getName(), clazz.getSimpleName());
    }
    public void testEntity(){
        StrongTextEncryptor ste = new StrongTextEncryptor();
        ste.setPassword("sdfwrew");
        for(int i=1; i<=10; i++){

            String plain = "123456";
            String encrypt = ste.encrypt(plain);
            log("第["+i+"]次加密", plain, encrypt, ste.decrypt(encrypt));
            try{
                Thread.sleep(1000);
            }
            catch (InterruptedException e){

            }
        }
    }
    private void log(Object...objects){
        for(Object o : objects){
            System.out.print(o==null?" " : o.toString());
            System.out.print("\t");
        }
        System.out.println();
    }
    @BeforeTest
    public void setup(){
    }
}
