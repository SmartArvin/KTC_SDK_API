package com.ktc.opensdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
*TODO KTC网络相关接口
*@author Arvin
*@Time 2018-1-9 下午15:44:12
*/
public class KtcNetworkUtil {
	
	private final String TAG = "KtcNetworkUtil";
	private static KtcNetworkUtil mKtcNetworkUtil;
	private static Context mContext = null;
	
	public static KtcNetworkUtil getInstance(Context context) {
		mContext = context;
    	if (mKtcNetworkUtil == null) {
    		mKtcNetworkUtil = new KtcNetworkUtil();
    	}
    	return mKtcNetworkUtil;
    }
	
	/**
	 * TODO 判断网络是否已连接
	 * @param null
	 * @return boolean
	 */
	public boolean isNetWorkConnected(){
		ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
            	return true ;
        }else {
        	return false ;
        }
	}
	
	/**
	 * TODO 获取当前已连接的网络类型
	 * @param null
	 * @return int
	 */
	public int getNetConnectType() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
           return networkInfo.getType() ;
        }else {
          return ConnectivityManager.TYPE_NONE ;
        }
    }
	
	//#####################################################################################################//
    /**
     * TODO 获取设备固定MAC地址
     * @param null
     * @return String
     */
    public String getDeviceMacAddress() {
        return getEthernetMacAddress() ;
    }

    /**
     * TODO 获取有线网络MAC地址
     * @param null
     * @return String
     */
    private String getEthernetMacAddress() {
        try {
            return readLine("/sys/class/net/eth0/address").toUpperCase();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * TODO 获取WLAN无线MAC地址
     * @param null
     * @return String
     */
    private String getWlanMacAddress() {
        try {
            return readLine("/sys/class/net/wlan0/address").toUpperCase();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String readLine(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename), 256);
        try {
            return reader.readLine();
        } finally {
            reader.close();
        }
    }
    
}
