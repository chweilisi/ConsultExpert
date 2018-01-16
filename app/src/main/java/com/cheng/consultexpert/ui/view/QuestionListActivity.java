package com.cheng.consultexpert.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.db.table.SubjectListItem;
import com.cheng.consultexpert.ui.adapter.QuestionListAdapter;
import com.cheng.consultexpert.db.table.Expert;
import com.cheng.consultexpert.ui.common.Constants;
import com.cheng.consultexpert.ui.common.PostCommonHead;
import com.cheng.consultexpert.ui.common.PostResponseBodyJson;
import com.cheng.consultexpert.ui.common.Urls;
import com.cheng.consultexpert.ui.presenter.QuestionListPresenter;
import com.cheng.consultexpert.ui.presenter.QuestionListPresenterImpl;
import com.cheng.consultexpert.utils.OkHttpUtils;
import com.cheng.consultexpert.utils.PreUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private List<SubjectListItem> mData;
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
        mQuestionCategory = getIntent().getIntExtra("position", 0) + 10;

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
                //领域问题查询，userId = -1，cateId要传领域，ismine:0，isAnswered:0
                mPresenter.loadQuestionList(-1, mQuestionCategory, mPageIndex, 0, 0);
            }
        }
    };

    Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
    private QuestionListAdapter.onExpListItemClickListener listener = new QuestionListAdapter.onExpListItemClickListener() {
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
    public void onRefresh() {
//        if(mData != null) {
//            mData.clear();
//        }
        //领域问题查询，userId = -1，cateId要传领域，ismine:0，isAnswered:0
        mPresenter.loadQuestionList(-1, mQuestionCategory, mPageIndex, 0, 0);
    }

    @Override
    public void showProgress() {
        mSwipe.setRefreshing(true);
    }

    @Override
    public void addQuestions(List<SubjectListItem> experts) {
        if(mData == null) {
            mData = new ArrayList<SubjectListItem>();
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
