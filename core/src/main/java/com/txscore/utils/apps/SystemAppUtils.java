package com.txscore.utils.apps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @Copyright © 2017 sanbo Inc. All rights reserved.
 * @Description: 跟App相关的辅助类
 * @Version: 1.0
 * @Create: 2017-6-2 下午9:56:23
 * @Author: sanbo
 */
public class SystemAppUtils {

    /**
     * 通过包名获取应用程序的名称。
     *
     * @param context     Context对象。
     * @param packageName 包名。
     * @return 返回包名所对应的应用程序的名称。
     */
    public static String getProgramNameByPackageName(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            name =
                    pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA))
                            .toString();
        } catch (NameNotFoundException e) {
        }
        return name;
    }

    /**
     * 根据 packageName 打开对应的app
     *
     * @param context
     * @param packageName 对应app Manifest package
     */
    public static void launchAppByPackageName(Context context, String packageName) {
        try {
            Object obj = new Intent("android.intent.action.MAIN", null);
            ((Intent) obj).addCategory("android.intent.category.LAUNCHER");
            ((Intent) obj).setPackage(packageName);
            obj = context.getPackageManager().queryIntentActivities((Intent) obj, 0).iterator().next();
            if (obj != null) {
                packageName = ((ResolveInfo) obj).activityInfo.packageName;
                obj = ((ResolveInfo) obj).activityInfo.name;
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");
                //                intent.addFlags(268435456);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.setComponent(new ComponentName(packageName, (String) obj));
                context.startActivity(intent);
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于检测app是否处于前台可见(只能用于Android7.0以下版本)
     *
     * @param packageName
     * @return
     */
    public static boolean checkAppIsForeground(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo;
        try {
            pInfo = pm.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException ignore) {
            return false;
        }

        String uid = String.valueOf(pInfo.applicationInfo.uid); // as 10045
        uid = uid.replaceFirst("10+", "");
        // "u0_a31  3305  396  1225804 84148 bg  SyS_epoll_ 00000000 S com.android.vending"
        try {
            Process ps = Runtime.getRuntime().exec("ps -P");
            InputStream is = ps.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            for (String line; (line = reader.readLine()) != null; ) {
                String[] info = line.split("\\s+");
                String userId = info[0];
                String processName = info[info.length - 1]; // as com.android.vending
                if (userId.endsWith(uid) && packageName.equals(processName)) {
                    String state = info[5]; // e.g fg|bg|ta
                    return !"bg".equals(state);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 启动应用
     *
     * @param context
     * @param packageName
     */
    public static void launchApp(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void launchAppB(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(packageName, 0);
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setPackage(pi.packageName);
            List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String className = ri.activityInfo.name;
                intent.setComponent(new ComponentName(packageName, className));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (NameNotFoundException e) {
        }
    }

    /**
     * 通过包名去启动一个Activity
     */
    public static void openApp(Context context, String packageName) {
        PackageInfo pi = null;
        try {
            pi = context.getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);
        PackageManager pManager = context.getApplicationContext().getPackageManager();
        List apps = pManager.queryIntentActivities(resolveIntent, 0);

        ResolveInfo ri = (ResolveInfo) apps.iterator().next();
        if (ri != null) {
            String startappName = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName cn = new ComponentName(startappName, className);

            intent.setComponent(cn);
            context.getApplicationContext().startActivity(intent);
        }
    }

    /**
     * 启动应用
     *
     * @param context
     * @param packageName
     * @param launchActivity
     */
    public static void launchApp(Context context, String packageName, String launchActivity) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, launchActivity));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 获取系统中是否安装某些应用
     *
     * @param context     内容上下文
     * @param packageName 判断是否安装的包名
     * @return
     */
    public static boolean isInstallApp(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        for (PackageInfo pi : packs) {
            if (pi.applicationInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断设备是否为平板
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isPackageInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        // In theory, if the package installer does not throw an exception,
        // package exists
        try {
            packageManager.getInstallerPackageName(packageName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }


    /**
     * 分享Apk信息
     */
    public void shareApkInfo(Context context, String info) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, info);
        context.startActivity(intent);
    }
}
