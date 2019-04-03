package com.example.testfirestoreapp.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.testfirestoreapp.Constants;
import com.example.testfirestoreapp.Data.ChatData;
import com.example.testfirestoreapp.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth fa;
    EditText edt_sendtext;
    ArrayAdapter<String> adapter;
    ListView listView;
    Button btn_send;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    String roomname = "";
    ArrayList<String> emaillist;
    ArrayList<String> uidlist;
    ArrayList<String> tokenlist;
    int chattype=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity);
        emaillist = getIntent().getExtras().getStringArrayList("emaillist");
        roomname = getIntent().getStringExtra("roomname");
        uidlist = getIntent().getExtras().getStringArrayList("uidlist");
        tokenlist =getIntent().getExtras().getStringArrayList("tokenlist");
        chattype =getIntent().getIntExtra("chattype",0);
        component();
    }

    public void component() {
        FirebaseApp.initializeApp(this);
        listView = (ListView) findViewById(R.id.listview);
        fa = FirebaseAuth.getInstance();
        edt_sendtext = (EditText) findViewById(R.id.edt_sendtext);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        if(chattype==0) {
            databaseReference = database.getReference(fa.getUid());
//        databaseReference.setValue("Hello, World!");  //message 필드 부분을 재설정함 (초기화)
            //message 필드값을 chatdata 형식으로 불러와 listview에 추가함
            databaseReference.child("room").child(roomname).child("message").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ChatData chatData = dataSnapshot.getValue(ChatData.class);  // chatData를 가져오고
                    adapter.add(chatData.getUserName() + ": " + chatData.getMessage());  // adapter에 추가합니다.
                    listView.setSelection(adapter.getCount() - 1);//스크롤을 맨아래로 내린다
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            databaseReference = database.getReference();
//        databaseReference.setValue("Hello, World!");  //message 필드 부분을 재설정함 (초기화)
            //message 필드값을 chatdata 형식으로 불러와 listview에 추가함
            databaseReference.child("openroom").child(roomname).child("message").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ChatData chatData = dataSnapshot.getValue(ChatData.class);  // chatData를 가져오고
                    adapter.add(chatData.getUserName() + ": " + chatData.getMessage());  // adapter에 추가합니다.
                    listView.setSelection(adapter.getCount() - 1);//스크롤을 맨아래로 내린다
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                final ChatData chat = new ChatData(Constants.nickname, edt_sendtext.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
//                databaseReference.child("room").child(roomname).child("message").child("message").setValue(chat);// 기본 database 하위 message라는 child에 chatData를 list로 만들기

                if(chattype==0) {
                    for (int i = 0; i < emaillist.size(); i++) {
                        if (uidlist.size() == emaillist.size()) {
                            String uid = uidlist.get(i);
                            sendmessgae(roomname, uid, chat, chattype);
                        }
                    }
                }else{
                    sendmessgae(roomname,"", chat, chattype);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            for(int i=0; i<tokenlist.size(); i++) {
                                if(!deviceToken.equals(tokenlist.get(i))) {
                                    sendPushNotification(chat, tokenlist.get(i));
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                }).start();
                ;
                edt_sendtext.setText("");
                break;
        }
    }

    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AIzaSyAG5ES3tjqegWcKLsdmPiLelzOkeHltWm8";

    public void sendmessgae(String roomname, String uid, ChatData chat,int type) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        if(type==0) {
            DatabaseReference dfr = db.getReference(uid);
            dfr.child("room").child(roomname).child("message").push().setValue(chat);
            dfr.child("room").child(roomname).child("roomText").setValue(chat.getMessage());
        }else{
            DatabaseReference dfr = db.getReference();
            dfr.child("openroom").child(roomname).child("message").push().setValue(chat);
            dfr.child("openroom").child(roomname).child("roomText").setValue(chat.getMessage());
        }
    }

    public String sendPushNotification(ChatData chat,String devicetoken)
            throws IOException {


        String message = chat.getMessage();
        String email = chat.getUserName();
        String result = "";
        URL url = new URL(FCM_MESSAGE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        JSONObject info = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put("to", devicetoken.trim());

            info.put("title", email); // Notification title
            info.put("body", message); // Notification
            // body
            json.put("notification", info);
        } catch (JSONException e) {
            Log.e("에러", e.getMessage().toString());
        }
        try {
            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
//                OutputStream wr = conn.getOutputStream();
            wr.write(json.toString());
//                wr.write(info.toString().getBytes("utf-8"));
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            conn.getResponseCode();
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            result = "성공";
        } catch (Exception e) {
            e.printStackTrace();
            result = "실패";
        }
        System.out.println(result + "FCM Notification is sent successfully");

        return result;

    }
}
