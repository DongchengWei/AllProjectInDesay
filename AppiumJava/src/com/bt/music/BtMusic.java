package com.bt.music;

import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.base.Assertion;
import com.base.Builder;
import com.base.InitAppium;
import com.operation.HomePageOperation;
import com.operation.MediaPageOperate;
import com.operation.SettingsPageOperation;

/**
 * 蓝牙音乐
 * */
public class BtMusic extends InitAppium implements InitAppium.OnInitOperate{

	private static Logger logger = Logger.getLogger(Log.class.getName());
	
    private HomePageOperation homePageOperation;
    private MediaPageOperate mediaPageOperate;
    private SettingsPageOperation settingsPageOperation;
    
    //是否进行全部测试
    private final boolean testAll = false;
    
    
    @Parameters({"udid","port"}) //注解的方式传递参数
    BtMusic(String udid, String port){
    	super(new Builder().setUdid(udid).setPort(port));
    	super.setOnInitOperate(this);
    }

    @Override
	public void initOperate() {
		Assert.assertNotNull(driver);
		
	    homePageOperation = new HomePageOperation(driver);
	    mediaPageOperate = new MediaPageOperate(driver);
	    settingsPageOperation = new SettingsPageOperation(driver);
	    homePageOperation.intoHomePage();
	}
	
	/**
	 * 初始化测试
	 * */
	@BeforeMethod
	public void initTest(){
		initOperate();
	}
    /**
     * 测试进入媒体页面
     */
    @Test(priority = 0,enabled = testAll)
    public void intoBtMusicPlay(){
    	logger.info("启动蓝牙音乐");
    	
        boolean isPlaying = false;
        print("进入媒体");
        if (homePageOperation.intoMediaPage()) {
        	print("进入FM");
        	if (mediaPageOperate.intoFm()) {
        		print("下一个音源切换到蓝牙音乐");
        		if (mediaPageOperate.isNextMediaBt()) {
        			print("判断是否正在播放");
        			if (mediaPageOperate.isPlaying()) {
        				print("返回上一个音源（FM）");
						if (mediaPageOperate.isPreviousMediaFm()) {
							print("通过source按钮切换到蓝牙音乐");
							if (mediaPageOperate.intoBtMusic()) {
								print("判断是否正在播放");
								isPlaying = mediaPageOperate.isPlaying();
							}
						}
					}
				}
			}
		}
    	Reporter.log("TC_ST16081568963,TC_ST16081569070:启动蓝牙音乐的两种方式。");
        Assertion.verifyEquals(isPlaying,true,"启动蓝牙音乐是否成功");
    }
    
    /**
     * 测试检查ID3页面
     */
    @Test(priority = 1,enabled = testAll)
    public void checkID3(){
    	logger.info("检查ID3");
    	boolean isID3Ok = false;
    	
    	print("进入媒体");
    	if (homePageOperation.intoMediaPage()) {
    		print("进入蓝牙音乐");
			if (mediaPageOperate.intoBtMusic()) {
				isID3Ok = mediaPageOperate.isID3AndMusicEndTimeNormal();
			}
		}        
    	Reporter.log("TC_ST16081568964：检查ID3");
    	Assertion.verifyEquals(isID3Ok,true,"ID3是否正常");
    }
    
    /**
     * 测试暂停播放
     */
    @Test(priority = 2,enabled = testAll)
    public void btMusicPausePlay(){
    	logger.info("蓝牙音乐暂停/播放");
        boolean isPausePlayOk = false;   
        
        print("intoMediaPage");
    	if (homePageOperation.intoMediaPage()) {
    		print("intoBtMusic");
			if (mediaPageOperate.intoBtMusic()) {
				print("pause and play...");
				isPausePlayOk = mediaPageOperate.pausePlayBtMusic();  
			}
		}        
    	Reporter.log("TC_ST16081568965,TC_ST16081568967：蓝牙音乐暂停/播放");
    	Assertion.verifyEquals(isPausePlayOk,true,"暂停播放是否正常");
    }
    
    /**
     * 测试上下一曲
     */
    @Test(priority = 3,enabled = testAll)
    public void btMusicNextPrevious(){
    	logger.info("蓝牙音乐下一曲上一曲");
    	boolean isOk = false;   
    	
    	print("进入媒体");
    	if (homePageOperation.intoMediaPage()) {
    		print("进入蓝牙音乐");
    		if (mediaPageOperate.intoBtMusic()) {
    			print("下一曲上一曲测试");
    			isOk = mediaPageOperate.nextPreviousMusic();  
    		}
    	}        
    	Reporter.log("TC_ST16081568969,TC_ST16081568970：蓝牙音乐下一曲上一曲");
    	Assertion.verifyEquals(isOk,true,"上下一曲是否正常");
    }
    
