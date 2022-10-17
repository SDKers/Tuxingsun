package com.txs;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;



/**
 * @Copyright © 2022 sanbo Inc. All rights reserved.
 * @Description: 土行孙实现类
 * @Version: 1.0
 * @Create: 2022-10-17 15:48:35
 * @author: sanbo
 */
public class CoreAbility extends AccessibilityService {

    @Override
    protected void onServiceConnected() {


        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();

        serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        serviceInfo.notificationTimeout = 0;
//        serviceInfo.packageNames = new String[]{...};

        serviceInfo.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
                | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY
                | AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
//                | AccessibilityServiceInfo.FLAG_INPUT_METHOD_EDITOR
//                | AccessibilityServiceInfo.FLAG_REQUEST_SHORTCUT_WARNING_DIALOG_SPOKEN_FEEDBACK
//                | AccessibilityServiceInfo.FLAG_ENABLE_ACCESSIBILITY_VOLUME
//                | AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
//                | AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS
//                | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY
//                  // 会导致无法点击
//                | AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE
        ;
        if (Build.VERSION.SDK_INT > 28) {
            serviceInfo.flags |= AccessibilityServiceInfo.FLAG_REQUEST_ACCESSIBILITY_BUTTON;
        }
        setServiceInfo(serviceInfo);
        super.onServiceConnected();
    }

    @TargetApi(16)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        mThis = this;
    }

    @Override
    public void onInterrupt() {
    }

    /*****************************************************************************/
    /*****************************实现外部获取本地的实例******************************/
    /******************************************************************************/
    private static AccessibilityService mThis = null;

    public static AccessibilityService getThis() {
        return mThis;
    }

    private CoreAbility() {
        mThis = this;
    }
}
