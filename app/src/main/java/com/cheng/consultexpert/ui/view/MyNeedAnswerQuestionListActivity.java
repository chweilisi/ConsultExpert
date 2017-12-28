package com.cheng.consultexpert.ui.view;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.ui.adapter.NeedAnswerQuestionListAdapter;
import com.cheng.consultexpert.ui.common.Urls;
import com.cheng.consultexpert.ui.presenter.QuestionListPresenter;
import com.cheng.consultexpert.ui.presenter.QuestionListPresenterImpl;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MyNeedAnswerQuestionListActivity extends BaseActivity implements IQuestionListView, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private NeedAnswerQuestionListAdapter mAdapter;
    private List<Subject> mData;
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

    private NeedAnswerQuestionListAdapter.onQuestionItemClickListener listener = new NeedAnswerQuestionListAdapter.onQuestionItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mData.size() <= 0) {
                return;
            }
            Subject subject = mAdapter.getQuestionItem(position);
            Long id = subject.getSubjectId();



            Intent intent = new Intent(MyNeedAnswerQuestionListActivity.this, QuestionDetailActivity.class);
            //intent.putExtra("id", id);

            Gson gson = new Gson();
            String data = gson.toJson(mData.get(position));
            intent.putExtra("question", data);

            startActivity(intent);
        }
    };

    @Override
    public void showProgress() {
        mSwipe.setRefreshing(true);
    }

    @Override
    public void addQuestions(List<Subject> subjects) {
        if(mData == null) {
            mData = new ArrayList<Subject>();
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
