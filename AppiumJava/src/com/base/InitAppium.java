package com.base;


import org.apache.http.util.TextUtils;
import org.junit.BeforeClass;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Listeners;

import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
 * 测试用例的父类
 * Created by LITP on 2016/9/7.
 */
@Listeners({com.base.AssertionListener.class})
public class InitAppium {
	
    //调试设备名字
    public static String deviceName = "2e6408f0344c0122";
    //调试设备系统版本
    public static String platformVersion = "5.1.1";
    //app路径
//    public static String appPath = System.getProperty("user.dir") + "/src/main/java/apps/shouhu2.2.3.apk";

    //包名
    public static String appPackage = "com.android.launcher";

    //是否需要重新安装
//    public static String noReset = "True";

    //是否不重新签名
//    public static String noSign = "True";

    //是否使用unicode输入法，真是支持中文
    public static String unicodeKeyboard = "True";

    //是否祸福默认呢输入法
    public static String resetKeyboard = "True";

    //要启动的Activity
    //public static String appActivity = appPackage + ".activity.login.WelcomeActivity";
    public static String appActivity = "com.thundersoft.hmi.TSLauncher";

    public static  AndroidDriver<AndroidElement> driver = null;
    
    public static String udid = "2e6408f0344c0122";
    public static String port = "6668";

    //构造方法
    public InitAppium() {
        this(new Builder());
    }

    public InitAppium(Builder builder) {

        appActivity = builder.appActivity;
        appPackage = builder.appPackage;
//        appPath = builder.appPath;
        deviceName = builder.deviceName;
//        noReset = builder.noReset;
//        noSign = builder.noSign;
        unicodeKeyboard = builder.unicodeKeyboard;
        resetKeyboard = builder.resetKeyboard;
        
        udid = builder.udid;
        port = builder.port;
    }

    public interface OnInitOperate{
    	public abstract void initOperate();
    }
    
    
    /**
     * appium启动参数
     * @return 
     * @return 
     *
     * @throws MalformedURLException
     */
    @BeforeClass
    public void initDriver() throws MalformedURLException {


        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformVersion", platformVersion);
//        capabilities.setCapability("app", new File(appPath).getAbsolutePath());
        capabilities.setCapability("appPackage", appPackage);
        //支持中文
        capabilities.setCapability("unicodeKeyboard", unicodeKeyboard);
        //运行完毕之后，变回系统的输入法
        capabilities.setCapability("resetKeyboard", resetKeyboard);
        //不重复安装
//        capabilities.setCapability("noReset", noReset);
        //不重新签名
//        capabilities.setCapability("noSign", noSign);
        //打开的activity
        if(!TextUtils.isEmpty(appActivity)){
            capabilities.setCapability("appActivity", appActivity);
        }
        
        if (!TextUtils.isEmpty(udid)) {
        	capabilities.setCapability("udid", udid);
        	capabilities.setCapability("deviceName", udid);
		}
        print("udid:" + udid);
        print("port:" + port);
        //启动Driver
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:" + port + "/wd/hub"), capabilities);
        
        print("初始化driver:" + (driver != null));
        

    }
 
    @AfterTest
    public void afterTest() {
//        driver.quit();
    }

    @AfterClass
    public void afterClass(){
        //每一个用例完毕结束这次测试
        driver.quit();
    }

    /**
     * 打印字符
     *
     * @param str 要打印的字符
     */
    public <T> void print(T str) {
        if (!TextUtils.isEmpty(String.valueOf(str))) {
            System.out.println(str);
        } else {
            System.out.println("输出了空字符");
        }
    }

	public void setOnInitOperate(InitAppium initAppium) {
		// TODO Auto-generated method stub
		try {
			initAppium.initDriver();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			print("initDriver:MalformedURLException");
			e.printStackTrace();
		}
	}
}
