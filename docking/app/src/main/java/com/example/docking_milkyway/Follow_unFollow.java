package com.example.docking_milkyway;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Follow_unFollow {

    String target_id;
    String user_id;
    String status;


    Follow_unFollow(String target_id, String user_id){
        this.target_id = target_id;
        this.user_id = user_id;
    }

    public String Follow_toggle(){

        Log.d("용태", "target_id : " + target_id + "\nuser_id : " + user_id);


        FirebaseFirestore followDB = FirebaseFirestore.getInstance();
        DocumentReference followRef = followDB.collection("Follow").document(target_id);
        followRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();                                   /* 팔로우/언팔로우 기능 구현 */
                    if(document.exists()){
                        Log.d("으아아아아아", "팔로잉 리스트 확인");
                        FollowDB fflist = document.toObject(FollowDB.class);
                        if(fflist.following.contains(user_id)) {
                            status = "해당 계정을 언팔로우합니다.";
                            Log.d("용태", "언팔로우하기전"+ fflist.following);
                            fflist.following.remove(user_id);
                            Log.d("용태", "언팔로우후"+ fflist.following);
                            followRef.set(fflist);
                            remove_follower();
                            Log.d("으아아아아아", "언팔로우한다");
                        } else {
                            status = "해당 계정을 팔로우합니다.";
                            fflist.following.add(user_id);
                            followRef.set(fflist);
                            add_follower();
                            Log.d("으아아아아아", "팔로우한다");
                        }
                    }else{
                        Log.d("following", "follow목록 없음..");
                    }
                }else{
                    Log.d("following", "get() failed");
                }
            }
        });
        Log.d("용태", ""+ status);
        return status;
    }
    public void add_follower(){
        FirebaseFirestore followDB = FirebaseFirestore.getInstance();
        DocumentReference followRef = followDB.collection("Follow").document(user_id);
        followRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {                              /*팔로우 되지 않았을경우 팔로우 버튼 클릭시 팔로우 목록에 해당계정 추가*/
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        FollowDB fflist = document.toObject(FollowDB.class);
                        fflist.follower.add(target_id);
                        followRef.set(fflist);
                    }
                }
            }
        });
    }
    public void remove_follower(){
        FirebaseFirestore followDB = FirebaseFirestore.getInstance();
        DocumentReference followRef = followDB.collection("Follow").document(user_id);      /*이미 팔로우 되어있을경우 언팔로우 버튼 클릭시 팔로우 목록에서 해당계정 삭제*/
        followRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        FollowDB fflist = document.toObject(FollowDB.class);
                        fflist.follower.remove(target_id);
                        followRef.set(fflist);
                    }
                }
            }
        });
    }
}
