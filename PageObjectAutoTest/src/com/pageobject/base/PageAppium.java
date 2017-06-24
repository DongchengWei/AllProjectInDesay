package com.pageobject.base;


import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;


/**
 * 页面UI获取定位父类，供给Page层使用
 * Created by LITP on 2016/9/23.
 */
public class PageAppium extends UiAutomatorTestCase{

//    UiDevice device;
//
//    public PageAppium(UiDevice androidDriver) {
//        this.device = androidDriver;
////        waitAuto(WAIT_TIME);
//    }
    
    
    /**
     * 根据id判断控件是否存在
     * 
     * @param id id字符串
     * @return 存在返回true
     * */
    public boolean isIdElementExist(String id) {
        return isIdElementExist(id, 0);
    }

    
    /**
     * 根据id判断控件是否存在
     * 
     * @param id id字符串
     * @param timeOut 判断超时时间，单位毫秒
     * @return 存在返回true
     * */
    public boolean isIdElementExist(String id,int timeOut) {
    	UiObject uiObject = new UiObject(new UiSelector().resourceId(id));
        return isElementExist(uiObject,timeOut);
    }

    /**
     * 根据UiObject判断对象是否存在
     *
     * @param uiObject   Ui对象
     * @param timeOut    等待对象出现的等待超时时间
     * @return 控件存在返回true
     */
    public boolean isElementExist(UiObject uiObject, long timeOut) {
    	boolean isExist = false;
		if (uiObject.waitForExists(timeOut)) {
			isExist = true;
		}
		return isExist;
	}
    
    /**
     * 根据id和控件上显示的文字 获取当前界面的一个控件
     *
     * @param id 要查找的id
     * @param desc 字符串内容
     * @return 返回对应的控件
     */
    public UiObject findById(String id,String desc) {
        return new UiObject(new UiSelector().resourceId(id).text(desc));
    }
    
    /**
     * 返回 根据id和控件上包含某个字符串找到的控件
     *
     * @param id 要查找的id
     * @param desc 包含的字符串内容
     * @return 返回对应的控件
     */
    public UiObject findByIdContainsString(String id,String desc) {
        return new UiObject(new UiSelector().resourceId(id).textContains(desc));
    }

    /**
     * 根据id获取控件
     * */
    public UiObject findById(String id) {
        return new UiObject(new UiSelector().resourceId(id));
    }
}
