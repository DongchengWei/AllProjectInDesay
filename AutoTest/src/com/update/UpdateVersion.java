/**
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-06-22
 */
package com.update;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.SettingsPage;
import com.pageutil.SysTabPage;
import com.pageutil.UpdatePage;
import com.runutils.RunTestCase;

import android.os.RemoteException;

/** 
* @author 作者 E-mail: Dongcheng.Wei@desay-svautomotive.com
* @version 创建时间：2017年6月22日 下午8:38:30 
* 类说明 :
* case: 系统升级
* 命令：uiautomator runtest AutoTest.jar -c com.update.UpdateVersion
* 前提：当前在系统界面，update.zip文件已通过sd或者u盘装载
* 步骤：进入系统升级进行升级
* 期望：成功升级
*/
public class UpdateVersion extends UiAutomatorTestCase {

	/**
	 * 
	 * @param args
	 * @Date 2017-06-22
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.update.UpdateVersion", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	}
	public void testUpdateVersion() {
		try {
			UiDevice.getInstance().wakeUp();
			Utils.stopRunningWatcher();
			
			assertEquals(true, updateToNewVersion());
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	public boolean updateToNewVersion() {
		boolean isOk = false;
		
		UpdatePage updatePage = new UpdatePage();
		HomePage homePage = new HomePage();
		SettingsPage settingsPage = new SettingsPage();
		SysTabPage sysTabPage = new SysTabPage();
		try {
			homePage.goBackHome();
			if (homePage.intoSettings()) {//进入设置
				if (settingsPage.intoSystemTab()) {//系统
					if (sysTabPage.intoSystemUpdatePage()) {//进入系统升级，会弹出升级成功对话框
						updatePage.clickConfirmUpdateOk();//确定
						if (updatePage.clickCheckUpdateBtn()) {//点击检查升级按钮
							if (updatePage.clickConfirmToUpdate()) {//确定升级
								if (updatePage.clickConfirmIfDialogExists(5000)) {//弹出确定框
									if (updatePage.clickUpdateNow()) {//立即升级
										Utils.logPrint("update now?");
										if (updatePage.clickConfirmIfDialogExists(5000)) {//确定
											isOk = true;
											Utils.logForResult("updating ...");
										} else {
											Utils.logPrint("Error:confirm update now fail...");
										}
									} else {
										Utils.logPrint("Error:update now fail...");
									}
								} else {
									Utils.logPrint("Error:updating1 fail...");
								}
							} else {
								Utils.logPrint("Error:updating fail...");
							}
						} else {
							Utils.logPrint("Error:check updating fail...");
						}
					} else {
						Utils.logPrint("Error:into system update fail...");
					}
				} else {
					Utils.logPrint("Error:intoSystemTab fail...");
				}
			} else {
				Utils.logPrint("Error:intoSettings fail...");
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("Error:UiObjectNotFoundException->" + e.toString());
		}
		
		return isOk;
	}

}
