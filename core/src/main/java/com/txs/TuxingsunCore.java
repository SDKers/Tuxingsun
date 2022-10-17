package com.txs;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.txs.impl.AccessibilityHelper;
import com.txs.memorydata.MemContent;


public class TuxingsunCore {


    public static void init(Context context, Class<? extends AccessibilityService> clazz) {

        ApplicationInfo ai = CoreAbility.getThis().getApplicationInfo();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            AccessibilityServiceInfo i = CoreAbility.getThis().getServiceInfo();
        }
    }

    /**
     * 判断是否开启辅助功能，开启的是内置的辅助功能
     * @param context
     * @return
     */
    public static boolean isPrepareWork(Context context) {
        return isPrepareWork(context, CoreAbility.class);
    }

    /**
     *  判断是否开启辅助功能
     * @param context
     * @param clazz
     * @return
     */
    public static boolean isPrepareWork(Context context, Class<? extends AccessibilityService> clazz) {
        if (clazz == null) {
            return false;
        }
        return Inner.isPrepareWork(context, clazz);
    }

    /**
     * 判断是否开启辅助功能，开启的是内置的辅助功能
     * @param context
     * @return
     */
    public static void prepare(Context context) {
        prepare(context, CoreAbility.class);
    }

    /**
     *  判断是否开启辅助功能
     * @param context
     * @param clazz
     * @return
     */
    public static void prepare(Context context, Class<? extends AccessibilityService> clazz) {
        if (clazz == null) {
            return;
        }
        AccessibilityHelper.openAccessibilityService(context);
    }

    private static class Inner {

        public static boolean isPrepareWork(Context context, Class<? extends AccessibilityService> clazz) {
            if (context != null) {
                MemContent.mContext = context.getApplicationContext();
            }
            return AccessibilityHelper.isAccessibilitySettingsOn(context, clazz);
        }
    }
}
