// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class zzrz<M extends zzry<M>, T>
{
    public final int tag;
    protected final int type;
    protected final Class<T> zzbil;
    protected final boolean zzbim;
    
    private zzrz(final int type, final Class<T> zzbil, final int tag, final boolean zzbim) {
        this.type = type;
        this.zzbil = zzbil;
        this.tag = tag;
        this.zzbim = zzbim;
    }
    
    private T zzF(final List<zzsg> list) {
        final ArrayList list2 = new ArrayList();
        final int n = 0;
        for (int i = 0; i < list.size(); ++i) {
            final zzsg zzsg = list.get(i);
            if (zzsg.zzbiw.length != 0) {
                this.zza(zzsg, list2);
            }
        }
        final int size = list2.size();
        if (size == 0) {
            return null;
        }
        final T cast = this.zzbil.cast(Array.newInstance(this.zzbil.getComponentType(), size));
        for (int j = n; j < size; ++j) {
            Array.set(cast, j, list2.get(j));
        }
        return cast;
    }
    
    private T zzG(final List<zzsg> list) {
        if (list.isEmpty()) {
            return null;
        }
        return this.zzbil.cast(this.zzF(zzrw.zzB(list.get(list.size() - 1).zzbiw)));
    }
    
    public static <M extends zzry<M>, T extends zzse> zzrz<M, T> zza(final int n, final Class<T> clazz, final long n2) {
        return new zzrz<M, T>(n, clazz, (int)n2, false);
    }
    
    final T zzE(final List<zzsg> list) {
        if (list == null) {
            return null;
        }
        if (this.zzbim) {
            return this.zzF(list);
        }
        return this.zzG(list);
    }
    
    protected Object zzF(final zzrw zzrw) {
        Class<?> clazz;
        if (this.zzbim) {
            clazz = this.zzbil.getComponentType();
        }
        else {
            clazz = this.zzbil;
        }
        try {
            switch (this.type) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown type ");
                    sb.append(this.type);
                    throw new IllegalArgumentException(sb.toString());
                }
                case 11: {
                    final zzse zzse = (zzse)clazz.newInstance();
                    zzrw.zza(zzse);
                    return zzse;
                }
                case 10: {
                    final zzse zzse2 = (zzse)clazz.newInstance();
                    zzrw.zza(zzse2, zzsh.zzlV(this.tag));
                    return zzse2;
                }
            }
        }
        catch (final IOException cause) {
            throw new IllegalArgumentException("Error reading extension field", cause);
        }
        catch (final IllegalAccessException cause2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Error creating instance of class ");
            sb2.append(clazz);
            throw new IllegalArgumentException(sb2.toString(), cause2);
        }
        catch (final InstantiationException cause3) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Error creating instance of class ");
            sb3.append(clazz);
            throw new IllegalArgumentException(sb3.toString(), cause3);
        }
    }
    
    int zzX(final Object o) {
        if (this.zzbim) {
            return this.zzY(o);
        }
        return this.zzZ(o);
    }
    
    protected int zzY(final Object o) {
        final int length = Array.getLength(o);
        int i = 0;
        int n = 0;
        while (i < length) {
            int n2 = n;
            if (Array.get(o, i) != null) {
                n2 = n + this.zzZ(Array.get(o, i));
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    protected int zzZ(final Object o) {
        final int zzlV = zzsh.zzlV(this.tag);
        switch (this.type) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown type ");
                sb.append(this.type);
                throw new IllegalArgumentException(sb.toString());
            }
            case 11: {
                return zzrx.zzc(zzlV, (zzse)o);
            }
            case 10: {
                return zzrx.zzb(zzlV, (zzse)o);
            }
        }
    }
    
    protected void zza(final zzsg zzsg, final List<Object> list) {
        list.add(this.zzF(zzrw.zzB(zzsg.zzbiw)));
    }
    
    void zza(final Object o, final zzrx zzrx) throws IOException {
        if (this.zzbim) {
            this.zzc(o, zzrx);
            return;
        }
        this.zzb(o, zzrx);
    }
    
    protected void zzb(Object o, final zzrx zzrx) {
        try {
            zzrx.zzlN(this.tag);
            switch (this.type) {
                default: {
                    o = new StringBuilder();
                    ((StringBuilder)o).append("Unknown type ");
                    ((StringBuilder)o).append(this.type);
                    throw new IllegalArgumentException(((StringBuilder)o).toString());
                }
                case 11: {
                    zzrx.zzc((zzse)o);
                    return;
                }
                case 10: {
                    final zzse zzse = (zzse)o;
                    final int zzlV = zzsh.zzlV(this.tag);
                    zzrx.zzb(zzse);
                    zzrx.zzC(zzlV, 4);
                }
            }
        }
        catch (final IOException cause) {
            throw new IllegalStateException(cause);
        }
    }
    
    protected void zzc(final Object o, final zzrx zzrx) {
        for (int length = Array.getLength(o), i = 0; i < length; ++i) {
            final Object value = Array.get(o, i);
            if (value != null) {
                this.zzb(value, zzrx);
            }
        }
    }
}
