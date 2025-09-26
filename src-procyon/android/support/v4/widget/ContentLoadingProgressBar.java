// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.widget;

import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.annotation.NonNull;
import android.content.Context;
import android.widget.ProgressBar;

public class ContentLoadingProgressBar extends ProgressBar
{
    private static final int MIN_DELAY = 500;
    private static final int MIN_SHOW_TIME = 500;
    private final Runnable mDelayedHide;
    private final Runnable mDelayedShow;
    boolean mDismissed;
    boolean mPostedHide;
    boolean mPostedShow;
    long mStartTime;
    
    public ContentLoadingProgressBar(@NonNull final Context context) {
        this(context, null);
    }
    
    public ContentLoadingProgressBar(@NonNull final Context context, @Nullable final AttributeSet set) {
        super(context, set, 0);
        this.mStartTime = -1L;
        this.mPostedHide = false;
        this.mPostedShow = false;
        this.mDismissed = false;
        this.mDelayedHide = new Runnable() {
            final ContentLoadingProgressBar this$0;
            
            @Override
            public void run() {
                this.this$0.mPostedHide = false;
                this.this$0.mStartTime = -1L;
                this.this$0.setVisibility(8);
            }
        };
        this.mDelayedShow = new Runnable() {
            final ContentLoadingProgressBar this$0;
            
            @Override
            public void run() {
                this.this$0.mPostedShow = false;
                if (!this.this$0.mDismissed) {
                    this.this$0.mStartTime = System.currentTimeMillis();
                    this.this$0.setVisibility(0);
                }
            }
        };
    }
    
    private void removeCallbacks() {
        this.removeCallbacks(this.mDelayedHide);
        this.removeCallbacks(this.mDelayedShow);
    }
    
    public void hide() {
        synchronized (this) {
            this.mDismissed = true;
            this.removeCallbacks(this.mDelayedShow);
            this.mPostedShow = false;
            final long n = System.currentTimeMillis() - this.mStartTime;
            if (n < 500L && this.mStartTime != -1L) {
                if (!this.mPostedHide) {
                    this.postDelayed(this.mDelayedHide, 500L - n);
                    this.mPostedHide = true;
                }
            }
            else {
                this.setVisibility(8);
            }
        }
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.removeCallbacks();
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeCallbacks();
    }
    
    public void show() {
        synchronized (this) {
            this.mStartTime = -1L;
            this.mDismissed = false;
            this.removeCallbacks(this.mDelayedHide);
            this.mPostedHide = false;
            if (!this.mPostedShow) {
                this.postDelayed(this.mDelayedShow, 500L);
                this.mPostedShow = true;
            }
        }
    }
}
