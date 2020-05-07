package com.tuxingsunlib.utils.log;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.tuxingsunlib.utils.ContextHolder;

/**
 * @Copyright © 2015 sanbo Inc. All rights reserved.
 * @Description: Toast同一管理类 多log堆积时候显示最后一个
 * @Version: 1.0
 * @Create: 2015年6月28日 下午1:41:16
 * @Author: sanbo
 */
public class T {
    private static android.widget.Toast mToast;
    
    private T() {
    }
    
    
    private static Context mContext = ContextHolder.getContext(null);
    private static Handler testHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(mContext, msg.getData().getString("test"), Toast.LENGTH_SHORT).show();
            }
        }
    };
    
    
    public static void show(final String message) {
        Message ms = new Message();
        ms.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString("test", message);
        ms.setData(bundle);
        
        if (testHandler.hasMessages(1)) {
            testHandler.removeMessages(1);
        }
        testHandler.sendMessageAtFrontOfQueue(ms);
    }
}
