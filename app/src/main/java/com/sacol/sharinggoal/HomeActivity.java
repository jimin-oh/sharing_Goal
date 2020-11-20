package com.sacol.sharinggoal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private ImageView add_btn;
    private ListView listview;
    private HomeAdapter adapter;
    final private String uid = FirebaseAuth.getInstance().getUid();
    private DatabaseReference databaseRefernece;
    private LinearLayout go_friend;

    private String data;
    private String goal;
    private String date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startSignupActivity();
        }

        init();
        setup();
        initDatabase();
    }


    private void init() {
        add_btn = findViewById(R.id.add_btn);
        adapter = new HomeAdapter();
        listview = findViewById(R.id.goal_list);
        listview.setAdapter(adapter);
        databaseRefernece = FirebaseDatabase.getInstance().getReference();
        go_friend = findViewById(R.id.go_friend);
    }

    private void setup() {
        add_btn.setOnClickListener(goInputPage);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startDetailActivity(position);
            }
        });
        go_friend.setOnClickListener(goFriendPage);
    }

    private void initDatabase() {
        databaseRefernece.child("goalList").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot goalDate : snapshot.getChildren()) {
                    goal = goalDate.child("goal").getValue().toString();
                    data = goalDate.getKey();
                    if (String.valueOf(goalDate.getChildrenCount()).equals("3")) {
                        date = goalDate.child("date").getValue().toString();
                        adapter.addItem(goal, date, data);

                    } else {

                        adapter.addItem(goal, data);

                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }



    View.OnClickListener goInputPage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startInputActivity();
        }
    };

    View.OnClickListener goFriendPage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startFriendActivity();
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

    private void startFriendActivity() {
        Intent intent = new Intent(this, FriendActivity.class);
        startActivity(intent);
    }

    private void startDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("goal", adapter.getGoal(position).toString());
        intent.putExtra("data", adapter.getAddress(position).toString());
        startActivity(intent);
    }
}
