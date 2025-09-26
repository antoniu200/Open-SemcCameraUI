// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.widget;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.View$MeasureSpec;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.annotation.NonNull;
import android.content.Context;
import android.view.View;

@Deprecated
public class Space extends View
{
    @Deprecated
    public Space(@NonNull final Context context) {
        this(context, null);
    }
    
    @Deprecated
    public Space(@NonNull final Context context, @Nullable final AttributeSet set) {
        this(context, set, 0);
    }
    
    @Deprecated
    public Space(@NonNull final Context context, @Nullable final AttributeSet set, final int n) {
        super(context, set, n);
        if (this.getVisibility() == 0) {
            this.setVisibility(4);
        }
    }
    
    private static int getDefaultSize2(final int a, int min) {
        final int mode = View$MeasureSpec.getMode(min);
        final int size = View$MeasureSpec.getSize(min);
        if (mode != Integer.MIN_VALUE) {
            min = a;
            if (mode != 0) {
                if (mode != 1073741824) {
                    min = a;
                }
                else {
                    min = size;
                }
            }
        }
        else {
            min = Math.min(a, size);
        }
        return min;
    }
    
    @Deprecated
    @SuppressLint({ "MissingSuperCall" })
    public void draw(final Canvas canvas) {
    }
    
    @Deprecated
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(getDefaultSize2(this.getSuggestedMinimumWidth(), n), getDefaultSize2(this.getSuggestedMinimumHeight(), n2));
    }
}
