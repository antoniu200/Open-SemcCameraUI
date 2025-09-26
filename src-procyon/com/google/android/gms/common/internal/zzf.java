// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import android.content.Context;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Collections;
import com.google.android.gms.common.api.Api;
import java.util.Map;
import com.google.android.gms.internal.zzqx;
import android.view.View;
import com.google.android.gms.common.api.Scope;
import java.util.Set;
import android.accounts.Account;

public final class zzf
{
    private final Account zzQd;
    private final String zzRq;
    private final Set<Scope> zzaaF;
    private final int zzaaG;
    private final View zzaaH;
    private final String zzaaI;
    private final zzqx zzaaT;
    private final Set<Scope> zzafh;
    private final Map<Api<?>, zza> zzafi;
    private Integer zzafj;
    
    public zzf(final Account zzQd, final Set<Scope> s, final Map<Api<?>, zza> map, final int zzaaG, final View zzaaH, final String zzRq, final String zzaaI, final zzqx zzaaT) {
        this.zzQd = zzQd;
        Set<Object> zzaaF;
        if (s == null) {
            zzaaF = Collections.EMPTY_SET;
        }
        else {
            zzaaF = (Set<Object>)Collections.unmodifiableSet((Set<? extends Scope>)s);
        }
        this.zzaaF = (Set<Scope>)zzaaF;
        Map<Api<?>, zza> empty_MAP = map;
        if (map == null) {
            empty_MAP = Collections.EMPTY_MAP;
        }
        this.zzafi = empty_MAP;
        this.zzaaH = zzaaH;
        this.zzaaG = zzaaG;
        this.zzRq = zzRq;
        this.zzaaI = zzaaI;
        this.zzaaT = zzaaT;
        final HashSet s2 = new HashSet(this.zzaaF);
        final Iterator<zza> iterator = this.zzafi.values().iterator();
        while (iterator.hasNext()) {
            s2.addAll(iterator.next().zzTm);
        }
        this.zzafh = (Set<Scope>)Collections.unmodifiableSet((Set<?>)s2);
    }
    
    public static zzf zzak(final Context context) {
        return new GoogleApiClient.Builder(context).zznB();
    }
    
    public Account getAccount() {
        return this.zzQd;
    }
    
    @Deprecated
    public String getAccountName() {
        if (this.zzQd != null) {
            return this.zzQd.name;
        }
        return null;
    }
    
    public void zza(final Integer zzafj) {
        this.zzafj = zzafj;
    }
    
    public Set<Scope> zzb(final Api<?> api) {
        final zza zza = this.zzafi.get(api);
        if (zza != null && !zza.zzTm.isEmpty()) {
            final HashSet set = new HashSet((Collection<? extends E>)this.zzaaF);
            set.addAll(zza.zzTm);
            return set;
        }
        return this.zzaaF;
    }
    
    public Account zzoI() {
        if (this.zzQd != null) {
            return this.zzQd;
        }
        return new Account("<<default account>>", "com.google");
    }
    
    public int zzoJ() {
        return this.zzaaG;
    }
    
    public Set<Scope> zzoK() {
        return this.zzaaF;
    }
    
    public Set<Scope> zzoL() {
        return this.zzafh;
    }
    
    public Map<Api<?>, zza> zzoM() {
        return this.zzafi;
    }
    
    public String zzoN() {
        return this.zzRq;
    }
    
    public String zzoO() {
        return this.zzaaI;
    }
    
    public View zzoP() {
        return this.zzaaH;
    }
    
    public zzqx zzoQ() {
        return this.zzaaT;
    }
    
    public Integer zzoR() {
        return this.zzafj;
    }
    
    public static final class zza
    {
        public final Set<Scope> zzTm;
        public final boolean zzafk;
        
        public zza(final Set<Scope> s, final boolean zzafk) {
            zzx.zzw(s);
            this.zzTm = Collections.unmodifiableSet((Set<? extends Scope>)s);
            this.zzafk = zzafk;
        }
    }
}
