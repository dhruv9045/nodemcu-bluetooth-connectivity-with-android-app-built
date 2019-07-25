package com.example.dksingh.mobileapphomeautomation;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import static app.akexorcist.bluetotohspp.library.BluetoothState.REQUEST_ENABLE_BT;

public class MainActivity extends AppCompatActivity {

    Button Blu,WiFi,Relay1,Relay2,Relay3,Relay4,All,Rst;
    Boolean isClickedDummy1,isClickedDummy2,isRelay1,isRelay2,isRelay3,isRelay4,isAll; // global after the declaration of your class
    static String MQTTHOST = "tcp://****.com:**PORT**";
    static String USERNAME = "*****";
    static String PASSWORD = "******";
    String topicStr = "LED";
    MqttAndroidClient client;


    BluetoothSPP bluetooth;



    final String D10 = "0";
    final String D11 = "1";
    final String D20 = "2";
    final String D21 = "3";
    final String D30 = "4";
    final String D31 = "5";
    final String D40 = "6";
    final String D41 = "7";
   final String D50 = "8";
    final String D51 = "9";
    final String RST = "A";


    // in your onCreate()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isClickedDummy1 = true;
       isClickedDummy2 = true;
        isRelay1 = true;
        isRelay2 = true;
        isRelay3 = true;
        isRelay4 = true;
        isAll =true;


        Blu = findViewById(R.id.bluetooth);
       WiFi = findViewById(R.id.wifi);

        Relay1 = findViewById(R.id.relay1);
        Relay2 = findViewById(R.id.relay2);
        Relay3 = findViewById(R.id.relay3);
        Relay4 = findViewById(R.id.relay4);
        All = findViewById(R.id.all);
        Rst =findViewById(R.id.rst);


       bluetooth = new BluetoothSPP(this);

