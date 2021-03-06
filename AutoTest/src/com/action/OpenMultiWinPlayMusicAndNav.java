/**
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-06-03
 */
package com.action;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.MediaPage;
import com.pageutil.NavBarPage;
import com.pageutil.SettingsPage;
import com.pageutil.SysTabPage;
import com.runutils.RunTestCase;

import android.os.RemoteException;

/** 
* @author 作者 E-mail: Dongcheng.Wei@desay-svautomotive.com
* @version 创建时间：2017年6月3日 上午11:48:21 
* 类说明 :
*/
/**
 * @author uidq0460
 * uiautomator runtest AutoTest.jar -c com.action.OpenMultiWinPlayMusicAndNav &
 */
public class OpenMultiWinPlayMusicAndNav extends UiAutomatorTestCase {

	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.action.OpenMultiWinPlayMusicAndNav", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	}
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("=============case:MusicForHours===================");
		System.out.println("==================================================");
	}
	public void testDemo(){
		try {
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			assertEquals(true, openMultiWinPlayMusicAndNav());
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return
	 * @Date 2017-06-03
	 */
	private Object openMultiWinPlayMusicAndNav() {
		// TODO Auto-generated method stub
		HomePage homePage = new HomePage();
		SettingsPage settingsPage = new SettingsPage();
		SysTabPage sysTabPage = new SysTabPage();
		MediaPage mediaPage = new MediaPage();
		NavBarPage navBarPage = new NavBarPage();
		
		homePage.goBackHome();
		try {
			homePage.intoSettings();
			settingsPage.intoSystemTab();
			sysTabPage.turnOnMultiWin(3000);
			navBarPage.clickMedia();
			sleep(1000);
			mediaPage.intoBtMusic();
			navBarPage.clickNav();
			sleep(1000);
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("Error:UiObjectNotFoundException->" + e.toString());
		}
		
		
		return true;
	}
}
