package com.txs;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.txs.utils.L;

import java.util.List;



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


//        setFlag(serviceInfo);
        setAllFlag(serviceInfo);
        setServiceInfo(serviceInfo);
        super.onServiceConnected();
    }

    private void setFlag(AccessibilityServiceInfo serviceInfo) {
        if (Build.VERSION.SDK_INT >= 16) {
            serviceInfo.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            serviceInfo.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
                    | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
        }
        if (Build.VERSION.SDK_INT >= 28) {
            serviceInfo.flags |= AccessibilityServiceInfo.FLAG_REQUEST_ACCESSIBILITY_BUTTON;
        }
    }



    @TargetApi(16)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        mThis = this;
        if (event == null) {
            return;
        }

        L.d(event.toString());
        // 监控所有的事件
//        processAllEvent(event);

        // 解析所有的元素并缓存内存中


        // 监控页面变化及页面内容变化
        onCall(event);

    }

    /**
     * 处理页面变动事件
     * @param event
     */
    private void onCall(AccessibilityEvent event) {
        int eventType = event.getEventType();

        // 安卓事件类型进行处理。内容/窗体变化
        if ((Build.VERSION.SDK_INT >= 4 && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
                        || (Build.VERSION.SDK_INT >= 14 && (eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED || eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED))
                        || (Build.VERSION.SDK_INT >= 16 && eventType == AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY)
                        || (Build.VERSION.SDK_INT >= 21 && eventType == AccessibilityEvent.TYPE_WINDOWS_CHANGED)
        ) {

            CharSequence pkg = event.getPackageName();
            CharSequence className = event.getClassName();
            List<CharSequence> texts = event.getText();
            CharSequence contentDescription = event.getContentDescription();
            L.i("------->" + pkg + "/" + className + "-----" + contentDescription + "----" + texts);

            // 将页面中所有的元素缓存至内存中
            if (Build.VERSION.SDK_INT<16){
                L.e("版本16以下,需要其他方案兼容处理... ...");
                return;
            }

            parserNodesIntoMemory(getRootInActiveWindow());
            // 根据外部传入的规则进行操作
        }


//        //Added in API level 16
//        //如果此服务可以检索窗口内容，则获取当前活动窗口中的根节点。 如果用户没有触摸任何窗口，则活动窗口是用户当前正在触摸的窗口或具有输入焦点的窗口。 它可以来自任何逻辑显示。
//        getRootInActiveWindow();

    }

    private void parserNodesIntoMemory(AccessibilityNodeInfo rootInActiveWindow) {
    }



    @Override
    public void onInterrupt() {
        L.e("onInterrupt");
    }

    /*****************************************************************************/
    /*****************************实现外部获取本地的实例******************************/
    /******************************************************************************/
    private static AccessibilityService mThis = null;

    public static AccessibilityService getThis() {
        return mThis;
    }

    public CoreAbility() {
        mThis = this;
    }

    /******************************************************************************/

    private void processAllEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        // Added in API level 4
        if (Build.VERSION.SDK_INT >= 4) {
            if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
                // Added in API level 4
                L.i("单击视图事件");
            } else if (eventType == AccessibilityEvent.TYPE_VIEW_LONG_CLICKED) {
                // Added in API level 4
                L.i("长按视图事件");
            } else if (eventType == AccessibilityEvent.TYPE_VIEW_SELECTED) {
                // Added in API level 4
                L.i("通常在 AdapterView 的上下文中选择项目的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
                // Added in API level 4
                L.i("设置 View 的输入焦点的事件");
            } else if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
                // Added in API level 4
                L.i("更改 EditText 的文本的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                // Added in API level 4
                L.i("用户界面视觉上不同的部分发生更改的事件。 这些事件只能从具有辅助功能窗格标题的视图中调度，并替换这些源的 TYPE_WINDOW_CONTENT_CHANGED");
            } else if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
                // Added in API level 4
                L.i("显示通知的事件。");
            }
        }
        // make sure append more event type
        if (Build.VERSION.SDK_INT >= 14) {
            if (eventType == AccessibilityEvent.TYPE_VIEW_HOVER_ENTER) {
                // Added in API level 14
                L.i("悬停进入视图的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_VIEW_HOVER_EXIT) {
                // Added in API level 14
                L.i("在视图上悬停退出的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START) {
                // Added in API level 14
                L.i("开始触摸探索手势的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END) {
                // Added in API level 14
                L.i("结束触摸探索手势的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                // Added in API level 14
                L.i("更改窗口内容的事件，更具体地说是更改以事件源为根的子树。");
            } else if (eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
                // Added in API level 14
                L.i("滚动视图的事件。 这种事件类型一般不直接发送。");
            } else if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
                // Added in API level 14
                L.i("更改 EditText 中的选择的事件。");
            }
        }

        // make sure append more event type
        if (Build.VERSION.SDK_INT >= 16) {
            if (eventType == AccessibilityEvent.TYPE_ANNOUNCEMENT) {
                // Added in API level 16
                L.i("应用程序发布公告的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
                // Added in API level 16
                L.i("获得可访问性焦点的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED) {
                // Added in API level 16
                L.i("清除可访问性焦点的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY) {
                // Added in API level 16
                L.i("以给定的移动粒度遍历视图文本的事件。");
            }
        }
        // make sure append more event type
        if (Build.VERSION.SDK_INT >= 17) {
            if (eventType == AccessibilityEvent.TYPE_GESTURE_DETECTION_START) {
                // Added in API level 17
                L.i("开始手势检测的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_GESTURE_DETECTION_END) {
                // Added in API level 17
                L.i("结束手势检测的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_TOUCH_INTERACTION_START) {
                // Added in API level 17
                L.i("用户开始触摸屏幕的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_TOUCH_INTERACTION_END) {
                // Added in API level 17
                L.i("用户结束触摸屏幕的事件。");
            }
        }

        // make sure append more event type
        if (Build.VERSION.SDK_INT >= 21) {
            if (eventType == AccessibilityEvent.TYPE_WINDOWS_CHANGED) {
                // Added in API level 21
                L.i("屏幕上显示的系统窗口中的事件更改。 此事件类型应仅由系统分派。");
            }
        }
        // make sure append more event type
        if (Build.VERSION.SDK_INT >= 23) {
            if (eventType == AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED) {
                // Added in API level 23
                L.i("上下文单击视图的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT) {
                // Added in API level 23
                L.i("助手当前读取用户屏幕上下文的事件。");
            } else if (eventType == AccessibilityEvent.TYPE_SPEECH_STATE_CHANGE) {
                // Added in API level 23
                L.i("由语音状态更改类型定义的语音状态的更改。当应用程序想要发出信号表明它正在说话或收听人类语音时，就会发生语音状态的变化。 " + "此事件有助于避免两个应用程序想要说话或一个应用程序在另一个应用程序说话时听的冲突。 发送此事件时，发送者应确保伴随的状态更改类型有意义。 " + "例如，发件人不应同时发送 SPEECH_STATE_SPEAKING_START 和 SPEECH_STATE_SPEAKING_END。");
            }
        }
    }

    /**
     * 各个版本的标签设置
     * @param serviceInfo
     */
    private void setAllFlag(AccessibilityServiceInfo serviceInfo) {
        // // 应用列表范围
        // serviceInfo.packageNames = new String[]{...};
        if (Build.VERSION.SDK_INT >= 16) {
            // Added in API level 16
            serviceInfo.flags
                    //如果设置了此标志，系统将考虑除了对可访问性重要的视图之外，对可访问性不重要的视图。也就是说，通过 View#IMPORTANT_FOR_ACCESSIBILITY_NO
                    // 或 View#IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS 标记为对可访问性不重要的视图和通过 View#IMPORTANT_FOR_ACCESSIBILITY_AUTO 标记为对可访问性可能重要的视图，
                    // 系统已确定对可访问性不重要，同时报告查询窗口内容和无障碍服务将从它们那里接收无障碍事件。
                    //
                    //注意：对于面向 Android 4.1（API 级别 16）或更高版本的无障碍服务，必须显式设置此标志，以便系统考虑对无障碍不重要的视图。对于面向 Android 4.0.4（API 级别 15）或更低版本的无障碍服务，
                    // 此标志将被忽略，并且所有视图都被视为可访问性目的。
                    //
                    //通常对可访问性不重要的视图是布局管理器，它们不响应用户操作，不绘制任何内容，并且在屏幕内容的上下文中没有任何特殊语义。例如，一个三乘三网格可以实现为三个水平线性布局和一个垂直，
                    // 或三个垂直线性布局和一个水平，或一个网格布局等。在这种情况下，实际的布局管理器用于实现网格配置不重要；重要的是有九个均匀分布的元素。
                    |= AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
            // Added in API level 16  会导致无法点击
            //该标志请求系统进入触摸探索模式。在这种模式下，在屏幕上移动的单个手指就像鼠标指针悬停在用户界面上一样。系统还将检测在触摸屏上执行的某些手势并通知此服务。
            // 如果至少有一个无障碍服务设置了此标志，系统将启用触摸探索模式。因此，清除此标志并不能保证设备不会处于触摸探索模式，因为可能有另一个启用的服务请求它。
            //
            //对于面向 Android 4.3（API 级别 18）或更高版本的无障碍服务，想要设置此标志，必须通过将属性 canRequestTouchExplorationMode 设置为 true，
            // 在其元数据中声明此功能。否则，该标志将被忽略。
            // // | AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE
            ;
        }

        // Added in API level 18
        if (Build.VERSION.SDK_INT >= 18) {
            serviceInfo.flags
                    // 会过滤掉home/back等虚拟键
                    //该标志从系统请求过滤关键事件。 如果设置了这个标志，无障碍服务将在应用程序允许它实现全局快捷方式之前接收关键事件。
                    // 想要设置此标志的服务必须通过将属性 #canRequestFilterKeyEvents canRequestFilterKeyEvents 设置为 true 在其元数据中声明此功能，否则此标志将被忽略。
                    |= AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS
                    //此标志请求 AccessibilityService 获得的 AccessibilityNodeInfos 包含源视图的 id。 源视图 id 将是“package:id/name”形式的完全限定资源名称，
                    // 例如“foo.bar:id/my_list”，它对于 UI 测试自动化很有用。 默认情况下未设置此标志。
                    | AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
                    // Deprecated in API level 26
                    | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;

        }
        // Added in API level 21
        if (Build.VERSION.SDK_INT >= 21) {
            serviceInfo.flags
                    //该标志向系统指示无障碍服务想要访问所有交互式窗口的内容。 交互式窗口是具有输入焦点或在未启用通过触摸探索时可以被有视力的用户触摸的窗口。
                    // 如果未设置此标志，您的服务将不会接收 AccessibilityEvent.TYPE_WINDOWS_CHANGED 事件，
                    // 调用 AccessibilityServiceAccessibilityService.getWindows() 将返回一个空列表，
                    // 而 AccessibilityNodeInfo.getWindow() 将返回 null。
                    //
                    //想要设置此标志的服务必须通过将属性 canRetrieveWindowContent 设置为 true 来声明在其元数据中检索窗口内容的能力，否则此标志将被忽略。
                    |= AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        }
        // Added in API level 26
        if (Build.VERSION.SDK_INT >= 26) {
            serviceInfo.flags
                    //该标志向系统指示无障碍服务请求在系统的导航区域内显示无障碍按钮（如果可用）。
                    //
                    //注意：对于面向 API 大于 API 29 的无障碍服务，必须在无障碍服务元数据文件中指定此标志。 否则，它将被忽略。
                    |= AccessibilityServiceInfo.FLAG_REQUEST_ACCESSIBILITY_BUTTON
                    //此标志请求系统范围内所有带有 AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY 的音轨都由 AudioManager.STREAM_ACCESSIBILITY 音量控制。
                    | AccessibilityServiceInfo.FLAG_ENABLE_ACCESSIBILITY_VOLUME;
        }
        // Added in API level 29
        if (Build.VERSION.SDK_INT >= 29) {
            serviceInfo.flags
                    //此标志要求可访问性快捷方式警告对话框在显示对话框时具有语音反馈。
                    |= AccessibilityServiceInfo.FLAG_REQUEST_SHORTCUT_WARNING_DIALOG_SPOKEN_FEEDBACK;
        }
        // Added in API level 33
        if (Build.VERSION.SDK_INT >= 33) {
            serviceInfo.flags
                    //此标志使 AccessibilityService 成为具有输入法编辑器功能子集的输入法编辑器：获取 InputConnection 并获取文本选择更改通知。
                    |= AccessibilityServiceInfo.FLAG_INPUT_METHOD_EDITOR;
        }
    }

}
