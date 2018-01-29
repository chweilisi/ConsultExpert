package com.cheng.consultexpert.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.app.App;
import com.cheng.consultexpert.ui.common.Constants;
import com.cheng.consultexpert.ui.view.MyNeedAnswerQuestionListActivity;
import com.cheng.consultexpert.ui.view.QuestionCategoryActivity;

import static com.cheng.consultexpert.app.App.getApplication;

/**
 * Created by cheng on 2017/11/13.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{
    private LinearLayout searchView;
    private LinearLayout mQuestionCate;
    private LinearLayout mQueryField;
    private LinearLayout mNeedToAnswerQuestion;
    private LinearLayout mEssense;
    private Button mQuickAskBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefrag_layout, null, false);
//        searchView = (LinearLayout)view.findViewById(R.id.homeFragment_search);
//        searchView.setOnClickListener(this);
        mQuestionCate = (LinearLayout)view.findViewById(R.id.home_Answer_icon);
        mQuestionCate.setOnClickListener(this);
//        mQueryField = (LinearLayout)view.findViewById(R.id.home_field_icon);
//        mQueryField.setOnClickListener(this);
        mNeedToAnswerQuestion = (LinearLayout)view.findViewById(R.id.home_needto_answer_icon);
        mNeedToAnswerQuestion.setOnClickListener(this);
//        mEssense = (LinearLayout)view.findViewById(R.id.essence_question_icon);
//        mEssense.setOnClickListener(this);
//        mMyLoveExperts = (LinearLayout)view.findViewById(R.id.home_my_expert_icon);
//        mMyLoveExperts.setOnClickListener(this);
//        mQuickAskBtn = (Button)view.findViewById(R.id.expert_detail_ask_button);
//        mQuickAskBtn.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_TAKE_PICTURE);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.homeFragment_search:
//            {
//                Intent intent = new Intent(getContext(),SearchActivity.class);
//                startActivity(intent);
//                if (getContext() instanceof Activity) {
//                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
//                }
//                break;
//            }

            case R.id.home_Answer_icon:
            {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new ConsultFragment()).commit();
                Intent intent = new Intent(getActivity(), QuestionCategoryActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }
                break;
            }

            case R.id.home_needto_answer_icon:
            {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new ConsultFragment()).commit();
                Intent intent = new Intent(getActivity(), MyNeedAnswerQuestionListActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }
                break;
            }

            default:
                break;
        }

    }
}
