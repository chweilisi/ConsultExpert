package com.cheng.consultexpert.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.db.table.SubjectListItem;

import java.util.List;

/**
 * Created by cheng on 2017/11/28.
 */

public class QuestionListAdapter extends RecyclerView.Adapter {

    private onExpListItemClickListener mItemClickListener;
    private List<SubjectListItem> mData;
    private Context mContext;

    public QuestionListAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<SubjectListItem> data){
        this.mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list_item, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            SubjectListItem item = mData.get(position);
            if(item == null) return;

            String[] good = mContext.getResources().getStringArray(R.array.consult_category);
            for (int i = 10; i < 23; i++){
                if(item.getQuestionCateId().trim().equalsIgnoreCase(String.valueOf(i))){
                    ((ItemViewHolder) holder).mCate.setText(good[i - 10]);
                    break;
                }
            }

            ((ItemViewHolder) holder).mTitle.setText(item.getTitle());
            ((ItemViewHolder) holder).mtime.setText(item.getAnsweredTime());
        }
    }

    @Override
    public int getItemCount() {
        if(null != mData){
            return mData.size();
        }
        return 0;
    }

    public SubjectListItem getExpItem(int position){
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
        public TextView mCate;
        public TextView mtime;
        //public TextView mDesc;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView)itemView.findViewById(R.id.ivSubject);
            mCate = (TextView)itemView.findViewById(R.id.question_category);
            mTitle = (TextView)itemView.findViewById(R.id.question_title);
            mtime = (TextView)itemView.findViewById(R.id.question_time);

            //mDesc = (TextView)itemView.findViewById(R.id.tvDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });

        }
    }
}
