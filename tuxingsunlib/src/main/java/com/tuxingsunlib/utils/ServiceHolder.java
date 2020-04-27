package com.tuxingsunlib.utils;

import android.accessibilityservice.AccessibilityService;

/**
 * @Copyright © 2019 Analysys Inc. All rights reserved.
 * @Description 服务帮助类
 * @Version 1.0
 * @Create 2019/1/15 15:55
 * @Author sanbo
 */
public class ServiceHolder {

  private AccessibilityService mService = null;

  private ServiceHolder() {}

  public static ServiceHolder getInstance() {
    return Holder.instance;
  }

  public AccessibilityService getService() {
    return mService;
  }

  public void setService(AccessibilityService service) {
    this.mService = service;
  }

  private static class Holder {
    private static ServiceHolder instance = new ServiceHolder();
  }
}
