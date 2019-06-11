package com.example.docking_milkyway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Mypage extends AppCompatActivity {

    Button My_info;
    private ArrayList<ArrayList<CommentDB>> commentDBS = new ArrayList<>(100);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        SaveSharedPreference login_hist = new SaveSharedPreference();

        String userid = login_hist.getUserName(getApplicationContext());

        FirebaseFirestore contentsDB = FirebaseFirestore.getInstance();
        ArrayList<ContentDB> recyclerlist = new ArrayList<>();
        Context context = Mypage.this;


        if(userid != null) {

            contentsDB.collection("Contents").document(userid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //success
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d("은하", "사용자 아이디에 해당하는 게시물 있음");
                        Log.d("은하", "document : "+document.getData());

                        ///ArrayList<ContentDB> contentsdata = new ArrayList<>();
                        long contentssize = (long) document.getData().get("contentssize");
                        Log.d("은하", "contentssize : "+contentssize);
                        //int contentssize=2;
                        for(int i=0 ; i < contentssize ; i++){
                            DocumentReference contentsRef = contentsDB.collection("Contents")
                                    .document(userid).collection(userid).document(String.valueOf(i));
                            int finalI = i;
                            Log.d("은하", finalI +": finalI");
                            contentsRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot contentdocument = task.getResult();
                                        if(contentdocument.exists()){
                                            Log.d("은하", contentdocument.toString());
                                            ContentDB data = (ContentDB) contentdocument.toObject(ContentDB.class);
                                            Log.d("은하", "data: "+data.toString());
                                            recyclerlist.add(data);
                                            Log.d("은하", "contentSSN : "+finalI + ", userid : "+userid);
                                            ArrayList<CommentDB> commentslist = new ArrayList<>();
                                            contentsDB.collection("Comments")
                                                    .whereEqualTo("User_SSN", userid)
                                                    .whereEqualTo("C_Num", finalI)
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
                                                                Log.d("은하", "finalI : "+ finalI +", commentslist : "+commentslist);
                                                                if(commentslist != null) {
                                                                    commentDBS.add(finalI, commentslist);
                                                                }
                                                                Log.d("은하", "commentDBS : "+commentDBS.toString());
                                                            } else {
                                                                Log.d("은하", "Error getting documents: ", task.getException());
                                                            }
                                                            //getcommentsfirestore(finalI, userid);
                                                            Log.d("은하", "여기 recyclerlist: "+recyclerlist);
                                                            if(finalI == contentssize-1){
                                                                Log.d("은하", "여기?");
                                                                setrecyclerview(recyclerlist, context);
                                                            }
                                                        }
                                                    });
                                        }
                                    }else{
                                        Log.d("은하", "get() failed");
                                    }
                                }
                            });

                        }


                    }
                    else{
                        Log.d("은하", "사용자 아이디에 해당하는 게시물 없음");
                    }
                }
                else{
                    Log.d("은하", "get() failed");
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
