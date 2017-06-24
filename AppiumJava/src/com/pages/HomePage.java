package com.pages;

import com.base.PageAppium;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class HomePage extends PageAppium {

	//home页面的设置按钮
    public final String SETTINGS_BTN_ID = "com.android.launcher:id/ts_home_button_item_settings_id";
    //home页面的媒体按钮
    public final String MEDIA_BTN_ID = "com.android.launcher:id/ts_home_button_item_media_id";
	//Media页面的source按钮
    public final String SOURCE_BTN_ID = "com.thundersoft.mediaplayer:id/media_source";
//	UiObject nvaObj = new UiObject(new UiSelector()
//			.resourceId("com.android.launcher:id/ts_home_button_item_nav_id"));
//	UiObject saveDrivingObj = new UiObject(new UiSelector()
//			.resourceId("com.android.launcher:id/ts_home_button_item_safe_id"));
//	UiObject multimediaObj = new UiObject(new UiSelector()
//			.resourceId("com.android.launcher:id/ts_home_button_item_media_id"));
//	UiObject appsObj = new UiObject(new UiSelector()
//			.resourceId("com.android.launcher:id/ts_home_button_item_apps_id"));
//	UiObject phoneObj = new UiObject(new UiSelector()
//			.resourceId("com.android.launcher:id/ts_home_button_item_call_id"));
//	UiObject settingObj = new UiObject(new UiSelector()
//			.resourceId("com.android.launcher:id/ts_home_button_item_settings_id"));
    
	public HomePage(AndroidDriver driver) {
		super(driver);
	}
	
    /**
     * 是否在home界面
     */
    public boolean isHome(){
        return isIdElementExist(SETTINGS_BTN_ID,3,true);
    }
    
    /**
     * 获取设置按钮对象
     */
    public AndroidElement getHomeSettingButton(){
        return findById(SETTINGS_BTN_ID);
    }
    
    /**
     * 获取媒体按钮对象
     */
    public AndroidElement getHomeMediaButton(){
    	return findById(MEDIA_BTN_ID);
    }
    /**
     * 获取媒体按钮对象
     */
    public AndroidElement getBtSourceButton(){
    	return findById(SOURCE_BTN_ID);
    }

}
