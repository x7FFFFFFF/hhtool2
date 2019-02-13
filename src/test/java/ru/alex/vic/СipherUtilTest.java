package ru.alex.vic;

import org.junit.Assert;
import org.junit.Test;

public class СipherUtilTest {


    @Test
    public void test() {
        CipherUtil util = new CipherUtil("key.sec");
        String token = "08590d6876379ce1b4b9e8fa69d479384acd920d442cccf80b37d0d7e6b4a53452e0c97837e864d2ea304";
        final String encrypt = util.encrypt(token);
        System.out.println("encrypt = " + encrypt);
        final String decrypt = util.decrypt(encrypt);
        System.out.println("decrypt = " + decrypt);
        Assert.assertEquals(token, decrypt);

    }

}