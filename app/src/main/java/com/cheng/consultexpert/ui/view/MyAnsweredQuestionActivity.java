package com.cheng.consultexpert.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.db.table.SubjectListItem;
import com.cheng.consultexpert.ui.adapter.MyConsultQuestionAdapter;
import com.cheng.consultexpert.ui.common.Constants;
import com.cheng.consultexpert.ui.common.PostCommonHead;
import com.cheng.consultexpert.ui.common.PostResponseBodyJson;
import com.cheng.consultexpert.ui.common.Urls;
import com.cheng.consultexpert.ui.presenter.MyAnsweredQuestionPresenter;
import com.cheng.consultexpert.ui.presenter.MyAnsweredQuestionPresenterImpl;
import com.cheng.consultexpert.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyAnsweredQuestionActivity extends BaseActivity implements IMyQuestionListView {

    private RecyclerView mRecyclerView;
    private List<SubjectListItem> mSubjects;
    private LinearLayoutManager mLinearLayoutManager;
    private MyConsultQuestionAdapter mAdapter;
    private MyAnsweredQuestionPresenter mQuestionPresenter;
    private int mPageIndex = 0;
    private int mUserId;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_consult_question;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MyConsultQuestionAdapter(this);
        mUserId = new Long(mApplication.mUserInfo.getUserId()).intValue();

        initView();
    }

    private void initView(){
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mQuestionPresenter = new MyAnsweredQuestionPresenterImpl(this);
        mAdapter.setOnQuestionListItemClickListener(listener);

        mRecyclerView.addOnScrollListener(mOnScrollListener);

        //add data
        refreshView();
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()) {

                mPageIndex = mPageIndex + 1;
                //回答的问题cateId:-1，ismine:1, isAnswered:1
                mQuestionPresenter.loadMyQuestion(mUserId, -1, mPageIndex, 1, 1);
            }
        }
    };

    private void refreshView(){
        if(null != mSubjects) {
            mSubjects.clear();
        }
        //回答的问题cateId:-1，ismine:1, isAnswered:1
        mQuestionPresenter.loadMyQuestion(mUserId, -1, mPageIndex, 1, 1);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void addData(List<SubjectListItem> subjects) {
        if(null == mSubjects) {
            mSubjects = new ArrayList<SubjectListItem>();
        }
        mSubjects.addAll(subjects);
        if(mPageIndex == 0) {
            mAdapter.setData(mSubjects);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showLoadFailMsg() {

    }

    Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

    private MyConsultQuestionAdapter.onQuestionListItemClickListener listener = new MyConsultQuestionAdapter.onQuestionListItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            final int pos = position;
            OkHttpUtils.ResultCallback<String> QuestionDetailResultCallback = new OkHttpUtils.ResultCallback<String>() {
                @Override
                public void onSuccess(String response) {
                    PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
                    boolean issuccessed = result.getResultCode().equalsIgnoreCase(Constants.LOGIN_OR_POST_SUCCESS);
                    if(issuccessed){
                        Intent intent = new Intent(mContext, QuestionDetailActivity.class);
                        String questionStr = result.getResultJson().trim();
                        intent.putExtra("questiondetail", questionStr);
                        startActivity(intent);
                    }else if (result.getResultCode().trim().equalsIgnoreCase(Constants.SYSTEM_ERROR_PROGRAM)){
                        Toast toast = Toast.makeText(mContext, "ErrorCode = "+ result.getResultCode() + " "
                                + getResources().getString(R.string.question_detail_hint_app_error) + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else if (result.getResultCode().trim().equalsIgnoreCase(Constants.SYSTEM_ERROR_SERVER)){
                        Toast toast = Toast.makeText(mContext, "ErrorCode = "+ result.getResultCode() + " "
                                + getResources().getString(R.string.question_detail_hint_server_error) + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            };

            //json格式post参数
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = dateFormat.format(date).toString();

            String url = Urls.HOST_TEST + Urls.FORUM;
            PostCommonHead.HEAD beanHead = new PostCommonHead.HEAD("1", "subjectView", mApplication.mAppSignature, dateStr, "9000");
            QuestionDetailPostBean param = new QuestionDetailPostBean(beanHead, String.valueOf(mSubjects.get(pos).getSubjectId()), "0", "20");
            String strParam = gson.toJson(param);
            OkHttpUtils.postJson(url, QuestionDetailResultCallback, strParam);

//            Intent intent = new Intent(MyAnsweredQuestionActivity.this, MyQuestionDetailActivity.class);
//            Gson gson = new Gson();
//            String data = gson.toJson(mSubjects.get(position));
//            intent.putExtra("subject", data);
//            startActivity(intent);
        }
    };

    class QuestionDetailPostBean{
        private PostCommonHead.HEAD head;
        private PostBody body;

        public QuestionDetailPostBean(PostCommonHead.HEAD head, String subjectId, String pageNum, String pageSize) {
            this.head = head;
            this.body = new PostBody(subjectId, pageNum, pageSize);
        }

        class PostBody{
            private String subjectId;
            private String pageNum;
            private String pageSize;

            public PostBody(String subjectId, String pageNum, String pageSize) {
                this.subjectId = subjectId;
                this.pageNum = pageNum;
                this.pageSize = pageSize;
            }
        }
    }
}
