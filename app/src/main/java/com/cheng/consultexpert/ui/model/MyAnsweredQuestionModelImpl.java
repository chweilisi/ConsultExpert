package com.cheng.consultexpert.ui.model;

import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheng on 2017/12/5.
 */

public class MyAnsweredQuestionModelImpl implements MyAnsweredQuestionModel {
    @Override
    public void loadMyQuestion(int userId, String url, int pageNum, int pageSize, int isAnswered, final OnLoadMyQuestionsListener listener) {
        OkHttpUtils.ResultCallback<String> loadMyQuestionCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
                List<Subject> subjects = gson.fromJson(response, new TypeToken<List<Subject>>() {}.getType());
                listener.onSuccess(subjects);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailed("load news list failure.", e);
            }
        };

        List<OkHttpUtils.Param> params = new ArrayList<>();
        try {
            OkHttpUtils.Param user = new OkHttpUtils.Param("userId", String.valueOf(userId));
            OkHttpUtils.Param pagenum = new OkHttpUtils.Param("pagenum", Integer.toString(pageNum));
            OkHttpUtils.Param pagesize = new OkHttpUtils.Param("pagesize", Integer.toString(pageSize));
            OkHttpUtils.Param answered = new OkHttpUtils.Param("isAnswered", String.valueOf(isAnswered));
            OkHttpUtils.Param cateid = new OkHttpUtils.Param("cateId", "-1");
            OkHttpUtils.Param ismy = new OkHttpUtils.Param("isMine", "1");
            OkHttpUtils.Param mothed = new OkHttpUtils.Param("method","list");

            params.add(user);
            params.add(pagenum);
            params.add(pagesize);
            params.add(answered);
            params.add(cateid);
            params.add(ismy);
            params.add(mothed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpUtils.post(url, loadMyQuestionCallback, params);
    }
}
