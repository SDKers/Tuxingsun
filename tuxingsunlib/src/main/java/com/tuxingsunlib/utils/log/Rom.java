package com.tuxingsunlib.utils.log;

import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Rom {
    
    public static final String ROM_MIUI = "miui";
    public static final String ROM_EMUI = "emui";
    public static final String ROM_FLYME = "flyme";
    public static final String ROM_OPPO = "oppo";
    public static final String ROM_SMARTISAN = "smartisan";
    public static final String ROM_VIVO = "vivo";
    public static final String ROM_QIKU = "qiku";
    
    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";
    
    private static String sName;
    private static String sVersion;
    
    public static boolean isEmui() {
        return check(ROM_EMUI);
    }
    
    public static boolean isMiui() {
        return check(ROM_MIUI);
    }
    
    public static boolean isVivo() {
        
        return Build.BRAND.toLowerCase().contains(ROM_VIVO);
    }
    
    public static boolean isOppo() {
        return Build.BRAND.toLowerCase().contains(ROM_OPPO);
    }
    
    public static boolean isFlyme() {
        return check(ROM_FLYME);
    }
    
    public static boolean is360() {
        return check(ROM_QIKU) || check("360");
    }
    
    public static boolean isSmartisan() {
        return check(ROM_SMARTISAN);
    }
    
    public static String getName() {
        if (sName == null) {
            check("");
        }
        return sName;
    }
    
    public static String getVersion() {
        if (sVersion == null) {
            check("");
        }
        return sVersion;
    }
    
    public static boolean check(String rom) {
        if (sName != null) {
            return sName.equalsIgnoreCase(rom);
        }
        
        if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_MIUI))) {
            sName = ROM_MIUI;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_EMUI))) {
            sName = ROM_EMUI;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_OPPO))) {
            sName = ROM_OPPO;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_VIVO))) {
            sName = ROM_VIVO;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_SMARTISAN))) {
            sName = ROM_SMARTISAN;
        } else {
            sVersion = Build.DISPLAY;
            if (sVersion.toUpperCase().contains(ROM_FLYME)) {
                sName = ROM_FLYME;
            } else {
                sVersion = Build.UNKNOWN;
                sName = Build.MANUFACTURER.toUpperCase();
            }
        }
        return sName.equals(rom);
    }
    
    public static String getProp(String name) {
        Process p = null;
        String line = null;
        BufferedReader input = null;
        InputStreamReader reader = null;
        try {
            p = Runtime.getRuntime().exec("getprop " + name);
            reader = new InputStreamReader(p.getInputStream());
            input = new BufferedReader(reader, 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            if (p != null) {
                p.destroy();
            }
        }
        return line;
    }
}
