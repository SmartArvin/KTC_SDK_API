package com.ktc.opensdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.ProgramInfo;

/**
*TODO KTC频道设置等
*@author Arvin
*@Time 2018-1-9 上午11:00:10
*/
public class KtcChannelUtil {
	
	private static final String TAG = "KtcChannelUtil";
	private static KtcChannelUtil mKtcChannelUtil;
	private static KtcShellUtil mKtcShellUtil ;
	private static KtcSystemUtil mKtcSystemUtil ;
	private static KtcLogerUtil mKtcLogerUtil ;
	private static Context mContext = null;
	
	//source type
	public static final int INPUT_SOURCE_ATV = TvCommonManager.INPUT_SOURCE_ATV;
	public static final int INPUT_SOURCE_DTV = TvCommonManager.INPUT_SOURCE_DTV;
	public static final int INPUT_SOURCE_AV = TvCommonManager.INPUT_SOURCE_CVBS;
	public static final int INPUT_SOURCE_YPBPR = TvCommonManager.INPUT_SOURCE_YPBPR;
	public static final int INPUT_SOURCE_HDMI = TvCommonManager.INPUT_SOURCE_HDMI;
	public static final int INPUT_SOURCE_HDMI2 = TvCommonManager.INPUT_SOURCE_HDMI2;
	public static final int INPUT_SOURCE_HDMI3 = TvCommonManager.INPUT_SOURCE_HDMI3;
	public static final int INPUT_SOURCE_VGA = TvCommonManager.INPUT_SOURCE_VGA;
	public static final int INPUT_SOURCE_STORAGE = TvCommonManager.INPUT_SOURCE_STORAGE;
	
	/**
	 * TODO 获取KtcChannelUtil实例对象
	 * @param Context
	 * @return KtcChannelUtil
	 */
	public static KtcChannelUtil getInstance(Context context) {
		mContext = context;
    	if (mKtcChannelUtil == null) {
    		mKtcChannelUtil = new KtcChannelUtil();
    	}
    	return mKtcChannelUtil;
    }
	
	
	/**
	 * TODO 获取KtcShellUtil实例对象
	 * @param null
	 * @return KtcShellUtil
	 */
	private static KtcShellUtil getKtcShellUtil() {
    	if (mKtcShellUtil == null) {
    		mKtcShellUtil = new KtcShellUtil();
    	}
    	return mKtcShellUtil;
    }
	
