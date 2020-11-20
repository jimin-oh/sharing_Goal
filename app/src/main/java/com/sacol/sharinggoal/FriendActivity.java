package com.sacol.sharinggoal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendActivity extends AppCompatActivity {

    private ImageView back_btn;
    private ImageView plus_btn;
    private ListView listview;
    private FriendAdapter adapter;
    final private String uid = FirebaseAuth.getInstance().getUid();
    private DatabaseReference databaseRefernece;

    private String friend_name="";
    private String friend_goal="";
    private String friend_uid="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        init();
        setup();
        initDatabase();
    }

    private void init() {
        plus_btn = findViewById(R.id.plus_btn);
        back_btn = findViewById(R.id.back_btn);
        adapter = new FriendAdapter();
        listview = findViewById(R.id.listview);
        listview.setAdapter(adapter);
        databaseRefernece = FirebaseDatabase.getInstance().getReference();
    }

    private void setup() {
//        plus_btn.setOnClickListener();
        back_btn.setOnClickListener(Activityfinish);
    }

    View.OnClickListener Activityfinish = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };


    private void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }
    private void initDatabase() {
        databaseRefernece.child("users").child(uid).child("friend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot friend : snapshot.getChildren()) {
                    friend_uid = friend.getValue().toString();

                    databaseRefernece.child("users").child(friend_uid).child("userName").addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            friend_name = snapshot.getValue().toString();
                            showToast(friend_name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    databaseRefernece.child("goalList").child(friend_uid).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            friend_goal = "진행중인 목표 :  "+String.valueOf(snapshot.getChildrenCount())+"개";
                            showToast(friend_goal);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    adapter.addItem(friend_uid,friend_name,friend_goal);
                }
                adapter.notifyDataSetChanged();
                showToast(String.valueOf(adapter.getCount()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//

    }


}
