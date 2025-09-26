package com.google.android.gms.common.stats;

import android.os.SystemClock;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zze {
    private final long zzahV;
    private final int zzahW;
    private final SimpleArrayMap<String, Long> zzahX;

    public zze() {
        this.zzahV = 60000L;
        this.zzahW = 10;
        this.zzahX = new SimpleArrayMap<>(10);
    }

    public zze(int i, long j) {
        this.zzahV = j;
        this.zzahW = i;
        this.zzahX = new SimpleArrayMap<>();
    }

    private void zzb(long j, long j2) {
        for (int size = this.zzahX.size() - 1; size >= 0; size--) {
            if (j2 - this.zzahX.valueAt(size).longValue() > j) {
                this.zzahX.removeAt(size);
            }
        }
    }

    public Long zzcx(String str) {
        Long lPut;
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.zzahV;
        synchronized (this) {
            while (this.zzahX.size() >= this.zzahW) {
                zzb(j, jElapsedRealtime);
                j /= 2;
                Log.w("ConnectionTracker", "The max capacity " + this.zzahW + " is not enough. Current durationThreshold is: " + j);
            }
            lPut = this.zzahX.put(str, Long.valueOf(jElapsedRealtime));
        }
        return lPut;
    }

    public boolean zzcy(String str) {
        boolean z;
        synchronized (this) {
            z = this.zzahX.remove(str) != null;
        }
        return z;
    }
}
