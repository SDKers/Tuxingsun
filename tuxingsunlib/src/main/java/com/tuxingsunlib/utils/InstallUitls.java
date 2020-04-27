package com.tuxingsunlib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

public class InstallUitls {

    /**
     * 通过隐式意图调用系统安装程序安装APK
     */
    public static void install(Context context, String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { // 判读版本是否在7.0以上
            // 参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致 参数3 共享的文件
            Uri apkUri = FileProvider.getUriForFile(context,
                    "com.a520wcf.chapter11.fileprovider", file);
            // 添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
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
     * 检查系统设置：是否允许安装来自未知来源的应用
     */
    private static boolean isSettingOpen(Context cxt) {
        boolean canInstall;
        // Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canInstall = cxt.getPackageManager().canRequestPackageInstalls();
        } else {
            canInstall = Settings.Secure.getInt(cxt.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 0) == 1;
        }
        return canInstall;
    }

    /**
     * 跳转到系统设置：允许安装来自未知来源的应用
     */
    private static void jumpToInstallSetting(Context cxt) {
        // Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            cxt.startActivity(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + cxt.getPackageName())));
        } else {
            cxt.startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
        }
    }

//    /**
//     * 安装APK
//     *
//     * @param apkFile APK文件的本地路径
//     */
//    public static void install(Context cxt, File apkFile) {
//        AccessibilityUtil.wakeUpScreen(cxt); //唤醒屏幕,以便辅助功能模拟用户点击"安装"
//        try {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            Uri uri;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                // Android 7.0以上不允许Uri包含File实际路径，需要借助FileProvider生成Uri（或者调低targetSdkVersion小于Android 7.0欺骗系统）
//                uri = FileProvider.getUriForFile(cxt, cxt.getPackageName() + ".fileProvider", apkFile);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            } else {
//                uri = Uri.fromFile(apkFile);
//            }
//            intent.setDataAndType(uri, "application/vnd.android.package-archive");
//            cxt.startActivity(intent);
//        } catch (Throwable e) {
//            Toast.makeText(cxt, "安装失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }
}
