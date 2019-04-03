package com.example.testfirestoreapp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.testfirestoreapp.Data.ChatData;
import com.example.testfirestoreapp.Data.RoomData;
import com.example.testfirestoreapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener {
    public static Context context;
    Button btn_add_room;
    ListView room_list;
    ArrayList<RoomData> roomdata = null;
    DatabaseReference databaseReference = null;
    FirebaseDatabase database = null;
    FirebaseAuth fa = null;
    RoomAdapter adapter = null;
    ArrayList<String> uidlist = null;
    public ArrayList<ArrayList<String>> uidlists = null;
    ArrayList<String> tokenlist = null;
    public ArrayList<ArrayList<String>> tokenlists = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_activity);
//        component();
        new LodingTask().execute();

    }

    public void component() {
        context = RoomActivity.this;
        fa = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(fa.getUid());
        btn_add_room = (Button) findViewById(R.id.btn_addroom);
        btn_add_room.setOnClickListener(this);
        room_list = (ListView) findViewById(R.id.room_list);
        roomdata = new ArrayList<RoomData>();
        uidlists = new ArrayList<ArrayList<String>>();
        tokenlists = new ArrayList<ArrayList<String>>();
        databaseReference.child("room").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    String roomname = dataSnapshot.getKey();
                    RoomData roomData = dataSnapshot.getValue(RoomData.class);  // chatData를 가져오고
                    adapter.addroomdata(roomData);
                    uidlist = new ArrayList<String>();
                    tokenlist = new ArrayList<String>();


                    getuidlist(roomData.getRoomName(), roomData.getEmailList());
                    getdevicetoken(roomData.getEmailList());
                }

