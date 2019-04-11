package com.example.testfirestoreapp.Activity;

import android.Manifest;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.testfirestoreapp.Constants;
import com.example.testfirestoreapp.InCallback;
import com.example.testfirestoreapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nullable;

public class MainPagerActivity extends TabActivity implements View.OnClickListener, InCallback {
    int PICK_IMAGE_REQUEST = 1;
    FirebaseStorage storage;
    ViewPager viewpager;
    Button btn_first, btn_second, btn_third, btn_four, btn_five, btn_left_menu;
    TabHost tabHost;
    Context context;
    DrawerLayout drawer;
    ListView leftmenu;
    Button btn_inmenu;
    InCallback incall;
    FirebaseAuth fa;
    ImageView iv_profile_picture;
    TextView tv_nickname;
    String[] ddq = {"메뉴1", "메뉴2", "메뉴3", "메뉴4", "메뉴5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_pager);
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)&&
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)&&
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)&&
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE)) {
                    //한번권한설정을 거절했을때 나오는부분
                    new AlertDialog.Builder(this).setTitle("알림").setMessage("앱정보->권한\n권한들을 전부 허용해주셔야 앱을 이용할 수 있습니다.")
                            .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).setNegativeButton("권한 설정", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent appDetail = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                            appDetail.addCategory(Intent.CATEGORY_DEFAULT);
                            appDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(appDetail);
                        }
                    }).setCancelable(false).show();


                } else {
                    //맨처음 설치시 나오거나 다시보지않기선택시 나오는 부분
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET
                                    , Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_NETWORK_STATE},
                            1);
                }
            }
        }
        component();

    }

    public void component() {
        storage = FirebaseStorage.getInstance();
        btn_left_menu = (Button) findViewById(R.id.btn_left_menu);
        btn_left_menu.setOnClickListener(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        leftmenu = (ListView) findViewById(R.id.left_menu);
        ArrayList<MenuData> md = new ArrayList<>();
        boolean df = true;
        for (int i = 0; i < ddq.length; i++) {
            if (i == 0 || i == 2) {
                df = false;
            } else {
                df = true;
            }
            MenuData mm = new MenuData(ddq[i], R.drawable.ic_launcher_foreground, df);
            md.add(mm);
        }

        MenuAdapter mm = new MenuAdapter(getApplicationContext(), md);
        leftmenu.setAdapter(mm);

        btn_inmenu = (Button) findViewById(R.id.btn_inmenu);
        btn_inmenu.setOnClickListener(this);
        iv_profile_picture = (ImageView) findViewById(R.id.iv_profile_picture);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname_left);
        fa = FirebaseAuth.getInstance();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        context = this;
        incall = this;
        firebaseGlide(fa.getCurrentUser().getEmail(), "profile.jpg", iv_profile_picture, context);

        /** TabHost ID */
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef2 = db.collection("userlist").document(fa.getCurrentUser().getUid());
        docRef2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Constants.nickname = documentSnapshot.get("nickname").toString();
                tv_nickname.setText(Constants.nickname);
            }
        });
        /** 새로운 탭을 추가하기 위한 TabSpect */
        TabHost.TabSpec TabSpec = tabHost.newTabSpec("tid1");
        TabHost.TabSpec Tab2Spec = tabHost.newTabSpec("tid2");
        TabHost.TabSpec Tab3Spec = tabHost.newTabSpec("tid3");
        TabHost.TabSpec Tab4Spec = tabHost.newTabSpec("tid4");
        TabHost.TabSpec Tab5Spec = tabHost.newTabSpec("tid5");
        ImageView tab1 = new ImageView(this);
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View bv = li.inflate(R.layout.tabbutton, null);
//        tab1.setImageResource(R.drawable.common_full_open_on_phone);
//        TabSpec.setIndicator("tab1", getResources().getDrawable(R.drawable.common_full_open_on_phone));
        TabSpec.setIndicator(bv);
        TabSpec.setContent(new Intent(this, new MainStartActivity().getClass()));
        Tab2Spec.setIndicator("tab2", getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light));
        Tab2Spec.setContent(new Intent(this, RoomActivity.class));
        Tab3Spec.setIndicator("tab3", getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light));
        Tab3Spec.setContent(new Intent(this, FriendActivity.class));
        Tab4Spec.setIndicator("tab4", getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light));
        Tab4Spec.setContent(new Intent(this, OpenRoomActivity.class));
        Tab5Spec.setIndicator("tab5", getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light));
        Tab5Spec.setContent(new Intent(this, Sub4Activity.class));

        /** 탭을 TabHost 에 추가한다 */
        tabHost.addTab(TabSpec);
        tabHost.addTab(Tab2Spec);
        tabHost.addTab(Tab3Spec);
        tabHost.addTab(Tab4Spec);
        tabHost.addTab(Tab5Spec);

        // TabHost 에 포함된 Tab의 색깔을 모두 바꾼다, 개별적용
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#534512"));
        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#4E4E9C"));
        tabHost.getTabWidget().getChildAt(2).setBackgroundColor(getResources().getColor(R.color.Orange));
        tabHost.getTabWidget().getChildAt(3).setBackgroundColor(getResources().getColor(R.color.colorAccent));
        tabHost.getTabWidget().getChildAt(4).setBackgroundColor(getResources().getColor(R.color.blue));

        // 탭의 선택
        tabHost.getTabWidget().setCurrentTab(0);
        tabHost.setOnTabChangedListener(dd);


    }

    TabHost.OnTabChangeListener dd = new TabHost.OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
            switch (tabId) {
                case "tid1":

                    break;

            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; ++i) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                        Toast.makeText(this, "권한오류", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left_menu:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_inmenu:
                drawer.closeDrawer(Gravity.LEFT);
                break;
        }
    }

    public void firebaseGlide(String email, String name, ImageView iv, Context ctx) {
        // Reference to an image file in Cloud Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = storageRef.child("profilepicture/" + email + "/" + name);

        Glide.with(ctx).load(islandRef).apply(RequestOptions.circleCropTransform()).into(iv);
    }

    public class MenuAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layoutInflater;
        ArrayList<MenuData> menudatalist;

        public MenuAdapter(Context con, ArrayList<MenuData> menud) {
            this.ctx = con;
            this.menudatalist = menud;
            layoutInflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return menudatalist.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final ViewHolder2 holder2;

            if (menudatalist.get(position).getMenuType()) {
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_left_menu, parent, false);
                    holder = new ViewHolder();
                    holder.iv_menu_icon = (ImageView) convertView.findViewById(R.id.iv_menu_icon);
                    holder.tv_menu_text = (TextView) convertView.findViewById(R.id.tv_menu_text);
                    holder.ll_menu = (LinearLayout) convertView.findViewById(R.id.ll_menu);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                if (menudatalist.get(position).getMenuicon() != 0) {
                    holder.iv_menu_icon.setImageResource(menudatalist.get(position).getMenuicon());
                }
                holder.tv_menu_text.setText(menudatalist.get(position).getMenuname());
                holder.ll_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), menudatalist.get(position).getMenuname() + "입니다", Toast.LENGTH_LONG).show();
                        switch (menudatalist.get(position).getMenuname()){
                            case "메뉴1":
                                break;
                            case "메뉴2":
                                startActivity(new Intent(context,MapActivity.class));
                                break;
                            case "메뉴3":

                                break;
                            case "메뉴4":
                                startActivity(new Intent(context,InsertActivity.class));
                                break;
                            case "메뉴5":
                                break;

                        }
                    }
                });
            } else {
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_left_sub_menu, parent, false);
                    holder2 = new ViewHolder2();
                    holder2.iv_submenu_icon = (ImageView) convertView.findViewById(R.id.iv_submenu_icon);
                    holder2.tv_submenu_text = (TextView) convertView.findViewById(R.id.tv_submenu_text);
                    holder2.ll_submenu = (LinearLayout) convertView.findViewById(R.id.ll_sub_menu);
                    convertView.setTag(holder2);
                } else {
                    holder2 = (ViewHolder2) convertView.getTag();
                }

                if (menudatalist.get(position).getMenuicon() != 0) {
                    holder2.iv_submenu_icon.setImageResource(menudatalist.get(position).getMenuicon());
                }
                holder2.tv_submenu_text.setText(menudatalist.get(position).getMenuname());
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView iv_menu_icon;
            TextView tv_menu_text;
            LinearLayout ll_menu;
        }

        private class ViewHolder2 {
            ImageView iv_submenu_icon;
            TextView tv_submenu_text;
            LinearLayout ll_submenu;
        }
    }

    public class MenuData {
        private String menuname = "";//메뉴이름
        private int menuicon = 0; //메뉴 아이콘
        private boolean menutype = true; //메뉴타입 true:일반메뉴 false:타이틀메뉴

        public MenuData(String menun, int menui, boolean menut) {
            this.menuname = menun;
            this.menuicon = menui;
            this.menutype = menut;
        }

        public int getMenuicon() {
            return menuicon;
        }

        public String getMenuname() {
            return menuname;
        }

        public boolean getMenuType() {
            return menutype;
        }
    }

    //이미지 스토라지에 업로드
    public void pictureupload(Bitmap bitmap, String email, String imagename, Uri uip) {
        // Create a storage reference from our app
        FirebaseStorage st = FirebaseStorage.getInstance();
        StorageReference storageRef = st.getReference();

        //경로없이 버킷 제일밖에 지정
        StorageReference mountainsRef = storageRef.child(imagename);
        //경로까지 지정
        StorageReference mountainImagesRef = storageRef.child("profilepicture/" + email + "/" + imagename);

        Uri file = uip;
        StorageReference riversRef = storageRef.child("profilepicture/" + email + "/" + imagename);


//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("실패", "업로드실패");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("성공", "업로드성공");
            }
        });
    }
    public String nowtime(){
        SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy년MM월dd일 HH시mm분ss초", Locale.KOREA );
        Date currentTime = new Date ( );
        String dTime = formatter.format ( currentTime );

        return dTime;
    }

}
