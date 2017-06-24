package com.g5blueautotest;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import android.os.RemoteException;
import android.util.Log;

public class G5BlueTest extends UiAutomatorTestCase{

	String BLUETOOTH_DEVICE_NAME = "samsungs5";
	
	public void testDemo() throws UiObjectNotFoundException{
		try{
			PrintInfo();
			getUiDevice().wakeUp();

			getUiDevice().pressHome();
			sleep(1000);
			new UiObject(new UiSelector().className("android.widget.TextView")
	                .text("设置")).clickAndWaitForNewWindow();
			//System.out.println("=========进入设置");
			logAndPrint("Enter Settings");
			
	        new UiObject(new UiSelector().className("android.widget.RadioButton")
	                .text("蓝牙")).click();
	        //System.out.println("=========点击蓝牙");
	        logAndPrint("Click Bluetooth");
	        sleep(5000);

	        UiObject blueSwitch= new UiObject(new UiSelector().className("android.widget.Switch")); 
	        if (!blueSwitch.isChecked()){	       	
	        	logAndPrint("Bluetooth is off,swithing on...");
	        	blueSwitch.click();
		        sleep(10000);
	        }
	        sleep(2000);
	        try 
	        {
	        	//com.android.settings:id/ts_bluetooth_dev_list
		        //可以滚动的listview   开始配对
	        	UiScrollable noteList = new UiScrollable( new UiSelector().
	        		resourceId("com.android.settings:id/ts_bluetooth_dev_list").scrollable(true));
	        	UiObject note = null; 
	        	if(noteList.exists())  //可以滚动的listview
	        	{
	        		note = noteList.getChildByText(new UiSelector().
	        				resourceId("com.android.settings:id/ts_bluetooth_dev_list"), BLUETOOTH_DEVICE_NAME); //, true 是否允许滚动查找
	        		if(note != null && note.exists()){				//存在目标匹配设备
	        			logAndPrint("New Devices noteList.exists");
	        			sleep(5000); 								//等待5s对应设备允许连接
	        			note.click();								//点击该设备进行匹配
	        		}else{											//不存在目标设备
	        			logAndPrint(BLUETOOTH_DEVICE_NAME + " New Devices noteList not Exist");
	        		}
	        	}
	        	else //不能滚动则直接找 
	        	{
	        		note = new UiObject(new UiSelector().
	        				className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME));
	        		if(note.exists()){
	        			logAndPrint(BLUETOOTH_DEVICE_NAME + " Exist");
	        			sleep(5000); //等待10s对应设备允许连接
	        			note.click();//点击该设备进行匹配
	        		}else{
	        			logAndPrint(BLUETOOTH_DEVICE_NAME + " not Exist");
	        		}
	        	}
	        	
	        	sleep(5000); //等待5s
	        	//开始连接
	        	UiScrollable pairedList = new UiScrollable( new UiSelector().
	        			resourceId("com.android.settings:id/ts_bluetooth_pair_dev_list").scrollable(true));
	        	UiObject pairedNote = null;
	        	if(pairedList.exists()){
	        		pairedNote = pairedList.getChildByText(new UiSelector().
	        				className("android.widget.TextView"), BLUETOOTH_DEVICE_NAME);//,true
	        		if(pairedNote != null && pairedNote.exists()){
	        			logAndPrint(BLUETOOTH_DEVICE_NAME + "Paired");
	        			sleep(10000); 				//等待10s对应设备连接
	        			pairedNote.click();			//点击该设备进行连接
        				logAndPrint(BLUETOOTH_DEVICE_NAME + " Click to connect");
        				sleep(5000); 				//等待5s对应设备连接com.android.settings:id/ts_bluetooth_device_status
        				int disconnectFailedTimes = 0;
        				int connectFailedTimes = 0;
        				for (int j = 1; j < 200+1; j++) {
	        				UiObject connectObject = new UiObject(new UiSelector().
	        						className("android.widget.TextView").textContains("(已连接)"));
	        				if(connectObject.exists()){//
		        					logAndPrint(BLUETOOTH_DEVICE_NAME + " The " + j + " st time connect sucessed");
		        					pairedNote.clickAndWaitForNewWindow();	//
		        					new UiObject(new UiSelector().resourceId("com.android.settings:id/dialog_confirm"))
		        						.clickAndWaitForNewWindow();//弹出确认断开对话框，点击确认
		        					
		        					sleep(2000); 		//等待2秒等待断开
		        					connectObject = new UiObject(new UiSelector().
		        							className("android.widget.TextView").textContains("(已连接)"));//确认是否断开
		        					
		        					if (connectObject.exists()) {//没有断开
		        						logAndPrint(BLUETOOTH_DEVICE_NAME + " The " + j + " st time disconnect failed");
		        						disconnectFailedTimes++;
		        					}else {						 //已断开
		        						logAndPrint(BLUETOOTH_DEVICE_NAME + " The " + j + " st time disconnect sucessed");
		        					}
		        					
		        					pairedNote.clickAndWaitForNewWindow();	//
		        					sleep(5000); 		        //等待5秒
	        				}
	        				else{
	        					logAndPrint(BLUETOOTH_DEVICE_NAME + " The " + j + " st time connect failed");
	        					connectFailedTimes++;
	        					pairedNote.clickAndWaitForNewWindow();	//
	        					sleep(5000); 		//
	        				}
        				}//for
        				logAndPrint("TestResult-->Connect Failed:" + connectFailedTimes + ",Disconnect Failed:" + disconnectFailedTimes + ":Total test:200");
	        		}else {
	        			logAndPrint(BLUETOOTH_DEVICE_NAME + " Paired Failed");
					}
	        	}else{//不滚动直接查找
	        		pairedNote = new UiObject(new UiSelector().
	        				className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME));
        			if(pairedNote.exists()){//如果存在则点击进行连接
        				logAndPrint(BLUETOOTH_DEVICE_NAME + " Paired");
        				pairedNote.click();	//点击该设备进行连接
        				logAndPrint(BLUETOOTH_DEVICE_NAME + " Click to connect");
        				sleep(5000); 		//等待5s对应设备连接com.android.settings:id/ts_bluetooth_device_status
        				int disconnectFailedTimes = 0;
        				int connectFailedTimes = 0;
        				for (int j = 1; j < 200+1; j++) {
	        				UiObject connectObject = new UiObject(new UiSelector().
	        						className("android.widget.TextView").textContains("(已连接)"));
	        				if(connectObject.exists()){//
		        					logAndPrint(BLUETOOTH_DEVICE_NAME + " The " + j + "st time connect sucessed");
		        					pairedNote.clickAndWaitForNewWindow();	//
		        					new UiObject(new UiSelector().resourceId("com.android.settings:id/dialog_confirm"))
		        						.clickAndWaitForNewWindow();//弹出确认断开对话框，点击确认
		        					connectObject = new UiObject(new UiSelector().
		        							className("android.widget.TextView").textContains("(已连接)"));//确认是否断开
		        					
		        					if (connectObject.exists()) {//没有断开
		        						logAndPrint(BLUETOOTH_DEVICE_NAME + " The " + j + "st time disconnect failed");
		        						disconnectFailedTimes++;
		        					}else {						 //已断开
		        						logAndPrint(BLUETOOTH_DEVICE_NAME + " The " + j + "st time disconnect sucessed");
		        					}
		        					
		        					sleep(5000); 		        //等待5秒再点击
		        					pairedNote.click();	//
		        					sleep(5000); 		        //等待5秒
	        				}
	        				else{
	        					logAndPrint(BLUETOOTH_DEVICE_NAME + " The " + j + "st time connect failed");
	        					sleep(5000); 		//
	        					connectFailedTimes++;
	        					pairedNote.click();	//
	        					sleep(5000); 		//
	        				}
        				}//for
        				logAndPrint("TestResult-->Connect Failed:" + connectFailedTimes + ",Disconnect Failed:" + disconnectFailedTimes + ":Total test:200");
        			}else{
        				logAndPrint(BLUETOOTH_DEVICE_NAME + " Paired Failed");
        			}
	        	}
	        } 
	        
	        catch (UiObjectNotFoundException e)
	        {
	        	e.printStackTrace();
	        }
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
		System.out.println("==================================================");
		Log.i("BLUETOOTHAUTOTEST","=========G5Android Bluetooth AutoTest v0.0.1======");
		Log.i("BLUETOOTHAUTOTEST","=========       2016-12-30       =================");
		Log.i("BLUETOOTHAUTOTEST","=========        DesaySV         =================");
	}
}
