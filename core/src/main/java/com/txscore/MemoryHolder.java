package com.txscore;

import android.os.Build;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.txscore.model.MemoryModel;
import com.txscore.utils.log.L;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Copyright © 2018 Analysys Inc. All rights reserved.
 * @Description 内存持有.
 * @Version 1.0
 * @Create 2018/11/29 18:08
 * @Author sanbo
 */
public class MemoryHolder {

    public static String ACTIONS_ACTIONS = "actions";
    public static String ACTIONS_ACTIVITY = "activity";
    public static String ACTIONS_XPATH = "xpath";
    public static String ACTIONS_ACTION = "action";
    public static String ACTIONS_ACTION_CLICK = "CLICK";
    public static String ACTIONS_ACTION_INPUTTEXT = "INPUTTEXT";
    public static String ACTIONS_TEXT = "text";
    public static String ACTIONS_THROTTLE = "throttle";
    /**
     * 内存里的模型。 key是 `class` . value是模型
     */
    private Map<String, MemoryModel> memoryModels = new HashMap<String, MemoryModel>();
    /**
     * 内存里的模型。没包名和类名的列表
     */
    private List<MemoryModel> nullModels = new ArrayList<MemoryModel>();
    private JSONObject mMemoryMatchXpathData = new JSONObject();

    private MemoryHolder() {
    }

    public static MemoryHolder getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Xpath
     *
     * @param arr
     */
    public void setMemoryMatchXpathData(JSONArray arr) {
        if (arr != null && arr.length() > 0) {
            for (int i = 0; i < arr.length(); i++) {
                try {
                    JSONObject obj = arr.getJSONObject(i);
                    if (obj != null && obj.length() > 0) {
                        if (obj.has(ACTIONS_ACTIVITY)) {
                            String activity = obj.getString(ACTIONS_ACTIVITY);
                            if (!TextUtils.isEmpty(activity)) {
                                mMemoryMatchXpathData.put(activity, obj);
                            }
                        }
                    }
                } catch (Throwable e) {
                    L.e();
                }
            }
        }
    }

    /**
     * 解析数据到对象中
     *
     * @param event
     * @param info
     */
    public void parser(AccessibilityEvent event, AccessibilityNodeInfo info) {

        // 获取类名
        CharSequence pkg = event.getPackageName();
        CharSequence clazz = event.getClassName();

        MemoryModel model = new MemoryModel();
        // 循环解析对象
        parserNode(info, model);
        // 解析对象到内存中
        parserToMemory(model, pkg, clazz);

        // xpath 解析
        checkXpath(clazz);
    }

    /**
     * 解析的东西放内存里
     *
     * @param model
     * @param pkg
     * @param clazz
     */
    private void parserToMemory(MemoryModel model, CharSequence pkg, CharSequence clazz) {
        if (TuxingsunAbility.DEBUG_TAG) {
            L.v("parserToMemory.  " + pkg + "/" + clazz);
        }
        if (!TextUtils.isEmpty(pkg)) {
            model.setPackageName(pkg);
        }
        if (!TextUtils.isEmpty(clazz)) {
            String clz = clazz.toString();
            model.setClassName(clazz);
            memoryModels.put(clz, model);
        } else {
            nullModels.add(model);
        }
    }

