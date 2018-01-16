package com.cheng.consultexpert.ui.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.db.table.SubjectItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AnswerItemDetailActivity extends BaseActivity {
    private TextView mAnswerDetail;
    private String mAnswer;
    private int mItemType = -1;
    private Subject mSubject;
    private SubjectItem mAnswerItem;
    private String mQuestionDes;
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_answer_item_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAnswerDetail = (TextView)findViewById(R.id.tv_answer_item_detail_page);

        String answerItem = getIntent().getStringExtra("answer_item");
        if(answerItem != null && !answerItem.isEmpty()){
            mAnswerItem = gson.fromJson(answerItem, SubjectItem.class);

            mAnswer = mAnswerItem.getContent();
            if(mAnswer != null && !mAnswer.isEmpty()){
                mAnswerDetail.setText(mAnswer);
            }

            mItemType = mAnswerItem.getItemType();
            if(0 == mItemType){
                getSupportActionBar().setTitle("问题详情");
            } else {
                getSupportActionBar().setTitle("回答详情");
            }
        }

        mQuestionDes = getIntent().getStringExtra("question_description");

        if(mQuestionDes != null && !mQuestionDes.isEmpty()){
            getSupportActionBar().setTitle("问题详情");
            mAnswerDetail.setText(mQuestionDes);
        }

    }
}
