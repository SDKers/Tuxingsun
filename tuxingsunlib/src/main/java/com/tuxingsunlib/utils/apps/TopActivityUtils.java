package com.tuxingsunlib.utils.apps;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tuxingsunlib.utils.log.L;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class TopActivityUtils {

    /**
     * 获取栈顶activity名字
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getTopActivityName(Context context) {
        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                .getRunningTasks(1)
                .get(0)
                .topActivity
                .getClassName();
    }

    @SuppressWarnings("unchecked")
    public static String getNowActivity(Application app) {
        Field f;
        Object thread = null;
        Instrumentation base;
        String result = "";
        // Replace instrumentation
        try {
            thread = getActivityThread(app);
            f = thread.getClass().getDeclaredField("mInstrumentation");
            f.setAccessible(true);
            base = (Instrumentation) f.get(thread);
            if (base != null) {
                // get instrumentation activity list
                List<Instrumentation.ActivityMonitor> mActivityMonitors;
                f = Instrumentation.class.getDeclaredField("mActivityMonitors");
                f.setAccessible(true);
                mActivityMonitors = (List<Instrumentation.ActivityMonitor>) f.get(base);
                for (int i = 0; i < mActivityMonitors.size(); i++) {
                    Instrumentation.ActivityMonitor activityMonitor = mActivityMonitors.get(i);
                    // 获取Activity名称
                    result = activityMonitor.getLastActivity().getClass().getName();
                    Log.d("sanbo", "AppUtils getNowActivity " + result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Object getActivityThread(Context context) {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            // ActivityThread.currentActivityThread()
            Method m = activityThread.getMethod("currentActivityThread");
            m.setAccessible(true);
            Object thread = m.invoke(null);
            if (thread != null) {
                return thread;
            }

            // context.@mLoadedApk.@mActivityThread
            Field mLoadedApk = context.getClass().getField("mLoadedApk");
            mLoadedApk.setAccessible(true);
            Object apk = mLoadedApk.get(context);
            Field mActivityThreadField = apk.getClass().getDeclaredField("mActivityThread");
            mActivityThreadField.setAccessible(true);
            return mActivityThreadField.get(apk);
        } catch (Throwable ignore) {
            throw new RuntimeException("Failed to get mActivityThread from context: " + context);
        }
    }

    /**
     * 获取栈顶activity名字
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getTopActivityNameBPlan(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =
                (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            // 包ming
            // String packageName = f.getPackageName();
            // 完整类名
            topActivityClassName = f.getClassName();
            // 单独类名
            // String temp[] = topActivityClassName.split("\\.");
            // String topActivityName = temp[temp.length - 1];
            // System.out.println("topActivityName=" + topActivityName);
        }
        return topActivityClassName;
    }

    /**
     * 检查activity是否为栈顶activity
     *
     * @param context
     * @param activityName
     * @return
     */
    public static boolean isTopActivity(Context context, String activityName) {

        try {
            String topActivity = getTopActivityName(context);
            if (!TextUtils.isEmpty(activityName) && topActivity.equalsIgnoreCase(activityName)) {
                L.d("isTopActivity  true");
                return true;
            }
        } catch (Throwable e) {
            L.e(e);
        }
        L.d("isTopActivity  false");
        return false;
    }

    /**
     * 检查activity是否为栈顶activity
     *
     * @param context
     * @param packageName
     * @param activityName
     * @return
     */
    public static boolean isTopActivity(Context context, String packageName, String activityName) {

        try {
            String topActivity = getTopActivityName(context);
            String pack = context.getPackageName();
            if (!TextUtils.isEmpty(packageName)
                    && !TextUtils.isEmpty(activityName)
                    && topActivity.equalsIgnoreCase(activityName)
                    && pack.equalsIgnoreCase(packageName)) {
                return true;
            }
        } catch (Throwable e) {
            L.e(e);
        }
        return false;
    }

}
