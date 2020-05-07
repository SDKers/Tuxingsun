package com.tuxingsunlib.utils.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Copyright © 2020 sanbo Inc. All rights reserved.
 * @Description: TODO
 * @Version: 1.0
 * @Create: 2020/4/27 17:46
 * @author: sanbo
 */
public class PubText {
    
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
    // 安装页面
    public static ArrayList<String> INSTALL_PAGES =
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
    public static ArrayList<String> REMOVE_PKGS = new ArrayList<String>();
    
    // 密码框
    public static ArrayList<String> PWD_DIALOG_PKG =
            new ArrayList<String>(
                    Arrays.asList(
                            "com.bbk.account" // vivo  Z1、vivo Y69A、vivo X21A
                            , "com.coloros.safecenter" // vivo R9s、oppo  A59S
                            , "com.oppo.usercenter" // vivo R9s、oppo  A59S
                            , "com.vivo.secime.service" // vivo的手机--暂时不记得机型
                    ));
    
    // 安装关键字.(识别是警告框),暂时针对特殊的手机机型vivo Y23L (4.4.4)
    public static ArrayList<String> INSTALL_STEP_TEXT =
            new ArrayList<String>(
                    Arrays.asList(
                            "好" // 好
                    ));
    public static ArrayList<String> DIALOG_TEXT =
            new ArrayList<String>(Arrays.asList("“电脑端未...”", "需要您验证身份后安装。", "取消", "安装"));
    /**
     * 安装中、导航中的文字
     */
    public static List<String> installAndNavigationTexts =
            new ArrayList<>(
                    Arrays.asList(
                            // 安装过程
                            "安装",
                            "继续",
                            "继续安装",
                            "重新安装"
                            // 锤子
                            ,
                            "同意并安装"
                            // vivo
                            ,
                            "同意并继续",
                            "继续安装旧版本",
                            "无视风险安装",
                            "好",
                            "允许",
                            "始终允许",
                            "仅允许一次",
                            "下一步",
                            "下一步>",
                            "替换",
                            "接受",
                            "设置"
                            // 会导致无线打开和关闭状态切换器
                            ,
                            "开启",
                            "关闭",
                            "同意"
                            // 更新相关的
                            ,
                            "稍后再说",
                            "以后再说",
                            "忽略本次",
                            "狠心放弃"
                            // 悬浮窗 引导页面
                            ,
                            "跳过  5",
                            "跳过  4",
                            "跳过  3",
                            "跳过  2",
                            "跳过  1",
                            "跳过  0",
                            "跳过 5",
                            "跳过 4",
                            "跳过 5",
                            "跳过 2",
                            "跳过 1",
                            "3S 关闭",
                            "2S 关闭",
                            "1S 关闭",
                            "4s 跳过",
                            "3s 跳过",
                            "2s 跳过",
                            "1s 跳过",
                            "开始体验",
                            "立即体验",
                            "开始使用",
                            "我同意",
                            "进入应用",
                            "知道了",
                            "稍后下载",
                            "立即开始运行",
                            "现在开启",
                            "立即开启",
                            "马上开启",
                            "是",
                            "去授权"
                            // 樊登读书
                            ,
                            "看看再说",
                            "全选进入书城逛逛",
                            "看看再说 >>",
                            "全选进入书城逛逛>>>",
                            "选好了",
                            "跳过广告",
                            "开启阅读好时光",
                            "开启阅读好时光 >>",
                            "随便看看",
                            "随便看看 >",
                            "随便看看 >>",
                            "随便看看 >>>",
                            "以后再提醒我",
                            "立即进入",
                            "直接试用",
                            "先去逛逛",
                            "点击跳过",
                            "我知道了"
                            // 银行系列【建设银行】: 支付环境风险.两个点击:  [仍然支付][解决风险]
                            ,
                            "仍然支付"
                            // 360异常警告
                            ,
                            "暂不处理"
                            
                            // 需要账号. 刺猬猫阅读
                            //  , "直接登录"
                            // 需要账号,瘦瘦-减肥
                            //  , "立即加入"
                    ));
    /**
     * 标准的结束的文字
     */
    public static List<String> completeTexts = new ArrayList<>(Arrays.asList("完成", "确认", "确定"));
}
