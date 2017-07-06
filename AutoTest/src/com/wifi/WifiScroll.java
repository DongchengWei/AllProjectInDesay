package com.wifi;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.SettingsPage;
import com.pageutil.WifiTabPage;
import com.runutils.RunTestCase;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

/**
 * @author uidq0460
 * Case: wifi打开-下滑列表到底-上滑列表到顶-关闭wifi	100次
 * 前提：
 * 操作：wifi打开-下滑列表到底-上滑列表到顶-关闭wifi	100次
 * 命令：uiautomator runtest AutoTest.jar --nohup -e TestTimes 100 -c com.wifi.WifiScroll
 * 其他：设置测试次数 -e TestTimes 100
 */
public class WifiScroll extends UiAutomatorTestCase {
	
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.wifi.WifiScroll", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator();
//		String logPathStr = runTestCase.runUiautomator();//返回文件路径
//		new LogUtil().analyseLog(logPathStr);//分析日志
	} 
	
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			//获取测试次数
			long testTimes = 0;
			Bundle paramsBundle = getParams();//获取参数
			String resultStr = paramsBundle.getString("TestTimes");
			if (resultStr != null) {
				testTimes = Long.parseLong(resultStr);
			} else {
				testTimes = 2;
			}
			Utils.logPrint("testTimes = " + testTimes);
			
			assertEquals(true, wifiScrollTest(testTimes)); 
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private boolean wifiScrollTest(long testTimes) {
		boolean isTestPass = false;
		HomePage homePage = new HomePage();
		SettingsPage settingsPage = new SettingsPage();
		WifiTabPage wifiPage = new WifiTabPage();
		
		homePage.goBackHome();
		try {
			homePage.intoSettings();
			settingsPage.intoWifiTab();
			
				
			boolean keepTesting = true;
			int testCounter = 0;
			int passCounter = 0;
			while(keepTesting){
				testCounter ++;
				
				if (wifiPage.turnOffWifi(5000)) {
					SystemClock.sleep(3000);
					if (wifiPage.turnOnWifi(5000)) {
						if (wifiPage.scrollToWifiListEnd()) {
							if (wifiPage.scrollToWifiListBegin()) {
								passCounter ++;
								Utils.logPrint("scroll up&down pass");
								Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
							}
						}
					}
				}
				if (testCounter == testTimes) {
					if (passCounter == testCounter) {
						isTestPass = true;//测试通过
					}
					keepTesting = false;
					Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
				}
			}
		} catch (UiObjectNotFoundException e1) {
			Utils.logPrint("UiObject not found" + e1.toString());
		}
		
		return isTestPass;
		
	}
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("===case:Wifi on & Auto connect & swithc connect===");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","===case:Wifi on & Auto connect & swithc connect===");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}