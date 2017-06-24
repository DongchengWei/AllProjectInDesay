/**
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-06-03
 */
package com.bt;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.MediaPage;
import com.runutils.RunTestCase;

import android.os.RemoteException;
import android.os.SystemClock;

/** 
* @author 作者 E-mail: Dongcheng.Wei@desay-svautomotive.com
* @version 创建时间：2017年6月3日 上午10:19:38 
* 类说明 :
*/
/**
 * @author uidq0460
 *
 */
public class MusicForHours extends UiAutomatorTestCase {

	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.bt.MusicForHours", "", "3");  
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
			
			assertEquals(true, btMusicForHours(12*60*60*1000));
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public boolean btMusicForHours(long timeout) {
		boolean isTestPass = true;
		
		HomePage homePage = new HomePage();
		homePage.goBackHome();
		try {
			homePage.intoMultimedia();
			MediaPage mediaPage = new MediaPage();
			if (mediaPage.intoBtMusic()) {
				
				//指定时间
				long startMills = SystemClock.uptimeMillis();
				long currentMills = 0;
				while(currentMills <= timeout){
					
					currentMills = SystemClock.uptimeMillis() - startMills;
					if(timeout > 0) {
						SystemClock.sleep(60000);
					}
					if (mediaPage.SourceJudge() == MediaPage.BLUETOOTHTITLE && mediaPage.isPlaying()) {
						currentMills = SystemClock.uptimeMillis() - startMills;
						Utils.logPrint("mediaPage.isPlaying():true ->Test Time:" + Utils.msToFormateTime(currentMills));
					} else {
						currentMills = timeout +1;//退出循环，退出测试
						isTestPass = false;
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("btMusicForHours UiObject not found:" + e.toString());
		}
		
		return isTestPass;
	}
}
