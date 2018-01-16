package com.cheng.consultexpert.ui.model;

import com.cheng.consultexpert.app.App;
import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.db.table.SubjectListItem;
import com.cheng.consultexpert.ui.common.PostCommonHead;
import com.cheng.consultexpert.ui.common.PostResponseBodyJson;
import com.cheng.consultexpert.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cheng.consultexpert.app.App.getApplication;

/**
 * Created by cheng on 2017/12/5.
 */

public class MyAnsweredQuestionModelImpl implements MyAnsweredQuestionModel {
    private PostResponseBodyJson mPostResult;

    @Override
    public void loadMyQuestion(int userId, String url, int pageNum, int pageSize, int isAnswered, int cateId, int ismine, final OnLoadQuestionListListener listener) {
        OkHttpUtils.ResultCallback<String> loadMyQuestionCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
                mPostResult = gson.fromJson(response, PostResponseBodyJson.class);
                String resultStr = mPostResult.getResultJson();
                if(null != resultStr && !resultStr.isEmpty()){
                    List<SubjectListItem> subjects = gson.fromJson(resultStr, new TypeToken<List<SubjectListItem>>() {}.getType());
                    listener.onSuccess(subjects);
                }
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news list failure.", e);
            }
        };

        //json格式post参数
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(date).toString();

        PostCommonHead.HEAD postHead = new PostCommonHead.HEAD("1", "subjectList", ((App) getApplication()).mAppSignature, dateStr, "9000");
        QuestionListPostParam param = new QuestionListPostParam(postHead, String.valueOf(userId), "-1", "200", Integer.toString(cateId),
                String.valueOf(ismine), Integer.toString(pageNum), Integer.toString(pageSize), String.valueOf(isAnswered));

        String strParam = new Gson().toJson(param);
        OkHttpUtils.postJson(url, loadMyQuestionCallback, strParam);
    }

    class QuestionListPostParam{
        private PostCommonHead.HEAD head;
        private PostBody body;

        public QuestionListPostParam(PostCommonHead.HEAD head,
                                     String userId, String expertId, String userType, String cateId, String isMine, String pageNum, String pageSize, String isAnswered) {
            this.head = head;
            this.body = new PostBody(userId, expertId, userType, cateId, isMine, pageNum, pageSize, isAnswered);
        }

        class PostBody{
            private String userId;
            private String expertId;//
            private String userType;
            private String cateId;
            private String isMine;//
            private String pageNum;
            private String pageSize;
            private String isAnswered;

            public PostBody(String userId, String expertId, String userType, String cateId, String isMine, String pageNum, String pageSize, String isAnswered) {
                this.userId = userId;
                this.expertId = expertId;
                this.userType = userType;
                this.cateId = cateId;
                this.isMine = isMine;
                this.pageNum = pageNum;
                this.pageSize = pageSize;
                this.isAnswered = isAnswered;
            }
        }
    }
}
