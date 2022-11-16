package com.example.dreamfood.BusinessLayer.Classes;

import junit.framework.TestCase;

import org.junit.Assert;

public class StringsTest extends TestCase {

    public void testEmailStart() {
        String email="yairby@gmail.com" ;
        try {

             Strings con=new Strings();
            Assert.assertNotEquals(con.emailStart(email), "yairby");
        }
        catch (Exception e){
            e.getMessage();
        }

    }
}