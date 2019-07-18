package com.example.testfirestoreapp.Activity;

import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testfirestoreapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edt_email, edt_password;
    Button btn_create, btn_login;
    FirebaseAuth mAuth;
    String TAG = "acountset";
    private FirebaseFirestore db;
    CheckBox chk_save_email, chk_auto_login;
    int mAppWidgetId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        FirebaseApp.initializeApp(this);
//        FirebaseInstanceId.getInstance().getToken();
        mAuth = FirebaseAuth.getInstance();
        component();
        Bundle mExtras = getIntent().getExtras();

        if (mExtras != null) {

            mAppWidgetId = mExtras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,

                    AppWidgetManager.INVALID_APPWIDGET_ID);

        }





    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void component() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        FirebaseInstanceId.getInstance().getToken();
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_create = findViewById(R.id.btn_create);
        btn_login = findViewById(R.id.btn_login);
        chk_save_email = findViewById(R.id.chk_save_email);
        chk_auto_login = findViewById(R.id.chk_auto_login);
        btn_login.setOnClickListener(this);
        btn_create.setOnClickListener(this);
        SharedPreferences sf = getSharedPreferences("acount", MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        String email = sf.getString("email", "").trim();
        String password = sf.getString("password", "").trim();
        edt_email.setText(email);
        edt_password.setText(password);
        if (!email.equals("") && password.equals("")) {
            chk_save_email.setChecked(true);
        } else if (!email.equals("") && !password.equals("")) {
            chk_auto_login.setChecked(true);
            new LoginTask(email,password).execute();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:

                android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.acount_dialog, null);
                dialogBuilder.setView(view);

                final android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final Button btn_create_acount = view.findViewById(R.id.btn_acount_create);
                final Button btn_create_cancel = view.findViewById(R.id.btn_create_cancel);
                final EditText edt_create_email = view.findViewById(R.id.edt_create_email);
                final EditText edt_create_password = view.findViewById(R.id.edt_create_password);
                final EditText edt_create_phonenum = view.findViewById(R.id.edt_create_phonenum);
                final EditText edt_create_nickname = view.findViewById(R.id.edt_create_nickname);
                btn_create_acount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String email = edt_create_email.getText().toString().trim();
                        String password = edt_create_password.getText().toString().trim();
                        String phonenum = edt_create_phonenum.getText().toString().trim();
                        String nickname = edt_create_nickname.getText().toString().trim();
                        if (email.equals("")) {
                            toastmessage(getApplicationContext(), getString(R.string.insert_email));
                            return;
                        } else if (password.equals("")) {
                            toastmessage(getApplicationContext(), getString(R.string.insert_password));
                            return;
                        }
                        if (password.length() < 6) {
                            toastmessage(getApplicationContext(), getString(R.string.insert_password_length));
                            return;
                        }
                        create_acount(email, password, nickname, phonenum, alertDialog);

                    }
                });
                btn_create_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
                alertDialog.show();

                break;
            case R.id.btn_login:

                String login_email = edt_email.getText().toString().trim();
                String login_password = edt_password.getText().toString().trim();
                if (edt_email.getText().toString().equals("")) {
                    toastmessage(getApplicationContext(), getString(R.string.insert_email));
                    return;
                } else if (edt_password.getText().toString().equals("")) {
                    toastmessage(getApplicationContext(), getString(R.string.insert_password));
                    return;
                }

//                login(login_email, login_password);
                new LoginTask(login_email, login_password).execute();
                break;
        }
    }

    public void toastmessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void create_acount(final String email, String password, final String nickname, final String phonenum, final AlertDialog dialog) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("성공", "계정생성성공");
                            toastmessage(getApplicationContext(), getString(R.string.join_sucess));
                            String createtime = getTime();
                            String uid = task.getResult().getUser().getUid();
                            add_user_data(email, phonenum, nickname, uid, createtime);
                            dialog.cancel();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("실패", "계정생성실패");
                            toastmessage(getApplicationContext(), getString(R.string.join_fail));
                            if (task.getException().getMessage().toString().contains("The email address is " +
                                    "already in use by another account")) {
                                toastmessage(getApplicationContext(), getString(R.string.email_duplicate));
                            }
                        }

                        // ...
                    }
                });
    }

    public void login(String login_email, String login_password) {
        mAuth.signInWithEmailAndPassword(login_email, login_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("성공", "로그인성공");
                            toastmessage(getApplicationContext(), getString(R.string.login_sucess));
                            String email = edt_email.getText().toString().trim();
                            String password = edt_password.getText().toString();
                            if (chk_save_email.isChecked()) {

                                save_id(email, "");
                            } if (chk_auto_login.isChecked()) {
                                save_id(email, password);
                            }  if (!chk_save_email.isChecked()) {
                                save_id("", "");
                            }
                            String token = FirebaseInstanceId.getInstance().getToken();

                            DocumentReference docRef = db.collection("usertoken").document(email);
                            docRef.update("devicetoken", token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.e("성공","토큰업데이트 성공");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("실패",e.getMessage().toString());
                                }
                            });


                            Intent intent = new Intent(getApplicationContext(), new MainPagerActivity().getClass());
                            startActivityForResult(intent, 1);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("실패", "로그인실패"+task.getException());
                            toastmessage(getApplicationContext(), getString(R.string.login_fail));
                        }

                        // ...
                    }
                });
    }

    public void checklogin(Context context) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            toastmessage(context, "로그인되어있음");
        } else {
            // No user is signed in
            toastmessage(context, "현재 로그아웃된 상태입니다.");
        }

    }

    public void logout() {
        mAuth.getInstance().signOut();
        //        btn_login.setText("login");


    }

    public void add_user_data(String email, String phonenum, String nickname, String uid, String createtime) {
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("phonenum", phonenum);
        user.put("nickname", nickname);
        user.put("createtime", createtime);
        user.put("updatetime", createtime);

        db.collection("userlist").document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("성공", "계정데이터추가 성공");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("실패", "계정데이터추가 실패");
            }
        });
        Map<String, Object> usertoken = new HashMap<>();
        usertoken.put("devicetoken", token);
        usertoken.put("uid", uid);
        db.collection("usertoken").document(email).set(usertoken).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("성공", "토큰데이터추가 성공");
                mAuth.signOut();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("실패", "토큰데이터추가 실패");
            }
        });
    }

    private String getTime() {
        long mNow;
        Date mDate;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    public void setprofiledata() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Jane Q. User")
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    public void save_id(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("acount", MODE_PRIVATE);

        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        String text = edt_email.getText().toString(); // 사용자가 입력한 저장할 데이터
        editor.putString("email", email); // key, value를 이용하여 저장하는 형태
        editor.putString("password", password);
        editor.commit();
    }

    //Asynctask <doinbackgroud변수,progress변수,postexcute변수>
    public class LoginTask extends AsyncTask<Integer, Integer, Boolean> {
        ProgressDialog asyncDialog = new ProgressDialog(LoginActivity.this);
        String email = null;
        String password = null;
        boolean result = false;

        public LoginTask(String em, String pw) {
            this.email = em;
            this.password = pw;
        }

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로그인 중 입니다...");
            asyncDialog.setCancelable(false);
            asyncDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {

            try {
                login(email, password);
                result = true;
            } catch (Exception e) {
                result = false;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
//           super.onPostExecute(aBoolean);

            asyncDialog.dismiss();


//           asyncDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent resultValue = new Intent();

        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

        setResult(RESULT_OK, resultValue);

        finish();
    }
}

