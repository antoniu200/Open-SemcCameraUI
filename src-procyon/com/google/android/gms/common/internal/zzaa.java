// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.os.IBinder;
import com.google.android.gms.dynamic.zze;
import android.view.View;
import android.content.Context;
import com.google.android.gms.dynamic.zzg;

public final class zzaa extends zzg<zzu>
{
    private static final zzaa zzags;
    
    static {
        zzags = new zzaa();
    }
    
    private zzaa() {
        super("com.google.android.gms.common.ui.SignInButtonCreatorImpl");
    }
    
    public static View zzb(final Context context, final int n, final int n2) throws zza {
        return zzaa.zzags.zzc(context, n, n2);
    }
    
    private View zzc(final Context context, final int i, final int j) throws zza {
        try {
            return zze.zzp(this.zzas(context).zza(zze.zzy(context), i, j));
        }
        catch (final Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not get button with size ");
            sb.append(i);
            sb.append(" and color ");
            sb.append(j);
            throw new zza(sb.toString(), ex);
        }
    }
    
    public zzu zzaN(final IBinder binder) {
        return zzu.zza.zzaM(binder);
    }
}
