package com.txscore.utils;

import android.app.Application;
import android.content.Context;

/**
 * @Copyright © 2017 sanbo Inc. All rights reserved.
 * @Description: TODO
 * @Version: 1.0
 * @Create: 2017/11/23 15:25
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
        if (ctx != null) {
            mContext = ctx.getApplicationContext();
        }
        if (mContext == null) {
            Application application = null;
            try {
                application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
            } catch (Throwable e) {
            }

            if (application == null) {
                try {
                    application = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
                } catch (Throwable e) {
                }
            }
            if (application != null) {
                mContext = application.getApplicationContext();
            }
        }

        return mContext;
    }
}
