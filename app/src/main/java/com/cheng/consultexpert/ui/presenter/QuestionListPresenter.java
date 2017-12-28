package com.cheng.consultexpert.ui.presenter;

/**
 * Created by cheng on 2017/11/28.
 */
//userId必传，QuestionCate如果不是-1则为查询领域问题，如果是-1，则为查询关注问题。ismine标识是否是@自己的问题
public interface QuestionListPresenter {
    public void loadQuestionList(int userId, int QuestionCate, int pageIndex, int ismine, int isAnswered);
}
