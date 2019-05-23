package com.example.docking_milkyway;

public class FollowDB {

    int following;
    int follower;
    int date;

    public FollowDB() { }

    public FollowDB(int following, int follower, int date) {
        this.following = following;
        this.follower = follower;
        this.date = date;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
