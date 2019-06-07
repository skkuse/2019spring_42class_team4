package com.example.docking_milkyway;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WalkRecorder {
    LocalDateTime starttime = LocalDateTime.now();
    LocalDateTime endtime;
    long elaspetime;

    int distance;
    double[] prevGPS = new double[2];
    double[] thisGPS = new double[2];

}
