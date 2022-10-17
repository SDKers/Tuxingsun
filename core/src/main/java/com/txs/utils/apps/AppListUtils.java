package com.txs.utils.apps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import com.txs.utils.ShellHelper;
import com.txs.utils.log.L;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Copyright © 2020 sanbo Inc. All rights reserved.
 * @Description: TODO
 * @Version: 1.0
 * @Create: 2020/4/27 17:49
 * @author: sanbo
 */
public class AppListUtils {

    /**
     * 1.PackageManager .getInstalledPackages(0) 此方法在华为、oppo手机上，把权限禁止后，就不能正确获取到已安装应用列表了。
     */
    public static List<String> getAppListByGetInstalledPackages(Context context) {
        List<String> appList = new ArrayList<String>();
        if (context == null) {
            return appList;
        }
        PackageManager pm = context.getPackageManager();
        String packageName = null;
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            packageName = packageInfo.packageName;
            if (!TextUtils.isEmpty(packageName)) {
                if (pm.getLaunchIntentForPackage(packageName) != null) {
                    //  L.i("===>" + packageName);
                    if (!appList.contains(packageName)) {
                        appList.add(packageName);
                    }
                } else {
                    //                    L.i("getAppListByGetInstalledPackages===>"+packageName);
                }
            }
        }
        return appList;
    }

    /**
     * <pre>
     * 2. pm list package -3 oppo手机上，如果禁止了获取已安装应用列表的权限，那么结果就会受到影响
     *
     * adb shell pm list packages -3 获取安装的三方软件。  格式  package:包名
     *              package:com.android.smoketest
     * adb shell pm list packages -f 获取系统软件。  格式  package:apk路径=包名
     *              package:/system/app/Development/Development.apk=com.android.development
     * adb shell pm list packages -d 获取禁用的软件。  格式  package:包名
     * adb shell pm list packages -e 获取启用的软件。  格式  package:包名
     * adb shell pm list packages -s 获取系统软件。  格式  package:包名
     * adb shell pm list packages -i 只输出包和安装信息（安装来源）。  格式  package:包名 installer=来源信息
     *              package:com.suishenbaodian.saleshelper  installer=com.huawei.appmarket
     *              package:com.huawei.android.dsdscardmanager  installer=null
     * adb shell pm list packages -u 只输出包和未安装包信息（安装来源）
     * adb shell pm list packages --user <USER_ID>，根据用户id查询用户的空间的所有包，USER_ID代表当前连接设备的顺序
     * adb shell pm list packages -e "ximalaya",只输出启用的包。
     * </pre>
     */
    public static List<String> getAppListByPM(Context context) {
        List<String> appList = new ArrayList<String>();
        try {
            String s = ShellHelper.getInstatnce().shell("pm list packages");
            String packageName = null;
            if (!TextUtils.isEmpty(s)) {
                if (s.contains("\n")) {
                    PackageManager pm = context.getPackageManager();
                    String ss[] = s.split("\n");
                    L.i(ss.length);
                    if (ss.length > 0) {
                        for (String line : ss) {
                            if (!TextUtils.isEmpty(line) && line.contains(":")) {
                                //                                L.d("line:"+line);
                                packageName = line.split(":")[1];
                                //                                L.i("packageName:"+packageName);

                                if (!TextUtils.isEmpty(packageName)) {
                                    if (pm.getLaunchIntentForPackage(packageName) != null) {
                                        // L.i("===>" + packageName);
                                        if (!appList.contains(packageName)) {
                                            appList.add(packageName);
                                        }
                                    } else {
                                        //   L.i("getAppListByPM===>"+packageName);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {

        }
        return appList;
    }

    /**
     * 3.getPackageManager().queryIntentActivities(intent,PackageManager.MATCH_ALL)
     * 华为等对应用安装列表有权限控制的手机上，采用隐式的Intent获取不到正确的信息，就连每个应用的启动Activity都获取不到。
     */
    public static List<String> getInstallByQueryIntentActivities(Context context) {
        List<String> appList = new ArrayList<String>();

        try {
            PackageManager pm = context.getPackageManager();
            // 查询所有已经安装的应用程序
            List<ApplicationInfo> appInfos =
                    pm.getInstalledApplications(
                            PackageManager.GET_UNINSTALLED_PACKAGES); // GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的
//      List<ApplicationInfo> applicationInfos = new ArrayList<ApplicationInfo>();

            // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 通过getPackageManager()的queryIntentActivities方法遍历,得到所有能打开的app的packageName
            List<ResolveInfo> resolveinfoList =
                    context.getPackageManager().queryIntentActivities(resolveIntent, 0);
            Set<String> allowPackages = new HashSet();
            for (ResolveInfo resolveInfo : resolveinfoList) {
                allowPackages.add(resolveInfo.activityInfo.packageName);
            }

            String packageName = null;
            for (ApplicationInfo app : appInfos) {

                packageName = app.packageName;
                if (!TextUtils.isEmpty(packageName)) {
                    if (allowPackages.contains(app.packageName)) {
                        //                    L.i("===>" + packageName);
                        if (!appList.contains(packageName)) {
                            appList.add(packageName);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            L.e(e);
        }
        return appList;
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }

    public static List<String> getApps(Context context) {
        List<String> apps = new ArrayList<String>();
        if (context == null) {
            return apps;
        }

        apps = getAppListByGetInstalledPackages(context);
        if (apps.size() <= 0) {
            apps = getAppListByPM(context);
        }

        if (apps.size() <= 0) {
            apps = getInstallByQueryIntentActivities(context);
        }
        return apps;
    }

    private static List<String> getRunningApps(Context context) {
        List<String> runningPackage = new ArrayList<String>();

        if (context == null) {
            return runningPackage;
        }

        List<String> apps = getInstallByQueryIntentActivities(context);
        if (apps.size() <= 0) {
            apps = getAppListByPM(context);
        }

        if (apps.size() <= 0) {
            apps = getAppListByGetInstalledPackages(context);
        }
        if (apps.size() > 0) {
            for (String packageName : apps) {
                if (SystemAppUtils.checkAppIsForeground(context, packageName)) {
                    if (!runningPackage.contains(packageName)) {
                        runningPackage.add(packageName);
                    }
                }
            }
        }
        return runningPackage;
    }
}
