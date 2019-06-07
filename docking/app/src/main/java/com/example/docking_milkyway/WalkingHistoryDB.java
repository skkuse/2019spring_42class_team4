package com.example.docking_milkyway;

import com.google.firebase.Timestamp;

public class WalkingHistoryDB {
    String Dog_SSN;
    int distance;
    int elapsetime;
    Timestamp starttime;
    Timestamp endtime;

    public WalkingHistoryDB() {
    }

    public WalkingHistoryDB(String dog_ssn, int dist, int elap, Timestamp stt, Timestamp end){
        this.Dog_SSN = dog_ssn;
        this.distance = dist;
        this.elapsetime = elap;
        this.starttime = stt;
        this.endtime = end;
    }

    public String getDog_SSN() {
        return Dog_SSN;
    }

    public void setDog_SSN(String dog_SSN) {
        Dog_SSN = dog_SSN;
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
