package com.sacol.sharinggoal;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private DatabaseReference databaseReference;
    private ImageView back_btn;
    private final int GET_GALLERY_IMAGE = 200;
    private EditText name;
    private EditText introduce_line;
    private TextView profile_name_edit;
    private TextView profile_image_edit;
    private TextView profile_Introduce_edit;
    private TextView profile_serch_edit;
    private RadioButton radiotrue;
    private RadioButton radiofalse;
    Uri imguri;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        setUp();

    }

    private void init() {
        profile_image = findViewById(R.id.profile_image);
        back_btn = findViewById(R.id.back_btn);
        name = findViewById(R.id.name);
        introduce_line = findViewById(R.id.introduce_line);
        profile_name_edit = findViewById(R.id.profile_name_edit);
        profile_image_edit = findViewById(R.id.profile_image_edit);
        profile_Introduce_edit = findViewById(R.id.profile_Introduce_edit);
        profile_serch_edit = findViewById(R.id.profile_serch_edit);
        radiotrue = findViewById(R.id.radiotrue);
        radiofalse = findViewById(R.id.radiofalse);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getUid();
    }


    private void setUp() {
        profile_image.setOnClickListener(getImage);
        back_btn.setOnClickListener(finishPage);
        databaseReference.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("userName").getValue().toString());
                if (snapshot.child("profileImg").getValue() != null) {
                    Glide
                            .with(getApplicationContext())
                            .load(snapshot.child("profileImg").getValue())
                            .into(profile_image);
                    profile_image_edit.setText("수정");
                }
                if (snapshot.child("introduce_line").getValue() != null) {
                    introduce_line.setText(snapshot.child("introduce_line").getValue().toString());
                    if (!snapshot.child("introduce_line").getValue().toString().equals("")){
                        profile_Introduce_edit.setText("수정");
                    }
                }
                if (snapshot.child("userSearch").getValue() != null) {
                    if(snapshot.child("userSearch").getValue().toString().equals("true")){
                        radiotrue.setChecked(true);
                    }else{
                        radiofalse.setChecked(true);
                    }
                }else{
                    radiotrue.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profile_name_edit.setOnClickListener(changeName);
        profile_image_edit.setOnClickListener(changeImage);
        profile_Introduce_edit.setOnClickListener(changeIntroduce);
        profile_serch_edit.setOnClickListener(changeSearch);
    }


    View.OnClickListener getImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, GET_GALLERY_IMAGE);

        }
    };

    View.OnClickListener finishPage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    View.OnClickListener changeName = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            databaseReference.child("users").child(uid).child("userName").setValue(name.getText().toString());
            showToast("이름이 변경되었습니다. ");

        }
    };
    View.OnClickListener changeIntroduce = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            databaseReference.child("users").child(uid).child("introduce_line").setValue(introduce_line.getText().toString());
            showToast("한줄소개가 변경되었습니다. ");

        }
    };
    View.OnClickListener changeSearch = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            databaseReference.child("users").child(uid).child("userSearch").setValue(radiotrue.isChecked());

            showToast("변경되었습니다. ");

        }
    };
    View.OnClickListener changeImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            if (imguri != null) {
                final StorageReference imgRef = firebaseStorage.getReference("userImg/" + uid + "Img");
                UploadTask uploadTask = imgRef.putFile(imguri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgRef.getDownloadUrl().addOnSuccessListener(
                                new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        databaseReference.child("users").child(uid).child("profileImg").setValue(uri.toString());
                                        showToast("프로필 사진이 변경되었습니다. ");

                                    }

                                }

                        );
                    }

                });

            } else {
                showToast("프로필 사진을 등록해주세요");
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imguri = data.getData();
            Glide.with(this).load(imguri).into(profile_image);
            profile_image.setImageURI(imguri);
        }

    }


    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }
}
