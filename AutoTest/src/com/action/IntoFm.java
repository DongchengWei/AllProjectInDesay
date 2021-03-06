/**
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-06-22
 */
package com.action;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.MediaPage;
import com.pageutil.UpdatePage;
import com.runutils.RunTestCase;

import android.os.RemoteException;

/** 
* @author 作者 E-mail: Dongcheng.Wei@desay-svautomotive.com
* @version 创建时间：2017年6月22日 下午9:01:28 
* 类说明 :
* case: 进入FM
* 命令：uiautomator runtest AutoTest.jar -c com.action.IntoFm
* 前提：
* 步骤：如果当前在刚升完级的界面，则点击确定进入系统后再进入FM。
* 期望：进入FM
*/
public class IntoFm extends UiAutomatorTestCase{

	/**
	 * 
	 * @param args
	 * @Date 2017-06-22
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.action.IntoFm", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	}
	
	public void testIntoFm() {
		try {
			UiDevice.getInstance().wakeUp();
			Utils.stopRunningWatcher();
			
			assertEquals(true, actionIntoFm());
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 进入FM界面
	 * @return
	 * @Date 2017-06-22
	 */
	public boolean actionIntoFm() {
		boolean isOk = false;
		
		HomePage homePage = new HomePage();
		MediaPage mediaPage = new MediaPage();
		UpdatePage updatePage = new UpdatePage();
		
		try {
			Utils.logPrint("Wait for confirm button into system...");
			if (updatePage.waitForConfirmBtnExists(5000)) {//刚升级完的系统弹出提示框需要点确认才进入系统
				Utils.logPrint("Confirm button comeout, into system...");
				updatePage.clickConfirmIntoSys();
				homePage.goBackHome();
				if (homePage.intoMultimedia()) {
					if (mediaPage.intoFmAmPage()) {
						isOk = true;
					} else {
						Utils.logPrint("intoFmAmPage fail");
					}
				} else {
					Utils.logPrint("intoMultimedia fail");
				}
			} else { //不是刚升级完，本身已进入系统
				homePage.goBackHome();
				if (homePage.intoMultimedia()) {
					if (mediaPage.intoFmAmPage()) {
						isOk = true;
					} else {
						Utils.logPrint("intoFmAmPage fail");
					}
				} else {
					Utils.logPrint("intoMultimedia fail");
				}
			}
		} catch (UiObjectNotFoundException e) {
			e.printStackTrace();
		}
		return isOk;
	}

}
