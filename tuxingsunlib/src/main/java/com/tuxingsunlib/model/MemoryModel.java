package com.tuxingsunlib.model;

import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.tuxingsunlib.SanboAbility;
import com.tuxingsunlib.utils.log.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Copyright © 2018 Analysys Inc. All rights reserved.
 * @Description TODO
 * @Version 1.0
 * @Create 2018/11/30 10:29
 * @Author sanbo
 */
public class MemoryModel {
    private String mPackageName = null;
    private String mClassName = null;

    // 默认所有元素
    private AccessibilityNodeInfo info = null;

    // 文字和对应的元素
    private List<String> text = new ArrayList<String>();
    private Map<String, List<AccessibilityNodeInfo>> textAndNodes =
            new HashMap<String, List<AccessibilityNodeInfo>>();

    // 描述文字
    private List<String> contentDesc = new ArrayList<String>();
    private Map<String, List<AccessibilityNodeInfo>> contentDescAndNodes =
            new HashMap<String, List<AccessibilityNodeInfo>>();

    // 所属的类
    private List<String> clazz = new ArrayList<String>();
    private Map<String, List<AccessibilityNodeInfo>> clazzAndNodes =
            new HashMap<String, List<AccessibilityNodeInfo>>();
    /**
     * xpath
     */
    private Map<String, List<AccessibilityNodeInfo>> xpathAndNodes =
            new HashMap<String, List<AccessibilityNodeInfo>>();

    // 可点击的节点
    private List<AccessibilityNodeInfo> clickableNode = new ArrayList<AccessibilityNodeInfo>();

    // 空文字且可以点击的组件。
    private List<AccessibilityNodeInfo> nullClickableNode = new ArrayList<AccessibilityNodeInfo>();

    /**
     * 可点击/滑动/操作系列
     */
    private List<AccessibilityNodeInfo> checkableNode = new ArrayList<AccessibilityNodeInfo>();

    private List<AccessibilityNodeInfo> checkedNode = new ArrayList<AccessibilityNodeInfo>();
    private List<AccessibilityNodeInfo> editableNode = new ArrayList<AccessibilityNodeInfo>();
    private List<AccessibilityNodeInfo> scrollableNode = new ArrayList<AccessibilityNodeInfo>();
    private List<AccessibilityNodeInfo> selectedNode = new ArrayList<AccessibilityNodeInfo>();
    private List<AccessibilityNodeInfo> passwordNode = new ArrayList<AccessibilityNodeInfo>();
    private List<AccessibilityNodeInfo> longClickableNode = new ArrayList<AccessibilityNodeInfo>();
    private List<AccessibilityNodeInfo> focusableNode = new ArrayList<AccessibilityNodeInfo>();
    private List<AccessibilityNodeInfo> focusedNode = new ArrayList<AccessibilityNodeInfo>();

    public List<AccessibilityNodeInfo> getNodeByXpath(String xpath) {
        if (xpathAndNodes.containsKey(xpath)) {
            return xpathAndNodes.get(xpath);
        }
        return null;
    }

    public List<AccessibilityNodeInfo> getCheckableNode() {
        return checkableNode;
    }

    public void setCheckableNode(AccessibilityNodeInfo checkableNode) {
        if (!this.checkableNode.contains(checkableNode)) {
            this.checkableNode.add(checkableNode);
        }
    }

    public List<AccessibilityNodeInfo> getCheckedNode() {
        return checkedNode;
    }

    public void setCheckedNode(AccessibilityNodeInfo checkedNode) {
        if (!this.checkedNode.contains(checkedNode)) {
            this.checkedNode.add(checkedNode);
        }
    }

    public List<AccessibilityNodeInfo> getEditableNode() {
        return editableNode;
    }

    public void setEditableNode(AccessibilityNodeInfo editableNode) {
        if (!this.editableNode.contains(editableNode)) {
            this.editableNode.add(editableNode);
        }
    }

    public List<AccessibilityNodeInfo> getScrollableNode() {
        return scrollableNode;
    }

