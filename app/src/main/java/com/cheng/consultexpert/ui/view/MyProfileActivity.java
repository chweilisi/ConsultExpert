package com.cheng.consultexpert.ui.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.cheng.consultexpert.R;
import com.cheng.consultexpert.ui.common.PostCommonHead;
import com.cheng.consultexpert.ui.common.Urls;
import com.cheng.consultexpert.utils.OkHttpUtils;
import com.cheng.consultexpert.utils.PreUtils;
import com.cheng.consultexpert.widget.MultiSpinner;
import com.cheng.consultexpert.widget.SimpleSpinnerOption;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MyProfileActivity extends BaseActivity implements View.OnTouchListener {

    private Toolbar mToolbar;
    private EditText mExpertName;
    private RadioGroup mSexGroup;
    private RadioButton mMale;
    private RadioButton mFemale;
    private String mSex;
    private EditText mPhoneNum;
    private EditText mQq;
    private EditText mWeixin;
    private EditText mAge;
    private EditText mArea;
    private EditText mWorkTime;
    private EditText mExpertDes;



    private List<OkHttpUtils.Param> mPostParams;
    private Button mSubmitBtn;
    private PreUtils mPreUtils;
    private List<String> mMyGoodat;
    private String[] mGoodatItems;

    private MultiSpinner mMyGoodatSpinner;

    private String mMyProfile;
    private MyProfileBean mMyProfileBean;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_profile;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mMyProfile = getIntent().getStringExtra("myProfile");
        Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        mMyProfileBean = gson.fromJson(mMyProfile, MyProfileBean.class);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.myfragment_my_profile_tip));

        mPreUtils = PreUtils.getInstance(mContext);

        //addEditTextIdToList();


        mExpertName = (EditText) findViewById(R.id.expert_name);

        //sex
        mSexGroup = (RadioGroup)findViewById(R.id.expert_sex);
        mSexGroup.setOnCheckedChangeListener(sexCheckListener);
        mMale = (RadioButton)findViewById(R.id.sex_male);
        mFemale = (RadioButton)findViewById(R.id.sex_female);

        mPhoneNum = (EditText)findViewById(R.id.expert_phonenum);
        mQq = (EditText)findViewById(R.id.expert_qq);
        mWeixin = (EditText)findViewById(R.id.expert_weixin);
        mAge = (EditText)findViewById(R.id.expert_age);
        mArea = (EditText)findViewById(R.id.expert_area);
        mWorkTime = (EditText)findViewById(R.id.expert_worktime);

        //init goodat multispinner
        mMyGoodatSpinner = (MultiSpinner)findViewById(R.id.goodat_spinner);
        initMyGoodat();

        mExpertDes = (EditText)findViewById(R.id.expert_des);


        //submit user profiles
        mSubmitBtn = (Button)findViewById(R.id.submit);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mMyProfileBean.getStatus().trim().equalsIgnoreCase("200")){
                    if(!isProfileDone()){
                        Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_profile_error_toast), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        //addParams();
                        saveUserProfile();
                        OkHttpUtils.ResultCallback<String> submitProfileCallback = new OkHttpUtils.ResultCallback<String>() {
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

                        PostCommonHead.HEAD head = new PostCommonHead.HEAD("1", "updateExpert", mApplication.mAppSignature, dateStr, "9000");

                        String goodat = getGoodatItemsValue();
                        ProfilePostParam post = new ProfilePostParam(head, mPreUtils.getUserLoginId(), mPreUtils.getUserLoginName(), mExpertName.getText().toString().trim(),
                                mPreUtils.getUserId().toString().trim(), mPreUtils.getUserType().trim(), mPreUtils.getStatus().trim(), "", mSex,
                                mQq.getText().toString().trim(), mPhoneNum.getText().toString().trim(), mWeixin.getText().toString().trim(),
                                mAge.getText().toString().trim(), mArea.getText().toString().trim(), mWorkTime.getText().toString().trim(),
                                goodat, mExpertDes.getText().toString().trim());
                        String postParam = new Gson().toJson(post);
                        //请求数据
                        String url = Urls.HOST_TEST + Urls.LOGIN;
                        OkHttpUtils.postJson(url, submitProfileCallback, postParam);

                        //OkHttpUtils.post(Urls.HOST_TEST + Urls.EXPERT, null, mPostParams);
                        finish();
                    }
                }else {
                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_profile_no_submit_toast), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });

        //set profile
        setExpertProfile(mMyProfileBean);
    }

    class ProfilePostParam{
        private PostCommonHead.HEAD head;
        private postBody body;

        public ProfilePostParam(PostCommonHead.HEAD head, String loginId, String loginName, String name, String userId, String userType, String status,
                                String imgSrc, String sex, String qq, String phoneNum, String weixin, String age, String area,
                                String expertTime, String goodField, String des) {
            this.head = head;
            this.body = new postBody(loginId, loginName, name, userId, userType, status, imgSrc, sex, qq, phoneNum, weixin, age, area,
                    expertTime, goodField, des);
        }

        class postBody{
            private String loginId;//登录id
            private String loginName;//登录名
            private String name;//专家名字
            private String userId;//
            private String userType;//用户类型
            private String status;//是否认证过，200标识已通过认证
            private String imgSrc;
            private String sex;//性别
            private String qq;
            private String phoneNum;
            private String weixin;
            private String age;
            private String area;
            private String expertTime;
            private String goodField;
            private String des;

            public postBody(String loginId, String loginName, String name, String userId, String userType, String status,
                            String imgSrc, String sex, String qq, String phoneNum, String weixin, String age, String area,
                            String expertTime, String goodField, String des) {
                this.loginId = loginId;
                this.loginName = loginName;
                this.name = name;
                this.userId = userId;
                this.userType = userType;
                this.status = status;
                this.imgSrc = imgSrc;
                this.sex = sex;
                this.qq = qq;
                this.phoneNum = phoneNum;
                this.weixin = weixin;
                this.age = age;
                this.area = area;
                this.expertTime = expertTime;
                this.goodField = goodField;
                this.des = des;
            }
        }
    }

    private void setExpertProfile(MyProfileBean profile){
        if(!profile.getUserId().trim().equalsIgnoreCase("-1")){//have profile content
            mExpertName.setText(profile.getName());

            String sex = profile.getSex();
            if(sex.trim().equalsIgnoreCase("0")){
                mMale.setChecked(true);
                mFemale.setChecked(false);
            }else {
                mMale.setChecked(false);
                mFemale.setChecked(true);
            }

            mPhoneNum.setText(profile.getPhoneNum());
            mQq.setText(profile.getQq());
            mWeixin.setText(profile.getWeixin());
            mAge.setText(profile.getAge());
            mArea.setText(profile.getArea());

            //goodat field
            String good = profile.getGoodField();
            restoreGoodat(good);

            mExpertDes.setText(profile.getDes());
        }

    }

    RadioGroup.OnCheckedChangeListener sexCheckListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == mMale.getId()){
                mSex = "0";
            }else if(checkedId == mFemale.getId()){
                mSex = "1";
            }
        }
    };

    private void initMyGoodat(){
        mGoodatItems = getResources().getStringArray(R.array.consult_category);
        mMyGoodat = new ArrayList<String>(Arrays.asList(mGoodatItems));

        mMyGoodatSpinner.setTitle("请选择领域");
        ArrayList multiSpinnerList=new ArrayList();
        for(int i = 0; i < mMyGoodat.size(); i++){
            SimpleSpinnerOption option=new SimpleSpinnerOption();
            option.setName(mMyGoodat.get(i));
            option.setValue(i);
            multiSpinnerList.add(option);
        }
        mMyGoodatSpinner.setDataList(multiSpinnerList);


//        //适配器
//        arrGoodatAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mMyGoodat);
//        //设置样式
//        arrGoodatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //加载适配器
//        mMyGoodatSpinner.setAdapter(arrGoodatAdapter);


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        //user profile
//        mExpertName.setText(mPreUtils.getUserName());
//        mPhoneNum.setText(mPreUtils.getUserPhone());
//        mAge.setText(mPreUtils.getUserAge());
//        mArea.setText(mPreUtils.getUserArea());
//        //good at
//        String strGood = mPreUtils.getUserGoodat();
//        restoreGoodat(strGood);
//        mWorkTime.setText(mPreUtils.getUserWorkTime());
//        mExpertDes.setText(mPreUtils.getUserDes());
//
//
//    }

    //TODO: restore spinner state
    private void restoreGoodat(String strGood){
        if(null != strGood && !strGood.isEmpty()){
            String[] strArrGood = strGood.split(",");
            List<String> goodatList = new ArrayList<String>(Arrays.asList(strArrGood));

            Set<Object> checkedSet = new HashSet<>();
            for(int i = 0; i < goodatList.size(); i++){
                checkedSet.add(Integer.parseInt(goodatList.get(i)));
            }
            mMyGoodatSpinner.setCheckedSet(checkedSet);
        }

    }

    private void addParams(){
        mPostParams = new ArrayList<>();

        OkHttpUtils.Param param1 = new OkHttpUtils.Param("id", mPreUtils.getUserId().toString().trim());
        mPostParams.add(param1);
        OkHttpUtils.Param param2 = new OkHttpUtils.Param("username", mExpertName.getText().toString().trim());
        mPostParams.add(param2);
        OkHttpUtils.Param param3 = new OkHttpUtils.Param("phonenum", mPhoneNum.getText().toString().trim());
        mPostParams.add(param3);
        OkHttpUtils.Param param4 = new OkHttpUtils.Param("area", mArea.getText().toString().trim());
        mPostParams.add(param4);
        OkHttpUtils.Param param5 = new OkHttpUtils.Param("age", mAge.getText().toString().trim());
        mPostParams.add(param5);
        OkHttpUtils.Param param6 = new OkHttpUtils.Param("worktime", mWorkTime.getText().toString().trim());
        mPostParams.add(param6);

        //TODO: goodat
        /*
        mGoodatCheckedSet = mMyGoodatSpinner.getCheckedSet();
        Iterator it = mGoodatCheckedSet.iterator();
        String goodatStr = "";
        while (it.hasNext()){
            int n = (int)it.next();
            goodatStr = goodatStr + n + ",";
        }
        */
        String goodatStrVal = getGoodatItemsValue();
        OkHttpUtils.Param param7 = new OkHttpUtils.Param("goodat", goodatStrVal);
        mPostParams.add(param7);

        //user description
        OkHttpUtils.Param param8 = new OkHttpUtils.Param("userdes", mExpertDes.getText().toString().trim());
        mPostParams.add(param8);
        OkHttpUtils.Param param9 = new OkHttpUtils.Param("method", "save");
        mPostParams.add(param9);

    }

    private void saveUserProfile(){
        mPreUtils.setUserName(mExpertName.getText().toString().trim());
        mPreUtils.setUserSex(mSex);
        mPreUtils.setUserPhone(mPhoneNum.getText().toString().trim());
        mPreUtils.setUserQq(mQq.getText().toString().trim());
        mPreUtils.setUserWeixin(mWeixin.getText().toString().trim());
        mPreUtils.setUserAge(mAge.getText().toString().trim());
        mPreUtils.setUserArea(mArea.getText().toString().trim());
        mPreUtils.setUserWorkTime(mWorkTime.getText().toString().trim());
        mPreUtils.setUserDes(mExpertDes.getText().toString().trim());

        //goodat spinner to string, to save it
        /*
        mGoodatCheckedSet = mMyGoodatSpinner.getCheckedSet();
        Iterator it = mGoodatCheckedSet.iterator();
        String goodatStr = "";
        while (it.hasNext()){
            int n = (int)it.next();
            goodatStr = goodatStr + n + ",";
        }
        */
        String goodatStrVal = getGoodatItemsValue();
        mPreUtils.setUserGoodat(goodatStrVal);
    }

    private String getGoodatItemsValue(){
        StringBuilder sb = new StringBuilder();
        for(SimpleSpinnerOption option:mMyGoodatSpinner.getCheckedOptions()){
            sb.append(option.getValue()).append(",");
        }
        if(sb.length() > 0){
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    private boolean isProfileDone(){
        boolean result = false;
        if((!mExpertName.getText().toString().trim().equals("")) && (!mPhoneNum.getText().toString().trim().equals("")) &&
                (!mArea.getText().toString().trim().equals("")) && (!mAge.getText().toString().trim().equals("")) &&
                (!mWorkTime.getText().toString().trim().equals("")) && (!mExpertDes.getText().toString().trim().equals(""))){
            result = true;
        }
        return result;
    }

    /**
     * 调整FrameLayout大小
     * @param tp
     */
    private void resizePikcer(FrameLayout tp){
        List<NumberPicker> npList = findNumberPicker(tp);
        for(NumberPicker np:npList){
            resizeNumberPicker(np);
        }
    }

    /*
     * 调整numberpicker大小
     */
    private void resizeNumberPicker(NumberPicker np){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);
    }

    /**
     * 得到viewGroup里面的numberpicker组件
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup){
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if(null != viewGroup){
            for(int i = 0;i<viewGroup.getChildCount();i++){
                child = viewGroup.getChildAt(i);
                if(child instanceof NumberPicker){
                    npList.add((NumberPicker)child);
                }
                else if(child instanceof LinearLayout){
                    List<NumberPicker> result = findNumberPicker((ViewGroup)child);
                    if(result.size()>0){
                        return result;
                    }
                }
            }
        }
        return npList;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        //if ((isEditText(v) && canVerticalScroll((EditText) v))) {
//        if(isEditText(v)){
//            v.getParent().requestDisallowInterceptTouchEvent(true);
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                v.getParent().requestDisallowInterceptTouchEvent(false);
//            }
//        }
        //v.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        boolean result = false;
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return result;
        }

        if((scrollY > 0) || (scrollY < scrollDifference - 1)){
            result = true;
        }
        return result;
    }

    class MyProfileBean{
        private String loginId;//登录id
        private String loginName;//登录名
        private String name;//专家名字
        private String userId;//
        private String userType;//用户类型
        private String status;//是否认证过，200标识已通过认证
        private String imgSrc;
        private String sex;//性别
        private String qq;
        private String phoneNum;
        private String weixin;
        private String age;
        private String area;
        private String expertTime;
        private String goodField;
        private String des;

        public MyProfileBean(String loginId, String loginName, String name, String userId, String userType, String status,
                             String imgSrc, String sex, String qq, String phoneNum, String weixin, String age, String area,
                             String expertTime, String goodField, String des) {
            this.loginId = loginId;
            this.loginName = loginName;
            this.name = name;
            this.userId = userId;
            this.userType = userType;
            this.status = status;
            this.imgSrc = imgSrc;
            this.sex = sex;
            this.qq = qq;
            this.phoneNum = phoneNum;
            this.weixin = weixin;
            this.age = age;
            this.area = area;
            this.expertTime = expertTime;
            this.goodField = goodField;
            this.des = des;
        }

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getWeixin() {
            return weixin;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getExpertTime() {
            return expertTime;
        }

        public void setExpertTime(String expertTime) {
            this.expertTime = expertTime;
        }

        public String getGoodField() {
            return goodField;
        }

        public void setGoodField(String goodField) {
            this.goodField = goodField;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }
    }
}
