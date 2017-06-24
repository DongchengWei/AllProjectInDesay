package com.example.cases;

import com.example.base.Assertion;
import com.example.base.InitAppium;
import com.example.operation.InspectionOperate;
import com.example.operation.LoginOperate;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by LITP on 2016/9/30.
 */

public class Inspection extends InitAppium {


    private LoginOperate loginOperate;
    private InspectionOperate operate;


    @BeforeClass
    public void initDriver() {

        Assert.assertNotNull(driver);
        loginOperate =new LoginOperate(driver);
        operate = new InspectionOperate(driver);

    }


    @Test
    public void Login() throws InterruptedException {
        //这里先登录，
        boolean flag = loginOperate.login("13192624740", "xxxxx");

        //断言是否成功登录
        Assert.assertTrue(flag);
    }



    @Test(priority = 1)
    public void openInspction(){
        //这里先登录，
        boolean flag = operate.intoInspection();

        //断言是否成功登录
        Assertion.verifyEquals(flag,true,"是否进入搜索界面");
    }

    @Test(priority = 2)
    public void search(){
        //Assertion.verifyEquals(operate.searchContent("攻击"),true,"搜索是否点击成功!");
    }

}
