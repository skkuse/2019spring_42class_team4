package com.example.docking_milkyway;

import java.sql.Timestamp;
import java.util.ArrayList;

public class WalkingDB {

    Timestamp starttime;
    Timestamp endtime;
    int elapsetime;
    int dogssn;
    int distance;
    ArrayList<Double> walklog;

    public WalkingDB() { }

    public WalkingDB(Timestamp starttime, Timestamp endtime, int elapsetime, int distance, int dogssn, ArrayList<Double> walklog) {
        this.starttime = starttime;
        this.endtime = endtime;
        this.elapsetime = elapsetime;
        this.distance = distance;
        this.dogssn = dogssn;
        this.walklog = walklog;
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

    public ArrayList<Double> getWalklog() {
        return walklog;
    }
    public void setWalklog(ArrayList<Double> walklog) {
        this.walklog = walklog;
    }
}
