package com.example.docking_milkyway;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class Peristalsis extends AppCompatActivity {
    boolean isConnected = false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peristalsis);
        intent = new Intent(this.getIntent());

        View animView1 = (View) findViewById(R.id.animView1);
        View animView2 = (View) findViewById(R.id.animView2);
        View animView3 = (View) findViewById(R.id.animView3);

        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotatealpha);
        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotatealpha);
        anim2.setStartTime(-1000);
        anim2.setDuration(6000);
        Animation anim3 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotatealpha);
        anim3.setStartTime(-2000);
        anim3.setDuration(4000);

        animView1.setAnimation(anim1);
        animView2.setAnimation(anim2);
        animView3.setAnimation(anim3);

        Toast.makeText(getApplicationContext(), "스마트밴드 연동을 시도합니다.", Toast.LENGTH_SHORT).show();

        while(!isConnected){
            connect();
        }
    }



    void connect(){
        // do peristalsis work with smart band
        /*
         * connecting
         * code
         * space
         */

        // Successfully connected
        Toast.makeText(getApplicationContext(), "연동 성공", Toast.LENGTH_SHORT).show();
        Log.d("연동시스템","연동 성공");
        isConnected = true;

        if (isConnected){
            // 테스트를 위해 8초 대기화면으로 설정했습니다
            Handler hd = new Handler();
            hd.postDelayed(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "연동에 성공하였습니다. 연동화면을 종료합니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, 8000);
        }
    }
}
