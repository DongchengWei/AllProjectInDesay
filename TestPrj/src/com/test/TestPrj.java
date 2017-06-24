package com.test;

import java.io.IOException;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.runutils.RunTestCase;

import android.os.RemoteException;
import android.util.Log;

public class TestPrj extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("TestPrj",  
				 "com.test.TestPrj", "", "2");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	
	public void testDemo(){
		try{
			PrintInfo();
			getUiDevice().wakeUp();
			getUiDevice().pressHome();
			try {
				new UiObject(new UiSelector()
						.resourceId("com.android.launcher:id/ts_home_button_item_settings_id")).click();
				new UiObject(new UiSelector()
						.resourceId("com.android.settings:id/ts_tab_bluetooth_id")).click();						
				UiObject hfpObj = new UiObject(new UiSelector().resourceId("com.android.settings:id/item_bt_icon_phone"));	
				UiObject a2dpObj = new UiObject(new UiSelector().resourceId("com.android.settings:id/item_bt_icon_music"));
				if (a2dpObj.exists() && hfpObj.exists()) {
					logAndPrint("A2DP and HFP is connected");
				}else if (a2dpObj.exists()) {
					logAndPrint("A2DP is connected");
				}else if (hfpObj.exists()) {
					logAndPrint("HFP is connected");
				}else {
					logAndPrint("none connected");
				}
			} catch (UiObjectNotFoundException e) {
				logAndPrint("TestPrj Test fail:UiObjectNotFoundException");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	
	public void logForResult(String str) {
		System.out.println("=========>>>>" + str + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		Log.d("TESTRESULT","=========>>>>" + str + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}
	
	
	//控制台输出和logcat输出信息
	public void logAndPrint(String str){
		System.out.println("=========" + str);
		Log.d("BLUETOOTRESULT","=========" + str);
	}
	
	//输出版本信息
	public void PrintInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android Bluetooth AutoTest v0.0.1======");
		System.out.println("=========       2016-12-30       =================");
		System.out.println("=========        DesaySV         =================");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android Bluetooth AutoTest v0.0.1======");
		Log.d("BLUETOOTHAUTOTEST","=========       2016-12-30       =================");
		Log.d("BLUETOOTHAUTOTEST","=========        DesaySV         =================");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
