package com.wbg.xigui.utils;

import android.content.Context;


/**
 * 工具类初始化
 */
public final class UtilApp {

    private Context mContext;

    private static final class Holder {
        private static final UtilApp INSTANCE = new UtilApp();
    }

    public static UtilApp getIntance() {
        return Holder.INSTANCE;
    }

    public void init(Context context) {
        mContext = context;

    }

    public Context getApplicationContext() {
        if (mContext == null)
            throw new RuntimeException("UtilApp is not init");
        return mContext;
    }
}
