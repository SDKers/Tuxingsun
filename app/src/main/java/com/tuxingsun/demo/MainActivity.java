package com.tuxingsun.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tuxingsunlib.SanboAbility;
import com.tuxingsunlib.utils.AccessibilityHelper;
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

        if (AccessibilityHelper.isAccessibilitySettingsOn(MainActivity.this, SanboAbility.class)) {
          AccessibilityHelper.openAccessibilityService(MainActivity.this);
        } else {开。。。");
        }
          T.show("已经打
        break;
      case R.id.btn3:
        new Thread(new Runnable() {
          @Override
          public void run() {
            if (AccessibilityHelper.isAccessibilitySettingsOn(MainActivity.this, SanboAbility.class)) {
              AccessibilityHelper.openAccessibilityService(MainActivity.this);
            } else {
              // will error. need update
              T.show("已经打开。。。");
            }
          }
        }).start();
        break;
      default:
        break;
    }
  }
}
