package com.example.docking_milkyway;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/*팔로잉 팔로우 목록 표시 액티비티입니다~~~~~~~*/


public class Follwing extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.following);

        SaveSharedPreference login_history = new SaveSharedPreference();
        String userid = login_history.getUserName(getApplicationContext());     // 로그인 기록 유지 위해서 사용
        //String userid = "yongtae0104@gmail.com";

        FirebaseFirestore followDB = FirebaseFirestore.getInstance();
        DocumentReference followRef = followDB.collection("Follow").document(userid);




        followRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        //String[] following = document.get("following",String[].class);
                        //arr.add()
                        Log.d("으아아아아아", "팔로잉 리스트 확인");
                        Log.d("add",""+ document.getData());
                        FollowDB fflist = document.toObject(FollowDB.class);        // FollowDB 객체를 받아옴
                        Log.d("logggggggggggg",""+fflist.follower);
                        Log.d("logggggggggggg",""+fflist.following);
                        //String[] s = {"aaaa", "bbbbb"};
                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, fflist.follower);  // 팔로워 목록을 위한 listview
                        ListView listView1 = (ListView) findViewById(R.id.listview1);
                        listView1.setAdapter(adapter1);
                        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_LONG).show();  // 팔로워 리스트 목록 클릭할경우 이벤트
                            }
                        });

                        ArrayAdapter<String> adapter2 = new <String>ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, fflist.following);  // 팔로잉 목록을 위한 listview
                        ListView listView2 = (ListView) findViewById(R.id.listview2);
                        listView2.setAdapter(adapter2);

                        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_LONG).show();   // 팔로잉 리스트 목록 클릭할경우 이벤트
                            }
                        });
                    }else{
                        Log.d("following", "follow목록 없음..");
                        finish();
                    }
                }else{
                    Log.d("following", "get() failed");
                }
            }
        });

    }
}
