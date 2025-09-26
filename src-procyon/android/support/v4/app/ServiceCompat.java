// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.app.Service;

public final class ServiceCompat
{
    public static final int START_STICKY = 1;
    public static final int STOP_FOREGROUND_DETACH = 2;
    public static final int STOP_FOREGROUND_REMOVE = 1;
    
    private ServiceCompat() {
    }
    
    public static void stopForeground(@NonNull final Service service, final int n) {
        if (Build$VERSION.SDK_INT >= 24) {
            service.stopForeground(n);
        }
        else {
            boolean b = true;
            if ((n & 0x1) == 0x0) {
                b = false;
            }
            service.stopForeground(b);
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface StopForegroundFlags {
    }
}
