package com.ktc.opensdk;

import com.customer.opensdk.CustomerSpecialUtil;

import android.content.Context;

/**
*TODO KTC第三方开放sdk
*@author Arvin
*@Time 2018-1-9 上午11:00:10
*/
public class KtcOpenSdk {
	
	private static KtcOpenSdk mKtcHotelSdk;
	private static Context mContext = null;
	
	private static KtcChannelUtil mKtcChannelUtil;
	private static KtcHotelUtil mKtcHotelUtil ;
	private static KtcLogoUtil mKtcLogoUtil ;
	private static KtcShellUtil mKtcShellUtil ;
	private static KtcSystemUtil mKtcSystemUtil ;
	private static KtcNetworkUtil mKtcNetworkUtil ;
	private static KtcLogerUtil mKtcLogerUtil ;
	private static CustomerSpecialUtil mCustomerSpecialUtil ;
	
	public static KtcOpenSdk getInstance(Context context) {
		mContext = context;
    	if (mKtcHotelSdk == null) {
    		mKtcHotelSdk = new KtcOpenSdk();
    	}
    	return mKtcHotelSdk;
    }
	
	/**
	 * TODO 获取KtcHotelUtil实例对象
	 * @param null
	 * @return KtcHotelUtil
	 */
	public KtcHotelUtil getKtcHotelUtil(){
		if(mKtcHotelUtil == null){
			mKtcHotelUtil = KtcHotelUtil.getInstance(mContext);
		}
		return mKtcHotelUtil ;
	}
	
	/**
	 * TODO 获取KtcChannelUtil实例对象
	 * @param null
	 * @return KtcChannelUtil
	 */
	public KtcChannelUtil getKtcChannelUtil(){
		if(mKtcChannelUtil == null){
			mKtcChannelUtil = KtcChannelUtil.getInstance(mContext);
		}
		return mKtcChannelUtil ;
	}
	
	/**
	 * TODO 获取KtcLogoUtil实例对象
	 * @param null
	 * @return KtcLogoUtil
	 */
	public KtcLogoUtil getKtcLogoUtil(){
		if(mKtcLogoUtil == null){
			mKtcLogoUtil = KtcLogoUtil.getInstance(mContext);
		}
		return mKtcLogoUtil ;
	}
	
	/**
	 * TODO 获取KtcShellUtil实例对象
	 * @param null
	 * @return KtcShellUtil
	 */
	public KtcShellUtil getKtcShellUtil(){
		if(mKtcShellUtil == null){
			mKtcShellUtil = KtcShellUtil.getInstance();
		}
		return mKtcShellUtil ;
	}
	
	/**
	 * TODO 获取KtcSystemUtil实例对象
	 * @param null
	 * @return KtcSystemUtil
	 */
	public KtcSystemUtil getKtcSystemUtil(){
		if(mKtcSystemUtil == null){
			mKtcSystemUtil = KtcSystemUtil.getInstance(mContext);
		}
		return mKtcSystemUtil ;
	}
	
	/**
	 * TODO 获取KtcNetworkUtil实例对象
	 * @param null
	 * @return KtcNetworkUtil
	 */
	public KtcNetworkUtil getKtcNetworkUtil(){
		if(mKtcNetworkUtil == null){
			mKtcNetworkUtil = KtcNetworkUtil.getInstance(mContext);
		}
		return mKtcNetworkUtil ;
	}
	
	/**
	 * TODO 获取KtcLogerUtil实例对象
	 * @param null
	 * @return KtcLogerUtil
	 */
	public KtcLogerUtil getKtcLogerUtil(){
		if(mKtcLogerUtil == null){
			mKtcLogerUtil = KtcLogerUtil.getInstance();
		}
		return mKtcLogerUtil ;
	}
	
	/**
	 * TODO 获取CustomerSpecialUtil实例对象(内部实现客户专用接口)
	 * @param null
	 * @return CustomerSpecialUtil
	 */
	public CustomerSpecialUtil getCustomerSpecialUtil(){
		if(mCustomerSpecialUtil == null){
			mCustomerSpecialUtil = CustomerSpecialUtil.getInstance(mContext);
		}
		return mCustomerSpecialUtil ;
	}
	
}
