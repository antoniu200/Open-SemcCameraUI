// 
// Decompiled by Procyon v0.6.0
// 

package android.arch.lifecycle;

import android.support.annotation.Nullable;

public interface Observer<T>
{
    void onChanged(@Nullable final T p0);
}
