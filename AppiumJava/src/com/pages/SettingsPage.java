package com.pages;

import com.base.PageAppium;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class SettingsPage extends PageAppium {

	//设置页面的wifi tab
    public final String SETTINGS_WIFI_TAB_ID = "com.android.settings:id/ts_tab_wifi_id";
    public final String SETTINGS_WIFI_SWITCH_ID = "com.android.settings:id/switch_view";
    
    
    //设置页面的蓝牙 tab
    public final String SETTINGS_BT_TAB_ID = "com.android.settings:id/ts_tab_bluetooth_id";
    //设置页面的音效 tab
    public final String SETTINGS_SOUND_TAB_ID = "com.android.settings:id/ts_tab_sound_id";
    
    //设置页面的显示 tab
    public final String SETTINGS_DISPLAY_TAB_ID = "com.android.settings:id/ts_tab_display_id";
    public final String DISPLAY_SETTINGS_LIST_ID = "com.android.settings:id/ts_display_settings_list";
    public final String DISPLAY_SETTINGS_ITEM_ID = "com.android.settings:id/ts_item_txt";
    
    
    //设置页面的时间 tab
    public final String SETTINGS_TIME_TAB_ID = "com.android.settings:id/ts_tab_time_id";
    //设置页面的系统 tab
    public final String SETTINGS_SYSTEM_TAB_ID = "com.android.settings:id/ts_tab_system_id";
	
	
	/**
	 * 用于把driver传递到此页面，使操作的都是已初始化的同一个设备
	 * */
	public SettingsPage(AndroidDriver androidDriver) {
		super(androidDriver);
	}

	
	/**
	 * 获取SETTINGS_WIFI_TAB_ID对象
	 */
	public AndroidElement getWifiTabBtn(){
		return findById(SETTINGS_WIFI_TAB_ID);
	}
	/**
	 * 获取SETTINGS_WIFI_TAB_ID对象
	 */
	public AndroidElement getWifiSwitch(){
		return findById(SETTINGS_WIFI_SWITCH_ID);
	}
	
	
	/**
	 * 获取SETTINGS_BT_TAB_ID对象
	 */
	public AndroidElement getBtTabBtn(){
		return findById(SETTINGS_BT_TAB_ID);
	}
	
	/**
	 * 获取SETTINGS_SOUND_TAB_ID对象
	 */
	public AndroidElement getSoundTabBtn(){
		return findById(SETTINGS_SOUND_TAB_ID);
	}
	
	/**
	 * 获取SETTINGS_DISPLAY_TAB_ID对象
	 */
	public AndroidElement getDisplayTabBtn(){
		return findById(SETTINGS_DISPLAY_TAB_ID);
	}
	
	/**
	 * 获取语言设置对象
	 */
	public AndroidElement getLanguageSettingBtn(){
		return findByNameEnCh("语言设置", "Language", "语言设置");
		
	}
	/**
	 * 获取简体中文对象
	 */
	public AndroidElement getChineseBtn(){
		return findByName("简体中文", "简体中文");
		
	}
	/**
	 * 获取英文对象
	 */
	public AndroidElement getEnglishBtn(){
		return findByName("English", "English");
		
	}
	
	/**
	 * 获取SETTINGS_TIME_TAB_ID对象
	 */
	public AndroidElement getTimeTabBtn(){
		return findById(SETTINGS_TIME_TAB_ID);
	}
	
	/**
	 * 获取SETTINGS_SYSTEM_TAB_ID对象
	 */
	public AndroidElement getSystemTabBtn(){
		return findById(SETTINGS_SYSTEM_TAB_ID);
	}
	
	
}
