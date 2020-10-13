package com.sacol.sharinggoal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton add_btn;
    private ListView listview;
    private ListViewAdapter adapter;
    private DatabaseReference databaseRefernece;
    private String goal;
    private String date;
    private String uid;
    private TextView goalCount;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startSignupActivity();
        }
        init();
        setup();
        initDatabase();
    }

    private void init() {
        add_btn = findViewById(R.id.add_btn);
        adapter = new ListViewAdapter();
        listview = findViewById(R.id.listview);
        listview.setAdapter(adapter);
        goalCount = findViewById(R.id.goalCount);
        userName = findViewById(R.id.user_name);

        uid = FirebaseAuth.getInstance().getUid();
        databaseRefernece = FirebaseDatabase.getInstance().getReference();


    }

    private void initDatabase() {
        databaseRefernece.child("goalList").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                goalCount.setText(String.valueOf(snapshot.getChildrenCount()) + "개의 목표 진행중입니다.");

                for (DataSnapshot goalDate : snapshot.getChildren()) {
                    goal = goalDate.child("goal").getValue().toString();
                    if (String.valueOf(goalDate.getChildrenCount()).equals("2")) {
                        date = goalDate.child("date").getValue().toString();
                        adapter.addItem(goal, date);
                    } else {

                        adapter.addItem(goal);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void setup() {
        add_btn.setOnClickListener(goInputPage);
    }

    View.OnClickListener goInputPage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startInputActivity();
        }
    };

    private void startSignupActivity() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    private void startInputActivity() {
        Intent intent = new Intent(this, InputActivity.class);
        startActivity(intent);
    }

    private void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }

}