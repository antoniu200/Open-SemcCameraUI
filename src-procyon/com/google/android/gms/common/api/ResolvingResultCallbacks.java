// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.api;

import com.google.android.gms.common.internal.zzx;
import android.app.Activity;

public abstract class ResolvingResultCallbacks<R extends Result> extends ResultCallbacks<R>
{
    private final Activity mActivity;
    private final int zzaaY;
    
    protected ResolvingResultCallbacks(final Activity activity, final int zzaaY) {
        this.mActivity = zzx.zzb(activity, "Activity must not be null");
        this.zzaaY = zzaaY;
    }
    
    @Override
    public abstract void onSuccess(final R p0);
    
    public abstract void onUnresolvableFailure(final Status p0);
}
