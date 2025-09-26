// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.playlog.internal;

import com.google.android.gms.common.ConnectionResult;
import android.os.Bundle;
import com.google.android.gms.internal.zzqd;
import com.google.android.gms.common.api.GoogleApiClient;

public class zzd implements ConnectionCallbacks, OnConnectionFailedListener
{
    private zzf zzaRE;
    private final zzqd.zza zzaRP;
    private boolean zzaRQ;
    
    public zzd(final zzqd.zza zzaRP) {
        this.zzaRP = zzaRP;
        this.zzaRE = null;
        this.zzaRQ = true;
    }
    
    @Override
    public void onConnected(final Bundle bundle) {
        this.zzaRE.zzap(false);
        if (this.zzaRQ && this.zzaRP != null) {
            this.zzaRP.zzBr();
        }
        this.zzaRQ = false;
    }
    
    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        this.zzaRE.zzap(true);
        if (this.zzaRQ && this.zzaRP != null) {
            if (connectionResult.hasResolution()) {
                this.zzaRP.zzf(connectionResult.getResolution());
            }
            else {
                this.zzaRP.zzBs();
            }
        }
        this.zzaRQ = false;
    }
    
    @Override
    public void onConnectionSuspended(final int n) {
        this.zzaRE.zzap(true);
    }
    
    public void zza(final zzf zzaRE) {
        this.zzaRE = zzaRE;
    }
    
    public void zzao(final boolean zzaRQ) {
        this.zzaRQ = zzaRQ;
    }
}
