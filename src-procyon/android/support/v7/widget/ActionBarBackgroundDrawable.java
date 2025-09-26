// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.graphics.ColorFilter;
import android.support.annotation.RequiresApi;
import android.support.annotation.NonNull;
import android.graphics.Outline;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

class ActionBarBackgroundDrawable extends Drawable
{
    final ActionBarContainer mContainer;
    
    public ActionBarBackgroundDrawable(final ActionBarContainer mContainer) {
        this.mContainer = mContainer;
    }
    
    public void draw(final Canvas canvas) {
        if (this.mContainer.mIsSplit) {
            if (this.mContainer.mSplitBackground != null) {
                this.mContainer.mSplitBackground.draw(canvas);
            }
        }
        else {
            if (this.mContainer.mBackground != null) {
                this.mContainer.mBackground.draw(canvas);
            }
            if (this.mContainer.mStackedBackground != null && this.mContainer.mIsStacked) {
                this.mContainer.mStackedBackground.draw(canvas);
            }
        }
    }
    
    public int getOpacity() {
        return 0;
    }
    
    @RequiresApi(21)
    public void getOutline(@NonNull final Outline outline) {
        if (this.mContainer.mIsSplit) {
            if (this.mContainer.mSplitBackground != null) {
                this.mContainer.mSplitBackground.getOutline(outline);
            }
        }
        else if (this.mContainer.mBackground != null) {
            this.mContainer.mBackground.getOutline(outline);
        }
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
}
