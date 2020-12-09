package kr.hs.emirim.sharinggoal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
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

import java.util.ArrayList;

public class GoalActivity extends AppCompatActivity {

    private ListView listview;
    private HomeAdapter adapter;
    final private String uid = FirebaseAuth.getInstance().getUid();
    private DatabaseReference databaseRefernece;
    private ArrayList<HomeItem> listViewItemList = new ArrayList<HomeItem>();

    private ImageView back_btn;
    private String addrees;
    private String goal;
    private String date;


    private Spinner goal_spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);


        init();
        setup();
    }


    private void init() {
        adapter = new HomeAdapter(listViewItemList);
        listview = findViewById(R.id.goal_list);
        goal_spinner = findViewById(R.id.goal_spinner);
        listview.setAdapter(adapter);
        final String[] curent_goal = {"전체목표", "진행중인 목표", "완료된 목표"};
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, curent_goal);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goal_spinner.setAdapter(spinnerAdapter);
        back_btn = findViewById(R.id.back_btn);

        databaseRefernece = FirebaseDatabase.getInstance().getReference();
    }

    private void setup() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startDetailActivity(position);
            }
        });
        back_btn.setOnClickListener(Activityfinish);
        goal_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        databaseRefernece.child("goalList").child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                listViewItemList.clear();
                                for (DataSnapshot goalDate : snapshot.getChildren()) {
                                    goal = goalDate.child("goal").getValue().toString();
                                    addrees = goalDate.getKey();

                                    if (goalDate.child("date").getValue() != null) {
                                        date = goalDate.child("date").getValue().toString();
                                        listViewItemList.add(new HomeItem(goal, date, addrees));
                                    } else {
                                        listViewItemList.add(new HomeItem(goal, addrees));
                                    }


                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        break;
                    case 1:
                        databaseRefernece.child("goalList").child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                listViewItemList.clear();
                                for (DataSnapshot goalDate : snapshot.getChildren()) {
                                    goal = goalDate.child("goal").getValue().toString();
                                    addrees = goalDate.getKey();

                                    if (goalDate.child("close").getValue() == null) {
                                        if (goalDate.child("date").getValue() != null) {
                                            date = goalDate.child("date").getValue().toString();

                                            listViewItemList.add(new HomeItem(goal, date, addrees));
                                        } else {
                                            listViewItemList.add(new HomeItem(goal, addrees));
                                        }
                                    }


                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        break;
                    case 2:
                        databaseRefernece.child("goalList").child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                listViewItemList.clear();
                                for (DataSnapshot goalDate : snapshot.getChildren()) {
                                    goal = goalDate.child("goal").getValue().toString();
                                    addrees = goalDate.getKey();

                                    if (goalDate.child("close").getValue() != null) {
                                        if (goalDate.child("date").getValue() != null) {
                                            date = goalDate.child("date").getValue().toString();

                                            listViewItemList.add(new HomeItem(goal, date, addrees));
                                        } else {
                                            listViewItemList.add(new HomeItem(goal, addrees));
                                        }
                                    }


                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        break;

                    default:
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    View.OnClickListener Activityfinish = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };


    private void startDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("goal", adapter.getGoal(position).toString());
        intent.putExtra("data", adapter.getAddress(position).toString());
        startActivity(intent);
    }
}
