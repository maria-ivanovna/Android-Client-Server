package com.grudovich.socketclientapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    EditText edittext, editTextServerIp;
    Button button, connectButton;
    Handler handler = new Handler();
    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edittext = findViewById(R.id.editText);
        editTextServerIp = findViewById(R.id.editTextIp);
        button = findViewById(R.id.send_button);
        connectButton  = findViewById(R.id.connect_button);

        connectButton.setOnClickListener((v)->{
            if (!connected && !editTextServerIp.getText().toString().isEmpty()){
                Thread cThread = new Thread(new ClientThread());
                cThread.start();
            }else{
                Toast.makeText(this, "no ip entered", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ClientThread implements Runnable {

        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(String.valueOf(editTextServerIp.getText()));
                Socket socket = new Socket(serverAddr, 8080);
                handler.post(()->{
                    connectButton.setEnabled(false);
                    connectButton.setText("connected");
                });

                connected = true;
                while (connected) {
                    try {
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);
                        button.setOnClickListener((v)->{
                            out.println(edittext.getText().toString());
                            edittext.setText("");
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                socket.close();
            } catch (Exception e) {
                connected = false;
            }
        }
    }


}
