package com.wbg.xigui.utils;

import android.os.Environment;

import java.io.File;

public class LubanImg {

    /***
     * 设置压缩后的本地路径
     * @return
     */
    public static String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }
}
