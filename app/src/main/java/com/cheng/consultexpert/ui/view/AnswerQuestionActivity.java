package com.cheng.consultexpert.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.ui.common.Urls;
import com.cheng.consultexpert.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

public class AnswerQuestionActivity extends BaseActivity {

    private EditText answer_content;
    private Button mSubmit;
    private String mAnswer;
    private Long mSubjectId;
    private List<OkHttpUtils.Param> paramList;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_answer_question_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSubjectId = getIntent().getLongExtra("subjectId", -1);
        answer_content = (EditText)findViewById(R.id.answer_content);

        answer_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(answer_content, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        mSubmit = (Button)findViewById(R.id.submit);

        paramList = new ArrayList<>();
        //add user id
        String id = String.valueOf(mApplication.mUserInfo.getUserId());
        OkHttpUtils.Param userId = new OkHttpUtils.Param("userId", id);
        paramList.add(userId);

        //add subjectid
        OkHttpUtils.Param subjectid = new OkHttpUtils.Param("subjectid", String.valueOf(mSubjectId));
        paramList.add(subjectid);

        //add post parameter question des
        mAnswer = answer_content.getText().toString().trim();
        OkHttpUtils.Param answer = new OkHttpUtils.Param("answerContent", mAnswer);
        paramList.add(answer);

        OkHttpUtils.Param method = new OkHttpUtils.Param("method", "save");
        paramList.add(method);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO submit question
                String url = Urls.HOST_TEST + Urls.QUESTION;
                OkHttpUtils.post(url, null, paramList);
                //finish self
                finish();
            }
        });
    }

}
