package com.example.docking_milkyway;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Mypage extends AppCompatActivity {

    Button My_info;
    Button follwoing;
    Button log_out;
    Button my_dog_info;
    private ArrayList<ArrayList<CommentDB>> commentDBS = new ArrayList<>(100);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        SaveSharedPreference login_hist = new SaveSharedPreference();

        String userid = login_hist.getUserName(getApplicationContext());

        FirebaseFirestore fireDB = FirebaseFirestore.getInstance();
        ArrayList<ContentDB> recyclerlist = new ArrayList<>();
        Context context = Mypage.this;


        if(userid != null) {

            fireDB.collection("Contents")
                    .whereEqualTo("userSSN", userid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot contentdocument : task.getResult()) {
                                    Log.d("은하", contentdocument.getId() + " => " + contentdocument.getData());
                                    ContentDB data = (ContentDB) contentdocument.toObject(ContentDB.class);
                                    Log.d("은하", "data: "+data.toString());
                                    recyclerlist.add(data);
                                    Log.d("은하", "contentSSN : "+data.SSN + ", userid : "+userid);

                                    ArrayList<String> iscomment = new ArrayList<>();
                                    ArrayList<CommentDB> commentslist = new ArrayList<>();

                                    fireDB.collection("Comments")
                                            .whereEqualTo("parent_content", data.SSN)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("은하", "여기까지?commenttasksuccess");
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d("은하", document.getId() + " => " + document.getData());
                                                            CommentDB tempcommentdb = document.toObject(CommentDB.class);
                                                            commentslist.add(tempcommentdb);
                                                            Log.d("은하", "commentslist에 잘 추가되었나? "+commentslist);
                                                        }
                                                        Log.d("은하", "finalI : "+ data.SSN +", commentslist : "+commentslist);
                                                        if(!commentslist.isEmpty()) {
                                                            Log.d("은하", data.SSN + "의 commentlist는 null이 아님!");
                                                            commentDBS.add(commentslist);
                                                            iscomment.add(data.SSN);
                                                            //getcommentsfirestore(finalI, userid);
                                                            Log.d("은하", "여기 recyclerlist: "+recyclerlist);
                                                            Log.d("은하", "여기?");
                                                            setrecyclerview(recyclerlist, context);
                                                        }
                                                        else{
                                                            commentDBS.add(commentslist);
                                                        }
                                                        Log.d("은하", "commentDBS : "+commentDBS.toString());
                                                    } else {
                                                        Log.d("은하", "Error getting documents: ", task.getException());
                                                    }

                                                }
                                            });
                                }
                            }
                            else{
                                Log.d("은하", "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }



        My_info = findViewById(R.id.my_info);

        My_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), my_info.class);
                startActivity(intent);
            }
        });

        my_dog_info = findViewById(R.id.my_dog_info);

        my_dog_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), my_dog_info.class);
                startActivity(intent);;
            }
        });

        follwoing = findViewById(R.id.Follow);

        follwoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Follwing.class);
                startActivity(intent);
            }
        });

        log_out = findViewById(R.id.log_out);
        log_out.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SaveSharedPreference login_history = new SaveSharedPreference();
                login_history.clearUserName(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setrecyclerview(ArrayList<ContentDB> recyclerlist, Context context){

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.mypage_recyclerview);
        //mRecyclerView.setHasFixedSize(true);

        //use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        //specify an adapter (see also next example)
        MyAdapter adapter = new MyAdapter(context, recyclerlist, commentDBS);
        Log.d("은하", "여기까지왔나?");
        mRecyclerView.setAdapter(adapter);

    }
}
