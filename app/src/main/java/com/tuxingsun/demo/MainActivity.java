package com.tuxingsun.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.txscore.TuxingsunAbility;
import com.txscore.utils.AccessibilityHelper;
import com.txscore.utils.log.L;
import com.txscore.utils.log.T;

import java.lang.reflect.Method;

/**
 * @Copyright © 2020 sanbo Inc. All rights reserved.
 * @Description: 示例页面
 * @Version: 1.0
 * @Create: 2020/4/27 18:28
 * @author: sanbo
 */
public class MainActivity extends Activity {
    private CRelativeLayout mLayout = null;
    private final String TAG = "MainActivity";
    private static Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mLayout = new CRelativeLayout(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Object thisObj = MainActivity.this;
                    int id = v.getId();
                    Method method = thisObj.getClass().getDeclaredMethod("button" + id);
                    if (method != null) {
                        method.setAccessible(true);
                        method.invoke(thisObj);
                    }
                } catch (Throwable e) {
                    L.e(e);
                }
            }
        }, 4, 10);
        setContentView(mLayout);
    }

    public void button1() {
        AccessibilityHelper.openAccessibilityService(mContext);
    }

    public static void button2() {
        if (!AccessibilityHelper.isAccessibilitySettingsOn(mContext, TuxingsunAbility.class)) {
            T.show("还未打开辅助功能～～");
        } else {
            T.show("已经打开。。。");
        }
    }


}
