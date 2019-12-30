package com.tuxingsunlib.utils.m;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.view.ViewConfiguration;

import com.tuxingsunlib.utils.ServiceHolder;

/**
 * @Copyright © 2019 sanbo Inc. All rights reserved. @Description: 来源
 * https://github.com/hfutxqd/ClickClick @Version: 1.0 @Create: 2019/1/24 22:28 @Author: sanbo
 */
@TargetApi(24)
public class GestureUtil {

  public static void tap(String x, String y) {
    GestureDescription description = makeTap(Float.valueOf(x), Float.valueOf(y), 50);
    AccessibilityService service = ServiceHolder.getInstance().getService();
    if (service != null) {
      service.dispatchGesture(description, null, null);
    }
  }

  public static void longClick(String x, String y) {
    GestureDescription description =
        makeTap(Float.valueOf(x), Float.valueOf(y), ViewConfiguration.getLongPressTimeout() + 200);
    AccessibilityService service = ServiceHolder.getInstance().getService();
    if (service != null) {
      service.dispatchGesture(description, null, null);
    }
  }

  public static void swipe(String x, String y, String x2, String y2, int duration) {
    GestureDescription description =
        GestureUtil.makeSwipe(
            Float.valueOf(x),
            Float.valueOf(y),
            Float.valueOf(x2),
            Float.valueOf(y2),
            Integer.valueOf(duration));
    AccessibilityService service = ServiceHolder.getInstance().getService();
    if (service != null) {
      service.dispatchGesture(description, null, null);
    }
  }

  // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  // public static void screenshot(String str) throws PendingIntent.CanceledException {
  //    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
  //        throw new UnsupportedOperationException();
  //    }
  //    Intent intent = new Intent();
  //    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  //    intent.setClass(ContextHolder.CONTEXT, ScreenCaptureActivity.class);
  //    PendingIntent.getActivity(App.get(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT).send();
  // }

  static GestureDescription makeTap(float x, float y, long dur) {
    Path path = new Path();
    path.moveTo(x, y);
    return new GestureDescription.Builder()
        .addStroke(new GestureDescription.StrokeDescription(path, 0, dur))
        .build();
  }

  static GestureDescription makeSwipe(float x, float y, float x2, float y2, int dur) {
    Path path = new Path();
    path.moveTo(x, y);
    path.lineTo(x2, y2);
    return new GestureDescription.Builder()
        .addStroke(new GestureDescription.StrokeDescription(path, 0, dur))
        .build();
  }

  static GestureDescription makeGesture(Path path, int dur) {
    return new GestureDescription.Builder()
        .addStroke(new GestureDescription.StrokeDescription(path, 0, dur))
        .build();
  }
}
