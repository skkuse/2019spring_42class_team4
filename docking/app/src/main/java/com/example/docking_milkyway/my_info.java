package com.example.docking_milkyway;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;




/* 내정보 수정 페이지 입니다~~~~~ */


public class my_info extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_info);

        //EditText Email_insert = findViewById(R.id.emailinsert);
        EditText Pw_insert = findViewById(R.id.pwinsert);
        EditText Name_insert = findViewById(R.id.nameinsert);
        EditText Nick_insert = findViewById(R.id.nicknameinsert);
        Button modify = findViewById(R.id.modify);

        SaveSharedPreference login_history = new SaveSharedPreference();
        String email = login_history.getUserName(getApplicationContext());

        FirebaseFirestore userDB = FirebaseFirestore.getInstance();
        DocumentReference userRef = userDB.collection("user").document(email);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        UserDB temp_user = document.toObject(UserDB.class);   // 데이터베이스에서 user 오브젝트 가져옴
                        //Email_insert.setText(temp_user.email);
                        Pw_insert.setText(temp_user.pw);
                        Name_insert.setText(temp_user.name);
                        Nick_insert.setText(temp_user.nickname);              // 가져온 유저 오브젝트의 pw, name , nick_name 등 표시

                        modify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UserDB modified = new UserDB(email, Name_insert.getText().toString(),Pw_insert.getText().toString(), Nick_insert.getText().toString(),20,"남성","전문가");
                                userRef.set(modified);
                                // 수정된 정보를 포함하는 유저 객체를 만들고 데이터베이스에 수정된 객체를 set함
                                Toast.makeText(getApplicationContext(),"수정완료",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }else{
                        // 계정에 해당하는 유저DB가 없을경우
                        Toast.makeText(getApplicationContext(),"유저정보 존재하지 않음",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });



    }
}
