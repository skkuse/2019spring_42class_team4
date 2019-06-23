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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Yourpage extends AppCompatActivity {

    Button Follow;
    String userid;

    private ArrayList<ArrayList<CommentDB>> commentDBS = new ArrayList<>(100);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yourpage);

        Intent intent = getIntent();
        String userid = intent.getStringExtra("writer");
        //SaveSharedPreference visit_hist = new SaveSharedPreference();
        //String userid = visit_hist.getVisitName(getApplicationContext());

        Log.d("용태태", "방문 페이지 : "+userid);


        FirebaseFirestore fireDB = FirebaseFirestore.getInstance();
        ArrayList<ContentDB> recyclerlist = new ArrayList<>();
        Context context = Yourpage.this;


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
                                            .whereEqualTo("User_SSN", userid)
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



        Follow = findViewById(R.id.Follow);
        Follow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SaveSharedPreference login_hist = new SaveSharedPreference();
                String target_id = login_hist.getUserName(getApplicationContext());

                Log.d("용태", "target_id : " + target_id + "\nuser_id : " + userid);

                Follow_unFollow toggle = new Follow_unFollow(target_id, userid);
                String status = toggle.Follow_toggle();
                Log.d("팔로우/언팔", "/" +  status);
                Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
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
