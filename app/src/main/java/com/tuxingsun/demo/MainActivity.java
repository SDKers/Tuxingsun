package com.tuxingsun.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tuxingsunlib.SanboAbility;
import com.tuxingsunlib.utils.AccessibilityHelper;
import com.tuxingsunlib.utils.log.L;
import com.tuxingsunlib.utils.log.T;

/**
 * @Copyright © 2020 sanbo Inc. All rights reserved.
 * @Description: 示例页面
 * @Version: 1.0
 * @Create: 2020/4/27 18:28
 * @author: sanbo
 */
public class MainActivity extends Activity {
    
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
                    if (!AccessibilityHelper.isAccessibilitySettingsOn(MainActivity.this, SanboAbility.class)) {
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
