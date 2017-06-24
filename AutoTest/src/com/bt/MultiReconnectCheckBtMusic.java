/**
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-05-09
 */
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

import android.os.RemoteException;
import android.os.SystemClock;

/** 
* @author 作者 E-mail: Dongcheng.Wei@desay-svautomotive.com
* @version 创建时间：2017年5月9日 下午4:20:23 
* 类说明 :
* case: 蓝牙连接断开 10次后检查蓝牙音乐播放状态，执行并检查10次
* 命令：uiautomator runtest AutoTest.jar -c com.bt.MultiReconnectCheckBtMusic
* 前提：蓝牙已匹配并连接过一次，手机端允许读短信和联系人权限，
* 		当前已匹配列表只有一个设备,当前未连接任何协议
* 步骤：进入蓝牙设置页，点击该设备，连接成功点击该蓝牙设备，断开蓝牙连接
* 期望：能正常断开重连 100次
* 其他：设置蓝牙名称和测试次数,每个-e对应一个参数
* uiautomator runtest AutoTest.jar -e DeviceName Nexus -c com.bt.MultiReconnectCheckBtMusic
*/

public class MultiReconnectCheckBtMusic extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.bt.MultiReconnectCheckBtMusic", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator();
//		String logPathStr = runTestCase.runUiautomator();//返回文件路径
//		new LogUtil().analyseLog(logPathStr);//分析日志
	} 
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("========case:MultiReconnectCheckBtMusic============");
		System.out.println("==================================================");
	}
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			assertEquals(true, multiReconnectCheckBtMusic());

			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return
	 * @Date 2017-05-09
	 */
	private Object multiReconnectCheckBtMusic() {
		// TODO Auto-generated method stub
		boolean testPass = false;
		
		String nowtimeStr = "";
		nowtimeStr = Utils.getNowTime();
		Utils.logPrint("start test time:" + nowtimeStr);
		
		String deviceNameStr = "S7";
		
		try {
			HomePage homePage = new HomePage();
			SettingsPage settingsPage = new SettingsPage();
			MediaPage mediaPage = new MediaPage();
			BtTabPage btTabPage = new BtTabPage();
			NavBarPage navBarPage = new NavBarPage();
			
			homePage.goBackHome();		//home
			homePage.intoSettings();
			settingsPage.intoBtTab();	//btsetting
			boolean keepTesting = true;
			long testCounter = 0;
			long testPassCounter = 0;
			
			int oneTestCounter = 0;
			while(keepTesting){
				testCounter ++;
				
				if (btTabPage.connectBtDevice(deviceNameStr, BtTabPage.NONE_CONNECT)) {
					Utils.logPrint("disconnect ok ...");
					SystemClock.sleep(10000);
					if (btTabPage.connectBtDevice(deviceNameStr, BtTabPage.ALL_CONNECT)) {
						Utils.logPrint("reconnect ok ...");
						SystemClock.sleep(10000);
						oneTestCounter ++;
						testPassCounter ++;
						Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
						
						if (oneTestCounter == 10) {
							oneTestCounter = 0;
							
							Utils.logPrint("Check BT music ...");
							navBarPage.clickMedia();
							SystemClock.sleep(1000);
							if (mediaPage.intoBtMusic()) {
								if (mediaPage.isPlaying()) {
									SystemClock.sleep(3000);
									homePage.goBackHome();		//home
									homePage.intoSettings();
									settingsPage.intoBtTab();	//btsetting
								}
							}
						}
					}
				}
				if (testCounter == 100) {
					if (testPassCounter == testCounter) {
						testPass = true;//测试通过
					}
					keepTesting = false;
					Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException:" + e.toString());
		}
		
		
		Utils.logPrint("Start test time:" + nowtimeStr);
		Utils.logPrint("End test time:" + Utils.getNowTime());
		
		return testPass;
	}
}
