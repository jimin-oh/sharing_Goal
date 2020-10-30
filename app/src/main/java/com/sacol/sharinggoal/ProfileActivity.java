package com.sacol.sharinggoal;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private final int GET_GALLERY_IMAGE = 200;
    Uri imguri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        setUp();

    }

    private void init() {
        profile_image = findViewById(R.id.profile_image);
        storageRef = FirebaseStorage.getInstance().getReference();
        storage= FirebaseStorage.getInstance();

    }


    private void setUp() {
        profile_image.setOnClickListener(getImage);
    }


    View.OnClickListener getImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK);

            intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, GET_GALLERY_IMAGE);

        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imguri = data.getData();
            Glide.with(this).load(imguri).into(profile_image);
            profile_image.setImageURI(imguri);

            //1. FirebaseStorage을 관리하는 객체 얻어오기
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

            //2. 업로드할 파일의 node를 참조하는 객체
            //파일 명이 중복되지 않도록 날짜를 이용
            String filename = FirebaseAuth.getInstance().getUid()+ "profile.png";//현재 시간으로 파일명 지정 20191023142634
            //원래 확장자는 파일의 실제 확장자를 얻어와서 사용해야함. 그러려면 이미지의 절대 주소를 구해야함.

            StorageReference imgRef = firebaseStorage.getReference("profileImg/" + filename);
            //uploads라는 폴더가 없으면 자동 생성

            //참조 객체를 통해 이미지 파일 업로드
            // imgRef.putFile(imgUri);
            //업로드 결과를 받고 싶다면..
            UploadTask uploadTask = imgRef.putFile(imguri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    showToast("suceess upload");
                }
            });

        }

    }

    public void clickUpload(View view) {
        //firebase storage에 업로드하기

    }
//    public String getPath(Uri uri){
//        String [] proj ={MediaStore.Images.Media.DATA};
//        CursorLoader cursorLoader = new CursorLoader(this, uri ,proj,null,null,null);
//
//        Cursor cursor = cursorLoader.loadInBackground();
//        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//        cursor.moveToFirst();
//
//        return cursor.getString(index);
//    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }
}
