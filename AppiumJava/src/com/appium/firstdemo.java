package com.appium;

import io.appium.java_client.AppiumDriver;  
import org.junit.After;  
import org.junit.Before;  
import org.junit.Test;  
import org.openqa.selenium.By;  
import org.openqa.selenium.WebElement;  
import org.openqa.selenium.remote.CapabilityType;  
import org.openqa.selenium.remote.DesiredCapabilities;  

import java.net.URL;  

public class firstdemo {

	   
    private AppiumDriver<WebElement> driver;  
   
    @Before  
    public void setUp() throws Exception {  
        // set up appium    
        DesiredCapabilities capabilities = new DesiredCapabilities();  
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");  
        capabilities.setCapability("platformName", "Android");  
        capabilities.setCapability("deviceName","2e6408f0344c0122");//设备名称jacinto6evm
        capabilities.setCapability("platformVersion", "5.1");  
        capabilities.setCapability("appPackage", "com.android.launcher");  
        capabilities.setCapability("appActivity", "com.thundersoft.hmi.TSLauncher");  
//        capabilities.setCapability("appPackage", "com.svauto.fastrvc");  
//        capabilities.setCapability("appActivity", ".FastRvcActivity");  
        driver = new AppiumDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);  
    }  
   
    @After  
    public void tearDown() throws Exception {  
        driver.quit();  
    }  
   
    @Test  
    public void addContact(){  
    	
//    	WebElement el = driver.findElement(By.id("com.svauto.fastrvc:id/isTabImage")); 
    	
        WebElement el = driver.findElement(By.id("com.android.launcher:id/ts_home_button_item_settings_id"));  
        el.click();  
//        List<WebElement> textFieldsList = driver.findElementsByClassName("android.widget.EditText");  
//        textFieldsList.get(0).sendKeys("Some Name");  
//        textFieldsList.get(2).sendKeys("Some@example.com");  
//        driver.findElementByName("Save").click();  
    }  
}
