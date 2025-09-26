// 
// Decompiled by Procyon v0.6.0
// 

package android.arch.lifecycle;

import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY })
public interface GenericLifecycleObserver extends LifecycleObserver
{
    void onStateChanged(final LifecycleOwner p0, final Lifecycle.Event p1);
}
