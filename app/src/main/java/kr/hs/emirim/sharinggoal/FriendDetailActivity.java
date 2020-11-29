package kr.hs.emirim.sharinggoal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    private String friendUid;
    private String uid;
    private DatabaseReference databaseRefernece;
    private TextView user_name;
    private TextView del_friend;

    private String data;
    private String goal;
    private String date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        init();
        setup();
        initDatabase();
    }


    private void init() {
        adapter = new HomeAdapter();
        listview = findViewById(R.id.goal_list);
        listview.setAdapter(adapter);
        databaseRefernece = FirebaseDatabase.getInstance().getReference();
        user_name = findViewById(R.id.user_name);
        del_friend = findViewById(R.id.del_friend);
        Intent intent = getIntent();
        uid = FirebaseAuth.getInstance().getUid();
        friendUid = intent.getExtras().getString("friendUid");
    }

    private void setup() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startDetailActivity(position);
            }
        });
        del_friend.setOnClickListener(deleteFriend);
    }

    private void initDatabase() {
        databaseRefernece.child("goalList").child(friendUid).addValueEventListener(new ValueEventListener() {
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

        databaseRefernece.child("users").child(friendUid).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_name.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    View.OnClickListener deleteFriend = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(FriendDetailActivity.this)
                    .setMessage("친구를 삭제하겠습니까 ?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            databaseRefernece.child("users").child(uid).child("friend").removeValue();
                            finish();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "삭제하지 않습니다", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }
    };

    private void startDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("goal", adapter.getGoal(position).toString());
        intent.putExtra("data", adapter.getAddress(position).toString());
        startActivity(intent);
    }
}
