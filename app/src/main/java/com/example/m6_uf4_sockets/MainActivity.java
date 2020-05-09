package com.example.m6_uf4_sockets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText txt_ip;
    private Button btn_status;
    private static final int PORT = 5000;
    private static final String PETICION = "wonder";
    private static final String IPDEFAULT = "54.163.168.66";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_ip = findViewById(R.id.txt_ip);
        btn_status = findViewById(R.id.btn_conect);

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_ip.getText().length() > 0) {
                    askCliente ac = new askCliente();
                    ac.execute(txt_ip.getText().toString(), PETICION);
                } else {
                    askCliente ac = new askCliente();
                    ac.execute(IPDEFAULT, PETICION);
                }
            }
        });
    }

    class askCliente extends AsyncTask <String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String result;
            Socket socket;
            try {
                socket = new Socket(strings[0], PORT);
                BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                PrintStream output = new PrintStream(socket.getOutputStream());
                output.println(strings[1]);
                result = input.readLine();
                socket.close();
            } catch (IOException e) {
                result = "fallo";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("fallo")) {
                btn_status.setBackgroundColor(getResources().getColor(R.color.No_Conection_Server));
            } else {
                btn_status.setBackgroundColor(getResources().getColor(R.color.Server_Conection_OK));
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
