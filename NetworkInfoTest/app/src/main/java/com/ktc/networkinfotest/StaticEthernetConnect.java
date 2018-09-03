package com.ktc.networkinfotest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ktc.networksdk.KtcNetworkUtil;
import com.ktc.opensdk.KtcOpenSdk;



public class StaticEthernetConnect extends Activity {

    private final int EthernetInformation = 0;
    EditText ip, gateway, subnet, dns1, dns2;
    Button submit;
    TextView tv;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EthernetInformation:
                    Intent intent = new Intent(StaticEthernetConnect.this, NetworkInformationActivity.class);
                    intent.putExtra("networkInformation", 1);
                    startActivity(intent);
                    tv.setText("");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_ethernet_connect);
        ip = findViewById(R.id.ip);
        gateway = findViewById(R.id.gateway);
        subnet = findViewById(R.id.subnet);
        dns1 = findViewById(R.id.dns1);
        dns2 = findViewById(R.id.dns2);
        submit = findViewById(R.id.submit);
        tv = findViewById(R.id.tv);
        tv.setText("");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String IP = ip.getText().toString().trim();
                String Gateway = gateway.getText().toString().trim();
                String Subnet = subnet.getText().toString().trim();
                String Dns1 = dns1.getText().toString().trim();
                String Dns2 = dns2.getText().toString().trim();

                KtcOpenSdk mKtcOpenSdk = KtcOpenSdk.getInstance(StaticEthernetConnect.this);
                KtcNetworkUtil mKtcNetworkUtil = mKtcOpenSdk.getKtcNetworkUtil();
                mKtcNetworkUtil.setStaticEthernetConnect(IP, Subnet, Gateway, Dns1, "");
                mHandler.sendEmptyMessageDelayed(EthernetInformation, 5000);
                tv.setText("正在连接.....");
                tv.invalidate();

            }
        });
    }
}
