package com.auto;

import java.io.IOException;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.runutils.RunTestCase;

import android.os.RemoteException;

public class FirstTest extends UiAutomatorTestCase {
	
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("au",  
				 "com.auto.FirstTest", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator();//返回文件路径
	}
	
	public void testDemo(){
		try{
			getUiDevice().wakeUp();		//唤醒设备
			getUiDevice().pressHome();	//返回home界面
			try {
				new UiObject(new UiSelector()
						.resourceId("com.android.launcher:id/ts_home_button_item_settings_id"))
						.click();
			} catch (UiObjectNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
