package com.sacol.sharinggoal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendDetailActivity extends AppCompatActivity {

    private ListView listview;
    private HomeAdapter adapter;
    private String uid;
    private DatabaseReference databaseRefernece;
    private LinearLayout go_friend;
    private TextView user_name;

    private String data;
    private String goal;
    private String date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freiend_detail);

        init();
        setup();
        initDatabase();
    }


    private void init() {
        adapter = new HomeAdapter();
        listview = findViewById(R.id.goal_list);
        listview.setAdapter(adapter);
        databaseRefernece = FirebaseDatabase.getInstance().getReference();
        go_friend = findViewById(R.id.go_friend);
        user_name = findViewById(R.id.user_name);
        Intent intent = getIntent();
        uid = intent.getExtras().getString("friendUid");
    }

    private void setup() {
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

        databaseRefernece.child("users").child(uid).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_name.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

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
        intent.putExtra("uid",uid);
        startActivity(intent);
    }

    private void startDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("goal", adapter.getGoal(position).toString());
        intent.putExtra("data", adapter.getAddress(position).toString());
        startActivity(intent);
    }
}
