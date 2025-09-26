// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout.zoombar;

import com.sonymobile.cameracommon.research.ResearchUtil;
import java.util.Locale;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.os.Handler;
import android.view.ViewPropertyAnimator;
import android.animation.Animator;
import android.util.AttributeSet;
import android.content.Context;
import java.util.List;
import android.widget.TextView;
import android.widget.ImageView;
import android.animation.Animator$AnimatorListener;
import android.widget.FrameLayout;

public class Zoombar extends FrameLayout
{
    private static final boolean DEBUG = false;
    public static final int DELAY_ZOOMBAR_HIDE = 1000;
    private static final long IMMEDIATE_ANIMATION_DURATION_IN_MILLIS = 0L;
    private static final float INVISIBLE_ALPHA = 0.0f;
    private static final long INVISIBLE_ANIMATION_DURATION_IN_MILLIS = 100L;
    public static final int MIN_VALUE = 0;
    public static final String TAG = "Zoombar";
    private static final float VISIBLE_ALPHA = 1.0f;
    private static final long VISIBLE_ANIMATION_DURATION_IN_MILLIS = 100L;
    private Animator$AnimatorListener mHideAnimationlistener;
    private final Runnable mHideEvent;
    private ImageView mLeftIndicator;
    private ImageView mRightIndicator;
    private TextView mValueIndicator;
    private List<Integer> mZoomRatios;
    private ZoombarDisplayChangedListener mZoombarDisplayChangedListener;
    
    public Zoombar(final Context context) {
        this(context, null);
    }
    
    public Zoombar(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public Zoombar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mZoombarDisplayChangedListener = null;
        this.mHideAnimationlistener = (Animator$AnimatorListener)new Animator$AnimatorListener() {
            final Zoombar this$0;
            
            public void onAnimationCancel(final Animator animator) {
                if (this.this$0.mZoombarDisplayChangedListener != null) {
                    this.this$0.mZoombarDisplayChangedListener.onZoombarHidden();
                }
            }
            
            public void onAnimationEnd(final Animator animator) {
                if (this.this$0.mZoombarDisplayChangedListener != null) {
                    this.this$0.mZoombarDisplayChangedListener.onZoombarHidden();
                }
            }
            
            public void onAnimationRepeat(final Animator animator) {
            }
            
            public void onAnimationStart(final Animator animator) {
            }
        };
        this.mHideEvent = new Runnable() {
            final Zoombar this$0;
            
            @Override
            public void run() {
                this.this$0.hideWithAnimation(true);
            }
        };
    }
    
    private void hideWithAnimation(final boolean b) {
        final ViewPropertyAnimator alpha = this.animate().alpha(0.0f);
        long duration;
        if (b) {
            duration = 100L;
        }
        else {
            duration = 0L;
        }
        alpha.setDuration(duration).setListener(this.mHideAnimationlistener).start();
    }
    
    private void showWithAnimation(final boolean b) {
        if (this.mZoombarDisplayChangedListener != null) {
            this.mZoombarDisplayChangedListener.onShowZoombar();
        }
        final ViewPropertyAnimator alpha = this.animate().setListener((Animator$AnimatorListener)null).alpha(1.0f);
        long duration;
        if (b) {
            duration = 100L;
        }
        else {
            duration = 0L;
        }
        alpha.setDuration(duration).start();
    }
    
    private boolean validateZoomParameters(final int n) {
        if (this.mZoomRatios == null) {
            return false;
        }
        final Integer n2 = this.mZoomRatios.get(n);
        return n2 != null && n2 >= 0;
    }
    
    public List<Integer> getZoomRatios() {
        return this.mZoomRatios;
    }
    
    public void hideDelayed() {
        final Handler handler = this.getHandler();
        if (handler != null) {
            handler.postDelayed(this.mHideEvent, 1000L);
        }
    }
    
    public void hideImmediately() {
        final Handler handler = this.getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.mHideEvent);
        }
        this.hideWithAnimation(false);
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mLeftIndicator = (ImageView)this.findViewById(2131296444);
        this.mRightIndicator = (ImageView)this.findViewById(2131296536);
        this.mValueIndicator = (TextView)this.findViewById(2131296692);
    }
    
    public void setSensorOrientation(final int n) {
        this.mValueIndicator.setRotation(RotationUtil.getAngle(n));
    }
    
    public void setZoomRatios(final List<Integer> mZoomRatios) {
        this.mZoomRatios = mZoomRatios;
    }
    
    public void setZoombarDisplayChangedListener(final ZoombarDisplayChangedListener mZoombarDisplayChangedListener) {
        this.mZoombarDisplayChangedListener = mZoombarDisplayChangedListener;
    }
    
    public void show() {
        final Handler handler = this.getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.mHideEvent);
        }
        this.showWithAnimation(true);
    }
    
    public void showImmediately() {
        final Handler handler = this.getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.mHideEvent);
        }
        this.showWithAnimation(false);
    }
    
    public int zoom(int i) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("zoom() current:");
            sb.append(i);
            sb.append(" maxZoom:");
            sb.append(120);
            sb.append(" zoomRatios:");
            sb.append(this.mZoomRatios);
            CamLog.d(sb.toString());
        }
        if (!this.validateZoomParameters(i)) {
            this.hideImmediately();
            return i;
        }
        int n;
        if ((n = i) < 0) {
            n = 0;
        }
        if ((i = n) > 120) {
            i = 120;
        }
        final Integer n2 = this.mZoomRatios.get(i);
        final int n3 = (int)this.getResources().getDimension(2131165435);
        final int intrinsicWidth = this.mLeftIndicator.getDrawable().getIntrinsicWidth();
        final int j = (n3 - intrinsicWidth) * (120 - i) / 120 + intrinsicWidth;
        final String format = String.format(Locale.getDefault(), "%.1f", n2 / 100.0f);
        this.mLeftIndicator.getLayoutParams().width = j;
        this.mRightIndicator.getLayoutParams().width = j;
        this.mValueIndicator.setText((CharSequence)format);
        this.mLeftIndicator.requestLayout();
        this.mRightIndicator.requestLayout();
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("zoom() position:");
            sb2.append(j);
            sb2.append(" srPosition:");
            sb2.append(0);
            CamLog.d(sb2.toString());
        }
        ResearchUtil.getInstance().setZoomRatio(Float.parseFloat(String.format(Locale.US, "%.1f", n2 / 100.0f)));
        return i;
    }
    
    public interface ZoombarDisplayChangedListener
    {
        void onShowZoombar();
        
        void onZoombarHidden();
    }
}
