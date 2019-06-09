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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class uploading extends AppCompatActivity {

    Button takeapicture, getalbum, uploadok, uplodacancel;
    EditText textinsert, taginsert, locationinsert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploading);
        Log.d("은하", "in uploading");

        final FirebaseFirestore contentDB = FirebaseFirestore.getInstance();

        takeapicture = findViewById(R.id.takeapicture);
        getalbum = findViewById(R.id.getalbum);
        uploadok = findViewById(R.id.uploadok);
        uplodacancel = findViewById(R.id.uploadcancel);

        textinsert = findViewById(R.id.textinsert);
        taginsert = findViewById(R.id.taginsert);
        locationinsert = findViewById(R.id.locationinsert);

        takeapicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //카메라 접근권한 있는지 확인
                //카메라 접근
                //사용자가 사진 찍은 거 받아와서
                //띄워줌
            }
        });

        getalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //앨범 접근권한 있는지 확인
                //앨범 접근
                //사용자가 사진 선택한 거 받아와서
                //띄워줌
            }
        });

        uploadok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = "1002galaxy@gmail.com"; //테스트 위함, 임시
                Log.d("은하", "uploadbtn click");
                String text = textinsert.getText().toString();
                Boolean tag=false;
                if(taginsert.getText().toString().length() > 0){
                    tag = true;
                }

                ContentDB temp_content = new ContentDB(text, tag, userid);

                DocumentReference contentsRef = contentDB.collection("Contents").document(userid);
                contentsRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Log.d("은하", "contents document list 존재 확인");
                                long newcontentssize = (long) document.getData().get("contentssize");
                                int SSN = (int) newcontentssize;
                                temp_content.SSN = SSN;
                                contentDB.collection("Contents").document(userid)
                                        .collection(userid).document(String.valueOf(SSN)).set(temp_content);
                                //contentssize update to firebase
                                newcontentssize++;
                                contentDB.collection("Contents")
                                        .document(userid).update("contentssize", newcontentssize);
                                Intent intent = new Intent(v.getContext(), MainActivity.class);
                                startActivity(intent);
                            }else{
                                Log.d("은하", "최초 게시글");
                                ArrayList<ContentDB> list = new ArrayList<>();
                                int SSN = 0;
                                temp_content.SSN = SSN;
                                list.add(temp_content);
                                contentDB.collection("Contents").document(userid).set(list);
                                //contentssize setting to firebase
                                //contentDB.collection("Contents").document(userid).set("contentssize", 1);
                                Intent intent = new Intent(v.getContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }else{
                            Log.d("은하", "get() failed");
                        }
                    }
                });

            }
        });

        uplodacancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });



    }
}
