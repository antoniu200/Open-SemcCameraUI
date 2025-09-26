// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.TreeSet;
import android.os.Parcel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Random;
import java.util.List;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class PasswordSpecification implements SafeParcelable
{
    public static final zze CREATOR;
    public static final PasswordSpecification zzSt;
    public static final PasswordSpecification zzSu;
    final int mVersionCode;
    private final int[] zzSA;
    final String zzSv;
    final List<String> zzSw;
    final List<Integer> zzSx;
    final int zzSy;
    final int zzSz;
    private final Random zzts;
    
    static {
        CREATOR = new zze();
        zzSt = new zza().zzg(12, 16).zzbD("abcdefghijkmnopqrstxyzABCDEFGHJKLMNPQRSTXY3456789").zzf("abcdefghijkmnopqrstxyz", 1).zzf("ABCDEFGHJKLMNPQRSTXY", 1).zzf("3456789", 1).zzlK();
        zzSu = new zza().zzg(12, 16).zzbD("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890").zzf("abcdefghijklmnopqrstuvwxyz", 1).zzf("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1).zzf("1234567890", 1).zzlK();
    }
    
    PasswordSpecification(final int mVersionCode, final String zzSv, final List<String> list, final List<Integer> list2, final int zzSy, final int zzSz) {
        this.mVersionCode = mVersionCode;
        this.zzSv = zzSv;
        this.zzSw = Collections.unmodifiableList((List<? extends String>)list);
        this.zzSx = Collections.unmodifiableList((List<? extends Integer>)list2);
        this.zzSy = zzSy;
        this.zzSz = zzSz;
        this.zzSA = this.zzlJ();
        this.zzts = new SecureRandom();
    }
    
    private int zza(final char c) {
        return c - ' ';
    }
    
    private static String zzb(final Collection<Character> collection) {
        final char[] value = new char[collection.size()];
        final Iterator iterator = collection.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            value[n] = (char)iterator.next();
            ++n;
        }
        return new String(value);
    }
    
    private static boolean zzb(final int n, final int n2, final int n3) {
        return n < n2 || n > n3;
    }
    
    private int[] zzlJ() {
        final int[] a = new int[95];
        Arrays.fill(a, -1);
        final Iterator<String> iterator = this.zzSw.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final char[] charArray = iterator.next().toCharArray();
            for (int length = charArray.length, i = 0; i < length; ++i) {
                a[this.zza(charArray[i])] = n;
            }
            ++n;
        }
        return a;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zze.zza(this, parcel, n);
    }
    
    public static class zza
    {
        private final TreeSet<Character> zzSB;
        private final List<String> zzSw;
        private final List<Integer> zzSx;
        private int zzSy;
        private int zzSz;
        
        public zza() {
            this.zzSB = new TreeSet<Character>();
            this.zzSw = new ArrayList<String>();
            this.zzSx = new ArrayList<Integer>();
            this.zzSy = 12;
            this.zzSz = 16;
        }
        
        private void zzlL() {
            final Iterator<Integer> iterator = this.zzSx.iterator();
            int n = 0;
            while (iterator.hasNext()) {
                n += iterator.next();
            }
            if (n > this.zzSz) {
                throw new zzb("required character count cannot be greater than the max password size");
            }
        }
        
        private void zzlM() {
            final boolean[] array = new boolean[95];
            final Iterator<String> iterator = this.zzSw.iterator();
            while (iterator.hasNext()) {
                for (final char c : iterator.next().toCharArray()) {
                    final int n = c - ' ';
                    if (array[n]) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("character ");
                        sb.append(c);
                        sb.append(" occurs in more than one required character set");
                        throw new zzb(sb.toString());
                    }
                    array[n] = true;
                }
            }
        }
        
        private TreeSet<Character> zzr(final String s, final String s2) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s2);
                sb.append(" cannot be null or empty");
                throw new zzb(sb.toString());
            }
            final TreeSet set = new TreeSet();
            for (final char c : s.toCharArray()) {
                if (zzb(c, 32, 126)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(s2);
                    sb2.append(" must only contain ASCII printable characters");
                    throw new zzb(sb2.toString());
                }
                set.add(c);
            }
            return set;
        }
        
        public zza zzbD(final String s) {
            this.zzSB.addAll(this.zzr(s, "allowedChars"));
            return this;
        }
        
        public zza zzf(final String s, final int i) {
            if (i < 1) {
                throw new zzb("count must be at least 1");
            }
            this.zzSw.add(zzb(this.zzr(s, "requiredChars")));
            this.zzSx.add(i);
            return this;
        }
        
        public zza zzg(final int zzSy, final int zzSz) {
            if (zzSy < 1) {
                throw new zzb("minimumSize must be at least 1");
            }
            if (zzSy > zzSz) {
                throw new zzb("maximumSize must be greater than or equal to minimumSize");
            }
            this.zzSy = zzSy;
            this.zzSz = zzSz;
            return this;
        }
        
        public PasswordSpecification zzlK() {
            if (this.zzSB.isEmpty()) {
                throw new zzb("no allowed characters specified");
            }
            this.zzlL();
            this.zzlM();
            return new PasswordSpecification(1, zzb(this.zzSB), this.zzSw, this.zzSx, this.zzSy, this.zzSz);
        }
    }
    
    public static class zzb extends Error
    {
        public zzb(final String message) {
            super(message);
        }
    }
}
