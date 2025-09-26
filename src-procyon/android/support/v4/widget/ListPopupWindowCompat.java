// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.widget;

import android.support.annotation.Nullable;
import android.os.Build$VERSION;
import android.view.View$OnTouchListener;
import android.view.View;
import android.support.annotation.NonNull;
import android.widget.ListPopupWindow;

public final class ListPopupWindowCompat
{
    private ListPopupWindowCompat() {
    }
    
    @Nullable
    public static View$OnTouchListener createDragToOpenListener(@NonNull final ListPopupWindow listPopupWindow, @NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return listPopupWindow.createDragToOpenListener(view);
        }
        return null;
    }
    
    @Deprecated
    public static View$OnTouchListener createDragToOpenListener(final Object o, final View view) {
        return createDragToOpenListener((ListPopupWindow)o, view);
    }
}
