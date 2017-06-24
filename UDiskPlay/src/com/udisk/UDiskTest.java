package com.udisk;

import java.io.IOException;

import com.android.uiautomator.core.UiCollection;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.runutils.RunTestCase;

import android.os.RemoteException;
import android.util.Log;

public class UDiskTest extends UiAutomatorTestCase {

	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("UDiskPlay",  
				 "com.udisk.UDiskTest", "", "2");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
		 
	public void testDemo(){
		int folderCounter = 0;
		try{
			PrintInfo();
			getUiDevice().wakeUp();
			getUiDevice().pressHome();
			//com.android.launcher:id/ts_home_button_item_media_id
			try {
				new UiObject(new UiSelector()
						.resourceId("com.android.launcher:id/ts_home_button_item_media_id")).click();//into multimedia
				
				UiObject allFolderObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/tv_media_music_all_folder"));
				if (allFolderObj.exists()) {
					UiDevice.getInstance().pressBack();
				}
				UiObject sourceSeleteObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/media_source"));// into switch source 
				sourceSeleteObj.click();
				
				UiObject usb1Obj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/ts_media_source_button_item_usb2"));
				usb1Obj.click();		//select usb1 source
				
				UiObject musicIdObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/ts_media_button_item_music_id"));
				musicIdObj.click();		//into music 
				
				allFolderObj.click();	//click folder
				
				//文件夹名称
				UiObject kindLableObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/tv_kindlabel"));
				//com.thundersoft.mediaplayer:id/gv_music_folder
				if (kindLableObj.waitForExists(240000)) {
					logAndPrint("folder found");
				}else {
					logAndPrint("can not find any folder!,test fail");
				}
				folderCounter = new UiCollection(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/gv_music_folder")).getChildCount();			   	//获取全部文件夹下的文件夹数量
				logAndPrint("All folders : " + folderCounter);
				
				UiObject musicTitleObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_title"));
				UiObject nextSongObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_next"));
				UiSelector sGvFolder = new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/gv_music_folder");
				
				int playedForder = 0;
				while(playedForder < folderCounter){
					//点击第i个文件夹播放
					UiSelector layoutForder = sGvFolder.childSelector(new UiSelector()
							.className("android.widget.RelativeLayout").index(playedForder));
					new UiObject(layoutForder).clickAndWaitForNewWindow();//点击第i个文件夹播放
					//刚开始播放时的第一首歌曲名
					String firstMusicTitle = musicTitleObj.getText();
					
					boolean forderPlaying = true;	//正在播放文件夹内的歌曲
					while(forderPlaying){			//一个文件夹没有播放完就一直播放下一曲
						switch (IsPlayingState()) {
						case 1:						//正在播放时歌曲总时间正常，则下一曲继续播放
							nextSongObj.click();	//下一曲
							sleep(3000);
							//下一曲后的歌曲名
							String nowTitle = musicTitleObj.getText();
							if (nowTitle.equals(firstMusicTitle)) {	//循环了一遍准备切换到下一个文件夹播放
								sourceSeleteObj.click();			//into switch source 
								usb1Obj.click();					//select usb2 source
								musicIdObj.click();					//into music 
								allFolderObj.click();				//click folder
								playedForder ++;
								forderPlaying = false;//切换到另外的文件夹播放
							}
							break;
						case 2:	//播放同一首歌且处在暂停或者总时间有变化的情况
							forderPlaying = false;
							logAndPrint("playedForder=" + playedForder + ",case 2");
							playedForder = folderCounter+1;//退出播放停在总时间异常界面
							break;
						default:
							forderPlaying = false;
							playedForder = folderCounter+1;//退出播放停在总时间异常界面
							logAndPrint("playedForder=" + playedForder + ",case 3");
							break;
						}
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
	}
	
}
