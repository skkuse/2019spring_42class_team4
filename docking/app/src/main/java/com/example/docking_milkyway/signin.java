package com.example.docking_milkyway;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class signin extends AppCompatActivity {

    private Button usertypeinsert;
    private Button sign_in;
    private EditText emailinsert;
    private EditText nameinsert;
    private EditText nicknameinsert;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        usertypeinsert = findViewById(R.id.usertypeinsert);
        sign_in = findViewById(R.id.sign_in);
    }
}
