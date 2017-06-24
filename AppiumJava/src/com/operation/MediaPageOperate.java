package com.operation;

import org.apache.http.util.TextUtils;

import com.base.OperateAppium;
import com.pages.MediaPage;

import io.appium.java_client.android.AndroidDriver;

public class MediaPageOperate extends OperateAppium {
	
    private MediaPage mediaPage;

    AndroidDriver driver;
    
	public MediaPageOperate(AndroidDriver driver) {
		super(driver);
		mediaPage = new MediaPage(driver);
		this.driver = driver;
	}
	
	//进入蓝牙音乐界面
	public boolean intoBtMusic() {
		boolean isOk = false;
		if (clickView(mediaPage.getSourceButton())) {
			if (clickView(mediaPage.getBtSourceButton())) {
				if (isBtMusic()) {
					isOk = true;
				}
			}
		}
		return isOk;
	}
	
	//进入FM界面
	public boolean intoFm() {
		boolean isOk = false;
		if (clickView(mediaPage.getSourceButton())) {
			if (clickView(mediaPage.getFMSourceButton())) {
				isOk = true;
			}
		}
		return isOk;
	}
	
	//按下一个媒体按钮后是否是切换到蓝牙音乐
	public boolean isNextMediaBt() {
		boolean isOk = false;
		if (clickView(mediaPage.getNextMediaButton())) {
			if (isBtMusic()) {
				isOk = true;
			}
		}
		
		return isOk;
	}
	
	//按下一个媒体按钮后是否是切换到蓝牙音乐
	public boolean isPreviousMediaFm() {
		boolean isOk = false;
		if (clickView(mediaPage.getPreviousMediaButton())) {
			if (isFmSource()) {
				isOk = true;
			}
		}
		return isOk;
	}
	
	//是否在bt source
	public boolean isBtMusic() {
		boolean isOk = false;
		
		String titleStr = getText(mediaPage.getMediaSourceTitleText());
		if (titleStr.equals("Bluetooth") || titleStr.equals("蓝牙设备")) {
			isOk = true;
		}
		
		return isOk;
	}
	
	//是否在FM source
	public boolean isFmSource() {
		boolean isOk = false;
		
		String titleStr = getText(mediaPage.getMediaSourceTitleText());
		if (titleStr.equals("FM/AM")) {
			isOk = true;
		}
		
		return isOk;
	}
	
	//判断是否在播放状态
	public boolean isPlaying() {
		boolean isOk = false;
		
		String startTimeStr = getText(mediaPage.getStartTimeText());
		sleep(2000);
		String nowTimeStr = getText(mediaPage.getStartTimeText());
		if (startTimeStr.equals(nowTimeStr)) {
			isOk = false;
		} else {
			isOk = true;
		}
		
		return isOk;
	}
	
	/**
	 * 播放音乐，如果已播放则返回true，如果没有播放则点击播放
	 * */
	public boolean doPlay() {
		boolean isOk = false;
		if (isPlaying()) {
			isOk = true;
		} else {
			clickView(mediaPage.getPausePlayButton());
			sleep(1000);
			if (isPlaying()) {
				isOk = true;
			} else {
				print("播放失败");
			}
		}
		
		return isOk;
	}
	
	/**
	 * 暂停音乐，如果已暂停则返回true，如果没有暂停则点击暂停
	 * */
	public boolean doPause() {
		boolean isOk = false;
		if (isPlaying()) {
			clickView(mediaPage.getPausePlayButton());//暂停
			sleep(1000);
			if (isPlaying()) {
				print("暂停失败");
			} else {
				isOk = true;
			}
		} else {
			isOk = true;
		}
		
		return isOk;
	}

    /**
     * 检查ID3是否正常
     */
	public boolean isID3AndMusicEndTimeNormal() {
		boolean isOk = false;
		
		String endTimeStr = getText(mediaPage.getEndTimeText());
		String musicTitleStr = getText(mediaPage.getMusicTitleText());
		String artistStr = getText(mediaPage.getMusicArtistText());
		String albumStr = getText(mediaPage.getMusicAlbumText());
		if (!TextUtils.isEmpty(musicTitleStr) && (!endTimeStr.equals("00:00"))) {
			print("歌曲名：" + musicTitleStr);
			print("歌手名：" + artistStr);
			print("专辑名：" + albumStr);
			print("歌曲总时长：" + endTimeStr);
			isOk = true;
		}
		return isOk;
	}
	
	//暂停、播放
	public boolean pausePlayBtMusic() {
		boolean isOk = false;
		print("点击下一曲");
		clickView(mediaPage.getNextMusicButton());//下一曲
		sleep(2000);
		print("暂停");
		if (clickView(mediaPage.getPausePlayButton())) {
			sleep(2000);
			print("判断暂停是否成功");
			if (! isPlaying()) {		//暂停成功
				print("播放");
				if (clickView(mediaPage.getPausePlayButton())) {
					print("判断播放是否成功");
					if (isPlaying()) {	//播放成功
						isOk = true;
					}
				}
			}
		}
		return isOk;
	}
	
	//下一曲，上一曲
	public boolean nextPreviousMusic() {
		boolean isOk = false;
		
		String musicTitleStr = getText(mediaPage.getMusicTitleText());
		if (clickView(mediaPage.getNextMusicButton())) {
			print("点击下一曲成功");
			sleep(2000);//等待2s
			String nowStr = getText(mediaPage.getMusicTitleText());
			if (!musicTitleStr.equals(nowStr)) {
				print("下一曲成功");
				musicTitleStr = nowStr;
				print("点击上一曲");
				clickView(mediaPage.getPreviousMusicButton());
				clickView(mediaPage.getPreviousMusicButton());
				sleep(2000);
				nowStr = getText(mediaPage.getMusicTitleText());
				if (!musicTitleStr.equals(nowStr)) {
					print("上一曲成功");
					isOk = true;
				}
			}
		}
		return isOk;
	}
	
	//点击快捷方式
	public boolean clickShortcut() {
		//UiDevice.getInstance().click(75, 620);
		press(75, 620);
		if (mediaPage.waitLockScreenButtonExists()) {
			return true;
		} else {
			return false;
		}
	}
	
	//锁屏
	public boolean lockScreen() {
		boolean isOk = false;
		
		if (clickShortcut()) {
			if (clickView(mediaPage.getLockScreenButton())) {
				if (mediaPage.waitLockScreenTimeExists()) {
					isOk = true;
				} else {
					print("锁屏界面未出现");
				}
			} else {
				print("点击锁屏失败");
			}
		} else {
			print("点击快捷方式失败");
		}
		return isOk;
	}
	
	public boolean unlockScreen() {
//		press();//点击屏幕中间
		boolean isOk = false;
		if (clickView(mediaPage.getLockScreenTimeButton())) {
			isOk = true;
		}
		return isOk;
	}
	
}