        if (!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
        }

        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Blu.setText("Connected to " + name);
                Blu.setBackgroundResource(R.drawable.button_view1);

            }

            public void onDeviceDisconnected() {

                Blu.setText("Connection lost");
                Blu.setBackgroundResource(R.drawable.button_view);

            }

            public void onDeviceConnectionFailed() {
                Blu.setText("Unable to connect");
                Blu.setBackgroundResource(R.drawable.button_view);

            }
        });

        Blu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED ) {
                    bluetooth.disconnect();



                }else {

                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });


        WiFi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (isClickedDummy2) {
                        WiFi.setBackgroundResource(R.drawable.button_view1);
                        isClickedDummy2 = false;

                        WiFi.setText("Online");
                        String clientId = MqttClient.generateClientId();
                        client =  new MqttAndroidClient(MainActivity.this, MQTTHOST, clientId);
                        MqttConnectOptions options = new MqttConnectOptions();
                        options.setUserName(USERNAME);
                        options.setPassword(PASSWORD.toCharArray());
                        try {
                            IMqttToken token = client.connect(options);
                            token.setActionCallback(new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    // We are connected

                                    Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                    // Something went wrong e.g. connection timeout or firewall problems

                                    Toast.makeText(MainActivity.this, "Not Connected", Toast.LENGTH_SHORT).show();

                                }
                            });
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Online", Toast.LENGTH_LONG).show();
                    } else {
                        WiFi.setBackgroundResource(R.drawable.button_view);
                        isClickedDummy2 = true;


                        Toast.makeText(getApplicationContext(), "DISABLED", Toast.LENGTH_SHORT).show();
                        WiFi.setText("Offline");
                        Toast.makeText(getApplicationContext(), "Offline", Toast.LENGTH_LONG).show();
                    }
                }

        });


        Relay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRelay1) {
                    if(bluetooth.isBluetoothEnabled()) {

                        bluetooth.send(D10, true);

                    } else{
                    String topic = topicStr;
                    String message = D10;
                    try{
                        client.publish(topic, message.getBytes(),0,false);
                    }catch (MqttException e) {
                        e.printStackTrace();
                    }
                    }
                    Relay1.setText("ON");
                    Toast.makeText(getApplicationContext(), "Relay1 on", Toast.LENGTH_LONG).show();
                    isRelay1 = false;
                } else {
                    if(bluetooth.isBluetoothEnabled()) {

                        bluetooth.send(D11, true);

                    }else{
                    String topic = topicStr;
                    String message = D11;
                    try{
                        client.publish(topic, message.getBytes(),0,false);
                    }catch (MqttException e) {
                        e.printStackTrace();
                    }
                    }
                    Relay1.setText("OFF");
                    Toast.makeText(getApplicationContext(), "Relay1 off", Toast.LENGTH_LONG).show();
                    isRelay1 = true;
                }
                check();
            }
        });
        Relay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRelay2) {
                    if(bluetooth.isBluetoothEnabled()) {

                        bluetooth.send(D20, true);

                    }else{
                    String topic = topicStr;
                    String message = D20;
                    try{
                        client.publish(topic, message.getBytes(),0,false);
                    }catch (MqttException e) {
                        e.printStackTrace();
                    }
                    }
                    Relay2.setText("ON");
                    Toast.makeText(getApplicationContext(), "Relay2 on", Toast.LENGTH_LONG).show();
                    isRelay2 = false;
                } else {
                    if(bluetooth.isBluetoothEnabled()) {

                        bluetooth.send(D21, true);

                    }else{
                    String topic = topicStr;
                    String message = D21;
                    try{
                        client.publish(topic, message.getBytes(),0,false);
                    }catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                    Relay2.setText("OFF");
                    Toast.makeText(getApplicationContext(), "Relay2 off", Toast.LENGTH_LONG).show();
                    isRelay2 = true;
                }
                check();
            }
        });
        Relay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isRelay3) {
                    if(bluetooth.isBluetoothEnabled()) {

                        bluetooth.send(D30, true);


                    }else{
                    String topic = topicStr;
                    String message = D30;
                    try{
                        client.publish(topic, message.getBytes(),0,false);
                    }catch (MqttException e) {
                        e.printStackTrace();
                    }
                    }
                    Relay3.setText("ON");
                    Toast.makeText(getApplicationContext(), "Relay3 on", Toast.LENGTH_LONG).show();
                    isRelay3 = false;
                } else {
                    if(bluetooth.isBluetoothEnabled()) {

                        bluetooth.send(D31, true);


                    }else{
                    String topic = topicStr;
                    String message = D31;
                    try{
                        client.publish(topic, message.getBytes(),0,false);
                    }catch (MqttException e) {
                        e.printStackTrace();
                    }
                    }
                    Relay3.setText("OFF");
                    Toast.makeText(getApplicationContext(), "Relay3 off", Toast.LENGTH_LONG).show();
                    isRelay3 = true;
                }
                check();
            }

        });

        Relay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isRelay4) {
                    if(bluetooth.isBluetoothEnabled()) {
                        bluetooth.send(D40, true);

                    }else{
                    String topic = topicStr;
                    String message = D40;
                    try{
                        client.publish(topic, message.getBytes(),0,false);
                    }catch (MqttException e) {
                        e.printStackTrace();
                    }}
                Relay4.setText("ON");
                Toast.makeText(getApplicationContext(), "Relay4 on", Toast.LENGTH_LONG).show();
                isRelay4 = false;
                } else {
                    if(bluetooth.isBluetoothEnabled()) {

                        bluetooth.send(D41, true);

                    }else{
                    String topic = topicStr;
                    String message = D41;
                    try{
                        client.publish(topic, message.getBytes(),0,false);
                    }catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                    Relay4.setText("OFF");
                    Toast.makeText(getApplicationContext(), "Relay4 off", Toast.LENGTH_LONG).show();
                    isRelay4 = true;
                }
                check();
            }

        });


       All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAll) {
                    if(bluetooth.isBluetoothEnabled()) {

                        bluetooth.send(D50, true);


                    }else {
                        String topic = topicStr;
                        String message = D50;
                        try {
                            client.publish(topic, message.getBytes(), 0, false);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                    isAll = false;
                    isRelay1 = false;
                    isRelay2 = false;
                    isRelay3 = false;
                    isRelay4 = false;
                    Relay1.setText("ON");
                    Relay2.setText("ON");
                    Relay3.setText("ON");
                    Relay4.setText("ON");
                    All.setText("ALL ON");
                    All.setBackgroundResource(R.drawable.button_view1);
                    Toast.makeText(getApplicationContext(), "All ON", Toast.LENGTH_LONG).show();
                } else {
                    if(bluetooth.isBluetoothEnabled()) {

                        bluetooth.send(D51, true);


                    }else {
                        String topic = topicStr;
                        String message = D51;
                        try {
                            client.publish(topic, message.getBytes(), 0, false);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                    isAll = true;
                    isRelay1 = true;
                    isRelay2 = true;
                    isRelay3 = true;
                    isRelay4 = true;
                    Relay1.setText("OFF");
                    Relay2.setText("OFF");
                    Relay3.setText("OFF");
                    Relay4.setText("OFF");
                    Toast.makeText(getApplicationContext(), "All OFF", Toast.LENGTH_LONG).show();
                    All.setBackgroundResource(R.drawable.button_view);
                    All.setText("ALL OFF");
                    }
                }
        });

        Rst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bluetooth.isBluetoothEnabled()) {
                bluetooth.send(RST, true);

            }else{
                String topic = topicStr;
                String message = RST;
                try {
                    client.publish(topic, message.getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
                Toast.makeText(getApplicationContext(), "Reset ", Toast.LENGTH_LONG).show();
            }

        });
    }


    public void onStart() {
        super.onStart();
        if (!bluetooth.isBluetoothEnabled()) {
            bluetooth.enable();
        } else {
            if (!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }

    public void check(){
        if(isRelay3 & isRelay2 & isRelay1 & isRelay4){
            All.setText("ALL OFF");
            All.setBackgroundResource(R.drawable.button_view);
            isAll=true;
        }
        else if(!isRelay3 & !isRelay2& !isRelay1 & !isRelay4 ){
            All.setText("ALL ON");
            All.setBackgroundResource(R.drawable.button_view1);
            isAll=false;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        bluetooth.stopService();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bluetooth.connect(data);
        } else if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetooth.setupService();
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth was not enabled.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
