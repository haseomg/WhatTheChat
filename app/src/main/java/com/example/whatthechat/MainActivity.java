package com.example.whatthechat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    SharedPreferences shared;
    SharedPreferences.Editor editor;

    EditText name, room;
    Button enter;

    String TAG = "[Main]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");

        initial();
        setEnter();

    } // onCreate END

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    } // onStart END

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    } // onResume END

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    } // onPause END

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    } // onStop END

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    } // onDestroy END



    void initial() {
        name = findViewById(R.id.nameEdit);
        room = findViewById(R.id.roomEdit);
        enter = findViewById(R.id.button);

        shared = getSharedPreferences("USER", MODE_PRIVATE);
        editor = shared.edit();
    }

    void setEnter() {
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "enter.onClick");

                String nameStr = name.getText().toString();
                Log.i(TAG, "nameStr check : " + nameStr);
                String roomStr = room.getText().toString();
                Log.i(TAG, "roomStr check : " + roomStr);

                editor.putString("name", nameStr);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("username", nameStr);
                intent.putExtra("roomName", roomStr);
                startActivity(intent);
            } // onClick END
        }); // setOnClickListener END
    } // setEnter END

} // Main CLASS END