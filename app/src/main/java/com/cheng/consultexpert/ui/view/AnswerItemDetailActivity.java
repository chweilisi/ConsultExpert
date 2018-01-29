package com.cheng.consultexpert.ui.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.db.table.Subject;
import com.cheng.consultexpert.db.table.SubjectItem;
import com.cheng.consultexpert.ui.common.Constants;
import com.cheng.consultexpert.ui.common.Urls;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class AnswerItemDetailActivity extends BaseActivity {
    private TextView mAnswerDetail;
    private String mAnswer;
    private int mItemType = -1;
    private Subject mSubject;
    private SubjectItem mAnswerItem;
    private String mQuestionDes;
    private String mAttachmentPath;
    private ImageView mAttachImg;
    private File mDownloadImgFile;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_answer_item_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAnswerDetail = (TextView)findViewById(R.id.tv_answer_item_detail_page);
        mAttachImg = (ImageView)findViewById(R.id.answer_item_detail_attachment_img);

        String answerItem = getIntent().getStringExtra("answer_item");
        if(answerItem != null && !answerItem.isEmpty()){
            mAnswerItem = gson.fromJson(answerItem, SubjectItem.class);

            mItemType = mAnswerItem.getItemType();
            if(0 == mItemType){
                getSupportActionBar().setTitle("问题详情");
            } else {
                getSupportActionBar().setTitle("回答详情");
            }

            mAnswer = mAnswerItem.getContent();
            if(mAnswer != null && !mAnswer.isEmpty()){
                mAnswerDetail.setText(mAnswer);
            }

            mAttachmentPath = mAnswerItem.getFilePath();
            if(null != mAttachmentPath && !mAttachmentPath.isEmpty()){
                int pre = mAttachmentPath.lastIndexOf("/");
                String name = mAttachmentPath.substring(pre + 1);
                mDownloadImgFile = new File(Constants.IMAGE_CACHE_DIR + "take_picture/", name);
                Thread imgThread = new GetAttachmentImage(Urls.ATTACHMENT_BASE_URL, mAttachmentPath);
                imgThread.start();
            }

        }

        mQuestionDes = getIntent().getStringExtra("question_description");
        mAttachmentPath = getIntent().getStringExtra("file_path");

        if(mQuestionDes != null && !mQuestionDes.isEmpty()){
            getSupportActionBar().setTitle("问题详情");
            mAnswerDetail.setText(mQuestionDes);

            if(null != mAttachmentPath && !mAttachmentPath.isEmpty()){
                int pre = mAttachmentPath.lastIndexOf("/");
                String name = mAttachmentPath.substring(pre + 1);
                mDownloadImgFile = new File(Constants.IMAGE_CACHE_DIR + "take_picture/", name);
                Thread imgThread = new GetAttachmentImage(Urls.ATTACHMENT_BASE_URL, mAttachmentPath);
                imgThread.start();
            }
        }
    }

    private class GetAttachmentImage extends Thread {
        private String fileUrl;
        private String fileName;
        public GetAttachmentImage(String fileUrl, String fileName){
            this.fileUrl = fileUrl;
            this.fileName = fileName;
        }

        public void run(){
            if(!mDownloadImgFile.exists()){
                try {
                    URL url = new URL(fileUrl + fileName);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    int code = conn.getResponseCode();
                    if (code == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        FileOutputStream fos = new FileOutputStream(mDownloadImgFile);
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }

                        is.close();
                        fos.close();
                    }
                }catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //set img
            if (mContext != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mDownloadImgFile.exists()){
                            mAttachImg.setVisibility(View.VISIBLE);
                            Bitmap bitmap = BitmapFactory.decodeFile(mDownloadImgFile.getAbsolutePath());
                            mAttachImg.setImageBitmap(bitmap);
                        }

                    }
                });
            }
        }
    }
}
