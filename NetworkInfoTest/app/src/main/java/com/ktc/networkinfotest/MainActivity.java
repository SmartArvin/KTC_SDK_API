package com.ktc.networkinfotest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    Button wifi_ip,eth_ip,dhcp_connect,static_connect,eth_connect_type,wifi_mac,eth_mac, cpu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifi_ip = findViewById(R.id.wifi_ip);
        eth_ip = findViewById(R.id.eth_ip);
        dhcp_connect = findViewById(R.id.dhcp_connect);
        static_connect = findViewById(R.id.static_connect);
        eth_connect_type = findViewById(R.id.eth_connect_type);
        wifi_mac = findViewById(R.id.wifi_mac);
        eth_mac = findViewById(R.id.eth_mac);
        cpu = findViewById(R.id.cpu);

        wifi_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NetworkInformationActivity.class);
                intent.putExtra("networkInformation", 0);
                startActivity(intent);
            }
        });

        eth_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NetworkInformationActivity.class);
                intent.putExtra("networkInformation", 1);
                startActivity(intent);
            }
        });

        dhcp_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NetworkInformationActivity.class);
                intent.putExtra("networkInformation", 2);
                startActivity(intent);
            }
        });

        static_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StaticEthernetConnect.class);
                startActivity(intent);
            }
        });

        eth_connect_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NetworkInformationActivity.class);
                intent.putExtra("networkInformation", 4);
                startActivity(intent);
            }
        });

        wifi_mac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NetworkInformationActivity.class);
                intent.putExtra("networkInformation", 5);
                startActivity(intent);
            }
        });

        eth_mac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NetworkInformationActivity.class);
                intent.putExtra("networkInformation", 6);
                startActivity(intent);
            }
        });

        cpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NetworkInformationActivity.class);
                intent.putExtra("networkInformation", 7);
                startActivity(intent);
            }
        });


    }
}
