// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

import java.util.ArrayList;

public abstract class zzf<T> extends AbstractDataBuffer<T>
{
    private boolean zzadD;
    private ArrayList<Integer> zzadE;
    
    protected zzf(final DataHolder dataHolder) {
        super(dataHolder);
        this.zzadD = false;
    }
    
    private void zzoz() {
        synchronized (this) {
            if (!this.zzadD) {
                final int count = this.zzabq.getCount();
                this.zzadE = new ArrayList<Integer>();
                if (count > 0) {
                    this.zzadE.add(0);
                    final String zzoy = this.zzoy();
                    String zzd = this.zzabq.zzd(zzoy, 0, this.zzabq.zzbt(0));
                    String s;
                    for (int i = 1; i < count; ++i, zzd = s) {
                        final int zzbt = this.zzabq.zzbt(i);
                        final String zzd2 = this.zzabq.zzd(zzoy, i, zzbt);
                        if (zzd2 == null) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Missing value for markerColumn: ");
                            sb.append(zzoy);
                            sb.append(", at row: ");
                            sb.append(i);
                            sb.append(", for window: ");
                            sb.append(zzbt);
                            throw new NullPointerException(sb.toString());
                        }
                        s = zzd;
                        if (!zzd2.equals(zzd)) {
                            this.zzadE.add(i);
                            s = zzd2;
                        }
                    }
                }
                this.zzadD = true;
            }
        }
    }
    
    @Override
    public final T get(final int n) {
        this.zzoz();
        return this.zzj(this.zzbw(n), this.zzbx(n));
    }
    
    @Override
    public int getCount() {
        this.zzoz();
        return this.zzadE.size();
    }
    
    int zzbw(final int n) {
        if (n >= 0 && n < this.zzadE.size()) {
            return this.zzadE.get(n);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Position ");
        sb.append(n);
        sb.append(" is out of bounds for this buffer");
        throw new IllegalArgumentException(sb.toString());
    }
    
    protected int zzbx(int zzbt) {
        if (zzbt < 0) {
            return 0;
        }
        if (zzbt == this.zzadE.size()) {
            return 0;
        }
        int n;
        if (zzbt == this.zzadE.size() - 1) {
            n = this.zzabq.getCount();
        }
        else {
            n = this.zzadE.get(zzbt + 1);
        }
        final int n2 = n - this.zzadE.get(zzbt);
        if (n2 == 1) {
            final int zzbw = this.zzbw(zzbt);
            zzbt = this.zzabq.zzbt(zzbw);
            final String zzoA = this.zzoA();
            if (zzoA != null && this.zzabq.zzd(zzoA, zzbw, zzbt) == null) {
                return 0;
            }
        }
        return n2;
    }
    
    protected abstract T zzj(final int p0, final int p1);
    
    protected String zzoA() {
        return null;
    }
    
    protected abstract String zzoy();
}
