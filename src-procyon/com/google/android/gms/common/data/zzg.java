// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

import java.util.NoSuchElementException;

public class zzg<T> extends zzb<T>
{
    private T zzadF;
    
    public zzg(final DataBuffer<T> dataBuffer) {
        super(dataBuffer);
    }
    
    @Override
    public T next() {
        if (!this.hasNext()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot advance the iterator beyond ");
            sb.append(this.zzadj);
            throw new NoSuchElementException(sb.toString());
        }
        ++this.zzadj;
        if (this.zzadj == 0) {
            this.zzadF = this.zzadi.get(0);
            if (!(this.zzadF instanceof zzc)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("DataBuffer reference of type ");
                sb2.append(this.zzadF.getClass());
                sb2.append(" is not movable");
                throw new IllegalStateException(sb2.toString());
            }
        }
        else {
            ((zzc)this.zzadF).zzbr(this.zzadj);
        }
        return this.zzadF;
    }
}
