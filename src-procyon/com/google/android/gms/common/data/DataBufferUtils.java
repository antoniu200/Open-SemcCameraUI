// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

import android.os.Bundle;
import java.util.Iterator;
import java.util.ArrayList;

public final class DataBufferUtils
{
    private DataBufferUtils() {
    }
    
    public static <T, E extends Freezable<T>> ArrayList<T> freezeAndClose(final DataBuffer<E> dataBuffer) {
        final ArrayList list = new ArrayList<Object>(dataBuffer.getCount());
        try {
            final Iterator iterator = dataBuffer.iterator();
            while (iterator.hasNext()) {
                list.add(((Freezable<Object>)iterator.next()).freeze());
            }
            return (ArrayList<T>)list;
        }
        finally {
            dataBuffer.close();
        }
    }
    
    public static boolean hasData(final DataBuffer<?> dataBuffer) {
        return dataBuffer != null && dataBuffer.getCount() > 0;
    }
    
    public static boolean hasNextPage(final DataBuffer<?> dataBuffer) {
        final Bundle zzor = dataBuffer.zzor();
        return zzor != null && zzor.getString("next_page_token") != null;
    }
    
    public static boolean hasPrevPage(final DataBuffer<?> dataBuffer) {
        final Bundle zzor = dataBuffer.zzor();
        return zzor != null && zzor.getString("prev_page_token") != null;
    }
}
