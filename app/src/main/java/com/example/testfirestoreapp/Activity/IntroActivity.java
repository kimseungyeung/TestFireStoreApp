package com.example.testfirestoreapp.Activity;

import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.testfirestoreapp.Constants;
import com.example.testfirestoreapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

public class IntroActivity extends AppCompatActivity {
        int mAppWidgetId=0;
        FirebaseAuth mAuth;
    String TAG = "acountset";
    private FirebaseFirestore db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);
        SharedPreferences sf = getSharedPreferences("acount", MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        String email = sf.getString("email", "").trim();
        String password = sf.getString("password", "").trim();
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        component();
        if(email!=null&&password!=null&&!email.equals("")&&!password.equals("")){
            new LoginTask(email,password).execute();
        }else{
            Intent intent = new Intent(getApplicationContext(), new LoginActivity().getClass());

            startActivityForResult(intent, 1);
            overridePendingTransition(0,R.anim.fade_out);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void component(){
        FirebaseInstanceId.getInstance().getToken();
        db = FirebaseFirestore.getInstance();
    }
    //Asynctask <doinbackgroud변수,progress변수,postexcute변수>
    public class LoginTask extends AsyncTask<Integer, Integer, Boolean> {
        ProgressDialog asyncDialog = new ProgressDialog(IntroActivity.this);
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

                            String token = FirebaseInstanceId.getInstance().getToken();

                            DocumentReference docRef = db.collection("usertoken").document(login_email);
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
                            overridePendingTransition(0,R.anim.fade_out);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(IntroActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("실패", "로그인실패"+task.getException());
                          // toastmessage(getApplicationContext(), getString(R.string.login_fail));
                        }

                        // ...
                    }
                });
    }

}
