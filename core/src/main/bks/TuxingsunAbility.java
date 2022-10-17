package com.txs;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.txs.impl.AccessibilityImpl;
import com.txs.model.MemoryHolder;
import com.txs.utils.ScreenSize;
import com.txs.utils.ServiceHolder;
import com.txs.utils.apps.AppListUtils;
import com.txs.utils.content.PubContext;
import com.txs.utils.log.L;
import com.txs.utils.log.Rom;
import com.txs.utils.log.SP;
import com.txs.utils.m.GestureUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Copyright © 2022 sanbo Inc. All rights reserved.
 * @Description: 土行孙的模块工具类
 * @Version: 1.0
 * @Create: 2022-10-17 14:58:03
 * @author: sanbo
 */
public class TuxingsunAbility extends AccessibilityService {

    // 内部debug页面
    public static final boolean DEBUG_TAG = true;
    private static final String OV_PASSWORD_KEY = "OV_PASSWORD_KEY";
    private static String curComponent = null;
    private static String password = "x12345678";
    private static Context mContext = null;
    // 安装中
    private static volatile boolean isInstalling = false;
    // 手机安装列表个数
    private static volatile int APP_LIST = -1;

    /**
     * 获取页面包名和类名
     *
     * @return
     */
    public static String getComponent() {
        return curComponent;
    }

    /**
     * 点击安装
     *
     * @param node
     */
    public static void setupClickForInstall(AccessibilityNodeInfo node) {
        if (DEBUG_TAG) {
            L.v("setupClickForInstall...isInstalling:" + isInstalling);
        }
        // 1. 判断是否结束
        if (hasComplete(node)) {
            isInstalling = false;
            return;
        }

        // 2. 标识开启
        if (!isInstalling) {
            isInstalling = true;
        }

        if (isInstalling) {
            // 3. 滑动就滑动
            //            if ("android.widget.ScrollView".equals(node.getClassName())) {
            //                node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            //            }
            // 是否有dialog
            //            boolean hasDialog = hasDialog(node);
            boolean containsDialog =
                    MemoryHolder.getInstance().hasDialog(node.getPackageName() + "/" + node.getClassName());
            // 4. 发现文字则点击
            List<String> texts = new ArrayList<String>();
            Map<String, AccessibilityNodeInfo> map = new HashMap<String, AccessibilityNodeInfo>();
            parser(node, texts, map);
            if (DEBUG_TAG) {
                L.d("setupClickForInstall 发现的文字: " + texts.toString());
            }

            // 5. 处理OPPO手机页面不能识别的情况， 底部的安装和取消是背景
            // 修改为运行中的app 导航跳过
            if (Rom.isOppo()) {
                // oppo 广告 警告
                boolean hasAdWarnning = false;
                // oppo 无风险安装
                boolean hasJustInstall = false;

                String clazzName = "";
                for (String tt : texts) {

                    try {
                        clazzName = map.get(tt).getPackageName().toString();
                        if ("com.android.packageinstaller".equals(clazzName)) {
                            if (tt.startsWith("广告插件：")
                                    || tt.startsWith("发现广告插件：")
                                    || tt.startsWith("删除安装包：")
                                    || tt.startsWith("我已知问题严重性。无视风险安装")) {
                                hasAdWarnning = true;
                            } else if (tt.startsWith("来自“")
                                    || tt.startsWith("大小：")
                                    || tt.startsWith("版本：")
                                    || tt.startsWith("我已知问题严重性。")) {
                                hasJustInstall = true;
                            }
                        }
                    } catch (Throwable e) {
                        if (DEBUG_TAG) {
                            L.v(e);
                        }
                    }
                }
                // 广告警告.详情见 imgs/oppo_dump_3914105703482907199.png.需要单独识别[无视风险安装]几个字，点击
                if (hasAdWarnning) {
                    if (DEBUG_TAG) {
                        L.i("oppo 安装界面中的 警告页面");
                    }
                }
                // 正常安装界面 imgs/oppo_dump_8370986014648322542.png
                if (hasJustInstall) {
                    if (DEBUG_TAG) {
                        L.i("oppo 安装界面中的 正常安装页面.....");
                    }
                    if (Build.VERSION.SDK_INT > 23) {
                        // 点击固定坐标
                        tryClickByPosition();
                    }
                }
            }

            // 6. 处理vivo手机警告安装情况
            if (Rom.isVivo()) {
                // 详情见 imgs/vivo_dump_3563595110006917732.png 和 imgs/vivo_dump_7742802218739782953.png
                String text = "我已知问题严重性，无视风险安装";
                if (texts.contains(text)
                        && "com.android.packageinstaller".equals(map.get(text).getPackageName())) {
                    if (DEBUG_TAG) {
                        L.i("vivo 遇到风险页面了");
                    }
                    if (Build.VERSION.SDK_INT > 23) {
                        //                        clickPostion(800, 900);
                    }
                }
            }
            // 7. 正常处理情况
            for (int i = 0; i < texts.size(); i++) {
                try {
                    String tt = texts.get(i);
                    if (!TextUtils.isEmpty(tt)) {
                        if (PubContext.installAndNavigationTexts.contains(tt)) {
                            AccessibilityNodeInfo nn = map.get(tt);
                            //                            if (DEBUG_TAG) {
                            //                                L.d("发现文字[" + nn.getClassName() + "]: " + tt);
                            //                            }
                            if ("关闭".equalsIgnoreCase(tt.trim())
                                    || "开启".equalsIgnoreCase(tt.trim())
                                    || "设置".equalsIgnoreCase(tt.trim())) {
                                if (containsDialog) {
                                    //                                    L.i("点击...A [有dialog]...因为文字[" + nn
                                    // .getClassName() + "]:" + tt);
                                    performViewClick(nn);
                                }
                            } else {
                                //                                L.i("点击...B...因为文字[" + nn.getClassName() + "]:"
                                // + tt);
                                performViewClick(nn);
                            }
                        }
                    }
                } catch (Throwable e) {
                    L.e(Log.getStackTraceString(e));
                }
            } // end with for earchna
        }
    }

