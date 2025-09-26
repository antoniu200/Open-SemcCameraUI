// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.api;

import android.os.Parcel;
import android.content.IntentSender$SendIntentException;
import android.content.Intent;
import android.app.Activity;
import com.google.android.gms.common.internal.zzw;
import android.app.PendingIntent;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class Status implements Result, SafeParcelable
{
    public static final Parcelable$Creator<Status> CREATOR;
    public static final Status zzabb;
    public static final Status zzabc;
    public static final Status zzabd;
    public static final Status zzabe;
    public static final Status zzabf;
    private final PendingIntent mPendingIntent;
    private final int mVersionCode;
    private final int zzYm;
    private final String zzZZ;
    
    static {
        zzabb = new Status(0);
        zzabc = new Status(14);
        zzabd = new Status(8);
        zzabe = new Status(15);
        zzabf = new Status(16);
        CREATOR = (Parcelable$Creator)new zzd();
    }
    
    public Status(final int n) {
        this(n, null);
    }
    
    Status(final int mVersionCode, final int zzYm, final String zzZZ, final PendingIntent mPendingIntent) {
        this.mVersionCode = mVersionCode;
        this.zzYm = zzYm;
        this.zzZZ = zzZZ;
        this.mPendingIntent = mPendingIntent;
    }
    
    public Status(final int n, final String s) {
        this(1, n, s, null);
    }
    
    public Status(final int n, final String s, final PendingIntent pendingIntent) {
        this(1, n, s, pendingIntent);
    }
    
    private String zznI() {
        if (this.zzZZ != null) {
            return this.zzZZ;
        }
        return CommonStatusCodes.getStatusCodeString(this.zzYm);
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof Status;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final Status status = (Status)o;
        boolean b3 = b2;
        if (this.mVersionCode == status.mVersionCode) {
            b3 = b2;
            if (this.zzYm == status.zzYm) {
                b3 = b2;
                if (zzw.equal(this.zzZZ, status.zzZZ)) {
                    b3 = b2;
                    if (zzw.equal(this.mPendingIntent, status.mPendingIntent)) {
                        b3 = true;
                    }
                }
            }
        }
        return b3;
    }
    
    public PendingIntent getResolution() {
        return this.mPendingIntent;
    }
    
    @Override
    public Status getStatus() {
        return this;
    }
    
    public int getStatusCode() {
        return this.zzYm;
    }
    
    public String getStatusMessage() {
        return this.zzZZ;
    }
    
    int getVersionCode() {
        return this.mVersionCode;
    }
    
    public boolean hasResolution() {
        return this.mPendingIntent != null;
    }
    
    @Override
    public int hashCode() {
        return zzw.hashCode(this.mVersionCode, this.zzYm, this.zzZZ, this.mPendingIntent);
    }
    
    public boolean isCanceled() {
        return this.zzYm == 16;
    }
    
    public boolean isInterrupted() {
        return this.zzYm == 14;
    }
    
    public boolean isSuccess() {
        return this.zzYm <= 0;
    }
    
    public void startResolutionForResult(final Activity activity, final int n) throws IntentSender$SendIntentException {
        if (!this.hasResolution()) {
            return;
        }
        activity.startIntentSenderForResult(this.mPendingIntent.getIntentSender(), n, (Intent)null, 0, 0, 0);
    }
    
    @Override
    public String toString() {
        return zzw.zzv(this).zzg("statusCode", this.zznI()).zzg("resolution", this.mPendingIntent).toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzd.zza(this, parcel, n);
    }
    
    PendingIntent zznH() {
        return this.mPendingIntent;
    }
}
