package com.txs.utils.m;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewConfiguration;

import com.txs.utils.ServiceHolder;

/**
 * @Copyright © 2019 sanbo Inc. All rights reserved.
 * @Description: 来源: https://github.com/chenshunlin/Auto.js
 * @Version: 1.0
 * @Create: 2019/1/24 22:23
 * @Author: sanbo
 */
public class GlobalActionAutomator {
    public static boolean back() {
        return performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    public static boolean home() {
        return performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    public static boolean recents() {
        return performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
    }

    @TargetApi(24)
    public static boolean splitScreen() {
        return performGlobalAction(AccessibilityService.GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN);
    }

    @TargetApi(24)
    public static boolean click(int x, int y) {
        return press(x, y, ViewConfiguration.getTapTimeout() + 50);
    }

    @TargetApi(24)
    public static boolean press(int x, int y, int delay) {
        return gesture(0, delay, new int[]{x, y});
    }

    @TargetApi(24)
    public static boolean longClick(int x, int y) {
        return gesture(0, ViewConfiguration.getLongPressTimeout() + 200, new int[]{x, y});
    }

    @TargetApi(24)
    public static boolean swipe(int x1, int y1, int x2, int y2, int delay) {
        return gesture(0, delay, new int[]{x1, y1}, new int[]{x2, y2});
    }

    @TargetApi(24)
    public static boolean gesture(long start, long duration, int[]... points) {
        Path path = pointsToPath(points);
        return gestures(new GestureDescription.StrokeDescription(path, start, duration));
    }

    @TargetApi(24)
    public static boolean gestures(GestureDescription.StrokeDescription... strokes) {
        if (ServiceHolder.getInstance().getService() == null) {
            return false;
        }
        GestureDescription.Builder builder = new GestureDescription.Builder();
        for (GestureDescription.StrokeDescription stroke : strokes) {
            builder.addStroke(stroke);
        }
        return gesturesWithoutHandler(builder.build());
    }

    @TargetApi(24)
    static boolean gesturesWithoutHandler(GestureDescription description) {
        prepareLooperIfNeeded();
        final VolatileBox<Boolean> result = new VolatileBox<>(false);
        Handler handler = new Handler(Looper.myLooper());
        ServiceHolder.getInstance()
                .getService()
                .dispatchGesture(
                        description,
                        new AccessibilityService.GestureResultCallback() {
                            @Override
                            public void onCompleted(GestureDescription gestureDescription) {
                                result.set(true);
                                quitLoop();
                            }

                            @Override
                            public void onCancelled(GestureDescription gestureDescription) {
                                result.set(false);
                                quitLoop();
                            }
                        },
                        handler);
        Looper.loop();
        return result.get();
    }

    static void quitLoop() {
        Looper looper = Looper.myLooper();
        if (looper != null) {
            looper.quit();
        }
    }

    static void prepareLooperIfNeeded() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
    }

    static Path pointsToPath(int[][] points) {
        Path path = new Path();
        path.moveTo(points[0][0], points[0][1]);
        for (int i = 1; i < points.length; i++) {
            int[] point = points[i];
            path.lineTo(point[0], point[1]);
        }
        return path;
    }

    static boolean performGlobalAction(int globalAction) {

        if (VERSION.SDK_INT > 15) {
            if (ServiceHolder.getInstance().getService() != null) {
                return ServiceHolder.getInstance().getService().performGlobalAction(globalAction);
            }
        }
        return false;
    }

    public boolean quickSettings() {
        return performGlobalAction(AccessibilityService.GLOBAL_ACTION_QUICK_SETTINGS);
    }
}
