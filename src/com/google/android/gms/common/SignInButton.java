package com.google.android.gms.common;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import com.google.android.gms.common.internal.zzaa;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zzg;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class SignInButton extends FrameLayout implements View.OnClickListener {
    public static final int COLOR_DARK = 0;
    public static final int COLOR_LIGHT = 1;
    public static final int SIZE_ICON_ONLY = 2;
    public static final int SIZE_STANDARD = 0;
    public static final int SIZE_WIDE = 1;
    private int mColor;
    private int mSize;
    private View zzaat;
    private View.OnClickListener zzaau;

    public SignInButton(Context context) {
        this(context, null);
    }

    public SignInButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SignInButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.zzaau = null;
        setStyle(0, 0);
    }

    private static Button zza(Context context, int i, int i2) throws Resources.NotFoundException {
        zzab zzabVar = new zzab(context);
        zzabVar.zza(context.getResources(), i, i2);
        return zzabVar;
    }

    private void zzai(Context context) {
        if (this.zzaat != null) {
            removeView(this.zzaat);
        }
        try {
            this.zzaat = zzaa.zzb(context, this.mSize, this.mColor);
        } catch (zzg.zza unused) {
            Log.w("SignInButton", "Sign in button not found, using placeholder instead");
            this.zzaat = zza(context, this.mSize, this.mColor);
        }
        addView(this.zzaat);
        this.zzaat.setEnabled(isEnabled());
        this.zzaat.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.zzaau == null || view != this.zzaat) {
            return;
        }
        this.zzaau.onClick(this);
    }

    public void setColorScheme(int i) {
        setStyle(this.mSize, i);
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.zzaat.setEnabled(z);
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.zzaau = onClickListener;
        if (this.zzaat != null) {
            this.zzaat.setOnClickListener(this);
        }
    }

    public void setSize(int i) {
        setStyle(i, this.mColor);
    }

    public void setStyle(int i, int i2) {
        zzx.zza(i >= 0 && i < 3, "Unknown button size %d", Integer.valueOf(i));
        zzx.zza(i2 >= 0 && i2 < 2, "Unknown color scheme %s", Integer.valueOf(i2));
        this.mSize = i;
        this.mColor = i2;
        zzai(getContext());
    }
}
