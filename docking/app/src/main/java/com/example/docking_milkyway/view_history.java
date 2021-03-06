package com.example.docking_milkyway;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;



/*산책정보 표시 액티비티 입니다~~*/

public class view_history extends AppCompatActivity {
    String email;
    WalkingHistoryDB history;
    int history_len = 0;
    int num_view = 5;

    public void draw_distance_graph(ArrayList<Entry> entries, ArrayList<Timestamp> date_entries){
        LineChart lineChart = findViewById(R.id.distance_chart);
        Log.d("....",""+ entries);

        Collections.sort(entries, new Comparator<Entry>() {         // 5일간의 산책기록을 시간순으로 정렬한다
            @Override
            public int compare(Entry o1, Entry o2) {
                if(o1.getX() <= o2.getX()){
                    return -1;
                }else{
                    return 1;
                }
            }
        });

        int sum = 0;
        for(int i = 0; i < entries.size(); i++){
            sum += entries.get(i).getY();                   // 산책기록 Y축 : distance 를 모두 sum 해준다.
        }
        TextView dist_text = findViewById(R.id.distance_text);
        dist_text.setText("최근 " + entries.size() + "번의 산책동안 총 " + sum+ "m\n"
                + "평균 " + sum/entries.size() +  "m 만큼 산책했습니다.");

        String[] values = {"1","2","3","4","5"};

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(values));

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");    // 산책거리 entry들로 구성된 dataset 객체 생성
        LineData data = new LineData(dataset);

        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setDrawFilled(true); //그래프 밑부분 색칠*/
        lineChart.setData(data);
        lineChart.animateY(2000);
    }
    public void draw_time_graph(ArrayList<Entry> entries, ArrayList<Timestamp> date_entries){
        LineChart lineChart = findViewById(R.id.elapsetime_chart);
        Log.d("....",""+ entries);

        Collections.sort(entries, new Comparator<Entry>() {     // 산책 elapsetime 기록을 시간순으로 정렬한다.
            @Override
            public int compare(Entry o1, Entry o2) {
                if(o1.getX() <= o2.getX()){
                    return -1;
                }else{
                    return 1;
                }
            }
        });
        int sum = 0;
        for(int i = 0; i < entries.size(); i++){
            sum += entries.get(i).getY();           // Y축 : 산책 elapsetime
        }
        TextView elapse_text = findViewById(R.id.elapsetime_text);
        elapse_text.setText("최근 " + entries.size() + "번의 산책동안 총 " + sum/60+ "분\n"           // 평균, total 시간 계산
                + "평균 " + sum/entries.size()/60 +  "분 만큼 산책했습니다.");

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");
        LineData data = new LineData(dataset);

        dataset.setColors(ColorTemplate.COLORFUL_COLORS);           //그래프 설정 관련부분
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setDrawFilled(true); //그래프 밑부분 색칠*/
        lineChart.setData(data);
        lineChart.animateY(2000);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_history);

        SaveSharedPreference login_history = new SaveSharedPreference();

        email = login_history.getUserName(getApplicationContext());
        
        DocumentReference walkRef = null;

        final FirebaseFirestore WalkingDB = FirebaseFirestore.getInstance();
        if(email.isEmpty()||email == "NULL") {
            Log.d("view history", "산책기록 없음!");          // 산책기록 존재하지않을경우 toast띄우고 메인페이지로 돌아감
            Toast.makeText(view_history.this, "기록이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            walkRef = WalkingDB.collection("Walking").document(email);

            DocumentReference finalWalkRef = walkRef;
            walkRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("view history", "산책기록 확인!");
                            history = document.toObject(WalkingHistoryDB.class);
                            history_len = document.get("length", int.class);
                            Log.d("view history", "" + history_len);


                            CollectionReference history_list_ref;
                            history_list_ref = finalWalkRef.collection(email);

                            ArrayList<Entry> distance_entries = new ArrayList<>();
                            ArrayList<Entry> time_entires = new ArrayList<>();
                            ArrayList<Timestamp> date_entries = new ArrayList<>();

                            if (history_len < 5) num_view = history_len;
                            else num_view = 5;

                            for (int i = history_len - num_view + 1; i <= history_len; i++) {
                                int cnt = i;
                                history_list_ref.document("h" + i)
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot walking_document = task.getResult();//.toObject(WalkingHistoryDB.class);
                                            if (walking_document.exists()) {
                                                WalkingHistoryDB temp = walking_document.toObject(WalkingHistoryDB.class);
                                                distance_entries.add(new Entry(cnt, temp.distance));
                                                time_entires.add(new Entry(cnt, temp.elapsetime));
                                                date_entries.add(temp.starttime);
                                                Log.d("Read History", "Read 성공" + distance_entries);

                                                if (distance_entries.size() == num_view && time_entires.size() == num_view) {
                                                    //Collections.sort(date_entries);
                                                    draw_distance_graph(distance_entries, date_entries);
                                                    draw_time_graph(time_entires, date_entries);
                                                }
                                            }
                                        } else {
                                            Log.d("Read History", "기록 데이터 read 실패");
                                        }
                                    }
                                });

                            }
                        } else {
                            Log.d("view history", "산책기록 없음!");
                            Toast.makeText(view_history.this, "기록이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } else {
                        Log.d("view history", "get() failed");
                    }

                }
            });
        }
    }
}
