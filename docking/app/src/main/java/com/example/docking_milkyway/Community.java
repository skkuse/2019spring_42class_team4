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

        //test를 위해 userid를 미리 선언
        //원래대로라면 사용자 캐시 혹은 로그인 데이터로부터 가져와야 한다
        String userid = "1002galaxy@gmail.com";

        //if 가져올 contents가 없다면 화면에 팔로우한 사용자가 없습니다 띄워주고 follow 추천
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


                        /*
                        List list = (List) document.getData().get("list");

                        Log.d("은하", list.size() + "");
                        for (int i=0;i < list.size(); i++) {
                            Log.d("은하", "data["+i+"] > " +list.get(i).toString());
                            HashMap map = (HashMap) list.get(i);
                            ContentDB data = new ContentDB();
                            data.setSSN(Integer.parseInt(map.get("SSN").toString()));
                            data.setLike(Integer.parseInt(map.get("like").toString()));
                            data.setSubstance(map.get("substance").toString());
                            data.setText(map.get("text").toString());
                            data.setUserSSN(map.get("userSSN").toString());
                            Log.d("은하", map.get("SSN").toString());
                            //data.settag <-- boolean 처리 어떻게?
                            //data.setdate <-- 처리 위한 코드 있음
                            Log.d("은하","contents 처리 작업 ok");

                            //이 뒤에 리사이클러뷰 조정
                            recyclerlist.add(i, data);
                            Log.d("은하", i+"");
                        }


                        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
                        //mRecyclerView.setHasFixedSize(true);

                        //use a linear layout manager
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

                        //specify an adapter (see also next example)
                        MyAdapter adapter = new MyAdapter(recyclerlist);
                        Log.d("은하", "여기까지왔나?" + recyclerlist.size());
                        mRecyclerView.setAdapter(adapter);
                        */
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
        //else{} 추가해야

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
