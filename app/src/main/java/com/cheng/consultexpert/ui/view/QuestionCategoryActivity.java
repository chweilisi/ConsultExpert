package com.cheng.consultexpert.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.ui.adapter.QuestionCategoryActivityAdapter;


public class QuestionCategoryActivity extends BaseActivity {
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLayoutManager;
    private QuestionCategoryActivityAdapter mAdapter;
    private String[] mConsultCategory;
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_expert_category;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mRecycleView = (RecyclerView)findViewById(R.id.recycle_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);

        mAdapter = new QuestionCategoryActivityAdapter(this);
        //mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnCategoryItemClickListener(listener);
        initView();
    }

    private void initView(){
        mConsultCategory = getResources().getStringArray(R.array.consult_category);
        mAdapter.setData(mConsultCategory);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    private QuestionCategoryActivityAdapter.categoryItemClickListener listener = new QuestionCategoryActivityAdapter.categoryItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            //TODO enter category

//            Intent intent = new Intent(ExpertCategoryActivity.class, ExpertListActivity.class);
            Intent intent = new Intent(QuestionCategoryActivity.this, QuestionListActivity.class);
            intent.putExtra("cat",mConsultCategory[position]);
            intent.putExtra("position",position);
            startActivity(intent);
//            if (getContext() instanceof Activity) {
//                ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
//            }
        }
    };

}
