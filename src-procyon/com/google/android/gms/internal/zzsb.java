// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.util.Collection;
import java.util.Iterator;
import java.util.Arrays;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class zzsb implements Cloneable
{
    private zzrz<?, ?> zzbir;
    private Object zzbis;
    private List<zzsg> zzbit;
    
    zzsb() {
        this.zzbit = new ArrayList<zzsg>();
    }
    
    private byte[] toByteArray() throws IOException {
        final byte[] array = new byte[this.zzB()];
        this.zza(zzrx.zzC(array));
        return array;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzsb)) {
            return false;
        }
        final zzsb zzsb = (zzsb)o;
        if (this.zzbis != null && zzsb.zzbis != null) {
            if (this.zzbir != zzsb.zzbir) {
                return false;
            }
            if (!this.zzbir.zzbil.isArray()) {
                return this.zzbis.equals(zzsb.zzbis);
            }
            if (this.zzbis instanceof byte[]) {
                return Arrays.equals((byte[])this.zzbis, (byte[])zzsb.zzbis);
            }
            if (this.zzbis instanceof int[]) {
                return Arrays.equals((int[])this.zzbis, (int[])zzsb.zzbis);
            }
            if (this.zzbis instanceof long[]) {
                return Arrays.equals((long[])this.zzbis, (long[])zzsb.zzbis);
            }
            if (this.zzbis instanceof float[]) {
                return Arrays.equals((float[])this.zzbis, (float[])zzsb.zzbis);
            }
            if (this.zzbis instanceof double[]) {
                return Arrays.equals((double[])this.zzbis, (double[])zzsb.zzbis);
            }
            if (this.zzbis instanceof boolean[]) {
                return Arrays.equals((boolean[])this.zzbis, (boolean[])zzsb.zzbis);
            }
            return Arrays.deepEquals((Object[])this.zzbis, (Object[])zzsb.zzbis);
        }
        else {
            if (this.zzbit != null && zzsb.zzbit != null) {
                return this.zzbit.equals(zzsb.zzbit);
            }
            try {
                return Arrays.equals(this.toByteArray(), zzsb.toByteArray());
            }
            catch (final IOException cause) {
                throw new IllegalStateException(cause);
            }
        }
    }
    
    @Override
    public int hashCode() {
        try {
            return 527 + Arrays.hashCode(this.toByteArray());
        }
        catch (final IOException cause) {
            throw new IllegalStateException(cause);
        }
    }
    
    int zzB() {
        if (this.zzbis != null) {
            return this.zzbir.zzX(this.zzbis);
        }
        final Iterator<zzsg> iterator = this.zzbit.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            n += iterator.next().zzB();
        }
        return n;
    }
    
    public final zzsb zzFI() {
        final zzsb zzsb = new zzsb();
        try {
            zzsb.zzbir = this.zzbir;
            if (this.zzbit == null) {
                zzsb.zzbit = null;
            }
            else {
                zzsb.zzbit.addAll(this.zzbit);
            }
            if (this.zzbis == null) {
                return zzsb;
            }
            Object zzbis = null;
            Label_0081: {
                if (this.zzbis instanceof zzse) {
                    zzbis = ((zzse)this.zzbis).zzFG();
                }
                else {
                    if (!(this.zzbis instanceof byte[])) {
                        final boolean b = this.zzbis instanceof byte[][];
                        int i = 0;
                        final int n = 0;
                        if (b) {
                            final byte[][] array = (byte[][])this.zzbis;
                            final byte[][] zzbis2 = new byte[array.length][];
                            zzsb.zzbis = zzbis2;
                            for (int j = n; j < array.length; ++j) {
                                zzbis2[j] = array[j].clone();
                            }
                        }
                        else {
                            if (this.zzbis instanceof boolean[]) {
                                zzbis = ((boolean[])this.zzbis).clone();
                                break Label_0081;
                            }
                            if (this.zzbis instanceof int[]) {
                                zzbis = ((int[])this.zzbis).clone();
                                break Label_0081;
                            }
                            if (this.zzbis instanceof long[]) {
                                zzbis = ((long[])this.zzbis).clone();
                                break Label_0081;
                            }
                            if (this.zzbis instanceof float[]) {
                                zzbis = ((float[])this.zzbis).clone();
                                break Label_0081;
                            }
                            if (this.zzbis instanceof double[]) {
                                zzbis = ((double[])this.zzbis).clone();
                                break Label_0081;
                            }
                            if (this.zzbis instanceof zzse[]) {
                                final zzse[] array2 = (zzse[])this.zzbis;
                                final zzse[] zzbis3 = new zzse[array2.length];
                                zzsb.zzbis = zzbis3;
                                while (i < array2.length) {
                                    zzbis3[i] = array2[i].zzFG();
                                    ++i;
                                }
                            }
                        }
                        return zzsb;
                    }
                    zzbis = ((byte[])this.zzbis).clone();
                }
            }
            zzsb.zzbis = zzbis;
            return zzsb;
        }
        catch (final CloneNotSupportedException detailMessage) {
            throw new AssertionError((Object)detailMessage);
        }
    }
    
    void zza(final zzrx zzrx) throws IOException {
        if (this.zzbis != null) {
            this.zzbir.zza(this.zzbis, zzrx);
            return;
        }
        final Iterator<zzsg> iterator = this.zzbit.iterator();
        while (iterator.hasNext()) {
            iterator.next().zza(zzrx);
        }
    }
    
    void zza(final zzsg zzsg) {
        this.zzbit.add(zzsg);
    }
    
     <T> T zzb(final zzrz<?, T> zzbir) {
        if (this.zzbis != null) {
            if (this.zzbir != zzbir) {
                throw new IllegalStateException("Tried to getExtension with a differernt Extension.");
            }
        }
        else {
            this.zzbir = zzbir;
            this.zzbis = zzbir.zzE(this.zzbit);
            this.zzbit = null;
        }
        return (T)this.zzbis;
    }
}
