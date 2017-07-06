package com.bt;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.BtTabPage;
import com.pageutil.ContactPage;
import com.pageutil.HomePage;
import com.pageutil.NavBarPage;
import com.pageutil.PhonePage;
import com.pageutil.SettingsPage;
import com.runutils.RunTestCase;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;



/* 
 * case: 蓝牙断开重连后检查联系人协议   100次
 * 命令：uiautomator runtest AutoTest.jar -c com.bt.BtConnectCheckContactDisconnect
 * 前提：蓝牙已匹配并连接过一次，手机端允许读短信和联系人权限，
 * 		当前已匹配列表只有一个设备,当前未连接任何协议
 * 步骤：进入蓝牙设置页，点击该设备，连接成功进入联系人检查联系人是否下载，
 *       返回蓝牙设置页点击该蓝牙设备，断开蓝牙连接
 * 期望：能正常断开重连并下载联系人  100次
 * 其他：
 * */
public class BtConnectCheckContactDisconnect extends UiAutomatorTestCase {
	
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.bt.BtConnectCheckContactDisconnect", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	
	public void testBtConnectCheckContactDisconnect(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			//获取测试设备名称
			String deviceNameStr = "";
			Bundle devicesNameBundle = getParams();//获取参数
			if (devicesNameBundle.getString("DeviceName") != null) {
				deviceNameStr = devicesNameBundle.getString("DeviceName");
			} else {
				deviceNameStr = "PLK";
			}
			Utils.logPrint("deviceNameStr = " + deviceNameStr);
			
			//获取测试次数
			long testTimes = 0;
			if (devicesNameBundle.getString("TestTimes") != null) {
				String testTimesStr = devicesNameBundle.getString("TestTimes");
				testTimes = Long.parseLong(testTimesStr);
			} else {
				testTimes = 100;
			}
			Utils.logPrint("testTimes = " + testTimes);
			
			
			btConnectCheckContactDisconnectTest(testTimes, deviceNameStr);		//
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public boolean btConnectCheckContactDisconnectTest(long testTimes, String deviceNameStr) {
		boolean isTestPass = false;
		try {
			HomePage homePage = new HomePage();
			SettingsPage settingsPage = new SettingsPage();
			BtTabPage btTabPage = new BtTabPage();
			NavBarPage navBarPage = new NavBarPage();
			PhonePage phonePage = new PhonePage();
			ContactPage contactPage = new ContactPage();
			
			homePage.goBackHome();//home
			homePage.intoSettings();//settings
			settingsPage.intoBtTab();//btsetting
			
			
			boolean keepTesting = true;
			int testCounter = 0;
			int testPassCounter = 0;
			
			while(keepTesting){
				testCounter ++;
				try {
					Utils.logPrint("connect to " + deviceNameStr);
					if (btTabPage.connectBtDevice(deviceNameStr, BtTabPage.ALL_CONNECT)) {
						Utils.logPrint("connect ok ...");
						SystemClock.sleep(10000);
						navBarPage.clickPhone();
						
						if (phonePage.intoContact()) {
							Utils.logPrint("contact connect ok ...");
							if (contactPage.contactsComeout(120000)) {
								int contactCounter = contactPage.getConactsCounter(20000);
								Utils.logPrint("contacts Counter : " + contactCounter);
								
								homePage.goBackHome();
								homePage.intoSettings();
								settingsPage.intoBtTab();
								if (btTabPage.connectBtDevice(deviceNameStr, BtTabPage.NONE_CONNECT)) {
									testPassCounter ++;
									Utils.logPrint("disconnect ok ...");
									Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
									sleep(3000);
								} else {
									Utils.logPrint("disconnect fail ...");
									keepTesting = false;
								}
							} else {
								Utils.logPrint("Contact download fail ...");
								keepTesting = false;
							}
						}else {
							Utils.logPrint("Contact connect fail ...");
							keepTesting = false;
						}
					}
					if (testCounter == testTimes) {
						if (testPassCounter == testCounter) {
							isTestPass = true;
						}
						Utils.logPrint("Finish test ...");
						Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
						keepTesting = false;
					}
				} catch (UiObjectNotFoundException e) {
					Utils.logPrint("UiObjectNotFoundException:" + e.toString());
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException,test fail ...");
		}
		return isTestPass;		
	}

	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("===case:BT connect check contacts and disconnect==");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","===case:BT connect check contacts and disconnect==");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
