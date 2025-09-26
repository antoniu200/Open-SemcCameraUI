// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.os.Message;
import android.os.Handler;
import com.google.android.gms.common.internal.zzx;
import android.os.Looper;

public final class zzlm<L>
{
    private volatile L mListener;
    private final zza zzacG;
    
    public zzlm(final Looper looper, final L l) {
        this.zzacG = new zza(looper);
        this.mListener = zzx.zzb(l, "Listener must not be null");
    }
    
    public void clear() {
        this.mListener = null;
    }
    
    public void zza(final zzb<? super L> zzb) {
        zzx.zzb(zzb, "Notifier must not be null");
        this.zzacG.sendMessage(this.zzacG.obtainMessage(1, (Object)zzb));
    }
    
    void zzb(final zzb<? super L> zzb) {
        final L mListener = this.mListener;
        if (mListener == null) {
            zzb.zznN();
            return;
        }
        try {
            zzb.zzq(mListener);
        }
        catch (final RuntimeException ex) {
            zzb.zznN();
            throw ex;
        }
    }
    
    private final class zza extends Handler
    {
        final zzlm zzacH;
        
        public zza(final zzlm zzacH, final Looper looper) {
            this.zzacH = zzacH;
            super(looper);
        }
        
        public void handleMessage(final Message message) {
            final int what = message.what;
            boolean b = true;
            if (what != 1) {
                b = false;
            }
            zzx.zzaa(b);
            this.zzacH.zzb((zzb<? super L>)message.obj);
        }
    }
    
    public interface zzb<L>
    {
        void zznN();
        
        void zzq(final L p0);
    }
}
