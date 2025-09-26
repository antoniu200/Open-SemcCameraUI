// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import com.google.android.gms.R;
import android.graphics.Typeface;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.Button;

public final class zzab extends Button
{
    public zzab(final Context context) {
        this(context, null);
    }
    
    public zzab(final Context context, final AttributeSet set) {
        super(context, set, 16842824);
    }
    
    private void zza(final Resources resources) {
        this.setTypeface(Typeface.DEFAULT_BOLD);
        this.setTextSize(14.0f);
        final int n = (int)(resources.getDisplayMetrics().density * 48.0f + 0.5f);
        this.setMinHeight(n);
        this.setMinWidth(n);
    }
    
    private void zzb(final Resources resources, int i, final int n) {
        int n2 = 0;
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown button size: ");
                sb.append(i);
                throw new IllegalStateException(sb.toString());
            }
            case 2: {
                i = R.drawable.common_signin_btn_icon_dark;
                n2 = R.drawable.common_signin_btn_icon_light;
                break;
            }
            case 0:
            case 1: {
                i = R.drawable.common_signin_btn_text_dark;
                n2 = R.drawable.common_signin_btn_text_light;
                break;
            }
        }
        i = this.zzd(n, i, n2);
        if (i == -1) {
            throw new IllegalStateException("Could not find background resource!");
        }
        this.setBackgroundDrawable(resources.getDrawable(i));
    }
    
    private void zzc(final Resources resources, int i, final int n) {
        this.setTextColor(resources.getColorStateList(this.zzd(n, R.color.common_signin_btn_text_dark, R.color.common_signin_btn_text_light)));
        CharSequence string = null;
        Label_0103: {
            switch (i) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown button size: ");
                    sb.append(i);
                    throw new IllegalStateException(sb.toString());
                }
                case 2: {
                    string = null;
                    break Label_0103;
                }
                case 1: {
                    i = R.string.common_signin_button_text_long;
                    break;
                }
                case 0: {
                    i = R.string.common_signin_button_text;
                    break;
                }
            }
            string = resources.getString(i);
        }
        this.setText(string);
    }
    
    private int zzd(final int i, final int n, final int n2) {
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown color scheme: ");
                sb.append(i);
                throw new IllegalStateException(sb.toString());
            }
            case 1: {
                return n2;
            }
            case 0: {
                return n;
            }
        }
    }
    
    public void zza(final Resources resources, final int i, final int j) {
        zzx.zza(i >= 0 && i < 3, "Unknown button size %d", i);
        zzx.zza(j >= 0 && j < 2, "Unknown color scheme %s", j);
        this.zza(resources);
        this.zzb(resources, i, j);
        this.zzc(resources, i, j);
    }
}
