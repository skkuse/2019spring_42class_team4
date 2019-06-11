package com.example.docking_milkyway;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private ArrayList<CommentDB> mData = null;

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userid, likes, commentstext;
        Button commentlike, co_comment;

        ViewHolder(View itemView) {
            super(itemView);

            //뷰 객체에 대한 참조. (hold strong reference)
            userid = itemView.findViewById(R.id.commentuser);
            likes = itemView.findViewById(R.id.commentlike);
            commentstext = itemView.findViewById(R.id.commentstext);
            commentlike = itemView.findViewById(R.id.commentlikebutton);
            co_comment = itemView.findViewById(R.id.co_comment);
        }
    }

    //여기부터해야함!
    //recyclerbutton 처리 어떻게?
    //생성자에서 데이터 리스트 객체를 전달받음.
    CommentsAdapter(ArrayList<CommentDB> list){
        mData = list;
        Log.d("은하", String.valueOf(mData.size()));
    }

    //onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        CommentsAdapter.ViewHolder vh = new CommentsAdapter.ViewHolder(view);

        return vh;
    }

    //on BindViewHolder() - Position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(CommentsAdapter.ViewHolder holder, int position) {
        CommentDB commentdb = mData.get(position);
        holder.userid.setText(commentdb.getUser_SSN());
        holder.commentstext.setText(commentdb.getSubstance());
        String thislike = Integer.toString(commentdb.getLike());
        holder.likes.setText("like " +thislike);
        Log.d("은하", commentdb.getUser_SSN()+", "+commentdb.getSubstance()+", "+commentdb.getLike());
    }

    //getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }
}
