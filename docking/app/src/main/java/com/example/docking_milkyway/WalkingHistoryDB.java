package com.example.docking_milkyway;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class WalkingHistoryDB {
    String dogssn;
    int distance;
    int elapsetime;
    Timestamp starttime;
    Timestamp endtime;
    ArrayList<Double> walklog;

    int length;
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }



    public ArrayList<Double> getWalklog() {
        return walklog;
    }

    public void setWalklog(ArrayList<Double> walklog) {
        this.walklog = walklog;
    }


    public WalkingHistoryDB() {
    }

    public WalkingHistoryDB(String dog_ssn, int dist, int elap, Timestamp stt, Timestamp end){
        this.dogssn = dog_ssn;
        this.distance = dist;
        this.elapsetime = elap;
        this.starttime = stt;
        this.endtime = end;
    }

    public String getdogssn() {
        return dogssn;
    }

    public void setdogssn(String dog_SSN) {
        dogssn = dog_SSN;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getElapsetime() {
        return elapsetime;
    }

    public void setElapsetime(int elapsetime) {
        this.elapsetime = elapsetime;
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
}
