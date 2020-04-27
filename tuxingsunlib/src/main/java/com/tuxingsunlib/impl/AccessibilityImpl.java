package com.tuxingsunlib.impl;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.tuxingsunlib.SanboAbility;
import com.tuxingsunlib.utils.ContextHolder;
import com.tuxingsunlib.utils.content.PubText;
import com.tuxingsunlib.utils.log.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Copyright © 2018 Analysys Inc. All rights reserved.
 * @Description: TODO
 * @Version: 1.0
 * @Create: 2018/10/22 17:23
 * @Author: sanbo
 */
public class AccessibilityImpl {

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
        context = ContextHolder.getContext(context);
        if (context == null) {
            if (SanboAbility.DEBUG_TAG) {
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
            SanboAbility.fillPassword(node);
        }
        //    }
        // 2.判断具体操作
        if (PubText.INSTALL_PKGS.contains(pkgName) || PubText.INSTALL_PAGES.contains(className)) {
            if (SanboAbility.DEBUG_TAG) {
                L.d("will 安装~~");
            }
            // 安装
            installApp(event, node);
        } else if (PubText.REMOVE_PKGS.contains(pkgName)) {
            if (SanboAbility.DEBUG_TAG) {
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
        SanboAbility.setupClickForInstall(node);
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
        if (PubText.PWD_DIALOG_PKG.contains(pkgName)) {
            return true;
        }
        List<String> result = new ArrayList<String>();
        Map<String, AccessibilityNodeInfo> map = new HashMap<String, AccessibilityNodeInfo>();
        SanboAbility.parser(node, result, map);

        return result.contains(PubText.DIALOG_TEXT.get(1))
                && result.contains(PubText.DIALOG_TEXT.get(2))
                && result.contains(PubText.DIALOG_TEXT.get(3));
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
        SanboAbility.parser(node, arr, map);
        for (String key : arr) {
            if (PubText.INSTALL_STEP_TEXT.contains(key)) {
                SanboAbility.performViewClick(map.get(key));
                return true;
            }
        }
        return false;
    }
}
