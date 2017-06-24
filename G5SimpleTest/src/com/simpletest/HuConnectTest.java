package com.simpletest;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.utils.PrintUtil;

import android.os.RemoteException;

public class HuConnectTest extends UiAutomatorTestCase {
	
	public void testDemo() {
		
		try {
			getUiDevice().wakeUp();
			getUiDevice().pressHome();
			try {
				//进入设置界面
				new UiObject(new UiSelector().
						resourceId("com.android.launcher:id/ts_home_button_item_settings_id")).clickAndWaitForNewWindow(2000);
				new UiObject(new UiSelector().resourceId("com.android.settings:id/ts_tab_time_id")).click();//点击时间
				
			} catch (UiObjectNotFoundException e) {
				PrintUtil.logAndPrint("UiObject not found:settings");
			}
		} catch (RemoteException e) {
			System.out.println("RemoteException");
		}
	}
}
