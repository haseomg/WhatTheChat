package com.example.whatthechat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "[Chat]";

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    private EditText chatMsg;
    private Button send;

    private boolean hasConn = false;
    private Socket chatSocket;

    private ArrayList<ChatModel> chatList = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private RecyclerView chat_recyclerView;

    private String getUsername, getRoomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Log.i(TAG, "onCreate");

        initial();
        setChatSocket();
        setSend();
    } // onCreate END

    private void initial() {
        shared = getSharedPreferences("USER", MODE_PRIVATE);
        editor = shared.edit();

        chatAdapter = new ChatAdapter(this, chatList);

        chat_recyclerView = findViewById(R.id.recyclerView);
        chat_recyclerView.setAdapter(chatAdapter);
        chat_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chat_recyclerView.setHasFixedSize(true);

        chatMsg = findViewById(R.id.chatMsg);
        send = findViewById(R.id.sendBtn);

        Intent intent = getIntent();
        getUsername = intent.getStringExtra("username");
        getRoomName = intent.getStringExtra("roomName");
    } // initial Method END

    private void setChatSocket() {
        if (hasConn) {
            return;
        } // if END

        try {
            chatSocket = IO.socket("http://15.164.215.145:3000/");
            chatSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        } // catch END

        chatSocket.on("connect user", onNewUser);
        chatSocket.on("chat message", onNewMessage);

        JSONObject userId = new JSONObject();
        try {
            userId.put("username", shared.getString("name", "") + " Connected");
            userId.put("roomName", "room_example");
            chatSocket.emit("connect user", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        } // catch END

        hasConn = true;

        send.setOnClickListener(v -> sendMessage());
    } // setChatSocket Method END

    private void setSend() {
        send.setOnClickListener(v -> sendMessage());
    } // setSend END

    private void sendMessage() {
        shared = getSharedPreferences("USER", Context.MODE_PRIVATE);
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String getToday = sdf.format(date);

        String message = chatMsg.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        } // if END

        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String getTime = sdfTime.format(date);
        String[] reTime = getTime.split(":");
        String hour = reTime[0];
        String minute = reTime[1];
        int hourToInt = Integer.parseInt(hour);
        int minuteToInt = Integer.parseInt(minute);
        String hourNminute;
        if (hourToInt > 12) {
            hourToInt -= 12;
            String reHour = Integer.toString(hourToInt);
            hourNminute = "오후" + reHour + ": " + minute;
        } else if (hour.equals("00")) {
            hourToInt += 12;
            String reHour = Integer.toString(hourToInt);
            hourNminute = "오전 " + reHour + ": " + minute;
        } else {
            String[] zeroCut = hour.split("0");
            String amHour = zeroCut[1];
            hourNminute = "오전 " + amHour + ": " + minute;
        } // else END

//        ChatModel item = new ChatModel(shared.getString("name", ""), chatMsg.getText().toString(), "example", hourNminute);
//        chatAdapter.addItem(item);
//        chatAdapter.notifyDataSetChanged();

        chatMsg.setText("");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", shared.getString("name", ""));
            jsonObject.put("script", message);
            jsonObject.put("profile_image", "example");
            jsonObject.put("date_time", hourNminute);
            jsonObject.put("roomName", "room_example");
        } catch (JSONException e) {
            e.printStackTrace();
        } // catch END
        chatSocket.emit("chat message", jsonObject);
    } // sendMessage Method END

    private Emitter.Listener onNewMessage = args -> runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        String name;
        String script;
        String profile_image;
        String date_time;

        try {
            name = data.getString("name");
            script = data.getString("script");
            profile_image = data.getString("profile_image");
            date_time = data.getString("date_time");

            ChatModel format = new ChatModel(name, script, profile_image, date_time);
            chatAdapter.addItem(format);
            chatAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        } // catch END
    }); // onNewMessage END

    private Emitter.Listener onNewUser = args -> runOnUiThread(() -> {
        int length = args.length;
        if (length == 0) {
            return;
        } // if END

        String username = args[0].toString();
        try {
            JSONObject object = new JSONObject(username);
            username = object.getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        } // catch END
    }); // onNewUser END

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
        chatSocket.disconnect();
        Log.i(TAG, "chatSocket.disconnect check : " + chatSocket);
    } // onDestroy END
} // ChatActivity Class END