package com.example.docking_milkyway;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button bt1, bt2, bt3;
    FragmentManager fm;
    FragmentTransaction fragtrans;
    Mypage mypage;
    Community community;
    Walking walking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        mypage = new Mypage();
        community = new Community();
        walking = new Walking();
        setFrag(1);

        //firebase 연동 확인
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");*/
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.bt1:
                Log.d("은하", "버튼 1 눌림");
                setFrag(0);
                Log.d("은하" , "fragment 1로 전환했음");
                break;
            case R.id.bt2:
                Log.d("은하", "버튼 2 눌");
                setFrag(1);
                Log.d("은하", "fragment 2로 전환했음");
                break;
            case R.id.bt3:
                Log.d("은하","버튼 3 눌림");
                setFrag(2);
                Log.d("은하","fragment 3으로 전환했음");
                break;
        }
    }

    private void setFrag(int n) {
        fm = getFragmentManager();
        fragtrans = fm.beginTransaction();
        switch (n) {
            case 0:
                fragtrans.replace(R.id.main_frame, mypage);
                fragtrans.commit();
                Log.d("은하", "fragment 1로 전환");
                break;
            case 1:
                fragtrans.replace(R.id.main_frame, community);
                fragtrans.commit();
                Log.d("은하", "fragment 2로 전환");
                break;
            case 2:
                fragtrans.replace(R.id.main_frame, walking);
                fragtrans.commit();
                Log.d("은하", "fragment 3로 전환");
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
