package com.tuxingsunlib.utils;

import android.app.Application;
import android.content.Context;

/**
 * @Copyright © 2017 sanbo Inc. All rights reserved. @Description: TODO @Version: 1.0 @Create:
 * 2017/11/23 15:25
 *
 * @author: safei
 * @mail: sanbo.xyz@gmail.com
 */
public class MContexts {
  public static Context CONTEXT = null;

  static {
    CONTEXT = getApplication();
  }

  public static Context init(Context context) {
    if (CONTEXT == null) {
      if (context != null) {
        CONTEXT = context.getApplicationContext();
      } else {
        CONTEXT = getApplication();
      }
    }
    return CONTEXT;
  }

  private static Application getApplication() {
    try {
      Class<?> activityThread = Class.forName("android.app.ActivityThread");
      Object at = activityThread.getMethod("currentActivityThread").invoke(null);
      Object app = activityThread.getMethod("getApplication").invoke(at);
      if (app != null) {
        return (Application) app;
      }
    } catch (Exception e) {
    }
    return null;
  }
}
