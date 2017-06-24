package com.g5autotest;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import android.os.RemoteException;
import android.util.Log;

public class G5test extends UiAutomatorTestCase{

	public void testDemo() throws UiObjectNotFoundException{
		try{
			System.out.println("==================================================");
			System.out.println("=========G5Android蓝牙自动化测试 v0.0.1===========");
			System.out.println("=========       2016-12-30       =================");
			System.out.println("=========        DesaySV         =================");
			System.out.println("==================================================");
			Log.i("BLUETOOTHAUTOTEST","=========G5Android蓝牙自动化测试 v0.0.1===========");
			Log.i("BLUETOOTHAUTOTEST","=========       2016-12-30       =================");
			Log.i("BLUETOOTHAUTOTEST","=========        DesaySV         =================");
			getUiDevice().wakeUp();
			getUiDevice().pressHome();
			sleep(1000);
			new UiObject(new UiSelector().className("android.widget.TextView")
	                .text("设置")).clickAndWaitForNewWindow();
			System.out.println("=========进入设置");
	        sleep(1000);
	        new UiObject(new UiSelector().className("android.widget.RadioButton")
	                .text("蓝牙")).click();
	        System.out.println("=========点击蓝牙");
	        sleep(1000);
	        UiObject blueSwitch= new UiObject(new UiSelector().className("android.widget.Switch")); 
	        if (!blueSwitch.isChecked()){	       	
	        	System.out.println("=========蓝牙开关未打开，正在打开。。。");
	        	blueSwitch.click();
		        sleep(20000);
	        }
	        
	        UiScrollable noteList = new UiScrollable( new UiSelector().resourceId("com.android.settings:id/ts_bluetooth_pair_dev_list").scrollable(true));
//	        UiScrollable noteList = new UiScrollable( new UiSelector().scrollable(true)); com.thundersoft.mediaplayer:id/media_next
	        UiObject note = null; 
	        try 
	        {
	        	if(noteList.exists()) 
	        	{
	        		note = noteList.getChildByText(new UiSelector().className("android.widget.TextView"), "(已连接)", true); 
	        		if(note != null && note.exists()){
	        			System.out.println("=========noteList.exists");
	        		}
	        	}
	        	else 
	        	{
	        		note = new UiObject(new UiSelector().className("android.widget.TextView").textContains("(已连接)"));
	        		if(note.exists()){
	        			System.out.println("=========蓝牙已连接");
	        		}
	        	}
	        } 
	        catch (UiObjectNotFoundException e)
	        {
	        	if(note == null || !note.exists()) 
	        	{
	            	System.out.println("=========没有找到(已连接)");
	               
	        	}
	        }
	        
	        AnotherCase();//影音媒体下一曲播放
	        
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("null")
	public void AnotherCase() throws RemoteException{
		System.out.println("====================Another Case========================");
		//getUiDevice().wakeUp(); com.android.launcher:id/contirm_btn
		getUiDevice().pressHome();
		sleep(1000);
		
		try {
			new UiObject(new UiSelector().className("android.widget.TextView")
			        .text("影音娱乐")).clickAndWaitForNewWindow();
			System.out.println("=========进入影音媒体");
	        int counter = 0;
	        //com.thundersoft.mediaplayer:id/media_title
	        UiObject mediaNext = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/media_next"));
	        UiObject playerMedia = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/media_title").text("媒体库"));
	        
	        for (counter=0; counter<5; counter++){
	        	if(!playerMedia.exists()){
	        		mediaNext.click();
	        		sleep(1000);
	        		playerMedia = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/media_title").text("媒体库"));
	        		System.out.println("=========找不到媒体库的播放界面" + counter);	        		
	        	}
	        	if(counter==5){
	        		
	        		System.out.println("=========四个界面都没有媒体库的播放界面");
	        	}
	        }
	        counter = 1;
        	if(playerMedia.exists())
        	{//com.thundersoft.mediaplayer:id/iv_ts_media_btn_next
        		
        		System.out.println("=========找到媒体库");
        		mediaNext = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_next"));
        		if(mediaNext.exists()){
            		for (counter=1; counter<5; counter++){
            			mediaNext.click();
            			System.out.println("=========下一曲 " + counter);
            			sleep(15000);
            			
            			String nowPlayTime = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_start_time")).getText(); 
        				String musicTitle = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_title")).getText();

            			String[] timeStr = new String[20];
        				timeStr = nowPlayTime.split(":");
        				int secondCount = Integer.parseInt(timeStr[1]);//00:10  只取后面的秒，0为取分        				
        				if(secondCount >= 10){
        					System.out.println("=========" + musicTitle + "播放成功");
        				}
            		}
        		}else{
        			
        			//com.thundersoft.mediaplayer:id/ts_media_button_item_music_id
        			new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/ts_media_button_item_music_id")).click();
        			//com.thundersoft.mediaplayer:id/tv_kindlabel
        			new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_kindlabel")).click();
        			mediaNext = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_next"));
        			
        			for (counter=1; counter<5; counter++){
        				mediaNext.click();
        				System.out.println("=========下一曲 " + counter);
        				sleep(15000);
        				//com.thundersoft.mediaplayer:id/tv_start_time
        				//com.thundersoft.mediaplayer:id/tv_ts_media_music_title
        				String nowPlayTime = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_start_time")).getText(); 
        				String musicTitle = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_title")).getText();

            			String[] timeStr = new String[20];
        				timeStr = nowPlayTime.split(":");
        				int secondCount = Integer.parseInt(timeStr[1]);//00:10  只取后面的秒，0为取分        				
        				if(secondCount >= 10){
        					System.out.println("=========" + musicTitle + "播放成功");
        				}
        			}
        		}
        	}
	        	
		} catch (UiObjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
