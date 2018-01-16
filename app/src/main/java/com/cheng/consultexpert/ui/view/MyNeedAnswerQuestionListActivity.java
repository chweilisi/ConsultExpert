package com.cheng.consultexpert.ui.view;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.db.table.SubjectListItem;
import com.cheng.consultexpert.ui.adapter.NeedAnswerQuestionListAdapter;
import com.cheng.consultexpert.ui.common.Constants;
import com.cheng.consultexpert.ui.common.PostCommonHead;
import com.cheng.consultexpert.ui.common.PostResponseBodyJson;
import com.cheng.consultexpert.ui.common.Urls;
import com.cheng.consultexpert.ui.presenter.QuestionListPresenter;
import com.cheng.consultexpert.ui.presenter.QuestionListPresenterImpl;
import com.cheng.consultexpert.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyNeedAnswerQuestionListActivity extends BaseActivity implements IQuestionListView, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private NeedAnswerQuestionListAdapter mAdapter;
    private List<SubjectListItem> mData;
    private QuestionListPresenter mPresenter;

    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipe;

    //应该从登陆信息中读取，临时赋值
    private int mUserId = -1;
    private int mQuestionCat = -1;
    private int mPageIndex = 0;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_love_expert_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mUserId = new Long(mApplication.mUserInfo.getUserId()).intValue();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        mSwipe.setOnRefreshListener(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.myfragment_my_love_question_tip));
        mPresenter = new QuestionListPresenterImpl(this);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new NeedAnswerQuestionListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mAdapter.setOnQuestionListItemClickListener(listener);

        onRefresh();
    }

//    private void initView(){
//        if(mData != null) {
//            mData.clear();
//        }
//        //TODO http or db to query data
//        mPresenter.loadExpertList(mUserId, -1, mPageIndex);
//    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                //需要回答的问题，mQuestionCat:-1，ismine:1，isAnswered:0
                mPageIndex = mPageIndex + 1;
                mPresenter.loadQuestionList(mUserId, mQuestionCat, mPageIndex, 1, 0);
            }
        }
    };

    Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

    private NeedAnswerQuestionListAdapter.onQuestionItemClickListener listener = new NeedAnswerQuestionListAdapter.onQuestionItemClickListener() {
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
            QuestionDetailPostBean param = new QuestionDetailPostBean(beanHead, String.valueOf(mData.get(pos).getSubjectId()), "0", "20");
            String strParam = gson.toJson(param);
            OkHttpUtils.postJson(url, QuestionDetailResultCallback, strParam);
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

    @Override
    public void showProgress() {
        mSwipe.setRefreshing(true);
    }

    @Override
    public void addQuestions(List<SubjectListItem> subjects) {
        if(mData == null) {
            mData = new ArrayList<SubjectListItem>();
        }
        mData.addAll(subjects);
        if(mPageIndex == 0) {
            mAdapter.setData(mData);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        //mPageIndex += Urls.PAZE_SIZE;
    }

    @Override
    public void hideProgress() {
        mSwipe.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {

    }

    @Override
    public void onRefresh() {
//        if(mData != null) {
//            mData.clear();
//        }
        //需要回答的问题，mQuestionCat:-1，ismine:1，isAnswered:0
        mPresenter.loadQuestionList(mUserId, mQuestionCat, mPageIndex, 1, 0);
    }
}
