package com.cheng.consultexpert.ui.model;

import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.ui.common.Urls;
import com.cheng.consultexpert.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheng on 2017/11/28.
 */

public class QuestionModelImpl implements QuestionModel {
    @Override
    /*
    public void loadExpertList(String url, final int type, final OnLoadExpertsListListener listener) {
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                List<Expert> ExpertList = ExpertJsonUtils.readJsonExpertList(response);
                listener.onSuccess(ExpertList);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news list failure.", e);
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }
    */
    public void loadQuestionList(String url, int userId, int pageNum, int pageSize, int cateId, int ismine, final OnLoadQuestionListListener listener) {
        OkHttpUtils.ResultCallback<String> loadQuestionCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
                List<Subject> subjects = gson.fromJson(response, new TypeToken<List<Subject>>() {}.getType());
                listener.onSuccess(subjects);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news list failure.", e);
            }
        };

        List<OkHttpUtils.Param> params = new ArrayList<>();
        try {
            OkHttpUtils.Param user = new OkHttpUtils.Param("userId", Integer.toString(userId));
            OkHttpUtils.Param expertCategory = new OkHttpUtils.Param("cateId", Integer.toString(cateId));
            OkHttpUtils.Param pagenum = new OkHttpUtils.Param("pagenum", Integer.toString(pageNum));
            OkHttpUtils.Param pagesize = new OkHttpUtils.Param("pagesize", Integer.toString(pageSize));
            OkHttpUtils.Param ismy = new OkHttpUtils.Param("isMine", String.valueOf(ismine));
            OkHttpUtils.Param mothed = new OkHttpUtils.Param("method","list");

            params.add(user);
            params.add(expertCategory);
            params.add(pagenum);
            params.add(pagesize);
            params.add(ismy);
            params.add(mothed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpUtils.post(url, loadQuestionCallback, params);
    }

}
