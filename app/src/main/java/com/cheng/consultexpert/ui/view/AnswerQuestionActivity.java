package com.cheng.consultexpert.ui.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.ui.common.Constants;
import com.cheng.consultexpert.ui.common.PostCommonHead;
import com.cheng.consultexpert.ui.common.PostResponseBodyJson;
import com.cheng.consultexpert.ui.common.Urls;
import com.cheng.consultexpert.utils.FileUtils;
import com.cheng.consultexpert.utils.OkHttpUtils;
import com.cheng.consultexpert.utils.PhotoUtils;
import com.cheng.consultexpert.utils.ToastUtils;
import com.cheng.consultexpert.utils.ToolsUtils;
import com.cheng.consultexpert.widget.BaseDialogue;
import com.cheng.consultexpert.widget.CommonImageLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AnswerQuestionActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private EditText answer_content;
    private Button mSubmit;
    private String mAnswer;
    private Long mSubjectId;
    private List<OkHttpUtils.Param> paramList;
    private ImageButton mBtnAttachment;
    protected BaseDialogue mBaseDialog;
    private Uri imageUri;
    private File mTakedPhoto = null;
    private Gson gson;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_answer_question_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSubjectId = getIntent().getLongExtra("subjectId", -1);
        answer_content = (EditText)findViewById(R.id.answer_content);
        mBtnAttachment = (ImageButton)findViewById(R.id.btn_answer_question_attach);

        mBtnAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureFrom();
            }
        });

        answer_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(answer_content, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        mSubmit = (Button)findViewById(R.id.submit_answer);



        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO submit question
//                String url = Urls.HOST_TEST + Urls.QUESTION;
//
//                paramList = new ArrayList<>();
//                //add user id
//                String id = String.valueOf(mApplication.mUserInfo.getUserId());
//                OkHttpUtils.Param userId = new OkHttpUtils.Param("userId", id);
//
//                //add subjectid
//                OkHttpUtils.Param subjectid = new OkHttpUtils.Param("subjectid", String.valueOf(mSubjectId));
//
//                //add post parameter question des
//                mAnswer = answer_content.getText().toString().trim();
//                OkHttpUtils.Param answer = new OkHttpUtils.Param("answerContent", mAnswer);
//
//                OkHttpUtils.Param method = new OkHttpUtils.Param("method", "saveItem");
//
//                paramList.add(userId);
//                paramList.add(subjectid);
//                paramList.add(answer);
//                paramList.add(method);
//
//                OkHttpUtils.post(url, null, paramList);

                OkHttpUtils.ResultCallback<String> submitQuestionCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                };

                //json格式post参数
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateStr = dateFormat.format(date).toString();

                String id = String.valueOf(mApplication.mUserId);
                mAnswer = answer_content.getText().toString().trim();
                PostCommonHead.HEAD postHead = new PostCommonHead.HEAD("1", "saveItem", mApplication.mAppSignature, dateStr, "9000");
                AnswerSubmitParam postParam = new AnswerSubmitParam(postHead, id, String.valueOf(mSubjectId), "1", mAnswer, "0", "20");


                String param = gson.toJson(postParam);
                String url = Urls.HOST_TEST + Urls.QUESTION;

                HashMap<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("file", mTakedPhoto);
                paramsMap.put("json", param);

                OkHttpUtils.uploadFile(url, submitQuestionCallback, paramsMap);


//                Intent intent = new Intent();
//                intent.setClass(mContext, QuestionDetailActivity.class);
//                intent.putExtra("resultCode", "submit");
//                AnswerQuestionActivity.this.setResult(RESULT_OK, intent);
                AnswerQuestionActivity.this.setResult(100);
                //finish self
                finish();
            }
        });
    }

    class AnswerSubmitParam{
        private PostCommonHead.HEAD head;
        private AnswerBody body;

        public AnswerSubmitParam(PostCommonHead.HEAD head,
                                 String relationId, String subjectId, String isType, String questionAnswer, String pageNum, String pageSize) {
            this.head = head;
            this.body = new AnswerBody(relationId, subjectId, isType, questionAnswer, pageNum, pageSize);
        }

        class AnswerBody{
            private String relationId;//追问就是用户id，专家回答就是专家id
            private String subjectId;
            private String isType;
            private String questionAnswer;
            private String pageNum;
            private String pageSize;

            public AnswerBody(String relationId, String subjectId, String isType, String questionAnswer, String pageNum, String pageSize) {

                this.relationId = relationId;
                this.subjectId = subjectId;
                this.isType = isType;
                this.questionAnswer = questionAnswer;
                this.pageNum = pageNum;
                this.pageSize = pageSize;
            }
        }
    }

    private List<String> mStringList;
    private void showPictureFrom() {
        ListView view = (ListView) getLayoutInflater().inflate(R.layout.file_chooser_layout, null);
        if (mStringList == null) {
            mStringList = new ArrayList<>();
            mStringList.add("拍照");
            mStringList.add("从手机上取");
        }

        view.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, mStringList));
        view.setOnItemClickListener(this);
        mBaseDialog.setDialogContentView(view).setTitle("上传").setBottomLayoutVisible(false).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mBaseDialog.dismiss();
        if (position == 0) {
            mTakedPhoto = CommonImageLoader.getInstance().createPhotoFile();
            DynamicObtainCameraPermissionAndTakePicture();
        } else {
            startPickPhoto();
        }
    }

    private void DynamicObtainCameraPermissionAndTakePicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    ToastUtils.showShort(mContext, "您已经拒绝过一次");
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_TAKE_PICTURE);
            } else {//有权限直接调用系统相机拍照
                if (FileUtils.isExistSDCard()) {
                    imageUri = Uri.fromFile(mTakedPhoto);
                    //通过FileProvider创建一个content类型的Uri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(mContext, "com.cheng.consultexpert.fileprovider", mTakedPhoto);
                    }
                    PhotoUtils.takePicture(this, imageUri, Constants.REQUEST_CODE_TAKE_PICTURE);
                } else {
                    ToastUtils.showShort(mContext, "设备没有SD卡！");
                }
            }
        }
    }

    private void startPickPhoto(){
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_FROM_LOCAL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_TAKE_PICTURE:
            {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        if (data.hasExtra("data")) {
                            Bitmap bitmap = data.getParcelableExtra("data");
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= 24){
                            if(mTakedPhoto.exists()){

                            }

                        }else {
                        }
                    }
                }
                break;
            }
            case Constants.REQUEST_CODE_SELECT_FROM_LOCAL:
            {
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = null;
                    try {
                        path = ToolsUtils.getPath(mContext, uri);
                        mTakedPhoto = new File(path);
                        if(mTakedPhoto.exists()){

                        }

                    } catch (URISyntaxException e) {

                    }
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
