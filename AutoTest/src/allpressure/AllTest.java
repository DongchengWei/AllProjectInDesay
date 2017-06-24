/**
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-06-05
 */
package allpressure;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.bt.BtConnectCheckContactDisconnect;
import com.otherutils.Utils;
import com.runutils.RunTestCase;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

/** 
* @author 作者 E-mail: Dongcheng.Wei@desay-svautomotive.com
* @version 创建时间：2017年6月5日 下午4:52:39 
* 类说明 :
*/
/**
 * @author uidq0460
 *
 */
public class AllTest extends UiAutomatorTestCase {

	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "allpressure.AllTest", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("===case:BT connect check contacts and disconnect==");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","===case:BT connect check contacts and disconnect==");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
	public void testAllTest(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.clickConfirmWatcher();
			
			//获取测试设备名称
			String deviceNameStr = "";
			Bundle devicesNameBundle = getParams();//获取参数
			if (devicesNameBundle.getString("DeviceName") != null) {
				deviceNameStr = devicesNameBundle.getString("DeviceName");
			} else {
				deviceNameStr = "PLK";
			}
			Utils.logPrint("deviceNameStr = " + deviceNameStr);
			
			//获取测试次数
			long testTimes = 0;
			if (devicesNameBundle.getString("TestTimes") != null) {
				String testTimesStr = devicesNameBundle.getString("TestTimes");
				testTimes = Long.parseLong(testTimesStr);
			} else {
				testTimes = 100;
			}
			Utils.logPrint("testTimes = " + testTimes);
			
			//获取测试次数
			long allTestTimes = 0;
			if (devicesNameBundle.getString("AllTimes") != null) {
				String allTimesStr = devicesNameBundle.getString("AllTimes");
				allTestTimes = Long.parseLong(allTimesStr);
			} else {
				allTestTimes = 1;
			}
			Utils.logPrint("allTestTimes = " + allTestTimes);
			
			boolean keepTesting = true;
			int testCounter = 0;
			
			BtConnectCheckContactDisconnect btConnectCheckContactDisconnect = new BtConnectCheckContactDisconnect();
			while(keepTesting){
				testCounter ++;
				
				boolean testPass = btConnectCheckContactDisconnect.btConnectCheckContactDisconnectTest(testTimes, deviceNameStr);
				assertEquals(true, testPass);
				
				
				
//			btConnectCheckContactDisconnectTest(testTimes, deviceNameStr);		//
				
				if (testCounter == allTestTimes) {
					keepTesting = false;
				}
			}
			
			
			
			
			
			UiDevice.getInstance().removeWatcher("confirmStopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
}
