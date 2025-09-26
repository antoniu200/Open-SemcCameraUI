// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.signin;

import java.util.HashSet;
import java.util.Arrays;
import android.os.Parcel;
import java.util.Iterator;
import com.google.android.gms.auth.api.signin.internal.zzc;
import java.util.List;
import java.util.Collections;
import android.text.TextUtils;
import java.util.Collection;
import java.util.Set;
import java.util.ArrayList;
import android.accounts.Account;
import com.google.android.gms.common.api.Scope;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.api.Api;

public class GoogleSignInConfig implements Optional, SafeParcelable
{
    public static final Parcelable$Creator<GoogleSignInConfig> CREATOR;
    public static final Scope zzTe;
    public static final Scope zzTf;
    public static final Scope zzTg;
    public static final GoogleSignInConfig zzTh;
    final int versionCode;
    private Account zzQd;
    private final ArrayList<Scope> zzSX;
    private boolean zzTi;
    private final boolean zzTj;
    private final boolean zzTk;
    private String zzTl;
    
    static {
        zzTe = new Scope("profile");
        zzTf = new Scope("email");
        zzTg = new Scope("openid");
        zzTh = new zza().zzmc();
        CREATOR = (Parcelable$Creator)new com.google.android.gms.auth.api.signin.zze();
    }
    
    GoogleSignInConfig(final int versionCode, final ArrayList<Scope> zzSX, final Account zzQd, final boolean zzTi, final boolean zzTj, final boolean zzTk, final String zzTl) {
        this.versionCode = versionCode;
        this.zzSX = zzSX;
        this.zzQd = zzQd;
        this.zzTi = zzTi;
        this.zzTj = zzTj;
        this.zzTk = zzTk;
        this.zzTl = zzTl;
    }
    
    private GoogleSignInConfig(final Set<Scope> c, final Account account, final boolean b, final boolean b2, final boolean b3, final String s) {
        this(1, new ArrayList<Scope>(c), account, b, b2, b3, s);
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        if (o == null) {
            return false;
        }
        try {
            final GoogleSignInConfig googleSignInConfig = (GoogleSignInConfig)o;
            boolean b2 = b;
            if (this.zzSX.size() == googleSignInConfig.zzlS().size()) {
                if (!this.zzSX.containsAll(googleSignInConfig.zzlS())) {
                    return false;
                }
                if (this.zzQd == null) {
                    b2 = b;
                    if (googleSignInConfig.getAccount() != null) {
                        return b2;
                    }
                }
                else {
                    b2 = b;
                    if (!this.zzQd.equals((Object)googleSignInConfig.getAccount())) {
                        return b2;
                    }
                }
                if (TextUtils.isEmpty((CharSequence)this.zzTl)) {
                    b2 = b;
                    if (!TextUtils.isEmpty((CharSequence)googleSignInConfig.zzmb())) {
                        return b2;
                    }
                }
                else {
                    b2 = b;
                    if (!this.zzTl.equals(googleSignInConfig.zzmb())) {
                        return b2;
                    }
                }
                b2 = b;
                if (this.zzTk == googleSignInConfig.zzma()) {
                    b2 = b;
                    if (this.zzTi == googleSignInConfig.zzlY()) {
                        final boolean zzTj = this.zzTj;
                        final boolean zzlZ = googleSignInConfig.zzlZ();
                        b2 = b;
                        if (zzTj == zzlZ) {
                            b2 = true;
                        }
                    }
                }
            }
            return b2;
        }
        catch (final ClassCastException ex) {
            return b;
        }
    }
    
    public Account getAccount() {
        return this.zzQd;
    }
    
    @Override
    public int hashCode() {
        final ArrayList list = new ArrayList();
        final Iterator<Scope> iterator = this.zzSX.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().zznG());
        }
        Collections.sort((List<Comparable>)list);
        return new com.google.android.gms.auth.api.signin.internal.zzc().zzl(list).zzl(this.zzQd).zzl(this.zzTl).zzP(this.zzTk).zzP(this.zzTi).zzP(this.zzTj).zzmd();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        com.google.android.gms.auth.api.signin.zze.zza(this, parcel, n);
    }
    
    public ArrayList<Scope> zzlS() {
        return new ArrayList<Scope>(this.zzSX);
    }
    
    public boolean zzlY() {
        return this.zzTi;
    }
    
    public boolean zzlZ() {
        return this.zzTj;
    }
    
    public boolean zzma() {
        return this.zzTk;
    }
    
    public String zzmb() {
        return this.zzTl;
    }
    
    public static final class zza
    {
        private Account zzQd;
        private boolean zzTi;
        private boolean zzTj;
        private boolean zzTk;
        private String zzTl;
        private Set<Scope> zzTm;
        
        public zza() {
            this.zzTm = new HashSet<Scope>(Arrays.asList(GoogleSignInConfig.zzTg));
        }
        
        public GoogleSignInConfig zzmc() {
            return new GoogleSignInConfig(this.zzTm, this.zzQd, this.zzTi, this.zzTj, this.zzTk, this.zzTl, null);
        }
    }
}
