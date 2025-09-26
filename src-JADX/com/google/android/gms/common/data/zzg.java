package com.google.android.gms.common.data;

import java.util.NoSuchElementException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzg<T> extends zzb<T> {
    private T zzadF;

    public zzg(DataBuffer<T> dataBuffer) {
        super(dataBuffer);
    }

    @Override // com.google.android.gms.common.data.zzb, java.util.Iterator
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Cannot advance the iterator beyond " + this.zzadj);
        }
        this.zzadj++;
        if (this.zzadj == 0) {
            this.zzadF = this.zzadi.get(0);
            if (!(this.zzadF instanceof zzc)) {
                throw new IllegalStateException("DataBuffer reference of type " + this.zzadF.getClass() + " is not movable");
            }
        } else {
            ((zzc) this.zzadF).zzbr(this.zzadj);
        }
        return this.zzadF;
    }
}
