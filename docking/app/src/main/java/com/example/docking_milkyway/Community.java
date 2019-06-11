package com.example.docking_milkyway;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Community extends Fragment {
    private View view;
    private Button uploading, search;
    private TextView isnotlogin;
    private ArrayList<ArrayList<CommentDB>> commentDBS = new ArrayList<>(100);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community, container, false);

        final FirebaseFirestore fireDB = FirebaseFirestore.getInstance();

        ArrayList<ContentDB> recyclerlist = new ArrayList<>();
        Context context = view.getContext();
        uploading = view.findViewById(R.id.upload);
        search = view.findViewById(R.id.search);
        isnotlogin = view.findViewById(R.id.isnotlogin);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        uploading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), uploading.class);
                Log.d("은하", "go uploading activity");
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), uploading.class);
                startActivity(intent);
            }
        });

        SaveSharedPreference login_history = new SaveSharedPreference();
        Log.d("은하", login_history.toString());
        //String userid = login_history.getUserName(view.getContext());
        String userid="1002galaxy@gmail.com"; //테스트용
        Log.d("은하", "userid : "+userid);
        if(userid.isEmpty()){
            Log.d("은하", "userid가 비어있음");
        }

        if(userid.isEmpty()||userid == "NULL") {
            mRecyclerView.setVisibility(View.GONE);
            isnotlogin.setVisibility(View.VISIBLE);
            uploading.setVisibility(View.INVISIBLE);
        }
        else{
            mRecyclerView.setVisibility(View.VISIBLE);
            isnotlogin.setVisibility(View.GONE);
            uploading.setVisibility(View.VISIBLE);

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
                                    ArrayList<CommentDB> commentslist = new ArrayList<>();
                                    fireDB.collection("Comments")
                                            .whereEqualTo("User_SSN", userid)
                                            .whereEqualTo("C_Num", data.SSN)
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
                                                        if(commentslist != null) {
                                                            commentDBS.add(data.SSN, commentslist);
                                                        }
                                                        Log.d("은하", "commentDBS : "+commentDBS.toString());
                                                    } else {
                                                        Log.d("은하", "Error getting documents: ", task.getException());
                                                    }
                                                    //getcommentsfirestore(finalI, userid);
                                                    Log.d("은하", "여기 recyclerlist: "+recyclerlist);
                                                    Log.d("은하", "여기?");
                                                    setrecyclerview(recyclerlist, context);

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

        return view;

    }

    public void setrecyclerview(ArrayList<ContentDB> recyclerlist, Context context){
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        //mRecyclerView.setHasFixedSize(true);

        //use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        //specify an adapter (see also next example)
        MyAdapter adapter = new MyAdapter(context, recyclerlist, commentDBS);
        Log.d("은하", "여기까지왔나?");
        mRecyclerView.setAdapter(adapter);

    }

}
