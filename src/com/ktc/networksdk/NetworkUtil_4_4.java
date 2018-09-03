package com.ktc.networksdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mstar.android.ethernet.EthernetDevInfo;
import com.mstar.android.ethernet.EthernetManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.RouteInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class NetworkUtil_4_4 {
	private final static String TAG = "NetworkUtil_4_4";
	private EthernetManager mEthernetManager;
	private WifiManager mWifiManager;
	private static Context mContext;
	private ConnectivityManager connectivityManager;

	NetworkUtil_4_4(Context context) {
		try {
			mContext = context;
			mEthernetManager = getEthernetManager();
			mWifiManager = getWifiManager();
		} catch (Exception e) {
		}
	}

	// 获取EthernetManager实例
	private EthernetManager getEthernetManager() {
		try {
			if (mEthernetManager == null) {
				mEthernetManager = EthernetManager.getInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mEthernetManager;
	}

	// 获得WifiManager实例
	private WifiManager getWifiManager() {
		try {
			if (mWifiManager == null) {
				mWifiManager = (WifiManager) mContext
						.getSystemService(Context.WIFI_SERVICE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mWifiManager;
	}

	public NetInformation getWifiInformation() {
		try {
			Log.d(TAG, "ethernet connected = " + isEthernetConnected()
					+ ", wifi connected = " + isWifiConnected());

			if (isWifiConnected()) {
				WifiInfo info = mWifiManager.getConnectionInfo();
				if (info == null) {
					return null;
				}
				NetInformation information = new NetInformation();
				WifiConfiguration config = null;

				List<WifiConfiguration> networks = mWifiManager
						.getConfiguredNetworks();
				if (networks != null) {
					for (WifiConfiguration net : networks) {
						if (net.networkId == info.getNetworkId()) {
							config = net;
							break;
						}
					}
				}
				information.setType(NetInformation.TYPE_WIFI);
				information.setDeviceName(info.getSSID().replace("\"", ""));
				information.setSSID(info.getSSID());
				information.setBSSID(info.getBSSID());
				int wifiSpeed = info.getLinkSpeed();
				information.setSpeed((wifiSpeed > 0 ? wifiSpeed : 0)
						+ WifiInfo.LINK_SPEED_UNITS);
				information.setQuality(WifiManager.calculateSignalLevel(
						info.getRssi(), 4));
				information.setMAC(info.getMacAddress());
				DhcpInfo dhcpInfo = mWifiManager.getDhcpInfo();
				if (dhcpInfo.leaseDuration == 0) {
					information.setMode(NetInformation.MODE_MANUAL);
				} else {
					information.setMode(NetInformation.MODE_DHCP);
				}
				information.setIP(intToIp(dhcpInfo.ipAddress));
				information.setGateway(intToIp(dhcpInfo.gateway));
				information.setSubnet(intToIp(dhcpInfo.netmask));
				information.setDNS1(intToIp(dhcpInfo.dns1));
				information.setDNS2(intToIp(dhcpInfo.dns2));
				
				if (config != null) {
					Log.d(TAG, "config.toString(): " + config.toString());
					if (config.allowedKeyManagement
							.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
						information.setSecuity(NetInformation.SECURITY_WPA);
					} else if (config.allowedKeyManagement
							.get(WifiConfiguration.KeyMgmt.WPA_EAP)
							|| config.allowedKeyManagement
									.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
						information.setSecuity(NetInformation.SECURITY_EAP);
					} else if (config.wepKeys[0] != null) {
						information.setSecuity(NetInformation.SECURITY_WEP);
					} else {
						information.setSecuity(NetInformation.SECURITY_NONE);
					}

				/*	String[] ips = null;
					LinkProperties linkProperties = config.linkProperties;
					Iterator<LinkAddress> iterator = linkProperties
							.getLinkAddresses().iterator();
					if (iterator.hasNext()) {
						LinkAddress linkAddress = iterator.next();
						information.setIP(linkAddress.getAddress()
								.getHostAddress());
						ips = resolutionIP(information.getIP());
					}

					String[] gateways = null;
					// gateway
					for (RouteInfo route : linkProperties.getRoutes()) {
						if (route.isDefaultRoute()) {
							information.setGateway(route.getGateway()
									.getHostAddress());
							gateways = resolutionIP(information.getGateway());
							break;
						}
					}

					String dns1 = null;
					String dns2 = null;
					// dns1
					Iterator<InetAddress> dnsIterator = linkProperties
							.getDnsServers().iterator();
					if (dnsIterator.hasNext()) {
						dns1 = dnsIterator.next().getHostAddress();
						if (matchAddress(dns1)) {
							information.setDNS1(dns1);
						}
					}
					// dns2
					if (dnsIterator.hasNext()) {
						dns2 = dnsIterator.next().getHostAddress();
						if (matchAddress(dns2)) {
							information.setDNS1(dns2);
						}
					}

					String mask = null;
					if (null != ips && null != gateways) {
						if (ips[0].equals(gateways[0])) {
							mask = "255";
						} else {
							mask = "0";
						}
						if (ips[1].equals(gateways[1])) {
							mask += ".255";
						} else {
							mask += ".0";
						}
						if (ips[2].equals(gateways[2])) {
							mask += ".255";
						} else {
							mask += ".0";
						}
						if (ips[3].equals(gateways[3])) {
							mask += ".255";
						} else {
							mask += ".0";
						}
					}
					information.setSubnet(mask);*/
				}
				Log.d(TAG, information.toString());
				return information;
			} else {

				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public NetInformation getEthernetInformation() {
		try {

			NetInformation information = new NetInformation();
			EthernetDevInfo info = mEthernetManager.getSavedConfig();
			Log.d(TAG, "info:  " + info.toString());
			if (info == null)
				return null;
			Log.d(TAG,
					"name: " + info.getIfName() + ", ip: "
							+ info.getIpAddress());

			information.setType(NetInformation.TYPE_ETHERNET);
			information.setDeviceName(info.getIfName());
			String mode = info.getConnectMode();

			if (mode.equals(EthernetDevInfo.ETHERNET_CONN_MODE_DHCP)) {
				information.setMode(NetInformation.MODE_DHCP);
			} else if (mode.equals(EthernetDevInfo.ETHERNET_CONN_MODE_MANUAL)) {
				information.setMode(NetInformation.MODE_MANUAL);
			}

			information.setIP(info.getIpAddress());
			information.setSubnet(info.getNetMask());
			information.setGateway(info.getRouteAddr());
			information.setDNS1(info.getDnsAddr());
			information.setDNS2(info.getDns2Addr());
			information.setMAC(info.getMacAddress(information.getDeviceName()));
			return information;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setDhcpEthernetConnect() {
		try {
			openEthernet();
			EthernetDevInfo devInfo = new EthernetDevInfo();
			devInfo.setIfName("eth0");
			devInfo.setConnectMode(EthernetDevInfo.ETHERNET_CONN_MODE_DHCP);
			devInfo.setIpAddress(null);
			devInfo.setNetMask(null);
			devInfo.setRouteAddr(null);
			devInfo.setDnsAddr(null);
			devInfo.setDns2Addr(null);
			mEthernetManager.updateDevInfo(devInfo);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int setStaticEthernetConnect(String ip, String submask,
			String gateway, String dns1, String dns2) {
		try {

			if (!matchAddress(ip)) {
				return 0;
			} else if (!matchAddress(submask)) {
				return 0;
			} else if (!matchAddress(gateway)) {
				return 0;
			} else if (!matchAddress(dns1)) {
				return 0;
			}

			openEthernet();
			EthernetDevInfo devInfo = new EthernetDevInfo();
			devInfo.setIfName("eth0");
			devInfo.setConnectMode(EthernetDevInfo.ETHERNET_CONN_MODE_MANUAL);
			devInfo.setIpAddress(ip);
			devInfo.setNetMask(submask);
			devInfo.setRouteAddr(gateway);
			devInfo.setDnsAddr(dns1);
			mEthernetManager.updateDevInfo(devInfo);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getEthConnectType() {
		if(isEthernetAutoIP()){
			return "DHCP";
		}else if(isEthernetStaticIP()){
			return "STATIC";
		}
		return "";
	}
	
	/**
	 * TODO 判断有线网络是否为自动连接（HDCP）
	 * @param null
	 * @return boolean
	 */
	public boolean isEthernetAutoIP() {
		EthernetDevInfo mEthInfo = getEthernetManager().getSavedConfig();
		if (null != mEthInfo
				&& mEthInfo.getConnectMode().equals(
						EthernetDevInfo.ETHERNET_CONN_MODE_DHCP)) {
			return true;
		}
		return false;
	}
	
	/**
	 * TODO 判断有线网络是否为静态IP连接
	 * @param null
	 * @return boolean
	 */
	public boolean isEthernetStaticIP() {
		EthernetDevInfo mEthInfo = getEthernetManager().getSavedConfig();
		if (null != mEthInfo
				&& mEthInfo.getConnectMode().equals(EthernetDevInfo.ETHERNET_CONN_MODE_MANUAL)) {
			return true;
		}

		return false;
	}

	public String getEthMac() {
		// eth mac
		String mac = null;
		try {
			EthernetManager mEtherneManager = EthernetManager.getInstance();
			EthernetDevInfo devInfo = mEtherneManager.getSavedConfig();
			String str_mac = "";
			if (devInfo == null) {
				str_mac = "";
			} else {
				str_mac = devInfo.getMacAddress(devInfo.getIfName());
			}
			if (str_mac.equals("00:30:1B:BA:02:DB")) {// default mac, not write
														// mac
				str_mac = " ";
			}
			return str_mac;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mac;
	}

	public String getWifiMac() {
		// wifi mac
		try {
			WifiManager wifiManager = (WifiManager) mContext
					.getSystemService(Context.WIFI_SERVICE);
			return wifiManager.getConnectionInfo().getMacAddress()
					.toUpperCase();
		} catch (Exception ex) {
		}
		return "02:00:00:00:00:00";
	}

	public WifiConfiguration getWifiConfiguredNetwork() {
		try {
			if (mWifiManager == null) {
				getWifiManager();
			}
			if (mWifiManager.isWifiEnabled()) {
				WifiInfo wifi = mWifiManager.getConnectionInfo();
				List<WifiConfiguration> configs = mWifiManager
						.getConfiguredNetworks();
				if (configs == null) {
					return null;
				}
				String ssid = wifi.getSSID();
				for (WifiConfiguration config : configs) {
					if (ssid.equals(config.SSID)) {
						return config;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isWifiConnected() {
		try {
			// wifi is disabled
			if (!isWifiOpen()) {
				return false;
			}

			ConnectivityManager manager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo.State state = manager.getNetworkInfo(
					ConnectivityManager.TYPE_WIFI).getState();

			// wifi have not connected
			WifiInfo info = mWifiManager.getConnectionInfo();
			if (info == null || info.getSSID() == null
					|| info.getNetworkId() == -1
					|| state != NetworkInfo.State.CONNECTED) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean isWifiOpen() {
		return mWifiManager.isWifiEnabled();
	}

	public void openEthernet() {
		try {
			if (!isEthernetEnabled()) {

				mEthernetManager.setEnabled(true);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isEthernetConnected() {
		if (isEthernetEnabled() && isNetInterfaceAvailable()) {
			return true;
		}
		return false;
	}

	public boolean isEthernetEnabled() {
		  if (EthernetManager.ETHERNET_STATE_ENABLED == mEthernetManager.getState()) {
	            return true;
	        }

		return false;
	}

	public boolean isNetInterfaceAvailable() {
		String netInterfaceStatusFile = "/sys/class/net/eth0/carrier";
		return isStatusAvailable(netInterfaceStatusFile);
	}

	private boolean isStatusAvailable(String statusFile) {
		char st = readStatus(statusFile);
		if (st == '1') {
			return true;
		}

		return false;
	}

	private synchronized char readStatus(String filePath) {
		int tempChar = 0;
		File file = new File(filePath);
		if (file.exists()) {
			Reader reader = null;
			try {
				reader = new InputStreamReader(new FileInputStream(file));
				tempChar = reader.read();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (reader != null)
						reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return (char) tempChar;
	}

	private String getIpAddress(int ip) {
		int ip1 = (ip & 0xFF000000) >> 24;
		ip1 = ip1 >= 0 ? ip1 : 256 + ip1;
		// int ip1 = (ip & 0x00FF000000) >> 24 ;
		int ip2 = (ip & 0x00FF0000) >> 16;
		int ip3 = (ip & 0x0000FF00) >> 8;
		int ip4 = ip & 0x000000FF;
		return ip4 + "." + ip3 + "." + ip2 + "." + ip1;
	}

	public String[] resolutionIP(String ip) {
		if (ip == null) {
			return null;
		}
		return ip.split("\\.");
	}

	public boolean matchAddress(String address) {
		if (address == null) {
			return false;
		}
		String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(address);
		return matcher.matches();
	}

	public int calculatePrefixLength(String submask) {
		String[] strs = submask.split("\\.");
		StringBuilder sb = new StringBuilder();
		for (String str : strs) {
			sb.append(Integer.toBinaryString(Integer.parseInt(str)));
		}
		return sb.toString().indexOf("0");
	}

	private String intToIp(int paramInt) {
		return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
				+ (0xFF & paramInt >> 24);
	}
	
	
}
