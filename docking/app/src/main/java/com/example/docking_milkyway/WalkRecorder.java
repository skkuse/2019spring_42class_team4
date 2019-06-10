package com.example.docking_milkyway;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WalkRecorder {
    LocalDateTime starttime = LocalDateTime.now();  //--> Timestamp starttime;
    LocalDateTime endtime;                          //--> Timestamp endtime;
    long elaspetime;                                //--> int elapsetime;

    double distance;                                   //--> int distance;
                                                    //--> int dogssn > get from useracount

    ArrayList<double[]> walkLog = new ArrayList<double[]>();    //--> double[] walklog

    double[] prevGPS = new double[2];
    double[] thisGPS = new double[2];








}
