package com.sacol.sharinggoal;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DetailActivity extends AppCompatActivity {

    private TextView current_goal;
    private ImageView back_btn;
    private CalendarView calendarView;
    private DatabaseReference databaseRefernece;
    private String uid;
    private String data;
    private String goal;
    private String current_date;
    private String end_date;
    private SimpleDateFormat transFormat;
    private Date to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        init();
        setUp();
        initDatabase();

    }

    private void init() {
        Intent intent = getIntent();
        goal = intent.getExtras().getString("goal");
        data = intent.getExtras().getString("data");
        current_goal = findViewById(R.id.current_goal);
        current_goal.setText(goal);
        back_btn = findViewById(R.id.back_btn);
        calendarView = findViewById(R.id.calendarView);
        uid = FirebaseAuth.getInstance().getUid();
        databaseRefernece = FirebaseDatabase.getInstance().getReference();
        transFormat = new SimpleDateFormat("yyyy/MM/dd");

    }

    private void initDatabase() {

        databaseRefernece.child("goalList").child(uid).child(data).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                current_date = snapshot.child("current_date").getValue().toString();
                try {
                    to = transFormat.parse(current_date);
                    calendarView.setMinDate(to.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
//               calendarView.setMinDate(to.getTime());
                if (String.valueOf(snapshot.getChildrenCount()).equals("3")) {
                    end_date = snapshot.child("date").getValue().toString();
                    try {
                        to = transFormat.parse(end_date);
                        calendarView.setMaxDate(to.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void setUp() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }

}