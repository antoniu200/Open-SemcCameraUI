// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.database.sqlite;

import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.database.sqlite.SQLiteCursor;

public final class SQLiteCursorCompat
{
    private SQLiteCursorCompat() {
    }
    
    public static void setFillWindowForwardOnly(@NonNull final SQLiteCursor sqLiteCursor, final boolean fillWindowForwardOnly) {
        if (Build$VERSION.SDK_INT >= 28) {
            sqLiteCursor.setFillWindowForwardOnly(fillWindowForwardOnly);
        }
    }
}
