package com.tuxingsunlib.impl;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.tuxingsunlib.MyAccessibilityService;
import com.tuxingsunlib.utils.MContexts;
import com.tuxingsunlib.utils.log.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Copyright © 2018 Analysys Inc. All rights reserved. @Description: TODO @Version: 1.0 @Create:
 * 2018/10/22 17:23 @Author: sanbo
 */
public class AccessibilityImpl {

    // 安装包名
    public static ArrayList<String> INSTALL_PKGS =
            new ArrayList<String>(
                    Arrays.asList(
                            "com.samsung.android.packageinstaller",
                            "com.android.packageinstaller",
                            "com.google.android.packageinstaller",
                            "com.lenovo.safecenter",
                            "com.lenovo.security",
                            "com.htc.htcappopsguarddog",
                            "com.xiaomi.gamecenter",
                            "cn.goapk.market",
                            //                            "com.vivo.secime.service",
                            //                            "com.coloros.safecenter",
                            //                            "com.bbk.account",
                            "com.samsung.android.packageinstaller" // 三星
                    ));
    private static Context mContext = null;
    // 安装页面
    private static ArrayList<String> INSTALL_PAGES =
            new ArrayList<String>(
                    Arrays.asList(
                            "com.androidtalle.packageinstaller.PackageInsrActivity",
                            "com.android.packageinstaller.OppoPackageInstallerActivity",
                            "com.android.packageinstaller.InstallAppProgress",
                            "com.lenovo.safecenter.install.InstallerActivity",
                            "com.lenovo.safecenter.defense.install.fragment.InstallInterceptActivity",
                            "com.lenovo.safecenter.install.InstallProgress",
                            "com.lenovo.safecenter.install.InstallAppProgress",
                            "com.android.packageinstaller.PackageInstallerActivity",
                            "com.lenovo.safecenter.defense.fragment.install.InstallInterceptActivity",
                            "com.htc.htcappopsguarddog.HtcAppOpsDetailsActivity"));

    // 卸载包名
    private static ArrayList<String> REMOVE_PKGS = new ArrayList<String>();

    // 密码框
    private static ArrayList<String> PWD_DIALOG_PKG =
            new ArrayList<String>(
                    Arrays.asList(
                            "com.bbk.account" // vivo  Z1、vivo Y69A、vivo X21A
                            ,
                            "com.coloros.safecenter" // vivo R9s、oppo  A59S
                            ,
                            "com.oppo.usercenter" // vivo R9s、oppo  A59S
                            ,
                            "com.vivo.secime.service" // vivo的手机--暂时不记得机型
                    ));

    // 安装关键字.(识别是警告框),暂时针对特殊的手机机型vivo Y23L (4.4.4)
    private static ArrayList<String> INSTALL_STEP_TEXT =
            new ArrayList<String>(
                    Arrays.asList(
                            "好" // 好
                    ));
    private static ArrayList<String> DIALOG_TEXT =
            new ArrayList<String>(Arrays.asList("“电脑端未...”", "需要您验证身份后安装。", "取消", "安装"));

    /**
     * 根据包名判断任务
     *
     * @param context
     * @param event
     * @param node
     */
    public static void process(
            Context context, AccessibilityEvent event, AccessibilityNodeInfo node) {

        if (node == null) {
            return;
        }
        if (mContext == null) {
            if (context != null) {
                mContext = context.getApplicationContext();
            } else {
                mContext = MContexts.CONTEXT;
            }
        }
        if (mContext == null) {
            if (MyAccessibilityService.DEBUG_TAG) {
                L.w("AccessibilityImpl.process failed. the context is null!");
            }
            return;
        }
        String pkgName = node.getPackageName().toString();
        String className = node.getClassName().toString();

        // 0. 手机禁止安装

        // 1. oppo/vivo会密码窗
        //        if (Rom.isVivo() || Rom.isVivo()) {
        if (hasPasswordArea(node)) {
            MyAccessibilityService.fillPassword(node);
        }
        //    }
        // 2.判断具体操作
        if (INSTALL_PKGS.contains(pkgName) || INSTALL_PAGES.contains(className)) {
            if (MyAccessibilityService.DEBUG_TAG) {
                L.d("will 安装~~");
            }
            // 安装
            installApp(event, node);
        } else if (REMOVE_PKGS.contains(pkgName)) {
            if (MyAccessibilityService.DEBUG_TAG) {
                L.d("will 卸载~~");
            }
            // 卸载
            removeApp(event, node);
        } else {

            // 兼容包名不对的
            if (!hasInstallTexts(node)) {
                //                if (MyAccessibilityService.DEBUG_TAG) {
                //                    L.d("不支持的~~");
                //                }
                // 暂时不支持的,保存本地
                saveToNative(event);
            }
            // 确定下是否有安装任务
            processInstall(event, node);
        }
    }

    /** ************************************************************************************* */
    /** ********************************** 第一级任务 ************************************* */
    /** ************************************************************************************* */

    /**
     * 安装app
     *
     * @param event
     * @param node
     */
    private static void installApp(AccessibilityEvent event, AccessibilityNodeInfo node) {

        // 处理安装中的处理
        processInstall(event, node);
    }

    /**
     * 卸载APP
     *
     * @param event
     * @param node
     */
    private static void removeApp(AccessibilityEvent event, AccessibilityNodeInfo node) {
    }

    /**
     * 不支持的处理方式，保存到本地(包括手机型号，版本号，app名称，app版本号，页面信息，甚至可以包括截图)，服务器mysql-ok可以上传
     *
     * @param event
     */
    private static void saveToNative(AccessibilityEvent event) {
        String pkgName = event.getPackageName().toString();
    }

    /** ************************************************************************************* */
    /** *************************** 二级任务:安装细节方式 ************************************* */
    /** ************************************************************************************* */

    /**
     * @param event
     * @param node
     */
    private static void processInstall(AccessibilityEvent event, AccessibilityNodeInfo node) {
        MyAccessibilityService.setupClickForInstall(node);
    }
    /** ************************************************************************************* */
    /** *************************** 三级任务: 详细实现方式 ************************************* */
    /** ************************************************************************************* */

    /**
     * 判断是否有密码框
     *
     * @param node
     * @return
     */
    private static boolean hasPasswordArea(AccessibilityNodeInfo node) {
        String pkgName = node.getPackageName().toString();
        if (PWD_DIALOG_PKG.contains(pkgName)) {
            return true;
        }
        List<String> result = new ArrayList<String>();
        Map<String, AccessibilityNodeInfo> map = new HashMap<String, AccessibilityNodeInfo>();
        MyAccessibilityService.parser(node, result, map);

        return result.contains(DIALOG_TEXT.get(1))
                && result.contains(DIALOG_TEXT.get(2))
                && result.contains(DIALOG_TEXT.get(3));
    }

    /**
     * 判断是否有关键字
     *
     * @param node
     * @return
     */
    private static boolean hasInstallTexts(AccessibilityNodeInfo node) {
        List<String> arr = new ArrayList<String>();
        Map<String, AccessibilityNodeInfo> map = new HashMap<String, AccessibilityNodeInfo>();
        MyAccessibilityService.parser(node, arr, map);
        for (String key : arr) {
            if (INSTALL_STEP_TEXT.contains(key)) {
                MyAccessibilityService.performViewClick(map.get(key));
                return true;
            }
        }
        return false;
    }
}
