package com.operation;

import com.base.OperateAppium;
import com.pages.HomePage;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class HomePageOperation extends OperateAppium {

    private HomePage homePage;

    AndroidDriver driver;

	public HomePageOperation(AndroidDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
		homePage = new HomePage(driver);
		this.driver = driver;
	}
	
	//进入home界面
	public boolean intoHomePage() {
		driver.pressKeyCode(AndroidKeyCode.HOME);
		if (homePage.isHome()) {
			return true;
		} else {
			return false;
		}
	}
	
	//进入设置界面
	public boolean intoSettingPage() {
		if (clickView(homePage.getHomeSettingButton())) {
			return true;
		} else {
			return false;
		}
	}
	
	//进入媒体界面
	public boolean intoMediaPage() {
		if (clickView(homePage.getHomeMediaButton())) {
			return true;
		} else {
			return false;
		}
	}
	//进入source界面
	public boolean intoSourcePage() {
		if (clickView(homePage.getBtSourceButton())) {
			return true;
		} else {
			return false;
		}
	}

}
