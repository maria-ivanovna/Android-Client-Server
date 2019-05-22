package com.grudovich.socketapp;

import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MyService extends Service {
    Handler handler;
    ServerSocket serverSocket;
    String ip;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO write your own code

        handler = new Handler();
        ip = intent.getStringExtra("ip_key");
        Thread fst = new Thread(new ServerThread());
        fst.start();

        return Service.START_NOT_STICKY;
    }

    public class ServerThread implements Runnable {

        String serverIp = ip;

        public void run() {
            try {
                if (serverIp != null) {
                    handler.post(()-> Toast.makeText(getApplicationContext(), "слухаємо IP: " + serverIp, Toast.LENGTH_SHORT).show());
                    serverSocket = new ServerSocket(8080);

                    while (true) {
                        Socket client = serverSocket.accept();
                        handler.post(()-> Toast.makeText(getApplicationContext(), "підключено ", Toast.LENGTH_SHORT).show());

                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            String line;
                            while ((line = in.readLine()) != null) {
                                String finalLine = line;
                                handler.post(()-> Toast.makeText(getApplicationContext(), finalLine, Toast.LENGTH_SHORT).show());
                            }
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else handler.post(()-> Toast.makeText(getApplicationContext(), "ip null", Toast.LENGTH_SHORT).show());

            } catch (Exception e) {
                handler.post(()-> Toast.makeText(getApplicationContext(), "помилка", Toast.LENGTH_SHORT).show());

                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}
