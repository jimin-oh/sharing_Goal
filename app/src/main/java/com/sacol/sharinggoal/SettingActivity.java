package com.sacol.sharinggoal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class SettingActivity extends AppCompatActivity {
    private Button logout_btn;
    private Button profile_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        setUp();
    }

    private void init() {
        logout_btn = findViewById(R.id.logout_btn);
        profile_btn= findViewById(R.id.profileSetting_btn);

    }


    private void setUp() {
        logout_btn.setOnClickListener(logout);
        profile_btn.setOnClickListener(gotoProfilePage);
    }

    View.OnClickListener logout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            startMainActivity();
        }
    };

    View.OnClickListener gotoProfilePage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startProfileActivity();
        }
    };

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
