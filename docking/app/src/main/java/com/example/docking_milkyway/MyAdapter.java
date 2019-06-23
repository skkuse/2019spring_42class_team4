package com.example.docking_milkyway;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<ContentDB> mData = null;
    private ArrayList<ArrayList<CommentDB>> commentDBS = new ArrayList<>(100);
    private Context mcontext;

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        Button writer;
        TextView likes;
        ImageView contentssubstance;
        TextView contentstext;
        Button contentslike;
        Button contentsdelete;
        RecyclerView commentsrecyclerview;

        ViewHolder(View itemView) {
            super(itemView);

            //뷰 객체에 대한 참조. (hold strong reference)
            writer = itemView.findViewById(R.id.writer);
            likes = itemView.findViewById(R.id.likes);
            contentstext = itemView.findViewById(R.id.contentstext);
            contentssubstance = itemView.findViewById(R.id.ccontentssubstance);
            contentslike = itemView.findViewById(R.id.contentlikebtn);
            commentsrecyclerview = itemView.findViewById(R.id.commentsrecyclerview);
            contentsdelete = itemView.findViewById(R.id.contentdeletebtn);
        }
    }

    //생성자에서 데이터 리스트 객체를 전달받음.
    MyAdapter(Context context, ArrayList<ContentDB> list, ArrayList<ArrayList<CommentDB>> commentlist){
        mData = list;
        mcontext = context;
        commentDBS = commentlist;
        Log.d("은하", String.valueOf(mData.size()));
    }

    //onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        this.mcontext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        MyAdapter.ViewHolder vh = new MyAdapter.ViewHolder(view);

        return vh;
    }

    //on BindViewHolder() - Position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        ContentDB contentdb = mData.get(position);
        holder.writer.setText(contentdb.getuserSSN());
        holder.contentstext.setText(contentdb.gettext());
        int like = contentdb.getlike();
        String thislike = Integer.toString(like);
        holder.likes.setText(thislike);
        Log.d("은하", contentdb.getuserSSN()+", "+contentdb.gettext()+", "+contentdb.getlike());
        Button contentslike = holder.contentslike;
        Button contentsdelete = holder.contentsdelete;
        Glide.with(mcontext).load(contentdb.substance).into(holder.contentssubstance);


        //여기 용태씨가 수정하시면 됩니다!
        holder.writer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("은하", "해당 writer의 page로 이동합니다.");
                //SaveSharedPreference visit_hist = new SaveSharedPreference();
                //visit_hist.setVisitName(v.getContext() , holder.writer.getText().toString());
                Intent intent = new Intent(v.getContext(), Yourpage.class );
                intent.putExtra("writer",holder.writer.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
        /*
        //comments를 위한 recyclerview
        holder.commentsrecyclerview.setLayoutManager(new LinearLayoutManager(mcontext));
        //specify an adapter (see also next example)
        Log.d("은하", "in adapter commentDBS =>"+commentDBS);
        Log.d("은하", "commentDBSsize =>"+commentDBS.get(contentdb.SSN));
        if(!commentDBS.get(contentdb.SSN).isEmpty()) {
            CommentsAdapter adapter = new CommentsAdapter(commentDBS.get(contentdb.SSN));
            Log.d("은하", "여기까지왔나?");
            holder.commentsrecyclerview.setAdapter(adapter);
        }
        */

        contentsdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Contents")
                        .document(contentdb.SSN)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("은하", "delete success!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("은하", "Error deleting document", e);
                            }
                        });

            }
        });

        contentslike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("은하", "like 버튼이 클릭된 아이템의 위치 : "+ position);
                final int newlike = like+1;
                holder.likes.setText(Integer.toString(newlike));
                //파이어베이스에 업데이트
                contentdb.setLike(newlike);
                FirebaseFirestore.getInstance().collection("Contents").document(contentdb.getuserSSN())
                        .collection(contentdb.getuserSSN()).document(String.valueOf(contentdb.SSN)).update("like", newlike);
            }
        });
    }

    //getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }
}
