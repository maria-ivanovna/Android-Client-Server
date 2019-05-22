package com.grudovich.socketapp;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private EditText readText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readText = findViewById(R.id.readtext);

        findViewById(R.id.button).setOnClickListener((v) ->{

            if (!String.valueOf(readText.getText()).isEmpty()){

              Intent intent = new Intent(this, MyService.class);
              intent.putExtra("ip_key", readText.getText().toString());
              startService(intent);

            }
        });
    }
}
