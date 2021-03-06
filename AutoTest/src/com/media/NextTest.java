/**
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-06-17
 */
package com.media;

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
 * case:下一曲测试,默认进入USB1的音乐点击下一曲停留10秒，点击下一曲10次。
 * 前提：待进入的source需要先插上对应的设备。尽量让界面先进入FM。
 * 命令：uiautomator runtest AutoTest.jar -c com.media.NextTest &
 * 如果需要修改默认设置，则添加参数如下：
 * 参数设置： -e Source USB1 -e MediaType VIDEO -e TestTimes 100    -e StayTime 10
 * 			 第一音源FM，                             第二音源BT          测试次数100            停留时间10秒
 * Source（进入的音源）参数：LOCAL/USB1/USB2/SD  分别对应本地、usb1、usb2、sd
 * MediaType（进入的媒体类型）参数：MUSIC/VIDEO/IMAGE  分别对应 音乐、视频、图片
 * TestTimes(测试次数)参数：long类型
 * StayTime（停留时间）：下一曲后播放的时间，单位秒
 * uiautomator runtest AutoTest.jar  -e Source LOCAL -e MediaType MUSIC -e TestTimes 2 -e StayTime 5 -c com.media.NextTest &
 */
public class NextTest extends UiAutomatorTestCase {
	
	public static void main(String[] args) {
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.media.NextTest", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	}
	
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("============case:Video next  test=================");
		System.out.println("==================2017-06-07======================");
	}
	
	public void testNextTest(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			int sourceSel = 0;
			int mediaType = 0;
			
			String sourceSelectStr = "";
			Bundle testBundle = getParams();//获取参数
			if (testBundle.getString("Source") != null) {
				sourceSelectStr = testBundle.getString("Source");
			} else {
				sourceSelectStr = "USB1";
			}
			Utils.logPrint("source = " + sourceSelectStr);
			if (sourceSelectStr.equals("LOCAL")) {
				sourceSel = MediaPage.LOCALSOURCE;
			} else if (sourceSelectStr.equals("USB1")) {
				sourceSel = MediaPage.USB1SOURCE;
			} else if (sourceSelectStr.equals("USB2")) {
				sourceSel = MediaPage.USB2SOURCE;
			} else if (sourceSelectStr.equals("SD")) {
				sourceSel = MediaPage.SDSOURCE;
			}
			
			String mediaTypeStr = "";
			if (testBundle.getString("MediaType") != null) {
				mediaTypeStr = testBundle.getString("MediaType");
			} else {
				mediaTypeStr = "MUSIC";
			}
			Utils.logPrint("mediaTypeStr = " + mediaTypeStr);
			if (mediaTypeStr.equals("VIDEO")) {
				mediaType = MediaPage.TYPEVIDEO;
			} else if (mediaTypeStr.equals("MUSIC")) {
				mediaType = MediaPage.TYPEMUSIC;
			} else if (mediaTypeStr.equals("IMAGE")) {
				mediaType = MediaPage.TYPEIMAGE;
			}
			
			
			//获取测试次数
			long testTimes = 0;
			String resultStr = testBundle.getString("TestTimes");
			if (resultStr != null) {
				testTimes = Long.parseLong(resultStr);
			} else {
				testTimes = 100;
			}
			Utils.logPrint("testTimes = " + testTimes);
			
			//间隔时间
			long stayTimeL = 0;
			String stayTimeStr = testBundle.getString("StayTime");
			if (stayTimeStr != null) {
				stayTimeL = Long.parseLong(stayTimeStr);
			} else {
				stayTimeL = 10;
			}
			Utils.logPrint("stayTimeL = " + stayTimeL);
			
			assertEquals(true, NextPressureTest(sourceSel, mediaType, testTimes, stayTimeL));
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public boolean NextPressureTest(int sourceInt, int type, long testTimes, long statyTimeLong) {
		boolean isPass = false;
		
		HomePage homePage = new HomePage();
		MediaPage mediaPage = new MediaPage();
		
		try {
			homePage.goBackHome();
			homePage.intoMultimedia();
			if (mediaPage.intoSourceSelect()) {
				if (mediaPage.selectSource(sourceInt)) {
					if (mediaPage.selectSourceType(type)) {
						boolean keepTesting = true;
						int testCounter = 0;
						int testPassCounter = 0;
						if (type == MediaPage.TYPEVIDEO) {//视频
							while(keepTesting){
								testCounter ++;
								
								if (mediaPage.clickNextVideoBtn()) {
									testPassCounter ++;
									Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
									SystemClock.sleep(statyTimeLong * 1000);
								} 
								if (testCounter == testTimes) {
									keepTesting = false;
									if (testPassCounter == testCounter) {
										isPass = true;
										Utils.logForResult("Test pass:" + testPassCounter);
									}else {
										Utils.logForResult("Test fail:" + (testCounter - testPassCounter) + " times in " + testCounter);
									}
								}
							}
						} else if (type == MediaPage.TYPEMUSIC) {
							while(keepTesting){
								testCounter ++;
								
								if (mediaPage.clickNextMusicPot()) {
									testPassCounter ++;
									Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
									SystemClock.sleep(statyTimeLong * 1000);
								} 
								if (testCounter == testTimes) {
									keepTesting = false;
									if (testPassCounter == testCounter) {
										isPass = true;
										Utils.logForResult("Test pass:" + testPassCounter);
									}else {
										Utils.logForResult("Test fail:" + (testCounter - testPassCounter) + " times in " + testCounter);
									}
								}
							}
						} else if (type == MediaPage.TYPEIMAGE) {
							while(keepTesting){
								testCounter ++;
								
								if (mediaPage.clickNextImageBtn()) {
									testPassCounter ++;
									Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
									SystemClock.sleep(statyTimeLong * 1000);
								} 
								if (testCounter == testTimes) {
									keepTesting = false;
									if (testPassCounter == testCounter) {
										isPass = true;
										Utils.logForResult("Test pass:" + testPassCounter);
									}else {
										Utils.logForResult("Test fail:" + (testCounter - testPassCounter) + " times in " + testCounter);
									}
								}
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException:" + e.toString());
		}
		
		return isPass;
	}
}
