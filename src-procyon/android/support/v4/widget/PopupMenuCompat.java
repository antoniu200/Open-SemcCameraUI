// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.widget;

import android.support.annotation.Nullable;
import android.widget.PopupMenu;
import android.os.Build$VERSION;
import android.view.View$OnTouchListener;
import android.support.annotation.NonNull;

public final class PopupMenuCompat
{
    private PopupMenuCompat() {
    }
    
    @Nullable
    public static View$OnTouchListener getDragToOpenListener(@NonNull final Object o) {
        if (Build$VERSION.SDK_INT >= 19) {
            return ((PopupMenu)o).getDragToOpenListener();
        }
        return null;
    }
}
