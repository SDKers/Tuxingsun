package com.txs.utils;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Copyright © 2016 sanbo Inc. All rights reserved.
 * @Description: hh
 * @Version: 1.0
 * @Create: 2016年10月24日 下午10:12:39
 * @Author: sanbo
 */
public class ShellHelper {
    /**
     * 执行拼接shell
     *
     * @param exec new String[]{"ls", "-l", "xx"}
     * @return
     */
    public static String exec(String[] exec) {
        StringBuffer sb = new StringBuffer();
        for (String s : exec) {
            sb.append(s).append(" ");
        }
        return shell(sb.toString());
    }

    /**
     * 执行shell指令
     *
     * @param cmd
     * @return
     */
    public static String shell(String cmd) {
        if (TextUtils.isEmpty(cmd)) {
            return "";
        }
        return more(true, cmd);
    }

    public static void more(String... cmds) {
        more(Arrays.asList(cmds));
    }

    private static String more(boolean isNeedResult, String... cmds) {
        if (isNeedResult) {
            return more(Arrays.asList(cmds), isNeedResult);
        } else {
            more(Arrays.asList(cmds), isNeedResult);
        }
        return null;
    }

    private static void more(List<String> cmds) {
        more(cmds, false);
    }

    private static String more(List<String> cmds, boolean isNeedResult) {
        String result = "";
        Process proc = null;
        BufferedInputStream in = null;
        BufferedReader br = null;
        InputStreamReader is = null;
        InputStream ii = null;
        StringBuilder sb = new StringBuilder();
        DataOutputStream os = null;
        OutputStream pos = null;
        try {
            proc = Runtime.getRuntime().exec("sh");
            pos = proc.getOutputStream();
            os = new DataOutputStream(pos);

            for (String cmd : cmds) {
                L.i("cmd: " +cmd);
                // donnot use os.writeBytes(commmand), avoid chinese charset error
                os.write(cmd.getBytes());
                os.writeBytes("\n");
                os.flush();
            }
            // exitValue
            os.writeBytes("exit\n");
            os.flush();
            if (isNeedResult) {
                ii = proc.getInputStream();
                in = new BufferedInputStream(ii);
                is = new InputStreamReader(in);
                br = new BufferedReader(is);
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                if (sb.length() > 0) {
                    return sb.substring(0, sb.length() - 1);
                }
                result = String.valueOf(sb);
                if (!TextUtils.isEmpty(result)) {
                    return result.trim();
                }
            }
        } catch (Throwable e) {
            L.e(e);
        } finally {
            StreamerUtils.close(pos, ii, br, is, in, os);
        }
        return null;
    }


    public static List<String> getResultArrays(String cmd) {
        return getArrays(cmd, true);
    }

    /**
     * 核心工作方法
     *
     * @param cmd          执行指令
     * @param isNeedResult 需要返回值
     * @return
     */
    public static List<String> getArrays(String cmd, boolean isNeedResult) {

        if (TextUtils.isEmpty(cmd)) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        Process proc = null;
        BufferedInputStream in = null;
        BufferedReader br = null;
        InputStreamReader is = null;
        InputStream ii = null;
        StringBuilder sb = new StringBuilder();
        DataOutputStream os = null;
        OutputStream pos = null;
        try {
            proc = Runtime.getRuntime().exec("sh");
            pos = proc.getOutputStream();
            os = new DataOutputStream(pos);
            // don't use os.writeBytes(commmand), avoid chinese charset error
            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            // 返回
            ii = proc.getInputStream();
            in = new BufferedInputStream(ii);
            is = new InputStreamReader(in);
            br = new BufferedReader(is);
            String line = "";
            while ((line = br.readLine()) != null) {
                if (!TextUtils.isEmpty(line)) {
                    line = line.trim();
                    if (isNeedResult && !result.contains(line)) {
                        result.add(line);
                    }
                }
            }

        } catch (Throwable e) {
            L.e(e);
        } finally {
            StreamerUtils.close(pos, ii, br, is, in, os);
        }
        return result;
    }

}
