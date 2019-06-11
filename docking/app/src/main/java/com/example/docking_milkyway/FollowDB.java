package com.example.docking_milkyway;

import java.util.ArrayList;

public class FollowDB {

    ArrayList<String>following;
    ArrayList<String> follower;


    public FollowDB() { }

    public FollowDB(ArrayList<String> following, ArrayList<String> follower) {
        this.following = following;
        this.follower = follower;

    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public ArrayList<String> getFollower() {
        return follower;
    }

    public void addFollwing(String user){
        following.add(user);
    }

    public void delFollowing(String user){
        following.remove(user);
    }

    public void addFollower(String user){
        follower.add(user);
    }

    public void delFollower(String user){
        follower.remove(user);
    }

    public void setFollower(ArrayList<String> follower) {
        this.follower = follower;
    }


}
