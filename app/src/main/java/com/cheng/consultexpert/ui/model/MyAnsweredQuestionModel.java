package com.cheng.consultexpert.ui.model;

/**
 * Created by cheng on 2017/12/5.
 */

public interface MyAnsweredQuestionModel {
    public void loadMyQuestion(int userId, String url, int pageNum, int pageSize, int isAnswered, OnLoadMyQuestionsListener listener);
}
