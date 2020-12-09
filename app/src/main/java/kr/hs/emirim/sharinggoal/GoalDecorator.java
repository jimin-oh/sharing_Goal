package kr.hs.emirim.sharinggoal;


import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public  class GoalDecorator implements DayViewDecorator {
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


