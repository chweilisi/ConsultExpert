package com.cheng.consultexpert.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.ui.adapter.QuestionListAdapter;
import com.cheng.consultexpert.db.table.Expert;
import com.cheng.consultexpert.ui.presenter.QuestionListPresenter;
import com.cheng.consultexpert.ui.presenter.QuestionListPresenterImpl;
import com.cheng.consultexpert.utils.PreUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class QuestionListActivity extends BaseActivity implements IQuestionListView, SwipeRefreshLayout.OnRefreshListener {

    private String mToolbarTitle;
    private int    mQuestionCategory;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout mSwipe;
    private QuestionListAdapter mAdapter;
    private List<Subject> mData;
    private int mPageIndex = 0;
    private QuestionListPresenter mPresenter;
    private PreUtils pre;
    private int mUserId;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_expert_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mUserId = new Long(mApplication.mUserId).intValue();
        mToolbarTitle = getIntent().getStringExtra("cat");
        mQuestionCategory = getIntent().getIntExtra("position", 0);

        mPresenter = new QuestionListPresenterImpl(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        mSwipe.setOnRefreshListener(this);

        setSupportActionBar(mToolbar);

        mRecycleView = (RecyclerView)findViewById(R.id.recycle_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLinearLayoutManager);

        getSupportActionBar().setTitle(mToolbarTitle);

        mAdapter = new QuestionListAdapter(getApplicationContext());
        mRecycleView.setAdapter(mAdapter);

        mAdapter.setOnExpListItemClickListener(listener);

        mRecycleView.addOnScrollListener(mOnScrollListener);

        onRefresh();
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
                mPresenter.loadQuestionList(mUserId, mQuestionCategory, mPageIndex, 0);
            }
        }
    };

    private QuestionListAdapter.onExpListItemClickListener listener = new QuestionListAdapter.onExpListItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mData.size() <= 0) {
                return;
            }
            Subject sub = mAdapter.getExpItem(position);
            //String id = expert.getId();
            Intent intent = new Intent(QuestionListActivity.this, QuestionDetailActivity.class);
            Gson gson = new Gson();
            String data = gson.toJson(mData.get(position));
            intent.putExtra("question", data);

            startActivity(intent);
//            View transitionView = view.findViewById(R.id.ivExpert);
//            ActivityOptionsCompat options =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(ExpertListActivity.this,
//                            transitionView, getString(R.string.transition_news_img));
//
//            ActivityCompat.startActivity(getApplicationContext(), intent, options.toBundle());
        }
    };

    @Override
    public void onRefresh() {
//        if(mData != null) {
//            mData.clear();
//        }
        mPresenter.loadQuestionList(mUserId, mQuestionCategory, mPageIndex, 0);
    }

    @Override
    public void showProgress() {
        mSwipe.setRefreshing(true);
    }

    @Override
    public void addQuestions(List<Subject> experts) {
        if(mData == null) {
            mData = new ArrayList<Subject>();
        }
        mData.addAll(experts);
        if(mPageIndex == 0) {
            mAdapter.setData(mData);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        //mPageIndex += Urls.PAZE_SIZE;
        //mPageIndex++;
    }

    @Override
    public void hideProgress() {
        mSwipe.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {

    }
}
