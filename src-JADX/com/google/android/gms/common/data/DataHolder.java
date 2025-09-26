package com.google.android.gms.common.data;

import android.database.CharArrayBuffer;
import android.database.CursorIndexOutOfBoundsException;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class DataHolder implements SafeParcelable {
    public static final zze CREATOR = new zze();
    private static final zza zzadx = new zza(new String[0], null) { // from class: com.google.android.gms.common.data.DataHolder.1
    };
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

    public static class zza {
        private final HashMap<Object, Integer> zzadA;
        private boolean zzadB;
        private String zzadC;
        private final String[] zzadp;
        private final ArrayList<HashMap<String, Object>> zzady;
        private final String zzadz;

        private zza(String[] strArr, String str) {
            this.zzadp = (String[]) zzx.zzw(strArr);
            this.zzady = new ArrayList<>();
            this.zzadz = str;
            this.zzadA = new HashMap<>();
            this.zzadB = false;
            this.zzadC = null;
        }
    }

    public static class zzb extends RuntimeException {
        public zzb(String str) {
            super(str);
        }
    }

    DataHolder(int i, String[] strArr, CursorWindow[] cursorWindowArr, int i2, Bundle bundle) {
        this.mClosed = false;
        this.zzadw = true;
        this.mVersionCode = i;
        this.zzadp = strArr;
        this.zzadr = cursorWindowArr;
        this.zzYm = i2;
        this.zzads = bundle;
    }

    private DataHolder(zza zzaVar, int i, Bundle bundle) {
        this(zzaVar.zzadp, zza(zzaVar, -1), i, bundle);
    }

    public DataHolder(String[] strArr, CursorWindow[] cursorWindowArr, int i, Bundle bundle) {
        this.mClosed = false;
        this.zzadw = true;
        this.mVersionCode = 1;
        this.zzadp = (String[]) zzx.zzw(strArr);
        this.zzadr = (CursorWindow[]) zzx.zzw(cursorWindowArr);
        this.zzYm = i;
        this.zzads = bundle;
        zzov();
    }

    public static DataHolder zza(int i, Bundle bundle) {
        return new DataHolder(zzadx, i, bundle);
    }

    private static CursorWindow[] zza(zza zzaVar, int i) {
        long jLongValue;
        if (zzaVar.zzadp.length == 0) {
            return new CursorWindow[0];
        }
        List listSubList = (i < 0 || i >= zzaVar.zzady.size()) ? zzaVar.zzady : zzaVar.zzady.subList(0, i);
        int size = listSubList.size();
        CursorWindow cursorWindow = new CursorWindow(false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(cursorWindow);
        cursorWindow.setNumColumns(zzaVar.zzadp.length);
        boolean z = false;
        CursorWindow cursorWindow2 = cursorWindow;
        int i2 = 0;
        while (i2 < size) {
            try {
                if (!cursorWindow2.allocRow()) {
                    Log.d("DataHolder", "Allocating additional cursor window for large data set (row " + i2 + ")");
                    cursorWindow2 = new CursorWindow(false);
                    cursorWindow2.setStartPosition(i2);
                    cursorWindow2.setNumColumns(zzaVar.zzadp.length);
                    arrayList.add(cursorWindow2);
                    if (!cursorWindow2.allocRow()) {
                        Log.e("DataHolder", "Unable to allocate row to hold data.");
                        arrayList.remove(cursorWindow2);
                        return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
                    }
                }
                Map map = (Map) listSubList.get(i2);
                boolean zPutDouble = true;
                for (int i3 = 0; i3 < zzaVar.zzadp.length && zPutDouble; i3++) {
                    String str = zzaVar.zzadp[i3];
                    Object obj = map.get(str);
                    if (obj == null) {
                        zPutDouble = cursorWindow2.putNull(i2, i3);
                    } else if (obj instanceof String) {
                        zPutDouble = cursorWindow2.putString((String) obj, i2, i3);
                    } else {
                        if (obj instanceof Long) {
                            jLongValue = ((Long) obj).longValue();
                        } else if (obj instanceof Integer) {
                            zPutDouble = cursorWindow2.putLong(((Integer) obj).intValue(), i2, i3);
                        } else if (obj instanceof Boolean) {
                            jLongValue = ((Boolean) obj).booleanValue() ? 1L : 0L;
                        } else if (obj instanceof byte[]) {
                            zPutDouble = cursorWindow2.putBlob((byte[]) obj, i2, i3);
                        } else if (obj instanceof Double) {
                            zPutDouble = cursorWindow2.putDouble(((Double) obj).doubleValue(), i2, i3);
                        } else {
                            if (!(obj instanceof Float)) {
                                throw new IllegalArgumentException("Unsupported object for column " + str + ": " + obj);
                            }
                            zPutDouble = cursorWindow2.putDouble(((Float) obj).floatValue(), i2, i3);
                        }
                        zPutDouble = cursorWindow2.putLong(jLongValue, i2, i3);
                    }
                }
                if (zPutDouble) {
                    z = false;
                } else {
                    if (z) {
                        throw new zzb("Could not add the value to a new CursorWindow. The size of value may be larger than what a CursorWindow can handle.");
                    }
                    Log.d("DataHolder", "Couldn't populate window data for row " + i2 + " - allocating new window.");
                    cursorWindow2.freeLastRow();
                    cursorWindow2 = new CursorWindow(false);
                    cursorWindow2.setStartPosition(i2);
                    cursorWindow2.setNumColumns(zzaVar.zzadp.length);
                    arrayList.add(cursorWindow2);
                    i2--;
                    z = true;
                }
                i2++;
            } catch (RuntimeException e) {
                int size2 = arrayList.size();
                for (int i4 = 0; i4 < size2; i4++) {
                    ((CursorWindow) arrayList.get(i4)).close();
                }
                throw e;
            }
        }
        return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
    }

    public static DataHolder zzbu(int i) {
        return zza(i, (Bundle) null);
    }

    private void zzh(String str, int i) {
        if (this.zzadq == null || !this.zzadq.containsKey(str)) {
            throw new IllegalArgumentException("No such column: " + str);
        }
        if (isClosed()) {
            throw new IllegalArgumentException("Buffer is closed.");
        }
        if (i < 0 || i >= this.zzadu) {
            throw new CursorIndexOutOfBoundsException(i, this.zzadu);
        }
    }

    public void close() {
        synchronized (this) {
            if (!this.mClosed) {
                this.mClosed = true;
                for (int i = 0; i < this.zzadr.length; i++) {
                    this.zzadr[i].close();
                }
            }
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    protected void finalize() throws Throwable {
        String string;
        try {
            if (this.zzadw && this.zzadr.length > 0 && !isClosed()) {
                if (this.zzadv == null) {
                    string = "internal object: " + toString();
                } else {
                    string = this.zzadv.toString();
                }
                Log.e("DataBuffer", "Internal data leak within a DataBuffer object detected!  Be sure to explicitly call release() on all DataBuffer extending objects when you are done with them. (" + string + ")");
                close();
            }
        } finally {
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
        boolean z;
        synchronized (this) {
            z = this.mClosed;
        }
        return z;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zze.zza(this, parcel, i);
    }

    public void zza(String str, int i, int i2, CharArrayBuffer charArrayBuffer) {
        zzh(str, i);
        this.zzadr[i2].copyStringToBuffer(i, this.zzadq.getInt(str), charArrayBuffer);
    }

    public long zzb(String str, int i, int i2) {
        zzh(str, i);
        return this.zzadr[i2].getLong(i, this.zzadq.getInt(str));
    }

    public int zzbt(int i) {
        int i2 = 0;
        zzx.zzZ(i >= 0 && i < this.zzadu);
        while (true) {
            if (i2 >= this.zzadt.length) {
                break;
            }
            if (i < this.zzadt[i2]) {
                i2--;
                break;
            }
            i2++;
        }
        return i2 == this.zzadt.length ? i2 - 1 : i2;
    }

    public int zzc(String str, int i, int i2) {
        zzh(str, i);
        return this.zzadr[i2].getInt(i, this.zzadq.getInt(str));
    }

    public boolean zzce(String str) {
        return this.zzadq.containsKey(str);
    }

    public String zzd(String str, int i, int i2) {
        zzh(str, i);
        return this.zzadr[i2].getString(i, this.zzadq.getInt(str));
    }

    public boolean zze(String str, int i, int i2) {
        zzh(str, i);
        return Long.valueOf(this.zzadr[i2].getLong(i, this.zzadq.getInt(str))).longValue() == 1;
    }

    public float zzf(String str, int i, int i2) {
        zzh(str, i);
        return this.zzadr[i2].getFloat(i, this.zzadq.getInt(str));
    }

    public byte[] zzg(String str, int i, int i2) {
        zzh(str, i);
        return this.zzadr[i2].getBlob(i, this.zzadq.getInt(str));
    }

    public Uri zzh(String str, int i, int i2) {
        String strZzd = zzd(str, i, i2);
        if (strZzd == null) {
            return null;
        }
        return Uri.parse(strZzd);
    }

    public boolean zzi(String str, int i, int i2) {
        zzh(str, i);
        return this.zzadr[i2].isNull(i, this.zzadq.getInt(str));
    }

    public Bundle zzor() {
        return this.zzads;
    }

    public void zzov() {
        this.zzadq = new Bundle();
        for (int i = 0; i < this.zzadp.length; i++) {
            this.zzadq.putInt(this.zzadp[i], i);
        }
        this.zzadt = new int[this.zzadr.length];
        int numRows = 0;
        for (int i2 = 0; i2 < this.zzadr.length; i2++) {
            this.zzadt[i2] = numRows;
            numRows += this.zzadr[i2].getNumRows() - (numRows - this.zzadr[i2].getStartPosition());
        }
        this.zzadu = numRows;
    }

    String[] zzow() {
        return this.zzadp;
    }

    CursorWindow[] zzox() {
        return this.zzadr;
    }

    public void zzr(Object obj) {
        this.zzadv = obj;
    }
}
