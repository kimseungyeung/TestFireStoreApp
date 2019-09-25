package com.example.testfirestoreapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.testfirestoreapp.Constants;
import com.example.testfirestoreapp.FireBaseMessagingService;
import com.example.testfirestoreapp.InCallback;
import com.example.testfirestoreapp.R;
import com.example.testfirestoreapp.Service.ScreenService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MainStartActivity extends BaseActivity implements View.OnClickListener {
    public static Context context;
    String TAG = "MainStartActivity";
    int PICK_IMAGE_REQUEST = 1;
    Button btn_logout, btn_login_check, btn_download, btn_send_fcm;
    ImageView imageView;
    TextView tv_user_profile;
    LoginActivity ma;

    private FirebaseFirestore db;
    FirebaseStorage storage;
    ProgressBar pr;
    DocumentSnapshot resultuser;
    FireBaseMessagingService fcm;
    FirebaseAuth mAuth;
    InCallback incall;
    MainPagerActivity mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_start_activity);
        component();

    }


    public void component() {
        Constants.userlogin=true;
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        context = getApplicationContext();
        tv_user_profile = (TextView) findViewById(R.id.tv_user_profile);
        mAuth = FirebaseAuth.getInstance();
        ma = new LoginActivity();
        mp =new MainPagerActivity();
        incall=(InCallback)mp;
        btn_logout = findViewById(R.id.btn_logout);
        btn_login_check = findViewById(R.id.btn_login_check);
        btn_logout.setOnClickListener(this);
        btn_login_check.setOnClickListener(this);
        btn_download = findViewById(R.id.btn_download);
        btn_download.setOnClickListener(this);
        btn_send_fcm = findViewById(R.id.btn_send_fcm);
        btn_send_fcm.setOnClickListener(this);
        imageView = findViewById(R.id.profile_picture);
        pr = findViewById(R.id.pr_downloading);
        fcm = new FireBaseMessagingService();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userdata(user.getUid());

//        firebase_Glide(mAuth.getCurrentUser().getEmail(),"profile.jpg",imageView);
        incall.firebaseGlide(mAuth.getCurrentUser().getEmail(),"profile.jpg",imageView,context);
        Intent intent = new Intent(this, ScreenService.class);

        startService(intent);

    }

    public void test() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            String phone = user.getPhoneNumber();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                ma.logout();
                onBackPressed();
                break;
            case R.id.btn_login_check:
//                ma.checklogin(context);
                loadImagefromGallery();
                break;
            case R.id.btn_download:
                downloacimage("맥주.jpg");
                break;
            case R.id.btn_send_fcm:
//                FcmDlg();

                Toast.makeText(this, Constants.nickname,Toast.LENGTH_LONG).show();
                break;
        }
    }
    //firestore에 있는 userdata를 가져옴
    public void userdata(String uid) {

        DocumentReference docRef = db.collection("userlist").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String dd = task.getResult().get("createtime").toString();
                        int ddd = 1;
                        resultuser = task.getResult();
                        Constants.nickname =task.getResult().get("nickname").toString();
                    }
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        });

    }

    ;

    //갤러리 선택기실행
    public void loadImagefromGallery() {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    public void downloacimage(String name) {
        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child("profilepicture/" + name);

        int check = 0;
        String dd = "";
        File localFile = new File(Environment.getExternalStorageDirectory(), name);
        while (localFile.exists()) {
            check++;
            dd = name.replace(".jpg", "") + "(" + Integer.toString(check) + ")" + ".jpg";
            localFile = new File(Environment.getExternalStorageDirectory(), dd);
        }
        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                // Local temp file has been created
                Log.d("성공", "다운로드 성공");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("실패", "다운로드 실패" + "/" + exception.getMessage().toString());
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred());

                pr.setProgress((int) Math.round(progress));
            }
        });

    }

    public void firebase_Glide(String email,String name ,ImageView iv) {
        // Reference to an image file in Cloud Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = storageRef.child("profilepicture/"+email+"/" + name);
// ImageView in your Activity


// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        Glide.with(context).load(islandRef).apply(RequestOptions.circleCropTransform()).into(iv);
    }

    public void FireBaseDelete(String name) {
// Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

// Create a reference to the file to delete
        StorageReference desertRef = storageRef.child("profilepicture/" + name);

// Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d("성공", "삭제성공" + "/");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d("실패", "삭제 실패" + "/" + exception.getMessage().toString());
            }
        });
    }


    public void FcmDlg() {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.send_fcm_dlg, null);
        dialogBuilder.setView(view);

        final android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Button btn_send_ok = view.findViewById(R.id.btn_send_ok);
        final Button btn_send_cancel = view.findViewById(R.id.btn_send_cancel);
        final EditText edt_send_message = view.findViewById(R.id.edt_send_message);
        token();
        btn_send_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog.cancel();
            }
        });
        btn_send_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }




    public String dtoken;

    public void token() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            // Send token to your backend via HTTPS
                            // ...
                            dtoken = idToken;
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                String name ="profile.jpg";
                incall.pictureupload(scaled,mAuth.getCurrentUser().getEmail(), name,uri);

            } else {
                Toast.makeText(context, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(context, "Oops! 로딩에 오류가 있습니다."+e.getMessage().toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //uri 경로를 절대경로로 변경함
    private String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }
        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = {MediaStore.Files.FileColumns.DATA};
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        }
        return null;
    }


}
