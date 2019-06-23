package com.example.docking_milkyway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DM extends AppCompatActivity {
    String myID;
    String yourID;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm);
        intent = new Intent(this.getIntent());
        Log.d("상아","start to create DM");

        // Matching 객체에서 정보를 전달받습니다.
        myID = intent.getExtras().getString("myID");
        yourID = intent.getExtras().getString("yourID");
        Log.d("상아","myID : "+myID+" / yourID : "+yourID);


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.chatlog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView chat1 = new TextView(this);
        chat1.setText("first message");
        chat1.setLayoutParams(params);
        chat1.setGravity(Gravity.LEFT);
        linearLayout.addView(chat1);

        TextView chat2 = new TextView(this);
        chat2.setText("second message");
        chat2.setLayoutParams(params);
        chat2.setGravity(Gravity.RIGHT);
        linearLayout.addView(chat2);




        Log.d("상아","추가되었음");





    }
}
