// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

import java.util.HashMap;
import android.net.Uri;
import android.database.CharArrayBuffer;
import android.os.Parcel;
import android.database.CursorIndexOutOfBoundsException;
import java.util.List;
import java.util.Map;
import android.util.Log;
import java.util.ArrayList;
import com.google.android.gms.common.internal.zzx;
import android.database.CursorWindow;
import android.os.Bundle;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class DataHolder implements SafeParcelable
{
    public static final zze CREATOR;
    private static final zza zzadx;
    boolean mClosed;
    private final int mVersionCode;
    private final int zzYm;
    private final String[] zzadp;
    Bundle zzadq;
    private final CursorWindow[] zzadr;
    private final Bundle zzads;
    int[] zzadt;
    int zzadu;
    private Object zzadv;
    private boolean zzadw;
    
    static {
        CREATOR = new zze();
        zzadx = (zza)new zza(new String[0], null) {};
    }
    
    DataHolder(final int mVersionCode, final String[] zzadp, final CursorWindow[] zzadr, final int zzYm, final Bundle zzads) {
        this.mClosed = false;
        this.zzadw = true;
        this.mVersionCode = mVersionCode;
        this.zzadp = zzadp;
        this.zzadr = zzadr;
        this.zzYm = zzYm;
        this.zzads = zzads;
    }
    
    private DataHolder(final zza zza, final int n, final Bundle bundle) {
        this(zza.zzadp, zza(zza, -1), n, bundle);
    }
    
    public DataHolder(final String[] array, final CursorWindow[] array2, final int zzYm, final Bundle zzads) {
        this.mClosed = false;
        this.zzadw = true;
        this.mVersionCode = 1;
        this.zzadp = zzx.zzw(array);
        this.zzadr = zzx.zzw(array2);
        this.zzYm = zzYm;
        this.zzads = zzads;
        this.zzov();
    }
    
    public static DataHolder zza(final int n, final Bundle bundle) {
        return new DataHolder(DataHolder.zzadx, n, bundle);
    }
    
    private static CursorWindow[] zza(final zza zza, int i) {
        final int length = zza.zzadp.length;
        final int n = 0;
        if (length == 0) {
            return new CursorWindow[0];
        }
        List list;
        if (i >= 0 && i < zza.zzady.size()) {
            list = zza.zzady.subList(0, i);
        }
        else {
            list = zza.zzady;
        }
        final int size = list.size();
        CursorWindow e = new CursorWindow(false);
        final ArrayList<CursorWindow> list2 = new ArrayList<CursorWindow>();
        list2.add(e);
        e.setNumColumns(zza.zzadp.length);
        int n2 = 0;
        i = 0;
        while (i < size) {
            CursorWindow e2 = e;
            try {
                if (!e.allocRow()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Allocating additional cursor window for large data set (row ");
                    sb.append(i);
                    sb.append(")");
                    Log.d("DataHolder", sb.toString());
                    final CursorWindow cursorWindow = new CursorWindow(false);
                    cursorWindow.setStartPosition(i);
                    cursorWindow.setNumColumns(zza.zzadp.length);
                    list2.add(cursorWindow);
                    e2 = cursorWindow;
                    if (!cursorWindow.allocRow()) {
                        Log.e("DataHolder", "Unable to allocate row to hold data.");
                        list2.remove(cursorWindow);
                        return list2.toArray(new CursorWindow[list2.size()]);
                    }
                }
                final Map map = (Map)list.get(i);
                int n3;
                boolean b;
                String str;
                Object value;
                long longValue;
                StringBuilder sb2;
                for (n3 = 0, b = true; n3 < zza.zzadp.length && b; ++n3) {
                    str = zza.zzadp[n3];
                    value = map.get(str);
                    if (value == null) {
                        b = e2.putNull(i, n3);
                    }
                    else if (value instanceof String) {
                        b = e2.putString((String)value, i, n3);
                    }
                    else {
                        if (value instanceof Long) {
                            longValue = (long)value;
                        }
                        else {
                            if (value instanceof Integer) {
                                b = e2.putLong((long)(int)value, i, n3);
                                continue;
                            }
                            if (value instanceof Boolean) {
                                if (value) {
                                    longValue = 1L;
                                }
                                else {
                                    longValue = 0L;
                                }
                            }
                            else {
                                if (value instanceof byte[]) {
                                    b = e2.putBlob((byte[])value, i, n3);
                                    continue;
                                }
                                if (value instanceof Double) {
                                    b = e2.putDouble((double)value, i, n3);
                                    continue;
                                }
                                if (value instanceof Float) {
                                    b = e2.putDouble((double)(float)value, i, n3);
                                    continue;
                                }
                                sb2 = new StringBuilder();
                                sb2.append("Unsupported object for column ");
                                sb2.append(str);
                                sb2.append(": ");
                                sb2.append(value);
                                throw new IllegalArgumentException(sb2.toString());
                            }
                        }
                        b = e2.putLong(longValue, i, n3);
                    }
                }
                if (!b) {
                    if (n2 != 0) {
                        throw new zzb("Could not add the value to a new CursorWindow. The size of value may be larger than what a CursorWindow can handle.");
                    }
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Couldn't populate window data for row ");
                    sb3.append(i);
                    sb3.append(" - allocating new window.");
                    Log.d("DataHolder", sb3.toString());
                    e2.freeLastRow();
                    e2 = new CursorWindow(false);
                    e2.setStartPosition(i);
                    e2.setNumColumns(zza.zzadp.length);
                    list2.add(e2);
                    --i;
                    n2 = 1;
                }
                else {
                    n2 = 0;
                }
                ++i;
                e = e2;
                continue;
            }
            catch (final RuntimeException ex) {
                int size2;
                for (size2 = list2.size(), i = n; i < size2; ++i) {
                    list2.get(i).close();
                }
                throw ex;
            }
            break;
        }
        return list2.toArray(new CursorWindow[list2.size()]);
    }
    
    public static DataHolder zzbu(final int n) {
        return zza(n, null);
    }
    
    private void zzh(final String str, final int n) {
        if (this.zzadq == null || !this.zzadq.containsKey(str)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("No such column: ");
            sb.append(str);
            throw new IllegalArgumentException(sb.toString());
        }
        if (this.isClosed()) {
            throw new IllegalArgumentException("Buffer is closed.");
        }
        if (n >= 0 && n < this.zzadu) {
            return;
        }
        throw new CursorIndexOutOfBoundsException(n, this.zzadu);
    }
    
    public void close() {
        synchronized (this) {
            if (!this.mClosed) {
                this.mClosed = true;
                for (int i = 0; i < this.zzadr.length; ++i) {
                    this.zzadr[i].close();
                }
            }
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            if (this.zzadw && this.zzadr.length > 0 && !this.isClosed()) {
                String str;
                if (this.zzadv == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("internal object: ");
                    sb.append(this.toString());
                    str = sb.toString();
                }
                else {
                    str = this.zzadv.toString();
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Internal data leak within a DataBuffer object detected!  Be sure to explicitly call release() on all DataBuffer extending objects when you are done with them. (");
                sb2.append(str);
                sb2.append(")");
                Log.e("DataBuffer", sb2.toString());
                this.close();
            }
        }
        finally {
            super.finalize();
        }
    }
    
    public int getCount() {
        return this.zzadu;
    }
    
    public int getStatusCode() {
        return this.zzYm;
    }
    
    int getVersionCode() {
        return this.mVersionCode;
    }
    
    public boolean isClosed() {
        synchronized (this) {
            return this.mClosed;
        }
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zze.zza(this, parcel, n);
    }
    
    public void zza(final String s, final int n, final int n2, final CharArrayBuffer charArrayBuffer) {
        this.zzh(s, n);
        this.zzadr[n2].copyStringToBuffer(n, this.zzadq.getInt(s), charArrayBuffer);
    }
    
    public long zzb(final String s, final int n, final int n2) {
        this.zzh(s, n);
        return this.zzadr[n2].getLong(n, this.zzadq.getInt(s));
    }
    
    public int zzbt(int n) {
        int n2 = 0;
        zzx.zzZ(n >= 0 && n < this.zzadu);
        int n3;
        while (true) {
            n3 = n2;
            if (n2 >= this.zzadt.length) {
                break;
            }
            if (n < this.zzadt[n2]) {
                n3 = n2 - 1;
                break;
            }
            ++n2;
        }
        if ((n = n3) == this.zzadt.length) {
            n = n3 - 1;
        }
        return n;
    }
    
    public int zzc(final String s, final int n, final int n2) {
        this.zzh(s, n);
        return this.zzadr[n2].getInt(n, this.zzadq.getInt(s));
    }
    
    public boolean zzce(final String s) {
        return this.zzadq.containsKey(s);
    }
    
    public String zzd(final String s, final int n, final int n2) {
        this.zzh(s, n);
        return this.zzadr[n2].getString(n, this.zzadq.getInt(s));
    }
    
    public boolean zze(final String s, final int n, final int n2) {
        this.zzh(s, n);
        return this.zzadr[n2].getLong(n, this.zzadq.getInt(s)) == 1L;
    }
    
    public float zzf(final String s, final int n, final int n2) {
        this.zzh(s, n);
        return this.zzadr[n2].getFloat(n, this.zzadq.getInt(s));
    }
    
    public byte[] zzg(final String s, final int n, final int n2) {
        this.zzh(s, n);
        return this.zzadr[n2].getBlob(n, this.zzadq.getInt(s));
    }
    
    public Uri zzh(String zzd, final int n, final int n2) {
        zzd = this.zzd(zzd, n, n2);
        if (zzd == null) {
            return null;
        }
        return Uri.parse(zzd);
    }
    
    public boolean zzi(final String s, final int n, final int n2) {
        this.zzh(s, n);
        return this.zzadr[n2].isNull(n, this.zzadq.getInt(s));
    }
    
    public Bundle zzor() {
        return this.zzads;
    }
    
    public void zzov() {
        this.zzadq = new Bundle();
        final int n = 0;
        for (int i = 0; i < this.zzadp.length; ++i) {
            this.zzadq.putInt(this.zzadp[i], i);
        }
        this.zzadt = new int[this.zzadr.length];
        int zzadu = 0;
        for (int j = n; j < this.zzadr.length; ++j) {
            this.zzadt[j] = zzadu;
            zzadu += this.zzadr[j].getNumRows() - (zzadu - this.zzadr[j].getStartPosition());
        }
        this.zzadu = zzadu;
    }
    
    String[] zzow() {
        return this.zzadp;
    }
    
    CursorWindow[] zzox() {
        return this.zzadr;
    }
    
    public void zzr(final Object zzadv) {
        this.zzadv = zzadv;
    }
    
    public static class zza
    {
        private final HashMap<Object, Integer> zzadA;
        private boolean zzadB;
        private String zzadC;
        private final String[] zzadp;
        private final ArrayList<HashMap<String, Object>> zzady;
        private final String zzadz;
        
        private zza(final String[] array, final String zzadz) {
            this.zzadp = zzx.zzw(array);
            this.zzady = new ArrayList<HashMap<String, Object>>();
            this.zzadz = zzadz;
            this.zzadA = new HashMap<Object, Integer>();
            this.zzadB = false;
            this.zzadC = null;
        }
    }
    
    public static class zzb extends RuntimeException
    {
        public zzb(final String message) {
            super(message);
        }
    }
}
