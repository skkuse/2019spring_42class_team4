package com.example.docking_milkyway;

public class WalkingDB {

    int starttime;
    int endtime;
    int elapsetime;
    int Dog_SSN;

    public WalkingDB() { }

    public WalkingDB(int starttime, int endtime, int elapsetime, int dog_SSN) {
        this.starttime = starttime;
        this.endtime = endtime;
        this.elapsetime = elapsetime;
        Dog_SSN = dog_SSN;
    }

    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public int getEndtime() {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    public int getElapsetime() {
        return elapsetime;
    }

    public void setElapsetime(int elapsetime) {
        this.elapsetime = elapsetime;
    }

    public int getDog_SSN() {
        return Dog_SSN;
    }

    public void setDog_SSN(int dog_SSN) {
        Dog_SSN = dog_SSN;
    }
}
