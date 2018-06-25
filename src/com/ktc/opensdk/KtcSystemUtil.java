package com.ktc.opensdk;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mstar.android.storage.MStorageManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.factory.FactoryManager;
import com.mstar.android.tvapi.factory.vo.EnumAcOnPowerOnMode;
import android.app.Instrumentation;
import android.content.Context;
import android.media.AudioManager;
import android.os.SystemProperties;
import android.provider.Settings;
import android.view.KeyEvent;

/**
*TODO KTC系统信息获取接口
*@author Arvin
*@Time 2018-1-10 上午11:20:33
*/
public class KtcSystemUtil {
	
	private static final String TAG = "KtcSystemUtil";
	private static KtcSystemUtil mKtcSystemUtil;
	private static KtcLogerUtil  mKtcLogerUtil;
	private static Context mContext = null;
	
	public static KtcSystemUtil getInstance(Context context) {
		mContext = context;
    	if (mKtcSystemUtil == null) {
    		mKtcSystemUtil = new KtcSystemUtil();
    	}
    	return mKtcSystemUtil;
    }
	
	/**
	 * TODO 获取KtcLogerUtil实例对象
	 * @param null
	 * @return KtcLogerUtil
	 */
	private static KtcLogerUtil getKtcLogerUtil() {
    	if (mKtcLogerUtil == null) {
    		mKtcLogerUtil = new KtcLogerUtil();
    	}
    	return mKtcLogerUtil;
    }
	
