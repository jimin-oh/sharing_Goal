package com.sacol.sharinggoal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        setUp();

    }

    private void init() {
        profile_image = findViewById(R.id.profile_image);

    }


    private void setUp() {
        profile_image.setOnClickListener(getImage);
    }


    View.OnClickListener getImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
