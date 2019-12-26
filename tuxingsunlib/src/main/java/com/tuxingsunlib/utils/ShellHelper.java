package com.tuxingsunlib.utils;

import android.text.TextUtils;

import com.tuxingsunlib.utils.log.L;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import static java.lang.Runtime.getRuntime;

/**
 * @Copyright © 2016 sanbo Inc. All rights reserved. @Description: hh @Version: 1.0 @Create:
 * 2016年10月24日 下午10:12:39 @Author: sanbo
 */
public class ShellHelper {
  //    private static Process mRootProcess = null;

  private ShellHelper() {}

  public static ShellHelper getInstatnce() {
    //        if (mRootProcess == null) {
    //            getRootProcess();
    //        }

    return Holder.INSTANCE;
  }

  /**
   * 非root执行shell脚本
   *
   * @param shell
   * @return
   */
  public String shell(String shell) {
    if (TextUtils.isEmpty(shell)) {
      L.e("命令是空的,亲~");
      return null;
    }
    Process proc = null;
    BufferedInputStream in = null;
    BufferedReader br = null;
    StringBuilder sb = new StringBuilder();
    try {
      proc = getRuntime().exec(shell);
      in = new BufferedInputStream(proc.getInputStream());
      br = new BufferedReader(new InputStreamReader(in));
      String line = "";
      while ((line = br.readLine()) != null) {
        sb.append(line).append("\n");
      }
    } catch (Throwable e) {
      L.e(e);
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (in != null) {

        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return sb.toString();
  }

  //    private static void getProcess() {
  //        try {
  //            if (mRootProcess == null) {
  //                mRootProcess = getRuntime().exec("su");
  //            }
  //        } catch (Throwable e) {
  //        }
  //    }

  public void shell(String[] shell) {
    if (shell == null || shell.length <= 0) {
      L.e("命令是空的,亲~");
      return;
    }
    Process proc = null;
    OutputStream os = null;
    DataOutputStream out = null;
    try {

      proc = getRuntime().exec("sh\n");
      os = proc.getOutputStream();
      out = new DataOutputStream(os);
      for (int i = 0; i < shell.length; i++) {
        out.writeBytes(shell[i]);
        out.flush();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (os != null) {
        try {
          os.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      if (proc != null) {
        proc.destroy();
      }
    }
  }

  private static class Holder {
    private static final ShellHelper INSTANCE = new ShellHelper();
  }

  /**
   * root身份执行一句shell.要结果的那种.
   *
   * @param shell linux shell. 需要adb shell的那种
   * @return
   */
  //    public String sudo(String shell) {
  //        if (TextUtils.isEmpty(shell)) {
  //            L.e("命令是空的,亲~");
  //            return null;
  //        }
  //        if (mRootProcess == null)
  //            getRootProcess();
  //
  //        StringBuilder sb = new StringBuilder();
  //        if (mRootProcess != null) {
  //            BufferedInputStream in = null;
  //            DataOutputStream out = null;
  //            BufferedReader br = null;
  //            InputStreamReader reader = null;
  //            try {
  //                out = new DataOutputStream(mRootProcess.getOutputStream());
  //                out.writeBytes(shell);
  //                out.flush();
  //                out.writeBytes("exit\n");
  //                out.flush();
  //                in = new BufferedInputStream(mRootProcess.getInputStream());
  //                reader = new InputStreamReader(in);
  //                br = new BufferedReader(reader);
  //                String line;
  //                while ((line = br.readLine()) != null) {
  //                    sb.append(line);
  //                }
  //            } catch (Exception e) {
  //            } finally {
  //                if (reader != null) {
  //                    try {
  //                        reader.close();
  //                    } catch (IOException e) {
  //                    }
  //                }
  //                if (br != null) {
  //                    try {
  //                        br.close();
  //                    } catch (IOException e) {
  //                    }
  //                }
  //                if (in != null) {
  //
  //                    try {
  //                        in.close();
  //                    } catch (IOException e) {
  //                    }
  //                }
  //                if (out != null) {
  //                    try {
  //                        out.close();
  //                    } catch (IOException e) {
  //                    }
  //                }
  //            }
  //
  //        }
  //
  //        return sb.toString();
  //    }

  /**
   * 批量执行shell
   *
   * @param shells
   * @return
   */
  //    public void sudo(String[] shells) {
  //        if (shells == null || shells.length < 1) {
  //            L.e("命令是空的,亲~");
  //            return;
  //        }
  //
  //        BufferedInputStream in = null;
  //        DataOutputStream out = null;
  //        BufferedReader br = null;
  //        InputStreamReader reader = null;
  //        try {
  //            mRootProcess = getRuntime().exec("su\n");
  //            out = new DataOutputStream(mRootProcess.getOutputStream());
  //            for (int i = 0; i < shells.length; i++) {
  //                out.writeBytes(shells[i]);
  //                out.flush();
  //            }
  //            // in = new BufferedInputStream(mRootProcess.getInputStream());
  //            // reader = new InputStreamReader(in);
  //            // br = new BufferedReader(reader);
  //        } catch (Exception e) {
  //        } finally {
  //            if (reader != null) {
  //                try {
  //                    reader.close();
  //                } catch (IOException e) {
  //                }
  //            }
  //            if (br != null) {
  //                try {
  //                    br.close();
  //                } catch (IOException e) {
  //                    e.printStackTrace();
  //                }
  //            }
  //            if (in != null) {
  //
  //                try {
  //                    in.close();
  //                } catch (IOException e) {
  //                    e.printStackTrace();
  //                }
  //            }
  //            if (out != null) {
  //                try {
  //                    out.close();
  //                } catch (IOException e) {
  //                    e.printStackTrace();
  //                }
  //            }
  //        }
  //
  //    }
}