    /**
     * 循环遍历
     *
     * @param info
     * @param model
     */
    private void parserNode(AccessibilityNodeInfo info, MemoryModel model) {
        if (info == null) {
            return;
        }
        CharSequence text = info.getText();
        CharSequence cd = info.getContentDescription();
        CharSequence clazz = info.getClassName();
        CharSequence pkg = info.getPackageName();
        boolean isClickable = info.isClickable();

        model.setInfo(info);
        if (Build.VERSION.SDK_INT > 17) {
            if (info.isEditable()) {
                model.setEditableNode(info);
            }
        }

        if (!TextUtils.isEmpty(text) && info != null) {
            model.setTextAndNode(text.toString(), info);
        } else {
            if (isClickable) {
                model.setNullClickableNode(info);
            }
        }
        if (!TextUtils.isEmpty(cd)) {
            model.setContentDescAndNode(cd.toString(), info);
        } else {
            if (isClickable) {
                model.setNullClickableNode(info);
            }
        }

        if (!TextUtils.isEmpty(pkg)) {
            model.setPackageName(pkg);
        }
        if (!TextUtils.isEmpty(clazz)) {
            model.setClazzAndNode(clazz.toString(), info);
        }
        if (info.isCheckable()) {
            model.setCheckableNode(info);
        }
        if (info.isChecked()) {
            model.setCheckedNode(info);
        }

        if (info.isScrollable()) {
            model.setScrollableNode(info);
        }
        if (info.isSelected()) {
            model.setSelectedNode(info);
        }
        if (info.isPassword()) {
            model.setPasswordNode(info);
        }
        if (info.isLongClickable()) {
            model.setLongClickableNode(info);
        }
        if (info.isFocusable()) {
            model.setFocusableNode(info);
        }
        if (info.isFocused()) {
            model.setFocusedNode(info);
        }

        // 遍历子控件
        int count = info.getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                AccessibilityNodeInfo chlid = info.getChild(i);
                parserNode(chlid, model);
            }
        }
    }

    /**
     * xpath解析对比
     *
     * @param clazz
     */
    private void checkXpath(CharSequence clazz) {
        if (TuxingsunAbility.DEBUG_TAG) {
            L.d("inside checkXpath class:" + clazz);
        }
        if (!TextUtils.isEmpty(clazz)) {
            String clz = clazz.toString();
            if (TuxingsunAbility.DEBUG_TAG) {
                L.d(
                        "checkXpath 内存中xpath包含该类:"
                                + mMemoryMatchXpathData.has(clz)
                                + " ;内存有该类模型: "
                                + memoryModels.containsKey(clz));
            }
            if (mMemoryMatchXpathData.has(clz) && memoryModels.containsKey(clz)) {

                if (TuxingsunAbility.DEBUG_TAG) {
                    L.i("checkXpath 内存中有该类的xpath: " + clz);
                }
                try {
                    JSONObject obj = mMemoryMatchXpathData.getJSONObject(clz);
                    if (obj == null || obj.length() <= 0) {
                        return;
                    }
                    // L.i("333333");
                    JSONArray actions = obj.getJSONArray(ACTIONS_ACTIONS);
                    if (actions == null || actions.length() <= 0) {
                        return;
                    }
                    // L.i("4444444");

                    for (int i = 0; i < actions.length(); i++) {

                        JSONObject one = actions.getJSONObject(i);
                        String xpath = one.optString(ACTIONS_XPATH);
                        if (TextUtils.isEmpty(xpath)) {
                            return;
                        }
                        // L.d("memoryModels: " + memoryModels.toString());

                        MemoryModel targetModel = memoryModels.get(clz);
                        // L.d("targetModel: " + targetModel.toStringXpath());

                        // debug();
                        List<AccessibilityNodeInfo> infos = targetModel.getNodeByXpath(xpath);

                        if (infos == null) {
                            continue;
                        }

                        if (infos.size() == 0) {
                            continue;
                        }

                        // 获取目标组件
                        AccessibilityNodeInfo info = infos.get(0);
                        if (info == null) {
                            continue;
                        }
                        if (TuxingsunAbility.DEBUG_TAG) {
                            L.i("根据Xpath获取到组件. xpath:" + xpath);
                        }
                        if (one != null && one.length() > 0) {
                            String action = one.optString(ACTIONS_ACTION);
                            if (ACTIONS_ACTION_CLICK.equalsIgnoreCase(action)) {
                                // 点击
                                if (TuxingsunAbility.DEBUG_TAG) {
                                    L.i(" 点击。。。");
                                }
                                TuxingsunAbility.performViewClick(info);
                            } else if (ACTIONS_ACTION_INPUTTEXT.equalsIgnoreCase(action)) {
                                // 字符输入
                                String tx = one.optString(ACTIONS_TEXT);
                                if (TuxingsunAbility.DEBUG_TAG) {
                                    L.i(" 字符输入。。。" + tx);
                                }
                                TuxingsunAbility.inputText(info, tx);
                            }
                        }
                    }
                } catch (Throwable e) {
                    L.e(e);
                }
            }
        }
    }

    /**
     * 是否有dialog
     *
     * @param component
     * @return
     */
    public boolean hasDialog(String component) {
        if (memoryModels.containsKey(component)) {
            MemoryModel model = memoryModels.get(component);
            List<String> clsz = model.getClazz();
            if (clsz.size() > 0) {
                return clsz.contains("android.app.Dialog")
                        || clsz.contains("android.support.v7.app.AlertDialog");
            }
        }
        return false;
    }

    //    /**
    //     * 测试方法
    //     */
    //    private void debug(String xpath, MemoryModel targetModel) {
    //        Map<String, List<AccessibilityNodeInfo>> map = targetModel.getXpathAndNotes();
    //        for (String key : map.keySet()) {
    ////            L.w("[" + key.equalsIgnoreCase(xpath) + "]   " + key + "-----" + xpath);
    ////            L.w("[" + (key == xpath) + "]   " + key + "-----" + xpath);
    ////            L.w("key:" + key);
    ////            for (String k : keyss) {
    ////                L.i(key + "<--->" + k);
    ////                L.wtf("result:" + key.equalsIgnoreCase(k));
    ////                L.wtf("result1111:" + key == k);
    ////            }
    //
    //            if (key.equalsIgnoreCase(xpath)) {
    //                List<AccessibilityNodeInfo> infos = map.get(key);
    //                L.wtf("infos:" + infos.size());
    //                // 处理第一个
    //            }
    //        }
    //        List<AccessibilityNodeInfo> infos = targetModel.getNodeByXpath(xpath);
    //        if (infos == null) {
    //            return;
    //        }
    //        L.i(xpath + "=====>" + infos.toString());
    //    }

    /**
     * 清除内存中的数据
     *
     * @return
     */
    public boolean clear() {
        memoryModels.clear();
        nullModels.clear();
        return memoryModels.size() == 0 && nullModels.size() == 0;
    }

    private static class Holder {
        public static final MemoryHolder INSTANCE = new MemoryHolder();
    }
}
