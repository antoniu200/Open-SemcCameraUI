package org.apache.commons.imaging.util;

import java.io.Closeable;
import java.io.IOException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class IoUtils {
    public static void closeQuietly(boolean z, Closeable... closeableArr) throws IOException {
        IOException iOException = null;
        for (Closeable closeable : closeableArr) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    if (z && iOException == null) {
                        iOException = e;
                    }
                }
            }
        }
        if (iOException != null) {
            throw iOException;
        }
    }

    private IoUtils() {
    }
}
