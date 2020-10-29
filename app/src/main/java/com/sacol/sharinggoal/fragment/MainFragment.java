package com.sacol.sharinggoal.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sacol.sharinggoal.DetailActivity;
import com.sacol.sharinggoal.InputActivity;
import com.sacol.sharinggoal.ListViewAdapter;
import com.sacol.sharinggoal.R;
import com.sacol.sharinggoal.SignupActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters


    private FloatingActionButton add_btn;
    private ListView listview;
    private ListViewAdapter adapter;
    private DatabaseReference databaseRefernece;
    private String data;
    private String goal;
    private String date;


    private String uid;
    private TextView goalCount;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        setup();
        initDatabase();
    }

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startSignupActivity();
        }
    }

    private void init() {
        add_btn = getView().findViewById(R.id.add_btn);
        adapter = new ListViewAdapter();
        listview = getView().findViewById(R.id.listview);
        listview.setAdapter(adapter);
        goalCount = getView().findViewById(R.id.goalCount);
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
                    data = goalDate.getKey();
                    if (String.valueOf(goalDate.getChildrenCount()).equals("2")) {
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

    private void setup() {
        add_btn.setOnClickListener(goInputPage);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("goal", adapter.getGoal(position).toString());
                intent.putExtra("data", adapter.getData(position).toString());
                startActivity(intent);
            }
        });
    }

    View.OnClickListener goInputPage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startInputActivity();
        }
    };


    private void startSignupActivity() {
        Intent intent = new Intent(getContext(), SignupActivity.class);
        startActivity(intent);
    }

    private void startInputActivity() {
        Intent intent = new Intent(getContext(), InputActivity.class);
        startActivity(intent);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void showToast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
    }
}

