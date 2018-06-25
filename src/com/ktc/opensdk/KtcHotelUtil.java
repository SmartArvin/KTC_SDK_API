package com.ktc.opensdk;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.vo.ProgramInfo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;

/**
*TODO KTC酒店功能常用接口
*@author Arvin
*@Time 2018-1-9 上午09:30:10
*/
public class KtcHotelUtil {
	
	private final String TAG = "KtcHotelUtil" ;
	private static KtcHotelUtil mKtcHotelSdk;
	private static KtcLogerUtil mKtcLogerUtil ;
	private static Context mContext = null;
	
	private static KtcDataUtil mKtcDataBaseUtil ;
	
	public static KtcHotelUtil getInstance(Context context) {
		mContext = context;
    	if (mKtcHotelSdk == null) {
    		mKtcHotelSdk = new KtcHotelUtil();
    	}
    	return mKtcHotelSdk;
    }
	
	private KtcDataUtil getKtcDataBaseUtil(){
		if(mKtcDataBaseUtil == null){
			mKtcDataBaseUtil = KtcDataUtil.getInstance(mContext);
		}
		return mKtcDataBaseUtil ;
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
	 * TODO 开/关酒店模式
	 * @param boolean toEnable
	 * @return void
	 */
	public void setHotelModeEnable(boolean toEnable) {
		getKtcLogerUtil().I(TAG, "setHotelModeEnable:  "+toEnable);
		getKtcDataBaseUtil().updateDatabase_systemsetting("Hotelmode", toEnable ? 1 : 0);
	}
	/**
	 * TODO 判断酒店模式是否打开
	 * @param null
	 * @return boolean
	 */
	public boolean isHotelModeEnable() {
		int mHotelmode = getKtcDataBaseUtil().getValueDatabase_systemsetting("Hotelmode");
		getKtcLogerUtil().I(TAG, "isHotelModeEnable:  "+(mHotelmode == 1 ? true : false));
		return mHotelmode == 1 ? true : false;
	}
	
	
	/**
	 * TODO 开/关频道锁
	 * @param boolean toEnable
	 * @return void
	 */
	public void setCHLockEnable(boolean toEnable){
		getKtcLogerUtil().I(TAG, "setCHLockEnable:  "+toEnable);
		getKtcDataBaseUtil().updateDatabase_systemsetting("SearchLock", toEnable ? 1 : 0);
	}
	/**
	 * TODO 判断频道锁是否打开
	 * @param 
	 * @return boolean
	 */
	public boolean isCHLockEnable() {
		int mSearchLock = getKtcDataBaseUtil().getValueDatabase_systemsetting("SearchLock");
		getKtcLogerUtil().I(TAG, "isCHLockEnable:  "+(mSearchLock == 1 ? true : false));
		return mSearchLock == 1 ? true : false;
	}
	
	
	/**
	 * TODO 设置系统最大音量
	 * @param int maxVol
	 * @return void
	 */
	public void setMaxVolume(int maxVol){
		if(maxVol >= 0 && maxVol <= 100){
			getKtcDataBaseUtil().updateDatabase_systemsetting("Maxvol", maxVol);
			if (maxVol < getKtcDataBaseUtil().getValueDatabase_systemsetting("PoweronVol")) {
				getKtcDataBaseUtil().updateDatabase_systemsetting("PoweronVol" , maxVol);
				getKtcLogerUtil().I(TAG, "setMaxVolume:  "+maxVol);
			}
		}else{
			getKtcLogerUtil().I(TAG, "setMaxVolume:  The maximum volume value is illegal!");
		}
	}
	/**
	 * TODO 获取当前系统最大音量
	 * @param null
	 * @return int
	 */
	public int getMaxVolume() {
		return getKtcDataBaseUtil().getValueDatabase_systemsetting("Maxvol");
	}
	
	
	/**
	 * TODO 开/关自动设置功能
	 * @param boolean toEnable
	 * @return void
	 */
	public void setAutoSetEnable(boolean toEnable) {
		getKtcLogerUtil().I(TAG, "setAutoSetEnable:  "+toEnable);
		getKtcDataBaseUtil().updateDatabase_systemsetting("Autoset", toEnable ? 1 : 0);
	}
	/**
	 * TODO 判断自动设置是否打开
	 * @param null
	 * @return boolean
	 */
	public boolean isAutoSetEnable() {
		int mAutoset = getKtcDataBaseUtil().getValueDatabase_systemsetting("Autoset");
		getKtcLogerUtil().I(TAG, "isAutoSetEnable:  "+(mAutoset == 1 ? true : false));
		return mAutoset == 1 ? true : false;
	}
	

	/**
	 * TODO 设置开机音量
	 * @param int powerVol
	 * @return void
	 */
	public void setPowerVol(int powerVol){
		int maxValue = getKtcDataBaseUtil().getValueDatabase_systemsetting("Maxvol");
		if (powerVol > maxValue) {
			powerVol = maxValue;
		}
		getKtcLogerUtil().I(TAG, "setPowerVol:  "+powerVol);
		getKtcDataBaseUtil().updateDatabase_systemsetting("PoweronVol", powerVol);
	}
	/**
	 * TODO 获取开机音量值
	 * @param null
	 * @return int
	 */
	public int getPowerVol() {
		return getKtcDataBaseUtil().getValueDatabase_systemsetting("PoweronVol");
	}
	
	/**
	 * TODO 开机默认信源切换至ATV(模拟信号)通道
	 * @param 
	 * @return void
	 */
	public void setPowerSourceToAtv(){
		setPowerSource(TvCommonManager.INPUT_SOURCE_ATV);
	}
	
	/**
	 * TODO 开机默认信源切换至DTV(数字信号)通道
	 * @param 
	 * @return void
	 */
	public void setPowerSourceToDtv(){
		setPowerSource(TvCommonManager.INPUT_SOURCE_DTV);
	}
	
	/**
	 * TODO 开机默认信源切换至AV(视频)通道
	 * @param 
	 * @return void
	 */
	public void setPowerSourceToAv(){
		setPowerSource(TvCommonManager.INPUT_SOURCE_CVBS);
	}
	
	/**
	 * TODO 开机默认信源切换至YPBPR(分量)通道
	 * @param 
	 * @return void
	 */
	public void setPowerSourceToYpbpr(){
		setPowerSource(TvCommonManager.INPUT_SOURCE_YPBPR);
	}
	
	/**
	 * TODO 开机默认信源切换至HDMI(HDMI1)通道
	 * @param 
	 * @return void
	 */
	public void setPowerSourceToHdmi1(){
		setPowerSource(TvCommonManager.INPUT_SOURCE_HDMI);
	}
	
	/**
	 * TODO 开机默认信源切换至HDMI2(HDMI2)通道
	 * @param 
	 * @return void
	 */
	public void setPowerSourceToHdmi2(){
		setPowerSource(TvCommonManager.INPUT_SOURCE_HDMI2);
	}
	
	/**
	 * TODO 开机默认信源切换至HDMI3(HDMI3)通道
	 * @param 
	 * @return void
	 */
	public void setPowerSourceToHdmi3(){
		setPowerSource(TvCommonManager.INPUT_SOURCE_HDMI3);
	}
	
	/**
	 * TODO 开机默认信源切换至VGA通道
	 * @param 
	 * @return void
	 */
	public void setPowerSourceToVga(){
		setPowerSource(TvCommonManager.INPUT_SOURCE_VGA);
	}
	
	/**
	 * TODO 开机默认信源切换至Storage通道(mk:主页及其他非TV界面均要保持在Storage通道，避免TV画面及声音出现)
	 * @param 
	 * @return void
	 */
	public void setPowerSourceToStorage(){
		setPowerSource(TvCommonManager.INPUT_SOURCE_STORAGE);
	}
	
	/**
	 * TODO 设置开机默认信源
	 * @param int powerSource
	 * @return void
	 */
	public void setPowerSource(int powerSource) {
		switch (powerSource) {
			case TvCommonManager.INPUT_SOURCE_ATV://ATV
			case TvCommonManager.INPUT_SOURCE_DTV://DTV
			case TvCommonManager.INPUT_SOURCE_CVBS:// av
			case TvCommonManager.INPUT_SOURCE_HDMI:// hdmi1
			case TvCommonManager.INPUT_SOURCE_HDMI2:// hdmi2
			case TvCommonManager.INPUT_SOURCE_HDMI3:// hdmi3
			case TvCommonManager.INPUT_SOURCE_STORAGE:// storage
			case TvCommonManager.INPUT_SOURCE_VGA:// VGA
				getKtcLogerUtil().I(TAG, "setPowerSource:  "+powerSource);
				getKtcDataBaseUtil().updateDatabase_systemsetting("PoweronSourceType", powerSource);
				break;
			default:
				getKtcDataBaseUtil().updateDatabase_systemsetting("PoweronSourceType", TvCommonManager.INPUT_SOURCE_STORAGE);
				break;
		}
	}
	
	/**
	 * TODO 获取开机默认信源类型
	 * @param null
	 * @return int
	 */
	public int getPowerSource() {
		return getKtcDataBaseUtil().getValueDatabase_systemsetting("PoweronSourceType");
	}
	
	/**
	 * TODO 设置开机默认频道
	 * @param int proNum , int proSourceType
	 * @return void
	 */
	public void setPowerChannel(int proNum , int proSourceType){
		if(proSourceType == TvCommonManager.INPUT_SOURCE_ATV
				|| proSourceType == TvCommonManager.INPUT_SOURCE_DTV){
			KtcChannelUtil mKtcChannelUtil = KtcChannelUtil.getInstance(mContext) ;
			ProgramInfo mProgram = mKtcChannelUtil.getProgramByNumber(proNum , mKtcChannelUtil.getProgramListByType(proSourceType));
			if(mProgram != null){
				getKtcDataBaseUtil().updateDatabase_systemsetting("PoweronSourceType", proSourceType);
				getKtcDataBaseUtil().updateDatabase_systemsetting("PoweronChannel", mProgram.number);
				getKtcLogerUtil().I(TAG, "setPowerChannel:  "+mProgram.number);
			}else{
				getKtcLogerUtil().I(TAG, "setPowerChannel:  The channel number does not match!");
			}
		}
		getKtcLogerUtil().I(TAG, "setPowerChannel:  is not ATV/DTV");
	}
	
	/**
	 * TODO 获取开机默认频道号
	 * @param null
	 * @return int
	 */
	public int getPowerChannel(){
		return getKtcDataBaseUtil().getValueDatabase_systemsetting("PoweronChannel");
	}
	
	/**
	 * TODO 开关按键板锁定功能
	 * @param  boolean toEnable
	 * @return void
	 */
	public void setLockKeyEnable(boolean toEnable) {
		getKtcLogerUtil().I(TAG, "setLockKeyEnable:  "+toEnable);
		getKtcDataBaseUtil().updateDatabase_systemsetting("KeyLock", toEnable ? 1 : 0);
	}
	
	/**
	 * TODO 判断按键板锁定功能是否打开
	 * @param null
	 * @return boolean
	 */
	public boolean isLockKeyEnable() {
		int isKeyLock = getKtcDataBaseUtil().getValueDatabase_systemsetting("KeyLock");
		getKtcLogerUtil().I(TAG, "isLockKeyEnable:  "+(isKeyLock == 1 ? true : false));
		return isKeyLock == 1 ? true : false ;
	}
	
	/**
	 * TODO 开关待机led灯是否亮起
	 * @param boolean toOpen
	 * @return void
	 */
	public void setLedModeEnable(boolean toOpen) {//0:light 1:dark
		getKtcLogerUtil().I(TAG, "setLedModeEnable:  "+toOpen);
		getKtcDataBaseUtil().updateDatabase_systemsetting("LedMode", toOpen ? 0 : 1);
	}
	
	/**
	 * TODO 判断待机led灯是否亮起
	 * @param null
	 * @return boolean
	 */
	public boolean isLedModeEnable() {
		int ledMode = getKtcDataBaseUtil().getValueDatabase_systemsetting("LedMode");
		getKtcLogerUtil().I(TAG, "isLedModeEnable:  "+(ledMode == 1 ? false : true));
		return ledMode == 1 ? false : true;
	}
	
	/**
	 * TODO 开关无信号待机功能
	 * @param boolean toEnable
	 * @return void
	 */
	public void setNoSignalStandbyEnable(boolean toEnable) {
		getKtcLogerUtil().I(TAG, "setNoSignalStandbyEnable:  "+toEnable);
		getKtcDataBaseUtil().updateDatabase_systemsetting("standbyNoSignal", toEnable ? 1 : 0);
	}
	
	/**
	 * TODO 判断无信号待机功能是否打开
	 * @param null
	 * @return boolean
	 */
	public boolean isNoSignalStandbyEnable() {
		int ledMode = getKtcDataBaseUtil().getValueDatabase_systemsetting("standbyNoSignal");
		getKtcLogerUtil().I(TAG, "isNoSignalStandbyEnable:  "+(ledMode == 1 ? true : false));
		return ledMode == 1 ? true : false;
	}
	
	/**
	 * TODO 开关是否允许启动设置等应用
	 * @param boolean toEnable
	 * @return void
	 */
	public void setSettingsEnable(boolean toEnable) {
		getKtcLogerUtil().I(TAG, "setSettingsEnable:  "+toEnable);
		if (toEnable) {
			getKtcDataBaseUtil().updateDatabase_systemsetting("SettingsEnable", 1);
			SystemProperties.set("persist.sys.settings", "true");
		} else {
			getKtcDataBaseUtil().updateDatabase_systemsetting("SettingsEnable", 0);
			SystemProperties.set("persist.sys.settings", "false");
		}
	}
	
	/**
	 * TODO 判断是否允许启动设置等应用
	 * @param null
	 * @return boolean
	 */
	public boolean isSettingsEnable() {
		int mSettingsEnable = getKtcDataBaseUtil().getValueDatabase_systemsetting("SettingsEnable");
		getKtcLogerUtil().I(TAG, "isSettingsEnable:  "+(mSettingsEnable == 1 ? true : false));
		return mSettingsEnable == 1 ? true : false;
	}
	
	/**
	 * TODO 开机进入TV或者主页
	 * @param null
	 * @return void
	 */
	public void switchTvOrHome() {
	  int openSource = 0;
		try{
			openSource = Settings.Global.getInt(mContext.getContentResolver(), "openSource");
		} catch (Settings.SettingNotFoundException e) {
			e.printStackTrace();
		}
		getKtcLogerUtil().I(TAG, "switchTvOrHome:  "+openSource);
		if (openSource == 1) {//to TV
			ComponentName componentName = new ComponentName(
                    "com.mstar.tv.tvplayer.ui", "com.mstar.tv.tvplayer.ui.RootActivity");
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(componentName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            mContext.startActivity(intent);
		} else {//stay Launcher
			if (TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_STORAGE) {
                TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
            }
		}
   }
	
	/**
	 * TODO 设置开机进入TV或者主页标志位
	 * @param boolean
	 * @return boolean
	 */
   public boolean setSwitchTvOrHomeFlag(boolean toTv){//0:home ;1:TV
	   getKtcLogerUtil().I(TAG, "setSwitchTvOrHomeFlag:  "+toTv);
	   return Settings.Global.putInt(mContext.getContentResolver(), "openSource" , toTv ? 1 : 0); 
   }
   
     
   /**
	 * TODO 设置信源键是否有效
	 * @param boolean 
	 * @return boolean
	 */
	public boolean setSourceKeyEnable(boolean toEnable){
	  getKtcLogerUtil().I(TAG, "setSourceKeyEnable:  "+toEnable);
	  return Settings.System.putInt(mContext.getContentResolver(), "source_hot_key_disable", toEnable ? 0 : 1);
   }
	
}
