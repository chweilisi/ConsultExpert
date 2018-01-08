package com.cheng.consultexpert.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by cheng on 2017/12/6.
 */

public class PreUtils {

    private static PreUtils instance;

    private SharedPreferences mPre;

    public static PreUtils getInstance(Context context) {
        if (instance == null) {
            instance = new PreUtils(context);
        }
        return instance;
    }

    private PreUtils(Context context) {
        mPre = context.getSharedPreferences("consult_expert_config", 0);
    }

/*
    public void setLoginStatus(boolean status) {
        mPre.edit().putBoolean("consult_login_status", status).commit();
    }


    public boolean getLoginStatus() {
        return mPre.getBoolean("conslut_login_status", false);
    }


    public void setCookie(String cookie) {
        mPre.edit().putString("cookie", cookie).commit();
    }


    public String getCookie() {
        return mPre.getString("cookie", "");
    }

    //这个只有登录之后才能使用 发帖用到

    public void setFormhash(String formhash) {
        mPre.edit().putString("formhash", formhash).commit();
    }

     //只有登录之后才能使用 发帖用到

    public String getFormhash() {
        return mPre.getString("formhash", null);
    }

    //用于缓存登录信息

    public void setLoginUid(String uid) {
        mPre.edit().putString("login_uid", uid).commit();
    }

    //获取当前登录的用户信息

    public String getloginUid() {
        return mPre.getString("login_uid", "0");
    }


    //用于缓存登录信息saltKey
    public void setSaltKey(String saltKey) {
        mPre.edit().putString("login_saltKey", saltKey).commit();
    }


     //获取当前登录的用户信息saltKey

    public String getSaltKey() {
        return mPre.getString("login_saltKey", "");
    }

    public void setLoginUserName(String member_username) {
        mPre.edit().putString("login_username", member_username).commit();
    }

    public void setLoginToken(String token) {
        mPre.edit().putString("login_token", token).commit();
    }

    public String getLoginToken() {
        return mPre.getString("login_token", "");
    }

    public String getLoninUserName() {
        return mPre.getString("login_username", "");
    }

    public void setAvator(String avator) {
        mPre.edit().putString("avator_", avator).commit();
    }

    public String getAvator() {
        return mPre.getString("avator_", "");
    }

    public void setAvatorStatus(int status) {
        mPre.edit().putInt("avator_status_" + getloginUid(), status).commit();
    }

    public int getAvatorStatus() {
        return mPre.getInt("avator_status_" + getloginUid(), -1);
    }


    public void setLoignAccount(String account) {
        mPre.edit().putString("account", account).commit();
    }

    public String getLoginAccount() {
        return mPre.getString("account", null);
    }


    public void setLoginPswd(String loginPswd) {
        mPre.edit().putString("loginPswd", loginPswd).commit();
    }

    public String getLoginPswd() {
        return mPre.getString("loginPswd", null);
    }


    public void setNickName(String account) {
        mPre.edit().putString("nickname", account).commit();
    }

    public String getNickName() {
        return mPre.getString("nickname", null);
    }

    public void setId(Long id){
        mPre.edit().putLong("id", id);
    }

    public Long getId(){
        return mPre.getLong("id", -1);
    }
*/
    //user profile
    public void setUserName(String name){
        mPre.edit().putString("username", name).commit();
    }
    public String getUserName(){
        return mPre.getString("username", "");
    }

    public void setUserPhone(String num){
        mPre.edit().putString("phonenum", num).commit();
    }
    public String getUserPhone(){
        return mPre.getString("phonenum", "");
    }

    public void setUserAge(String cap){
        mPre.edit().putString("age", cap).commit();
    }
    public String getUserAge(){
        return mPre.getString("age", "");
    }

    public void setUserArea(String area){
        mPre.edit().putString("area", area).commit();
    }
    public String getUserArea(){
        return mPre.getString("area", "");
    }

    public void setUserWorkTime(String ind){
        mPre.edit().putString("worktime", ind).commit();
    }
    public String getUserWorkTime(){
        return mPre.getString("worktime", "");
    }

    public void setUserGoodat(String num){
        mPre.edit().putString("goodat", num).commit();
    }
    public String getUserGoodat(){
        return mPre.getString("goodat", "");
    }

    public void setUserDes(String num){
        mPre.edit().putString("userdes", num).commit();
    }
    public String getUserDes(){
        return mPre.getString("userdes", "");
    }



    //login associated
    public void setUserLoginName(String id){
        mPre.edit().putString("loginusername", id).commit();
    }
    public String getUserLoginName(){
        return mPre.getString("loginusername", "");
    }

    public void setUserId(Long id){
        mPre.edit().putLong("userid", id).commit();
    }

    public Long getUserId(){
        return mPre.getLong("userid", -1);
    }

    public void setUserLoginPsw(String psw){
        mPre.edit().putString("userpsw", psw).commit();
    }
    public String getUserLoginPsw(){
        return mPre.getString("userpsw", "");
    }

    public void setUserLoginId(String id){
        mPre.edit().putString("userloginid", id).commit();
    }
    public String getUserLoginId(){
        return mPre.getString("userloginid", "-1");
    }

    public void setUserIsLogin(int login){
        mPre.edit().putInt("islogined", login).commit();
    }
    public int getUserIsLogin(){
        return mPre.getInt("islogined", -1);
    }

    public void setUserType(String userType){
        mPre.edit().putString("usertype", userType).commit();
    }
    public String getUserType(){
        return mPre.getString("usertype", "");
    }

    public void clearUserInfo(){
        mPre.edit().clear().commit();
    }
}
