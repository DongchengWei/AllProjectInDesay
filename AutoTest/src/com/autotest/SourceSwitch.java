/**
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-06-07
 */
package com.autotest;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.MediaPage;
import com.runutils.RunTestCase;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;

/**
 * source音乐切换，如果不添加参数，默认是FM和BT切换，默认每次切换音乐停留5秒，默认循环5次
 * 前提：当前在任意界面，待切换的音源要先插入设备，比如蓝牙\ipod等
 * 步骤：1.进入选择的第一个音源停留5秒并检查ID3   2.进入第二个音源停留5秒并检查ID3
 * 命令：uiautomator runtest AutoTest.jar -c com.autotest.SourceSwitch &
 * 参数设置： -e FirstSource FM -e SecondSource BT -e TestTimes 100 -e StayTime 20
 * 			 第一音源FM，                             第二音源BT          测试次数100            停留时间10秒
 * 各个音源简写：FM/BT/USB1/USB2/SD/IPOD/AUX/LOCAL
 * 分别对应 收音/蓝牙/U盘1/U盘2/SD卡/IPOD/AUX/本地音乐
 * 如：uiautomator runtest AutoTest.jar  -e FirstSource FM -e SecondSource SD -c com.autotest.SourceSwitch &
 * 如果有fail则自动保存log到路径：/sdcard/AutoTest/sourceSwitchTest/fail的时刻
 */
public class SourceSwitch extends UiAutomatorTestCase {

	
	/**
	 * 
	 * @param args
	 * @Date 2017-06-07
	 */
	public static void main(String[] args) {
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.autotest.SourceSwitch", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	}
	
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("===========case:Source Switch test================");
		System.out.println("==================2017-06-07======================");
	}
	
