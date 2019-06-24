package com.example.docking_milkyway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;


public class signin extends AppCompatActivity {

    Context context = this;

    String sex;
    String user_type;

    Button usertypeinsert;
    Button sign_in;
    Button sexinsert;
    Button ageinsert;
    private EditText emailinsert;
    private EditText pwinsert;
    private EditText nameinsert;
    private EditText nicknameinsert;

    ArrayAdapter<String> sexadapter;
    ArrayAdapter<String> usertypeadapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        usertypeinsert = findViewById(R.id.userTypeinsert);
        sign_in = findViewById(R.id.sign_in);

        emailinsert = findViewById(R.id.emailinsert);
        pwinsert = findViewById(R.id.pwinsert);
        nameinsert = findViewById(R.id.nameinsert);
        nicknameinsert = findViewById(R.id.nicknameinsert);
        sexinsert = findViewById(R.id.sexinsert);

        //김은하; 성별 선택창 작성
        sexadapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice);
        sexadapter.addAll("남성", "여성");
        sexadapter.notifyDataSetChanged();

        sexinsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("성별 선택"); //타이틀

                alert.setAdapter(sexadapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sex = sexadapter.getItem(which);
                    }
                });
                alert.show();
            }
        });

        //김은하; 유저타입 선택창 작성
        usertypeadapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice);
        usertypeadapter.addAll("반려인", "비반려인", "전문가");
        usertypeadapter.notifyDataSetChanged();

        usertypeinsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("유저 타입 선택");

                alert.setAdapter(usertypeadapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user_type = usertypeadapter.getItem(which);
                    }
                });
                alert.show();
            }
        });

        final FirebaseFirestore userDB = FirebaseFirestore.getInstance();

        sign_in.setOnClickListener(new Button.OnClickListener(){
            @Override

            public void onClick(View view){
                String email = emailinsert.getText().toString();
                String pw = pwinsert.getText().toString();
                String name = nameinsert.getText().toString();
                String nickname = nicknameinsert.getText().toString();
                int age = 10;

               // User temp_user = new UserDB(email,name,pw,nickname,age,sex,user_type);
                UserDB temp_user = new UserDB(email,name,pw,nickname,age,sex,user_type);
                //CollectionReference userRef = userDB.collection("user");
                DocumentReference userRef = userDB.collection("user").document(email);
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Log.d("login", "계정 존재 확인");
                                Toast.makeText(signin.this, "이미 존재하는 계정입니다.", Toast.LENGTH_LONG).show();
                            }else{
                                Log.d("login", "최초 계정!");
                                userDB.collection("user").document(temp_user.getEmail()).set(temp_user);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }else{
                            Log.d("login", "get() failed");
                        }
                    }
                });
            }
        });
    }
}
