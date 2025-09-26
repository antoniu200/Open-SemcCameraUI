// 
// Decompiled by Procyon v0.6.0
// 

package android.arch.lifecycle;

import android.support.annotation.NonNull;

public interface LifecycleOwner
{
    @NonNull
    Lifecycle getLifecycle();
}