	public void testSourceSwitch(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			
			String firstSourceStr = "";
			Bundle testBundle = getParams();//获取参数
			if (testBundle.getString("FirstSource") != null) {
				firstSourceStr = testBundle.getString("FirstSource");
			} else {
				firstSourceStr = "BT";
			}
			Utils.logPrint("firstSourceStr = " + firstSourceStr);
			
			String secondSourceStr = "";
			if (testBundle.getString("SecondSource") != null) {
				secondSourceStr = testBundle.getString("SecondSource");
			} else {
				secondSourceStr = "SD";
			}
			Utils.logPrint("secondSourceStr = " + secondSourceStr);
			
			
			//获取测试次数
			long testTimes = 0;
			String resultStr = testBundle.getString("TestTimes");
			if (resultStr != null) {
				testTimes = Long.parseLong(resultStr);
			} else {
				testTimes = 10;
			}
			Utils.logPrint("testTimes = " + testTimes);
			
			//间隔时间
			long stayTimeL = 0;
			String stayTimeStr = testBundle.getString("StayTime");
			if (stayTimeStr != null) {
				stayTimeL = Long.parseLong(stayTimeStr);
			} else {
				stayTimeL = 5;
			}
			Utils.logPrint("stayTimeL = " + stayTimeL);
			
			assertEquals(true, sourceSwitchTest(firstSourceStr, secondSourceStr, testTimes, stayTimeL));
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 切换source
	 * @param testTimes 测试次数
	 * @return
	 * @Date 2017-06-07
	 */
	public boolean sourceSwitchTest(String firstSourceStr, String secondSourceStr, long testTimes, long statyTimeLong) {
		boolean isOk = false;
		String caseStartTime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		String casePathStr = "/sdcard/AutoTest/sourceSwitchTest/caseStartTime_" + caseStartTime + "/";
		String delCasePathStr = "/sdcard/AutoTest/sourceSwitchTest/caseStartTime_" + caseStartTime;
		
		Utils.logPrint("Log path=" + casePathStr);
		long statyTime = statyTimeLong*1000;//停留时间
		
		HomePage homePage = new HomePage();
		MediaPage mediaPage = new MediaPage();
		
		try {
			homePage.goBackHome();
			Utils.takeScreenshotToPath(casePathStr, "goBackHome.png");
			homePage.intoMultimedia();
			Utils.takeScreenshotToPath(casePathStr, "intoMultimedia.png");
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException:sourceSwitchTest()");
			e.printStackTrace();
		}
		
		try {
			boolean keepTesting = true;
			int testCounter = 0;
			int testPassCounter = 0;
			boolean isFirstOk = false;
			boolean isSecondOk = false;
			while(keepTesting){
				testCounter ++;
				
				String stepTimeStr = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
				String stepPathStr = "/sdcard/AutoTest/sourceSwitchTest/caseStartTime_" + caseStartTime + "/caseCouter_" + stepTimeStr + "/";
				String deleteStr = "/sdcard/AutoTest/sourceSwitchTest/caseStartTime_" + caseStartTime + "/caseCouter_" + stepTimeStr;
//				Utils.markAndSaveLogs(false, "UiObjectNotFoundException", casePathStr);
				
				if (firstSourceStr.equals("LOCAL")) {//本地音乐
					if (mediaPage.intoLocalMusic()) {
						Utils.takeScreenshotToPath(stepPathStr, "intoLocalMusic.png");
						if (mediaPage.id3Check()) {
							Utils.takeScreenshotToPath(stepPathStr, "id3Check.png");
							SystemClock.sleep(statyTime);
							Utils.takeScreenshotToPath(stepPathStr, "SourceJudge.png");
							if (mediaPage.SourceJudge() == MediaPage.STORAGETITLE) {
								isFirstOk = true;
							}
						}
					}
				} else if (firstSourceStr.equals("USB1")) {//USB1音乐
					if (mediaPage.intoUsb1Music()) {
						Utils.takeScreenshotToPath(stepPathStr, "intoUsb1Music.png");
						if (mediaPage.id3Check()) {
							Utils.takeScreenshotToPath(stepPathStr, "id3Check.png");
							SystemClock.sleep(statyTime);
							Utils.takeScreenshotToPath(stepPathStr, "SourceJudge.png");
							if (mediaPage.SourceJudge() == MediaPage.USB1TITLE) {
								isFirstOk = true;
							}
						}
					}
				} else if (firstSourceStr.equals("USB2")) {//USB2音乐
					if (mediaPage.intoUsb2Music()) {
						Utils.takeScreenshotToPath(stepPathStr, "intoUsb2Music.png");
						if (mediaPage.id3Check()) {
							Utils.takeScreenshotToPath(stepPathStr, "id3Check.png");
							SystemClock.sleep(statyTime);
							Utils.takeScreenshotToPath(stepPathStr, "SourceJudge.png");
							if (mediaPage.SourceJudge() == MediaPage.USB2TITLE) {
								isFirstOk = true;
							}
						}
					}
				} else if (firstSourceStr.equals("SD")) {//SD音乐
					if (mediaPage.intoSdMusic()) {
						Utils.takeScreenshotToPath(stepPathStr, "intoSdMusic.png");
						if (mediaPage.id3Check()) {
							Utils.takeScreenshotToPath(stepPathStr, "id3Check.png");
							SystemClock.sleep(statyTime);
							Utils.takeScreenshotToPath(stepPathStr, "SourceJudge.png");
							if (mediaPage.SourceJudge() == MediaPage.SDCARDTITLE) {
								isFirstOk = true;
							}
						}
					}
				} else if (firstSourceStr.equals("FM")) {//FM
					if (mediaPage.intoFmAmPage()) {
						Utils.takeScreenshotToPath(stepPathStr, "intoFmAmPage.png");
						SystemClock.sleep(statyTime);
						Utils.takeScreenshotToPath(stepPathStr, "SourceJudge.png");
						if (mediaPage.SourceJudge() == MediaPage.FMAMTITLE) {
							isFirstOk = true;
						}
					}
				} else if (firstSourceStr.equals("BT")) {//BT音乐
					if (mediaPage.intoBtMusic()) {
						Utils.takeScreenshotToPath(stepPathStr, "intoBtMusic.png");
						if (mediaPage.id3Check()) {
							Utils.takeScreenshotToPath(stepPathStr, "id3Check.png");
							SystemClock.sleep(statyTime);
							Utils.takeScreenshotToPath(stepPathStr, "SourceJudge.png");
							if (mediaPage.SourceJudge() == MediaPage.BLUETOOTHTITLE) {
								isFirstOk = true;
							}
						}
					}
				} else if (firstSourceStr.equals("IPOD")) {//IPOD音乐
					if (mediaPage.intoIpodMusic()) {
						Utils.takeScreenshotToPath(stepPathStr, "intoBtMusic.png");
						if (mediaPage.id3Check()) {
							Utils.takeScreenshotToPath(stepPathStr, "id3Check.png");
							SystemClock.sleep(statyTime);
							Utils.takeScreenshotToPath(stepPathStr, "SourceJudge.png");
							if (mediaPage.SourceJudge() == MediaPage.IPODTITLE) {
								isFirstOk = true;
							}
						}
					}
				} else if (firstSourceStr.equals("AUX")) {//AUX音乐
					if (mediaPage.intoAuxMusic()) {
						Utils.takeScreenshotToPath(stepPathStr, "intoAuxMusic.png");
						SystemClock.sleep(statyTime);
						Utils.takeScreenshotToPath(stepPathStr, "SourceJudge.png");
						if (mediaPage.SourceJudge() == MediaPage.AUXTITLE) {
							isFirstOk = true;
						}
					}
				} else {
					Utils.logPrint("firstSource:No such source,please check");
					isFirstOk = false;
				}
				
				if (isFirstOk) {
					if (secondSourceStr.equals("LOCAL")) {//本地音乐
						if (mediaPage.intoLocalMusic()) {
							Utils.takeScreenshotToPath(stepPathStr, "step2_intoLocalMusic.png");
							if (mediaPage.id3Check()) {
								Utils.takeScreenshotToPath(stepPathStr, "step2_id3Check.png");
								SystemClock.sleep(statyTime);
								Utils.takeScreenshotToPath(stepPathStr, "step2_SourceJudge.png");
								if (mediaPage.SourceJudge() == MediaPage.STORAGETITLE) {
									isSecondOk = true;
								}
							}
						}
					} else if (secondSourceStr.equals("USB1")) {//USB1音乐
						if (mediaPage.intoUsb1Music()) {
							Utils.takeScreenshotToPath(stepPathStr, "step2_intoUsb1Music.png");
							if (mediaPage.id3Check()) {
								Utils.takeScreenshotToPath(stepPathStr, "step2_id3Check.png");
								SystemClock.sleep(statyTime);
								Utils.takeScreenshotToPath(stepPathStr, "step2_SourceJudge.png");
								if (mediaPage.SourceJudge() == MediaPage.USB1TITLE) {
									isSecondOk = true;
								}
							}
						}
					} else if (secondSourceStr.equals("USB2")) {//USB2音乐
						if (mediaPage.intoUsb2Music()) {
							Utils.takeScreenshotToPath(stepPathStr, "step2_intoUsb2Music.png");
							if (mediaPage.id3Check()) {
								Utils.takeScreenshotToPath(stepPathStr, "step2_id3Check.png");
								SystemClock.sleep(statyTime);
								Utils.takeScreenshotToPath(stepPathStr, "step2_SourceJudge.png");
								if (mediaPage.SourceJudge() == MediaPage.USB2TITLE) {
									isSecondOk = true;
								}
							}
						}
					} else if (secondSourceStr.equals("SD")) {//SD音乐
						if (mediaPage.intoSdMusic()) {
							Utils.takeScreenshotToPath(stepPathStr, "step2_intoSdMusic.png");
							if (mediaPage.id3Check()) {
								Utils.takeScreenshotToPath(stepPathStr, "step2_id3Check.png");
								SystemClock.sleep(statyTime);
								Utils.takeScreenshotToPath(stepPathStr, "step2_SourceJudge.png");
								if (mediaPage.SourceJudge() == MediaPage.SDCARDTITLE) {
									isSecondOk = true;
								}
							}
						}
					} else if (secondSourceStr.equals("FM")) {//FM
						if (mediaPage.intoFmAmPage()) {
							Utils.takeScreenshotToPath(stepPathStr, "step2_intoFmAmPage.png");
							SystemClock.sleep(statyTime);
							Utils.takeScreenshotToPath(stepPathStr, "step2_SourceJudge.png");
							if (mediaPage.SourceJudge() == MediaPage.FMAMTITLE) {
								isSecondOk = true;
							}
						}
					} else if (secondSourceStr.equals("BT")) {//BT音乐
						if (mediaPage.intoBtMusic()) {
							Utils.takeScreenshotToPath(stepPathStr, "step2_intoBtMusic.png");
//							if (mediaPage.id3Check()) {
								SystemClock.sleep(statyTime);
								Utils.takeScreenshotToPath(stepPathStr, "step2_SourceJudge.png");
								if (mediaPage.SourceJudge() == MediaPage.BLUETOOTHTITLE) {
									isSecondOk = true;
								}
//							}
						}
					} else if (secondSourceStr.equals("IPOD")) {//IPOD音乐
						if (mediaPage.intoIpodMusic()) {
							Utils.takeScreenshotToPath(stepPathStr, "step2_intoIpodMusic.png");
							if (mediaPage.id3Check()) {
								Utils.takeScreenshotToPath(stepPathStr, "step2_id3Check.png");
								SystemClock.sleep(statyTime);
								Utils.takeScreenshotToPath(stepPathStr, "step2_SourceJudge.png");
								if (mediaPage.SourceJudge() == MediaPage.IPODTITLE) {
									isSecondOk = true;
								}
							}
						}
					} else if (secondSourceStr.equals("AUX")) {//AUX音乐
						if (mediaPage.intoAuxMusic()) {
							Utils.takeScreenshotToPath(stepPathStr, "step2_intoAuxMusic.png");
							SystemClock.sleep(statyTime);
							Utils.takeScreenshotToPath(stepPathStr, "step2_SourceJudge.png");
							if (mediaPage.SourceJudge() == MediaPage.AUXTITLE) {
								isSecondOk = true;
							}
						}
					} else {
						Utils.logPrint("firstSource:No such source,please check");
						isSecondOk = false;
					}
					
					if (isSecondOk) {
						testPassCounter ++;
						Utils.deleteDir(deleteStr);
						Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
					} else {
						Utils.logPrint("into " + secondSourceStr + " fail");
						Utils.markAndSaveLogs(isFirstOk, "intoSourcefail", stepPathStr);
						keepTesting = false;
					}
					
				} else {
					Utils.logPrint("into " + firstSourceStr + " fail");
					Utils.markAndSaveLogs(isFirstOk, "intoSourcefail", stepPathStr);
					keepTesting = false;
				}
				
				if (testCounter == testTimes) {
					keepTesting = false;
					if (testPassCounter == testCounter) {
						Utils.deleteDir(delCasePathStr);
						isOk = true;
						Utils.logForResult("Test pass:" + testPassCounter);
					}else {
						Utils.logForResult("Test fail:" + (testCounter - testPassCounter) + " times in " + testCounter);
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.markAndSaveLogs(false, "UiObjectNotFoundException", casePathStr);
			Utils.logPrint("UiObjectNotFoundException:sourceSwitchTest()" + e.toString());
		}
		
		return isOk;
	}

}
