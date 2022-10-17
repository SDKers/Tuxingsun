# 备份方法

``` java
    /**
     * 通过文字点击对应组件
     *
     * @param text
     */
    @TargetApi(16)
    public void clickTextViewByText(String text) {
        if (isDebug) {
            L.v("clickTextViewByText" + text);
        }
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                performViewClick(nodeInfo);
                return;
            }
        }
    }

    /**
     * oppo暂时不能兼容。安装、取消底部是一个图标。需要点击位置
     *
     * @param info
     */
    private static void checkText(AccessibilityNodeInfo info) {
        try {
            try {
                boolean isClickable = info.isClickable();
                boolean isEnabled = info.isEnabled();
                boolean isSelected = info.isSelected();
                boolean isFocusable = info.isFocusable();
                if (isDebug) {
                    L.v("[" + info.getClassName() + "]" + isClickable + "--" + isEnabled + "--" + isSelected + "--" + "--" + isFocusable + "--" + info.toString());
                    if (info.getClassName().equals("android.widget.ImageView")) {
                        MyAccessibilityService.performViewClick(info);
                    }
                }
            } catch (Throwable e) {
            }

            try {
                int count = info.getChildCount();
                if (count > 0) {
                    for (int i = 0; i < count; i++) {
                        AccessibilityNodeInfo child = info.getChild(i);
                        if (child != null) {
                            checkText(child);
                        }
                    }
                }
            } catch (Throwable e) {
            }

        } catch (Throwable e) {
            L.e(Log.getStackTraceString(e));
        }
    }

    @TargetApi(16)
    public AccessibilityNodeInfo findViewContainsText(String text) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        int i = 0;
        int count = accessibilityNodeInfo.getChildCount();
        while (i < count) {
            AccessibilityNodeInfo node = accessibilityNodeInfo.getChild(i++);
            if (node != null && node.getText() != null &&
                    node.getText().toString().contains(text)) {
                return node;
            }
        }
        return null;
    }

    private static void parserNull(AccessibilityNodeInfo info) {
        if (AccessibilityImpl.INSTALL_PKGS.contains(info.getPackageName())) {
            if (info.isClickable()) {
                L.i("安装文件中 .点击...因为空组件，且可以点击..[" + info.getClassName() + "]");
                performViewClick(info);
            }
        }
        //如果没文字的可点击的 ，直接点击了
        if (info.isClickable()
                &&
                !info.getClassName().equals("android.view.ViewGroup")
                && !info.getClassName().equals("android.widget.FrameLayout")
                && !info.getClassName().equals("android.widget.LinearLayout")
                && !info.getClassName().equals("android.widget.ListView")
//                       && !info.getClassName().equals("android.widget.ImageView")
                ) {
            L.i("点击...因为空组件，且可以点击..[" + info.getClassName() + "]");
            performViewClick(info);
        }
    }

    /**
     * 模拟返回操作
     */
    public static void performBackClick() {
        if (isDebug) {
            L.v("performBackClick");
        }
        if (Build.VERSION.SDK_INT > 15) {
            new MyAccessibilityService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }

    private static boolean hasDialog(AccessibilityNodeInfo node) {
        try {
            CharSequence cs = node.getPackageName();
            if (cs != null) {
                if (cs.toString().trim().equals("android.app.Dialog")) {
                    return true;
                }
            }
        } catch (Throwable e) {
            if (isDebug) {
                L.v(e);
            }
        }
        try {

            //子父节点循环遍历
            int count = node.getChildCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    AccessibilityNodeInfo child = node.getChild(i);
                    if (child != null) {
                        hasDialog(child);
                    }
                }
            }
        } catch (Throwable e) {
            if (isDebug) {
                L.v(e);
            }
        }
        return false;
    }

    /**
     * android 7开始增强可以点击坐标
     *
     * @param x
     * @param y
     */
    @TargetApi(24)
    private static void clickPostion(int x, int y) {
        Point position = new Point(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path p = new Path();
        p.moveTo(position.x, position.y);
        builder.addStroke(new GestureDescription.StrokeDescription(p, 0L, 100L));
        GestureDescription gesture = builder.build();
        boolean isDispatched = getInstance().dispatchGesture(gesture, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                L.d("onCompleted: 完成..........");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                L.d("onCompleted: 取消..........");
            }
        }, null);
    }
    
    
    
    
    
```

## root使用打开辅助功能

``` java
private static final String cmd = "enabled=$(settings get secure enabled_accessibility_services)\n" +
        "pkg=%s\n" +
        "if [[ $enabled == *$pkg* ]]\n" +
        "then\n" +
        "echo already_enabled\n" +
        "else\n" +
        "enabled=$pkg:$enabled\n" +
        "settings put secure enabled_accessibility_services $enabled\n" +
        "fi";

public static boolean enableAccessibilityServiceByRoot(Context context, Class<? extends AccessibilityService> accessibilityService) {
    String serviceName = context.getPackageName() + "/" + accessibilityService.getName();
    try {
        return TextUtils.isEmpty(ProcessShell.execCommand(String.format(Locale.getDefault(), cmd, serviceName), true).error);
    } catch (Exception ignored) {
        return false;
    }
}
```

``` jshelllanguage
enabled=$(settings get secure enabled_accessibility_services)
pkg="com.helper/com.auto.assess.MyAccessibilityService"
if [[ $enabled == *$pkg* ]]
then
echo already_enabled
else
enabled=$pkg:$enabled
settings put secure enabled_accessibility_services $enabled
fi
```

    //    @Override
    //    protected void onServiceConnected() {
    ////        L.i("MyAccessibilityService onServiceConnected will start config!");
    ////        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
    ////        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
    ////        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
    ////        accessibilityServiceInfo.notificationTimeout = 1;
    ////        setServiceInfo(accessibilityServiceInfo);
    ////        ServiceHolder.getInstance().setService(this);
    //        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
    //        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
    //        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
    //        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS |
    //                AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS |
    //                AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
    //        info.notificationTimeout = 0;
    //        setServiceInfo(info);
    //    }
    //
    //    private void enterTouchExplorationMode() {
    //        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
    //        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
    //        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
    //        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS |
    //                AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS |
    //                AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS |
    //                AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
    //        info.notificationTimeout = 0;
    //        setServiceInfo(info);
    //    }