	/**
	 * TODO 获取KtcSystemUtil实例对象
	 * @param null
	 * @return KtcSystemUtil
	 */
	private static KtcSystemUtil getKtcSystemUtil() {
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
	 * TODO 切换至ATV(模拟信号)通道
	 * @param 
	 * @return void
	 */
	public void changeTvSourceToAtv(){
		changeTvSource(TvCommonManager.INPUT_SOURCE_ATV);
	}
	
	/**
	 * TODO 切换至DTV(数字信号)通道
	 * @param 
	 * @return void
	 */
	public void changeTvSourceToDtv(){
		changeTvSource(TvCommonManager.INPUT_SOURCE_DTV);
	}
	
	/**
	 * TODO 切换至AV(视频)通道
	 * @param 
	 * @return void
	 */
	public void changeTvSourceToAv(){
		changeTvSource(TvCommonManager.INPUT_SOURCE_CVBS);
	}
	
	/**
	 * TODO 切换至YPBPR(分量)通道
	 * @param 
	 * @return void
	 */
	public void changeTvSourceToYpbpr(){
		changeTvSource(TvCommonManager.INPUT_SOURCE_YPBPR);
	}
	
	/**
	 * TODO 切换至HDMI(HDMI1)通道
	 * @param 
	 * @return void
	 */
	public void changeTvSourceToHdmi1(){
		changeTvSource(TvCommonManager.INPUT_SOURCE_HDMI);
	}
	
	/**
	 * TODO 切换至HDMI2(HDMI2)通道
	 * @param 
	 * @return void
	 */
	public void changeTvSourceToHdmi2(){
		changeTvSource(TvCommonManager.INPUT_SOURCE_HDMI2);
	}
	
	/**
	 * TODO 切换至HDMI3(HDMI3)通道
	 * @param 
	 * @return void
	 */
	public void changeTvSourceToHdmi3(){
		changeTvSource(TvCommonManager.INPUT_SOURCE_HDMI3);
	}
	
	/**
	 * TODO 切换至VGA通道
	 * @param 
	 * @return void
	 */
	public void changeTvSourceToVga(){
		changeTvSource(TvCommonManager.INPUT_SOURCE_VGA);
	}
	
	/**
	 * TODO 切换至Storage通道(mk:主页及其他非TV界面均要保持在Storage通道，避免TV画面及声音出现)
	 * @param 
	 * @return void
	 */
	public void changeTvSourceToStorage(){
		changeTvSource(TvCommonManager.INPUT_SOURCE_STORAGE);
	}
	
	/**
	 * TODO 根据频道号获取对应ProgramInfo
	 * @param int proNum , ArrayList<ProgramInfo>
	 * @return ProgramInfo
	 */
	public ProgramInfo getProgramByNumber(int proNum , ArrayList<ProgramInfo> proLists) {
		if(proLists != null && proLists.size() > 0){
			for(ProgramInfo mProgramInfo : proLists){
				if(mProgramInfo.number == proNum){
					getKtcLogerUtil().I(TAG, "getProgramByNumber:  "+mProgramInfo.number);
					return mProgramInfo ;
				}
			}
		}
		return null ;
	}
	
	/**
	 * TODO 获取ATV(模拟电视)频道列表
	 * @param null
	 * @return ArrayList<ProgramInfo>
	 */
	public ArrayList<ProgramInfo> getProgramListForAtv(){
		return getProgramListByType(TvCommonManager.INPUT_SOURCE_ATV);
	}
	
	/**
	 * TODO 获取DTV(数字电视)频道列表
	 * @param null
	 * @return ArrayList<ProgramInfo>
	 */
	public ArrayList<ProgramInfo> getProgramListForDtv(){
		return getProgramListByType(TvCommonManager.INPUT_SOURCE_DTV);
	}
	
	/**
	 * TODO 获取对应信源的频道列表
	 * @param int sourceType
	 * @return ArrayList<ProgramInfo>
	 */
	public ArrayList<ProgramInfo> getProgramListByType(int sourceType){
	    int mServiceNum = 0;
		ProgramInfo pgi = null;
		ArrayList<ProgramInfo> mProInfoList_DTV = new ArrayList<ProgramInfo>();
		ArrayList<ProgramInfo> mProInfoList_ATV = new ArrayList<ProgramInfo>();
		
		
		mServiceNum = TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
		for (int k = 0; k < mServiceNum; k++) {
            pgi = TvChannelManager.getInstance().getProgramInfoByIndex(k);
            if (pgi != null) {
                // Show All Programs
                if ((pgi.isDelete == true) || (pgi.isVisible == false)) {
                    continue;
                } else {
                	ProgramInfoObject pfo = new ProgramInfoObject();
                    if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                        pfo.setChannelId(String.valueOf(getATVDisplayChNum(pgi.number)));
                    } else if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_DTV){
                        pfo.setChannelId(String.valueOf(pgi.number));
                    }
                    
                    pfo.setChannelName(pgi.serviceName);
                    pfo.setServiceType(pgi.serviceType);
                    pfo.setSkipImg(pgi.isSkip);
                    pfo.setSslImg(pgi.isScramble);
                    pfo.setFrequenry(pgi.frequency);
                    
                    if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                    	getKtcLogerUtil().I(TAG, "getProgramListByType——ATV:  "+pgi.serviceName);
                    	mProInfoList_ATV.add(pgi);
                    } else if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_DTV){
                    	getKtcLogerUtil().I(TAG, "getProgramListByType——DTV:  "+pgi.serviceName);
                    	mProInfoList_DTV.add(pgi);
                    }
                }
            }
        }
		
		if (sourceType == TvCommonManager.INPUT_SOURCE_ATV) {
			return mProInfoList_ATV;
		} else if(sourceType == TvCommonManager.INPUT_SOURCE_DTV){
			return mProInfoList_DTV;
		}
		
		return null ;
	}
	
	private class ProgramInfoObject {
        private String channelId = null;
        private String channelName = null;
        private short serviceType;
        private boolean skipImg = false;
        private boolean sslImg = false;
        private int frequenry = 0;
        private int indexOfProgInfoList = 0;

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public short getServiceType() {
            return serviceType;
        }

        public void setServiceType(short type) {
            this.serviceType = type;
        }

        public boolean isSkipImg() {
            return skipImg;
        }

        public void setSkipImg(boolean skipImg) {
            this.skipImg = skipImg;
        }

        public boolean isSslImg() {
            return sslImg;
        }

        public void setSslImg(boolean sslImg) {
            this.sslImg = sslImg;
        }

        public void setFrequenry(int f) {
            frequenry = f;
        }

        public int getFrequenry() {
            return frequenry;
        }

        public void setProgInfoListIdx(int idx) {
            indexOfProgInfoList = idx;
        }

        public int getProgInfoListIdx() {
            return indexOfProgInfoList;
        }
    }
	
	
	/**
	 * TODO 复制TV频道表至USB
	 * @param null
	 * @return boolean
	 */
	public boolean cloneProgramsTvToUSB() {
		int i = 0;
		String usbPath = getKtcSystemUtil().getFirstUsbPath();
		
		File USBFile_DTMB[] = new File[2];
		File TVFile_DTMB[] = new File[2];
		
		USBFile_DTMB[0] = new File(usbPath+"atv_cmdb.bin");
		USBFile_DTMB[1] = new File(usbPath+"dtv_cmdb_0.bin");
		
		TVFile_DTMB[0] = new File("/tvdatabase/Database/atv_cmdb.bin");
		TVFile_DTMB[1] = new File("/tvdatabase/Database/dtv_cmdb_0.bin");

		for (i = 0; i < 2; i++) {
			getKtcShellUtil().cp(TVFile_DTMB[i].getAbsolutePath() , USBFile_DTMB[i].getAbsolutePath());
			getKtcShellUtil().sync();
		}

		for (i = 0; i < 2; i++) {
			if (!USBFile_DTMB[i].exists()) {
				getKtcLogerUtil().I(TAG, "cloneProgramsTvToUSB:  file clone fail!!!");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * TODO 克隆USB中频道表至TV
	 * @param null
	 * @return boolean
	 */
	public boolean cloneProgramsUSBToTv() {
		int i = 0;
		String usbPath = getKtcSystemUtil().getFirstUsbPath();
		
		File USBFile_DTMB[] = new File[2];
		File TVFile_DTMB[] = new File[2];
		
		USBFile_DTMB[0] = new File(usbPath+"atv_cmdb.bin");
		USBFile_DTMB[1] = new File(usbPath+"dtv_cmdb_0.bin");
		
		TVFile_DTMB[0] = new File("/tvdatabase/Database/atv_cmdb.bin");
		TVFile_DTMB[1] = new File("/tvdatabase/Database/dtv_cmdb_0.bin");


		for (i = 0; i < 2; i++) {
			if (!USBFile_DTMB[i].exists()) {
				getKtcLogerUtil().I(TAG, "cloneProgramsUSBToTv:  file not exit!!!");
				return false;
			}
		}
		try {
			for (i = 0; i < 2; i++) {
				FileInputStream fis = new FileInputStream(USBFile_DTMB[i]);
				FileOutputStream fos = new FileOutputStream(TVFile_DTMB[i]);
				byte[] bt = new byte[fis.available()];
				fis.read(bt);
				fos.write(bt);
				fos.close();
				fis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			getKtcLogerUtil().I(TAG, "cloneProgramsUSBToTv:  file clone fail!!!");
			return false;
		}
		TvCommonManager.getInstance().rebootSystem("reboot");
	
		return true;
	}
	
	/**
	 * TODO 根据频道号切换ATV频道
	 * @param int proNum
	 * @return boolean
	 */
	public boolean selectProgramForAtv(int proNum){
		if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV){
			TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
			return selectProgram(proNum);
		}else{
			getKtcLogerUtil().I(TAG, "selectProgram:  Program Type Illegal!");
			return false;
		}
	}
	
	/**
	 * TODO 根据频道号切换DTV频道
	 * @param int proNum 
	 * @return boolean
	 */
	public boolean selectProgramForDtv(int proNum){
		if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV){
			TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
			return selectProgram(proNum);
		}else{
			getKtcLogerUtil().I(TAG, "selectProgram:  Program Type Illegal!");
			return false;
		}
	}
	
	private boolean selectProgram(int proNum) {
		
		TvCommonManager mTvCommonManager = TvCommonManager.getInstance();
		TvChannelManager mTvChannelManager = TvChannelManager.getInstance();
		
		boolean isSuccess = false;
		int mProgramType = getProgramType(proNum);
		int mInputSource = mTvCommonManager.getCurrentTvInputSource();
		
		if (mProgramType >= 0) {
			if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
				isSuccess = mTvChannelManager.selectProgram((proNum - 1) , TvChannelManager.SERVICE_TYPE_ATV);
			} else if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
				switch (mProgramType) {
				case TvChannelManager.SERVICE_TYPE_DTV:
					isSuccess = mTvChannelManager.selectProgram(proNum , TvChannelManager.SERVICE_TYPE_DTV);
					break;
				case TvChannelManager.SERVICE_TYPE_RADIO:
					isSuccess = mTvChannelManager.selectProgram(proNum , TvChannelManager.SERVICE_TYPE_RADIO);
					break;
				case TvChannelManager.SERVICE_TYPE_DATA:
					isSuccess = mTvChannelManager.selectProgram(proNum , TvChannelManager.SERVICE_TYPE_DATA);
					break;
				}
			}
		}
		getKtcLogerUtil().I(TAG, "selectProgram:  "+isSuccess);
		return isSuccess;
	}
	
	private int getProgramType(int proNum) {
		
		TvCommonManager mTvCommonManager = TvCommonManager.getInstance();
		
		int mProgramType = -1;
		ArrayList<ProgramInfo> mProgramList = getAllProgramList();
		int curInputSrc = mTvCommonManager.getCurrentTvInputSource();
		
		if (TvCommonManager.INPUT_SOURCE_ATV == curInputSrc) {
			for (ProgramInfo mAtvPro : mProgramList) {
				if ((proNum - 1) == mAtvPro.number && mAtvPro.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
					mProgramType = TvChannelManager.SERVICE_TYPE_ATV;
					break;
				}
			}
		} else if (TvCommonManager.INPUT_SOURCE_DTV == curInputSrc) {
			for (ProgramInfo mDtvPro : mProgramList) {
				if (proNum == mDtvPro.number) {
					mProgramType = TvChannelManager.SERVICE_TYPE_DTV;
					break;
				}
			}
			
			for (ProgramInfo mRadioPro : mProgramList) {
				if (proNum == mRadioPro.number) {
					if (mRadioPro.serviceType == TvChannelManager.SERVICE_TYPE_RADIO) {
						mProgramType = TvChannelManager.SERVICE_TYPE_RADIO;
						break;
					}
				}
			}

			for (ProgramInfo mDataPro : mProgramList) {
				if (proNum == mDataPro.number) {
					if (mDataPro.serviceType == TvChannelManager.SERVICE_TYPE_DATA) {
						mProgramType = TvChannelManager.SERVICE_TYPE_DATA;
						break;
					}
				}
			}
		}
		getKtcLogerUtil().I(TAG, "getProgramType:  "+mProgramType);
		return mProgramType;
	}
	
	private ArrayList<ProgramInfo> getAllProgramList() {
		
		TvChannelManager mTvChannelManager = TvChannelManager.getInstance();

		ArrayList<ProgramInfo> mProgramNumbers = new ArrayList<ProgramInfo>();
		int m_nServiceNum = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
		
		for (int i = 0; i < m_nServiceNum; i++) {
			ProgramInfo mProgramInfo = (ProgramInfo) mTvChannelManager.getProgramInfoByIndex(i);
			if (mProgramInfo != null) {
				if (mProgramInfo.isDelete == true) {
					continue;
				} else {
					getKtcLogerUtil().I(TAG, "getAllProgramList:  "+mProgramInfo.serviceName);
					mProgramNumbers.add(mProgramInfo);
				}
			}
		}
		
		return mProgramNumbers;
	}
	
	
	/**
	 * TODO 切换至指定信源
	 * @param final int sourceType
	 * @return void
	 */
	public void changeTvSource(final int sourceType){
		switch (sourceType) {
		case TvCommonManager.INPUT_SOURCE_ATV:
		case TvCommonManager.INPUT_SOURCE_DTV:
		case TvCommonManager.INPUT_SOURCE_CVBS:
		case TvCommonManager.INPUT_SOURCE_YPBPR:
		case TvCommonManager.INPUT_SOURCE_HDMI:
		case TvCommonManager.INPUT_SOURCE_HDMI2:
		case TvCommonManager.INPUT_SOURCE_HDMI3:
		case TvCommonManager.INPUT_SOURCE_VGA:
			if(sourceType == TvCommonManager.INPUT_SOURCE_DTV && !getKtcSystemUtil().hasDTMB()){
				return ;
			}
			
	        if (TvCommonManager.getInstance().getCurrentTvInputSource() >= TvCommonManager.INPUT_SOURCE_STORAGE) {
	            Intent source_switch_from_storage = new Intent("source.switch.from.storage");
	            mContext.sendBroadcast(source_switch_from_storage);
	            executePreviousTask(sourceType);
	        } else {
	            new Thread(new Runnable(){
	                @Override
	                public void run(){
	                	try {
	                        Thread.sleep(1000);
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                	updateSourceInputType(sourceType);
	                }
	           }).start();
	        }
			break;
		case TvCommonManager.INPUT_SOURCE_STORAGE:
			TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
			break;

		default:
			break;
		}
	}
	
    private void executePreviousTask(final int inputSource) {
    	getKtcLogerUtil().I(TAG, "executePreviousTask:  "+inputSource);
    	
    	final Intent mIntent = new Intent("com.mstar.android.intent.action.START_TV_PLAYER");
    	mIntent.putExtra("task_tag", "input_source_changed");
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (inputSource == TvCommonManager.INPUT_SOURCE_ATV){
                	TvCommonManager.getInstance().setInputSource(inputSource);
                    int curChannelNumber = TvChannelManager.getInstance().getCurrentChannelNumber();
                    if (curChannelNumber > 0xFF) {
                        curChannelNumber = 0;
                    }
                    TvChannelManager.getInstance().setAtvChannel(curChannelNumber);
                }else if (inputSource == TvCommonManager.INPUT_SOURCE_DTV){
                	TvCommonManager.getInstance().setInputSource(inputSource);
                	TvChannelManager.getInstance().playDtvCurrentProgram();
                }else{
                	TvCommonManager.getInstance().setInputSource(inputSource);
                }

                try {
                    if (mIntent != null){
                    	mContext.startActivity(mIntent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
    private void updateSourceInputType(int inputSourceTypeIdex){
    	getKtcLogerUtil().I(TAG, "updateSourceInputType:  "+inputSourceTypeIdex);
        long ret = -1;
        ContentValues vals = new ContentValues();
        vals.put("enInputSourceType", inputSourceTypeIdex);
        try{
            ret = mContext.getContentResolver().update(Uri.parse("content://mstar.tv.usersetting/systemsetting"),
                            vals, null, null);
            //call Supernova
            short T_SystemSetting_IDX = 0x19;
            TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(T_SystemSetting_IDX);
        }catch(SQLException e){
        	e.printStackTrace();
        } catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	private int getATVDisplayChNum(int chNo) {
        int num = chNo;
        if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_PAL_ENABLE)) {
            num += 1;
        } else if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE)) {
            if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ISDB) {
                num += 1;
            }
        }
        getKtcLogerUtil().I(TAG, ""+num);
        return num;
    }

	
	/**
	 * TODO 获取当前系统信源通道类型
	 * @param null
	 * @return int
	 */
	public int getCurrentInputSource(){
		return TvCommonManager.getInstance().getCurrentTvInputSource();
	}
	
}
