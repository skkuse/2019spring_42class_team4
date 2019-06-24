package com.example.docking_milkyway;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DM extends AppCompatActivity {

    String myID;
    String yourID;
    String myID_cvted;
    String yourID_cvted;
    String chatroomName;
    char userType;

    DatabaseReference mDatabase;
    LinearLayout linearLayout;
    EditText inputText;
    Button sendText;
    int count; // 본인 메세지 카운팅

    LinearLayout.LayoutParams params;
    LayoutInflater inflater;


    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm);
        intent = new Intent(this.getIntent());
        Log.d("상아","start to create DM");

        Toast.makeText(getApplicationContext(), "산책메이트와 연결되었습니다!\n메이트와 함께 산책 계획을 잡아보세요.", Toast.LENGTH_SHORT).show();

        // Matching 객체에서 정보를 전달받습니다. firebase에 저장해야 하므로 convert 합니다
        myID =      intent.getExtras().getString("myID");
        yourID =    intent.getExtras().getString("yourID");
        myID_cvted =      convertID(myID);
        yourID_cvted =    convertID(yourID);
        Log.d("상아","myID : "+myID+" / yourID : "+yourID);

        // chatroom이름을 생성하고, userType을 정합니다
        // chatroom이름은 나와 상대의 userID를 알파벳순으로 나열한 것입니다!
        // 알파벳순으로 먼저인 유저는 typeA이고 뒤인 유저는 typeB입니다.
        setChatroomName(myID_cvted, yourID_cvted);

        // 레이아웃에 포함된 오브젝트들을 생성합니다
        mDatabase = FirebaseDatabase.getInstance().getReference();
        linearLayout = (LinearLayout) findViewById(R.id.chatlog);
        inputText = (EditText) findViewById(R.id.inputtext);
        sendText = (Button) findViewById(R.id.sendtext);

        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        // 생성한 chatroom이름으로 chatroom을 생성합니다
        Log.d("상아","cvtdid : "+myID_cvted+" "+yourID_cvted);
        if (userType == 'A'){       mDatabase.child("DMList").child(chatroomName).child("_userID_A").setValue(myID_cvted);    }
        else if (userType == 'B'){  mDatabase.child("DMList").child(chatroomName).child("_userID_B").setValue(myID_cvted);    }


        // send button의 clicklistener을 생성합니다
        // send 버튼을 누를 경우 firebase에 해당 내용을 적절한 키값과 함께 저장합니다
        sendText.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                String sendString = inputText.getText().toString();
                String sendNumber = String.format("%03d", count)+userType;

                mDatabase.child("DMList").child(chatroomName).child(sendNumber).setValue(sendString);
                inputText.setText(null);
                count++;
            }
        });


        // 실시간 DB의 chatroom에 대해 childlistener을 생성합니다
        // 추가되는 모든 채팅로그에 대해 검사하여 적절히 유저 화면에 띄웁니다
        mDatabase.child("DMList").child(chatroomName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                char[] keyarr = key.toCharArray();
                String msg = dataSnapshot.getValue(String.class);

                // 유저로부터 보내진 메세지일 경우
                if(  keyarr.length == 4 ){
                    // 나의 메세지인 경우
                    if ( keyarr[3] == userType ){
                        makeBubble(myID, msg);
                    }
                    // 상대방의 메세지인 경우
                    else {
                        makeBubble(yourID, msg);
                    }
                }
                // 상대 유저가 나간 경우
                if ( key.equals("_exit") && msg.equals("true") ){
                    Toast.makeText(getApplicationContext(), "연결된 산책메이트가 DM창을 떠났습니다. 산책을 시작합니다!", Toast.LENGTH_SHORT).show();
                    mDatabase.child("DMList").child(chatroomName).setValue(null);
                    // 현재 intent 끝내기
                    Handler hd = new Handler();
                    hd.postDelayed(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "match process will end in 5sec", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, 5000);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 사용자가 매칭과정 중간에 나갈경우 아래의 값을 설정
        mDatabase.child("DMList").child(chatroomName).child("_exit").setValue("true");
        Toast.makeText(getApplicationContext(), "매칭 과정을 마치고 산책을 시작합니다!", Toast.LENGTH_SHORT).show();
    }

    // 말풍선 생성기
    public void makeBubble(String writerText, String bodyText){
        // relative layout을 가져와서 inflate한다
        View v1;
        if (writerText.equals(myID)){             // 나의 아이디인경우
            v1 = inflater.inflate(R.layout.dm_mybubble, null);
        } else if (writerText.equals(yourID)){    // 상대의 아이디인 경우
            v1 = inflater.inflate(R.layout.dm_yrbubble, null);
        } else {
            return;
        }


        // bubbleholder로 Textview들을 정의한다
        bubbleHolder holder = new bubbleHolder();
        holder.body = (TextView) v1.findViewById(R.id.message_body);
        holder.writer = (TextView) v1.findViewById(R.id.message_writer);
        v1.setTag(holder);

        // holder를 통해 가져온 텍스트뷰의 내용을 편집한다
        holder.body.setText(bodyText);
        holder.writer.setText(writerText);

        // 수정한 relative layour을 추가한다
        linearLayout.addView(v1, params);
    }

    // chatroom이름을 생성하고, userType을 정합니다
    public void setChatroomName(String myID, String yourID){
        // myID 가 yourID 보다 알파벳순으로 뒤에 있을 경우
        if(myID.compareToIgnoreCase(yourID)>0){
            chatroomName = yourID + myID;
            userType = 'B';
        }
        // myID 가 yourID 보다 알파벳순으로 앞에 있을 경우
        else{
            chatroomName = myID + yourID ;
            userType = 'A';
        }
        Log.d("상아","chatroomname CREATED : "+chatroomName);
        Log.d("상아","youruserType CREATED : "+userType);
    }

    /*
     * 상아 : firebase 실시간 데이터베이스에서는 . 문자를 기록할 수 없습니다
     * 이메일에는 . 이 포함되어 있어 이메일을 그대로 실시간 데이터베이스에 저장할 수 없고
     * user@email.com 을 user@email>com 으로 변형 저장하여 매칭 및 상대방을 찾는 과정을 진행한 후
     * 상대 화면에서 아이디가 보여지도록 할 때에는 다시 재변형하여 사용합니다
     * 아래의 두 메서드는 이메일 변형/재변형을 담당합니다!
     */

    // "testemail@gmail.com" -> "testemail@gmail>com"
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

    // "testemail@gmail>com" -> "testemail@gmail.com"
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



}



class bubbleHolder{
    public TextView writer;
    public TextView body;
}
