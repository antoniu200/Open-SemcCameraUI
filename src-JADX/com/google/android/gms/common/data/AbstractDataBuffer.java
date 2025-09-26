package com.google.android.gms.common.data;

import android.os.Bundle;
import java.util.Iterator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class AbstractDataBuffer<T> implements DataBuffer<T> {
    protected final DataHolder zzabq;

    protected AbstractDataBuffer(DataHolder dataHolder) {
        this.zzabq = dataHolder;
        if (this.zzabq != null) {
            this.zzabq.zzr(this);
        }
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    @Deprecated
    public final void close() {
        release();
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public abstract T get(int i);

    @Override // com.google.android.gms.common.data.DataBuffer
    public int getCount() {
        if (this.zzabq == null) {
            return 0;
        }
        return this.zzabq.getCount();
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    @Deprecated
    public boolean isClosed() {
        return this.zzabq == null || this.zzabq.isClosed();
    }

    @Override // com.google.android.gms.common.data.DataBuffer, java.lang.Iterable
    public Iterator<T> iterator() {
        return new zzb(this);
    }

    @Override // com.google.android.gms.common.data.DataBuffer, com.google.android.gms.common.api.Releasable
    public void release() {
        if (this.zzabq != null) {
            this.zzabq.close();
        }
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public Iterator<T> singleRefIterator() {
        return new zzg(this);
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public Bundle zzor() {
        return this.zzabq.zzor();
    }
}
