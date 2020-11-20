package com.sacol.sharinggoal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<FriendItem> listViewItemList = new ArrayList<FriendItem>();

    // ListViewAdapter의 생성자
    public FriendAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_freiend, parent, false);
        }

//        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView freiend_name =  convertView.findViewById(R.id.freiend_name);
        TextView freiend_goal =  convertView.findViewById(R.id.freiend_goal);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        FriendItem friendItem = listViewItemList.get(pos);

        freiend_goal.setText(friendItem.getFriend_goal());
        freiend_name.setText(friendItem.getFriend_name());


        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String friend_uid, String friend_name, String friend_goal) {
        FriendItem item = new FriendItem();
        item.setFriend_uid(friend_uid);
        item.setFriend_name(friend_name);
        item.setFriend_goal(friend_goal);

        listViewItemList.add(item);
    }



}

