#!/bin/bash
adb shell pm grant com.tuxingsun.demo android.permission.WRITE_SECURE_SETTINGS
adb shell settings put secure enabled_accessibility_services  com.tuxingsun.demo/com.txs.CoreAbility
adb shell settings put secure accessibility_enabled 1