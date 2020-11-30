package kr.hs.emirim.sharinggoal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.HashMap;
import java.util.Map;

public class FriendActivity extends AppCompatActivity {

    private ImageView back_btn;
    private ImageView plus_btn;
    private ListView listview;
    private FriendAdapter adapter;
    final private String uid = FirebaseAuth.getInstance().getUid();
    private DatabaseReference databaseRefernece;


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
        plus_btn.setOnClickListener(plusFriend);
        back_btn.setOnClickListener(Activityfinish);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startFriendDetailActivity(position);
            }
        });
    }

    View.OnClickListener Activityfinish = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    View.OnClickListener plusFriend = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDialog();
        }
    };


    private void initDatabase() {
        databaseRefernece.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = adapter.getCount();
                for (int i = 0; i < count; i++) {
                    adapter.remove(i);
                }
                for (DataSnapshot friendEmail : snapshot.child("friend").getChildren()) {
                    final String email = friendEmail.getValue().toString();
                    databaseRefernece.child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot user : snapshot.getChildren()) {
                                if (user.child("email").getValue().toString().equals(email)) {
                                    final String friend_uid = user.getKey();
                                    String friend_profile = null;

                                    if(user.child("profileImg").getValue()==null){

                                        friend_profile = user.child("profileImg").getValue().toString();
                                    }
                                    final String finalFriend_profile = friend_profile;
                                    databaseRefernece.child("users").child(friend_uid).child("userName").addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            final String friend_name = snapshot.getValue().toString();
                                            databaseRefernece.child("goalList").child(friend_uid).addValueEventListener(new ValueEventListener() {

                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String friend_goal = "진행중인 목표 :  " + String.valueOf(snapshot.getChildrenCount()) + "개";
                                                    if (finalFriend_profile == null) {
                                                        adapter.addItem(friend_uid, friend_name, friend_goal);

                                                    } else {
                                                        adapter.addItem(finalFriend_profile, friend_uid, friend_name, friend_goal);

                                                    }
                                                    adapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }

                                    });

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(FriendActivity.this);
        alert.setTitle("친구 등록");
        alert.setMessage("등록할 친구의 이메일을 작성해주세요");
        final EditText email = new EditText(getApplicationContext());
        email.setHint("이메일을 입력해주세요");
        alert.setView(email);
        alert.setPositiveButton(Html.fromHtml("<font color='#000000'>확인</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                databaseRefernece.child("users").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean i = false;
                        for (DataSnapshot user : snapshot.getChildren()) {
                            if (user.child("email").getValue().toString().equals(email.getText().toString())) {
                                if (user.child("friendSearch").getValue() == null || user.child("friendSearch").getValue().toString().equals("true")) {
                                    i = true;
                                    final String name = user.child("userName").getValue().toString();
                                    new AlertDialog.Builder(FriendActivity.this)
                                            .setTitle(Html.fromHtml("<font color='#000000'>찬구 \'" + user.child("userName").getValue().toString() + "\' 님을 추가하시겠습니까?</font>"))
                                            .setPositiveButton(Html.fromHtml("<font color='#000000'>아니요</font>"), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(getApplicationContext(), "추가하지 않았습니다", Toast.LENGTH_SHORT).show();

                                                }
                                            })
                                            .setNegativeButton(Html.fromHtml("<font color='#000000'>예</font>"), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Map<String, Object> friendEmail = new HashMap<>();
                                                    friendEmail.put(name + "email", email.getText().toString());
                                                    databaseRefernece.child("users").child(uid).child("friend").setValue(friendEmail);

                                                }
                                            }).show();
                                }

                            }
                        }
                        if (!i) {
                            showToast("공개를 원하지 않거나 없는 사용자입니다.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        });

        alert.show();
    }

    private void startFriendDetailActivity(int position) {
        Intent intent = new Intent(this, FriendDetailActivity.class);
        intent.putExtra("friendUid", adapter.getFriendUid(position).toString());
        startActivity(intent);
    }

    private void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }
}
