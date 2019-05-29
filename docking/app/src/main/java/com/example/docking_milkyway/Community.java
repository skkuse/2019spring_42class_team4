package com.example.docking_milkyway;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community, container, false);

        //test를 위해 userid를 미리 선언
        //원래대로라면 사용자 캐시 혹은 로그인 데이터로부터 가져와야 한다
        String userid = "1002galaxy@gmail.com";

        //if 가져올 contents가 없다면 화면에 팔로우한 사용자가 없습니다 띄워주고 follow 추천
        if(userid != null) {

            FirebaseFirestore.getInstance().collection("Contents").document(userid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //success
                    DocumentSnapshot document = task.getResult();
                    List list = (List) document.getData().get("list"); //list말고 문서번호로 바꿀 것 <-- error
                    for (int i = 0; i < list.size(); i++) {
                        Log.d("은하", "data " + list.get(i).toString());
                        HashMap map = (HashMap) list.get(i);
                        ContentDB data = new ContentDB();
                        data.setSSN(Integer.parseInt(map.get("SSN").toString()));
                        Log.d("은하", map.get("SSN").toString());
                    }
                }

            });

        }
        //else{} 추가해야

        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<100; i++) {
            list.add(String.format("TEXT %d", i));
        }

        Context context = view.getContext();

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        //mRecyclerView.setHasFixedSize(true);

        //use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        //specify an adapter (see also next example)
        MyAdapter adapter = new MyAdapter(list);
        mRecyclerView.setAdapter(adapter);

        return view;

    }

}
