package com.example.docking_milkyway;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {

    Button forgetidpw;
    Button log_in;
    EditText emailinsert;
    EditText pwinsert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        forgetidpw = findViewById(R.id.forgetidpw);
        log_in = findViewById(R.id.log_in);
        emailinsert = findViewById(R.id.emailinsert);
        pwinsert = findViewById(R.id.pwinsert);
        forgetidpw.setOnClickListener((View.OnClickListener) this);
        log_in.setOnClickListener((View.OnClickListener) this);

    }

    public void onClick(View v){

    }
}
