// 
// Decompiled by Procyon v0.6.0
// 

package android.arch.lifecycle;

import android.support.annotation.NonNull;

@Deprecated
public interface LifecycleRegistryOwner extends LifecycleOwner
{
    @NonNull
    LifecycleRegistry getLifecycle();
}
