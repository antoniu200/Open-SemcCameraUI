package com.google.android.gms.common.data;

import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class zzf<T> extends AbstractDataBuffer<T> {
    private boolean zzadD;
    private ArrayList<Integer> zzadE;

    protected zzf(DataHolder dataHolder) {
        super(dataHolder);
        this.zzadD = false;
    }

    private void zzoz() {
        synchronized (this) {
            if (!this.zzadD) {
                int count = this.zzabq.getCount();
                this.zzadE = new ArrayList<>();
                if (count > 0) {
                    this.zzadE.add(0);
                    String strZzoy = zzoy();
                    String strZzd = this.zzabq.zzd(strZzoy, 0, this.zzabq.zzbt(0));
                    for (int i = 1; i < count; i++) {
                        int iZzbt = this.zzabq.zzbt(i);
                        String strZzd2 = this.zzabq.zzd(strZzoy, i, iZzbt);
                        if (strZzd2 == null) {
                            throw new NullPointerException("Missing value for markerColumn: " + strZzoy + ", at row: " + i + ", for window: " + iZzbt);
                        }
                        if (!strZzd2.equals(strZzd)) {
                            this.zzadE.add(Integer.valueOf(i));
                            strZzd = strZzd2;
                        }
                    }
                }
                this.zzadD = true;
            }
        }
    }

    @Override // com.google.android.gms.common.data.AbstractDataBuffer, com.google.android.gms.common.data.DataBuffer
    public final T get(int i) {
        zzoz();
        return zzj(zzbw(i), zzbx(i));
    }

    @Override // com.google.android.gms.common.data.AbstractDataBuffer, com.google.android.gms.common.data.DataBuffer
    public int getCount() {
        zzoz();
        return this.zzadE.size();
    }

    int zzbw(int i) {
        if (i >= 0 && i < this.zzadE.size()) {
            return this.zzadE.get(i).intValue();
        }
        throw new IllegalArgumentException("Position " + i + " is out of bounds for this buffer");
    }

    protected int zzbx(int i) {
        if (i < 0 || i == this.zzadE.size()) {
            return 0;
        }
        int count = (i == this.zzadE.size() - 1 ? this.zzabq.getCount() : this.zzadE.get(i + 1).intValue()) - this.zzadE.get(i).intValue();
        if (count == 1) {
            int iZzbw = zzbw(i);
            int iZzbt = this.zzabq.zzbt(iZzbw);
            String strZzoA = zzoA();
            if (strZzoA != null && this.zzabq.zzd(strZzoA, iZzbw, iZzbt) == null) {
                return 0;
            }
        }
        return count;
    }

    protected abstract T zzj(int i, int i2);

    protected String zzoA() {
        return null;
    }

    protected abstract String zzoy();
}
