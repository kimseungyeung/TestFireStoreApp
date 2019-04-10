package com.example.testfirestoreapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testfirestoreapp.Constants;
import com.example.testfirestoreapp.Data.ChatData;
import com.example.testfirestoreapp.InCallback;
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
    Button btn_send,btn_picture;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    String roomname = "";
    ArrayList<String> emaillist;
    ArrayList<String> uidlist;
    ArrayList<String> tokenlist;
    int chattype=0;
    int PICK_IMAGE_REQUEST =1;
    InCallback incall;
    ArrayList<ChatData> chatlist;
    MainPagerActivity mp= new MainPagerActivity();
    ChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatactivity);
        emaillist = getIntent().getExtras().getStringArrayList("emaillist");
        roomname = getIntent().getStringExtra("roomname");
        uidlist = getIntent().getExtras().getStringArrayList("uidlist");
        tokenlist =getIntent().getExtras().getStringArrayList("tokenlist");
        chattype =getIntent().getIntExtra("chattype",0);
        component();
    }

    public void component() {
        chatlist = new ArrayList<ChatData>();
        FirebaseApp.initializeApp(this);
        listView = (ListView) findViewById(R.id.listview);

        chatAdapter= new ChatAdapter(chatlist,ChatActivity.this);
        fa = FirebaseAuth.getInstance();
        edt_sendtext = (EditText) findViewById(R.id.edt_sendtext);
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        btn_picture =(Button)findViewById(R.id.btn_send_picture);
        btn_picture.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        incall=(InCallback)mp;

        if(chattype==0) {
            databaseReference = database.getReference(fa.getUid());
//        databaseReference.setValue("Hello, World!");  //message 필드 부분을 재설정함 (초기화)
            //message 필드값을 chatdata 형식으로 불러와 listview에 추가함
            databaseReference.child("room").child(roomname).child("message").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ChatData chatData = dataSnapshot.getValue(ChatData.class);  // chatData를 가져오고
                    chatAdapter.addlist(chatData);  // adapter에 추가합니다.
                   listView.setSelection(chatAdapter.getCount() - 1);//스크롤을 맨아래로 내린다

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
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatData chatData = dataSnapshot.getValue(ChatData.class);  // chatData를 가져오고
                    //adapter.add(chatData.getUserName() + ": " + chatData.getMessage());  // adapter에 추가합니다.
                    chatAdapter.addlist(chatData);
                    listView.setSelection(chatAdapter.getCount() - 1);//스크롤을 맨아래로 내린다
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

        //listView.setAdapter(adapter);
        listView.setAdapter(chatAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String date= incall.nowtime();
                final ChatData chat = new ChatData(Constants.nickname, edt_sendtext.getText().toString(),fa.getCurrentUser().getEmail(),date,""); //ChatDTO를 이용하여 데이터를 묶는다.
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
            case R.id.btn_send_picture:
                loadImagefromGallery();
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
    //갤러리 선택기실행
    public void loadImagefromGallery() {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴
                Uri uri = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
//                File ff = new File(getRealPathFromURI(uri));
////                String name = ff.getName();
                String date =incall.nowtime();
                String name =date+"jpg";


            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다."+e.getMessage().toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public class ChatAdapter extends BaseAdapter{
        ArrayList<ChatData> chatData;
        LayoutInflater layoutInflater;
        Context context;
        public ChatAdapter(ArrayList<ChatData> cd ,Context con){
            this.chatData=cd;
            this.layoutInflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context=con;
        }
        @Override
        public int getCount() {
            return this.chatData.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
        public void setlist(ArrayList<ChatData> cd){
            this.chatData=cd;
        }
        public void addlist (ChatData cdd){
            this.chatData.add(cdd);
            notifyDataSetChanged();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Viewholder holder=null;
            if(convertView==null){
                convertView =layoutInflater.inflate(R.layout.item_chat_text,parent,false);
                holder = new Viewholder();
                holder.iv_chat_profile =(ImageView)convertView.findViewById(R.id.iv_chat_profile);
                holder.iv_chat_picture=(ImageView)convertView.findViewById(R.id.iv_chat_picture);
                holder.tv_chat =(TextView)convertView.findViewById(R.id.tv_chat_text);
                holder.tv_date =(TextView)convertView.findViewById(R.id.tv_chat_date);
                holder.ll_chat_picture=(LinearLayout)convertView.findViewById(R.id.ll_chat_picture);
                holder.ll_chat_text=(LinearLayout)convertView.findViewById(R.id.ll_chat_text);
                holder.tv_nickname =(TextView)convertView.findViewById(R.id.tv_nickname);
                convertView.setTag(holder);
            }else{
               holder= (Viewholder) convertView.getTag();
            }
            if(chatData.size()!=0) {
                //if(chatData.get(position).getEmail()!=null&&!chatData.get(position).getEmail().equals("")) {
                    incall.firebaseGlide(chatData.get(position).getEmail(), "profile.jpg", holder.iv_chat_profile, context);
                //}

                    holder.tv_nickname.setText(chatData.get(position).getUserName());

                holder.tv_chat.setText(chatData.get(position).getMessage());
                if(chatData.get(position).getDate()!=null&&!chatData.get(position).getDate().equals("")) {
                    int k=Integer.parseInt(chatData.get(position).getDate().substring(12, 14));
                    String ss;
                    if(k>12){
                        ss="오후 "+String.valueOf(k-12)+"시 "+chatData.get(position).getDate().substring(15,18);
                    }else{
                        ss="오전 "+String.valueOf(k)+"시 "+chatData.get(position).getDate().substring(15,18);
                    }
                    holder.tv_date.setText(ss);
                }
            }
            return convertView;
        }


        public class Viewholder{
            TextView tv_chat,tv_date,tv_nickname;
            ImageView iv_chat_picture,iv_chat_profile;
            LinearLayout ll_chat_text,ll_chat_picture;
        }
    }


}
