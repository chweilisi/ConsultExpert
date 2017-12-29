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

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    private View mHeaderView = null;
    private View mFooterView = null;

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView) {
        if(null != headerView){
            mHeaderView = headerView;
            notifyItemInserted(0);
        }
    }
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        if(null != footerView){
            mFooterView = footerView;
            notifyItemInserted(getItemCount()-1);
        }
    }

    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    * */
    @Override
    public int getItemViewType(int position) {
        int count = getItemCount();
        if (mHeaderView == null && mFooterView == null){
            return TYPE_NORMAL;
        }
        //根据有没有header footer判断返回类型
        if (position == 0){
            if(null != mHeaderView){
                return TYPE_HEADER;
            } else {
                return TYPE_NORMAL;
            }
        }

        if (position == count - 1){
            if(null != mFooterView){
                return TYPE_FOOTER;
            }else {
                return TYPE_NORMAL;
            }
        }
        return TYPE_NORMAL;
    }


    public MyQuestionDetailAdapter(Context context, List<SubjectItem> data) {
        this.mContext = context;
        this.mData = data;
    }

    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new ItemViewHolder(mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new ItemViewHolder(mFooterView);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_answer_item, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){//此处没有判断是否有header，所以mData取值就是position，如果有header，则需要position-1
            if(holder instanceof ItemViewHolder){
                String des = mData.get(position).getContent();
                ((ItemViewHolder) holder).mDesc.setText(des);
                return;
            }
            return;
        }else if(getItemViewType(position) == TYPE_HEADER){
            return;
        }else{
            return;
        }


//        if(holder instanceof ItemViewHolder){
//
//            //((ItemViewHolder) holder).mImg.setBackgroundResource(R.drawable.ic_answer);
//
//            ((ItemViewHolder) holder).mDesc.setText(mData.get(position).getContent());
//
//            //((ItemViewHolder) holder).mTitle.setText(exp.getName());
//            //((ItemViewHolder) holder).mDesc.setText(exp.getDes());
//            //ImageLoaderUtils.display(mContext, (((ItemViewHolder) holder).mImg), exp.getImgSrc());
//        }
    }

//    @Override
//    public int getItemCount() {
//        return ((mData.size() == 0) ? 1 : mData.size());
//    }

    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null){
            return mData.size();
        }else if(mHeaderView == null && mFooterView != null){
            return mData.size() + 1;
        }else if (mHeaderView != null && mFooterView == null){
            return mData.size() + 1;
        }else {
            return mData.size() + 2;
        }
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
