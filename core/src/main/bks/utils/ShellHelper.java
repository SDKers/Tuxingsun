package com.txs.utils;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @Copyright © 2016 sanbo Inc. All rights reserved.
 * @Description: hh
 * @Version: 1.0
 * @Create: 2016年10月24日 下午10:12:39
 * @Author: sanbo
 */
public class ShellHelper {
    private ShellHelper() {
    }

    public static ShellHelper getInstatnce() {

        return Holder.INSTANCE;
    }

    public String shell(String cmd) {
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

            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.flush();
            //exitValue
            os.writeBytes("exit\n");
            os.flush();
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
                result = result.trim();
            }
        } catch (Throwable e) {
        } finally {
            safeClose(pos);
            safeClose(ii);
            safeClose(br);
            safeClose(is);
            safeClose(in);
            safeClose(os);
        }

        return result;
    }

    private void safeClose(Closeable able) {
        if (able != null) {
            try {
                able.close();
            } catch (Throwable e) {
            }
        }
    }


    private static class Holder {
        private static final ShellHelper INSTANCE = new ShellHelper();
    }

}
