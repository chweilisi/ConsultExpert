package com.cheng.consultexpert.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.db.table.SubjectItem;

import java.util.List;

/**
 * Created by cheng on 2017/12/7.
 */

public class MyQuestionDetailAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<SubjectItem> mData;

    public MyQuestionDetailAdapter(Context context, List<SubjectItem> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_answer_item, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){

            //((ItemViewHolder) holder).mImg.setBackgroundResource(R.drawable.ic_answer);

            ((ItemViewHolder) holder).mDesc.setText(mData.get(position).getContent());

            //((ItemViewHolder) holder).mTitle.setText(exp.getName());
            //((ItemViewHolder) holder).mDesc.setText(exp.getDes());
            //ImageLoaderUtils.display(mContext, (((ItemViewHolder) holder).mImg), exp.getImgSrc());
        }
    }

    @Override
    public int getItemCount() {
        return ((mData.size() == 0) ? 1 : mData.size());
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImg;
//        public TextView mTitle;
        public TextView mDesc;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView)itemView.findViewById(R.id.ivSubject);
//            mTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            mDesc = (TextView)itemView.findViewById(R.id.tvDesc);
        }
    }
}
