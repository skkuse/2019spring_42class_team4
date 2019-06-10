package com.example.docking_milkyway;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button bt1, bt2, bt3;
    FragmentManager fm ;
    FragmentTransaction fragtrans ;
    Community community;
    nowWalking nowwalking;
    Walking walking;;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_main);

        // set : toolbar and drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Log.d("상아","메인 등장!");

        // set : other menu buttons
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);
        community = new Community();
        nowwalking = new nowWalking();
        walking = new Walking();
        setFrag(0);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("은하", "버튼 1 눌림");
                setFrag(0);
                Log.d("은하" , "fragment 1로 전환했음");
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("은하", "버튼 2 눌림");
                setFrag(1);
                Log.d("은하", "fragment 2로 전환했음");
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), view_history.class);
                startActivity(intent);
            }
        });
        Log.d("상아","리스너 추가 완료");

        // create Navigation Header
        // 여기서 유저 프로필 이미지 , 유저이름, 유저정보 수정해주시면 될 것 같습니다!
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header);
        ImageView userIMG = (ImageView)hView.findViewById(R.id.userImg);
        TextView userN = (TextView)hView.findViewById(R.id.userNickname);
        userN.setText("newUserNIckname!");

        //firebase 연동 확인
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");

        //myRef.setValue("Hello, World!");

    }

    private void setFrag(int n) {
        fm = getFragmentManager();
        fragtrans = fm.beginTransaction();
        switch (n) {
            case 0:
                fragtrans.replace(R.id.main_frame, community);
                fragtrans.commit();
                Log.d("은하", "fragment 1로 전환");
                break;
            case 1:
                fragtrans.replace(R.id.main_frame, nowwalking);
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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signin) {
            Intent intent = new Intent(this, signin.class);
            startActivity(intent);
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        } else if (id == R.id.nav_mypage) {
            Intent intent = new Intent(this, Mypage.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}