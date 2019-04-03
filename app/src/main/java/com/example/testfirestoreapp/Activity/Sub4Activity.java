package com.example.testfirestoreapp.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.KeyListener;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.testfirestoreapp.Data.ImageData;
import com.example.testfirestoreapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Sub4Activity extends AppCompatActivity implements View.OnClickListener {
    CalendarView cg;
    Button btn_camera, btn_gal, btn_save, btn_edit;
    EditText edt_calendar_memo;
    FirebaseAuth fa;
    FirebaseStorage storage;
    ArrayList<ImageData> Imagelist;
    String selectdate = null;
    FirebaseDatabase fd;
    DatabaseReference fdr;
    int imageidx = 0;
    ArrayList<CheckStringData> checkStringlist;
    String resulttext = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub4activity);
        component();
        Calendar calendar = Calendar.getInstance();
        long milliTime = calendar.getTimeInMillis();
        cg.setDate(milliTime, true, true);

    }

    SpannableString cc;

    public void component() {
        Imagelist = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        fa = FirebaseAuth.getInstance();
        fd = FirebaseDatabase.getInstance();
        fdr = fd.getReference(fa.getUid());
        cg = (CalendarView) findViewById(R.id.calandar_view);
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyy-MM-dd", Locale.KOREA);

        cg.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth);
                Toast.makeText(getApplicationContext(), date, Toast.LENGTH_LONG).show();
                selectdate = date;
                Imagelist = new ArrayList<>();
                fdr.child("calendar").child(date).child("schedule").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        edt_calendar_memo.setText("");
                        if (dataSnapshot.getValue() != null) {
                            checkStringlist = new ArrayList<>();
                            imageidx = 0;
                            resulttext = dataSnapshot.getValue(String.class);


                            String ch = "\\[HIDAYZ-IMAGE";
                            String[] chs = resulttext.split(ch);
                            int k = chs.length;
                            for (int i = 0; i < k; i++) {
                                String st = "[HIDAYZ-IMAGE" + String.valueOf(i) + "]";
                                if (resulttext.contains(st)) {
                                    int stt = resulttext.indexOf(st);
                                    int edt = stt + st.length();
                                    CheckStringData chd = new CheckStringData(st, stt, edt);
                                    checkStringlist.add(chd);
                                }
                            }


                            cc = new SpannableString(resulttext);
                            for (int j = 0; j < checkStringlist.size(); j++) {
                                final String checkname = checkStringlist.get(j).getCheckString();
                                final int stt = checkStringlist.get(j).getStartidx();
                                final int edt = checkStringlist.get(j).getEndidx();
                                StorageReference stRef = storage.getReference().child("calendar").child(fa.getCurrentUser().getUid()).child(selectdate).child(checkname + ".jpg");
                                Glide.with(Sub4Activity.this).asBitmap().load(stRef).into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        BitmapDrawable bb = new BitmapDrawable(resource);

                                        ImageData imgd = new ImageData(resource, checkname);
                                        Imagelist.add(imgd);
                                        bb.setBounds(0, 0, bb.getIntrinsicWidth(), bb.getIntrinsicHeight());
                                        ImageSpan span = new ImageSpan(bb, ImageSpan.ALIGN_BASELINE);
                                        cc.setSpan(span, stt, edt, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        edt_calendar_memo.setText(cc);
//                                        edt_calendar_memo.setCompoundDrawables(null,null,bb,null);
                                    }

                                });
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        });

        cg.getDate();
        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_gal = (Button) findViewById(R.id.btn_gal);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        edt_calendar_memo = (EditText) findViewById(R.id.edt_calandar_memo);
        edt_calendar_memo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        btn_edit.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
        btn_gal.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        edt_calendar_memo.setTag(edt_calendar_memo.getKeyListener());
        edt_calendar_memo.setKeyListener(null);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                Toast.makeText(getApplicationContext(), Html.toHtml(edt_calendar_memo.getText()), Toast.LENGTH_LONG).show();

                break;
            case R.id.btn_save:
                updatecalandar(selectdate, edt_calendar_memo.getText().toString());
                for (int i = 0; i < Imagelist.size(); i++) {
                    savebitmapimage(Imagelist.get(i));
                }
                Imagelist = new ArrayList<>();
                imageidx = 0;
                break;
            case R.id.btn_gal:
                loadImagefromGallery();
                break;
            case R.id.btn_edit:
                if (edt_calendar_memo.getKeyListener() != null) {
                    edt_calendar_memo.setTag(edt_calendar_memo.getKeyListener());
                    edt_calendar_memo.setKeyListener(null);
                } else {
                    edt_calendar_memo.setKeyListener((KeyListener) edt_calendar_memo.getTag());
                }
                break;
        }
    }

    public void loadImagefromGallery() {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public void updatecalandar(String date, String memo) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String uid = fa.getUid();
        DatabaseReference dfr = db.getReference(uid);
        dfr.child("calendar").child(date).child("schedule").setValue(memo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("성공", "업로드성공");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("실패", "업로드실패");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = sendPicture(data.getData());
                // bitmap 타입을 drawable로 변경

                ImageData imgd = new ImageData(bitmap, "[HIDAYZ-IMAGE" + String.valueOf(imageidx) +
                        "]");
                Imagelist.add(imgd);
                edt_calendar_memo.append(GetImageSpan(imgd, 0, imgd.getBitmapName().length(), imgd.getBitmapName()));
                imageidx++;
            } else {
                return;
            }
        }
    }

    //텍스트뷰에 이미지나 사진넣는 함수
    //bit=넣을이미지 ,nowtext=현재텍스트,stlength=이미지시작위치
    public SpannableString GetImageSpan(ImageData imageData, int stlength, int edlength, String edittext) {
        Drawable drawable = new BitmapDrawable(imageData.getBitmap());
        //[HIDAYZ.Image]텍스트로 인식되지만 해당텍스트부분이 곧 이미지임
        SpannableString ss = new SpannableString(edittext);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, stlength, edlength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public void savebitmapimage(ImageData imageData) {
        StorageReference storageRef = storage.getReference();
        Bitmap bit = imageData.getBitmap();
        //경로까지 지정
        StorageReference mountainImagesRef = storageRef.child("calendar/" + fa.getUid() + "/" + selectdate + "/" + imageData.getBitmapName() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datad = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(datad);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });

    }

    private Bitmap sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환

        return bitmap;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {

// Matrix 객체 생성
        Matrix matrix = new Matrix();
// 회전 각도 셋팅
        matrix.postRotate(degree);
// 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

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

    public class CheckStringData {
        String CheckString;
        int startidx;
        int endidx;

        public CheckStringData(String checks, int stt, int edx) {
            CheckString = checks;
            startidx = stt;
            endidx = edx;
        }

        public int getEndidx() {
            return endidx;
        }

        public int getStartidx() {
            return startidx;
        }

        public String getCheckString() {
            return CheckString;
        }
    }

}
