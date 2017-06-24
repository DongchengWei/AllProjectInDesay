package com.compa;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.core.UiWatcher;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.utils.RunTestCase;

import android.os.RemoteException;
import android.util.Log;


public class CompaTest extends UiAutomatorTestCase {

	static final String BLUETOOTH_DEVICE_NAME = "华为P9";		//待测手机蓝牙名字
	static final String BLUETOOTH_HELP_DEVICE_NAME = "helpphone";	//辅助手机蓝牙名字
	static final int SECONDS30 = 30000;								//等待超时时间
	static final int RETRY_MAX_COUNTER = 3;							//失败重试次数（局部）
	int testCaseCounter = 0;
	static final int CONNECT_ALL = 1;
	static final int CONNECT_HFP = 2;
	static final int CONNECT_A2DP = 3;
	static final int CONNECT_FAIL = 0;
	
	//resourceId常量
	static final String WIFI = "com.android.settings:id/ts_tab_wifi_id";
	static final String BLUETOOTH = "com.android.settings:id/ts_tab_bluetooth_id";
	static final String SETTINGS = "com.android.launcher:id/ts_home_button_item_settings_id";//蓝牙设置可被发现
	static final String BTSETTING = "com.android.settings:id/ts_bluetooth_settings";
	static final String MULTIMEDIA = "com.android.launcher:id/ts_home_button_item_media_id";
	static final String ACCEPT_CALL = "com.thundersoft.call:id/img_accept";
	static final String REFUSE_CALL = "com.thundersoft.call:id/img_refuse";
	static final String BT_TAB = "com.android.settings:id/ts_tab_bluetooth_id";
	//source
	static final String LOCAL_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_local";
	static final String USB1_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_usb1";
	static final String USB2_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_usb2";
	static final String SD_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_card";
	static final String FM_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_device_radio";
	static final String BT_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_device_bt";
	static final String IPOD_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_device_ipod";
	static final String AUX_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_device_aux";
	
