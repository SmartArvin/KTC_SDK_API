package com.ktc.networksdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.IpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkUtils;
import android.net.RouteInfo;
import android.net.StaticIpConfiguration;
import android.net.EthernetManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

public class NetworkUtil_6_0 {
	private EthernetManager mEthernetManager;
	private WifiManager mWifiManager;
	private static Context mContext;
	private ConnectivityManager connectivityManager;

	NetworkUtil_6_0(Context context) {
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
			if (mEthernetManager == null && mContext != null) {
				mEthernetManager = (EthernetManager) mContext
						.getSystemService(Context.ETHERNET_SERVICE);
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
			if (mWifiManager.isWifiEnabled()) {
				WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
				if (null != wifiInfo
						&& null != wifiInfo.getSSID()
						&& wifiInfo.getNetworkId() != WifiConfiguration.INVALID_NETWORK_ID) {
					{
						String localSsid = wifiInfo.getSSID();
						String netWorkSSID;
						// get wifi name.
						if (localSsid.contains("\"")) {
							netWorkSSID = localSsid.substring(1,
									localSsid.lastIndexOf("\""));
						} else {
							netWorkSSID = wifiInfo.getSSID();
						}

						NetInformation information = new NetInformation();
						WifiConfiguration config = getWifiConfiguredNetwork();
						if (config != null) {
							String ip = null;
							String[] ips = null;
							connectivityManager = (ConnectivityManager) mContext
									.getSystemService(Context.CONNECTIVITY_SERVICE);
							LinkProperties linkProperties = connectivityManager
									.getLinkProperties(ConnectivityManager.TYPE_WIFI);

							WifiInfo info = mWifiManager.getConnectionInfo();
							int intIp = info.getIpAddress();
							ip = getIpAddress(intIp);
							ips = resolutionIP(ip);
							// yih end

							String gateway = null;
							String[] gateways = null;
							// gateway
							if (!(linkProperties.getRoutes() == null)) {
								for (RouteInfo route : linkProperties
										.getRoutes()) {
									if (route.isDefaultRoute()) {
										String tmp = route.getGateway()
												.getHostAddress();

										if (matchAddress(tmp)) {
											gateway = tmp;
											gateways = resolutionIP(gateway);
										}
										break;
									}
								}
							}

							String dns1 = null;
							String dns2 = null;
							// dns1
							Iterator<InetAddress> dnsIterator = linkProperties
									.getDnsServers().iterator();
							if (dnsIterator.hasNext()) {
								String tmp = dnsIterator.next()
										.getHostAddress();

								if (matchAddress(tmp)) {
									dns1 = tmp;
								}
							}
							// dns2
							if (dnsIterator.hasNext()) {
								String tmp = dnsIterator.next()
										.getHostAddress();

								if (matchAddress(tmp)) {
									dns2 = tmp;
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

							String macAddr = null;
							if (null != wifiInfo) {
								macAddr = wifiInfo.getMacAddress();
							}
							information.setBSSID(netWorkSSID);
							information.setIP(ip);
							information.setSubnet(mask);
							information.setGateway(gateway);
							information.setDNS1(dns1);
							information.setDNS2(dns2);
							information.setSpeed(wifiInfo.getLinkSpeed() + "");
							return information;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public NetInformation getEthernetInformation() {
		try {
			if (isEthernetConnected()) {
				String[] ips = null;
				NetInformation information = new NetInformation();
				connectivityManager = (ConnectivityManager) mContext
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				LinkProperties linkProperties = connectivityManager
						.getLinkProperties(ConnectivityManager.TYPE_ETHERNET);
				if (linkProperties == null)
					return null;
				Iterator<LinkAddress> iterator = linkProperties
						.getLinkAddresses().iterator();
				if (iterator.hasNext()) {
					LinkAddress linkAddress = iterator.next();
					information
							.setIP(linkAddress.getAddress().getHostAddress());
					ips = resolutionIP(information.getIP());
				}

				String[] gateways = null;
				// gateway
				if (!(linkProperties.getRoutes() == null)) {
					for (RouteInfo route : linkProperties.getRoutes()) {
						if (route.isDefaultRoute()) {
							information.setGateway(route.getGateway()
									.getHostAddress());
							gateways = resolutionIP(information.getGateway());
							break;
						}
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
				information.setSubnet(mask);

				return information;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setDhcpEthernetConnect() {
		try {
			IpConfiguration ipConfiguration = mEthernetManager
					.getConfiguration();
			if (ipConfiguration == null) {
				ipConfiguration = new IpConfiguration();
			}
			Log.d("lixq", "aaaaaaaaa");
			ipConfiguration.setIpAssignment(IpAssignment.DHCP);
			ipConfiguration.setStaticIpConfiguration(null);
			mEthernetManager.setConfiguration(ipConfiguration);
			Log.d("lixq", "bbbbbbbb");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int setStaticEthernetConnect(String ip, String submask,
			String gateway, String dns1, String dns2) {
		try {
			IpConfiguration ipConfiguration = mEthernetManager
					.getConfiguration();
			if (ipConfiguration == null) {
				ipConfiguration = new IpConfiguration();
			}
			ipConfiguration.setIpAssignment(IpAssignment.STATIC);
			StaticIpConfiguration staticConfig = new StaticIpConfiguration();
			ipConfiguration.setStaticIpConfiguration(staticConfig);

			String ipAddr = ip;
			if (TextUtils.isEmpty(ipAddr))
				return 0;

			Inet4Address inetAddr = null;
			try {
				inetAddr = (Inet4Address) NetworkUtils
						.numericToInetAddress(ipAddr);
			} catch (IllegalArgumentException | ClassCastException e) {
				return 0;
			}

			int networkPrefixLength = -1;
			try {
				networkPrefixLength = calculatePrefixLength(submask);
				if (networkPrefixLength < 0 || networkPrefixLength > 32) {
					return 0;
				}
				staticConfig.ipAddress = new LinkAddress(inetAddr,
						networkPrefixLength);

			} catch (NumberFormatException e) {
				return 0;
			}
			String checkStr = "";
			checkStr = gateway.replace(".", "");
			if (TextUtils.isEmpty(checkStr)) {
				return 0;
			}
			if (!TextUtils.isEmpty(checkStr)) {
				try {
					staticConfig.gateway = (Inet4Address) NetworkUtils
							.numericToInetAddress(gateway);
				} catch (IllegalArgumentException | ClassCastException e) {
					return 0;
				}
			} else {
				gateway = "";
			}

			checkStr = dns1.replace(".", "");
			if (TextUtils.isEmpty(checkStr)) {
				return 0;
			}
			if (!TextUtils.isEmpty(checkStr)) {
				try {
					staticConfig.dnsServers.add((Inet4Address) NetworkUtils
							.numericToInetAddress(dns1));
				} catch (IllegalArgumentException | ClassCastException e) {
					return 0;
				}
			} else {
				dns1 = "";
			}

			checkStr = dns2.replace(".", "");
			if (!TextUtils.isEmpty(checkStr)) {
				try {
					staticConfig.dnsServers.add((Inet4Address) NetworkUtils
							.numericToInetAddress(dns2));
				} catch (IllegalArgumentException | ClassCastException e) {
					return 0;
				}
			} else {
				dns2 = "";
			}
			Log.d("lixq", "111111111");
			mEthernetManager.setConfiguration(ipConfiguration);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getEthConnectType() {
		try {
			if (mEthernetManager.getConfiguration().getIpAssignment() == IpConfiguration.IpAssignment.DHCP) {
				return "DHCP";
			} else if (mEthernetManager.getConfiguration().getIpAssignment() == IpConfiguration.IpAssignment.STATIC) {
				return "STATIC";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getEthMac() {
		// eth mac
		String mac = null;
		try {
			EthernetManager ethernetManager = getEthernetManager();
			mac = ethernetManager.getMacAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mac;
	}

	public String getWifiMac() {
		// wifi mac
		try {
			List<NetworkInterface> all = Collections.list(NetworkInterface
					.getNetworkInterfaces());
			for (NetworkInterface nif : all) {
				if (!nif.getName().equalsIgnoreCase("wlan0"))
					continue;
				byte[] macBytes = nif.getHardwareAddress();
				if (macBytes == null) {
					return "";
				}
				StringBuilder res1 = new StringBuilder();
				for (byte b : macBytes) {
					res1.append(String.format("%02X:", b));
				}
				if (res1.length() > 0) {
					res1.deleteCharAt(res1.length() - 1);
				}
				return res1.toString().toLowerCase();
			}

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

	public boolean isEthernetConnected() {
		if (isEthernetEnabled() && isNetInterfaceAvailable()) {
			return true;
		}
		return false;
	}

	public boolean isEthernetEnabled() {
		if (true == mEthernetManager.isEnabled()) {
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

}
