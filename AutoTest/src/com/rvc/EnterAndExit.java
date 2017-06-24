package com.rvc;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.ReversePage;
import com.runutils.RunTestCase;

import android.os.Bundle;
import android.os.RemoteException;


/** 
 * case: 进入退出倒车10000次
 * 命令：uiautomator runtest AutoTest.jar -c com.rvc.EnterAndExit &
 * 前提：当前在任意界面
 * 步骤：1.系统进入倒车  2.系统退出倒车
 * 期望：能正常进入退出倒车  10000次 
 * 其他：修改测试次数加上参数 -e TestTimes 次数
 * 如：uiautomator runtest AutoTest.jar  -e TestTimes 100 -c com.rvc.EnterAndExit &
 * 进入倒车失败（倒车界面的设置栏5秒不出现）则退出测试
 * Date:2017-03-29
 * */
public class EnterAndExit extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.rvc.EnterAndExit", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("========case:RVC enter and exit test==============");
		System.out.println("==================================================");
	}
	
	
	public void testRvcEnterExit(){
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
				testTimes = 10000;
			}
			Utils.logPrint("testTimes = " + testTimes);
			
			assertEquals(true, enterAndExitTest(testTimes));
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public boolean enterAndExitTest(long testTimes) {
		
		boolean isOk = false;
		
		ReversePage reversePage = new ReversePage();
		
		boolean keepTesting = true;
		int testCounter = 0;
		int testPassCounter = 0;
		while(keepTesting){
			testCounter ++;
			
			Utils.logPrint("Enter RVC...");
			if (reversePage.intoReversePage()) {
				Utils.logPrint("Enter RVC ok,exit RVC...");
				
				reversePage.exitReversePage();
				testPassCounter ++;
				Utils.logPrint("Exit RVC ok...");
				Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
			} else {
				Utils.logPrint("Enter RVC fail...");
				keepTesting = false;
			}
			if (testCounter == testTimes) {
				keepTesting = false;
				if (testPassCounter == testCounter) {
					isOk = true;
					reversePage.exitReversePage();
					Utils.logForResult("Test pass:" + testPassCounter);
				}else {
					Utils.logForResult("Test fail:" + (testCounter - testPassCounter) + " times in " + testCounter);
				}
			}
		}
		
		return isOk;
	}
}
