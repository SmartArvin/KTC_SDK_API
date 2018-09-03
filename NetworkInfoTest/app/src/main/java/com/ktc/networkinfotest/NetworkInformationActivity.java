package com.ktc.networkinfotest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.ktc.networksdk.KtcNetworkUtil;
import com.ktc.networksdk.NetInformation;
import com.ktc.opensdk.KtcOpenSdk;
import com.ktc.opensdk.KtcSystemUtil;

public class NetworkInformationActivity extends Activity {
    private final static  String TAG = "NetworkInformation";
    private final int EthernetInformation = 0;
    private TextView tv;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EthernetInformation:
                    Intent intent = new Intent(NetworkInformationActivity.this, NetworkInformationActivity.class);
                    intent.putExtra("networkInformation", 1);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_information);
        tv = findViewById(R.id.tv);
        KtcOpenSdk mKtcOpenSdk = KtcOpenSdk.getInstance(this);
        KtcNetworkUtil mKtcNetworkUtil = mKtcOpenSdk.getKtcNetworkUtil();
        int flag = getIntent().getIntExtra("networkInformation", 0);
        Log.d(TAG, "flag: " + flag);
        NetInformation mNetInformation = null;
        String str = null;
        switch (flag) {
            case 0:
                mNetInformation= mKtcNetworkUtil.getWifiInformation();
                if (mNetInformation != null)
                str = "Wifi information: " + "\n"
                        + "IP: " + mNetInformation.getIP() + "\n"
                        + "Gateway: " + mNetInformation.getGateway() + "\n"
                        + "Subnet: " + mNetInformation.getSubnet() + "\n"
                        + "DNS: " + mNetInformation.getDNS1() + "\n";
                break;
            case 1:
                mNetInformation= mKtcNetworkUtil.getEthernetInformation();
                Log.d(TAG, "mNetInformation: " + mNetInformation);
                if (mNetInformation != null)
                str = "Ethernet information: " + "\n"
                        + "IP: " + mNetInformation.getIP() + "\n"
                        + "Gateway: " + mNetInformation.getGateway() + "\n"
                        + "Subnet: " + mNetInformation.getSubnet() + "\n"
                        + "DNS: " + mNetInformation.getDNS1() + "\n";

                break;
            case 2:
                mKtcNetworkUtil.setDhcpEthernetConnect();
                str = "DHCP ethernet connnect ...";
                mHandler.sendEmptyMessageDelayed(EthernetInformation, 2000);
                finish();
                break;
            case 3:
                String ip = "192.118.1.105";
                String subnet = "255.255.255.0";
                String gateway = "192.118.1.1";
                String dns = "192.168.1.2";
                mKtcNetworkUtil.setStaticEthernetConnect(ip, subnet, gateway, dns, "");
                str = "STATIC ethernet connnect ...";
                break;
            case 4:
                str = "Ethernet Type: " + mKtcNetworkUtil.getEthConnectType();
                break;
            case 5:
                str = "Wifi Mac: " + mKtcNetworkUtil.getWifiMac();
                break;
            case 6:
                str = "Ethernet Mac: " + mKtcNetworkUtil.getEthMac();
                break;
            case 7:
                KtcSystemUtil mKtcSystemUtil = mKtcOpenSdk.getKtcSystemUtil();
                str = "CPU: " + mKtcSystemUtil.getCPUInfo() + "\n"
                    + "GPU: " + mKtcSystemUtil.getGPUInfo() + "\n";
                break;
            default:
                break;

        }

        tv.setText(str);

    }
}
