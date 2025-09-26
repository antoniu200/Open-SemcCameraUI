// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.content;

import java.util.ArrayList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class MimeTypeFilter
{
    private MimeTypeFilter() {
    }
    
    @Nullable
    public static String matches(@Nullable String s, @NonNull final String[] array) {
        if (s == null) {
            return null;
        }
        final String[] split = s.split("/");
        for (int length = array.length, i = 0; i < length; ++i) {
            s = array[i];
            if (mimeTypeAgainstFilter(split, s.split("/"))) {
                return s;
            }
        }
        return null;
    }
    
    @Nullable
    public static String matches(@Nullable final String[] array, @NonNull String s) {
        if (array == null) {
            return null;
        }
        final String[] split = s.split("/");
        for (int length = array.length, i = 0; i < length; ++i) {
            s = array[i];
            if (mimeTypeAgainstFilter(s.split("/"), split)) {
                return s;
            }
        }
        return null;
    }
    
    public static boolean matches(@Nullable final String s, @NonNull final String s2) {
        return s != null && mimeTypeAgainstFilter(s.split("/"), s2.split("/"));
    }
    
    @NonNull
    public static String[] matchesMany(@Nullable final String[] array, @NonNull String e) {
        int i = 0;
        if (array == null) {
            return new String[0];
        }
        final ArrayList list = new ArrayList();
        final String[] split = e.split("/");
        while (i < array.length) {
            e = array[i];
            if (mimeTypeAgainstFilter(e.split("/"), split)) {
                list.add(e);
            }
            ++i;
        }
        return list.toArray(new String[list.size()]);
    }
    
    private static boolean mimeTypeAgainstFilter(@NonNull final String[] array, @NonNull final String[] array2) {
        if (array2.length != 2) {
            throw new IllegalArgumentException("Ill-formatted MIME type filter. Must be type/subtype.");
        }
        if (!array2[0].isEmpty() && !array2[1].isEmpty()) {
            return array.length == 2 && ("*".equals(array2[0]) || array2[0].equals(array[0])) && ("*".equals(array2[1]) || array2[1].equals(array[1]));
        }
        throw new IllegalArgumentException("Ill-formatted MIME type filter. Type or subtype empty.");
    }
}
