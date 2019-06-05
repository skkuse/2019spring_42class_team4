package com.example.docking_milkyway;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class search extends Fragment {
    private View view;
    private Button search;
    private EditText searchtext;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search, container, false);

        search = view.findViewById(R.id.search);
        searchtext = view.findViewById(R.id.searchtext);

        String text;
        text = searchtext.getText().toString();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //db.collection("Contents").whereEqualTo("text", text).get();
            }
        });

        return view;
    }
}
