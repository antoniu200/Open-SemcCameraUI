// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth;

import android.content.Intent;

public class GooglePlayServicesAvailabilityException extends UserRecoverableAuthException
{
    private final int zzRy;
    
    GooglePlayServicesAvailabilityException(final int zzRy, final String s, final Intent intent) {
        super(s, intent);
        this.zzRy = zzRy;
    }
    
    public int getConnectionStatusCode() {
        return this.zzRy;
    }
}
