package com.sacol.sharinggoal;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DetailActivity extends AppCompatActivity {

    private TextView current_goal;
    private ImageView back_btn;
    private MaterialCalendarView calendarView;
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
        calendarView.addDecorator(new MySelectorDecorator(this));
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {


            }
        });
    }


    private void initDatabase() {

        databaseRefernece.child("goalList").child(uid).child(data).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                current_date = snapshot.child("current_date").getValue().toString();
                try {
                    to = transFormat.parse(current_date);
                    calendarView.state().edit()
                            .setMinimumDate(to)
                            .setCalendarDisplayMode(CalendarMode.MONTHS)
                            .commit();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (String.valueOf(snapshot.getChildrenCount()).equals("3")) {
                    end_date = snapshot.child("date").getValue().toString();
                    try {
                        to = transFormat.parse(end_date);
                        calendarView.state().edit()
                                .setMaximumDate(to)
                                .setCalendarDisplayMode(CalendarMode.MONTHS)
                                .commit();
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

class MySelectorDecorator implements DayViewDecorator {
    private  Drawable drawable;
    private final Calendar calendar = Calendar.getInstance();
    private CalendarDay date;

    public MySelectorDecorator(Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.bottom_rect);
        date = CalendarDay.from(calendar);

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }


}