package kr.hs.emirim.sharinggoal;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Objects;


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
    private LinearLayout goalQ;
    private ImageView check_goal;
    private ImageView cancle_goal;
    private ImageView detail_more;
    private String clickData;
    private View view5;
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
        if (intent.getExtras().getString("friendUid") == null) {
            uid = FirebaseAuth.getInstance().getUid();
        } else {
            uid = intent.getExtras().getString("friendUid");
        }
        current_goal = findViewById(R.id.current_goal);
        current_goal.setText(goal);
        back_btn = findViewById(R.id.back_btn);
        calendarView = findViewById(R.id.calendarView);
        goalQ = findViewById(R.id.goalQ);
        check_goal = findViewById(R.id.check_goal);
        cancle_goal = findViewById(R.id.cancle_goal);
        detail_more = findViewById(R.id.detail_more);
        view5 = findViewById(R.id.view5);
        databaseRefernece = FirebaseDatabase.getInstance().getReference();
        transFormat = new SimpleDateFormat("yyyy/MM/dd");
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        calendarView.addDecorators(
                new GoalDecorator(this,uid,data)
        );
        if (uid == FirebaseAuth.getInstance().getUid()) {
            final CalendarDay today = CalendarDay.today();

            calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                    int now =Integer.parseInt(String.format("%02d%02d%02d", today.getYear(), today.getMonth() + 1, today.getDay()));
                    int select = Integer.parseInt(String.format("%02d%02d%02d", date.getYear(), date.getMonth() + 1, date.getDay()));

                    clickData = String.valueOf(select);
                    if (select > now) {
                        showToast("미래 날짜 입니다.");
                        goalQ.setVisibility(View.GONE);
                    } else {
                        final int[] i = {0};
                        databaseRefernece.child("goalList").child(uid).child(data).child("date_goal").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot date : snapshot.getChildren()) {

                                    for (DataSnapshot date_goal : date.getChildren()) {
                                        if (date_goal.getKey().toString().equals(clickData)) {
                                            i[0] = 1;
                                            goalQ.setVisibility(View.GONE);
                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        if (i[0] == 0) {
                            goalQ.setVisibility(View.VISIBLE);
                        }

                    }
                }
            });
        } else {
            detail_more.setVisibility(View.GONE);
            view5.setVisibility(View.VISIBLE);
        }


    }


    private void initDatabase() {

        databaseRefernece.child("goalList").child(uid).child(data).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    current_date = Objects.requireNonNull(snapshot.child("current_date").getValue()).toString();
                    to = transFormat.parse(current_date);
                    calendarView.state().edit()
                            .setMinimumDate(to)
                            .setCalendarDisplayMode(CalendarMode.MONTHS)
                            .commit();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (snapshot.child("date").getValue() != null) {
                    try {
                        end_date = snapshot.child("date").getValue().toString();
                        to = transFormat.parse(end_date);
                        calendarView.state().edit()
                                .setMaximumDate(to)
                                .setCalendarDisplayMode(CalendarMode.MONTHS)
                                .commit();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

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
        cancle_goal.setOnClickListener(cancleGoal);
        check_goal.setOnClickListener(checkGoal);
    }

    View.OnClickListener cancleGoal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            databaseRefernece.child("goalList").child(uid).child(data).child("date_goal").child("false").child(clickData).setValue("true");

        }
    };

    View.OnClickListener checkGoal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            databaseRefernece.child("goalList").child(uid).child(data).child("date_goal").child("true").child(clickData).setValue("true");

        }
    };

    public void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }

}

class GoalDecorator implements DayViewDecorator {
    private Drawable drawable;
    private final Calendar calendar = Calendar.getInstance();
    private CalendarDay date;
    private Activity context;
    private String uid;
    private String data;
    private DatabaseReference  databaseRefernece = FirebaseDatabase.getInstance().getReference();
    public GoalDecorator(Activity context,String uid,String data) {
        this.context = context;
        drawable = context.getResources().getDrawable(R.drawable.calendar_back_item);
        date = CalendarDay.from(calendar);
        this.uid = uid;
        this.data = data;

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {


        day.copyTo(calendar);
        final String date1 = String.format("%02d%02d%02d", day.getYear(), day.getMonth() + 1, day.getDay());
//        showToast(date1+"20201130");
        final boolean[] n = {false};
        databaseRefernece.child("goalList").child(uid).child(data).child("date_goal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot date : snapshot.getChildren()) {

                    for (DataSnapshot date_goal : date.getChildren()) {
                        if (date_goal.getKey().toString().equals(date1)){
                           showToast("같다");
                           n[0] =true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return n[0];

    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }

    public void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}
