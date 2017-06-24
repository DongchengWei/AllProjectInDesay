package com.operation;

import com.base.OperateAppium;
import com.pages.SettingsPage;

import io.appium.java_client.android.AndroidDriver;

public class SettingsPageOperation extends OperateAppium {

	
    private SettingsPage settingsPage;

    AndroidDriver driver;
    
    
	public SettingsPageOperation(AndroidDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
		settingsPage = new SettingsPage(driver);
		this.driver = driver;
	}
	
	/**
	 * 进入显示
	 * */
	public boolean intoDisplayTab() {
		return clickView(settingsPage.getDisplayTabBtn(), "显示按钮");
	}
	
	/**
	 * 点击语言设置
	 * */
	public boolean intoLanguageSetting() {
		return clickView(settingsPage.getLanguageSettingBtn(), "语言按钮");
	}
	/**
	 * 点击简体中文
	 * */
	public boolean selectChinese() {
		return clickView(settingsPage.getChineseBtn(), "简体中文");
	}
	/**
	 * 点击英文
	 * */
	public boolean selectEnglish() {
		return clickView(settingsPage.getEnglishBtn(), "English");
	}
	/**
	 * 判断是否成功设置成英文
	 * */
	public boolean isEnglish() {
		boolean isOk = false;
		String enString = settingsPage.getDisplayTabBtn().getText();
		print("设置后显示结果：" + enString);
		if (enString.equals("Display")) {
			isOk = true;
		}
		return isOk;
	}
	
	/**
	 * 点击WIFI Tab
	 * */
	public boolean intoWifiTab() {
		return clickView(settingsPage.getWifiTabBtn(), "Wifi Tab 按钮");
	}
	/**
	 * 判断开关是否打开
	 * */
	public boolean isWifiSwitchOn() {
		boolean isOk = false;
		isOk = Boolean.parseBoolean(settingsPage.getWifiSwitch().getAttribute("checked"));
		return isOk;
	}
	/**
	 * 打开wifi开关并判断是否成功打开，如果已经打开则直接返回true
	 * */
	public boolean turnOnWifiSwitch() {
		boolean isOk = false;
		
		boolean isOn = isWifiSwitchOn();
		if (isOn) {
			isOk = true;
		} else {
			isOk = clickView(settingsPage.getWifiSwitch(), "wifi开关");
		}
		
		return isOk;
	}
	/**
	 * 关闭wifi开关并判断是否成功关闭，如果已经关闭则直接返回true
	 * */
	public boolean turnOffWifiSwitch() {
		boolean isOk = false;
		
		boolean isOn = isWifiSwitchOn();
		if (isOn == false) {
			isOk = true;
		} else {
			isOk = clickView(settingsPage.getWifiSwitch(), "wifi开关");
		}
		return isOk;
	}

}
