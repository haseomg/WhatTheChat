package com.example.whatthechat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import io.socket.client.Socket;

public class ChatActivity extends AppCompatActivity {

    private Socket socket;
    String TAG = "[Chat]";

    SharedPreferences shared;
    SharedPreferences.Editor editor;

    EditText chatMsg;
    Button send;

    ArrayList<ChatModel> chatList = new ArrayList<>();
    ChatAdapter chatAdapter;
    RecyclerView chat_recyclerView;

    String getUsername, getRoomName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initial();
        setSend();

    } // onCreate END

    void initial() {
        shared = getSharedPreferences("USER", MODE_PRIVATE);
        editor = shared.edit();

        // 어댑터 선언
        chatAdapter = new ChatAdapter(ChatActivity.this, chatList);

        // 리사이클러뷰
        chat_recyclerView = findViewById(R.id.recyclerView);
        chat_recyclerView.setAdapter(chatAdapter);

        // 레이아웃 매니저 선언
        LinearLayoutManager lm = new LinearLayoutManager(this);
        chat_recyclerView.setLayoutManager(lm);
        chat_recyclerView.setHasFixedSize(true);

        chatMsg = findViewById(R.id.chatMsg);
        send = findViewById(R.id.sendBtn);

        Intent intent = getIntent();
        getUsername = intent.getStringExtra("username");
        Log.i("getUsername check : ", getUsername);
        getRoomName = intent.getStringExtra("roomName");
        Log.i("getRoomNumber check : ", getRoomName);
    }

    void setSend() {
        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                sendMessage();
            } // onClick END
        }); // setOnClickListener END
    } // setSend END

    @RequiresApi(api = Build.VERSION_CODES.O)
    void sendMessage() {
        Log.i(TAG, "sendMessage Method");
        long now = System.currentTimeMillis();
        Log.i(TAG, "long now check : " + now);
        Date date = new Date(now);
        Log.i(TAG, "date check : " + date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Log.i(TAG, "sdf check : " + sdf);
        String getTime = sdf.format(date);
        Log.i(TAG, "getTime check : " + getTime);

        LocalDate nowDate = LocalDate.now();
        Log.i(TAG, "LocalDate nowDate : " + nowDate);
        int year = nowDate.getYear(); // 연도
        Log.i(TAG, "year check : " + year);
        String month = nowDate.getMonth().toString(); // 몇 월
        Log.i(TAG, "month check : " + month);
        int dayOfMonth = nowDate.getDayOfMonth(); // 몇 일
        Log.i(TAG, "dayOfmonth check : " + dayOfMonth);

        LocalTime nowTime = LocalTime.now();
        Log.i(TAG, "LocalTime nowTime : " + nowTime);
        int hour;
        hour = nowTime.getHour(); // 몇 시
        Log.i(TAG, "hour check : " + hour);
        if (hour < 13) {
            hour = hour + 12;
            Log.i(TAG, "hour < 13 check : " + hour);
        } else if (hour > 12) {
            hour = hour - 12;
            Log.i(TAG, "hour > 12 check : " + hour);
        }
        int minute = nowTime.getMinute(); // 몇 분
        Log.i(TAG, "minute check : " + minute);
        int second = nowTime.getSecond(); // 몇 초
        Log.i(TAG, "second check : " + second);
        String hourStr = Integer.toString(hour);
        Log.i(TAG, "hourStr check : " + hourStr);
        String minuteStr = Integer.toString(minute);
        Log.i(TAG, "minuteStr check : " + minuteStr);
        String hourNminute;
        if (hourStr.length() < 2) {
            hourNminute = hourStr + "시 "  + minuteStr + "분";
            Log.i(TAG, "hourNminute check : " + hourNminute);
        } else {
            hourNminute = hourStr + "시 "  + minuteStr + "분";
        }

        ChatModel item = new ChatModel(shared.getString("name",""), chatMsg.getText().toString(), "example", hourNminute);
        chatAdapter.addItem(item);
        chatAdapter.notifyDataSetChanged();

        chatMsg.setText("");
    } // sendMessage Method END

//    public void init() {
//        try {
//            socket = IO.socket("http://54.180.81.237:3000/");
//            Log.d("SOCKET", "Connection success : " + socket);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//        Intent intent = getIntent();
//        username = intent.getStringExtra("username");
//        roomNumber = intent.getStringExtra("roomNumber");
//
//        socket.connect();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        socket.disconnect();
//    }
}