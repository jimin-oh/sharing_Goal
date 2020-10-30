package com.sacol.sharinggoal;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private final int GET_GALLERY_IMAGE = 200;

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
            intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, GET_GALLERY_IMAGE);

        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
//            Glide.with(this).load(selectedImageUri).into(profile_image);
            profile_image.setImageURI(selectedImageUri);



        }

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
}