    public void setScrollableNode(AccessibilityNodeInfo scrollableNode) {
        if (!this.scrollableNode.contains(scrollableNode)) {
            this.scrollableNode.add(scrollableNode);
        }
    }

    public List<AccessibilityNodeInfo> getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(AccessibilityNodeInfo selectedNode) {
        if (!this.selectedNode.contains(selectedNode)) {
            this.selectedNode.add(selectedNode);
        }
    }

    public List<AccessibilityNodeInfo> getPasswordNode() {
        return passwordNode;
    }

    public void setPasswordNode(AccessibilityNodeInfo passwordNode) {
        if (!this.passwordNode.contains(passwordNode)) {
            this.passwordNode.add(passwordNode);
        }
    }

    public List<AccessibilityNodeInfo> getLongClickableNode() {
        return longClickableNode;
    }

    public void setLongClickableNode(AccessibilityNodeInfo longClickableNode) {
        if (!this.longClickableNode.contains(longClickableNode)) {
            this.longClickableNode.add(longClickableNode);
        }
    }

    public List<AccessibilityNodeInfo> getFocusableNode() {
        return focusableNode;
    }

    public void setFocusableNode(AccessibilityNodeInfo focusableNode) {
        if (!this.focusableNode.contains(focusableNode)) {
            this.focusableNode.add(focusableNode);
        }
    }

    public List<AccessibilityNodeInfo> getFocusedNode() {
        return focusedNode;
    }