//                adapter.notifyDataSetChanged();
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
                Log.e("에러", databaseError.getMessage().toString());
            }
        });

       setadapthread.start();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(RoomActivity.this==null){
            int dd =1;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addroom:
                AddroomDialog();

                break;
        }
    }


    public void addroom(String uid, RoomData rd) {
        DatabaseReference dfr = database.getReference(uid);
        dfr.child("room").child(rd.getRoomName()).setValue(rd);
    }

    public class RoomAdapter extends BaseAdapter {
        private Context mcontext;
        private LayoutInflater layoutInflater;
        private ArrayList<RoomData> roomdata;
        private ArrayList<ArrayList<String>> uidlists;
        private ArrayList<ArrayList<String>> tokenlists;

        public RoomAdapter(Context con, ArrayList<RoomData> roomda) {
            this.mcontext = con;
            this.roomdata = roomda;
            layoutInflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return roomdata.size();
        }

        public void setuid(ArrayList<ArrayList<String>> uidl) {
            this.uidlists = uidl;
        }

        public void settoken(ArrayList<ArrayList<String>> tokenl) {
            this.tokenlists = tokenl;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addroomdata(RoomData rr) {
            boolean check = true;
            for (int i = 0; i < this.roomdata.size(); i++) {
                if (this.roomdata.get(i).getRoomName().equals(rr.getRoomName())) {
                    check = false;
                }
            }
            if (check) {
                this.roomdata.add(rr);
                notifyDataSetChanged();
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_room_list, parent, false);
                holder = new ViewHolder();
                holder.iv_room_profile = (ImageView) convertView.findViewById(R.id.iv_room_profile);
                holder.tv_room_text = (TextView) convertView.findViewById(R.id.tv_room_text);
                holder.tv_roomname = (TextView) convertView.findViewById(R.id.tv_roomname);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            room_Glide(fa.getCurrentUser().getEmail(), holder.iv_room_profile);
            holder.tv_roomname.setText(roomdata.get(position).getRoomName());
            holder.tv_room_text.setText(roomdata.get(position).getRoomText());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), new ChatActivity().getClass());
                    intent.putExtra("emaillist", roomdata.get(position).getEmailList());
                    intent.putExtra("roomname", roomdata.get(position).getRoomName());
                    intent.putExtra("uidlist", uidlists.get(position));
                    intent.putExtra("tokenlist", tokenlists.get(position));

                    startActivityForResult(intent, 1);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView iv_room_profile;
            TextView tv_roomname, tv_room_text;
        }
    }

    public void room_Glide(String email, ImageView iv) {
        // Reference to an image file in Cloud Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = storageRef.child("profilepicture/" + email + "/" + "profile.jpg");
// ImageView in your Activity


// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        Glide.with(context).load(islandRef).apply(RequestOptions.circleCropTransform()).into(iv);

    }

    public void getuidlist(final String roomname, final ArrayList<String> emailist) {

        for (int i = 0; i < emailist.size(); i++) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("usertoken").document(emailist.get(i));
            //get으로 가져올시엔 값이들어오는 순서가정렬되서 가져와 지므로 addsnapshotlisttener로 있는그대로 순서유지해서 가져옴
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

                    if (documentSnapshot.exists()) {

                        String dd = documentSnapshot.get("uid").toString();
                        uidlist.add(dd);
                        Log.d("유아이디", roomname + ":" + dd);
                        if (uidlist.size() == emailist.size()) {
                            uidlists.add(uidlist);
                            adapter.setuid(uidlists);
                            uidlist = new ArrayList<String>();
                        }
                    } else {

                    }

                }
            });
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
//
//                                    String dd = task.getResult().get("uid").toString();
//                                    uidlist.add(dd);
//                                    Log.d("유아이디",roomname+":"+dd);
//                                    if (userlist.size() == emailist.size()) {
//                                        uidlists.add(userlist);
//                                        adapter.setuid(uidlists);
//                                        userlist = new ArrayList<String>();
//                                    }
//                                }
//                            } else {
//
//                            }
//
//                        }
//
//
//                    });


        }
    }

    public void getdevicetoken(final ArrayList<String> emaillist) {
        for (int i = 0; i < emaillist.size(); i++) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("usertoken").document(emaillist.get(i));
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {
                        String dd = documentSnapshot.get("devicetoken").toString();
                        tokenlist.add(dd);
                        if (tokenlist.size() == emaillist.size()) {
                            tokenlists.add(tokenlist);
                            adapter.settoken(tokenlists);
                            tokenlist = new ArrayList<String>();
                        }
                    } else {

                    }
                }
            });
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            String dd = task.getResult().get("devicetoken").toString();
//                            tokenlist.add(dd);
//                            if (tokenlist.size() == emaillist.size()) {
//                                tokenlists.add(tokenlist);
//                                adapter.settoken(tokenlists);
//                                tokenlist = new ArrayList<String>();
//                            }
//                        } else {
//
//                        }
//                    }
//                }
//
//            });
        }
    }

    public void AddroomDialog() {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_room_dialog, null);
        dialogBuilder.setView(view);

        final android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btn_addroom = (Button) view.findViewById(R.id.btn_addroom_create);
        Button btn_addroom_cancel = (Button) view.findViewById(R.id.btn_addroom_cancel);
        final EditText edt_addroom_email = (EditText) view.findViewById(R.id.edt_addroom_email);
        final EditText edt_addroom_name = (EditText) view.findViewById(R.id.edt_addroom_name);
        btn_addroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toemail = edt_addroom_email.getText().toString().trim();
                String roomname = edt_addroom_name.getText().toString().trim();

                ArrayList<String> ee = new ArrayList<String>();
                ee.add(fa.getCurrentUser().getEmail());
                ee.add(toemail);
                ArrayList<ChatData> dd = new ArrayList<ChatData>();
                final RoomData rr = new RoomData(ee, roomname, "", dd); //ChatDTO를 이용하여 데이터를 묶는다.


                addroom(fa.getUid(), rr);
                getuidroom(toemail, rr);

                adapter.addroomdata(rr);
                adapter.notifyDataSetChanged();
                alertDialog.cancel();
            }
        });
        btn_addroom_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void getuidroom(String toemail, final RoomData rr) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("usertoken").document(toemail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String dd = task.getResult().get("uid").toString();
                    addroom(dd, rr);
                } else {

                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("uid가져오기 실패", e.getMessage().toString());
            }
        });
    }

    public class LodingTask extends AsyncTask<Integer, Integer, Boolean> {
        ProgressDialog asyncDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isFinishing() && this != null) {
                asyncDialog = new ProgressDialog(RoomActivity.this);
                asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                asyncDialog.setMessage("로딩 중 입니다...");
                asyncDialog.setCancelable(false);
                asyncDialog.show();

            }
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            component();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (asyncDialog.isShowing()) {
                asyncDialog.dismiss();
            }
        }
    }
    Thread setadapthread = new Thread(new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new RoomAdapter(context, roomdata);
                    room_list.setAdapter(adapter);
                }
            });
        }
    });
}
