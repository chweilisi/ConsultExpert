package com.cheng.consultexpert.ui.presenter;

/**
 * Created by cheng on 2017/12/5.
 */
//isAnswered标识是否回答
public interface MyAnsweredQuestionPresenter {
    public void loadMyQuestion(int userId, int pageIndex, int isAnswered);
}
