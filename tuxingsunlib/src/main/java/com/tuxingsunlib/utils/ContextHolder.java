package com.tuxingsunlib.utils;

import android.app.Application;
import android.content.Context;

/**
 * @Copyright © 2017 sanbo Inc. All rights reserved. @Description: TODO @Version: 1.0 @Create:
 * 2017/11/23 15:25
 * @author: safei
 * @mail: sanbo.xyz@gmail.com
 */
public class ContextHolder {

    private static Context mContext = null;

    /**
     * 获取Context
     *
     * @param ctx
     * @return
     */
    public static Context getContext(Context ctx) {
        try {
            if (ctx != null) {
                mContext = ctx.getApplicationContext();
            }
            if (mContext != null) {
                return mContext;
            } else {
                Class<?> activityThread = Class.forName("android.app.ActivityThread");
                Object at = activityThread.getMethod("currentActivityThread").invoke(null);
                Object app = activityThread.getMethod("getContext").invoke(at);
                if (app != null) {
                    Application ap = (Application) app;
                    if (ap != null) {
                        mContext = ap.getApplicationContext();
                    }
                    if (mContext != null) {
                        return mContext;
                    }
                }
            }
        } catch (Throwable igone) {
        }
        return null;
    }
}
