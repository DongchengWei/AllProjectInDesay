logAndPrint("Check detail,press home to confirm pass, and back to fail in 60s...");
bookTitleObj.waitUntilGone(60000);	//等待1分钟确认后转到其他界面
if (homeCallObj.exists()) {			//如果返回的是home界面则确定pass
	logForResult(testCaseCounter + ".DownloadPhonebook() pass");
}else{
	logForResult(testCaseCounter + ".DownloadPhonebook() fail");
}

	//10.在执行重新连接后由主机上/下一首 ，蓝牙必须已连接
	private void ReconnectBTThenHuNextAndPre() {
    	EnterSettingsWhichFragment("蓝牙");
    	if (WaitToConnectByHand()) {//检查蓝牙是否已连接，没有连接则等待30秒手动连接
    		try {
    			//点击已连接的设备，先断开此设备的连接
    			logAndPrint("Click " + BLUETOOTH_DEVICE_NAME);
    			UiObject testDeviceObj = new UiObject(new UiSelector().
    					className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME));
    			testDeviceObj.clickAndWaitForNewWindow();
    			
    			logAndPrint("Confirm to disconnect");
    			new UiObject(new UiSelector().resourceId("com.android.settings:id/dialog_confirm"))
    			.clickAndWaitForNewWindow();//弹出确认断开对话框，点击确认
    			
    			EnterSettingsWhichFragment("蓝牙");//15版本断开蓝牙连接后会跳转到FM界面，需要再进入设置连接
    			//点击连接目标设备
    			logAndPrint("Click to connect " + BLUETOOTH_DEVICE_NAME);
    			testDeviceObj.click();
    			UiObject connectOK = new UiObject(new UiSelector().
    					resourceId("com.android.settings:id/item_bt_icon_phone"));
    			connectOK.waitForExists(30000);//在20秒内等待连接上
    			if (connectOK.exists()) {
    				logAndPrint("reconnect sucess!");
    				EnterWhichMedia("蓝牙设备");//进入影音媒体的蓝牙设备
    				UiObject nextSongObj = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_next"));//下一曲按钮
    				nextSongObj.click();
    				sleep(2000);
        			//暂停按钮
        			UiObject pauseObject = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_pause"));
        			if (IsPlayingJudge()) {
        				logAndPrint("is now playing");
        			}else {
        				pauseObject.click();
        				logAndPrint("Click to play");
        				sleep(1000);
        			}
        			//获取当前曲目com.thundersoft.mediaplayer:id/tv_ts_media_music_title
        			String musicTitle = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_title")).getText();
        			logAndPrint("Before Next Playing:" + musicTitle);
        			if (musicTitle!=null && !musicTitle.equals("")) {
        				//com.thundersoft.mediaplayer:id/iv_ts_media_btn_previous //下一曲
        				nextSongObj.click();//下一曲按钮
        				sleep(10000);//播放10s
        				String nextMusicTitle = new UiObject(new UiSelector().
        						resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_title")).getText();//下一曲后的歌曲名
        				
        				if (musicTitle.equals(nextMusicTitle)) {				//说明当前播放的是同一首歌
        					logForResult(testCaseCounter + "Next Failed");		//下一曲失败
        				}else {
        					logAndPrint("After Next Playing:" + nextMusicTitle + "-->Next song Pass");//与点击下一曲之前的歌曲不同com.thundersoft.mediaplayer:id/iv_ts_media_btn_next
        					
        					String playTimeStr = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_start_time")).getText();
        					String[] timeStr = new String[20];
            				timeStr = playTimeStr.split(":");
            				int secondCount = Integer.parseInt(timeStr[1]);//00:10  只取后面的秒，0为取分  
            				if (secondCount>3) {//播放时间大于3秒需要点击上一曲两次
            					new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_previous")).click();//上一曲
            					//上一曲(播放时间小于3秒就下一首，否则当前歌曲重新播放)
            					sleep(1000);//这个延时必须要,等待开始播放后再上一曲
            					new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_previous")).click();
        					}else {//播放时间小于于3秒只需要点击上一曲1次
        						new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_previous")).click();
        					}
        					sleep(10000);//上一曲 播放10s
        					String preMusicTitle = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_title")).getText();
        					if (preMusicTitle.equals(musicTitle)) {
        						logAndPrint("After Previous Playing:" + preMusicTitle + "-->Previous Pass");
        						logForResult(testCaseCounter + "ReconnectBTThenHuNextAndPre() pass"); 
        					}else {
        						logAndPrint("Previous Failed");
        						logForResult(testCaseCounter + "ReconnectBTThenHuNextAndPre() fail");
        					}
        				}
        			}else {
        				logForResult(testCaseCounter + "ReconnectBTThenHuNextAndPre() fail: music title is null");
					}
    			}else {
    				logForResult(testCaseCounter + "ReconnectBTThenHuNextAndPre() fail: reconnect Failed!");
    			}
    		} catch (UiObjectNotFoundException e) {
    			logForResult(testCaseCounter + "ReconnectBTThenHuNextAndPre failed: UiObject not found");
    		}
		}else {
			logForResult(testCaseCounter + "ReconnectBTThenHuNextAndPre fail: testphone is not connected");
		}

	}
	
	
	
UiObject musicTitleObj = new UiObject(new UiSelector()
		.resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_title"));
UiObject singerNameObj = new UiObject(new UiSelector()
		.resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_artist_name"));
UiObject albumNameObj = new UiObject(new UiSelector()
		.resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_Album_name"));
UiObject nextSongObj = new UiObject(new UiSelector()
		.resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_next"));
String musicTitle;
String singerName;
String albumName;
int checkID3Counter = 0;
while(checkID3Counter<3){
	musicTitle = musicTitleObj.getText();
	singerName = singerNameObj.getText();
	albumName = albumNameObj.getText();
	if (singerName.equals("") && albumName.equals("")) {
		checkID3Counter ++;
		if (checkID3Counter == 3) {
			logForResult(testCaseCounter + "ID3InfoCheck() fail: ID3 is null for 3 songs");
		}else {
			logAndPrint("This song ID3 is null,checking another song...");
			nextSongObj.click();//next song
			sleep(1000);
		}
	}else {
		if ((!singerName.equals("<unknown>")) || (!albumName.equals("<unknown>"))) {
			logAndPrint("ID3 Check-->Title:  " + musicTitle + ",Singer:" + singerName + ",Album:" + albumName);
			logForResult(testCaseCounter + "ID3InfoCheck() pass");
			checkID3Counter = 5;// bigger than 3 to exit while
		}else {
			checkID3Counter ++;
			if (checkID3Counter==3) {
				logForResult(testCaseCounter + "ID3InfoCheck() fail: ID3 is unknown for 3 songs,Please check later");
			}else {
				logForResult(testCaseCounter + "ID3InfoCheck(): This song ID3 unknown,checking another song...");
				nextSongObj.click();//next song
				sleep(1000);
			}
		}
	}
}
sleep(2000);