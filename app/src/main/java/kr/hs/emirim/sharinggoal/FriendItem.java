package kr.hs.emirim.sharinggoal;

public class FriendItem {
    String image;
    String friend_uid;
    String friend_name;
    String friend_goal;

    public FriendItem() {
    }

    public FriendItem(String friend_uid) {
        this.friend_uid = friend_uid;
    }

    public FriendItem(String friend_uid, String friend_name) {
        this.friend_uid = friend_uid;
        this.friend_name = friend_name;
    }

    public FriendItem(String friend_uid, String friend_name, String friend_goal){
        this.friend_uid = friend_uid;
        this.friend_name = friend_name;
        this.friend_goal = friend_goal;
    }
    public FriendItem(String friend_uid, String friend_name, String friend_goal,String image) {
        this.friend_uid = friend_uid;
        this.friend_name = friend_name;
        this.friend_goal = friend_goal;
        this.image = image;
    }

    public String getFriend_uid() {
        return friend_uid;
    }

    public String getFriend_name(){
        return friend_name;
    }

    public String getFriend_goal(){
        return friend_goal;
    }

    public String getImage(){
        return image;
    }


    public void setFriend_uid(String friend_uid){
        this.friend_uid = friend_uid;
    }

    public void setFriend_name(String friend_name){
        this.friend_name = friend_name;
    }

    public void setFriend_goal(String friend_goal){
        this.friend_goal = friend_goal;
    }

    public void setImage(String image){
        this.image = image;
    }

}
