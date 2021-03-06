package com.bt;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.BtTabPage;
import com.pageutil.HomePage;
import com.pageutil.MediaPage;
import com.pageutil.NavBarPage;
import com.pageutil.SettingsPage;
import com.runutils.RunTestCase;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;


/**
 * case: 连接蓝牙后检查蓝牙音乐播放  100次
 * 命令：uiautomator runtest AutoTest.jar -c com.bt.BtConnectCheckSmsDisconnect
 * 前提：蓝牙已匹配并连接过一次，手机端允许读短信和联系人权限，当前已匹配列表少于5个
 * 步骤：进入蓝牙设置页，点击该设备，连接成功后进入蓝牙音乐，播放20秒，断开蓝牙
 * 期望：断开重连后蓝牙音乐正常播放，100次
 * 其他：设置蓝牙名称和测试次数,每个-e对应一个参数，-e DeviceName vivo -e TestTimes 50
 * uiautomator runtest AutoTest.jar -e TestTimes 200 -c com.bt.BtConnectCheckBtMusic &
 * */

public class BtConnectCheckBtMusic extends UiAutomatorTestCase {
	
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.bt.BtConnectCheckBtMusic", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.clickConfirmWatcher();
			
			String deviceNameStr = "";
			Bundle devicesNameBundle = getParams();//获取参数
			if (devicesNameBundle.getString("DeviceName") != null) {
				deviceNameStr = devicesNameBundle.getString("DeviceName");
			} else {
				deviceNameStr = "vivo";
			}
			Utils.logPrint("deviceNameStr = " + deviceNameStr);
			
			//获取测试次数
			long testTimes = 0;
			Bundle paramsBundle = getParams();//获取参数
			String resultStr = paramsBundle.getString("TestTimes");
			if (resultStr != null) {
				testTimes = Long.parseLong(resultStr);
			} else {
				testTimes = 100;
			}
			Utils.logPrint("testTimes = " + testTimes);
			
			btConnectCheckBtMusicTest(deviceNameStr, testTimes);		//
			
			UiDevice.getInstance().removeWatcher("confirmStopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	public boolean btConnectCheckBtMusicTest(String deviceNameStr, long testTimes) {
		
		boolean isTestPass = false;
		try {
			HomePage homePage = new HomePage();
			SettingsPage settingsPage = new SettingsPage();
			BtTabPage btTabPage = new BtTabPage();
			MediaPage mediaPage = new MediaPage();
			NavBarPage navBarPage = new NavBarPage();
			
			homePage.goBackHome();//home
			homePage.intoSettings();//settings
			settingsPage.intoBtTab();//btsetting
			
			
			boolean keepTesting = true;
			int testCounter = 0;
			int testPassCounter = 0;
			
			while(keepTesting){
				testCounter ++;
				
				Utils.logPrint("connect to " + deviceNameStr);
				if (btTabPage.connectBtDevice(deviceNameStr, BtTabPage.ALL_CONNECT)) {
					Utils.logPrint("connect ok ...");
					navBarPage.clickMedia();
					if (mediaPage.intoBtMusic()) {
						SystemClock.sleep(20000);
						if (mediaPage.SourceJudge() == MediaPage.BLUETOOTHTITLE) {
							Utils.logPrint("source == BLUETOOTHTITLE");
							testPassCounter ++;
							Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
							
							homePage.goBackHome();
							homePage.intoSettings();
							settingsPage.intoBtTab();
							if (btTabPage.connectBtDevice(deviceNameStr, BtTabPage.NONE_CONNECT)) {
								Utils.logPrint("disconnect ok ...");
								SystemClock.sleep(5000);
							} else {
								Utils.logPrint("disconnect fail ...");
							}
						}
					}
				} else {
					Utils.logPrint("connect fail ...");
//					keepTesting = false;
				}
				if (testCounter == testTimes) {
					if (testPassCounter == testCounter) {
						isTestPass = true;
					}
					btTabPage.connectBtDevice(deviceNameStr, BtTabPage.NONE_CONNECT);
					Utils.logPrint("Finish test ...");
					Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
					keepTesting = false;
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException,test fail:" + e.toString());
		}
		return isTestPass;
	}

	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("====case:BT connect check sms and disconnect======");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","====case:BT connect check sms and disconnect======");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