    public void setFocusedNode(AccessibilityNodeInfo focusedNode) {
        if (!this.focusedNode.contains(focusedNode)) {
            this.focusedNode.add(focusedNode);
        }
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(CharSequence pkgName) {
        if (!TextUtils.isEmpty(pkgName)) {
            this.mPackageName = pkgName.toString();
        }
    }

    public String getClassName() {
        return mClassName;
    }

    public void setClassName(CharSequence clz) {
        if (!TextUtils.isEmpty(clz)) {
            this.mClassName = clz.toString();
        }
    }

    public AccessibilityNodeInfo getInfo() {
        return info;
    }

    public void setInfo(AccessibilityNodeInfo info) {
        this.info = info;
        parserXpath(info);
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public Map<String, List<AccessibilityNodeInfo>> getTextAndNodes() {
        return textAndNodes;
    }

    public void setTextAndNode(String text, AccessibilityNodeInfo node) {
        List<AccessibilityNodeInfo> nodes = new ArrayList<AccessibilityNodeInfo>();
        try {
            if (textAndNodes.containsKey(text)) {
                nodes = textAndNodes.get(text);
            }
            nodes.add(node);
            textAndNodes.put(text, nodes);
        } catch (Throwable e) {
            L.e(Log.getStackTraceString(e));
        }
    }

    public List<String> getContentDesc() {
        return contentDesc;
    }

    public void setContentDesc(List<String> contentDesc) {
        this.contentDesc = contentDesc;
    }

    public Map<String, List<AccessibilityNodeInfo>> getContentDescAndNode() {
        return contentDescAndNodes;
    }

    public void setContentDescAndNode(String contentDesc, AccessibilityNodeInfo node) {
        // set contentDescAndNodes
        List<AccessibilityNodeInfo> nodes = new ArrayList<AccessibilityNodeInfo>();

        if (contentDescAndNodes.containsKey(contentDesc)) {
            nodes = contentDescAndNodes.get(contentDesc);
        }
        nodes.add(node);
        contentDescAndNodes.put(contentDesc, nodes);
    }

    public List<String> getClazz() {
        return clazz;
    }

    /**
     * 包括父类一起回放到该类集合中。兼容自定义组件
     *
     * @param clz
     */
    private void addClassToList(String clz) {
        if (TextUtils.isEmpty(clz)) {
            return;
        }
        try {
            //            Class<?> tempClass = Class.forName(clz);
            Class<?> tempClass = ClassLoader.getSystemClassLoader().loadClass(clz);
            while (!tempClass.equals(Object.class)) {
                if (!clazz.contains(tempClass.toString())) {
                    clazz.add(tempClass.toString());
                }
                tempClass = tempClass.getSuperclass();
            }
        } catch (java.lang.ClassNotFoundException e) {
            //            L.e("类加载出现问题。。。。" + clz);
        } catch (Throwable e) {
            L.e(Log.getStackTraceString(e));
        }
    }

    public Map<String, List<AccessibilityNodeInfo>> getClazzAndNode() {
        return clazzAndNodes;
    }

    public void setClazzAndNode(String clz, AccessibilityNodeInfo node) {
        if (TextUtils.isEmpty(clz)) {
            if (node.isClickable()) {
                nullClickableNode.add(node);
            }
        } else {
            List<AccessibilityNodeInfo> list = new ArrayList<AccessibilityNodeInfo>();
            if (clazzAndNodes.containsKey(clz)) {
                list = clazzAndNodes.get(clz);
            }

            list.add(node);
            clazzAndNodes.put(clz, list);
            addClassToList(clz);
        }
    }

    public List<AccessibilityNodeInfo> getClickableNode() {
        return clickableNode;
    }

    public void setClickableNode(AccessibilityNodeInfo node) {
        if (!clickableNode.contains(node)) {
            clickableNode.add(node);
        }
    }

    public List<AccessibilityNodeInfo> getNullClickableNode() {
        return nullClickableNode;
    }

    public void setNullClickableNode(AccessibilityNodeInfo node) {
        if (!nullClickableNode.contains(node)) {
            nullClickableNode.add(node);
        }
    }

    /**
     * 解析XPATH
     *
     * @param info
     */
    private void parserXpath(AccessibilityNodeInfo info) {
        //        L.d("inside parserXpath");
        boolean hasText = false;
        StringBuilder xpath = new StringBuilder();
        CharSequence clazz = info.getClassName();
        CharSequence text = info.getText();
        CharSequence contentDescription = info.getContentDescription();
        if (TextUtils.isEmpty(clazz)) {
            return;
        }

        if (!TextUtils.isEmpty(text)) {
            hasText = true;
            xpath
                    .append("[@text=\"")
                    .append(text.toString().replaceAll("\"", "\\\\\"").replaceAll("\'", "\\\\\'"))
                    .append("\"");
        }
        if (!TextUtils.isEmpty(contentDescription)) {
            hasText = true;
            if (TextUtils.isEmpty(text)) {
                xpath
                        .append("[@content-desc=\"")
                        .append(contentDescription.toString().replaceAll("'", "\\'"))
                        .append("\"");
            } else {
                xpath
                        .append(" and @content-desc=\"")
                        .append(
                                contentDescription.toString().replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\""))
                        .append("\"");
            }
        }
        if (hasText) {
            String pa = "//" + clazz + xpath.toString() + "]";
            if (SanboAbility.DEBUG_TAG) {
                L.v("xpath:" + pa);
            }
            List<AccessibilityNodeInfo> nodes = new ArrayList<AccessibilityNodeInfo>();
            if (xpathAndNodes.containsKey(pa)) {
                nodes = xpathAndNodes.get(pa);
            }
            nodes.add(info);
            xpathAndNodes.put(pa, nodes);

            if (SanboAbility.DEBUG_TAG) {
                L.v("xpath:" + xpathAndNodes.toString());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("包名:")
                .append(mPackageName)
                .append("; ")
                .append("类名:")
                .append(mClassName)
                .append("; ")
                .append(String.format("文字个数:[%d]", text.size()))
                .append("; ")
                .append(String.format("描述文字个数:[%d]", contentDesc.size()))
                .append("; ")
                .append(String.format("类型个数:[%d]", clazz.size()))
                .append("; ")
                .append(String.format("可点击个数:[%d]", clickableNode.size()))
                .append("; ")
                .append(String.format("没文字可点击元素个数:[%d]", nullClickableNode.size()))
                .append("; ");
        return sb.toString();
    }
}
