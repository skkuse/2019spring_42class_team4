package com.example.docking_milkyway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Matching extends AppCompatActivity {
    boolean matchSuccess = false;
    boolean matchAccept = false;
    Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching);
        intent = new Intent(this.getIntent());

    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(getApplicationContext(), "매칭 준비", Toast.LENGTH_SHORT).show();

        while(!matchAccept){
            match();
        }

        // 테스트를 위해 8초 대기화면으로 설정했습니다만 이후 매칭과정을 추가하겠습니다.
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "산책을 시작합니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, 8000);




    }









    void match(){
        // find and match appropriate user

        /*
        * matching
        * code
        * space
         */


        // Successfully matched
        Toast.makeText(getApplicationContext(), "매칭 성공", Toast.LENGTH_SHORT).show();
        Log.d("상아","매칭성공");
        matchSuccess = true;
        matchAccept = true;
/*
        if (matchAccept){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //this.finish();
        }*/


    }



}
