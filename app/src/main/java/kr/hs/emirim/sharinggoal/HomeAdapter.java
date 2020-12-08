package kr.hs.emirim.sharinggoal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class HomeAdapter extends BaseAdapter {
    private ArrayList<HomeItem> listViewItemList = new ArrayList<HomeItem>();

    public HomeAdapter( ArrayList<HomeItem> listViewItemList) {
        this.listViewItemList = listViewItemList;
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_goal, parent, false);
        }

        TextView textDate =  convertView.findViewById(R.id.date);
        TextView textGoal =  convertView.findViewById(R.id.goal);

        HomeItem homeItem = listViewItemList.get(position);

        if (homeItem.getDate() != null) {
            textDate.setText(homeItem.getDate());
        }
        textGoal.setText(homeItem.getGoal());


        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Object getGoal(int position) {
        return listViewItemList.get(position).getGoal();
    }

    public Object getAddress(int position) {
        return listViewItemList.get(position).getAddress();
    }

    public void addItem(String goal, String date, String address) {
        HomeItem item = new HomeItem();

        item.setDate(date);
        item.setGoal(goal);
        item.setAddress(address);

        listViewItemList.add(item);

    }

    public void addItem(String goal, String address) {
        HomeItem item = new HomeItem();

        item.setGoal(goal);
        item.setAddress(address);

        listViewItemList.add(item);
    }


}

