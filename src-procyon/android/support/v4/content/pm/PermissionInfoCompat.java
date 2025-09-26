// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.content.pm;

import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import android.annotation.SuppressLint;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.content.pm.PermissionInfo;

public final class PermissionInfoCompat
{
    private PermissionInfoCompat() {
    }
    
    @SuppressLint({ "WrongConstant" })
    public static int getProtection(@NonNull final PermissionInfo permissionInfo) {
        if (Build$VERSION.SDK_INT >= 28) {
            return permissionInfo.getProtection();
        }
        return permissionInfo.protectionLevel & 0xF;
    }
    
    @SuppressLint({ "WrongConstant" })
    public static int getProtectionFlags(@NonNull final PermissionInfo permissionInfo) {
        if (Build$VERSION.SDK_INT >= 28) {
            return permissionInfo.getProtectionFlags();
        }
        return permissionInfo.protectionLevel & 0xFFFFFFF0;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public @interface Protection {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @SuppressLint({ "UniqueConstants" })
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public @interface ProtectionFlags {
    }
}
