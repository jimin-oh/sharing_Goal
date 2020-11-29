package kr.hs.emirim.sharinggoal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter {
    private ArrayList<FriendItem> listViewItemList = new ArrayList<FriendItem>();

    public FriendAdapter() {

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
            convertView = inflater.inflate(R.layout.item_freiend, parent, false);
        }

        TextView freiend_name =  convertView.findViewById(R.id.freiend_name);
        TextView freiend_goal =  convertView.findViewById(R.id.freiend_goal);

        FriendItem friendItem = listViewItemList.get(position);

        freiend_goal.setText(friendItem.getFriend_goal());
        freiend_name.setText(friendItem.getFriend_name());


        return convertView;
    }

    public Object getFriendUid(int position) {
        return listViewItemList.get(position).getFriend_uid();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String friend_uid, String friend_name, String friend_goal) {
        FriendItem item = new FriendItem();
        item.setFriend_uid(friend_uid);
        item.setFriend_name(friend_name);
        item.setFriend_goal(friend_goal);

        listViewItemList.add(item);
    }



}

