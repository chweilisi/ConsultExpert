package com.cheng.consultexpert.ui.model;

import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.db.table.SubjectListItem;

import java.util.List;

/**
 * Created by cheng on 2017/11/28.
 */

public interface OnLoadQuestionListListener {
    void onSuccess(List<SubjectListItem> list);
    void onFailure(String msg, Exception e);
}