	//蓝牙设置界面
	static final String HFP_A2DP_SELECT = "com.android.settings:id/item_profile_layout";
	
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("Compatible",  
				 "com.compa.CompaTest", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
		 
	public void testDemo(){
	
		try{
			PrintInfo();
			stopRunningWatcher();
			String startTestTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			logAndPrint("start test time: " + startTestTimeStr);
			getUiDevice().wakeUp();
			
			//蓝牙连接
			testCaseCounter ++;
			HuPairedPhoneReject();							//1.先匹配（主机发起配对）,手机取消请求
			testCaseCounter ++;
			HuPairedCheck();								//2.先匹配（主机发起配对）,手机确认请求
			testCaseCounter ++;
			HuConnectA2DP();								//3.Connects A2DP By HU由车机连接媒体音频（A2DP）
			testCaseCounter ++;
			HuDisconnectA2DP();								//4.Disconnected A2DP By HU由车机断开媒体音频（A2DP）
			testCaseCounter ++;
			HuConnectHfp();									//5.Connects HFP By HU由车机连接电话音频（HFP）
			testCaseCounter ++;
			HuDisconnectHFP();								//6.Disconnected HFP By HU由车机断开电话音频（HFP)
			testCaseCounter ++;
			HuConnectToPhone(); 							//7.主机连接手机所有协议
			testCaseCounter ++;
			HuDisconnect();									//8.主机断开连接所有协议
			testCaseCounter ++;
			HuSwitchA2DP();									//9.在车机上切换两台手机的A2DP连接
			testCaseCounter ++;
			HuSwitchHFP();									//10.在车机上切换两台手机的HFP连接
			testCaseCounter ++;
			switchDeviceTest();								//11.切换连接手机（已有配对记录但当前连接的是另一手机）
			testCaseCounter ++;
			PhoneConnToHu();								//12.手机连接车机测试            
			testCaseCounter ++;
			PhoneDisconnectA2DP();							//13.由手机断开媒体音频连接(A2DP)
			testCaseCounter ++;
			PhoneDisconnectHFP();							//14.由手机断开电话音频连接(HFP)
			testCaseCounter ++;
			PhoneDisconnect();  							//15.手机断开连接车机	
			testCaseCounter ++;
			RebootHuBTAutoConnectCheck();					//16.主机重启蓝牙开关，自动连接检查                       
			testCaseCounter ++;
			RebootPhoneBT();								//17.手机重启蓝牙开关检查蓝牙是否自动连接
			testCaseCounter ++;
			RebootPhonePower();								//18.重启手机检查蓝牙是否自动连接
			testCaseCounter ++;
			BtPhoneNameDisplay();							//19.BT Phone name display /车机端手机名称的显示
			testCaseCounter ++;
			BtOutRange();									//20.out of range超出连接范围，重连
			
			//蓝牙电话
			testCaseCounter ++;
			HuAnswerAndEndCall();							//1.2.主机接电话,结束通话
			testCaseCounter ++;
			HuChangePrivate();								//3.主机切换免提/私密模式
			testCaseCounter ++;
			HuRejectCall();									//4.Rejected a call by HU由主机拒接
			testCaseCounter ++;
			PhoneDialAndEndCall();							//5.6.由手机拨打电话,由手机结束通话
			testCaseCounter ++;
			PhoneAnswerACall();								//7.由手机接电话
			testCaseCounter ++;
			PhoneRejectACall();								//8.Rejected a call by phone由手机拒接
			testCaseCounter ++;
			PhoneVoiceAssistant();							//9.手机语音助手（声音输出）|播放蓝牙音乐时启动/退出语音助手
			testCaseCounter ++;
			WebChatVoicePlay();								//10.微信的语音（手机端播放收到的微信语音）
			testCaseCounter ++;
			CallQuality();									//11.12.Call quality/通话质量
															//13.Incoming call while in reversing(Bell)/倒车时来电(注意来电铃声)
			
			//蓝牙短信
			testCaseCounter ++;
			DialFromPhonebook();							//1.从车机端下载的通讯录中拨打电话
			testCaseCounter ++;
			DialFromMessageSender();						//2.拨打短信发送人电话
			testCaseCounter ++;
			SMSDownload();									//3.短信下载
			testCaseCounter ++;
			SMSReply();										//4.短信回复
			testCaseCounter ++;
			IncomingShowContack();							//5.来电显示头像和联系人名称
			testCaseCounter ++;
			DownloadPhonebook();							//6.7.手机电话本下载正常（显示正常）sim电话
			testCaseCounter ++;
			SmsIncomingAlarm();								//8.SMS get/短信提醒
			testCaseCounter ++;
			HuDeleteSMS();									//SMS Deleted from HU/车机短信删除
			
			
			//蓝牙音乐
			testCaseCounter ++;
			ReconnectBTThenHuNextAndPre();					//1.重新连接蓝牙后主机上下一曲	OK
			testCaseCounter ++;
			HuStopStartPlay();								//2.由主机播放停止				OK
			testCaseCounter ++;
	        PlayListSuppurt();								//3.播放列表支持				OK
	        testCaseCounter ++;
			HuChangeLoopMode();								//4.车机更改随机与重复模式（手机模式随着变化）
			testCaseCounter ++;
			PhoneChangePlayMode();							//5.手机更改随机与重复模式（车机模式随着变化）
			testCaseCounter ++;
			SwitchWIFIIsPlayNormal();						//6.播放音乐时打开wifi开关开音乐是否正常
			testCaseCounter ++;
			ID3InfoCheck();									//7.主机ID3功能检查
			testCaseCounter ++;
			BTPlayingIncallAcceptEndcall();					//8.蓝牙音乐播放->来电->接听->挂断->恢复蓝牙音乐
			testCaseCounter ++;
			BTPlayingIncallReject();						//9.蓝牙音乐播放->来电->拒接->恢复蓝牙音乐
			testCaseCounter ++;
			BTplayingHuDialOnlineEndcall();					//10.蓝牙音乐播放->车机拨号->通话->挂断->恢复蓝牙音乐				
			testCaseCounter ++;
			PhoneMusicNextAndPrevious();					//11.Track up/down by phone由手机执行上/下曲
			testCaseCounter ++;
			PhoneMusicPlayAndPause();						//12.Play/pause by phone由手机执行播放/暂停
			testCaseCounter ++;
			HuVolAdjust();									//13.HU VOL adjust主机音量调整
			testCaseCounter ++;
			PhoneVolAdjust();								//14.Phone VOL adjust手机音量调整
			testCaseCounter ++;
			PhoneThirdMusicPlayer();						//15.第三方播放器(手机端)（AVRCP操控、ID3显示）
			testCaseCounter ++;
			HuLaunchPhoneMusicPlayer();						//16.手機不須先進入播放音樂程式，才能啟動
			testCaseCounter ++;
			PlayingListSurport();							//17.正在播放列表支持
			
			testCaseCounter ++;
			PlayMusicForLongTime();							//9.播放音乐较长时间， 大约20分钟
			
			String endTestTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			logAndPrint("start test time: " + startTestTimeStr);
			logAndPrint("end test time: " + endTestTimeStr);
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	//车机删除短信
	private void HuDeleteSMS() {
		logAndPrint("HuDeleteSMS test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				getUiDevice().pressHome();
				new UiObject(new UiSelector()//电话通讯
						.resourceId("com.android.launcher:id/ts_home_button_item_call_id")).click();
				new UiObject(new UiSelector()//短信
						.resourceId("com.thundersoft.call:id/ts_call_menu_sms_id")).click();
				
				try {
					UiScrollable smsList = new UiScrollable( new UiSelector().
							resourceId("com.thundersoft.call:id/ts_sms_list_view"));	//获取listview对象
					smsList.getChildByText(new UiSelector()					//根据文本获取listview中对应item
							.className("android.widget.TextView"), "10086sim",true);		//滚动到有目标的界面
					new UiObject(new UiSelector()
							.resourceId("com.thundersoft.call:id/sms_delete")).click();//获取删除按钮目标并点击
					new UiObject(new UiSelector()
							.className("android.widget.TextView").text("10086sim")).click();//选择删除的短信
				} catch (UiObjectNotFoundException e) {
					logAndPrint("can not find 10086sim,find 10086 instead...");
					try {
						getUiDevice().pressHome();
						new UiObject(new UiSelector()//电话通讯
								.resourceId("com.android.launcher:id/ts_home_button_item_call_id")).click();
						new UiObject(new UiSelector()//短信
								.resourceId("com.thundersoft.call:id/ts_call_menu_sms_id")).click();
						UiScrollable smsList = new UiScrollable( new UiSelector().
								resourceId("com.thundersoft.call:id/ts_sms_list_view"));	//获取listview对象
						smsList.getChildByText(new UiSelector()					//根据文本获取listview中对应item
								.className("android.widget.TextView"), "10086",true);		//滚动到有目标的界面
						new UiObject(new UiSelector()
								.resourceId("com.thundersoft.call:id/sms_delete")).click();//获取删除按钮目标并点击
						new UiObject(new UiSelector()
								.className("android.widget.TextView").text("10086")).click();//选择删除的短信
					} catch (UiObjectNotFoundException e2) {
						logAndPrint("can not find 10086sim and 10086,find 移动  instead...");
						try {
							getUiDevice().pressHome();
							new UiObject(new UiSelector()//电话通讯
									.resourceId("com.android.launcher:id/ts_home_button_item_call_id")).click();
							new UiObject(new UiSelector()//短信
									.resourceId("com.thundersoft.call:id/ts_call_menu_sms_id")).click();
							UiScrollable smsList = new UiScrollable( new UiSelector().
									resourceId("com.thundersoft.call:id/ts_sms_list_view"));	//获取listview对象
							smsList.getChildByText(new UiSelector()					//根据文本获取listview中对应item
									.className("android.widget.TextView"), "移动",true);		//滚动到有目标的界面
							new UiObject(new UiSelector()
									.resourceId("com.thundersoft.call:id/sms_delete")).click();//获取删除按钮目标并点击
							new UiObject(new UiSelector()
									.className("android.widget.TextView").text("移动")).click();//选择删除的短信
						} catch (UiObjectNotFoundException e3) {
							throw new UiObjectNotFoundException("can not find 10086sim or 10086 or 移动!");
						}
					}
				}
				new UiObject(new UiSelector()
						.resourceId("com.thundersoft.call:id/sms_delete")).click();//获取删除按钮目标并点击
				logAndPrint("Delected,please check phone sms is delected");
				logAndPrint("ok press home and fail press back in 2min...");
				UiObject smsTitleUiObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.call:id/sms_center_title_tv"));
				UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
				if (smsTitleUiObj.waitUntilGone(120000)) {//大约2分钟
					if (homePageObj.exists()) {			//home界面
						logForResult(testCaseCounter + ".HuDeleteSMS() pass");
					}else {
						logForResult(testCaseCounter + ".HuDeleteSMS() fail");
					}
				}else {
					logForResult(testCaseCounter + ".HuDeleteSMS() fail: timeout");
				}
			}else{
				logForResult(testCaseCounter + ".HuDeleteSMS() fail: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuDeleteSMS() fail: UiObjectNotFoundException");
		}
	}

	//由手机断开电话音频连接(HFP)
	private void PhoneDisconnectHFP() {
		logAndPrint("PhoneDisconnectHFP test:");
		try {
			logAndPrint("Into BT connect");
			IntoSettingsFragments(BT_TAB);
			logAndPrint("Connect HFP first in 60s,then press home...");
			
			UiObject connectHfpObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/item_bt_icon_phone"));
			UiObject homePageObj = new UiObject(new UiSelector()
					.resourceId("com.android.launcher:id/ts_home_button_item_settings_id"));
			if (homePageObj.waitForExists(60000)) {
				IntoSettingsFragments(BT_TAB);
				if (connectHfpObj.exists()) {
					logAndPrint("Disconnect HFP via phone in 30s...");
					if (connectHfpObj.waitUntilGone(30000)) {
						logForResult(testCaseCounter + ".PhoneDisconnectHFP() pass");
					}else {
						logForResult(testCaseCounter + ".PhoneDisconnectHFP() fail: still connected");
					}
				}else {
					logForResult(testCaseCounter + ".PhoneDisconnectHFP() fail: HFP not connected first");
				}
			}else {
				logForResult(testCaseCounter + ".PhoneDisconnectHFP() fail: connect timeout");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PhoneDisconnectHFP() fail: UiObjectNotFoundException");
		}
	}

	//由手机断开媒体音频连接(A2DP)
	private void PhoneDisconnectA2DP() {
		logAndPrint("PhoneDisconnectA2DP test:");
		try {
			logAndPrint("Into BT connect");
			IntoSettingsFragments(BT_TAB);
			logAndPrint("Connect A2DP first in 60s,then press home...");
			UiObject connectA2dpObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/item_bt_icon_music"));
			UiObject homePageObj = new UiObject(new UiSelector()
					.resourceId("com.android.launcher:id/ts_home_button_item_settings_id"));
			if (homePageObj.waitForExists(60000)) {
				IntoSettingsFragments(BT_TAB);
				if (connectA2dpObj.exists()) {
					logAndPrint("Disconnect A2DP via phone in 30s...");
					if (connectA2dpObj.waitUntilGone(30000)) {
						logForResult(testCaseCounter + ".PhoneDisconnectA2DP() pass");
					}else {
						logForResult(testCaseCounter + ".PhoneDisconnectA2DP() fail: still connected");
					}
				}else {
					logForResult(testCaseCounter + ".PhoneDisconnectA2DP() fail: A2DP not connected first");
				}
			}else {
				logForResult(testCaseCounter + ".PhoneDisconnectA2DP() fail: connect timeout");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PhoneDisconnectA2DP() fail: UiObjectNotFoundException");
		}
	}

	//由主机切换HFP连接
	private void HuSwitchHFP() {
		logAndPrint("HuSwitchHFP test:");	
		try {
			logAndPrint("Into BT connect");
			IntoSettingsFragments(BT_TAB);
			
			logAndPrint("Disconnect BT via phone in 60s...");
			UiObject connectHfpObj = new UiObject(new UiSelector().
					resourceId("com.android.settings:id/item_bt_icon_phone"));
			UiObject connectA2dpObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/item_bt_icon_music"));
			connectHfpObj.waitUntilGone(60000);
			connectA2dpObj.waitUntilGone(60000);
			
			UiObject testphoneObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/ts_bluetooth_device_name")
					.text(BLUETOOTH_DEVICE_NAME));
			UiObject helpphoneObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/ts_bluetooth_device_name")
					.text(BLUETOOTH_HELP_DEVICE_NAME));
			UiObject connectTestObj = testphoneObj.getFromParent(new UiSelector()
					.resourceId("com.android.settings:id/item_profile_layout"));
			UiObject connectHelpObj = helpphoneObj.getFromParent(new UiSelector()
					.resourceId("com.android.settings:id/item_profile_layout"));
			connectTestObj.click();//点击进入选择testphone的HFP
			
			UiObject supportListObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/profile_check_list"));
			UiObject hfpObj = supportListObj.getChild(new UiSelector()
					.className("android.widget.RelativeLayout").index(1));//0为all,1为hfp,2为A2DP
			hfpObj.click();		//点击选择testphone的HFP
			UiObject confirmObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/profile_dialog_confirm"));
			
			logAndPrint("connect testphone HFP");
			confirmObj.click();
			if (connectHfpObj.waitForExists(30000)) {
				logAndPrint("connect helpphone HFP");
				connectHelpObj.click();	//点击进入选择helpphone的HFP
				hfpObj.click();			//点击选择helpphone的HFP
				confirmObj.click();		//确定连接
				if (connectHfpObj.waitForExists(30000)){
					logForResult(testCaseCounter + ".HuSwitchHFP() pass");
				}else {
					logForResult(testCaseCounter + ".HuSwitchHFP() fail:connect helpphone fail");
				}
			}else {
				logForResult(testCaseCounter + ".HuSwitchHFP() fail: connect testphone fail");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuSwitchHFP() fail: UiObjectNotFoundException");
		}		
	}

	//在车机上切换两台手机的A2DP连接
	private void HuSwitchA2DP() {
		logAndPrint("HuSwitchA2DP test:");	
		try {
			logAndPrint("Into BT connect");
			IntoSettingsFragments(BT_TAB);
			
			logAndPrint("Disconnect testphone via phone in 60s...");
			UiObject connectHfpObj = new UiObject(new UiSelector().
					resourceId("com.android.settings:id/item_bt_icon_phone"));
			UiObject connectA2dpObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/item_bt_icon_music"));
			if (connectHfpObj.exists() || connectA2dpObj.exists()) {
				connectHfpObj.waitUntilGone(60000);
				connectA2dpObj.waitUntilGone(60000);
			}
			
			UiObject testphoneObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/ts_bluetooth_device_name")
					.text(BLUETOOTH_DEVICE_NAME));
			UiObject helpphoneObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/ts_bluetooth_device_name")
					.text(BLUETOOTH_HELP_DEVICE_NAME));
			UiObject connectTestObj = testphoneObj.getFromParent(new UiSelector()
					.resourceId("com.android.settings:id/item_profile_layout"));
			UiObject connectHelpObj = helpphoneObj.getFromParent(new UiSelector()
					.resourceId("com.android.settings:id/item_profile_layout"));
			connectTestObj.click();//点击连接testphone的A2DP
			
			UiObject supportListObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/profile_check_list"));
			UiObject a2dpObj = supportListObj.getChild(new UiSelector()
					.className("android.widget.RelativeLayout").index(2));//0为all,1为hfp,2为A2DP
			a2dpObj.click();//点击连接testphone的A2DP
			
			UiObject confirmObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/profile_dialog_confirm"));
			
			logAndPrint("connect testphone A2DP");
			confirmObj.click();
			if (connectA2dpObj.waitForExists(30000)) {
				connectHelpObj.click();	//点击连接helpphone的A2DP
				a2dpObj.click();		//点击连接helpphone的A2DP
				logAndPrint("connect helpphone A2DP");
				confirmObj.click();
				if (connectA2dpObj.waitForExists(30000)){
					logForResult(testCaseCounter + ".HuSwitchA2DP() pass");
				}else {
					logForResult(testCaseCounter + ".HuSwitchA2DP() fail: connect helpphone fail");
				}
			}else {
				logForResult(testCaseCounter + ".HuSwitchA2DP() fail: connect testphone fail");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuConnectA2DP() fail: UiObjectNotFoundException");
		}
	}

	private void HuDisconnectHFP() {
		logAndPrint("HuDisconnectHFP test:");
		try {
			logAndPrint("Into BT connect");
			IntoSettingsFragments(BT_TAB);
			UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
			UiObject connectHfpObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/item_bt_icon_phone"));
			
			if (!connectHfpObj.exists()) {
				logAndPrint("Connect HFP first in 60s,if connected press home...");
				if (homePageObj.waitForExists(60000)) {
					IntoSettingsFragments(BT_TAB);
				}
			}
			logAndPrint("Disconnect HFP");
			new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/ts_bluetooth_device_name")
					.text(BLUETOOTH_DEVICE_NAME)).click();
			new UiObject(new UiSelector().	//确定
					resourceId("com.android.settings:id/dialog_confirm")).click();
			
			if (connectHfpObj.waitUntilGone(30000)) {
				int statusInt = ConnectStatus();
				if (statusInt == CONNECT_FAIL || statusInt == CONNECT_A2DP) {//如果只连接A2DP或者都没有连接
					logForResult(testCaseCounter + ".HuDisconnectHFP() pass");
				}else {
					logForResult(testCaseCounter + ".HuDisconnectHFP() fail: still connect");
				}
			}else {
				logForResult(testCaseCounter + ".HuDisconnectHFP() fail: connect not gone");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuDisconnectHFP() fail: UiObjectNotFoundException");
		}		
	}

	//Connects HFP By HU由车机连接电话音频（HFP）
	private void HuConnectHfp() {
		logAndPrint("HuConnectHfp test:");
		try {
			logAndPrint("Into BT connect");
			IntoSettingsFragments(BT_TAB);
			
			UiObject connectHfpObj = new UiObject(new UiSelector().
					resourceId("com.android.settings:id/item_bt_icon_phone"));
			UiObject connectA2dpObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/item_bt_icon_music"));
			logAndPrint("Disconnect via phone in 60s...");
			connectHfpObj.waitUntilGone(60000);
			connectA2dpObj.waitUntilGone(60000);
			
			if (!connectHfpObj.exists() && !connectA2dpObj.exists()) {
				logAndPrint("connect HFP");
				UiObject testphoneObj = new UiObject(new UiSelector()
						.resourceId("com.android.settings:id/ts_bluetooth_device_name")
						.text(BLUETOOTH_DEVICE_NAME));
				UiObject connectObj = testphoneObj.getFromParent(new UiSelector()
						.resourceId("com.android.settings:id/item_profile_layout"));
				connectObj.click();
				UiObject supportListObj = new UiObject(new UiSelector()
						.resourceId("com.android.settings:id/profile_check_list"));
				supportListObj.getChild(new UiSelector()
						.className("android.widget.RelativeLayout").index(1)).click();//0为all,1为hfp,2为A2DP
				new UiObject(new UiSelector()
						.resourceId("com.android.settings:id/profile_dialog_confirm")).click();
				if (connectHfpObj.waitForExists(30000)) {
					if (ConnectStatus() == CONNECT_HFP) {
						logForResult(testCaseCounter + ".HuConnectHfp() pass");
					}else {
						logForResult(testCaseCounter + ".HuConnectHfp() fail: not only connect HFP");
					}
				}else {
					logForResult(testCaseCounter + ".HuConnectHfp() fail: can not connect");
				}
			}else {
				logForResult(testCaseCounter + ".HuConnectHfp() fail: Please disconnect first");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuConnectHfp() fail: UiObjectNotFoundException");
		}
	}

	//Disconnected A2DP By HU由车机断开媒体音频（A2DP） to be test
	private void HuDisconnectA2DP() {
		logAndPrint("HuDisconnectA2DP test:");
		try {
			logAndPrint("Into BT connect");
			IntoSettingsFragments(BT_TAB);
			UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
			UiObject connectA2dpObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/item_bt_icon_music"));
			
			if (!connectA2dpObj.exists()) {
				logAndPrint("Connect A2DP first in 60s,if connected press home...");
				if (homePageObj.waitForExists(60000)) {
					IntoSettingsFragments(BT_TAB);
				}
			}
			
			logAndPrint("Disconnect A2DP");
			new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/ts_bluetooth_device_name")
					.text(BLUETOOTH_DEVICE_NAME)).click();
			new UiObject(new UiSelector().	//确定
					resourceId("com.android.settings:id/dialog_confirm")).click();
			if (connectA2dpObj.waitUntilGone(30000)) {
				if (ConnectStatus() == CONNECT_FAIL) {
					logForResult(testCaseCounter + ".HuDisconnectA2DP() pass");
				}else {
					logForResult(testCaseCounter + ".HuDisconnectA2DP() fail: others still connect");
				}
			}else {
				logForResult(testCaseCounter + ".HuDisconnectA2DP() fail: can not disconnect");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuDisconnectA2DP() fail: UiObjectNotFoundException");
		}
	}

	//Connects A2DP By HU由车机连接媒体音频（A2DP）to be test
	private void HuConnectA2DP() {
		logAndPrint("HuConnectA2DP test:");
		try {
			logAndPrint("Into BT connect");
			IntoSettingsFragments(BT_TAB);
			UiObject connectHfpObj = new UiObject(new UiSelector().
					resourceId("com.android.settings:id/item_bt_icon_phone"));
			UiObject connectA2dpObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/item_bt_icon_music"));
			logAndPrint("Disconnect via phone in 60s...");
			connectHfpObj.waitUntilGone(60000);
			connectA2dpObj.waitUntilGone(60000);
			if ((!connectHfpObj.exists()) && (!connectA2dpObj.exists())) {
				logAndPrint("connect A2DP...");
				UiObject testphoneObj = new UiObject(new UiSelector()
						.resourceId("com.android.settings:id/ts_bluetooth_device_name")
						.text(BLUETOOTH_DEVICE_NAME));
				UiObject connectObj = testphoneObj.getFromParent(new UiSelector()
						.resourceId("com.android.settings:id/item_profile_layout"));
				connectObj.click();
				UiObject supportListObj = new UiObject(new UiSelector()
						.resourceId("com.android.settings:id/profile_check_list"));
				supportListObj.getChild(new UiSelector()
						.className("android.widget.RelativeLayout").index(2)).click();//0为all,1为hfp,2为A2DP
				new UiObject(new UiSelector()
						.resourceId("com.android.settings:id/profile_dialog_confirm")).click();
				if (connectA2dpObj.waitForExists(30000)) {
					if (ConnectStatus() == CONNECT_A2DP) {
						logForResult(testCaseCounter + ".HuConnectA2DP() pass");
					}else {
						logForResult(testCaseCounter + ".HuConnectA2DP() fail: not only connect A2DP");
					}
				}else {
					logForResult(testCaseCounter + ".HuConnectA2DP() fail: can not connect");
				}
			}else {
				logForResult(testCaseCounter + ".HuConnectA2DP() fail: Please disconnect first");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuConnectA2DP() fail: UiObjectNotFoundException");
		}
	}

	//先匹配（主机发起配对）,手机取消请求
	private void HuPairedPhoneReject() {
		logAndPrint("HuPairedPhoneReject test:");
		try {
			logAndPrint("Into BT connect");
			IntoSettingsFragments(BT_TAB);
			logAndPrint("Ready,please pair testphone via HU and reject by Phone...");
			logAndPrint("OK press home and fail press BTsettings in 2min...");
			UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
			//蓝牙设置界面的齿轮按钮消失说明有操作
			UiObject btSettingObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/ts_bluetooth_settings"));
			if (btSettingObj.waitUntilGone(120000)) {//跳转
				if (homePageObj.exists()) {//如果跳转到了HOME界面
					logForResult(testCaseCounter + ".HuPairedPhoneReject() pass");
				}else {
					logForResult(testCaseCounter + ".HuPairedPhoneReject() fail");
				}
			}else {
				logForResult(testCaseCounter + ".HuPairedPhoneReject() fail: timeout");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuPairedPhoneReject() fail: UiObjectNotFoundException");
		}
	}


	//14.手机更改随机与重复模式（车机模式随着变化）
	private void PhoneChangePlayMode() {
		logAndPrint("PhoneChangePlayMode test:");
		try {
			IntoSettingsFragments(BT_TAB);
			UiObject btUiObj = new UiObject(new UiSelector()//蓝牙音乐界面
					.resourceId("com.thundersoft.mediaplayer:id/media_title"));
			UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
			if (WaitToConnectByHand()) {
				SwitchSource(BT_SOURCE);
				logAndPrint("Please check play mode via phone in 60s,ok press home fail press source...");
				if (btUiObj.waitUntilGone(120000)) {	//大约2分钟
					if (homePageObj.exists()) {			//home界面
						logForResult(testCaseCounter + ".PhoneChangePlayMode() pass");
					}else {
						logForResult(testCaseCounter + ".PhoneChangePlayMode() fail");
					}
				}else {
					logForResult(testCaseCounter + ".PhoneChangePlayMode() fail: timeout");
				}
			}else {
				logForResult(testCaseCounter + ".PhoneChangePlayMode() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PhoneChangePlayMode() fail: UiObjectNotFoundException");
		}
	}

	private void stopRunningWatcher(){
		//先要注册监听器
		UiDevice.getInstance().registerWatcher("stopRunning",new UiWatcher(){
			@Override
			public boolean checkForCondition()
			{
				UiObject messageDialogObj = new UiObject(new UiSelector().resourceId("android:id/message"));
				UiObject confirmBtnObj = new UiObject(new UiSelector().resourceId("android:id/button1"));
				if(messageDialogObj.exists())
				{
					try {
						logAndPrint("UiWatcher trigger: " + messageDialogObj.getText());
						confirmBtnObj.click();
						return true;
					} catch (UiObjectNotFoundException e) {
						logAndPrint("UiWatcher trigger: UiObjectNotFoundException");
						return false;
					}
				}else {
					return false;
				}
			}
		});
	}
	
	//48.播放音乐较长时间， 大约20分钟
	private void PlayMusicForLongTime() {
		logAndPrint("PlayMusicForLongTime test:");
		try {
			IntoSettingsFragments(BT_TAB);
			UiObject btUiObj = new UiObject(new UiSelector()
					.resourceId("com.thundersoft.mediaplayer:id/media_title"));
			UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
			if (WaitToConnectByHand()) {
				SwitchSource(BT_SOURCE);
				String startTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				
				logAndPrint("play music for 20min,start at " + startTimeStr);
				logAndPrint("pass press home and fail press source...");
				if (btUiObj.waitUntilGone(1200000)) {//大约20分钟
					String endTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					logAndPrint("play music end time at " + endTimeStr);
					if (homePageObj.exists()) {			//home界面
						logForResult(testCaseCounter + ".PlayMusicForLongTime() pass");
					}else {
						logForResult(testCaseCounter + ".PlayMusicForLongTime() fail");
					}
				}else {
					logForResult(testCaseCounter + ".PlayMusicForLongTime() pass: played for 20min");
				}
			}else {
				logForResult(testCaseCounter + ".PlayMusicForLongTime() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PlayMusicForLongTime() fail: UiObjectNotFoundException");
		}
	}

	private void SmsIncomingAlarm() {
		logAndPrint("SmsIncomingAlarm test:");
		try {
		IntoSettingsFragments(BT_TAB);
		if (WaitToConnectByHand()) {
				getUiDevice().pressHome();
				new UiObject(new UiSelector()//电话通讯
						.resourceId("com.android.launcher:id/ts_home_button_item_call_id")).click();
				new UiObject(new UiSelector()//短信
						.resourceId("com.thundersoft.call:id/ts_call_menu_sms_id")).click();
				new UiObject(new UiSelector()//写新短信
						.resourceId("com.thundersoft.call:id/sms_new_write")).click();
				new UiObject(new UiSelector()//收件人10086
						.resourceId("com.thundersoft.call:id/send_to_contacts")).setText("10086");

				logAndPrint("setText:1314");
				new UiObject(new UiSelector()
						.resourceId("com.thundersoft.call:id/chat_msg_input")).setText("1314");
				getUiDevice().pressEnter();
				sleep(1000);
				new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/chat_msg_send_newchat")).click();
				UiObject smsUiObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.call:id/sms_center_title_tv"));
				UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
				logAndPrint("sms sent,check Incoming alarm,ok press Home and fail press back in 60s...");
				if (smsUiObj.waitUntilGone(120000)) {
					if (homePageObj.exists()) {
						logForResult(testCaseCounter + ".SmsIncomingAlarm() pass");
					}else {
						logForResult(testCaseCounter + ".SmsIncomingAlarm() fail");
					}
				}else {
					logForResult(testCaseCounter + ".SmsIncomingAlarm() fail: timeout");
				}
			}else{
				logForResult(testCaseCounter + ".SmsIncomingAlarm() fail: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".SmsIncomingAlarm() fail: UiObjectNotFoundException");
		}
	}

	//46.Call quality/通话质量
	private void CallQuality() {
		logAndPrint("CallQuality test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				getUiDevice().pressHome();
				UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
				new UiObject(new UiSelector()
						.resourceId("com.android.launcher:id/ts_home_button_item_call_id")).click();
				new UiObject(new UiSelector()
						.resourceId("com.thundersoft.call:id/ts_call_menu_key_id")).click();
				logAndPrint("Please dial a number(test 10086) and check call quality...");
				UiObject callStateObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.call:id/call_state"));
				UiObject callBtnObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.call:id/btn_call"));
				if (callStateObj.waitForExists(60000)) {
					logAndPrint("on phone now,checking...");
					if (callStateObj.waitUntilGone(120000)) {//2分钟检查
						logAndPrint("call is end,ok press home and fail press back...");
						callBtnObj.waitUntilGone(60000);//1分钟选择结果
						if (homePageObj.exists()) {
							logForResult(testCaseCounter + ".CallQuality() pass");
						}else {
							logForResult(testCaseCounter + ".CallQuality() fail");
						}	
					}else {
						logForResult(testCaseCounter + ".CallQuality() fail: check timeout");
					}
				}else {
					logForResult(testCaseCounter + ".CallQuality() fail: dial fail");
				}
			}else {
				logForResult(testCaseCounter + ".CallQuality() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".CallQuality() fail: UiObjectNotFoundException");
		}
	}

	//45.正在播放列表支持
	private void PlayingListSurport() {
		logAndPrint("PlayingListSurport test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				SwitchSource(BT_SOURCE);
				logAndPrint("check Playing list,ok press Hu Home and fail press source...");
				UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
				UiObject btUiObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/media_title"));
				if (btUiObj.waitUntilGone(60000)) {//跳转
					if (homePageObj.exists()) {//如果跳转到了HOME界面
						logForResult(testCaseCounter + ".PlayingListSurport() pass");
					}else {
						logForResult(testCaseCounter + ".PlayingListSurport() fail");
					}
				}else {
					logForResult(testCaseCounter + ".PlayingListSurport() fail: timeout");
				}
			}else {
				logForResult(testCaseCounter + ".PlayingListSurport() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PlayingListSurport() fail: UiObjectNotFoundException");
		}
	}

	//44.手機不須先進入播放音樂程式，才能啟動
	private void HuLaunchPhoneMusicPlayer() {
		logAndPrint("HuLaunchPhoneMusicPlayer test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				SwitchSource(FM_SOURCE);
				logAndPrint("Exit phone musicplayer and press home in 60s...");
				UiObject btUiObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/media_title"));
				UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
				if (btUiObj.waitUntilGone(60000)) {//界面消失说明有动作
					if (homePageObj.exists()) {		//如果是主界面
						SwitchSource(BT_SOURCE);
						logAndPrint("check phone player is launch?Yes press Home,No press source...");
						if (btUiObj.waitUntilGone(60000)) {
							if (homePageObj.exists()) {
								logForResult(testCaseCounter + ".HuLaunchPhoneMusicPlayer() pass");
							}else {
								logForResult(testCaseCounter + ".HuLaunchPhoneMusicPlayer() fail");
							}
						}else {
							logForResult(testCaseCounter + ".HuLaunchPhoneMusicPlayer() fail: check timeout");
						}
					}else {
						logForResult(testCaseCounter + ".HuLaunchPhoneMusicPlayer() fail: not homepage");
					}
				}else {
					logForResult(testCaseCounter + ".HuLaunchPhoneMusicPlayer() fail: exit phone player timeout");
				}
			}else {
				logForResult(testCaseCounter + ".HuLaunchPhoneMusicPlayer() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuLaunchPhoneMusicPlayer() fail: UiObjectNotFoundException");
		}
	}

	//43.BT Phone name display /车机端手机名称的显示
	private void BtPhoneNameDisplay() {
		logAndPrint("BtPhoneNameDisplay test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				logAndPrint("check BT phone name,ok press Hu Home and fial press BtSettings...");
				UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
				//蓝牙设置界面的齿轮按钮消失说明有操作
				UiObject btSettingObj = new UiObject(new UiSelector()
						.resourceId("com.android.settings:id/ts_bluetooth_settings"));
				if (btSettingObj.waitUntilGone(90000)) {//跳转
					if (homePageObj.exists()) {//如果跳转到了HOME界面
						logForResult(testCaseCounter + ".BtPhoneNameDisplay() pass");
					}else {
						logForResult(testCaseCounter + ".BtPhoneNameDisplay() fail");
					}
				}else {
					logForResult(testCaseCounter + ".BtPhoneNameDisplay() fail: timeout");
				}
			}else {
				logForResult(testCaseCounter + ".BtPhoneNameDisplay() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".BtPhoneNameDisplay() fail: UiObjectNotFoundException");
		}
	}

	//42.微信的语音（手机端播放收到的微信语音）
	private void WebChatVoicePlay() {
		logAndPrint("WebChatVoicePlay test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				logAndPrint("play webchat voice,ok press Hu Home and fail press BtSetting...");
				//蓝牙设置界面的齿轮按钮消失说明有操作
				UiObject btSettingObj = new UiObject(new UiSelector()
						.resourceId("com.android.settings:id/ts_bluetooth_settings"));
				//手机播放微信语音时会跳转到正在通话界面
				UiObject callStateObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.call:id/call_state"));
				if (btSettingObj.waitUntilGone(90000)) {//跳转
					if (callStateObj.exists()) {//如果跳转到了通话界面
						logForResult(testCaseCounter + ".WebChatVoicePlay() pass");
						callStateObj.waitUntilGone(30000);
					}else {
						logForResult(testCaseCounter + ".WebChatVoicePlay() fail");
					}
				}else {
					logForResult(testCaseCounter + ".WebChatVoicePlay() fail: Phone voice assistant not ready");
				}
			}else {
				logForResult(testCaseCounter + ".WebChatVoicePlay() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".WebChatVoicePlay() fail: UiObjectNotFoundException");
		}
	}

	//41.手机语音助手（声音输出）|播放蓝牙音乐时启动/退出语音助手
	private void PhoneVoiceAssistant() {
		logAndPrint("PhoneVoiceAssistant test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				SwitchSource(BT_SOURCE);
				logAndPrint("play voice assistant,ok press Hu Home and fail press BtSetting...");
				UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
				//蓝牙设置界面的齿轮按钮消失说明有操作
				UiObject btUiObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/media_title"));
				if (btUiObj.waitUntilGone(90000)) {
					if (homePageObj.exists()) {//如果主动返回home则说明pass
						logForResult(testCaseCounter + ".PhoneVoiceAssistant() pass");
					}else {
						logForResult(testCaseCounter + ".PhoneVoiceAssistant() fail");
					}
				}else {
					logForResult(testCaseCounter + ".PhoneVoiceAssistant() fail: Phone voice assistant not ready");
				}
			}else {
				logForResult(testCaseCounter + ".PhoneVoiceAssistant() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PhoneVoiceAssistant() fail: UiObjectNotFoundException");
		}
	}

	//40.第三方播放器(手机端)（AVRCP操控、ID3显示）
	private void PhoneThirdMusicPlayer() {
		logAndPrint("PhoneThirdMusicPlayer test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				logAndPrint("into phone third MusicPlayer and Press Hu Home...");
				UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
				if (homePageObj.waitForExists(90000)) {
					logAndPrint("third player ready...");
					SwitchSource(BT_SOURCE);
					//检查ID3和AVRCP(60秒)
					logAndPrint("check ID3,play,pause,next,previous,ok press home,fail press source in 60s...");
					UiObject btUiObj = new UiObject(new UiSelector()
							.resourceId("com.thundersoft.mediaplayer:id/media_title"));
					btUiObj.waitUntilGone(120000);
					if (homePageObj.exists()) {
						logForResult(testCaseCounter + ".PhoneThirdMusicPlayer() pass");
					}else {
						logForResult(testCaseCounter + ".PhoneThirdMusicPlayer() fail");
					}
				}else {
					logForResult(testCaseCounter + ".PhoneThirdMusicPlayer() fail: third player not ready");
				}
			}else {
				logForResult(testCaseCounter + ".PhoneThirdMusicPlayer() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PhoneThirdMusicPlayer() fail: UiObjectNotFoundException");
		}
	}

	//39.out of range超出连接范围，重连
	private void BtOutRange() {
		logAndPrint("BtOutRange test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				logAndPrint("Please move phone out of BT range(10m) than back in 60s...");
				UiObject hfpObj = new UiObject(new UiSelector()
						.resourceId("com.android.settings:id/item_bt_icon_phone"));
				
				if (hfpObj.waitUntilGone(60000)) {
					logAndPrint("disconnected,wait for coming back in 60s...");
					if (hfpObj.waitForExists(90000)) {
						logForResult(testCaseCounter + ".BtOutRange() pass");
					}else {
						logForResult(testCaseCounter + ".BtOutRange() fail: reconnect fail");
					}
				}else {
					logForResult(testCaseCounter + ".BtOutRange() fail: can not disconnect");
				}
			}else {
				logForResult(testCaseCounter + ".BtOutRange() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".BtOutRange() fail: UiObjectNotFoundException");
		}
	}

	//38.Phone VOL adjust手机音量调整
	private void PhoneVolAdjust() {
		logAndPrint("PhoneVolAdjust test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				SwitchSource(BT_SOURCE);
				UiObject btUiObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/media_title"));
				logAndPrint("Adjust volume by Phone in 60s,OK press home and fail press source...");
				btUiObj.waitUntilGone(60000);
				UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
				if (homePageObj.exists()) {
					logForResult(testCaseCounter + ".PhoneVolAdjust() pass");
				}else {
					logForResult(testCaseCounter + ".PhoneVolAdjust() fail");
				}
			}else {
				logForResult(testCaseCounter + ".PhoneVolAdjust() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PhoneVolAdjust() fail: UiObjectNotFoundException");
		}
	}

	//37.HU VOL adjust主机音量调整
	private void HuVolAdjust() {
		logAndPrint("HuVolAdjust test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				SwitchSource(BT_SOURCE);
				UiObject btUiObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.mediaplayer:id/media_title"));
				logAndPrint("check play and adjust volume by HU in 60s,OK press home and fail press source...");
				btUiObj.waitUntilGone(60000);
				UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
				if (homePageObj.exists()) {
					logForResult(testCaseCounter + ".HuVolAdjust() pass");
				}else {
					logForResult(testCaseCounter + ".HuVolAdjust() fail");
				}
			}else {
				logForResult(testCaseCounter + ".HuVolAdjust() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuVolAdjust() fail: UiObjectNotFoundException");
		}
	}

	//35.手机电话本下载正常（显示正常） 36.sim卡电话本
	private void DownloadPhonebook() {
		logAndPrint("DownloadPhonebook() test:");
		try {
				IntoSettingsFragments(BT_TAB);
				if (WaitToConnectByHand()) {
					int retryCounter = 0;
					while(retryCounter < RETRY_MAX_COUNTER){
						retryCounter ++;
						getUiDevice().pressHome();
						UiObject homeCallObj = new UiObject(new UiSelector()//电话通讯
								.resourceId("com.android.launcher:id/ts_home_button_item_call_id"));
						homeCallObj.click();
						UiObject bookObj = new UiObject(new UiSelector()
								.resourceId("com.thundersoft.call:id/ts_call_menu_book_id"));//通讯录
						bookObj.click();
						UiObject bookTitleObj = new UiObject(new UiSelector()//通讯录标题
								.resourceId("com.thundersoft.call:id/txt_title"));
						UiScrollable contactListScr = new UiScrollable(new UiSelector()
								.resourceId("com.thundersoft.call:id/list_phonebook_contacts"));
						int contactCounter = contactListScr.getChildCount();
						if (contactCounter>0) {
							retryCounter = RETRY_MAX_COUNTER + 1;
							logForResult(testCaseCounter + ".DownloadPhonebook(cellphone) pass: contacts are " + contactCounter);
							testCaseCounter ++;
							//判断sim卡联系人是否下载(手机SIM保存10086为“10086sim”)
							UiObject simObj = contactListScr.getChildByText(new UiSelector()
									.resourceId("com.thundersoft.call:id/text_name"), "10086sim", true);
							UiObject numObj = simObj.getFromParent(new UiSelector()
									.resourceId("com.thundersoft.call:id/text_number"));
							if (simObj.exists()) {
								logForResult(testCaseCounter + ".DownloadPhonebook(sim) pass: " 
										+ simObj.getText() + "(" + numObj.getText() + ")");
							}else {
								logForResult(testCaseCounter + ".DownloadPhonebook(sim) fail: sim book no found");
							}
							logAndPrint("Check detail,press home to confirm pass, and back to fail in 60s...");
							bookTitleObj.waitUntilGone(60000);	//等待1分钟确认后转到其他界面
							if (homeCallObj.exists()) {			//如果返回的是home界面则确定pass
								logForResult(testCaseCounter + ".DownloadPhonebook() pass");
							}else{
								logForResult(testCaseCounter + ".DownloadPhonebook() fail");
							}
						}else {
							if (retryCounter == RETRY_MAX_COUNTER) {
								logForResult(testCaseCounter + ".DownloadPhonebook() fail: contacts are " + contactCounter);
							}else {
								logAndPrint("retry connect testphone " + retryCounter + "st time");
								if (ReconnectBluetooth()) {
									sleep(10000);//等十秒下载完成
								}else {
									logForResult(testCaseCounter + ".DownloadPhonebook() fail: testphone reconnect fail");
									retryCounter = RETRY_MAX_COUNTER + 1;
								}
							}
						}
					}//while
				}else {
					logForResult(testCaseCounter + ".DownloadPhonebook() fail: testphone not connected");
				}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".DownloadPhonebook() fail: UiObjectNotFoundException");
		}
	}

	//34.来电显示头像和联系人名称
	private void IncomingShowContack() {
		logAndPrint("IncomingShowContack() test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				SwitchSource(FM_SOURCE);
				logAndPrint("Ready, please call testphone in 60s...");
				UiObject acceptObj = new UiObject(new UiSelector().resourceId(ACCEPT_CALL));
				acceptObj.waitForExists(60000);
				if (acceptObj.exists()) {
					String callNameStr = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/call_name")).getText();
					logForResult(testCaseCounter + ".IncomingShowContack() pass: call name is " + callNameStr);
					sleep(3000);
					new UiObject(new UiSelector().resourceId(REFUSE_CALL)).click();
					sleep(3000);
				}else {
					logForResult(testCaseCounter + ".IncomingShowContack() fail: have no incoming call");
				}
			}else {
				logForResult(testCaseCounter + ".IncomingShowContack() fail: testphone is not connect");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".IncomingShowContack() fail: UiObjectNotFoundException");
		}
		
	}

	//33.短信回复
	private void SMSReply() {
		logAndPrint("SMSReply test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				getUiDevice().pressHome();
				new UiObject(new UiSelector()//电话通讯
						.resourceId("com.android.launcher:id/ts_home_button_item_call_id")).click();
				new UiObject(new UiSelector()//短信
						.resourceId("com.thundersoft.call:id/ts_call_menu_sms_id")).click();
				
				try {
					UiScrollable smsList = new UiScrollable( new UiSelector().
							resourceId("com.thundersoft.call:id/ts_sms_list_view"));	//获取listview对象
					smsList.getChildByText(new UiSelector()					//根据文本获取listview中对应item
							.className("android.widget.TextView"), "10086sim",true);		//滚动到有目标的界面
					new UiObject(new UiSelector()
							.className("android.widget.TextView").text("10086sim")).click();//获取目标并点击
				} catch (UiObjectNotFoundException e) {
					logAndPrint("can not find 10086sim,find 10086 instead...");
					try {
						getUiDevice().pressHome();
						new UiObject(new UiSelector()//电话通讯
								.resourceId("com.android.launcher:id/ts_home_button_item_call_id")).click();
						new UiObject(new UiSelector()//短信
								.resourceId("com.thundersoft.call:id/ts_call_menu_sms_id")).click();
						UiScrollable smsList = new UiScrollable( new UiSelector().
								resourceId("com.thundersoft.call:id/ts_sms_list_view"));	//获取listview对象
						smsList.getChildByText(new UiSelector()					//根据文本获取listview中对应item
								.className("android.widget.TextView"), "10086",true);		//滚动到有目标的界面
						new UiObject(new UiSelector()
								.className("android.widget.TextView").text("10086")).click();//获取目标并点击
					} catch (UiObjectNotFoundException e2) {
						logAndPrint("can not find 10086sim and 10086,find 移动 instead...");
						try {
							getUiDevice().pressHome();
							new UiObject(new UiSelector()//电话通讯
									.resourceId("com.android.launcher:id/ts_home_button_item_call_id")).click();
							new UiObject(new UiSelector()//短信
									.resourceId("com.thundersoft.call:id/ts_call_menu_sms_id")).click();
							UiScrollable smsList = new UiScrollable( new UiSelector().
									resourceId("com.thundersoft.call:id/ts_sms_list_view"));	//获取listview对象
							smsList.getChildByText(new UiSelector()					//根据文本获取listview中对应item
									.className("android.widget.TextView"), "移动",true);		//滚动到有目标的界面
							new UiObject(new UiSelector()
									.className("android.widget.TextView").text("移动")).click();//获取目标并点击
						} catch (UiObjectNotFoundException e3) {
							throw new UiObjectNotFoundException("can not find 10086sim or 10086 or 移动!");
						}
					}
				}
				logAndPrint("setText:LL");
				new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/chat_msg_input")).setText("ll");
				getUiDevice().pressEnter();
				getUiDevice().pressEnter();
				sleep(10000);
				logAndPrint("send");
				new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/chat_msg_send")).click();
				UiObject chatContentObj = new UiObject(new UiSelector()
						.resourceId("com.thundersoft.call:id/tv_chatcontent").text("ll"));
				if (chatContentObj.exists()) {
					logAndPrint("test ll exists");
					logForResult(testCaseCounter + ".SMSReply() pass: please check receive SMS form 10086 in 20s...");
				}else {
					logForResult(testCaseCounter + ".SMSReply() fail: sent SMS 'll' not found");
				}
			}else{
				logForResult(testCaseCounter + ".SMSReply() fail: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".SMSReply() fail: UiObjectNotFoundException");
		}
	}

	//断开蓝牙重新连接，必须之前已连接
	private boolean ReconnectBluetooth() {
		try {
			IntoSettingsFragments(BT_TAB);
			UiObject targetDevice = new UiObject(new UiSelector()
					.className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME));
			UiObject connectOKObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/item_bt_icon_phone"));
			UiObject confirmObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/dialog_confirm"));
			
			int retryCount = 0;
			boolean connectDone = false;
			while(retryCount < RETRY_MAX_COUNTER){
				retryCount ++;
				//15版本需要先重新连接才能比较容易下载到短信
				targetDevice.click();//点击断开
				confirmObj.click();	 //确认断开
				sleep(3000);
				connectOKObj.waitUntilGone(3000);
				if (!connectOKObj.exists()) {
					retryCount = RETRY_MAX_COUNTER +1;
					targetDevice.click();					//点击连接
					connectOKObj.waitForExists(30000);
					if (connectOKObj.exists()) {
						connectDone =  true;
					}else {
						connectDone =  false;
					}
				}else {
					if (retryCount == RETRY_MAX_COUNTER) {
						logAndPrint("ReconnectBluetooth fail: after retry " + RETRY_MAX_COUNTER + " times");
						connectDone = false;
					}else {
						connectDone = false;
					}
				}
			}
			return connectDone;
		} catch (UiObjectNotFoundException e) {
			logAndPrint("ReconnectBluetooth fail: UiObjectNotFoundException");
			return false;
		}
	}
	
	//32.短信下载
	private void SMSDownload() {
		logAndPrint("SMSDownload test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				int retryCounter = 0;
				while(retryCounter<RETRY_MAX_COUNTER){
					retryCounter ++;
					getUiDevice().pressHome();
					new UiObject(new UiSelector()//电话通讯
							.resourceId("com.android.launcher:id/ts_home_button_item_call_id")).click();
					new UiObject(new UiSelector()//短信
							.resourceId("com.thundersoft.call:id/ts_call_menu_sms_id")).click();
					
					UiObject smsCenterObj = new UiObject(new UiSelector()
							.resourceId("com.thundersoft.call:id/sms_center_title_tv"));
					String smsCenterText = smsCenterObj.getText();
					String smsCounterStr = smsCenterText.substring(smsCenterText.indexOf("(")+1, smsCenterText.indexOf(")"));
					logAndPrint("SMS Counter:" + smsCounterStr);
					int smsCounter = Integer.parseInt(smsCounterStr);
					if (smsCounter>0) {
						logForResult(testCaseCounter + ".SMSDownload() pass: Total SMS:" + smsCounter);
						retryCounter = RETRY_MAX_COUNTER + 1;//bigger than 3 to exit while loop
					}else {
						logAndPrint("SMS download fial,reconnect bluetooth and retry to download for " + retryCounter + "st time");
						if (retryCounter == RETRY_MAX_COUNTER) {
							logForResult(testCaseCounter + ".SMSDownload() fail: SMS counter is 0");
						}else {
							if (ReconnectBluetooth()) {
								sleep(10000);	// 10s wait for sms download
							}else {				//if reconnect fail test fail no need to continus
								logForResult(testCaseCounter + ".SMSDownload() fail: testphone reconnect fail");
								retryCounter = RETRY_MAX_COUNTER + 1;
							}
						}
					}
				}
			}else {
				logForResult(testCaseCounter + ".SMSDownload() fail: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".SMSDownload() fail: UiObjectNotFoundException");
		}
	}

	//31.拨打短信发送人电话(让第一个短信联系人为10086或是有效电话)
	private void DialFromMessageSender() {
		logAndPrint("DialFromMessageSender test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				int retryCounter = 0;
				while(retryCounter<RETRY_MAX_COUNTER){
					retryCounter ++;
					boolean isPass = false;
					try {
						getUiDevice().pressHome();
						new UiObject(new UiSelector()//电话通讯
								.resourceId("com.android.launcher:id/ts_home_button_item_call_id")).click();
						new UiObject(new UiSelector()//短信
								.resourceId("com.thundersoft.call:id/ts_call_menu_sms_id")).click();
						
						UiObject smsListObj = new UiObject( new UiSelector().
								resourceId("com.thundersoft.call:id/ts_sms_list_view"));
						UiObject smsContactObj = smsListObj.getChild(new UiSelector()
								.resourceId("com.thundersoft.call:id/sms_contact_name"));
						
						smsContactObj.waitForExists(30000);
						if (smsContactObj.exists()) {
							logAndPrint("sms exists");
							smsContactObj.click();
							new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/dial_out_form_sms")).click();
							UiObject endCallObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/btn_call"));
							UiObject callStateObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/call_state"));
							callStateObj.waitForExists(5000);
							String callStateStr = callStateObj.getText();
							if (callStateObj.exists()) {
								if (callStateStr.equals("通话中...")) {
									logForResult(testCaseCounter + ".DialFromMessageSender() pass");
									endCallObj.click();
									retryCounter = RETRY_MAX_COUNTER + 1;//exit while loop
									isPass = true;
								}else {
									if (retryCounter == RETRY_MAX_COUNTER) {
										logForResult(testCaseCounter + ".DialFromMessageSender() fail: call state____" + callStateStr);
									}
									endCallObj.click();
									isPass = false;
								}
							}else {
								if (retryCounter == RETRY_MAX_COUNTER) {
									logForResult(testCaseCounter + ".DialFromMessageSender() fail: call state UiObject not exists");
								}
								isPass = false;
							}
						}else {
							if (retryCounter == RETRY_MAX_COUNTER) {
								logForResult(testCaseCounter + ".DialFromMessageSender() fail: sms UiObject not exists");
							}
							isPass = false;
						}
					} catch (UiObjectNotFoundException e) {
						if (retryCounter == RETRY_MAX_COUNTER) {
							logForResult(testCaseCounter + ".DialFromMessageSender() fail: UiObjectNotFoundException");
						}
						isPass = false;
					}
					if (isPass == false) {
						if (ReconnectBluetooth()) {
							sleep(10000);
						}else {
							logForResult(testCaseCounter + ".DialFromMessageSender() fail: testphone reconnect fail");
							retryCounter = RETRY_MAX_COUNTER +1;
						}
					}
				}
			}else {
				logForResult(testCaseCounter + ".DialFromMessageSender() fail: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".DialFromMessageSender() fail: UiObjectNotFoundException");
		}
	}

	//30.从车机下载的通讯录中拨打电话（先在手机联系人保存10086为“移动”）
	private void DialFromPhonebook() {
		logAndPrint("DialFromPhonebook test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				UiObject targetDevice = new UiObject(new UiSelector()
						.className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME));
				UiObject connectOKObj = new UiObject(new UiSelector()
						.resourceId("com.android.settings:id/item_bt_icon_phone"));
				
				targetDevice.clickAndWaitForNewWindow();//点击断开
				new UiObject(new UiSelector().resourceId("com.android.settings:id/dialog_confirm")).click();
				sleep(3000);
				targetDevice.click();					//点击连接
				connectOKObj.waitForExists(30000);
				if (connectOKObj.exists()) {
					logAndPrint("reconnected!");
					sleep(15000);//等待下载短信
					//重新连接后开始打电话
					getUiDevice().pressHome();
					new UiObject(new UiSelector()//电话通讯
							.resourceId("com.android.launcher:id/ts_home_button_item_call_id")).click();
					new UiObject(new UiSelector()
							.resourceId("com.thundersoft.call:id/ts_call_menu_book_id")).click();
					
					UiScrollable contackList = new UiScrollable( new UiSelector().
		        			resourceId("com.thundersoft.call:id/list_phonebook_contacts"));//获取listview对象
					UiObject contackObj = contackList.getChildByText(new UiSelector()//根据文本获取listview中对应item
							.className("android.widget.TextView"), "10086",true);	 //滚动到有目标的界面
					
					contackObj.waitForExists(30000);
					if (contackObj.exists()) {
						logAndPrint("contact exists");
						contackObj = new UiObject(new UiSelector().className("android.widget.TextView").text("10086"));
						contackObj.click();
						new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/ic_dial_img")).click();
						UiObject endCallObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/btn_call"));
					    UiObject callStateObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/call_state"));
					    callStateObj.waitForExists(5000);
					    String callStateStr = callStateObj.getText();
					    if (callStateObj.exists()) {
					    	if (callStateStr.equals("通话中...")) {
					    		logForResult(testCaseCounter + ".DialFromPhonebook() pass");
					    		endCallObj.click();
							}else {
								logForResult(testCaseCounter + ".DialFromPhonebook() fail: call state____" + callStateStr);
								endCallObj.click();
							}
						}else {
							logForResult(testCaseCounter + ".DialFromPhonebook() fail: call state UiObject not exists");
						}
					}else {
						logForResult(testCaseCounter + ".DialFromPhonebook() fail: contact list UiObject not exists");
					}
				}else {
					logForResult(testCaseCounter + ".DialFromPhonebook() fail: reconnect testphone fail");
				}
			}else {
				logForResult(testCaseCounter + ".DialFromPhonebook() fail: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".DialFromPhonebook() fail:UiObjectNotFoundException");
		}
	}

	//29.由手机执行播放、暂停
	private void PhoneMusicPlayAndPause() {
		logAndPrint("PhoneMusicPlayAndPause test:");
		try {
			IntoSettingsFragments(BT_TAB);
			UiObject btUiObj = new UiObject(new UiSelector()	//蓝牙音乐界面
					.resourceId("com.thundersoft.mediaplayer:id/media_title"));
			UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
			if (WaitToConnectByHand()) {
				SwitchSource(BT_SOURCE);
				logAndPrint("Ready");
				logAndPrint("Please play and pause via phone in 60s,ok press home fail press source...");
				if (btUiObj.waitUntilGone(120000)) {			//大约2分钟
					if (homePageObj.exists()) {					//home界面
						logForResult(testCaseCounter + ".PhoneMusicPlayAndPause() pass");
					}else {
						logForResult(testCaseCounter + ".PhoneMusicPlayAndPause() fail");
					}
				}else {
					logForResult(testCaseCounter + ".PhoneMusicPlayAndPause() fail: timeout");
				}
			}else {
				logForResult(testCaseCounter + ".PhoneMusicPlayAndPause() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PhoneMusicPlayAndPause() fail: UiObjectNotFoundException");
		}
	}

	//28.由手机执行上下首歌曲
	private void PhoneMusicNextAndPrevious() {
		logAndPrint("PhoneMusicNextAndPrevious test:");
		try {
			IntoSettingsFragments(BT_TAB);
			UiObject btUiObj = new UiObject(new UiSelector()	//蓝牙音乐界面
					.resourceId("com.thundersoft.mediaplayer:id/media_title"));
			UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));
			if (WaitToConnectByHand()) {
				SwitchSource(BT_SOURCE);
				logAndPrint("Ready");
				logAndPrint("Please next and previous via phone in 60s,ok press home fail press source...");
				if (btUiObj.waitUntilGone(120000)) {			//大约2分钟
					if (homePageObj.exists()) {					//home界面
						logForResult(testCaseCounter + ".PhoneMusicNextAndPrevious() pass");
					}else {
						logForResult(testCaseCounter + ".PhoneMusicNextAndPrevious() fail");
					}
				}else {
					logForResult(testCaseCounter + ".PhoneMusicNextAndPrevious() fail: timeout");
				}
			}else {
				logForResult(testCaseCounter + ".PhoneMusicNextAndPrevious() fail: testphone not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PhoneMusicNextAndPrevious() fail: UiObjectNotFoundException");
		}
	}

	//27.Rejected a call by phone由手机拒接
	private void PhoneRejectACall() {
		logAndPrint("PhoneRejectACall test:");
		try {
			SwitchSource(FM_SOURCE);
			logAndPrint("Please dial testphone in 60s ...");
			UiObject acceptObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/img_accept"));
			acceptObj.waitForExists(60000);
			if (acceptObj.exists()) {
				logAndPrint("incoming, Please reject via testphone in 60s ...");
				UiObject onPhoneObj = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/media_title"));
				onPhoneObj.waitForExists(60000);
				if (onPhoneObj.exists()) {
					logForResult(testCaseCounter + ".PhoneRejectACall() pass");
				}else {
					logForResult(testCaseCounter + ".PhoneRejectACall() fail: onPhoneObj not exists");
				}
			}else {
				logForResult(testCaseCounter + ".PhoneRejectACall() fail: acceptObj not exists");
			}		
		} catch (UiObjectNotFoundException e) {
			logAndPrint(testCaseCounter + ".PhoneRejectACall() fail: UiObjectNotFoundException");
		}
	}

	//26.由手机接电话
	private void PhoneAnswerACall() {
		logAndPrint("PhoneAnswerACall test:");
		try {
			SwitchSource(FM_SOURCE);
			logAndPrint("Please dial testphone in 60s ...");
			UiObject acceptObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/img_accept"));
			acceptObj.waitForExists(60000);
			if (acceptObj.exists()) {
				logAndPrint("incoming, Please accept via testphone in 60s ...");
				UiObject onPhoneObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/call_state"));
				onPhoneObj.waitForExists(60000);
				if (onPhoneObj.exists()) {
					logForResult(testCaseCounter + ".PhoneAnswerACall() pass");
					sleep(2000);
					new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/btn_call")).click();//挂断
				}else {
					logForResult(testCaseCounter + ".PhoneAnswerACall() fail: onPhoneObj not exists");
				}
			}else {
				logForResult(testCaseCounter + ".PhoneDialACall() fail: acceptObj not exists");
			}
			
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PhoneDialACall() fail:UiObjectNotFoundException");
		}		
	}

	//24.25.由手机拨打电话和结束通话
	private void PhoneDialAndEndCall() {
		logAndPrint("PhoneDialAndEndCall test:");
		try {
			SwitchSource(FM_SOURCE);
			logAndPrint("Please dial 10086 via Phone in 60s ...");
			UiObject onPhoneObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/call_state"));
			onPhoneObj.waitForExists(60000);
			if (onPhoneObj.exists()) {
				logAndPrint("phone state : " + onPhoneObj.getText());
				logForResult(testCaseCounter + ".PhoneDialACall() pass");
				
				//手机端挂断
				logAndPrint("Please end call via Phone is 60s");
				UiObject fmAmObj = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/media_title"));
				fmAmObj.waitForExists(60000);
				if (fmAmObj.exists()) {
					testCaseCounter ++;
					logForResult(testCaseCounter + ".PhoneEndACall() pass");
				}else {
					testCaseCounter ++;
					logForResult(testCaseCounter + ".PhoneEndACall() fail:fmAmObj is not exists");
				}
			}else {
				logForResult(testCaseCounter + ".PhoneDialACall() fail: onPhoneObj not exists");
			}
			
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PhoneDialACall() fail:UiObjectNotFoundException");
		}
	}

	//23.蓝牙音乐播放->车机拨号->通话->挂断->恢复蓝牙音乐
	private void BTplayingHuDialOnlineEndcall() {
		logAndPrint("BTplayingHuDialOnlineEndcall test:");
		try {
			SwitchSource(BT_SOURCE);
			if (IsPlayingJudge()) {
				logAndPrint("is playing");
			}else {
					new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_pause")).click();
					logAndPrint("is not playing,play...");
			}
			getUiDevice().pressHome();
			logAndPrint("Enter Phone");
			new UiObject(new UiSelector().resourceId("com.android.launcher:id/ts_home_button_item_call_id")
					).clickAndWaitForNewWindow();
			//com.thundersoft.call:id/ts_call_menu_key_id
			logAndPrint("into dialpad ...");
			new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/ts_call_menu_key_id")).click();
			
			//com.thundersoft.call:id/edit_input_number
			logAndPrint("input 10086 ...");
			UiObject oneObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/grid_number"));
			char[] dailNumberArr = "10086".toCharArray();
			for (int i = 0; i < dailNumberArr.length; i++) {
				oneObj.getChild(new UiSelector().className("android.widget.TextView").text(dailNumberArr[i] + "")).click();
			}
			//com.thundersoft.call:id/btn_call
			logAndPrint("dialing ...");
			new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/btn_call")).click();//拨打
			sleep(1000);
			UiObject onPhoneObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/call_state"));
			onPhoneObj.waitForExists(10000);
			if (onPhoneObj.exists()) {
				logAndPrint("phone state : " + onPhoneObj.getText());
				logAndPrint("end call, Judge play...");
				new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/btn_call")).click();//挂断
				SwitchSource(BT_SOURCE);
				if (IsPlayingJudge()) {
					logForResult(testCaseCounter + ".BTplayingHuDialOnlineEndcall() pass");
				}else {
					logForResult(testCaseCounter + ".BTplayingHuDialOnlineEndcall() fail:BT music is not playing");
				}
			}else {
				logForResult(testCaseCounter + ".BTplayingHuDialOnlineEndcall() fail: onPhoneObj not exists");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".BTplayingHuDialOnlineEndcall: UiObjectNotFoundException");
		}
		
	}

	//22.蓝牙音乐播放->来电->拒接->恢复蓝牙音乐
	private void BTPlayingIncallReject() {
		logAndPrint("BTPlayingIncallReject test:");
		try {
			SwitchSource(BT_SOURCE);
			if (IsPlayingJudge()) {
				logAndPrint("is playing");
			}else {
					new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_pause")).click();
					logAndPrint("is not playing,play...");
			}
			logAndPrint("Ready,please make a call to testphone");
			UiObject rejectObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/img_refuse"));
			rejectObj.waitForExists(60000);
			if (rejectObj.exists()) {
				logAndPrint("reject call, Judge playing...");
				sleep(2000);
				rejectObj.click();//挂断
				if (IsPlayingJudge()) {
					logForResult(testCaseCounter + ".BTPlayingIncallReject() pass");
				}else {
					logForResult(testCaseCounter + ".BTPlayingIncallReject() fail: BT music is not playing");
				}
			}else {
				logForResult(testCaseCounter + ".BTPlayingIncallReject fail:reject not exists");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".BTPlayingIncallReject() fail: UiObjectNotFoundException");
		}
	}

	//21.蓝牙音乐播放->来电->接听->挂断->恢复蓝牙音乐
	private void BTPlayingIncallAcceptEndcall() {
		logAndPrint("BTPlayingIncallAcceptEndcall test:");
		try {
			SwitchSource(BT_SOURCE);
			if (IsPlayingJudge()) {
				logAndPrint("is playing");
			}else {
					new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_pause")).click();
					logAndPrint("is not playing,play...");
			}
			logAndPrint("Ready,please make a call to testphone");
			UiObject acceptCall = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/img_accept"));
			acceptCall.waitForExists(60000);
			if (acceptCall.exists()) {
				logAndPrint("accept call...");
				acceptCall.click();//接听
				sleep(2000);
				//挂断
				UiObject endCallObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/btn_call"));
				endCallObj.waitForExists(10000);
				endCallObj.click();//挂断
				if (IsPlayingJudge()) {
					logForResult(testCaseCounter + ".BTPlayingIncallAcceptEndcall() pass");
				}else {
					logForResult(testCaseCounter + ".BTPlayingIncallAcceptEndcall() fail: BT music is not playing");
				}
			}else {
				logForResult(testCaseCounter + ".BTPlayingIncallAcceptEndcall fail:acceptCall not found");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".BTPlayingIncallAcceptEndcall() fail: UiObjectNotFoundException");
		}
	}

	//20.Rejected a call by HU由主机拒接
	private void HuRejectCall() {
		logAndPrint("HuRejectCall test:");
		try {
			SwitchSource(FM_SOURCE);
			logAndPrint("Ready,please make a call to testphone");	
			UiObject rejectObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/img_refuse"));
			rejectObj.waitForExists(60000);
			if (rejectObj.exists()) {
				logAndPrint("reject call...");
				rejectObj.click();//拒接电话
				rejectObj.waitUntilGone(10000);
				if (rejectObj.exists()) {
					logForResult(testCaseCounter + ".HuRejectCall() fail: end call button still exist");
				}else {
					logForResult(testCaseCounter + ".HuRejectCall() pass");
				}
			}else {
				logForResult(testCaseCounter + ".HuRejectCall():rejectObj not exists");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuRejectCall() fail: UiObjectNotFoundException");
		}
	}

	//19.主机切换免提/私密模式
	private void HuChangePrivate() {
		logAndPrint("HuChangePrivate test:");
		try {
			SwitchSource(FM_SOURCE);
			logAndPrint("Ready,please make a call to testphone");
			UiObject acceptCall = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/img_accept"));
			acceptCall.waitForExists(60000);
			if (acceptCall.exists()) {
				sleep(1000);
				logAndPrint("accept call...");
				acceptCall.click();//接听  com.thundersoft.call:id/btn_open
				UiObject privateObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/btn_open"));
				sleep(5000);
				privateObj.click();				//改变模式
				logAndPrint("change, please check have changed...");
				sleep(5000);				
				privateObj.click();				//改变模式
				logAndPrint("change, please check have changed...");
				sleep(5000);
				logForResult(testCaseCounter + ".HuChangePrivate() : Please check The mode change see log");
			}else {
				logForResult(testCaseCounter + ".HuChangePrivate() fail: acceptCall not exists");
			}
			sleep(2000);
			new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/btn_call")).click();//挂断电话
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuChangePrivate() fail: UiObjectNotFoundException");
		}
	}

	//17.18.主机接电话，挂电话
	private void HuAnswerAndEndCall() {
		logAndPrint("HuAnswerAndEndCall test:");
		try {
			SwitchSource(FM_SOURCE);
			logAndPrint("Ready,please check auto accept is off and make a call to testphone");
			UiObject acceptCall = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/img_accept"));
			acceptCall.waitForExists(60000);
			if (acceptCall.exists()) {
				sleep(2000);
				acceptCall.click();//接听
				sleep(2000);
				UiObject endCallObj = new UiObject(new UiSelector().resourceId("com.thundersoft.call:id/btn_call"));
			    endCallObj.waitForExists(10000);
			    if (endCallObj.exists()) {
			    	logForResult(testCaseCounter + ".HuAnswerACall() pass");
			    	sleep(10000);
			    	endCallObj.click();//挂断电话
			    	endCallObj.waitUntilGone(10000);
			    	if (endCallObj.exists()) {
			    		testCaseCounter ++;
			    		logForResult(testCaseCounter + ".HuEndACall() fail: can not end the call");
					}else {
						testCaseCounter ++;
						logForResult(testCaseCounter + ".HuEndACall() pass");
					}
				}else {
					testCaseCounter ++;
					logForResult(testCaseCounter + ".HuEndACall() fail: no end call button found!Please check");
				}
			}else {
				logForResult(testCaseCounter + ".HuAnswerAndEndCall() fail: No accept button exists!Please check");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuAnswerAndEndCall() fail: UiObjectNotFoundException");
		}
	}

	//16.主机ID3功能检查
	private void ID3InfoCheck() {
		logAndPrint("ID3InfoCheck test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				SwitchSource(BT_SOURCE);
				sleep(1000);
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
							logForResult(testCaseCounter + ".ID3InfoCheck() fail: ID3 is null for 3 songs");
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
								logForResult(testCaseCounter + ".ID3InfoCheck() fail: ID3 is unknown for 3 songs,Please check later");
							}else {
								logForResult(testCaseCounter + ".ID3InfoCheck(): This song ID3 unknown,checking another song...");
								nextSongObj.click();//next song
								sleep(1000);
							}
						}
					}
				}
				sleep(2000);
			}else {
				logForResult(testCaseCounter + ".ID3InfoCheck() fail: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".ID3InfoCheck() fail: UiObjectNotFoundException");
		}
	}

	//15.播放音乐时打开wifi开关，看音乐是否正常
	private void SwitchWIFIIsPlayNormal() {
		logAndPrint("SwitchWIFIIsPlayNormal test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				SwitchSource(BT_SOURCE);
				UiObject btUiObj = new UiObject(new UiSelector()//蓝牙音乐界面
						.resourceId("com.thundersoft.mediaplayer:id/media_title"));
				UiObject homePageObj = new UiObject(new UiSelector().resourceId(MULTIMEDIA));//主界面
				if (!IsPlayingJudge()) {
					new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_pause")).click();
					logAndPrint("Checked is not playing,click to play...");
					sleep(1000);
				}else {
					logAndPrint("Checked is playing");
				}
				EnterSettingsWhichFragment("WIFI");
				UiObject wifiSwitch= new UiObject(new UiSelector().className("android.widget.Switch")); 
				boolean wifiIsOn = false;
				if (wifiSwitch.isChecked()) {
					wifiIsOn = true;
				}else {
					logAndPrint("WIFI switch is off,turn on ...");
					wifiSwitch.click();
					sleep(5000);
					if (wifiSwitch.isChecked()) {
						wifiIsOn = true;
					}else {
						logForResult(testCaseCounter + "WIFI turn on fail,Please check later");
						wifiIsOn = false;
					}
				}
				if (wifiIsOn) {
					logForResult("WIFI turn on OK!");
					logForResult("Please check bluetooth media is Normal,ok press home and fail press source in 60s...");
					SwitchSource(BT_SOURCE);
					if (btUiObj.waitUntilGone(120000)) {	//大约2分钟
						if (homePageObj.exists()) {			//home界面
							logForResult(testCaseCounter + ".SwitchWIFIIsPlayNormal() pass");
						}else {
							logForResult(testCaseCounter + ".SwitchWIFIIsPlayNormal() fail");
						}
					}else {
						logForResult(testCaseCounter + ".SwitchWIFIIsPlayNormal() fail: timeout");
					}
				}
			}else {
				logForResult(testCaseCounter + ".SwitchWIFIIsPlayNormal() fail: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".SwitchWIFIIsPlayNormal():UiObjectNotFoundException");
		}
		

		
	}

	private boolean WaitToConnectByHand() {
		try {
			UiObject connectOK = new UiObject(new UiSelector().
					resourceId("com.android.settings:id/item_bt_icon_phone"));
			UiObject connectHfpOK = new UiObject(new UiSelector().
							resourceId("com.android.settings:id/item_bt_icon_music"));
			logAndPrint("Please check bluetooth is connected in 30s");
			connectOK.waitForExists(60000);
			connectHfpOK.waitForExists(30000);
			if (connectOK.exists() && connectHfpOK.exists()) {
				logAndPrint("WaitToConnectByHand(): connected!");
				return true;
			}else {
				logAndPrint("WaitToConnectByHand(): is not connected");
				return false;
			}
		} catch (Exception e) {
			logForResult(testCaseCounter + ".WaitToConnectByHand() failed: can not find connected UiObject");
			return false;
		}
	}

	//13.检查是否支持改变播放循环模式，蓝牙必须已经连接
	private void HuChangeLoopMode() {
		logAndPrint("HuChangeLoopMode test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {//检查蓝牙是否已连接，没有连接则等待30秒手动连接
			SwitchSource(BT_SOURCE);	//com.thundersoft.mediaplayer:id/iv_ts_media_btn_play_list
			UiObject playLoopMenuObj = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_play_mode"));
				if (playLoopMenuObj.isEnabled()) {
					logForResult(testCaseCounter + ".HuChangeLoopMode(): Suport to change Play Loop Mode");
				}else{
					logForResult(testCaseCounter + ".HuChangeLoopMode(): Do not Suport change Play Loop Mode");
				}
			}else {
				logForResult(testCaseCounter + ".HuChangeLoopMode() fail: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuChangeLoopMode() fail: UiObjectNotFoundException");
		}
	}
	
	//12.检查是否支持播放列表，蓝牙必须已经先连接
	private void PlayListSuppurt() {
		logAndPrint("PlayListSuppurt test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {//检查蓝牙是否已连接，没有连接则等待30秒手动连接
				SwitchSource(BT_SOURCE);;	//com.thundersoft.mediaplayer:id/iv_ts_media_btn_play_list
				UiObject playListMenuObj = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_play_list"));
				if (playListMenuObj.isEnabled()) {
					logForResult(testCaseCounter + ".PlayListSuppurt(): Suport Play List");
				}else{
					logForResult(testCaseCounter + ".PlayListSuppurt(): Do not Suport Play List");
				}
			}else {
				logForResult(testCaseCounter + ".PlayListSuppurt() fail: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PlayListSuppurt() fail: UiObjectNotFoundException ");
		}
	}


	//播放器播放判断（隔5秒获取播放时间判断是否在播放）
	private boolean IsPlayingJudge(){
		boolean isPlaying = false;
		try {
			UiObject startTimeObj = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_start_time"));
			String nowPlayTime = startTimeObj.getText();
			sleep(3000);								//过5秒再读取播放时间
			String fiveSecPlayTime = startTimeObj.getText();
			if (nowPlayTime.equals(fiveSecPlayTime)) {	//时间相同说明处在暂停状态
				isPlaying = false;
			}else {
				isPlaying = true;
			}
		} catch (UiObjectNotFoundException e) {
			logAndPrint("isPlaying() fail: UiObjectNotFoundException");
		}
		return isPlaying;
	}
	//11.主机播放和暂停测试，蓝牙必须已经连接
	private void HuStopStartPlay() {
		logAndPrint("HuStopStartPlay test:");
		try {
			IntoSettingsFragments(BT_TAB);
	    	if (WaitToConnectByHand()) {		//检查蓝牙是否已连接，没有连接则等待30秒手动连接
	    		SwitchSource(BT_SOURCE);;			//进入蓝牙设备com.thundersoft.mediaplayer:id/iv_ts_media_btn_pause
    			UiObject startTimeObj = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_start_time"));
    			String nowPlayTime = startTimeObj.getText();
    			sleep(5000);								//过5秒再读取播放时间
    			String fiveSecPlayTime = startTimeObj.getText();
    			//暂停按钮
    			UiObject pauseObject = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_pause"));
    			if (nowPlayTime.equals(fiveSecPlayTime)) {	//时间相同说明处在暂停状态
    				pauseObject.click();					//点击使其播放
    				sleep(5000);							//播放5秒后再检查时间
    				String start5SPlayTime = new UiObject(new UiSelector().resourceId("com.thundersoft.mediaplayer:id/tv_start_time")).getText();
    				if (start5SPlayTime.equals(fiveSecPlayTime)) {
    					logAndPrint("Playing fail");
    					logForResult(testCaseCounter + ".HuStopStartPlay() Failed");
    				}else {
    					logAndPrint("Playing OK");
    					//从暂停状态播放后，要再暂停看看能否暂停
    					pauseObject.click();				//点击使其暂停
    					
    					sleep(2000);//开始暂停时间(为防止点击后过段时间才响应加点延时确保时间暂停)
    					String pausePlayTime = startTimeObj.getText();
    					sleep(5000);//暂停5秒后再检查时间
    					String pause5SPlayTime = startTimeObj.getText();
    					if (pause5SPlayTime.equals(pausePlayTime)) {
    						logAndPrint("Play then Pause OK");
    						logForResult(testCaseCounter + ".HuStopStartPlay() Pass");
    					}else {
    						logAndPrint("Play then Pause Failed");
    						logForResult(testCaseCounter + ".HuStopStartPlay() failed");
    					}
    				}
    			}else { // not the same is playing
    				pauseObject.click();//点击使其暂停
    				
    				sleep(2000);//开始暂停时间(为防止点击后过段时间才响应加点延时确保时间暂停)
    				String pausePlayTime = startTimeObj.getText();
    				sleep(5000);//暂停5秒后再检查时间
    				String pause5SPlayTime = startTimeObj.getText();
    				if (pause5SPlayTime.equals(pausePlayTime)) {
    					logAndPrint("Pause OK");
    					
    					//暂停后再播放，能播放说明case可以通过
    					pauseObject.click();//点击使其播放
    					sleep(5000);//播放5秒后再检查时间
    					String afterPlay = startTimeObj.getText();
    					if (afterPlay.equals(pause5SPlayTime)) {
    						logAndPrint("Pause then Play Failed");
    						logForResult(testCaseCounter + ".HuStopStartPlay() Failed");//播放后时间还是播放时间还是一样说明没有播放成功
    					}else {
    						logAndPrint("Pause then Play OK");
    						logForResult(testCaseCounter + ".HuStopStartPlay() Pass");  //播放成功
    					}
    				}else {
    					logForResult(testCaseCounter + ".HuStopStartPlay() fail: pause fail");
    				}
    			}
			}else {
				logForResult(testCaseCounter + ".HuStopStartPlay() fail: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuStopStartPlay() failed: UiObject not fount ");
		}
	}
	
	//10.在执行重新连接后由主机上/下一首 ，蓝牙必须已连接
	private void ReconnectBTThenHuNextAndPre() {
		logAndPrint("ReconnectBTThenHuNextAndPre test:");
		try {
			IntoSettingsFragments(BT_TAB);
    	if (WaitToConnectByHand()) {//检查蓝牙是否已连接，没有连接则等待30秒手动连接
    			//点击已连接的设备，先断开此设备的连接
    			logAndPrint("Click " + BLUETOOTH_DEVICE_NAME);
    			UiObject testDeviceObj = new UiObject(new UiSelector().
    					className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME));
    			testDeviceObj.clickAndWaitForNewWindow();
    			
    			logAndPrint("Confirm to disconnect");
    			new UiObject(new UiSelector().resourceId("com.android.settings:id/dialog_confirm"))
    			.clickAndWaitForNewWindow();//弹出确认断开对话框，点击确认
    			
    			IntoSettingsFragments(BT_TAB);//15版本断开蓝牙连接后会跳转到FM界面，需要再进入设置连接
    			//点击连接目标设备
    			logAndPrint("Click to connect " + BLUETOOTH_DEVICE_NAME);
    			testDeviceObj.click();
    			UiObject connectOK = new UiObject(new UiSelector().
    					resourceId("com.android.settings:id/item_bt_icon_phone"));
    			connectOK.waitForExists(30000);//在20秒内等待连接上
    			if (connectOK.exists()) {
    				logAndPrint("reconnect sucess!");
    				SwitchSource(BT_SOURCE);;//进入影音媒体的蓝牙设备
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
        						logForResult(testCaseCounter + ".ReconnectBTThenHuNextAndPre() pass"); 
        					}else {
        						logAndPrint("Previous Failed");
        						logForResult(testCaseCounter + ".ReconnectBTThenHuNextAndPre() fail");
        					}
        				}
        			}else {
        				logForResult(testCaseCounter + ".ReconnectBTThenHuNextAndPre() fail: music title is null");
					}
    			}else {
    				logForResult(testCaseCounter + ".ReconnectBTThenHuNextAndPre() fail: reconnect Failed!");
    			}
			}else {
				logForResult(testCaseCounter + ".ReconnectBTThenHuNextAndPre fail: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".ReconnectBTThenHuNextAndPre failed: UiObject not found");
		}

	}

	
	//9.重启手机测试是否自动连接
	private void RebootPhonePower() {
		logAndPrint("RebootPhonePower test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				logAndPrint("Please reboot Phone in 30s...");
				UiObject connectOK = new UiObject(new UiSelector()
						.resourceId("com.android.settings:id/item_bt_icon_phone"));
				connectOK.waitUntilGone(60000);
				if (connectOK.exists()) {
					logForResult(testCaseCounter + ".RebootPhonePower() fail: testphone still connected");
				}else {
					logAndPrint("Connected gone: phone rebooting...");
					connectOK.waitForExists(120000);
					if (connectOK.exists()) {
						logForResult(testCaseCounter + ".RebootPhonePower() Pass!");
					}else {
						logForResult(testCaseCounter + ".RebootPhonePower() Fail!");
					}
				}
			}else {
				logForResult(testCaseCounter + ".RebootPhonePower() fail: testphone is not connect");
			}
		} catch (UiObjectNotFoundException e) {
			logAndPrint(testCaseCounter + ".RebootPhonePower() fail: UiObjectNotFoundException");
		}
	}

	
	//8.重启手机蓝牙开关测试是否会重新连接
	private void RebootPhoneBT() {
		logAndPrint("RebootPhoneBT test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {
				logAndPrint("Please reboot Phone BT in 30s...");
				UiObject connectOK = new UiObject(new UiSelector().
						resourceId("com.android.settings:id/item_bt_icon_phone"));
				connectOK.waitUntilGone(30000);
				if (connectOK.exists()) {
					logForResult(testCaseCounter + ".RebootPhoneBT() fail: testphone still connected");
				}else {
					logAndPrint("Rebooting Phone BT...");
					connectOK.waitForExists(60000);
					if (connectOK.exists()) {
						logForResult(testCaseCounter + ".RebootPhoneBT() pass");
					}else {
						logForResult(testCaseCounter + ".RebootPhoneBT() fail");
					}
				}
			}else {
				logForResult(testCaseCounter + ".RebootPhoneBT() fail: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logAndPrint(testCaseCounter + ".RebootPhoneBT() fail: UiObjectNotFoundException");
		}
	}

	
	//7.重启车机蓝牙开关
	private void RebootHuBTAutoConnectCheck() {
		logAndPrint("RebootHuBTAutoConnectCheck test:");
		try {
			IntoSettingsFragments(BT_TAB);
			if (WaitToConnectByHand()) {//检查蓝牙是否已连接，没有连接则等待30秒手动连接
				UiObject connectOK = new UiObject(new UiSelector().
						resourceId("com.android.settings:id/item_bt_icon_phone"));
				//重启车机蓝牙开关
				UiObject blueSwitch= new UiObject(new UiSelector().resourceId("com.android.settings:id/switch_view")); 
				logAndPrint("Turn off Bluetooth");
				blueSwitch.click();
				sleep(10000);
				if (!blueSwitch.isChecked()){
					logAndPrint("Bluetooth is OFF");
					logAndPrint("Turn on Bluetooth");
					blueSwitch.click();
					sleep(20000);
					if (blueSwitch.isChecked()) {
						logAndPrint("Bluetooth turn ON OK");
						connectOK.waitForExists(30000);
						if (connectOK.exists()) {
							logForResult(testCaseCounter + ".RebootHuBTAutoConnectCheck() PASS");
						}else {
							logForResult(testCaseCounter + ".RebootHuBTAutoConnectCheck() Failed");
						}
					}else {
						logForResult(testCaseCounter + ".RebootHuBTAutoConnectCheck() failed:bluetooth switch turn on fail");
					}
				}else {
					logForResult(testCaseCounter + ".RebootHuBTAutoConnectCheck() failed:bluetooth switch turn off fail");
				}
			}else {
				logForResult(testCaseCounter + ".RebootHuBTAutoConnectCheck() Failed: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".RebootHuBTAutoConnectCheck() failed:bluetooth switch UiObject not found");
		}
		
	}


	//3.由主机断开连接
	private void HuDisconnect() {
		logAndPrint("HuDisconnect test:");
		try {
			IntoSettingsFragments(BT_TAB);
			UiObject connectHfpObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/item_bt_icon_phone"));
			UiObject connectA2dpObj = new UiObject(new UiSelector()
					.resourceId("com.android.settings:id/item_bt_icon_music"));
			logAndPrint("please connect devices first in 60s...");
			if (isConnectOk() == CONNECT_ALL) {
				logAndPrint("device connected,disconnect by Hu...");
				
				UiObject targetDevice = new UiObject(new UiSelector()
						.className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME));
				targetDevice.click();
				new UiObject(new UiSelector()
						.resourceId("com.android.settings:id/dialog_confirm")).click();
				connectHfpObj.waitUntilGone(30000);
				connectA2dpObj.waitUntilGone(30000);
				if (!connectHfpObj.exists() && !connectA2dpObj.exists()) {
					logForResult(testCaseCounter + ".HuDisconnect() pass: disconnect ok");
//					targetDevice.click();//断开后重新连接上方便后面的测试
				}else {
					logForResult(testCaseCounter + ".HuDisconnect() fail: can not disconnect");
				}
			}else {
				logForResult(testCaseCounter + ".HuDisconnect() fail: device not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".HuDisconnect() fail:UiObjectNotFoundException");
		}
	}

	private int ConnectStatus() {
		UiObject connectHfpObj = new UiObject(new UiSelector().
				resourceId("com.android.settings:id/item_bt_icon_phone"));
		UiObject connectA2dpObj = new UiObject(new UiSelector()
				.resourceId("com.android.settings:id/item_bt_icon_music"));
		if (connectHfpObj.exists() && connectA2dpObj.exists()) {
			logAndPrint("HFP & A2DP both connected");
			return CONNECT_ALL;
		}else if (connectHfpObj.exists() && !connectA2dpObj.exists()) {
			logAndPrint("only HFP connected");
			return CONNECT_HFP;
		}else if (!connectHfpObj.exists() && connectA2dpObj.exists()) {
			logAndPrint("only A2DP connected");
			return CONNECT_A2DP;
		}else {
			logAndPrint("connected none");
			return CONNECT_FAIL;
		}
	}
	//判断是否连接上返回四种状态（int）
	private int isConnectOk() {
		UiObject connectHfpObj = new UiObject(new UiSelector().
				resourceId("com.android.settings:id/item_bt_icon_phone"));
		UiObject connectA2dpObj = new UiObject(new UiSelector()
				.resourceId("com.android.settings:id/item_bt_icon_music"));
		connectHfpObj.waitForExists(30000);
		connectA2dpObj.waitForExists(30000);
		if (connectHfpObj.exists() && connectA2dpObj.exists()) {
			logAndPrint("HFP & A2DP both connected");
			return CONNECT_ALL;
		}else if (connectHfpObj.exists() && !connectA2dpObj.exists()) {
			logAndPrint("only HFP connected");
			return CONNECT_HFP;
		}else if (!connectHfpObj.exists() && connectA2dpObj.exists()) {
			logAndPrint("only A2DP connected");
			return CONNECT_A2DP;
		}else {
			return CONNECT_FAIL;
		}
	}
	
	//2.由主机连接手机,必须先匹配
	private void HuConnectToPhone() {
		logAndPrint("HuConnectToPhone test:");
        try {
        	IntoSettingsFragments(BT_TAB);
        	UiScrollable deviceList = new UiScrollable( new UiSelector().
        			resourceId("com.android.settings:id/ts_bluetooth_pair_dev_list"));
        	UiObject pairedDeviceObj = deviceList.getChildByText(new UiSelector()
        			.className("android.widget.TextView"), BLUETOOTH_DEVICE_NAME,true);
			logAndPrint("please paired devices first in 60s...");
			pairedDeviceObj.waitForExists(60000);
			if (pairedDeviceObj.exists()) {
				new UiObject(new UiSelector()
						.className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME)).click();
				logAndPrint("click " + BLUETOOTH_DEVICE_NAME);

				switch (isConnectOk()) {
					case CONNECT_ALL:
						logForResult(testCaseCounter + ".HuConnectToPhone() pass");
						break;
					case CONNECT_HFP:
						logForResult(testCaseCounter + ".HuConnectToPhone() fail: only HFP connected");
						break;
					case CONNECT_A2DP:
						logForResult(testCaseCounter + ".HuConnectToPhone() fail: only A2DP connected");
						break;
					case CONNECT_FAIL:
						logForResult(testCaseCounter + ".HuConnectToPhone() fail: Both HFP&A2DP fail");
						break;
					default:
						break;
				}
			}else {
				logForResult(testCaseCounter + "HuConnectToPhone() fail: devices is not paired");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + "HuConnectToPhone() fail: device maybe not paird");
		}
	}

	//6.由手机断开连接
	public void PhoneDisconnect() {
		logAndPrint("PhoneDisconnect test:");
		if (WaitToConnectByHand()) {//检查蓝牙是否已连接，没有连接则等待30秒手动连接
			logAndPrint("Please disconnect via phone in 10s...");
			UiObject connectOK = new UiObject(new UiSelector().
					resourceId("com.android.settings:id/item_bt_icon_phone"));
			connectOK.waitUntilGone(30000);
			if (connectOK.exists()) {
				logForResult(testCaseCounter + ".PhoneDisconnect() failed: connect" + BLUETOOTH_DEVICE_NAME + " Failed!");
			}else {
				logForResult(testCaseCounter + ".PhoneDisconnect() pass : connect" + BLUETOOTH_DEVICE_NAME + " OK!");
				//重新连接方便后面的测试
				UiObject targetDevice = new UiObject(new UiSelector().
						className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME));
				try {
					targetDevice.click();
				} catch (UiObjectNotFoundException e) {
					logAndPrint(testCaseCounter + "Can not reconnect,plz check later");
				}
			}
		}else {
			logForResult(testCaseCounter + ".PhoneDisconnect() failed: testphone is not connected");
		}
		
	}
	//5.手机连接车机
	public void PhoneConnToHu(){
		logAndPrint("PhoneConnToHu test:");
		try {
			IntoSettingsFragments(BT_TAB);
	    	if (WaitToConnectByHand()) {
    		//先断开此设备的连接,测试手机主动连接
	    		logAndPrint("Click " + BLUETOOTH_DEVICE_NAME);
    			new UiObject(new UiSelector().
    					className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME)).clickAndWaitForNewWindow();
    			
    			logAndPrint("Confirm to disconnect");
    			new UiObject(new UiSelector().resourceId("com.android.settings:id/dialog_confirm"))
    			.clickAndWaitForNewWindow();
    			logAndPrint("Please connect Hu via Phone in 10s...");
    			
    			UiObject connectOK = new UiObject(new UiSelector().
    					resourceId("com.android.settings:id/item_bt_icon_phone"));
    			connectOK.waitForExists(30000);
    			if (connectOK.exists()) {
    				logForResult(testCaseCounter + ".PhoneConnToHu() Pass!");
    			}else {
    				logForResult(testCaseCounter + ".PhoneConnToHu() Fail!");
    			}
			}else {
				logForResult(testCaseCounter + ".PhoneConnToHu() failed: testphone is not connected");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".PhoneConnToHu() failed: UiObjectNotFoundException");
		}
	}
	
	
	//4.车机已连接其他手机(HelpPhone)，先需要切换连接到待测手机TestPhone（两台手机都已配对）
	public void switchDeviceTest(){
		logAndPrint("switchDeviceTest test:");
		try {
			if (WaitToConnectByHand()) {
				IntoSettingsFragments(BT_TAB);
				UiObject connectOK = new UiObject(new UiSelector().
						resourceId("com.android.settings:id/item_bt_icon_phone"));
				logAndPrint("Please check testphone is connected in 60s...");
				UiObject testDeviceObj = new UiObject(new UiSelector()
						.className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME));
				UiObject helpDeviceObj = new UiObject(new UiSelector()
						.className("android.widget.TextView").text(BLUETOOTH_HELP_DEVICE_NAME));
				if (isConnectOk() == CONNECT_ALL) {
					logAndPrint(BLUETOOTH_DEVICE_NAME + " connected, switch connect...");
					logAndPrint("connect " + BLUETOOTH_HELP_DEVICE_NAME);
					helpDeviceObj.click();//切换连接到helpphone
					connectOK.waitUntilGone(20000);
					if (isConnectOk() == CONNECT_ALL) {
						logAndPrint("connect " + BLUETOOTH_HELP_DEVICE_NAME 
								+ " ok, switch connect to " + BLUETOOTH_DEVICE_NAME + "...");
						testDeviceObj.click();//切换连接到testphone
						connectOK.waitUntilGone(20000);
						if (isConnectOk() == CONNECT_ALL) {
							logForResult(testCaseCounter + ".switchDeviceTest:pass");
						}else {
							logForResult(testCaseCounter + ".switchDeviceTest:fail");
						}
					}else {
						logForResult(testCaseCounter + ".switchDeviceTest:fail");
					}
				}else {
					logForResult(testCaseCounter + ".switchDeviceTest fail: device is not connect");
				}
			}else {
				logForResult(testCaseCounter + ".switchDeviceTest fail:testphone not connect");
			}
		} catch (UiObjectNotFoundException e) {
			logForResult(testCaseCounter + ".switchDeviceTest fail: can not find UiObject");
		}
	}
	
	//1.主机发起配对测试，配对设备
	private void HuPairedCheck() {
		logAndPrint("HuPairedCheck test:");
		getUiDevice().pressHome();
		try {
			new UiObject(new UiSelector().className("android.widget.TextView")
			        .text("设置")).clickAndWaitForNewWindow();
			logAndPrint("Enter Settings");
			new UiObject(new UiSelector().className("android.widget.RadioButton")
					.text("蓝牙")).click();
		} catch (UiObjectNotFoundException e1) {
			logForResult(testCaseCounter + ".HuPairedCheck() failed: UiObjectNotFoundException");
		}
        logAndPrint("Click Radio_Button bluetooth");
        sleep(5000);
        
        //查找可用设备中是否存在目标设备,并点击设备进行匹配
        int retryCounter = 0;
        UiScrollable deviceList = new UiScrollable( new UiSelector().
        		resourceId("com.android.settings:id/ts_bluetooth_dev_list"));
        while (retryCounter<10) {//尝试5次
        	retryCounter ++;
        	try {
        		UiObject targetDevice = deviceList.getChildByText(new UiSelector().className("android.widget.TextView"), BLUETOOTH_DEVICE_NAME,true);
        		if (targetDevice.exists()) {
        			new UiObject(new UiSelector()//点击配对
        					.className("android.widget.TextView").text(BLUETOOTH_DEVICE_NAME)).click();
        			sleep(10000);			//等待对方设备确认连接，这个延时必须要
        	        //查找已匹配列表中是否存在目标设备
        	        try {
        	        	UiScrollable devicePairedList = new UiScrollable( new UiSelector().
        	        			resourceId("com.android.settings:id/ts_bluetooth_pair_dev_list"));//获取listview对象
        				UiObject targetPairedDevice = devicePairedList.getChildByText(new UiSelector()//根据文本获取listview中对应item
        						.className("android.widget.TextView"), BLUETOOTH_DEVICE_NAME,true);
        				targetPairedDevice.waitForExists(30000);
        				if (targetPairedDevice.exists()) {//如果存在说明已配对成功
        					logForResult(testCaseCounter + ".HuPairedCheck() pass");
        					retryCounter = 11;			  //大于5直接退出循环
        				}else {
        					logForResult(testCaseCounter + ".HuPairedCheck() failed");
        				}
        			} catch (UiObjectNotFoundException e) {
        				logForResult(testCaseCounter + ".HuPairedCheck() failed : UiObject not found");
        			}
				}else {
					//刷新列表
					if (retryCounter == 10) {
						logForResult(testCaseCounter + ".HuPairedCheck() failed : can't find device after 10 times refresh");
					}else {
						new UiObject(new UiSelector().resourceId("com.android.settings:id/ts_bluetooth_refresh")).click();
						sleep(3000);
					}
				}
        	} catch (UiObjectNotFoundException e) {
        		if (retryCounter == 10) {
        			logForResult(testCaseCounter + ".HuPairedCheck() failed : targetDevice UiObject not found");
				}else {
					try {
						new UiObject(new UiSelector().resourceId("com.android.settings:id/ts_bluetooth_refresh")).click();
						sleep(3000);
					} catch (UiObjectNotFoundException e1) {
						logAndPrint("UiObjectNotFoundException: refresh no found");
					}
				}
        	}
		}
	}
	
	//进入设置的某个界面（wifi，蓝牙，音效。。。）
	public void EnterSettingsWhichFragment(String fragmentWhich) {
		sleep(1000);
		getUiDevice().pressHome();
    	try {
			new UiObject(new UiSelector().className("android.widget.TextView")
					.text("设置")).clickAndWaitForNewWindow();
			logAndPrint("Enter Settings");
			new UiObject(new UiSelector().className("android.widget.RadioButton")
					.text(fragmentWhich)).click();
			logAndPrint("Click " + fragmentWhich);
		} catch (UiObjectNotFoundException e) {
			logForResult("EnterSettingsWhichFragment() failed: UiObjectNotFoundException");
		}
	}
	
	public void logForResult(String str) {
		System.out.println("=========>>>>" + str + "<<");
		Log.d("TESTRESULT","=========>>>>" + str + "<<");
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
	
	//切换音源
	public void SwitchSource(String source) throws UiObjectNotFoundException {
		getUiDevice().pressHome();
		new UiObject(new UiSelector().resourceId(MULTIMEDIA)).click();
		new UiObject(new UiSelector()
				.resourceId("com.thundersoft.mediaplayer:id/media_source")).click();
		new UiObject(new UiSelector().resourceId(source)).click();
	}
	//进入设置的某个界面（wifi，蓝牙，音效。。。）
	public void IntoSettingsFragments(String fragmentWhich) throws UiObjectNotFoundException {
		getUiDevice().pressHome();
		new UiObject(new UiSelector().resourceId(SETTINGS)).clickAndWaitForNewWindow();
		new UiObject(new UiSelector().resourceId(fragmentWhich)).click();
	}
	
}