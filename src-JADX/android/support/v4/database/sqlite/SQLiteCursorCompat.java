package android.support.v4.database.sqlite;

import android.database.sqlite.SQLiteCursor;
import android.os.Build;
import android.support.annotation.NonNull;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class SQLiteCursorCompat {
    private SQLiteCursorCompat() {
    }

    public static void setFillWindowForwardOnly(@NonNull SQLiteCursor sQLiteCursor, boolean z) {
        if (Build.VERSION.SDK_INT >= 28) {
            sQLiteCursor.setFillWindowForwardOnly(z);
        }
    }
}
