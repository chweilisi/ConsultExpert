package com.cheng.consultexpert.ui.view;

import com.cheng.consultexpert.db.table.Expert;

import java.util.List;

/**
 * Created by cheng on 2017/12/8.
 */

public interface IMyLoveExpertView {
    void showProgress();

    void addLoveExp(List<Expert> expList);

    void hideProgress();

    void showLoadFailMsg();
}
