package com.cheng.consultexpert.ui.model;

import com.cheng.consultexpert.db.table.Subject;

import java.util.List;

/**
 * Created by cheng on 2017/11/28.
 */

public interface OnLoadQuestionListListener {
    void onSuccess(List<Subject> list);

    void onFailure(String msg, Exception e);
}
