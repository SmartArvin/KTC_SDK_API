package com.customer.opensdk;

import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvPictureManager;

import android.content.Context;
import android.content.Intent;

/**
*TODO 客户专用功能处理工具类
*@author Arvin
*@Time 2018-6-20 下午13:24:02
*/
public class CustomerSpecialUtil {
	
	private final String TAG = "CustomerSpecialUtil";
	private static CustomerSpecialUtil mCustomerSpecialUtil;
	private static Context mContext = null;
	
	private int[]  zoomTypes = new int[]{//Auto/16:9/4:3/Movie/Subtitle/PC Mode
  			TvPictureManager.VIDEO_ARC_AUTO,TvPictureManager.VIDEO_ARC_16x9 ,
  			TvPictureManager.VIDEO_ARC_4x3 ,TvPictureManager.VIDEO_ARC_ZOOM1,
  			TvPictureManager.VIDEO_ARC_ZOOM2 ,TvPictureManager.VIDEO_ARC_DOTBYDOT};
	//Standard/Lightness/Soft/User
	private int[] picModes = new int[]{
	    		TvPictureManager.PICTURE_MODE_NORMAL , TvPictureManager.PICTURE_MODE_DYNAMIC ,
	    		TvPictureManager.PICTURE_MODE_SOFT , TvPictureManager.PICTURE_MODE_USER
	        };
	
	//Standard/Music/Movie/User
	private int[] soundModes = new int[]{
			TvAudioManager.SOUND_MODE_STANDARD , TvAudioManager.SOUND_MODE_MUSIC ,
			TvAudioManager.SOUND_MODE_MOVIE , TvAudioManager.SOUND_MODE_USER
	};
	
	public static CustomerSpecialUtil getInstance(Context context) {
		try {
			mContext = context;
	    	if (mCustomerSpecialUtil == null) {
	    		mCustomerSpecialUtil = new CustomerSpecialUtil();
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return mCustomerSpecialUtil;
    }
	
	/**
	 * TODO 客户需要在launcher中调用
	 * @author lixq
	 * @Time  2018-7-2    下午17:03:02
	 */
	public void showOptionMenu(){
		try {
			Intent intent = new Intent("com.mstar.tv.tvplayer.ui.optionmenu.OptionMenuActivity");
			if (intent.resolveActivity(mContext.getPackageManager()) != null) {
	            mContext.startActivity(intent);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPictureMode(int index) {
		try {
			TvPictureManager mTvPictureManager = TvPictureManager.getInstance();
			if (mTvPictureManager != null) {
				mTvPictureManager.setPictureMode(picModes[index]);
				int mArcType = mTvPictureManager.getVideoArcType();
				 for(int i = 0 ; i < zoomTypes.length ; i++){
		         	if(mArcType == zoomTypes[i]){
		         		mTvPictureManager.setVideoArcType(
		                		zoomTypes[i]);
		         	}
		         }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getPictureMode() {
		try {
			TvPictureManager mTvPictureManager = TvPictureManager.getInstance();
			if (mTvPictureManager != null) {
	            int mPictureMode = mTvPictureManager.getPictureMode();
	            //初始化pictureMode
	            for(int index = 0 ; index< picModes.length ; index++){
	            	if(mPictureMode == picModes[index]){
	            		return index;
	            	}
	            }
		  }
		} catch (Exception e) {
			e.printStackTrace();
		}
		  return 0;
	}
	
	
	public void setSoundMode(int index) {
		try {
			TvAudioManager tvAudioManager = TvAudioManager.getInstance();
			if (tvAudioManager != null) {
	            tvAudioManager.setAudioSoundMode(soundModes[index]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public int getSoundMode() {
		try {
			TvAudioManager tvAudioManager = TvAudioManager.getInstance();
			if (tvAudioManager != null) {
				for(int index = 0 ; index < soundModes.length ; index++){
		    		if(tvAudioManager.getAudioSoundMode() == soundModes[index]){
		    			 return index;
		    		}
		    	}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return 0;
	}
	
}
