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
import com.cheng.consultexpert.utils.OkHttpUtils;
import com.cheng.consultexpert.utils.PreUtils;
import com.cheng.consultexpert.widget.MultiSpinner;
import com.cheng.consultexpert.widget.SimpleSpinnerOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MyProfileActivity extends BaseActivity implements View.OnTouchListener {

    private Toolbar mToolbar;
    private EditText mUserName;
    private EditText mPhoneNum;
    private EditText mAge;
    private EditText mArea;
    private EditText mEstTime;
    private EditText mWorkTime;
    private EditText mUserDes;
    private boolean mIsConsulted = false;
    private String saleArea = "native";
    private int saleMode = 0;

    private List<Integer> mEditTextLists;
    private List<OkHttpUtils.Param> mPostParams;
    private Button mSubmitBtn;
    private PreUtils mPreUtils;
    private List<String> mMyGoodat;
    private String[] mGoodatItems;
    private ArrayAdapter<String> arrGoodatAdapter;
    private MultiSpinner mMyGoodatSpinner;
    private Set<Object> mGoodatCheckedSet;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_profile;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.myfragment_my_profile_tip));

        mPreUtils = PreUtils.getInstance(mContext);

        //addEditTextIdToList();

        mMyGoodatSpinner = (MultiSpinner)findViewById(R.id.goodat_spinner);

        //init goodat multispinner
        initMyGoodat();

        mUserName = (EditText) findViewById(R.id.user_name);
        //mUserName.setOnTouchListener(this);
        mPhoneNum = (EditText)findViewById(R.id.user_phonenum);
        //mPhoneNum.setOnTouchListener(this);
        mAge = (EditText)findViewById(R.id.user_age);
        //mIndustry.setOnTouchListener(this);
        mArea = (EditText)findViewById(R.id.user_area);

        mWorkTime = (EditText)findViewById(R.id.user_worktime);

        mUserDes = (EditText)findViewById(R.id.user_des);


        //submit user profiles
        mSubmitBtn = (Button)findViewById(R.id.submit);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isProfileDone()){
                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_profile_error_toast), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    addParams();
                    saveUserProfile();
                    //OkHttpUtils.post("http://101.200.40.228:8080/public/api/case", null, mPostParams);
                    finish();
                }
            }
        });

    }

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

    @Override
    protected void onStart() {
        super.onStart();
        //user profile
        mUserName.setText(mPreUtils.getUserName());
        mPhoneNum.setText(mPreUtils.getUserPhone());
        mAge.setText(mPreUtils.getUserAge());
        mArea.setText(mPreUtils.getUserArea());
        //good at
        restoreGoodat();
        mWorkTime.setText(mPreUtils.getUserWorkTime());
        mUserDes.setText(mPreUtils.getUserDes());


    }

    //TODO: restore spinner state
    private void restoreGoodat(){
        String strGood = mPreUtils.getUserGoodat();
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
        OkHttpUtils.Param param2 = new OkHttpUtils.Param("username", mUserName.getText().toString().trim());
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
        OkHttpUtils.Param param8 = new OkHttpUtils.Param("userdes", mUserDes.getText().toString().trim());
        mPostParams.add(param8);

    }

    private void saveUserProfile(){
        mPreUtils.setUserName(mUserName.getText().toString().trim());
        mPreUtils.setUserPhone(mPhoneNum.getText().toString().trim());
        mPreUtils.setUserArea(mArea.getText().toString().trim());
        mPreUtils.setUserAge(mAge.getText().toString().trim());
        mPreUtils.setUserWorkTime(mWorkTime.getText().toString().trim());
        mPreUtils.setUserDes(mUserDes.getText().toString().trim());

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
        if((!mUserName.getText().toString().trim().equals("")) && (!mPhoneNum.getText().toString().trim().equals("")) &&
                (!mArea.getText().toString().trim().equals("")) && (!mAge.getText().toString().trim().equals("")) &&
                (!mWorkTime.getText().toString().trim().equals("")) && (!mUserDes.getText().toString().trim().equals(""))){
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
}
