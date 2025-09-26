// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.api;

import com.google.android.gms.common.internal.zzx;

public class BooleanResult implements Result
{
    private final Status zzSC;
    private final boolean zzaaE;
    
    public BooleanResult(final Status status, final boolean zzaaE) {
        this.zzSC = zzx.zzb(status, "Status must not be null");
        this.zzaaE = zzaaE;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BooleanResult)) {
            return false;
        }
        final BooleanResult booleanResult = (BooleanResult)o;
        return this.zzSC.equals(booleanResult.zzSC) && this.zzaaE == booleanResult.zzaaE;
    }
    
    @Override
    public Status getStatus() {
        return this.zzSC;
    }
    
    public boolean getValue() {
        return this.zzaaE;
    }
    
    @Override
    public final int hashCode() {
        return 31 * (527 + this.zzSC.hashCode()) + (this.zzaaE ? 1 : 0);
    }
}
