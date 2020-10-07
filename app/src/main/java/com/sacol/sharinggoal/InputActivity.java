package com.sacol.sharinggoal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class InputActivity extends AppCompatActivity {

    private CheckBox checkDate;
    private LinearLayout datelayout;
    private View view1;
    private View view2;
    private View view3;
    private TextView cancle_btn;
    private EditText input_goal;
    private Button input_btn;
    private DatePicker datapicker;
    private String date;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        init();
        setUp();
    }

    private void init() {
        checkDate = findViewById(R.id.checkDate);
        datelayout = findViewById(R.id.dateLayout);
        view1 = findViewById(R.id.view);
        view2 = findViewById(R.id.view3);
        view3 = findViewById(R.id.view4);
        input_goal = findViewById(R.id.input_goal);
        cancle_btn = findViewById(R.id.cancle_btn);
        input_btn = findViewById(R.id.singup_btn);
        datapicker= findViewById(R.id.dataPicker);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        datapicker.setMinDate(cal.getTimeInMillis());

    }


    private void setUp() {
        checkDate.setOnClickListener(check);
        cancle_btn.setOnClickListener(goMainPage);
        input_btn.setOnClickListener(plusGoal);
        datapicker.init(datapicker.getYear(), datapicker.getMonth(), datapicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,  int dayOfMonth) {
                        date= String.format("%d/%d/%d", year,monthOfYear + 1, dayOfMonth);

                    }
                });
    }

    View.OnClickListener plusGoal = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HashMap<Object,String> goal = new HashMap<>();
            goal.put("goal", input_goal.getText().toString() );
            if (date != null){
                goal.put("date", date);
            }
            mDatabase.child("goalList").child(FirebaseAuth.getInstance().getUid().toString()).push().setValue(goal);
            startMainActivity();
        }
    };
    View.OnClickListener check = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (((CheckBox)v).isChecked()) {
                datelayout.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);

            } else {
                datelayout.setVisibility(View.GONE);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
            }
        }
    };

    View.OnClickListener goMainPage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

