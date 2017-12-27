package com.cheng.consultexpert.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.db.table.Expert;
import com.cheng.consultexpert.db.table.Subject;

import java.util.List;

/**
 * Created by cheng on 2017/12/5.
 */

public class NeedAnswerQuestionListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Subject> mDatas;
    private onQuestionItemClickListener mItemClickListener;

    public NeedAnswerQuestionListAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Subject> subjects){
        this.mDatas = subjects;
        this.notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expert_item, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            Subject exp = mDatas.get(position);
            if(exp == null) return;

            ((ItemViewHolder) holder).mTitle.setText(exp.getTitle());
            ((ItemViewHolder) holder).mDesc.setText(exp.getContent());
            //((ItemViewHolder) holder).mImg.setImageDrawable(mContext.getDrawable(R.drawable.mypage_concerned_teacher_icon));
            //ImageLoaderUtils.display(mContext, (((ItemViewHolder) holder).mImg), exp.getImgSrc());
        }

    }

    @Override
    public int getItemCount() {
        if(null != mDatas){
            return mDatas.size();
        }
        return 0;
    }

    public Subject getQuestionItem(int position){
        return (null == mDatas) ? null : mDatas.get(position);
    }

    public interface onQuestionItemClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnQuestionListItemClickListener(onQuestionItemClickListener listener){
        this.mItemClickListener = listener;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        //public CircularImage mImg;
        public TextView mTitle;
        public TextView mDesc;
        public ItemViewHolder(View itemView) {
            super(itemView);
            //mImg = (CircularImage)itemView.findViewById(R.id.ivExpert);
            mTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            mDesc = (TextView)itemView.findViewById(R.id.tvDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });

        }
    }
}
