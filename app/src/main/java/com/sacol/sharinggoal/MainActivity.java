package com.sacol.sharinggoal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
        private FloatingActionButton add_btn;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                startSignupActivity();
            }
            init();
            setup();
        ListView listview ;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem(
                "Box", "Account Box Black 36dp") ;
        // 두 번째 아이템 추가.
        adapter.addItem(
                "Circle", "Account Circle Black 36dp") ;
        // 세 번째 아이템 추가.
        }
        private void init() {
            add_btn = findViewById(R.id.add_btn);

        }
        private void setup(){
            add_btn.setOnClickListener(goInputPage);

        }

        private void list(){

        }
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


        private void showToast(String str){
            Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();
        }

}