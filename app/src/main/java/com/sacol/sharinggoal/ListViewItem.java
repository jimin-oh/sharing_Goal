package com.sacol.sharinggoal;

public class ListViewItem {
    private String date;
    private String goal;

    public ListViewItem() {
    }
    public ListViewItem(String goal) {
        this.goal = goal;
    }

    public ListViewItem(String date, String goal) {
        this.date = date;
        this.goal = goal;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }


    public String getDate() {
        return this.date;
    }

    public String getGoal() {
        return this.goal;
    }
}
