package com.cheng.consultexpert.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.ui.common.PostCommonHead;
import com.cheng.consultexpert.ui.view.LoginActivity;
import com.cheng.consultexpert.ui.view.MyAnsweredQuestionActivity;
import com.cheng.consultexpert.ui.view.MyProfileActivity;
import com.cheng.consultexpert.utils.OkHttpUtils;
import com.cheng.consultexpert.utils.PreUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.cheng.consultexpert.app.App.mApp;

/**
 * Created by cheng on 2017/11/13.
 */

public class InfoFragment extends Fragment {
    private LinearLayout mMyAnswerQuestion;
    //private LinearLayout mMyLoveAnswer;
    private LinearLayout mMyProfile;
    private Button mLogout;
    private PreUtils pre;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.infofrag_layout, null, false);
        mMyAnswerQuestion = (LinearLayout)view.findViewById(R.id.my_answer_question_parent);
        //mMyLoveAnswer = (LinearLayout)view.findViewById(R.id.my_love_question_parent);
        mMyProfile = (LinearLayout)view.findViewById(R.id.my_profile_parent);

        pre = PreUtils.getInstance(getActivity());
        mLogout = (Button)view.findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction("logout_app");
//                getContext().sendBroadcast(intent);
//                //jump to login
                //pre.setUserIsLogin(-1);
                mApp.rmAllActivity_();
                //restoreUserInfo();
                pre.clearUserInfo();
                Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(intentLogin);

//                Intent intent2 = new Intent(Intent.ACTION_MAIN);
//                intent2.addCategory(Intent.CATEGORY_HOME);
//                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent2);
//                //android.os.Process.killProcess(android.os.Process.myPid());

            }
        });
//        mMyLoveAnswer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MyLoveQuestionListActivity.class);
//                startActivity(intent);
//            }
//        });
        mMyAnswerQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyAnsweredQuestionActivity.class);
                startActivity(intent);
            }
        });
        mMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtils.ResultCallback<String> myProfileResultCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String response) {

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                };

                //json格式post参数
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateStr = dateFormat.format(date).toString();





                Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    class ProfilePostBean{
        private PostCommonHead.HEAD head;
        private ProfileBody body;
        public ProfilePostBean(PostCommonHead.HEAD head, String loginId, String userId) {
            this.head = head;
            this.body = new ProfileBody(loginId, userId);
        }

        class ProfileBody{
            private String loginId;//登录id
            private String userId;//专家id

            public ProfileBody(String loginId, String userId) {
                this.loginId = loginId;
                this.userId = userId;
            }

        }
    }

}
