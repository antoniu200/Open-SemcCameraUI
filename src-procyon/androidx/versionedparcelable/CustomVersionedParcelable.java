// 
// Decompiled by Procyon v0.6.0
// 

package androidx.versionedparcelable;

import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public abstract class CustomVersionedParcelable implements VersionedParcelable
{
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void onPostParceling() {
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void onPreParceling(final boolean b) {
    }
}
