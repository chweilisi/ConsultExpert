package com.cheng.consultexpert.ui.presenter;

import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.ui.common.Urls;
import com.cheng.consultexpert.ui.model.MyAnsweredQuestionModel;
import com.cheng.consultexpert.ui.model.MyAnsweredQuestionModelImpl;
import com.cheng.consultexpert.ui.model.OnLoadMyQuestionsListener;
import com.cheng.consultexpert.ui.view.IMyQuestionListView;

import java.util.List;

/**
 * Created by cheng on 2017/12/5.
 */

public class MyAnsweredQuestionPresenterImpl implements MyAnsweredQuestionPresenter {
    private MyAnsweredQuestionModel model;
    private IMyQuestionListView questionListView;

    public MyAnsweredQuestionPresenterImpl(IMyQuestionListView view){
        questionListView = view;
        model = new MyAnsweredQuestionModelImpl();
    }

    @Override
    public void loadMyQuestion(int userId, int pageIndex, int isAnswered) {
        String url = Urls.HOST_TEST + Urls.QUESTION;

        model.loadMyQuestion(userId, url, pageIndex, Urls.PAZE_SIZE, isAnswered, listener);
    }

    private OnLoadMyQuestionsListener listener = new OnLoadMyQuestionsListener() {
        @Override
        public void onSuccess(List<Subject> subjects) {
            questionListView.addData(subjects);
        }

        @Override
        public void onFailed(String msg, Exception e) {

        }
    };

}
