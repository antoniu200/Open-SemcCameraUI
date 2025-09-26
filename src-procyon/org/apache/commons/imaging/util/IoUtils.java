// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.util;

import java.io.IOException;
import java.io.Closeable;

public final class IoUtils
{
    private IoUtils() {
    }
    
    public static void closeQuietly(final boolean b, final Closeable... array) throws IOException {
        final int length = array.length;
        IOException ex = null;
        IOException ex2;
        for (int i = 0; i < length; ++i, ex = ex2) {
            final Closeable closeable = array[i];
            ex2 = ex;
            if (closeable != null) {
                try {
                    closeable.close();
                    ex2 = ex;
                }
                catch (final IOException ex3) {
                    ex2 = ex;
                    if (b && (ex2 = ex) == null) {
                        ex2 = ex3;
                    }
                }
            }
        }
        if (ex != null) {
            throw ex;
        }
    }
}