    /** ************************************************************************************* */
    /** ************************************ 工具方法 **************************************** */
    /**
     * ************************************************************************************
     */

    private static void tryClickByPosition() {
        L.i("===============dianji。。。。。");
        int[] sizes = ScreenSize.getScreenSize(TuxingsunAbility.mContext);
        if (sizes.length == 2) {
            int w = sizes[0];
            int h = sizes[1];

            int x = w * 2 / 3;
            int y = h - 150;
            L.i("原始分辨率[%dx%d]--点击[%d-%d]", w, h, x, y);
            try {
                GestureUtil.tap(String.valueOf(x), String.valueOf(y));
            } catch (Throwable e) {
            }
        }
    }

    /**
     * 判断是否结束
     *
     * @param info
     * @return
     */
    private static boolean hasComplete(AccessibilityNodeInfo info) {
        /** 1. 判断安装列表 */
        if (APP_LIST != -1) {
            int currentSize = AppListUtils.getApps(mContext).size();
            if (APP_LIST != currentSize) {
                APP_LIST = currentSize;
                return true;
            }
        } else {
            APP_LIST = AppListUtils.getApps(mContext).size();
        }
        /** 2. 文字匹配 */
        List<String> texts = new ArrayList<String>();
        if (DEBUG_TAG) {
            L.v("hasComplete 发现的文字: " + texts.toString());
        }
        Map<String, AccessibilityNodeInfo> map = new HashMap<String, AccessibilityNodeInfo>();
        parser(info, texts, map);
        for (String text : texts) {
            for (String ct : PubContext.completeTexts) {
                if (ct.equals(text)) {
                    //                    L.i("点击..结束..因为文字 :" + text);
                    performViewClick(map.get(text));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 界面搜索结果
     *
     * @param info
     * @param result
     * @param map
     */
    public static void parser(
            AccessibilityNodeInfo info, List<String> result, Map<String, AccessibilityNodeInfo> map) {

        try {
            CharSequence cc = info.getText();
            if (cc != null) {
                String text =
                        cc.toString()
                                .trim()
                                .replaceAll("\t", "")
                                .replaceAll("\r", "")
                                .replaceAll("\n", "")
                                .replaceAll("\r\n", "");
                result.add(text);
                map.put(text, info);
            } else {
                if (DEBUG_TAG) {
                    if (info.isClickable()) {
                        L.v("空且可点击: " + info.toString());
                    }
                }
                //                parserNull(info);
            }

            // 貌似不好使。为兼容OPPO 7以下版本
            if (Build.VERSION.SDK_INT > 17) {
                if (DEBUG_TAG) {
                    L.i("ID：" + info.getViewIdResourceName());
                }
                if ("com.android.packageinstaller:id/bottom_button_layout"
                        .equals(info.getViewIdResourceName())) {
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        } catch (Throwable e) {
            if (DEBUG_TAG) {
                L.v(e);
            }
        }
        try {

            if (DEBUG_TAG) {
                L.v("parser:" + info.toString());
            }
            // 子父节点循环遍历
            int count = info.getChildCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    AccessibilityNodeInfo child = info.getChild(i);
                    if (child != null) {
                        parser(child, result, map);
                    }
                }
            }
        } catch (Throwable e) {
            if (DEBUG_TAG) {
                L.v(e);
            }
        }
    }

    /**
     * 填写密码
     *
     * @param node
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void fillPassword(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo editText = node.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (editText == null) {
            return;
        }

        if ("android.widget.EditText".equals(editText.getClassName())) {
            inputText(editText, password);
        }

        List<AccessibilityNodeInfo> nodeInfoList = node.findAccessibilityNodeInfosByText("确定");
        for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        nodeInfoList = node.findAccessibilityNodeInfosByText("确认");
        for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        nodeInfoList = node.findAccessibilityNodeInfosByText("安装");
        for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        nodeInfoList = node.findAccessibilityNodeInfosByText("好");
        for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        nodeInfoList = node.findAccessibilityNodeInfosByText("登录");
        for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    /**
     * 模拟输入文字
     *
     * @param nodeInfo nodeInfo
     * @param text     text
     */
    public static void inputText(AccessibilityNodeInfo nodeInfo, String text) {
        if (DEBUG_TAG) {
            L.v("inputText [" + text + "] =====> " + nodeInfo.toString());
        }
        // 21  5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            // 18 4.3
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ClipboardManager clipboard =
                    (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", text);
            clipboard.setPrimaryClip(clip);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }

    /**
     * 模拟点击事件，全局只有一个点击事件
     *
     * @param nodeInfo nodeInfo
     */
    public static void performViewClick(AccessibilityNodeInfo nodeInfo) {
        if (DEBUG_TAG) {
            L.v("performViewClick" + nodeInfo.toString());
        }
        if (nodeInfo == null) {
            return;
        }
        while (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
            nodeInfo = nodeInfo.getParent();
        }
    }

    @TargetApi(16)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        ServiceHolder.getInstance().setService(this);
        if (DEBUG_TAG) {
            L.v("--onAccessibilityEvent--event:" + event);
        }
        mContext = getApplicationContext();
        try {
            getInstallPwd();
            if (APP_LIST == -1) {
                APP_LIST = AppListUtils.getApps(this).size();
            }
            if (event != null) {
                AccessibilityNodeInfo info = getRootInActiveWindow();
                if (info != null) {
                    initPageName(info);
                    parserPageAndSupportAutoInstall(event, info);
                }
            }
        } catch (Throwable e) {
            L.e(Log.getStackTraceString(e));
        }
    }

    @Override
    public void onInterrupt() {
        ServiceHolder.getInstance().setService(this);
    }


    /**
     * 设置当前的页面
     *
     * @param info
     */
    private void initPageName(AccessibilityNodeInfo info) {
        curComponent = info.getPackageName() + "/" + info.getClassName();
    }

    /**
     * 接续和自动化安装跳过导航入口
     *
     * @param event
     * @param info
     */
    private void parserPageAndSupportAutoInstall(
            AccessibilityEvent event, AccessibilityNodeInfo info) {
        // 页面变化或者内存变化
        int eventType = event.getEventType();
        if (DEBUG_TAG) {
            L.v(AccessibilityEvent.eventTypeToString(eventType));
        }
        /*
         * 只有页面/页面内容变化的时才查看元素。
         */
        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                || eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // 解析对象到内存
            MemoryHolder.getInstance().parser(event, info);
            // 处理引导页面和自动安装
            AccessibilityImpl.process(this, event, info);
        }
    }

    /**
     * 获取安装密码
     */
    private void getInstallPwd() {
        String pwd = (String) SP.getParam(OV_PASSWORD_KEY, "x12345678");
        if (!TextUtils.isEmpty(pwd)) {
            password = pwd;
        }
    }
}
