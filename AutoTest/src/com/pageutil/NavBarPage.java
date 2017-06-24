package com.pageutil;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;

import android.os.SystemClock;

/*
 * 左侧导航栏操作
 * */
public class NavBarPage extends UiAutomatorTestCase {

	//点击返回
	public boolean clickBack() {
		if (UiDevice.getInstance().click(75, 150)) {
			SystemClock.sleep(500);
			return true;
		} else {
			return false;
		}
	}
	//点击导航
	public boolean clickNav() {
		if (UiDevice.getInstance().click(75, 275)) {
			SystemClock.sleep(500);
			return true;
		} else {
			return false;
		}
	}
	//点击多媒体
	public boolean clickMedia() {
		Utils.getCurrentMethodName();
		if (UiDevice.getInstance().click(75, 395)) {
			SystemClock.sleep(500);
			return true;
		} else {
			return false;
		}
	}
	//点击phone
	public boolean clickPhone() {
		if (UiDevice.getInstance().click(75, 510)) {
			SystemClock.sleep(500);
			return true;
		} else {
			return false;
		}
	}
	//点击快捷方式
	public boolean clickShortcut() {
		if (UiDevice.getInstance().click(75, 620)) {
			SystemClock.sleep(500);
			return true;
		} else {
			return false;
		}
	}
}
