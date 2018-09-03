package com.ktc.networksdk;

import android.os.Parcel;
import android.os.Parcelable;

public class NetInformation implements Parcelable {

    public static final int TYPE_WIFI = 0;
    public static final int TYPE_ETHERNET = 1;

    public static final int STATE_CONNECTED = 0;
    public static final int STATE_DISCONNECTED = 1;
    public static final int STATE_SAVED = 2;


    public static final int MODE_DHCP = 0;
    public static final int MODE_MANUAL = 1;

    public static final int SECURITY_NONE = 0;
    public static final int SECURITY_WEP = 1;
    public static final int SECURITY_WPA = 2;
    public static final int SECURITY_EAP = 3;

    private int mType = -1;
    private String mDeviceName;
    private int mMode = -1;
    private String mSpeed;
    private String mIP;
    private String mSubnet;
    private String mGateway;
    private String mDNS1;
    private String mDNS2;
    private String mMAC;
    private String mSSID;
    private String mBSSID;
    private int mNetState = -1;
    private int mQuality = -1;
    private int mSecuity;

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        if (type == TYPE_ETHERNET || type == TYPE_WIFI) {
            mType = type;
        } else {
            mType = -1;
        }
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        if (mode == MODE_DHCP || mode == MODE_MANUAL) {
            mMode = mode;
        } else {
            mMode = -1;
        }
    }

    public String getSpeed() {
        return mSpeed;
    }

    public void setSpeed(String speed) {
        mSpeed = speed;
    }

    public String getIP() {
        return mIP;
    }

    public void setIP(String IP) {
        mIP = IP;
    }

    public String getSubnet() {
        return mSubnet;
    }

    public void setSubnet(String subnet) {
        mSubnet = subnet;
    }

    public String getGateway() {
        return mGateway;
    }

    public void setGateway(String gateway) {
        mGateway = gateway;
    }

    public String getDNS1() {
        return mDNS1;
    }

    public void setDNS1(String DNS1) {
        mDNS1 = DNS1;
    }

    public String getDNS2() {
        return mDNS2;
    }

    public void setDNS2(String DNS2) {
        mDNS2 = DNS2;
    }

    public String getMAC() {
        return mMAC;
    }

    public void setMAC(String MAC) {
        mMAC = MAC;
    }

    public String getSSID() {
        return mSSID;
    }

    public void setSSID(String SSID) {
        mSSID = SSID;
    }

    public String getBSSID() {
        return mBSSID;
    }

    public void setBSSID(String bssid) {
        mBSSID = bssid;
    }

    public int getNetState() {
        return mNetState;
    }

    public void setNetState(int netState) {
        if (netState == STATE_CONNECTED
                || netState == STATE_DISCONNECTED
                || netState == STATE_SAVED) {
            mNetState = netState;
        } else {
            mNetState = -1;
        }
    }

    public int getQuality() {
        return mQuality;
    }

    public void setQuality(int quality) {
        mQuality = quality;
    }

    public int getSecuity() {
        return mSecuity;
    }

    public void setSecuity(int secuity) {
        if (secuity == SECURITY_NONE
                || secuity == SECURITY_WEP
                || secuity == SECURITY_WPA
                || secuity == SECURITY_EAP) {
            mSecuity = secuity;
        } else {
            secuity = -1;
        }
    }

    public NetInformation() {
    }

    public NetInformation(int type, String deviceName, int mode, String IP,
                          String subnet, String gateway, String DNS1, String DNS2, String MAC) {
        this(type, deviceName, mode, IP, subnet, gateway, DNS1, DNS2, MAC, null, null, null, -1, -1, -1);
    }

    public NetInformation(int type, String deviceName, int mode, String IP, String subnet,
                          String gateway, String DNS1, String DNS2, String MAC, String speed,
                          String SSID, String bssid, int netState, int quality, int secuity) {
        setType(type);
        setDeviceName(deviceName);
        setMode(mode);
        setSpeed(speed);
        setIP(IP);
        setSubnet(subnet);
        setGateway(gateway);
        setDNS1(DNS1);
        setDNS2(DNS2);
        setMAC(MAC);
        setSSID(SSID);
        setBSSID(bssid);
        setNetState(netState);
        setQuality(quality);
        setSecuity(secuity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeString(this.mDeviceName);
        dest.writeInt(this.mMode);
        dest.writeString(this.mSpeed);
        dest.writeString(this.mIP);
        dest.writeString(this.mSubnet);
        dest.writeString(this.mGateway);
        dest.writeString(this.mDNS1);
        dest.writeString(this.mDNS2);
        dest.writeString(this.mMAC);
        dest.writeString(this.mSSID);
        dest.writeString(this.mBSSID);
        dest.writeInt(this.mNetState);
        dest.writeInt(this.mQuality);
        dest.writeInt(this.mSecuity);
    }

    public static final Creator<NetInformation> CREATOR = new Creator() {
        public NetInformation createFromParcel(Parcel in) {
            NetInformation info = new NetInformation();
            info.setType(in.readInt());
            info.setDeviceName(in.readString());
            info.setMode(in.readInt());
            info.setSpeed(in.readString());
            info.setIP(in.readString());
            info.setSubnet(in.readString());
            info.setGateway(in.readString());
            info.setDNS1(in.readString());
            info.setDNS2(in.readString());
            info.setMAC(in.readString());
            info.setSSID(in.readString());
            info.setBSSID(in.readString());
            info.setNetState(in.readInt());
            info.setQuality(in.readInt());
            info.setSecuity(in.readInt());
            return info;
        }

        public NetInformation[] newArray(int size) {
            return new NetInformation[size];
        }
    };


    @Override
    public String toString() {
        return "NetInformation{" +
                "mType=" + mType +
                ", mDeviceName='" + mDeviceName + '\'' +
                ", mMode=" + mMode +
                ", mSpeed='" + mSpeed + '\'' +
                ", mIP='" + mIP + '\'' +
                ", mSubnet='" + mSubnet + '\'' +
                ", mGateway='" + mGateway + '\'' +
                ", mDNS1='" + mDNS1 + '\'' +
                ", mDNS2='" + mDNS2 + '\'' +
                ", mMAC='" + mMAC + '\'' +
                ", mSSID='" + mSSID + '\'' +
                ", mBSSID='" + mBSSID + '\'' +
                ", mNetState=" + mNetState +
                ", mQuality=" + mQuality +
                ", mSecuity='" + mSecuity + '\'' +
                '}';
    }
}
