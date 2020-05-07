package com.tuxingsun.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tuxingsunlib.SanboAbility;
import com.tuxingsunlib.utils.AccessibilityHelper;
import com.tuxingsunlib.utils.ContextHolder;
import com.tuxingsunlib.utils.log.L;
import com.tuxingsunlib.utils.log.T;
import com.tuxingsunlib.utils.log.Tt;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Copyright © 2020 sanbo Inc. All rights reserved.
 * @Description: 示例页面
 * @Version: 1.0
 * @Create: 2020/4/27 18:28
 * @author: sanbo
 */
public class MainActivity extends Activity {
    
    private static Toast toast;
    private static Object mTN;
    private static Method show;
    private static Method hide;
    private static Field tnNextViewField;
    private static Handler testHandler = null;
    private static Toast mt = null;
    private Handler mh = null;
    private Toast mToast = null;
    
    public static void testShow(final String message) {

//    Log.e("wupan","message:"+message);
        
        init();
        Message ms = new Message();
        ms.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString("test", message);
        ms.setData(bundle);
        
        if (testHandler.hasMessages(1)) {
            testHandler.removeMessages(1);
            testHandler.sendEmptyMessage(2);
        }
        testHandler.sendMessageAtFrontOfQueue(ms);
        
    }
    
    private static void init() {
        if (testHandler == null) {
            testHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.i("sanbo", "msg.what:" + msg.what);
                    if (msg.what == 1) {
                        Log.i("sanbo", "message:" + msg.getData().getString("test"));
                        String text = "";
                        if (mt == null) {
                            mt = Toast.makeText(ContextHolder.getContext(null), text, Toast.LENGTH_SHORT);
                        } else {
                            mt.setText(text);
                        }
                        mt.show();
                    } else if (msg.what == 2) {
                        if (mt != null) {
                            mt.cancel();
                        }
                    }
                }
            };
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                AccessibilityHelper.openAccessibilityService(this);
                break;
            case R.id.btn2:
                
                try {
                    if (AccessibilityHelper.isAccessibilitySettingsOn(MainActivity.this, SanboAbility.class)) {
                        AccessibilityHelper.openAccessibilityService(MainActivity.this);
                    } else {
                        T.show("已经打开。。。");
                    }
                } catch (Throwable e) {
                    L.e(e);
                }
                break;
            case R.id.btn3:
                break;
            case R.id.btn4:
                break;
            case R.id.btn5:
                break;
            case R.id.btn6:
                break;
            default:
                break;
        }
    }
    
}
