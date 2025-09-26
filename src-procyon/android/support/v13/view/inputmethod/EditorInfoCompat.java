// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v13.view.inputmethod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.os.Build$VERSION;
import android.view.inputmethod.EditorInfo;

public final class EditorInfoCompat
{
    private static final String CONTENT_MIME_TYPES_KEY = "android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
    private static final String[] EMPTY_STRING_ARRAY;
    public static final int IME_FLAG_FORCE_ASCII = Integer.MIN_VALUE;
    public static final int IME_FLAG_NO_PERSONALIZED_LEARNING = 16777216;
    
    static {
        EMPTY_STRING_ARRAY = new String[0];
    }
    
    @Deprecated
    public EditorInfoCompat() {
    }
    
    @NonNull
    public static String[] getContentMimeTypes(final EditorInfo editorInfo) {
        if (Build$VERSION.SDK_INT >= 25) {
            String[] array = editorInfo.contentMimeTypes;
            if (array == null) {
                array = EditorInfoCompat.EMPTY_STRING_ARRAY;
            }
            return array;
        }
        if (editorInfo.extras == null) {
            return EditorInfoCompat.EMPTY_STRING_ARRAY;
        }
        String[] array2 = editorInfo.extras.getStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
        if (array2 == null) {
            array2 = EditorInfoCompat.EMPTY_STRING_ARRAY;
        }
        return array2;
    }
    
    public static void setContentMimeTypes(@NonNull final EditorInfo editorInfo, @Nullable final String[] contentMimeTypes) {
        if (Build$VERSION.SDK_INT >= 25) {
            editorInfo.contentMimeTypes = contentMimeTypes;
        }
        else {
            if (editorInfo.extras == null) {
                editorInfo.extras = new Bundle();
            }
            editorInfo.extras.putStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", contentMimeTypes);
        }
    }
}
