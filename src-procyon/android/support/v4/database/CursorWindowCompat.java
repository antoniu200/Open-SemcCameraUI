// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.database;

import android.support.annotation.NonNull;
import android.os.Build$VERSION;
import android.database.CursorWindow;
import android.support.annotation.Nullable;

public final class CursorWindowCompat
{
    private CursorWindowCompat() {
    }
    
    @NonNull
    public static CursorWindow create(@Nullable final String s, final long n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return new CursorWindow(s, n);
        }
        if (Build$VERSION.SDK_INT >= 15) {
            return new CursorWindow(s);
        }
        return new CursorWindow(false);
    }
}
