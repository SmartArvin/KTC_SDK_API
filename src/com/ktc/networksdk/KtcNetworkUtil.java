package com.ktc.networksdk;

import android.content.Context;
import android.os.Build;
import android.util.Log;

public class KtcNetworkUtil {

	private final static String TAG = "KtcNetworkUtil";
	private static KtcNetworkUtil instance;
	private static Context mContext;

	private static final String VERSION_RELEASE_6_0 = "6.0";
	private static final String VERSION_RELEASE_4_4 = "4.4.4";

	private NetworkUtil_4_4 mNetworkUtil_4_4;
	private NetworkUtil_6_0 mNetworkUtil_6_0;

	public static KtcNetworkUtil getInstance(Context context) {
		try {
			mContext = context;
			if (null == instance) {
				instance = new KtcNetworkUtil();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	private KtcNetworkUtil() {
		Log.d(TAG, TAG + " >>  Build.VERSION.RELEASE: " + Build.VERSION.RELEASE);
		try {
			if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_6_0)) {
				mNetworkUtil_6_0 = new NetworkUtil_6_0(mContext);
			} else if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_4_4)) {
				mNetworkUtil_4_4 = new NetworkUtil_4_4(mContext);
			}
		} catch (Exception e) {
		}
	}

	public NetInformation getWifiInformation() {
		try {
			NetInformation information = new NetInformation();
			if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_6_0)) {
				information = mNetworkUtil_6_0.getWifiInformation();
			} else if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_4_4)) {
				information = mNetworkUtil_4_4.getWifiInformation();
			}
			return information;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public NetInformation getEthernetInformation() {
		try {
			NetInformation information = new NetInformation();
			if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_6_0)) {
				information = mNetworkUtil_6_0.getEthernetInformation();
			} else if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_4_4)) {
				information = mNetworkUtil_4_4.getEthernetInformation();
			}
			return information;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setDhcpEthernetConnect() {
		try {
			if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_6_0)) {
				mNetworkUtil_6_0.setDhcpEthernetConnect();
			} else if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_4_4)) {
				mNetworkUtil_4_4.setDhcpEthernetConnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int setStaticEthernetConnect(String ip, String submask,
			String gateway, String dns1, String dns2) {
		try {
			int flag = -1;
			if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_6_0)) {
				flag = mNetworkUtil_6_0.setStaticEthernetConnect(ip, submask,
						gateway, dns1, dns2);
			} else if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_4_4)) {
				flag = mNetworkUtil_4_4.setStaticEthernetConnect(ip, submask,
						gateway, dns1, dns2);
			}
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getEthMac() {
		// eth mac
		String mac = null;
		try {
			if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_6_0)) {
				mac = mNetworkUtil_6_0.getEthMac();
			} else if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_4_4)) {
				mac = mNetworkUtil_4_4.getEthMac();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mac;
	}

	public String getWifiMac() {
		// wifi mac
		String mac = null;
		try {
			if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_6_0)) {
				mac = mNetworkUtil_6_0.getWifiMac();
			} else if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_4_4)) {
				mac = mNetworkUtil_4_4.getWifiMac();
			}
			return mac;
		} catch (Exception ex) {
		}
		return "02:00:00:00:00:00";
	}

	public String getEthConnectType() {
		String ethConnectType = null;
		try {
			if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_6_0)) {
				ethConnectType = mNetworkUtil_6_0.getEthConnectType();
			} else if (Build.VERSION.RELEASE.endsWith(VERSION_RELEASE_4_4)) {
				ethConnectType = mNetworkUtil_4_4.getEthConnectType();
			}
			return ethConnectType;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
}
