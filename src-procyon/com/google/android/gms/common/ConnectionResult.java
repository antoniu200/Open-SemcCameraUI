// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common;

import android.os.Parcel;
import android.content.IntentSender$SendIntentException;
import android.content.Intent;
import android.app.Activity;
import com.google.android.gms.common.internal.zzw;
import android.app.PendingIntent;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class ConnectionResult implements SafeParcelable
{
    public static final int API_UNAVAILABLE = 16;
    public static final int CANCELED = 13;
    public static final Parcelable$Creator<ConnectionResult> CREATOR;
    public static final int DEVELOPER_ERROR = 10;
    @Deprecated
    public static final int DRIVE_EXTERNAL_STORAGE_REQUIRED = 1500;
    public static final int INTERNAL_ERROR = 8;
    public static final int INTERRUPTED = 15;
    public static final int INVALID_ACCOUNT = 5;
    public static final int LICENSE_CHECK_FAILED = 11;
    public static final int NETWORK_ERROR = 7;
    public static final int RESOLUTION_REQUIRED = 6;
    public static final int SERVICE_DISABLED = 3;
    public static final int SERVICE_INVALID = 9;
    public static final int SERVICE_MISSING = 1;
    public static final int SERVICE_MISSING_PERMISSION = 19;
    public static final int SERVICE_UPDATING = 18;
    public static final int SERVICE_VERSION_UPDATE_REQUIRED = 2;
    public static final int SIGN_IN_FAILED = 17;
    public static final int SIGN_IN_REQUIRED = 4;
    public static final int SUCCESS = 0;
    public static final int TIMEOUT = 14;
    public static final ConnectionResult zzZY;
    private final PendingIntent mPendingIntent;
    final int mVersionCode;
    private final int zzYm;
    private final String zzZZ;
    
    static {
        zzZY = new ConnectionResult(0);
        CREATOR = (Parcelable$Creator)new zzb();
    }
    
    public ConnectionResult(final int n) {
        this(n, null, null);
    }
    
    ConnectionResult(final int mVersionCode, final int zzYm, final PendingIntent mPendingIntent, final String zzZZ) {
        this.mVersionCode = mVersionCode;
        this.zzYm = zzYm;
        this.mPendingIntent = mPendingIntent;
        this.zzZZ = zzZZ;
    }
    
    public ConnectionResult(final int n, final PendingIntent pendingIntent) {
        this(n, pendingIntent, null);
    }
    
    public ConnectionResult(final int n, final PendingIntent pendingIntent, final String s) {
        this(1, n, pendingIntent, s);
    }
    
    static String getStatusString(final int i) {
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("UNKNOWN_ERROR_CODE(");
                sb.append(i);
                sb.append(")");
                return sb.toString();
            }
            case 19: {
                return "SERVICE_MISSING_PERMISSION";
            }
            case 18: {
                return "SERVICE_UPDATING";
            }
            case 17: {
                return "SIGN_IN_FAILED";
            }
            case 16: {
                return "API_UNAVAILABLE";
            }
            case 15: {
                return "INTERRUPTED";
            }
            case 14: {
                return "TIMEOUT";
            }
            case 13: {
                return "CANCELED";
            }
            case 11: {
                return "LICENSE_CHECK_FAILED";
            }
            case 10: {
                return "DEVELOPER_ERROR";
            }
            case 9: {
                return "SERVICE_INVALID";
            }
            case 8: {
                return "INTERNAL_ERROR";
            }
            case 7: {
                return "NETWORK_ERROR";
            }
            case 6: {
                return "RESOLUTION_REQUIRED";
            }
            case 5: {
                return "INVALID_ACCOUNT";
            }
            case 4: {
                return "SIGN_IN_REQUIRED";
            }
            case 3: {
                return "SERVICE_DISABLED";
            }
            case 2: {
                return "SERVICE_VERSION_UPDATE_REQUIRED";
            }
            case 1: {
                return "SERVICE_MISSING";
            }
            case 0: {
                return "SUCCESS";
            }
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ConnectionResult)) {
            return false;
        }
        final ConnectionResult connectionResult = (ConnectionResult)o;
        return this.zzYm == connectionResult.zzYm && zzw.equal(this.mPendingIntent, connectionResult.mPendingIntent) && zzw.equal(this.zzZZ, connectionResult.zzZZ);
    }
    
    public int getErrorCode() {
        return this.zzYm;
    }
    
    public String getErrorMessage() {
        return this.zzZZ;
    }
    
    public PendingIntent getResolution() {
        return this.mPendingIntent;
    }
    
    public boolean hasResolution() {
        return this.zzYm != 0 && this.mPendingIntent != null;
    }
    
    @Override
    public int hashCode() {
        return zzw.hashCode(this.zzYm, this.mPendingIntent, this.zzZZ);
    }
    
    public boolean isSuccess() {
        return this.zzYm == 0;
    }
    
    public void startResolutionForResult(final Activity activity, final int n) throws IntentSender$SendIntentException {
        if (!this.hasResolution()) {
            return;
        }
        activity.startIntentSenderForResult(this.mPendingIntent.getIntentSender(), n, (Intent)null, 0, 0, 0);
    }
    
    @Override
    public String toString() {
        return zzw.zzv(this).zzg("statusCode", getStatusString(this.zzYm)).zzg("resolution", this.mPendingIntent).zzg("message", this.zzZZ).toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzb.zza(this, parcel, n);
    }
}
