package com.example.docking_milkyway;

import java.sql.Timestamp;

public class WalkingDB {

    Timestamp starttime;
    Timestamp endtime;
    int elapsetime;
    int dogssn;
    int distance;

    public WalkingDB() { }

    public WalkingDB(Timestamp starttime, Timestamp endtime, int elapsetime, int distance, int dogssn) {
        this.starttime = starttime;
        this.endtime = endtime;
        this.elapsetime = elapsetime;
        this.distance = distance;
        this.dogssn = dogssn;
    }

    public Timestamp getStarttime() {
        return starttime;
    }
    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    public Timestamp getEndtime() {
        return endtime;
    }
    public void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

    public int getElapsetime() {
        return elapsetime;
    }
    public void setElapsetime(int elapsetime) {
        this.elapsetime = elapsetime;
    }

    public int getDistance() {
        return distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDogssn() {
        return dogssn;
    }
    public void setDogssn(int dogssn) {
        this.dogssn = dogssn;
    }
}
