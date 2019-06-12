package com.example.docking_milkyway;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Matching extends AppCompatActivity {
    boolean matchSuccess = false;
    boolean matchAccept = false;
    Intent intent;

    String myUserID;            // 사용자의 userID
    matching_doginfo myDogInfo; // 사용자 반려견의 정보
    String matchmateUserID;     // 매칭성공한 상대방의 userID
    String SendReqUserID;       // 해당 userID 로 매칭 요청을 보냈음(변형된 상태)
    String ReceiveReqUserID;    // 해당 userID 로부터 매칭 요청을 받음(변형된 상태)


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching);
        intent = new Intent(this.getIntent());

        View animView1 = (View) findViewById(R.id.animView1);
        View animView2 = (View) findViewById(R.id.animView2);
        View animView3 = (View) findViewById(R.id.animView3);

        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotatealpha);
        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotatealpha);
        anim2.setStartTime(-1000);
        anim2.setDuration(6000);
        Animation anim3 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotatealpha);
        anim3.setStartTime(-2000);
        anim3.setDuration(4000);

        animView1.setAnimation(anim1);
        animView2.setAnimation(anim2);
        animView3.setAnimation(anim3);

        Toast.makeText(getApplicationContext(), "산책메이트 매칭을 시작합니다.", Toast.LENGTH_SHORT).show();

        String dogSpecies;

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // 유저 이메일, 반려견 견종 받아오기
        SaveSharedPreference login_history = new SaveSharedPreference();
        myUserID = login_history.getUserName(getApplicationContext());
        dogSpecies = "Chihuahua";               // 유저정보에서 가져오는걸로 수정,비반려인 유저의 경우 finish 되도록

        // 유저정보에 등록된 견종으로 강아지의 특성정보 asset json에서 받아오기
        myDogInfo = getDogAttriPart(dogSpecies); // match_dog class로 구성

        // 스스로를 실시간DB에 등록
        String myCvtdID = convertID(myUserID);
        mDatabase.child("MatchList").child(myCvtdID).setValue(myDogInfo);
        Log.d("상아","converted ID : "+myCvtdID);

        //  set test values !
        /*
        String userID1 = "test1@gmail.com";
        String dogSpecies1 = "Afghan Hound";
        String userID2 = "test2@gmail.com";
        String dogSpecies2 = "American Pit Bull Terrier";
        String userID3 = "test3@gmail.com";
        String dogSpecies3 = "Pomeranian";
        String userID4 = "test4@gmail.com";
        String dogSpecies4 = "Maltese";
        String userID5 = "test5@gmail.com";
        String dogSpecies5 = "English Cocker Spaniel";
        matching_doginfo DogInfo1 = getDogAttriPart(dogSpecies1);
        mDatabase.child("MatchList").child(convertID(userID1)).setValue(DogInfo1);
        matching_doginfo DogInfo2 = getDogAttriPart(dogSpecies2);
        mDatabase.child("MatchList").child(convertID(userID2)).setValue(DogInfo2);
        matching_doginfo DogInfo3 = getDogAttriPart(dogSpecies3);
        mDatabase.child("MatchList").child(convertID(userID3)).setValue(DogInfo3);
        matching_doginfo DogInfo4 = getDogAttriPart(dogSpecies4);
        mDatabase.child("MatchList").child(convertID(userID4)).setValue(DogInfo4);
        matching_doginfo DogInfo5 = getDogAttriPart(dogSpecies5);
        mDatabase.child("MatchList").child(convertID(userID5)).setValue(DogInfo5);
        */

        // 데이터베이스에 올려진 자신의 정보에 listener
        // userGetMatchRequest(상대로부터 받은 매칭요청) userMatchRequestAccepted(내가 보낸 요청에 대한 상대의 답신)
        mDatabase.child("MatchList").child(myCvtdID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                String value = dataSnapshot.getValue(String.class);
                Log.d("상아","change on myuserID : "+key+" : "+value);
                // userGetMatchRequest(상대로부터 받은 매칭요청)  => ReceiveReqUserID
                // 이 사람에 대한 매칭요청을 수락할지 말지 선택해야 - 수락할 경우 디엠창으로, 거절할경우 해당값 초기화
                if (key.equals("userGetMatchRequest")){
                    ReceiveReqUserID = value;
                    GiveAnswer(mDatabase,myCvtdID);
                }
                // userMatchRequestAccepted(내가 보낸 요청에 대한 상대의 답신) => SendReqUserID
                // 이 값이 상대의 아이디와 같다면 스스로 종료하고 디엠창으로
                if (key.equals("userMatchRequestAccepted") && value.equals(SendReqUserID)){
                    startDM(myUserID, matchmateUserID);
                    matchSuccess = true;
                    MatchingSuccess(mDatabase,myCvtdID);
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        // 사용자 이후 추가된 유저들에 대해 검색하고
        // 적합할 경우 물어본후 매칭 요청 보내기
        mDatabase.child("MatchList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String newUserID = dataSnapshot.getKey();
                matching_doginfo newDogInfo = dataSnapshot.getValue(matching_doginfo.class);
                Log.d("상아","newUserID : "+newUserID);
                Log.d("상아","newDoginfo size : "+newDogInfo.size);
                // 상대방의 반려견 정보와 사용자의 반려견 정보값의 차이 측정
                int enerDiff = Math.abs(myDogInfo.energyLevel - newDogInfo.energyLevel);
                int exerDiff = Math.abs(myDogInfo.exerciseNeeds - newDogInfo.exerciseNeeds);
                int friDiff = Math.abs(myDogInfo.friendlyTowardStrangers - newDogInfo.friendlyTowardStrangers);
                int potDiff = Math.abs(myDogInfo.potentialForPlayfulness - newDogInfo.potentialForPlayfulness);
                int senDiff = Math.abs(myDogInfo.sensitivityLevel - newDogInfo.sensitivityLevel);
                int Diff = enerDiff + exerDiff + friDiff + potDiff + senDiff;
                Log.d("상아","newUserID's diff : "+Diff);

                // 차이값이 10미만이고 사이즈가 같을 경우
                // 요청 보내기 : 상대방의 userGetMatchRequest 에 자신의 id적기
               if (Diff < 10 && myDogInfo.size == newDogInfo.size && myCvtdID!=newUserID){
                   mDatabase.child("MatchList").child(newUserID).child("userGetMatchRequest").setValue(myCvtdID);
                   Log.d("상아","newUserID gets match request from : "+ myCvtdID);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                /*
                String key = dataSnapshot.getKey();
                String value = dataSnapshot.getValue(String.class);
                Log.d("상아","change on myuserID : "+key+" : "+value);
                // userGetMatchRequest(상대로부터 받은 매칭요청)  => ReceiveReqUserID
                // 이 사람에 대한 매칭요청을 수락할지 말지 선택해야 - 수락할 경우 디엠창으로, 거절할경우 해당값 초기화
                if (key.equals("userGetMatchRequest")){
                    ReceiveReqUserID = value;
                    GiveAnswer(mDatabase,myCvtdID);
                }
                // userMatchRequestAccepted(내가 보낸 요청에 대한 상대의 답신) => SendReqUserID
                // 이 값이 상대의 아이디와 같다면 스스로 종료하고 디엠창으로
                if (key.equals("userMatchRequestAccepted") && value.equals(SendReqUserID)){
                    startDM(myUserID, matchmateUserID);
                    matchSuccess = true;
                    MatchingSuccess(mDatabase,myCvtdID);
                }*/
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });



        /*while(!matchSuccess){
            TryMatch(mDatabase, myCvtdID);
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 사용자가 매칭과정 중간에 나갈경우 실시간db에 올려놓은 자기정보 삭제
    }


    // 매칭에 성공할 경우 (상대방이 요청을 수락하거나, 상대의 요청을 수락하거나)
    // matchSuccess = true 하고 바로 return 한다
    void TryMatch(DatabaseReference mDatabase, String cvtdID) {
        // 사용자가 다른 사용자에게 요청을 보내는 부분
        // 실시간DB에서 사용자JSON 파일 가져와서 검색
        // 사이즈동일, 나머지 수치들의 차이값 합이 5 이하인 유저 찾아서 같이 산책할지 물어보기 : 상대방의 userGetMatchRequest = 자신의 userID (exception 처리)
        SendReqUserID = "!!";
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 매칭 요청이 왔을 때 해당 요청을 수락 또는 거절
    public void GiveAnswer(DatabaseReference mDatabase, String cvtdID){
        // 다이얼로그 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.accept_matched_mate, null);
        builder.setView(view);

        // 다이얼로그 세팅
        TextView matchedmateinfo = (TextView) view.findViewById(R.id.matchedmateinfo);
        matchedmateinfo.setText(REconvertID(ReceiveReqUserID) + "\n 반려견정보"); // 반려견 정보 추가
        final Button acceptB = (Button) view.findViewById(R.id.accept);
        final Button rejectB = (Button) view.findViewById(R.id.reject);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // 버튼리스너 세팅 : 수락할 경우
        acceptB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "산책메이트 수락! DM창으로 이동합니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                matchmateUserID = REconvertID(ReceiveReqUserID);   // 디엠상대의 userID
                startDM(myUserID, matchmateUserID);
                matchSuccess = true;
                MatchingSuccess(mDatabase,cvtdID);
            }
        });
        // 버튼리스너 세팅 : 거절할 경우
        rejectB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "산책메이트 거절! 다시 매칭을 시도합니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                matchSuccess = false;
            }
        });
    }

    // 매칭에 성공함 : 스스로의 정보를 지우고, 현재 intent를 끝낸다
    public void MatchingSuccess(DatabaseReference mDatabase, String cvtdID){
        // Successfully matched
        Toast.makeText(getApplicationContext(), "매칭 성공", Toast.LENGTH_SHORT).show();
        Log.d("상아","매칭성공");
        // 매칭에 성공할 경우 스스로의 정보 지우기
        mDatabase.child("MatchList").child(cvtdID).setValue(null);
        // 현재 intent 끝내기
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "match process will end in 15sec", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, 15000);
    }

    // 반려견의 견종으로 관련 정보들 중 일부 받아와서 match_dog 클래스로 생성
    public matching_doginfo getDogAttriPart(String DogSpecies){
        String json = null;
        try {
            // read json file from asset
            InputStream is = getResources().getAssets().open("dog.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            Log.d("상아","getDotAttriPart : get dog.json from asset");

            // make json object
            JSONObject obj = new JSONObject(json);
            // 해당하는 견종의 정보를 받아옴
            JSONObject Dogobj = obj.getJSONObject("dog_breeds").getJSONObject(DogSpecies);
            int ExerciseNeeds = Dogobj.getInt("Exercise Needs");
            int EnergyLevel = Dogobj.getInt("Energy Level");
            int FriendlyTowardStrangers = Dogobj.getInt("Friendly Toward Strangers");
            int PotentialForPlayfulness = Dogobj.getInt("Potential For Playfulness");
            int SensitivityLevel = Dogobj.getInt("Sensitivity Level");
            int Size = Dogobj.getInt("Size");

            matching_doginfo DogInfo = new matching_doginfo(DogSpecies, ExerciseNeeds, EnergyLevel,
                    FriendlyTowardStrangers, PotentialForPlayfulness,SensitivityLevel, Size,
                    "null", "null");
            Log.d("상아","getDotAttriPart : get info of dogspecies : "+DogSpecies);

            return DogInfo;
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            Log.d("상아","getDotAttriPart : unsupported encoding");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d("상아","getDotAttriPart : io exception");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("상아","getDotAttriPart : json  exception");
        }
        return null;
    }

    // "testemail@gmail.com" -> "testemail@gmail/com"
    // 스스로의 이메일을 실시간DB에 저장할때 사용
    public String convertID (String baseID){
        char c;
        String result="";
        for (int i = 0; i<baseID.length(); i++ ){
            c = baseID.charAt(i);
            if (c=='.') { // .은 pathname이 될 수 없으므로 수정
                result += '>';      }
            else {
                result += c;        }
        }
        Log.d("상아","convertID : "+baseID+"->"+result );
        return result;
    }

    // "testemail@gmail/com" -> "testemail@gmail.com"
    // 매칭상대의 이메일 보여주는 데에 사용
    public String REconvertID (String convertedID){
        char c;
        String result="";
        for (int i = 0; i<convertedID.length(); i++ ){
            c = convertedID.charAt(i);
            if (c=='>') { // '>'로 수정했던 것을 다시'.'로 수정
                result += '.';      }
            else {
                result += c;        }
        }
        Log.d("상아","convertID : "+convertedID+"->"+result );
        return result;
    }

    // start DM process
    void startDM(String myID, String othersID){
        Intent intent = new Intent(getApplicationContext(), DM.class);
        startActivity(intent);
    }




}