	/**
	 * TODO 获取设备型号
	 * @param null
	 * @return String
	 */
	public String getProductsModel() {
		try {
			String productMode = getProp("/system/build.prop" , "ro.product.model");
			return productMode;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * TODO 获取设备软件版本号
	 * @param null
	 * @return String
	 */
	public String getProductsVersion() {
		String softVersionx = "";
		softVersionx = getProp("/system/build.prop", "ro.product.version");
		return softVersionx;
	}
	
	/**
	 * TODO 获取设备Memory信息
	 * @param null
	 * @return String
	 */
	public String getSystemMemory() {
		String mSystemMemory = "";
		mSystemMemory = getProp("/system/build.prop", "ktc.board.memory");
		return mSystemMemory;
	}
	
	/**
	 * TODO 获取设备支持的SDK版本
	 * @param null
	 * @return String
	 */
	public String getSdkVersion() {
		String mSdkVersion = "";
		mSdkVersion = getProp("/system/build.prop", "ro.build.version.sdk");
		return mSdkVersion;
	}
	
	/**
	 * TODO 获取设备Android版本
	 * @param null
	 * @return String
	 */
	public String getAndroidVersion() {
		String mAndroidVersion = "";
		mAndroidVersion = getProp("/system/build.prop", "ro.build.version.release");
		return mAndroidVersion;
	}
	
	/**
	 * TODO 重启设备
	 * @param null
	 * @return void
	 */
	public void Reboot() {
		try {
			TvCommonManager.getInstance().rebootSystem("reboot");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * TODO 关机
	 * @param null
	 * @return void
	 */
	public void ShutDown() {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_POWER);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	/**
	 * TODO 调节系统音量
	 * @param int volume
	 * @return boolean
	 */
	public boolean setStreamVolume(int volume) {
		getKtcLogerUtil().I(TAG, "setStreamVolume:  "+volume);
		if ((volume >= 0) && (volume <= 100)) {
			try {
				AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume,
						AudioManager.FLAG_PLAY_SOUND);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		} else {
			return false;
		}
	}
	
	/**
	 * TODO 设置开机模式
	 * @param int value
	 * @return boolean
	 */
	public boolean setPowerMode(int value) {
		getKtcLogerUtil().I(TAG, "setPowerMode:  "+value);
		
		boolean ret = false;
		EnumAcOnPowerOnMode poweronmode = null;
		poweronmode = EnumAcOnPowerOnMode.values()[value];
		try {
			FactoryManager fm = TvManager.getInstance().getFactoryManager();
			ret = fm.setEnvironmentPowerMode(poweronmode);
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * TODO 获取当前开机启动模式
	 * @param null
	 * @return int
	 */
	public int getPowerMode() {
		int ret = 0;
		try {
			FactoryManager fm = TvManager.getInstance().getFactoryManager();
			ret = fm.getEnvironmentPowerMode().ordinal();
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getKtcLogerUtil().I(TAG, "getPowerMode:  "+ret);
		return ret;
	}
	
	/**
	 * TODO 获取已挂载的第一个U盘路径(eg:/mnt/usb/sda1/)
	 * @param null
	 * @return String
	 */
   public String getFirstUsbPath(){
   	MStorageManager storageManager = MStorageManager.getInstance(mContext);
		String[] volumes = storageManager.getVolumePaths();
		boolean isUsbAsSd = SystemProperties.getBoolean("mstar.usb.as.sdcard", false);
		for(String mVolume : volumes){
			//filter sdcard
			if(!isUsbAsSd && mVolume.equals("/mnt/sdcard")){
				continue ;
			}
			//for usb plug
			File mFile = new File(mVolume);
			if(mFile != null && mFile.list().length > 0){
				getKtcLogerUtil().I(TAG, "getFirstUsbPath:  "+(mVolume.endsWith("/") ? mVolume : mVolume+"/"));
				return mVolume.endsWith("/") ? mVolume : mVolume+"/";
			}
		}
		return null;
   }
	
	/**
	 * TODO 开/关adb调试
	 * @param boolean toEnable
	 * @return void
	 */
	public void setAdbEnable(boolean toEnable) {
		getKtcLogerUtil().I(TAG, "setAdbEnable:  "+toEnable);
		Settings.Global.putInt(mContext.getContentResolver(), Settings.Global.ADB_ENABLED, toEnable ? 1 : 0);
        SystemProperties.set("persist.service.adb.enable", String.valueOf(toEnable ? 1 : 0));
        String cmd = "setprop service.adb.tcp.port 5555";
        String stop = "stop adbd";
        String start = "start adbd";
        try {
            Runtime.getRuntime().exec(cmd);
            if(toEnable){
                Runtime.getRuntime().exec(start);
            }else {
                Runtime.getRuntime().exec(stop);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
	
	/**
	 * TODO 判断是否已打开adb调试
	 * @param null
	 * @return boolean
	 */
	public boolean isAdbEnable() {
		int mAdbEnabled = Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.ADB_ENABLED, 0);
        int mAdbEnable = SystemProperties.getInt("persist.service.adb.enable", 0);
        
        getKtcLogerUtil().I(TAG, "isAdbEnable:  "+(mAdbEnabled == 1 && mAdbEnable == 1 ? true : false));
        return mAdbEnabled == 1 && mAdbEnable == 1 ? true : false ;
    }
	
	private String getProp(String file, String key) {
		String value = "";
		Properties props = new Properties();
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			props.load(in);
			value = props.getProperty(key);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (value != null) {
			String[] array = value.split(";");
			if (array[0].length() > 0) {
				value = array[0];
			}
			value = value.replace("\"", "");
			value = value.trim();
			return value;
		} else {
			return "";
		}
	}
	
	/**
	 * TODO 判断是否支持DTMB
	 * @param null
	 * @return boolean
	 */
	public boolean hasDTMB() {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream("/system/build.prop"));
            props.load(in);
            String value = props.getProperty("ktc.dtmb");
            boolean tag = false;
            if(value != null && value.equals("true"))
                    tag = true;
            return tag;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
	/**
	 * TODO 判断是否为T4C1
	 * @param null
	 * @return boolean
	 */
	public boolean isT4C1(){
		try {
		Properties props = new Properties();
		InputStream in = new BufferedInputStream(new FileInputStream("/system/build.prop"));
		props.load(in);
		String value = props.getProperty("ktc.board.t4c1");
		return (value != null && value.equals("true"));
		}
		catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * TODO 判断是否为T5C1
	 * @param null
	 * @return boolean
	 */
	public boolean isT5c1(){
		try {
		Properties props = new Properties();
		InputStream in = new BufferedInputStream(new FileInputStream("/system/build.prop"));
		props.load(in);
		String value = props.getProperty("ktc.board.t5c1");
		return (value != null && value.equals("true"));
		}
		catch (Exception e) {
		}
		return false;
	}

}
