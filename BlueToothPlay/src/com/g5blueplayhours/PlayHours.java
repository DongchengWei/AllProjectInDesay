package com.g5blueplayhours;

import android.os.RemoteException;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import android.util.Log;

public class PlayHours extends UiAutomatorTestCase{
	
	public void playHoursTest() throws UiObjectNotFoundException{
			PrintInfo();
			try {
				getUiDevice().wakeUp();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
	}
	
	
	//控制台输出和logcat输出信息
	public void logAndPrint(String str){
		System.out.println("=========" + str);
		Log.i("BLUETOOTHAUTOTEST","=========" + str);
	}
	//输出版本信息
	public void PrintInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android Bluetooth AutoTest v0.0.1======");
		System.out.println("=========       2016-12-30       =================");
		System.out.println("=========        DesaySV         =================");
		System.out.println("===============PlayHoursTest======================");
		Log.i("BLUETOOTHAUTOTEST","=========G5Android Bluetooth AutoTest v0.0.1======");
		Log.i("BLUETOOTHAUTOTEST","=========       2016-12-30       =================");
		Log.i("BLUETOOTHAUTOTEST","=========        DesaySV         =================");
		Log.i("BLUETOOTHAUTOTEST","===============PlayHoursTest======================");
	}
}
