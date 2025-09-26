// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public abstract class zze
{
    public static final zze zzaeL;
    public static final zze zzaeM;
    public static final zze zzaeN;
    public static final zze zzaeO;
    public static final zze zzaeP;
    public static final zze zzaeQ;
    public static final zze zzaeR;
    public static final zze zzaeS;
    public static final zze zzaeT;
    public static final zze zzaeU;
    public static final zze zzaeV;
    public static final zze zzaeW;
    public static final zze zzaeX;
    public static final zze zzaeY;
    public static final zze zzaeZ;
    
    static {
        zzaeL = zza("\t\n\u000b\f\r \u0085\u1680\u2028\u2029\u205f\u3000 \u180e\u202f").zza(zza('\u2000', '\u200a'));
        zzaeM = zza("\t\n\u000b\f\r \u0085\u1680\u2028\u2029\u205f\u3000").zza(zza('\u2000', '\u2006')).zza(zza('\u2008', '\u200a'));
        zzaeN = zza('\0', '\u007f');
        zze zzaeO2 = zza('0', '9');
        for (final char c : "\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".toCharArray()) {
            zzaeO2 = zzaeO2.zza(zza(c, (char)(c + '\t')));
        }
        zzaeO = zzaeO2;
        zzaeP = zza('\t', '\r').zza(zza('\u001c', ' ')).zza(zzc('\u1680')).zza(zzc('\u180e')).zza(zza('\u2000', '\u2006')).zza(zza('\u2008', '\u200b')).zza(zza('\u2028', '\u2029')).zza(zzc('\u205f')).zza(zzc('\u3000'));
        zzaeQ = new zze() {
            @Override
            public boolean zzd(final char ch) {
                return Character.isDigit(ch);
            }
        };
        zzaeR = new zze() {
            @Override
            public boolean zzd(final char ch) {
                return Character.isLetter(ch);
            }
        };
        zzaeS = new zze() {
            @Override
            public boolean zzd(final char ch) {
                return Character.isLetterOrDigit(ch);
            }
        };
        zzaeT = new zze() {
            @Override
            public boolean zzd(final char ch) {
                return Character.isUpperCase(ch);
            }
        };
        zzaeU = new zze() {
            @Override
            public boolean zzd(final char ch) {
                return Character.isLowerCase(ch);
            }
        };
        zzaeV = zza('\0', '\u001f').zza(zza('\u007f', '\u009f'));
        zzaeW = zza('\0', ' ').zza(zza('\u007f', ' ')).zza(zzc('\u00ad')).zza(zza('\u0600', '\u0603')).zza(zza("\u06dd\u070f\u1680\u17b4\u17b5\u180e")).zza(zza('\u2000', '\u200f')).zza(zza('\u2028', '\u202f')).zza(zza('\u205f', '\u2064')).zza(zza('\u206a', '\u206f')).zza(zzc('\u3000')).zza(zza('\ud800', '\uf8ff')).zza(zza("\ufeff\ufff9\ufffa\ufffb"));
        zzaeX = zza('\0', '\u04f9').zza(zzc('\u05be')).zza(zza('\u05d0', '\u05ea')).zza(zzc('\u05f3')).zza(zzc('\u05f4')).zza(zza('\u0600', '\u06ff')).zza(zza('\u0750', '\u077f')).zza(zza('\u0e00', '\u0e7f')).zza(zza('\u1e00', '\u20af')).zza(zza('\u2100', '\u213a')).zza(zza('\ufb50', '\ufdff')).zza(zza('\ufe70', '\ufeff')).zza(zza('\uff61', '\uffdc'));
        zzaeY = new zze() {
            @Override
            public zze zza(final zze zze) {
                zzx.zzw(zze);
                return this;
            }
            
            @Override
            public boolean zzb(final CharSequence charSequence) {
                zzx.zzw(charSequence);
                return true;
            }
            
            @Override
            public boolean zzd(final char c) {
                return true;
            }
        };
        zzaeZ = new zze() {
            @Override
            public zze zza(final zze zze) {
                return zzx.zzw(zze);
            }
            
            @Override
            public boolean zzb(final CharSequence charSequence) {
                return charSequence.length() == 0;
            }
            
            @Override
            public boolean zzd(final char c) {
                return false;
            }
        };
    }
    
    public static zze zza(final char c, final char c2) {
        zzx.zzaa(c2 >= c);
        return new zze(c, c2) {
            final char zzafd;
            final char zzafe;
            
            @Override
            public boolean zzd(final char c) {
                return this.zzafd <= c && c <= this.zzafe;
            }
        };
    }
    
    public static zze zza(final CharSequence charSequence) {
        switch (charSequence.length()) {
            default: {
                final char[] charArray = charSequence.toString().toCharArray();
                Arrays.sort(charArray);
                return new zze(charArray) {
                    final char[] zzafc;
                    
                    @Override
                    public boolean zzd(final char key) {
                        return Arrays.binarySearch(this.zzafc, key) >= 0;
                    }
                };
            }
            case 2: {
                return new zze(charSequence.charAt(0), charSequence.charAt(1)) {
                    final char zzafa;
                    final char zzafb;
                    
                    @Override
                    public boolean zzd(final char c) {
                        return c == this.zzafa || c == this.zzafb;
                    }
                };
            }
            case 1: {
                return zzc(charSequence.charAt(0));
            }
            case 0: {
                return zze.zzaeZ;
            }
        }
    }
    
    public static zze zzc(final char c) {
        return new zze(c) {
            final char zzaff;
            
            @Override
            public zze zza(final zze zze) {
                if (zze.zzd(this.zzaff)) {
                    return zze;
                }
                return super.zza(zze);
            }
            
            @Override
            public boolean zzd(final char c) {
                return c == this.zzaff;
            }
        };
    }
    
    public zze zza(final zze zze) {
        return new zza(Arrays.asList(this, zzx.zzw(zze)));
    }
    
    public boolean zzb(final CharSequence charSequence) {
        for (int i = charSequence.length() - 1; i >= 0; --i) {
            if (!this.zzd(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public abstract boolean zzd(final char p0);
    
    private static class zza extends zze
    {
        List<zze> zzafg;
        
        zza(final List<zze> zzafg) {
            this.zzafg = zzafg;
        }
        
        @Override
        public zze zza(final zze zze) {
            final ArrayList list = new ArrayList((Collection<? extends E>)this.zzafg);
            list.add(zzx.zzw(zze));
            return new zza(list);
        }
        
        @Override
        public boolean zzd(final char c) {
            final Iterator<zze> iterator = this.zzafg.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().zzd(c)) {
                    return true;
                }
            }
            return false;
        }
    }
}
