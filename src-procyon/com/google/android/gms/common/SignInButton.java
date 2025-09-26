// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zzg;
import android.util.Log;
import com.google.android.gms.common.internal.zzaa;
import com.google.android.gms.common.internal.zzab;
import android.widget.Button;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.View$OnClickListener;
import android.widget.FrameLayout;

public final class SignInButton extends FrameLayout implements View$OnClickListener
{
    public static final int COLOR_DARK = 0;
    public static final int COLOR_LIGHT = 1;
    public static final int SIZE_ICON_ONLY = 2;
    public static final int SIZE_STANDARD = 0;
    public static final int SIZE_WIDE = 1;
    private int mColor;
    private int mSize;
    private View zzaat;
    private View$OnClickListener zzaau;
    
    public SignInButton(final Context context) {
        this(context, null);
    }
    
    public SignInButton(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public SignInButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.zzaau = null;
        this.setStyle(0, 0);
    }
    
    private static Button zza(final Context context, final int n, final int n2) {
        final zzab zzab = new zzab(context);
        zzab.zza(context.getResources(), n, n2);
        return zzab;
    }
    
    private void zzai(final Context context) {
        if (this.zzaat != null) {
            this.removeView(this.zzaat);
        }
        try {
            this.zzaat = zzaa.zzb(context, this.mSize, this.mColor);
        }
        catch (final zzg.zza zza) {
            Log.w("SignInButton", "Sign in button not found, using placeholder instead");
            this.zzaat = (View)zza(context, this.mSize, this.mColor);
        }
        this.addView(this.zzaat);
        this.zzaat.setEnabled(this.isEnabled());
        this.zzaat.setOnClickListener((View$OnClickListener)this);
    }
    
    public void onClick(final View view) {
        if (this.zzaau != null && view == this.zzaat) {
            this.zzaau.onClick((View)this);
        }
    }
    
    public void setColorScheme(final int n) {
        this.setStyle(this.mSize, n);
    }
    
    public void setEnabled(final boolean b) {
        super.setEnabled(b);
        this.zzaat.setEnabled(b);
    }
    
    public void setOnClickListener(final View$OnClickListener zzaau) {
        this.zzaau = zzaau;
        if (this.zzaat != null) {
            this.zzaat.setOnClickListener((View$OnClickListener)this);
        }
    }
    
    public void setSize(final int n) {
        this.setStyle(n, this.mColor);
    }
    
    public void setStyle(final int n, final int n2) {
        zzx.zza(n >= 0 && n < 3, "Unknown button size %d", n);
        zzx.zza(n2 >= 0 && n2 < 2, "Unknown color scheme %s", n2);
        this.mSize = n;
        this.mColor = n2;
        this.zzai(this.getContext());
    }
}
