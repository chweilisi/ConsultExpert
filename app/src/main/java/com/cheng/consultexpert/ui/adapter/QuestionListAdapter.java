package com.cheng.consultexpert.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.db.table.Expert;
import com.cheng.consultexpert.db.table.Subject;

import java.util.List;

/**
 * Created by cheng on 2017/11/28.
 */

public class QuestionListAdapter extends RecyclerView.Adapter {

    private onExpListItemClickListener mItemClickListener;
    private List<Subject> mData;
    private Context mContext;

    public QuestionListAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Subject> data){
        this.mData = data;
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
            Subject exp = mData.get(position);
            if(exp == null) return;

            ((ItemViewHolder) holder).mTitle.setText(exp.getTitle());
            ((ItemViewHolder) holder).mDesc.setText(exp.getContent());
            //ImageLoaderUtils.display(mContext, (((ItemViewHolder) holder).mImg), exp.getImgSrc());
        }

    }

    @Override
    public int getItemCount() {
        if(null != mData){
            return mData.size();
        }
        return 0;
    }

    public Subject getExpItem(int position){
        return (null == mData) ? null : mData.get(position);
    }

    public interface onExpListItemClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnExpListItemClickListener(onExpListItemClickListener listener){
        this.mItemClickListener = listener;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImg;
        public TextView mTitle;
        public TextView mDesc;
        public ItemViewHolder(View itemView) {
            super(itemView);
            //mImg = (ImageView)itemView.findViewById(R.id.ivExpert);
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
