package com.example.docking_milkyway;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class signin extends AppCompatActivity {

    private Button usertypeinsert;
    private Button sign_in;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        usertypeinsert = findViewById(R.id.usertypeinsert);
        sign_in = findViewById(R.id.sign_in);
    }
}
