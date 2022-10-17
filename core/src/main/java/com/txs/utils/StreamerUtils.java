package com.txs.utils;

import com.txs.utils.log.L;

import java.io.Closeable;
import java.net.HttpURLConnection;
import java.nio.channels.FileLock;

public class StreamerUtils {

    public static void close(Object... os) {
        if (os != null && os.length > 0) {
            for (Object o : os) {
                if (o != null) {
                    try {
                        if (o instanceof HttpURLConnection) {
                            ((HttpURLConnection) o).disconnect();
                        } else if (o instanceof Closeable) {
                            ((Closeable) o).close();
                        } else if (o instanceof FileLock) {
                            ((FileLock) o).release();
                        }
                    } catch (Throwable e) {
                        L.e(e);
                    }
                }
            }
        }
    }
}
