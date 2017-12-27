package com.cheng.consultexpert.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.db.table.Subject;
import com.google.gson.Gson;

public class QuestionDetailActivity extends BaseActivity {
    private Button mAnswerButton;
    private Subject mSubject;
    private TextView mQuestionCate;
    private TextView mQuestionTitle;
    private TextView mQuestionDes;


    @Override
    protected int getContentViewLayoutId() {
        return R.layout.question_detail_layout;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mQuestionCate = (TextView)findViewById(R.id.question_category);
        mQuestionTitle = (TextView)findViewById(R.id.question_title);
        mQuestionDes = (TextView)findViewById(R.id.question_des);
        mAnswerButton = (Button)findViewById(R.id.answer);

        //获取activity传递数据
        String data = getIntent().getStringExtra("question");
        Gson gson = new Gson();
        mSubject = gson.fromJson(data, Subject.class);

        //set data
        int cateInt = mSubject.getQuestionCate();
        String cate = "";
        String cat[] = getResources().getStringArray(R.array.consult_category);
        for(int i = 0; i < cat.length; i++){
            if(cateInt == i){
                cate = cat[i];
            }
        }
        mQuestionCate.setText(cate);
        mQuestionTitle.setText(mSubject.getTitle());
        mQuestionDes.setText(mSubject.getContent());
        final Long subjectId = mSubject.getSubjectId();

        mAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionDetailActivity.this, AnswerQuestionActivity.class);
                intent.putExtra("subjectId", subjectId);
                startActivity(intent);
            }
        });
    }

}
