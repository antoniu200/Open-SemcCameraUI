package com.google.android.gms.internal;

import com.google.android.gms.internal.zzry;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzrz<M extends zzry<M>, T> {
    public final int tag;
    protected final int type;
    protected final Class<T> zzbil;
    protected final boolean zzbim;

    private zzrz(int i, Class<T> cls, int i2, boolean z) {
        this.type = i;
        this.zzbil = cls;
        this.tag = i2;
        this.zzbim = z;
    }

    private T zzF(List<zzsg> list) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            zzsg zzsgVar = list.get(i);
            if (zzsgVar.zzbiw.length != 0) {
                zza(zzsgVar, arrayList);
            }
        }
        int size = arrayList.size();
        if (size == 0) {
            return null;
        }
        T tCast = this.zzbil.cast(Array.newInstance(this.zzbil.getComponentType(), size));
        for (int i2 = 0; i2 < size; i2++) {
            Array.set(tCast, i2, arrayList.get(i2));
        }
        return tCast;
    }

    private T zzG(List<zzsg> list) {
        if (list.isEmpty()) {
            return null;
        }
        return this.zzbil.cast(zzF(zzrw.zzB(list.get(list.size() - 1).zzbiw)));
    }

    public static <M extends zzry<M>, T extends zzse> zzrz<M, T> zza(int i, Class<T> cls, long j) {
        return new zzrz<>(i, cls, (int) j, false);
    }

    final T zzE(List<zzsg> list) {
        if (list == null) {
            return null;
        }
        return this.zzbim ? zzF(list) : zzG(list);
    }

    protected Object zzF(zzrw zzrwVar) {
        Class componentType = this.zzbim ? this.zzbil.getComponentType() : this.zzbil;
        try {
            switch (this.type) {
                case 10:
                    zzse zzseVar = (zzse) componentType.newInstance();
                    zzrwVar.zza(zzseVar, zzsh.zzlV(this.tag));
                    return zzseVar;
                case 11:
                    zzse zzseVar2 = (zzse) componentType.newInstance();
                    zzrwVar.zza(zzseVar2);
                    return zzseVar2;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading extension field", e);
        } catch (IllegalAccessException e2) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType, e2);
        } catch (InstantiationException e3) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType, e3);
        }
    }

    int zzX(Object obj) {
        return this.zzbim ? zzY(obj) : zzZ(obj);
    }

    protected int zzY(Object obj) {
        int length = Array.getLength(obj);
        int iZzZ = 0;
        for (int i = 0; i < length; i++) {
            if (Array.get(obj, i) != null) {
                iZzZ += zzZ(Array.get(obj, i));
            }
        }
        return iZzZ;
    }

    protected int zzZ(Object obj) {
        int iZzlV = zzsh.zzlV(this.tag);
        switch (this.type) {
            case 10:
                return zzrx.zzb(iZzlV, (zzse) obj);
            case 11:
                return zzrx.zzc(iZzlV, (zzse) obj);
            default:
                throw new IllegalArgumentException("Unknown type " + this.type);
        }
    }

    protected void zza(zzsg zzsgVar, List<Object> list) {
        list.add(zzF(zzrw.zzB(zzsgVar.zzbiw)));
    }

    void zza(Object obj, zzrx zzrxVar) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (this.zzbim) {
            zzc(obj, zzrxVar);
        } else {
            zzb(obj, zzrxVar);
        }
    }

    protected void zzb(Object obj, zzrx zzrxVar) {
        try {
            zzrxVar.zzlN(this.tag);
            switch (this.type) {
                case 10:
                    int iZzlV = zzsh.zzlV(this.tag);
                    zzrxVar.zzb((zzse) obj);
                    zzrxVar.zzC(iZzlV, 4);
                    return;
                case 11:
                    zzrxVar.zzc((zzse) obj);
                    return;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    protected void zzc(Object obj, zzrx zzrxVar) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            Object obj2 = Array.get(obj, i);
            if (obj2 != null) {
                zzb(obj2, zzrxVar);
            }
        }
    }
}
