// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.graphics.Path;
import android.graphics.Canvas;
import android.net.Uri;
import android.widget.ImageView;

public final class zzlu extends ImageView
{
    private int zzaeA;
    private zza zzaeB;
    private int zzaeC;
    private float zzaeD;
    private Uri zzaey;
    private int zzaez;
    
    protected void onDraw(final Canvas canvas) {
        if (this.zzaeB != null) {
            canvas.clipPath(this.zzaeB.zzk(this.getWidth(), this.getHeight()));
        }
        super.onDraw(canvas);
        if (this.zzaeA != 0) {
            canvas.drawColor(this.zzaeA);
        }
    }
    
    protected void onMeasure(int measuredWidth, int measuredHeight) {
        super.onMeasure(measuredWidth, measuredHeight);
        switch (this.zzaeC) {
            default: {
                return;
            }
            case 2: {
                measuredWidth = this.getMeasuredWidth();
                measuredHeight = (int)(measuredWidth / this.zzaeD);
                break;
            }
            case 1: {
                measuredHeight = this.getMeasuredHeight();
                measuredWidth = (int)(measuredHeight * this.zzaeD);
                break;
            }
        }
        this.setMeasuredDimension(measuredWidth, measuredHeight);
    }
    
    public void zzbA(final int zzaez) {
        this.zzaez = zzaez;
    }
    
    public void zzj(final Uri zzaey) {
        this.zzaey = zzaey;
    }
    
    public int zzoH() {
        return this.zzaez;
    }
    
    public interface zza
    {
        Path zzk(final int p0, final int p1);
    }
}
