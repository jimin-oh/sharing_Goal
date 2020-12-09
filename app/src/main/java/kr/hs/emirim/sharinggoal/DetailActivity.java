package kr.hs.emirim.sharinggoal;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    private List<String> databool;
    private Activity context;


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
        databool = new ArrayList<String>();
        databaseRefernece = FirebaseDatabase.getInstance().getReference();
        transFormat = new SimpleDateFormat("yyyy/MM/dd");
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        context = this;

        databaseRefernece.child("goalList").child(uid).child(data).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("close").getValue()==null ){
                    if (uid.equals(FirebaseAuth.getInstance().getUid())) {

                        final CalendarDay today = CalendarDay.today();

                        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                            @Override
                            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                                int now = Integer.parseInt(String.format("%02d%02d%02d", today.getYear(), today.getMonth() + 1, today.getDay()));
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

                                                    if (date_goal.getKey().equals(clickData)) {
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
                }else {
                    detail_more.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private void initDatabase() {

        databaseRefernece.child("goalList").child(uid).child(data).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    current_date = snapshot.child("current_date").getValue().toString();
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

        databaseRefernece.child("goalList").child(uid).child(data).child("date_goal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot date : snapshot.getChildren()) {

                    for (DataSnapshot date_goal : date.getChildren()) {
                        if (date.getKey().equals("true")) {
                            databool.add(date_goal.getKey().toString());
                        }
                    }
                }
                calendarView.addDecorators(
                        new GoalDecorator(context, databool)
                );
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
        detail_more.setOnClickListener(showDetailMenu);
    }

    View.OnClickListener cancleGoal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            databaseRefernece.child("goalList").child(uid).child(data).child("date_goal").child("false").child(clickData).setValue("false");

        }
    };

    View.OnClickListener checkGoal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            databaseRefernece.child("goalList").child(uid).child(data).child("date_goal").child("true").child(clickData).setValue("true");

        }
    };

    View.OnClickListener showDetailMenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final PopupMenu popup = new PopupMenu(getApplicationContext(), view);
            getMenuInflater().inflate(R.menu.detail_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.close:
                            new AlertDialog.Builder(DetailActivity.this)
                                    .setTitle(Html.fromHtml("<font color='#000000'>목표를 종료 하시겠습니까?</font>"))
                                    .setPositiveButton(Html.fromHtml("<font color='#000000'>아니요</font>"), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(getApplicationContext(), "종료하지 않습니다", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .setNegativeButton(Html.fromHtml("<font color='#000000'>예</font>"), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            databaseRefernece.child("goalList").child(uid).child(data).child("close").setValue("true");

                                        }
                                    }).show();
                            break;
                        case R.id.goal_edit:
//                            startprofileActivity();
                            break;
                        case R.id.delete:
                            new AlertDialog.Builder(DetailActivity.this)
                                    .setTitle(Html.fromHtml("<font color='#000000'>삭제 하시겠습니까?</font>"))
                                    .setPositiveButton(Html.fromHtml("<font color='#000000'>아니요</font>"), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(getApplicationContext(), "삭제하지 않습니다", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .setNegativeButton(Html.fromHtml("<font color='#000000'>예</font>"), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            databaseRefernece.child("goalList").child(uid).child(data).removeValue();
                                        }
                                    }).show();
                            break;
                        default:
                            break;
                    }
                    return false;
                }

            });
            popup.show();
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

    private List<String> databool = new ArrayList<>();

    public GoalDecorator(Activity context, List<String> datebool) {
        this.context = context;
        drawable = context.getResources().getDrawable(R.drawable.calendar_back_item);
        date = CalendarDay.from(calendar);
        this.databool = datebool;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {


        day.copyTo(calendar);
        final String date1 = String.format("%02d%02d%02d", day.getYear(), day.getMonth() + 1, day.getDay());

        for (int i = 0; i < databool.size(); i++) {
            if (databool.get(i).equals(date1)) {
                return true;
            }
        }
        return false;

    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }

}


