// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.os;

import android.os.Message;
import android.os.Build$VERSION;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.os.Handler;

public final class HandlerCompat
{
    private HandlerCompat() {
    }
    
    public static boolean postDelayed(@NonNull final Handler handler, @NonNull final Runnable runnable, @Nullable final Object obj, final long n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return handler.postDelayed(runnable, obj, n);
        }
        final Message obtain = Message.obtain(handler, runnable);
        obtain.obj = obj;
        return handler.sendMessageDelayed(obtain, n);
    }
}
