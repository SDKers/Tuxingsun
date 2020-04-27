package com.tuxingsunlib.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.tuxingsunlib.utils.log.L;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

/**
 * @Copyright © 2017 sanbo Inc. All rights reserved.
 * @Description: 跟App相关的辅助类
 * @Version: 1.0
 * @Create: 2017-6-2 下午9:56:23
 * @Author: sanbo
 */
public class AppUtils {

  /**
   * 通过包名获取应用程序的名称。
   *
   * @param context Context对象。
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
      //            e.printStackTrace();
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

  /** 通过隐式意图调用系统安装程序安装APK */
  public static void install(Context context, String filePath) {
    //        File file = new File(filePath);
    //        Intent intent = new Intent(Intent.ACTION_VIEW);
    //        // 由于没有在Activity环境下启动Activity,设置下面的标签
    //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //        if (Build.VERSION.SDK_INT >= 24) { // 判读版本是否在7.0以上
    //            // 参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致 参数3 共享的文件
    //            Uri apkUri = FileProvider.getUriForFile(context,
    // "com.a520wcf.chapter11.fileprovider", file);
    //            // 添加这一句表示对目标应用临时授权该Uri所代表的文件
    //            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    //            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
    //        } else {
    //            intent.setDataAndType(Uri.fromFile(file),
    // "application/vnd.android.package-archive");
    //        }
    //        context.startActivity(intent);
  }

  /**
   * 智能安装
   *
   * @param context
   * @param apkPath
   */
  public static void smartInstall(Context context, String apkPath) {
    Uri uri = Uri.fromFile(new File(apkPath));
    Intent localIntent = new Intent(Intent.ACTION_VIEW);
    localIntent.setDataAndType(uri, "application/vnd.android.package-archive");
    context.startActivity(localIntent);
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

  /** 通过包名去启动一个Activity */
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
    List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
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

  /**
   * 获取系统中是否安装某些应用
   *
   * @param context 内容上下文
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

  /** 获取应用程序名称 */
  public static String getAppName(Context context) {
    try {
      PackageManager packageManager = context.getPackageManager();
      PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
      int labelRes = packageInfo.applicationInfo.labelRes;
      return context.getResources().getString(labelRes);
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }
    return null;
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

  /**
   * 判断当前应用是否具有指定的权限
   *
   * @param context
   * @param permission 权限信息的完整名称 如：<code>android.permission.INTERNET</code>
   * @return 当前仅当宿主应用含有 参数 permission 对应的权限 返回true 否则返回 false
   */
  public static boolean checkPermission(Context context, String permission) {
    boolean result = false;
    if (Build.VERSION.SDK_INT >= 23) {
      try {
        Class<?> clazz = Class.forName("android.content.Context");
        Method method = clazz.getMethod("checkSelfPermission", String.class);
        int rest = (Integer) method.invoke(context, permission);
        result = rest == PackageManager.PERMISSION_GRANTED;
      } catch (Throwable e) {
        result = false;
      }
    } else {
      PackageManager pm = context.getPackageManager();
      if (pm.checkPermission(permission, context.getPackageName())
          == PackageManager.PERMISSION_GRANTED) {
        result = true;
      }
    }
    return result;
  }

  /**
   * Method to reflectively invoke the SystemProperties.get command - which is the equivalent to the
   * adb shell getProp command.
   *
   * @param context A {@link Context} object used to get the proper ClassLoader (just needs to be
   *     Application Context object)
   * @param property A {@code String} object for the property to retrieve.
   * @return {@code String} value of the property requested.
   */
  public static String getProp(Context context, String property) {
    try {
      Class<?> systemProperties = context.getClassLoader().loadClass("android.os.SystemProperties");

      Method get = systemProperties.getMethod("get", String.class);

      Object[] params = new Object[1];
      params[0] = new String(property);

      return (String) get.invoke(systemProperties, params);
    } catch (Exception igone) {
      return null;
    }
  }

  /**
   * 通过/system/build.prop获取对应对
   *
   * @param propertyKey
   * @return
   */
  public static String getBuildProp(String propertyKey) {
    Properties pts = getBuildProp();
    if (pts.size() > 0) {
      return pts.getProperty(propertyKey);
    } else {
      return null;
    }
  }

  private static Properties getBuildProp() {
    Properties prop = new Properties();
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
      prop.load(fis);
    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (Throwable e) {
        }
      }
    }
    return prop;
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
   * Check if the normal method of "isUserAMonkey" returns a quick win of who the user is.
   *
   * @return {@code true} if the user is a Maxim or {@code false} if not.
   */
  public static boolean isUseMonkey() {
    return ActivityManager.isUserAMonkey();
  }

  /** Believe it or not, there are packers that use this... */
  public static boolean isBeingDebugged() {
    return Debug.isDebuggerConnected();
  }

  /** 分享Apk信息 */
  public void shareApkInfo(Context context, String info) {
    Intent intent = new Intent();
    intent.setAction("android.intent.action.SEND");
    intent.addCategory("android.intent.category.DEFAULT");
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT, info);
    context.startActivity(intent);
  }
}
