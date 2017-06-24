package com.simpletest;

import java.io.IOException;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.utils.PrintUtil;
import com.utils.RunTestCase;

import android.os.RemoteException;

public class SimpleTest extends UiAutomatorTestCase {
	
	String BLUETOOTH_DEVICE_NAME = "testphone";			//待测手机蓝牙名字
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("SimpleTest",  
				 "com.simpletest.SimpleTest", "", "2");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	
	public void testDemo() {
		
		try {
			PrintUtil.PrintInfo();
			getUiDevice().wakeUp();
			getUiDevice().pressHome();
				
			pairedCheckTest();									//1.pairedCheckTest
			huConnectTest();									//2.Hu connect test
			
		} catch (RemoteException e) {
			System.out.println("RemoteException");
		}
	}

	
	//2.huConnectTest
	private void huConnectTest() {
		try {
			//查找可用设备中是否存在目标设备,并点击设备进行匹配
			UiScrollable deviceList = new UiScrollable( new UiSelector().
					resourceId("com.android.settings:id/ts_bluetooth_pair_dev_list"));
			UiObject targetDevice = deviceList.getChildByText(new UiSelector().resourceId("com.android.settings:id/ts_bluetooth_device_name"), BLUETOOTH_DEVICE_NAME, true);
			targetDevice.clickAndWaitForNewWindow();
			sleep(1000);
			UiObject connOkObj = deviceList.getChildByText(new UiSelector().resourceId("com.android.settings:id/ts_bluetooth_device_status"), "(已连接)", true);
			connOkObj.waitForExists(30000);
			if (connOkObj.exists()) {
				PrintUtil.resultInfoprint("huConnectTest-->pass");
			}else {
				PrintUtil.resultInfoprint("huConnectTest-->failed");
			}
		} catch (UiObjectNotFoundException e) {
			// TODO Auto-generated catch block
			PrintUtil.resultInfoprint("huConnectTest failed: UiObject not found");
		}
	}

	//1.pairedCheckTest
	public void pairedCheckTest(){
		try {
			//进入设置界面
			new UiObject(new UiSelector().
					resourceId("com.android.launcher:id/ts_home_button_item_settings_id")).clickAndWaitForNewWindow(2000);
			new UiObject(new UiSelector().resourceId("com.android.settings:id/ts_tab_bluetooth_id")).click();//进入蓝牙
			
			//查找可用设备中是否存在目标设备,并点击设备进行匹配
			UiScrollable deviceList = new UiScrollable( new UiSelector().
					resourceId("com.android.settings:id/ts_bluetooth_dev_list"));
			UiObject targetDevice = deviceList.getChildByText(new UiSelector().resourceId("com.android.settings:id/ts_bluetooth_device_name"), BLUETOOTH_DEVICE_NAME, true);
			targetDevice.waitForExists(30000);
			if (targetDevice.exists()) {
				PrintUtil.logAndPrint(BLUETOOTH_DEVICE_NAME + " Exists in device list");
				targetDevice.clickAndWaitForNewWindow();//点击配对
				PrintUtil.logAndPrint("Plz confirm via Phone in 10s");
				sleep(10000);

				UiScrollable devicePairedList = new UiScrollable( new UiSelector().
						resourceId("com.android.settings:id/ts_bluetooth_pair_dev_list"));
				UiObject targetPairedDevice = devicePairedList.getChildByText(new UiSelector().resourceId("com.android.settings:id/ts_bluetooth_device_name"), BLUETOOTH_DEVICE_NAME,true);
				targetPairedDevice.waitForExists(30000);
				if (targetPairedDevice.exists()) {
					PrintUtil.resultInfoprint("Paired Check-->pass");
				}else {
					PrintUtil.resultInfoprint("Paired Check-->fail");
				}
			}else {
				PrintUtil.logAndPrint(BLUETOOTH_DEVICE_NAME + " not exists in device list,Plz check later");
			}
			
		} catch (UiObjectNotFoundException e) {
			PrintUtil.resultInfoprint("pairedCheckTest failed: UiObject not found");
		}
	}
}
