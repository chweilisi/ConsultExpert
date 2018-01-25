package com.cheng.consultexpert.ui.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by cheng on 2017/12/5.
 */

public class Constants {
    public static final int USER_TYPE_COMPANY = 100;
    public static final int USER_TYPE_EXPERT = 200;
    public static final String SYSTEM_ERROR_PROGRAM = "500";
    public static final String SYSTEM_ERROR_SERVER = "100";
    public static final String LOGIN_OR_POST_SUCCESS = "200";

    private static final String BASE_CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "wenceguexpert" + File.separator;

    //声音的缓存目录
    public static final String VOICE_CACHE_DIR = BASE_CACHE_DIR + "voice" + File.separator;

    public static final String IMAGE_CACHE_DIR = BASE_CACHE_DIR + "image" + File.separator;
    public static final int REQUEST_CODE_TAKE_PICTURE = 10;
    public static final int REQUEST_CODE_SELECT_FROM_LOCAL = 20;

    //13个领域
    public static final int CATEGORY_1  = 0x01;
    public static final int CATEGORY_2  = 0x02;
    public static final int CATEGORY_3  = 0x04;
    public static final int CATEGORY_4  = 0x08;
    public static final int CATEGORY_5  = 0x10;
    public static final int CATEGORY_6  = 0x20;
    public static final int CATEGORY_7  = 0x40;
    public static final int CATEGORY_8  = 0x80;
    public static final int CATEGORY_9  = 0x100;
    public static final int CATEGORY_10 = 0x200;
    public static final int CATEGORY_11 = 0x400;
    public static final int CATEGORY_12 = 0x800;
    public static final int CATEGORY_13 = 0x1000;

}
