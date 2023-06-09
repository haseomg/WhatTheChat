package com.example.whatthechat;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<ChatModel> chatList;
    private SharedPreferences preferences;

    String TAG = "[ChatAdapter]";

    ChatAdapter(Context context, ArrayList<ChatModel> chatList) {
        Log.i(TAG, "constructor (context, arraylist)");
        this.context = context;
        this.chatList = chatList;
    } // constructor END




//    ChatAdapter(Context context, ArrayList arrayList, RecyclerView.Adapter adapter) {
//
//    }

//    ChatAdapter() {
//
//    }

    public void addItem(ChatModel item) {
        Log.i(TAG, "addItem Method");
        if (chatList != null) {
            Log.i(TAG, "chatList != null : " + chatList);
            chatList.add(item);
        } // if END
        else {
            Log.i(TAG, "chatList == null : " + chatList);
        } // else END
    } // addItem Method END

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");
        View view;
        // getItemViewType 에서 뷰타입 1을 리턴받았다면 내 채팅 레이아웃을 받은 Holder를 리턴
        if (viewType == 1) {
            Log.i(TAG, "viewType == 1 : " + viewType);
            view = LayoutInflater.from(context).inflate(R.layout.my_chat_item, parent, false);
            Log.i(TAG, "view check : " + view);
            return new MyHolder(view);
        } // if END
        // getItemViewType 에서 뷰타입 2을 리턴받았다면 상대 채팅 레이아웃을 받은 Holder2를 리턴
        else {
            Log.i(TAG, "viewType != 1 : " + viewType);
            view = LayoutInflater.from(context).inflate(R.layout.your_chat_item, parent, false);
            Log.i(TAG, "view check : " + view);
            return new YourHolder(view);
        } // else END
    } // onCreateViewHolder END

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount Method");
        Log.i(TAG, "chatList.size check : " + chatList.size());
        return chatList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder Method");
        // onCreateViewHolder에서 리턴받은 뷰홀더가 Holder라면 내채팅, item_my_chat의 뷰들을 초기화 해줌
        if (holder instanceof MyHolder) {
            ((MyHolder) holder).chat_Text.setText(chatList.get(position).getScript());
            ((MyHolder) holder).chat_Time.setText(chatList.get(position).getDate_time());
        }
        // onCreateViewHolder에서 리턴받은 뷰홀더가 Holder2라면 상대의 채팅, item_your_chat의 뷰들을 초기화 해줌
        else if (holder instanceof YourHolder) {
            ((YourHolder) holder).chat_You_Image.setImageResource(R.mipmap.ic_launcher);
            ((YourHolder) holder).chat_You_Name.setText(chatList.get(position).getName());
            ((YourHolder) holder).your_chat_Text.setText(chatList.get(position).getScript());
            ((YourHolder) holder).your_chat_Time.setText(chatList.get(position).getDate_time());
        } // else if END
    } // onBindViewHolder END

    // 내가 친 채팅 뷰홀더
    public class MyHolder extends RecyclerView.ViewHolder {
        // 친구목록 모델의 변수들 정의하는부분
        public final TextView chat_Text;
        public final TextView chat_Time;

        public MyHolder(View itemView) {
            super(itemView);
            chat_Text = itemView.findViewById(R.id.chat_Text);
            chat_Time = itemView.findViewById(R.id.chat_Time);
        } // constructor END
    } // MyHolder class END

    // 상대가 친 채팅 뷰홀더
    public class YourHolder extends RecyclerView.ViewHolder {
        // 친구목록 모델의 변수들 정의하는부분
        public final ImageView chat_You_Image;
        public final TextView chat_You_Name;
        public final TextView your_chat_Text;
        public final TextView your_chat_Time;


        public YourHolder(@NonNull View itemView) {
            super(itemView);
            chat_You_Image = itemView.findViewById(R.id.chat_You_Image);
            chat_You_Name = itemView.findViewById(R.id.chat_You_Name);
            your_chat_Text = itemView.findViewById(R.id.your_chat_Text);
            your_chat_Time = itemView.findViewById(R.id.your_chat_Time);
        } // constructor END
    } // YourHolder class END

    @Override
    public int getItemViewType(int position) {
        Log.i(TAG, "getItemViewType Method");
        preferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);

        Log.i(TAG, "shared name check : " + preferences.getString("name", ""));
        // 내 아이디와 arraylist의 name이 같다면 내꺼 아니면 상대꺼
        if (chatList.get(position).name.equals(preferences.getString("name", ""))) {
            Log.i(TAG, "내 아이디 == chatList.get(position).name : " + chatList.get(position).name);
            return 1;
        } else {
            Log.i(TAG, "내 아이디 != chatList.get(position).name : " + chatList.get(position).name);
            return 2;
        } // else END
    } // getItemViewType END

} // ChatAdapter END
