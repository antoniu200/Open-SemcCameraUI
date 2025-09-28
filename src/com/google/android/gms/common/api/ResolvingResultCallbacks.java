package com.google.android.gms.common.api;

import android.app.Activity;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.internal.zzx;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class ResolvingResultCallbacks<R extends Result> extends ResultCallbacks<R> {
    private final Activity mActivity;
    private final int zzaaY;

    protected ResolvingResultCallbacks(Activity activity, int i) {
        this.mActivity = (Activity) zzx.zzb(activity, "Activity must not be null");
        this.zzaaY = i;
    }

    @Override // com.google.android.gms.common.api.ResultCallbacks
    public abstract void onSuccess(R r);

    public abstract void onUnresolvableFailure(Status status);
}
