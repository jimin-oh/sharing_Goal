package com.sacol.sharinggoal;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;



public class DetailActivity extends AppCompatActivity {

    private TextView current_goal ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        init();
        setUp();

    }

    private void init() {
        Intent intent  = getIntent();
        String goal = intent.getExtras().getString("goal");
        current_goal = findViewById(R.id.current_goal);
        current_goal.setText(goal);
    }

    private void setUp() {

    }


}