package com.cheng.consultexpert.ui.model;

/**
 * Created by cheng on 2017/11/28.
 */

public interface QuestionModel {
    //void loadQuestionList(String url, int userId, int pageNum, int pageSize, int cateId, int ismine, OnLoadQuestionListListener listener);
    void loadQuestionList (int userId, String url, int pageNum, int pageSize, int isAnswered, int cateId, int ismine, OnLoadQuestionListListener listener);

}
