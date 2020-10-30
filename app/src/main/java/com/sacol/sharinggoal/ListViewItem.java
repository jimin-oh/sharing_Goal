package com.sacol.sharinggoal;

import com.google.firebase.database.DatabaseReference;

public class ListViewItem {
    private String date;
    private String goal;
    private String address;


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

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return this.date;
    }

    public String getGoal() {
        return this.goal;
    }

    public String getAddress() {
        return this.address;
    }
}
