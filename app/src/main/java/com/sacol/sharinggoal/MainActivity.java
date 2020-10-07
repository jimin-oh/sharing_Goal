package com.sacol.sharinggoal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
        private TextView t1 ;
        private FloatingActionButton add_btn;
        private Button logout_btn;
        private Button btn_mypage;
        private FirebaseDatabase mDatabase;
        private DatabaseReference mReference;
        private String uid = FirebaseAuth.getInstance().getUid();
        private String name="jiji";
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                startSignupActivity();
            }

            init();
            setup();
        }
        private void init() {
            add_btn = findViewById(R.id.add_btn);
            t1 = findViewById(R.id.t1);
            logout_btn = findViewById(R.id.logout_btn);
            mDatabase = FirebaseDatabase.getInstance();
            btn_mypage = findViewById(R.id.btn_mypage);
            mReference = mDatabase.getReference("users");

        }
        private void setup(){
            add_btn.setOnClickListener(goInputPage);
            logout_btn.setOnClickListener(goSignupPage);
            btn_mypage.setOnClickListener(goMypage);

        }
        View.OnClickListener goMypage = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                starMypageActivity();
            }
        };
        View.OnClickListener goSignupPage = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startSignupActivity();
            }
        };

        View.OnClickListener goInputPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startInputActivity();
            }
        };

        private void startSignupActivity(){
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        }

        private void startInputActivity(){
            Intent intent = new Intent(this, InputActivity.class);
            startActivity(intent);
        }
        private void starMypageActivity(){
            Intent intent = new Intent(this, MypageActivity.class);
            startActivity(intent);
        }


        private void showToast(String str){
            Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();
        }

}