package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.common.internal.zzx;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzlm<L> {
    private volatile L mListener;
    private final zzlm<L>.zza zzacG;

    private final class zza extends Handler {
        public zza(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            zzx.zzaa(message.what == 1);
            zzlm.this.zzb((zzb) message.obj);
        }
    }

    public interface zzb<L> {
        void zznN();

        void zzq(L l);
    }

    public zzlm(Looper looper, L l) {
        this.zzacG = new zza(looper);
        this.mListener = (L) zzx.zzb(l, "Listener must not be null");
    }

    public void clear() {
        this.mListener = null;
    }

    public void zza(zzb<? super L> zzbVar) {
        zzx.zzb(zzbVar, "Notifier must not be null");
        this.zzacG.sendMessage(this.zzacG.obtainMessage(1, zzbVar));
    }

    void zzb(zzb<? super L> zzbVar) {
        L l = this.mListener;
        if (l == null) {
            zzbVar.zznN();
            return;
        }
        try {
            zzbVar.zzq(l);
        } catch (RuntimeException e) {
            zzbVar.zznN();
            throw e;
        }
    }
}
