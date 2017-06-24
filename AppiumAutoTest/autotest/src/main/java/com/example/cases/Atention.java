package com.example.cases;

import com.example.base.InitAppium;
import com.example.operation.AtentionOperate;
import com.example.operation.LoginOperate;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * 关注测试
 * Created by LITP on 2016/9/28.
 */

public class Atention extends InitAppium{


    private AtentionOperate operate;

    private LoginOperate loginOperate;


    @BeforeClass
    public void initDriver() {

        Assert.assertNotNull(driver);
        loginOperate = new LoginOperate(driver);
        operate = new AtentionOperate(driver);

    }


    @Test
    private void Login() throws InterruptedException {
        //这里先登录，
        boolean flag = loginOperate.login("13192624740", "xxxxx");

        //断言是否成功登录
        Assert.assertTrue(flag);
    }





}
