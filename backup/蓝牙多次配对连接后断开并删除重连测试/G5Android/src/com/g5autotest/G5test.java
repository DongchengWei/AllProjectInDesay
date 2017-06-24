package com.g5autotest;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import android.os.RemoteException;
import android.util.Log;

public class G5test extends UiAutomatorTestCase{

	String BLUETOOTH_DEVICE_NAME = "HTC M8w";
	
	public void testDemo() throws UiObjectNotFoundException{
		try{
			PrintInfo();
			getUiDevice().wakeUp();

			for(int i=0; i<5; i++){
			getUiDevice().pressHome();
			sleep(1000);
			new UiObject(new UiSelector().className("android.widget.TextView")
	                .text("设置")).clickAndWaitForNewWindow();
			//System.out.println("=========进入设置");
			logAndPrint("进入设置");
			
	        new UiObject(new UiSelector().className("android.widget.RadioButton")
	                .text("蓝牙")).click();
	        //System.out.println("=========点击蓝牙");
	        logAndPrint("点击蓝牙");
	        sleep(5000);

	        UiObject blueSwitch= new UiObject(new UiSelector().className("android.widget.Switch")); 
	        if (!blueSwitch.isChecked()){	       	
	        	System.out.println("=========蓝牙开关未打开，正在打开。。。");
	        	blueSwitch.click();
		        sleep(20000);
	        }
	        //刷新蓝牙列表
//	        new UiObject(new UiSelector().
//					resourceId("com.android.settings:id/ts_bluetooth_refresh")).click();
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
	        				resourceId("com.android.settings:id/ts_bluetooth_dev_list"), BLUETOOTH_DEVICE_NAME); //, true
	        		if(note != null && note.exists()){
	        			System.out.println("=========New Devices noteList.exists");
	        			sleep(5000); //等待5s对应设备允许连接
	        			note.click();//点击该设备进行匹配
	        		}
	        	}
	        	else //不能滚动则直接找 
	        	{
	        		note = new UiObject(new UiSelector().
	        				className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME));
	        		if(note.exists()){
	        			System.out.println("=========" + BLUETOOTH_DEVICE_NAME + " Exist");
	        			sleep(5000); //等待10s对应设备允许连接
	        			note.click();//点击该设备进行匹配
	        		}else{
	        			System.out.println("=========" + BLUETOOTH_DEVICE_NAME + " not Exist");
	        		}
	        	}
	        	
	        	sleep(5000); //等待10s
	        	//开始连接
	        	UiScrollable pairedList = new UiScrollable( new UiSelector().
	        			resourceId("com.android.settings:id/ts_bluetooth_pair_dev_list").scrollable(true));
	        	UiObject pairedNote = null;
	        	if(pairedList.exists()){
	        		pairedNote = pairedList.getChildByText(new UiSelector().
	        				className("android.widget.TextView"), BLUETOOTH_DEVICE_NAME,true);
	        		if(pairedNote != null && pairedNote.exists()){
	        			System.out.println("=========Paired Devices exists");
	        			sleep(10000); //等待10s对应设备连接
	        			pairedNote.click();//点击该设备进行连接
	        		}
	        	}else{//不滚动直接查找
	        		pairedNote = new UiObject(new UiSelector().
	        				className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME));
        			if(pairedNote.exists()){//如果存在则点击进行连接
        				System.out.println("=========" + BLUETOOTH_DEVICE_NAME + " Paired");
        				pairedNote.click();	//点击该设备进行连接
        				System.out.println("=========" + BLUETOOTH_DEVICE_NAME + " Click to connect");
        				sleep(5000); 		//等待5s对应设备连接com.android.settings:id/ts_bluetooth_device_status
        				
        				UiObject connectObject = new UiObject(new UiSelector().
        						className("android.widget.TextView").textContains("(已连接)"));
        				if(connectObject.exists()){//如果已连接上全部协议
        					System.out.println("=========" + BLUETOOTH_DEVICE_NAME + " Connected");
//        					com.android.settings:id/dialog_confirm
        					pairedNote.clickAndWaitForNewWindow();
        					new UiObject(new UiSelector().
            						resourceId("com.android.settings:id/dialog_confirm")).clickAndWaitForNewWindow();
        					//com.android.settings:id/ts_bluetooth_delete //删除蓝牙设备
        					new UiObject(new UiSelector().
            						resourceId("com.android.settings:id/ts_bluetooth_delete")).click();//
        					//com.android.settings:id/ts_bluetooth_item_checkbox
        				}
        				else{
        					System.out.println("=========" + BLUETOOTH_DEVICE_NAME + " Connected Failed");
        				}
        			}else{
        				System.out.println("=========" + BLUETOOTH_DEVICE_NAME + " Paired Failed");
        			}
	        	}
	        	
	        	UiScrollable deletePairList = new UiScrollable( new UiSelector().
	        			resourceId("com.android.settings:id/ts_bluetooth_pair_dev_list").scrollable(true));
	        	UiObject deletePairNote = null;
	        	if(deletePairList.exists()){
	        		deletePairNote = deletePairList.getChildByText(new UiSelector().
	        				className("android.widget.TextView"), BLUETOOTH_DEVICE_NAME,true);
	        		if(deletePairNote != null && deletePairNote.exists()){
	        			System.out.println("=========CheckBox Devices exists");
	        			new UiObject(new UiSelector().text(BLUETOOTH_DEVICE_NAME).fromParent(new UiSelector()
	        					.resourceId("com.android.settings:id/ts_bluetooth_item_checkbox"))).click();
	        			
	        			//com.android.settings:id/ts_bluetooth_delete //删除蓝牙设备System.out.println("=========HTC M8w Paired");
    					new UiObject(new UiSelector().
        						resourceId("com.android.settings:id/ts_bluetooth_delete")).click();
	        			
	        		}
	        	
	        	}else{//不能滚动则直接查找
	        		new UiObject(new UiSelector().
	        				className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME).fromParent(new UiSelector().
	        						resourceId("com.android.settings:id/ts_bluetooth_item_checkbox"))).click();
	        		new UiObject(new UiSelector().
    						resourceId("com.android.settings:id/ts_bluetooth_delete")).clickAndWaitForNewWindow();
	        	}
	        	
	        } 
	        
	        catch (UiObjectNotFoundException e)
	        {
	        	e.printStackTrace();
	        }
			} //for 循环
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
		System.out.println("=========G5Android蓝牙自动化测试 v0.0.1===========");
		System.out.println("=========       2016-12-30       =================");
		System.out.println("=========        DesaySV         =================");
		System.out.println("==================================================");
		Log.i("BLUETOOTHAUTOTEST","=========G5Android蓝牙自动化测试 v0.0.1===========");
		Log.i("BLUETOOTHAUTOTEST","=========       2016-12-30       =================");
		Log.i("BLUETOOTHAUTOTEST","=========        DesaySV         =================");
	}
}
