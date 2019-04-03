package com.example.testfirestoreapp.Activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.example.testfirestoreapp.Data.FriendData;
import com.example.testfirestoreapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lv_friend;
    FirebaseFirestore fs;
    FirebaseAuth fa;
    ArrayList<FriendData> flist;
    FriendAdapter fadapter;
    Button btn_friend_plus;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendactivity);
        component();
    }
    public void component(){
        flist =new ArrayList<>();
        fs=FirebaseFirestore.getInstance();
        fa = FirebaseAuth.getInstance();
        lv_friend =(ListView)findViewById(R.id.lv_friend);
        fs.collection("userfriend").document(fa.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                try {
                    if (documentSnapshot.exists()) {
                        String fname = documentSnapshot.get("friendname").toString();
                        String fmail = documentSnapshot.get("friendemail").toString();
                        FriendData ff = new FriendData(fname, fmail);
                        flist.add(ff);
                        fadapter.setadapter(flist);
                    }
                }catch (NullPointerException ee){

                }
            }
        });

        fadapter = new FriendAdapter(flist,this);
        lv_friend.setAdapter(fadapter);
        btn_friend_plus =findViewById(R.id.btn_plus);
        btn_friend_plus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_plus:
                AddFriendDialog();
                break;
        }
    }

    public class FriendAdapter extends BaseAdapter{
        Context context=null;
        ArrayList<FriendData> fdat ;
        private LayoutInflater layoutInflater;
        public FriendAdapter(ArrayList<FriendData> ff,Context con){
           fdat=ff;
            layoutInflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return fdat.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
        public void addfriend(FriendData fd){
          this.fdat.add(fd);
        }
        public void setadapter(ArrayList<FriendData> fd){
            this.fdat=fd;
            notifyDataSetChanged();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Viewholder holder=null;
            if(convertView==null){
                convertView =layoutInflater.inflate(R.layout.item_friend_list,parent,false);
               holder = new Viewholder();

                holder.iv_friend_profile =(ImageView)convertView.findViewById(R.id.iv_friend_profile);
                holder.tv_friend_name=(TextView) convertView.findViewById(R.id.tv_friend_name);
                convertView.setTag(holder);
            }else{
                convertView.getTag();
            }
            holder.iv_friend_profile.setImageResource(R.drawable.common_full_open_on_phone);
            holder.tv_friend_name.setText(fdat.get(position).getFriendname());
            return convertView;
        }
    }
    private class Viewholder{
        private ImageView iv_friend_profile;
        private TextView tv_friend_name;
    }
    public void AddFriendDialog() {
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
                String friendname = edt_addroom_name.getText().toString().trim();


                final FriendData fd = new FriendData(friendname,toemail); //ChatDTO를 이용하여 데이터를 묶는다.
                fs.collection("userfriend").document(fa.getCurrentUser().getUid()).set(fd).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("성공","성공했소");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("실패","실패");
                    }
                });
                fadapter.addfriend(fd);


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
}
