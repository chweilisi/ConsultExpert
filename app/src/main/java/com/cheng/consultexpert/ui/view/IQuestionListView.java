package com.cheng.consultexpert.ui.view;

import com.cheng.consultexpert.db.table.Expert;
import com.cheng.consultexpert.db.table.Subject;

import java.util.List;

/**
 * Created by cheng on 2017/11/28.
 */

public interface IQuestionListView {
    void showProgress();

    void addQuestions(List<Subject> questionList);

    void hideProgress();

    void showLoadFailMsg();
}
