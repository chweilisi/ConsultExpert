package com.cheng.consultexpert.ui.presenter;

import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.db.table.SubjectListItem;
import com.cheng.consultexpert.ui.common.Urls;
import com.cheng.consultexpert.ui.model.QuestionModel;
import com.cheng.consultexpert.ui.model.QuestionModelImpl;
import com.cheng.consultexpert.ui.model.OnLoadQuestionListListener;
import com.cheng.consultexpert.ui.view.IQuestionListView;

import java.util.List;

/**
 * Created by cheng on 2017/11/28.
 */

public class QuestionListPresenterImpl implements QuestionListPresenter {

    private IQuestionListView mIQuestionView;
    private QuestionModel mModel;

    public QuestionListPresenterImpl(IQuestionListView IExpView) {
        this.mIQuestionView = IExpView;
        mModel = new QuestionModelImpl();
    }

    //userId必传，QuestionCate如果不是-1则为查询领域问题，如果是-1，则为查询需要回答的问题
    @Override
    public void loadQuestionList(int userId, int questionCate, int pageIndex, int ismine, int isAnswered) {
        String url = getUrl(questionCate);

        //只有第一页的或者刷新的时候才显示刷新进度条
        if(pageIndex == 0) {
            mIQuestionView.showProgress();
        }
        //other method to implement
        //mNewsModel.loadNews(url, type, this);
        //replace implement class interface
        mModel.loadQuestionList(userId, url, pageIndex, Urls.PAZE_SIZE,isAnswered, questionCate, ismine, loadQuestionListener);
    }

    OnLoadQuestionListListener loadQuestionListener = new OnLoadQuestionListListener(){
        @Override
        public void onSuccess(List<SubjectListItem> list) {
            mIQuestionView.hideProgress();
            mIQuestionView.addQuestions(list);
        }

        @Override
        public void onFailure(String msg, Exception e) {
            mIQuestionView.hideProgress();
            mIQuestionView.showLoadFailMsg();
        }
    };

    //如果questionCate不为-1，则为查询领域问题，否则查询需要回答的问题
    private String getUrl(int questionCate){
        String result = "";
        if(-1 == questionCate){
            result = Urls.HOST_TEST + Urls.FORUM;
        }else {
            result = Urls.HOST_TEST + Urls.FORUM;
        }
        return result;
    }

    private String getNeedAnswerQuestionUrl(){
        return Urls.HOST_TEST + Urls.LOVEEXPERT;
    }

    private String getCategoryQuestionUrl(){
        return Urls.HOST_TEST + Urls.EXPERT;
    }

}
