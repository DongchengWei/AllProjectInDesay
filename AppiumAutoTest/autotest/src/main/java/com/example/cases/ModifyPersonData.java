package com.example.cases;

import com.example.base.Assertion;
import com.example.base.InitAppium;
import com.example.operation.ModifyPersonDataOperate;
import com.example.operation.LoginOperate;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * 修改个人资料
 * Created by LITP on 2016/9/27.
 */

public class ModifyPersonData extends InitAppium {


    private LoginOperate loginOperate;

    private ModifyPersonDataOperate operate;

    @BeforeClass
    public void initDriver() {

        Assert.assertNotNull(driver);
        loginOperate = new LoginOperate(driver);
        operate = new ModifyPersonDataOperate(driver);
    }

    @Test
    public void Login() throws InterruptedException {
        //这里先登录，
        boolean flag = loginOperate.login("13192624740", "xxxxx");

        //断言是否成功登录
        Assert.assertTrue(flag);
    }


    /**
     * 是否进入用户资料页面
     */
    @Test
    private void testIntoPersonPage(){
        Assertion.verifyEquals(operate.verfiyIntoPage(),true,"验证是否进入了个人资料界面");
    }

    /**
     * 验证修改名字
     */
    @Test(priority = 1)
    private void testChangeName(){
        Assertion.verifyEquals(operate.verfiyChangeName("自动化测试"),true,"验证修改名字");
    }

    /**
     * 验证修改头像
     */
    @Test(priority = 2)
    private void testChangeAvatar(){
        Assertion.verifyEquals(operate.verfiyChangeAvatarTake(),true,"验证拍照修改头像");
        Assertion.verifyEquals(operate.verfiyChangeAvatarSelect(),true,"验证图库选择修改头像");
    }

    /**
     * 验证修改性别
     */
    @Test(priority = 3)
    private void testChangeSex(){
        Assertion.verifyEquals(operate.verfiyChangeSexMale(),true,"验证设置男头像");
        Assertion.verifyEquals(operate.verfiyChangeSexFemale(),true,"验证设置女头像");

    }
    /**
     * 验证修改出生日期
     */
    @Test(priority = 4)
    private void testChangeBirth(){
        Assertion.verifyEquals(operate.verfiyChangeBirth(),true,"验证设置女头像");

    }

    /**
     * 验证修改邮箱
     */
    @Test(priority = 5)
    private void testChangeEmail(){
        Assertion.verifyEquals(operate.verfiyChangeEmail("2269721300@qq.com"),true,"验证设置邮箱");
    }

    /**
     * 验证提交
     */
    @Test(priority = 6)
    private void testSubmit(){
        Assertion.verifyEquals(operate.verfiySubmit(),true,"验证提交");
    }


}
