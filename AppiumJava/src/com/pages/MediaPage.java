package com.pages;

import com.base.PageAppium;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class MediaPage extends PageAppium {

	//Media页面的source按钮
    public final String SOURCE_BTN_ID = "com.thundersoft.mediaplayer:id/media_source";
    //Media页面的媒体标题
    public final String MEDIA_TITLE_TEXT_ID = "com.thundersoft.mediaplayer:id/media_title";
    //歌曲已播放时间
    public final String START_TIME_TEXT_ID = "com.thundersoft.mediaplayer:id/tv_start_time";
    //歌曲总时间
    public final String END_TIME_TEXT_ID = "com.thundersoft.mediaplayer:id/tv_end_time";
    //下一个媒体
    public final String NEXT_MEDIA_BTN_ID = "com.thundersoft.mediaplayer:id/media_next";
    //上一个媒体
    public final String PREVIOUS_MEDIA_BTN_ID = "com.thundersoft.mediaplayer:id/media_pre";
    //歌曲名
    public final String MUSIC_TITLE_TEXT_ID = "com.thundersoft.mediaplayer:id/tv_ts_media_music_title";
    //歌手
    public final String MUSIC_ARTIST_TEXT_ID = "com.thundersoft.mediaplayer:id/tv_ts_media_music_artist_name";
    //专辑
    public final String MUSIC_ALBUM_TEXT_ID = "com.thundersoft.mediaplayer:id/tv_ts_media_music_Album_name";
    //暂停/播放按钮
    public final String PAUSE_MUSIC_BTN_ID = "com.thundersoft.mediaplayer:id/iv_ts_media_btn_pause";
    //上一曲
    public final String PREVIOUS_MUSIC_BTN_ID = "com.thundersoft.mediaplayer:id/iv_ts_media_btn_previous";
    //下一曲
    public final String NEXT_MUSIC_BTN_ID = "com.thundersoft.mediaplayer:id/iv_ts_media_btn_next";
    
    //source
    public final String BT_SOURCE_BTN_ID = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_device_bt";
    public final String FM_SOURCE_BTN_ID = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_device_radio";
    public final String SD_SOURCE_BTN_ID = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_card";
    
    //选择source后，如果是本地、u盘、sdcard会有选择音乐/视频/图片
    public final String MUSIC_MEDIA_TYPE_BTN_ID = "com.thundersoft.mediaplayer:id/ts_media_button_item_music_id";
    public final String VIDEO_MEDIA_TYPE_BTN_ID = "com.thundersoft.mediaplayer:id/media_title";
    public final String PICTURE_MEDIA_TYPE_BTN_ID = "com.thundersoft.mediaplayer:id/media_title";
    
    //图片播放暂停按钮
    public final String PICTURE_PAUSE_BTN_ID = "com.thundersoft.mediaplayer:id/iv_ts_media_btn_pause_picture";
    
    //选择音乐后出现的音乐状态栏
    public final String MUSIC_STATUS_BAR_BTN_ID = "com.thundersoft.mediaplayer:id/rl_media_music_all_statusbar";
    
    //点击快捷应用后出现的快捷应用页，第一个应用（锁屏）
    public final String SHORTCUT_BTN_ID = "com.android.systemui:id/ts_dialog_app_0";
    //锁屏后显示的时间界面
    public final String LOCKSCREEN_TIME_TEXT_ID = "com.android.launcher:id/textTime";
    
    
    
	public MediaPage(AndroidDriver androidDriver) {
		super(androidDriver);
	}
	
	/**
	 * 获取NEXT_MUSIC_BTN_ID对象
	 */
	public AndroidElement getLockScreenButton(){
		return findById(SHORTCUT_BTN_ID);
	}
	
	/**
	 * 获取NEXT_MUSIC_BTN_ID对象
	 */
	public boolean waitLockScreenButtonExists(){
		return waitForExists(SHORTCUT_BTN_ID,3000);
	}
	
	/**
	 * 获取NEXT_MUSIC_BTN_ID对象
	 */
	public boolean waitLockScreenTimeExists(){
		return waitForExists(LOCKSCREEN_TIME_TEXT_ID,3000);
	}
	
	/**
	 * 获取LOCKSCREEN_TIME_TEXT_ID对象
	 */
	public AndroidElement getLockScreenTimeButton(){
		return findById(LOCKSCREEN_TIME_TEXT_ID);
	}
	
	/**
	 * 获取NEXT_MUSIC_BTN_ID对象
	 */
	public AndroidElement getNextMusicButton(){
		return findById(NEXT_MUSIC_BTN_ID);
	}
	
	/**
	 * 获取PREVIOUS_MUSIC_BTN_ID对象
	 */
	public AndroidElement getPreviousMusicButton(){
		return findById(PREVIOUS_MUSIC_BTN_ID);
	}
    /**
     * 获取END_TIME_TEXT_ID对象
     */
    public AndroidElement getEndTimeText(){
    	return findById(END_TIME_TEXT_ID);
    }
    
    /**
     * 获取PREVIOUS_MEDIA_BTN_ID对象
     */
    public AndroidElement getMusicTitleText(){
    	return findById(MUSIC_TITLE_TEXT_ID);
    }
    /**
     * 获取MUSIC_ARTIST_TEXT_ID对象
     */
    public AndroidElement getMusicArtistText(){
    	return findById(MUSIC_ARTIST_TEXT_ID);
    }
    /**
     * 获取MUSIC_ALBUM_TEXT_ID对象
     */
    public AndroidElement getMusicAlbumText(){
    	return findById(MUSIC_ALBUM_TEXT_ID);
    }

	/**
     * 获取source按钮对象
     */
    public AndroidElement getSourceButton(){
        return findById(SOURCE_BTN_ID);
    }
    
    /**
     * 获取BT_SOURCE_BTN_ID按钮对象
     */
    public AndroidElement getBtSourceButton(){
    	return findById(BT_SOURCE_BTN_ID);
    }
    /**
     * 获取FM_SOURCE_BTN_ID按钮对象
     */
    public AndroidElement getFMSourceButton(){
    	return findById(FM_SOURCE_BTN_ID);
    }
    /**
     * 获取BT_SOURCE_BTN_ID按钮对象
     */
    public AndroidElement getMediaSourceTitleText(){
    	return findById(MEDIA_TITLE_TEXT_ID);
    }
    /**
     * 获取StartTime对象
     */
    public AndroidElement getStartTimeText(){
    	return findById(START_TIME_TEXT_ID);
    }
    /**
     * 获取NEXT_MEDIA_BTN_ID对象
     */
    public AndroidElement getNextMediaButton(){
    	return findById(NEXT_MEDIA_BTN_ID);
    }
    /**
     * 获取PREVIOUS_MEDIA_BTN_ID对象
     */
    public AndroidElement getPreviousMediaButton(){
    	return findById(PREVIOUS_MEDIA_BTN_ID);
    }
    
    /**
     * 获取PAUSE_MUSIC_BTN_ID对象
     */
    public AndroidElement getPausePlayButton(){
    	return findById(PAUSE_MUSIC_BTN_ID);
    }

}
