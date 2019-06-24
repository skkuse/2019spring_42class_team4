package com.example.docking_milkyway;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class search extends AppCompatActivity {

    private Button search, dockinghome;
    private EditText searchtext;

    private ArrayList<ArrayList<CommentDB>> commentDBS = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Log.d("은하", "search page로 이동");

        search = findViewById(R.id.search);
        dockinghome = findViewById(R.id.dockinghome);
        searchtext = findViewById(R.id.searchtext);

        final FirebaseFirestore fireDB = FirebaseFirestore.getInstance();
        Context context = this;
        ArrayList<ContentDB> recyclerlist = new ArrayList<>();

        Intent intent = getIntent();
        String searchingtext = intent.getStringExtra("searchtext");

        //intent로 받아온 searchtext에 대한 query
        fireDB.collection("Contents")
                .whereEqualTo("userSSN", searchingtext)
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
                                Log.d("은하", "contentSSN : "+data.SSN + ", searchtext_id : "+searchingtext);

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

        //docking main화면으로 돌아가기
        dockinghome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                Log.d("은하", "go Mainactivity");
                startActivity(intent);
            }
        });

        //새 searchtext가 입력된 경우
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchingtext = searchtext.getText().toString();

                fireDB.collection("Contents")
                        .whereEqualTo("userSSN", searchingtext)
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
                                        Log.d("은하", "contentSSN : "+data.SSN + ", searchtext_id : "+searchingtext);

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
        });
    }

    public void setrecyclerview(ArrayList<ContentDB> recyclerlist, Context context){
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.searchrecyclerview);
        //mRecyclerView.setHasFixedSize(true);

        //use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        //specify an adapter (see also next example)
        MyAdapter adapter = new MyAdapter(context, recyclerlist, commentDBS);
        Log.d("은하", "여기까지왔나?");
        mRecyclerView.setAdapter(adapter);

    }

}
