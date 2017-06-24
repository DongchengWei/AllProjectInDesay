package com.compa;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class BtTabPage extends UiAutomatorTestCase {
	
	//蓝牙开关
	UiObject btSwitchObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/switch_view"));
	//蓝牙标题
	UiObject btTabObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_tab_bluetooth_id"));
	
	//已匹配列表
	UiObject pairedDeviceListObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_bluetooth_pair_dev_list"));
	
	//已连接HFP成功图标
	UiObject hfpConnectedObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/item_bt_icon_phone"));
	
	//已连接A2DP成功图标
	UiObject a2dpConnectedObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/item_bt_icon_music"));
	
	//已匹配的第一个列表设备
	UiObject firstDeviceObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_bluetooth_device_name"));
	
	//等待已匹配列表出现
	public boolean waitPairedListExists(int millSecond) {
		if (pairedDeviceListObj.waitForExists(millSecond)) {
			return true;
		} else {
			return false;
		}
	}
	//等待BT界面消失
	public boolean waitUntilGone(int millSecond) {
		boolean isGone = false;
		if (btTabObj.waitUntilGone(millSecond)) {
			isGone = true;
		}else {
			isGone = false;
		}
		return isGone;
	}
	
	//判断BT开关是否处于打开状态,打开状态返回true
	public boolean isBTOn() throws UiObjectNotFoundException {
		if (btSwitchObj.isChecked()) {
			return true;
		}else {
			return false;
		}
	}
	
	//打开BT开关
	public void turnOnBT() throws UiObjectNotFoundException {
		btSwitchObj.click();
	}
	
	//关闭BT开关
	public void turnOffBT() throws UiObjectNotFoundException {
		btSwitchObj.click();
	}
	
	//判断是否已连接上蓝牙全部协议，已连接返回true
	public boolean isBTConnectedAll() {
		
		boolean isConnected = false;
		
		hfpConnectedObj.waitForExists(30000);
		a2dpConnectedObj.waitForExists(30000);
		//连接上HFP和A2DP则返回true
		if (hfpConnectedObj.exists() && a2dpConnectedObj.exists()) {
			isConnected = true;
		}else {
			isConnected = false;
		}
		return isConnected;
	}
	
	//连接所有协议,前提是列表第一个设备，且未连接任何协议的情况下
	public boolean btConnectFirstDeviceAllByName(String deviceName) throws UiObjectNotFoundException {
		
		boolean connectOk = false;
		UiObject firstDeviceByNameObj = new UiObject(new UiSelector()
				.resourceId("com.android.settings:id/ts_bluetooth_device_name").text(deviceName));
		if (firstDeviceByNameObj.exists()) {
			firstDeviceByNameObj.click();
			if (isBTConnectedAll()) {
				connectOk = true;
			}
		}
		return connectOk;
	}
	//断开所有协议,前提是连接的是列表中第一个设备名称
	public boolean btDisconnectFirstDeviceAllByName(String deviceName) throws UiObjectNotFoundException {
		
		boolean disconnectOk = false;
		UiObject firstDeviceByNameObj = new UiObject(new UiSelector()
				.resourceId("com.android.settings:id/ts_bluetooth_device_name").text(deviceName));
		UiObject confirmDialogObj = new UiObject(new UiSelector()
				.resourceId("com.android.settings:id/dialog_summary1"));
		UiObject confirmButtonObj = new UiObject(new UiSelector()
				.resourceId("com.android.settings:id/dialog_confirm"));
		if (isBTConnectedAll()) {
			if (firstDeviceByNameObj.waitForExists(10000)) {
				firstDeviceByNameObj.click();
				if (confirmDialogObj.waitForExists(30000)) {
					confirmButtonObj.click();
					if (hfpConnectedObj.waitUntilGone(5000) && a2dpConnectedObj.waitUntilGone(5000)) {
						disconnectOk = true;
					}
				}
			} 
		}
		return disconnectOk;
	}
}
