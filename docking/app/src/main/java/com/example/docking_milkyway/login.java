package com.example.docking_milkyway;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    Button log_in;
    EditText emailinsert;
    EditText pwinsert;
    String email;
    String pw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Intent intent = new Intent(this.getIntent());

        final FirebaseFirestore userDB = FirebaseFirestore.getInstance();


        log_in = findViewById(R.id.log_in);
        emailinsert = findViewById(R.id.emailinsert);
        pwinsert = findViewById(R.id.pwinsert);

        log_in.setOnClickListener(new Button.OnClickListener(){
            @Override

            public void onClick(View view){
                email = emailinsert.getText().toString();
                pw = pwinsert.getText().toString();

                DocumentReference userRef = userDB.collection("user").document(email);
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Log.d("login", "계정 존재 확인");
                            }else{
                                Toast.makeText(login.this, "계정이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                                Log.d("login", "계정 틀림");
                            }
                        }else{
                            Log.d("login", "get() failed");
                        }
                    }
                });

            }

        });

    }

}