    /**
     * 蓝牙音乐播放时锁屏
     */
    @Test(priority = 4,enabled = testAll)
    public void lockScreenWhenBtPlay(){
    	logger.info("蓝牙音乐：音乐播放时锁屏");
    	boolean isOk = false;   
    	
    	print("进入媒体");
    	if (homePageOperation.intoMediaPage()) {
    		print("进入蓝牙音乐");
    		if (mediaPageOperate.intoBtMusic()) {
    			print("判断是否正在播放");
    			if (mediaPageOperate.doPlay()) {
    				print("锁屏");
    				if (mediaPageOperate.lockScreen()) {
    					print("解锁");
    					mediaPageOperate.unlockScreen();
    					print("判断是否正在播放");
    					isOk = mediaPageOperate.isPlaying();
					}
				}
    		}
    	}        
    	Reporter.log("TC_ST16081569076：蓝牙音乐播放时锁屏");
    	Assertion.verifyEquals(isOk,true,"蓝牙音乐播放时锁屏是否正常");
    }
    
    /**
     * 蓝牙音乐暂停时锁屏
     */
    @Test(priority = 5,enabled = testAll)
    public void lockScreenWhenBtPause(){
    	logger.info("蓝牙音乐：蓝牙音乐暂停时锁屏");
    	boolean isOk = false;   
    	
    	print("进入媒体");
    	if (homePageOperation.intoMediaPage()) {
    		print("进入蓝牙音乐");
    		if (mediaPageOperate.intoBtMusic()) {
    			print("暂停音乐");
    			if (mediaPageOperate.doPause()) {
    				print("锁屏");
    				if (mediaPageOperate.lockScreen()) {
    					print("解锁");
    					mediaPageOperate.unlockScreen();
    					print("判断是否还在暂停");
    					isOk = !mediaPageOperate.isPlaying();
    				}
    			}
    		}
    	}        
    	Reporter.log("TC_ST16081569077：蓝牙音乐暂停时锁屏");
    	Assertion.verifyEquals(isOk,true,"蓝牙音乐暂停时锁屏是否正常");
    }
    
    /**
     * 蓝牙音乐播放时修改系统语言
     */
    @Test(priority = 6,enabled = testAll)
    public void changeLanguageWhenBtPlay(){
    	logger.info("蓝牙音乐：蓝牙音乐播放时改变系统语言");
    	boolean isOk = false;   
    	
    	print("进入蓝牙音乐。。。");
    	homePageOperation.intoMediaPage();
    	mediaPageOperate.intoBtMusic();
    	if (mediaPageOperate.isPlaying()) {
    		print("蓝牙音乐播放中，切换系统语言为英文。。。");
    		homePageOperation.intoHomePage();
    		homePageOperation.intoSettingPage();
    		settingsPageOperation.intoDisplayTab();
    		settingsPageOperation.intoLanguageSetting();
    		settingsPageOperation.selectEnglish();
    		if (settingsPageOperation.isEnglish()) {
    			isOk = true;//切换成英文成功
    			
    			//切换为默认语言：中文
    			homePageOperation.intoHomePage();
    			homePageOperation.intoMediaPage();
    			if (mediaPageOperate.isPlaying()) {
    				print("蓝牙正在播放，恢复中文语言。。。");
    				homePageOperation.intoHomePage();
    				homePageOperation.intoSettingPage();
    				settingsPageOperation.intoDisplayTab();
    				settingsPageOperation.intoLanguageSetting();
    				settingsPageOperation.selectChinese();
    			}
			}
		}
    			
					
    	Reporter.log("TC_ST17031090857：蓝牙音乐播放时改变系统语言");
    	Assertion.verifyEquals(isOk,true,"蓝牙音乐播放时改变系统语言");
    }

    /**
     * 蓝牙音乐播放时切换wifi开关
     * 判断开关有问题，待解决
     */
    @Test(priority = 7,enabled = true)
    public void switchWifiWhenBtPlay(){
    	logger.info("蓝牙音乐播放时切换wifi开关");
    	boolean isOk = false;   
    	
		homePageOperation.intoHomePage();
		homePageOperation.intoSettingPage();
		if (settingsPageOperation.turnOffWifiSwitch()) {
			print("关闭wifi开关成功，准备打开wifi...");
			if (settingsPageOperation.turnOnWifiSwitch()) {
				print("打开wifi开关成功");
				isOk = true;
			}
		}
    	
	   	Reporter.log("TC_ST17031090858：蓝牙音乐播放时切换wifi开关不会crash");
	   	Assertion.verifyEquals(isOk,true,"蓝牙音乐播放时切换wifi开关失败");
    }
}
