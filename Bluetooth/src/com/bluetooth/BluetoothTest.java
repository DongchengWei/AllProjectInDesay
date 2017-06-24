package com.bluetooth;

import java.io.IOException;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.runutils.RunTestCase;

import android.os.RemoteException;
import android.util.Log;

public class BluetoothTest extends UiAutomatorTestCase {

	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("Bluetooth",  
				 "com.bluetooth.BluetoothTest", "", "2");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
		 
	public void testDemo(){
		try{
			PrintInfo();
			getUiDevice().wakeUp();
			getUiDevice().pressHome();
			//com.android.launcher:id/ts_home_button_item_media_id
			try {
				new UiObject(new UiSelector()
						.resourceId("com.android.launcher:id/ts_home_button_item_media_id")).click();				//into multimedia
				new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/media_source")).click();						// into switch source 
				new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/ts_media_source_button_item_device_bt")).click();	//select bluetooth
				UiObject nextSong = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_next"));
				sleep(2000);
				
				UiObject musicTitleObj = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_title"));
				String strMusicTitle = musicTitleObj.getText();
				while(strMusicTitle.equals("") || (strMusicTitle == null)){
					nextSong.click();
					strMusicTitle = musicTitleObj.getText();
				}
				
				boolean playingNormal = true;
				while(playingNormal){
					switch (IsPlayingState()) {
					case 1:
						logAndPrint("play normal,next song...");
						nextSong.click();
						sleep(2000);
						String nowStrTitle = musicTitleObj.getText();
						if (nowStrTitle.equals(strMusicTitle)) {
							playingNormal = false;
							logAndPrint("Forder play end,exit");
						}
						break;

					default:
						playingNormal = false;
						logAndPrint("exit play");
						break;
					}
				}
			} catch (UiObjectNotFoundException e) {
				logAndPrint("UDiskPlay Test fail:UiObjectNotFoundException");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	


	//播放器播放判断（隔5秒获取播放时间判断是否在播放）
	private int IsPlayingState(){
		int playState = 0;
		try {
			String nowPlayTime = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_start_time")).getText();
			//com.thundersoft.mediaplayer:id/tv_end_time
			String nowEndTime = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_end_time")).getText();
			String strMusicTitle = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_title")).getText();
			logAndPrint("Music Title:" + strMusicTitle);
			sleep(5000);								//过5秒再读取播放时间，读取歌曲名判断是否是同一首歌
			String fiveSecPlayTime = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_start_time")).getText();
			String fiveSecEndTime = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_end_time")).getText();
			String fiveSecMusicTitle = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_title")).getText();
			if (fiveSecMusicTitle.equals(strMusicTitle)) {	//同一首歌的情况下1.正在播放 ， 2.播放时歌曲总时间跳动
				if ((!nowPlayTime.equals(fiveSecPlayTime)) && fiveSecEndTime.equals(nowEndTime)) {
					playState = 1;//正在播放时歌曲总时间正常
					logAndPrint("play normal:" + fiveSecMusicTitle);
				}else {
					playState = 2;//处在暂停或者总时间有变化的情况
					logAndPrint("strMusicTitle=" + strMusicTitle + ",fiveSecMusicTitle=" + fiveSecMusicTitle + ", nowEndTime=" + nowEndTime + ",fiveSecEndTime=" + fiveSecEndTime);
				}
			}else{//2s内播放了不同歌曲
				playState = 3;
				logAndPrint("strMusicTitle=" + strMusicTitle + ",fiveSecMusicTitle=" + fiveSecMusicTitle);
			}
		} catch (UiObjectNotFoundException e) {
			// TODO Auto-generated catch block
			logAndPrint("IsPlayingOK() fail: UiObjectNotFoundException");
		}
		return playState;
	}
	
	public void logForResult(String str) {
		System.out.println("=========>>>>" + str + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		Log.d("TESTRESULT","=========>>>>" + str + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}
	
	
	//控制台输出和logcat输出信息
	public void logAndPrint(String str){
		System.out.println("=========" + str);
		Log.d("BLUETOOTRESULT","=========" + str);
	}
	
	//输出版本信息
	public void PrintInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android Bluetooth AutoTest v0.0.1======");
		System.out.println("=========       2016-12-30       =================");
		System.out.println("=========        DesaySV         =================");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android Bluetooth AutoTest v0.0.1======");
		Log.d("BLUETOOTHAUTOTEST","=========       2016-12-30       =================");
		Log.d("BLUETOOTHAUTOTEST","=========        DesaySV         =================");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
	
}
