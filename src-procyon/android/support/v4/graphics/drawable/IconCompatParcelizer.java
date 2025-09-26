// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.graphics.drawable;

import androidx.versionedparcelable.VersionedParcel;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY })
public final class IconCompatParcelizer extends androidx.core.graphics.drawable.IconCompatParcelizer
{
    public static IconCompat read(final VersionedParcel versionedParcel) {
        return androidx.core.graphics.drawable.IconCompatParcelizer.read(versionedParcel);
    }
    
    public static void write(final IconCompat iconCompat, final VersionedParcel versionedParcel) {
        androidx.core.graphics.drawable.IconCompatParcelizer.write(iconCompat, versionedParcel);
    }
}
