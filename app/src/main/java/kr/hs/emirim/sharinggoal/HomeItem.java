package kr.hs.emirim.sharinggoal;

import com.google.firebase.database.DatabaseReference;

public class HomeItem {
    private String date;
    private String goal;
    private String address;


    public HomeItem() {
    }

    public HomeItem(String goal) {
        this.goal = goal;
    }

    public HomeItem(String goal , String address) {
        this.address = address;
        this.goal = goal;
    }

    public HomeItem(String goal, String date,String address) {
        this.date = date;
        this.goal = goal;
        this.address = address;
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
