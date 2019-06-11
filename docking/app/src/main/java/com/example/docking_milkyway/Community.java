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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community, container, false);

        final FirebaseFirestore contentsDB = FirebaseFirestore.getInstance();

        ArrayList<ContentDB> recyclerlist = new ArrayList<>();
        Context context = view.getContext();
        uploading = view.findViewById(R.id.upload);
        search = view.findViewById(R.id.search);
        isnotlogin = view.findViewById(R.id.isnotlogin);

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

        //SaveSharedPreference login_history = new SaveSharedPreference();
        //String userid = login_history.getUserName(view.getContext());

        String userid="1002galaxy@gmail.com";
        if(userid != "NULL") {

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
                                            Log.d("은하", "여기 recyclerlist: "+recyclerlist);
                                            if(finalI ==contentssize-1){
                                                Log.d("은하", "여기?");
                                                setrecyclerview(recyclerlist, context);
                                            }
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
        else{
            isnotlogin.setVisibility(View.VISIBLE);
            uploading.setVisibility(View.INVISIBLE);
        }

        return view;

    }

    public void setrecyclerview(ArrayList<ContentDB> recyclerlist, Context context){
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        //mRecyclerView.setHasFixedSize(true);

        //use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        //specify an adapter (see also next example)
        MyAdapter adapter = new MyAdapter(recyclerlist);
        Log.d("은하", "여기까지왔나?");
        mRecyclerView.setAdapter(adapter);

    }

}
