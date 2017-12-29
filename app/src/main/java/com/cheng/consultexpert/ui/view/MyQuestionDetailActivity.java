package com.cheng.consultexpert.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.db.table.SubjectItem;
import com.cheng.consultexpert.ui.adapter.MyQuestionDetailAdapter;
import com.google.gson.Gson;

import java.util.List;

public class MyQuestionDetailActivity extends BaseActivity {

    private Subject mSubject;
    private List<SubjectItem> mQContent;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private MyQuestionDetailAdapter mAdapter;
    private ImageView mSubjectIcon;
    private TextView mSubjectTitle;
    private TextView mSubjectDes;
    private ListView mListView;
    private AnswerAdapter mLvAdapter;
    private Button mBtnAnswer;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_question_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mSubjectTitle = (TextView)findViewById(R.id.tvTitle);
        mSubjectDes = (TextView)findViewById(R.id.tvDesc);


        //mListView = (ListView)findViewById(R.id.list_view);

        //获取activity传递数据
        String data = getIntent().getStringExtra("subject");
        Gson gson = new Gson();
        mSubject = gson.fromJson(data, Subject.class);
        mQContent = mSubject.getItems();

        //设置问题标题 正文
        mSubjectTitle.setText(mSubject.getTitle());
        mSubjectDes.setText(mSubject.getContent());

//        mLvAdapter = new AnswerAdapter();
//
//        mListView.setAdapter(mLvAdapter);

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mSubject = gson.fromJson(data, Subject.class);

        mSubjectTitle.setText(mSubject.getTitle());
        mSubjectDes.setText(mSubject.getContent());

        mQContent = mSubject.getItems();
        if(null != mQContent) {
            mAdapter = new MyQuestionDetailAdapter(this, mQContent);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        //set footerview
        View footer = LayoutInflater.from(mContext).inflate(R.layout.my_question_detail_foot_answer_button, mRecyclerView, false);
        mBtnAnswer = (Button)footer.findViewById(R.id.question_detail_btn_answer);
        mAdapter.setFooterView(footer);
        mAdapter.setHeaderView(null);
        mBtnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyQuestionDetailActivity.this, AnswerQuestionActivity.class);
                intent.putExtra("subjectId", mSubject.getSubjectId());
                startActivityForResult(intent, 10);
            }
        });
    }

    private class AnswerAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mQContent.size();
        }

        @Override
        public Object getItem(int position) {
            return mQContent.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SubjectItem item = mQContent.get(position);
            View view;
            ViewHolder vh;

            if(null == convertView){
                view = LayoutInflater.from(mContext).inflate(R.layout.question_answer_item, null);
                vh = new ViewHolder();
                vh.tvAnswer = (TextView)view.findViewById(R.id.tvDesc);
                view.setTag(vh);
            } else {
                view = convertView;
                vh = (ViewHolder)view.getTag();
            }

            vh.tvAnswer.setText(mQContent.get(position).getContent());
            return view;
        }

        class ViewHolder{
            TextView tvAnswer;
        }
    }

}
