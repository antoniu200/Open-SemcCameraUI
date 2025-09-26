// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.semiauto;

import android.os.SystemClock;
import java.lang.ref.WeakReference;
import android.view.Choreographer;
import android.view.Choreographer$FrameCallback;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.view.MotionEvent;
import android.graphics.Rect;
import android.content.Context;
import android.view.ViewGroup$LayoutParams;
import com.sonyericsson.cameracommon.utility.ResourceUtil;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewStub;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.view.View$OnTouchListener;
import android.view.View$OnClickListener;

public class SemiAutoControlView implements OnSemiAutoSeekBarChangeListener, View$OnClickListener, View$OnTouchListener
{
    private static final int SEEK_BAR_BRIGHTNESS = 2131296593;
    private static final int SEEK_BAR_COLORING = 2131296594;
    private static final int SEEK_BAR_MAXIMUM_VALUE = 100;
    private static final int SEEK_BAR_MINIMUM_VALUE = 0;
    private static final int SEMI_AUTO_EXPAND_BUTTON = 2131296588;
    private static final int SEMI_AUTO_RESET_BUTTON = 2131296590;
    public static final String TAG = "SemiAutoControlView";
    private SemiAutoSeekBarView mBrightness;
    private SemiAutoSeekBarView mColor;
    private ImageView mExpand;
    private View mIndicatorView;
    private boolean mIsExpanded;
    private boolean mIsTracking;
    private OnSemiAutoChangeListener mListener;
    private OpacityReductionTask mOpacityReductionTask;
    private ImageView mReset;
    
    public SemiAutoControlView(final ViewGroup viewGroup, final LayoutDependencyResolver.ScreenAspect screenAspect) {
        this.mIsTracking = false;
        this.mIsExpanded = false;
        this.init(viewGroup, screenAspect);
    }
    
    private void hide() {
        this.mBrightness.hide();
        this.mColor.hide();
        this.mReset.setVisibility(4);
        this.mReset.setOnClickListener((View$OnClickListener)null);
        this.mExpand.setVisibility(4);
        this.mExpand.setOnClickListener((View$OnClickListener)null);
    }
    
    private void init(final ViewGroup viewGroup, final LayoutDependencyResolver.ScreenAspect screenAspect) {
        final Context context = viewGroup.getContext();
        final ViewStub viewStub = (ViewStub)viewGroup.findViewById(2131296587);
        if (viewStub != null) {
            final Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize(context);
            final int min = Math.min(viewFinderSize.width(), viewFinderSize.height());
            final int max = Math.max(viewFinderSize.width(), viewFinderSize.height());
            final FrameLayout$LayoutParams layoutParams = new FrameLayout$LayoutParams(-2, -1);
            layoutParams.gravity = 5;
            int dimensionPixelSize;
            if (screenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
                dimensionPixelSize = ResourceUtil.getDimensionPixelSize(context, context.getPackageName(), 2131165428);
            }
            else {
                dimensionPixelSize = 0;
            }
            layoutParams.setMargins(0, 0, max - min * 4 / 3 - dimensionPixelSize, 0);
            final int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(2131165568);
            if (min > dimensionPixelSize2) {
                layoutParams.height = dimensionPixelSize2;
                layoutParams.gravity |= 0x10;
            }
            (this.mIndicatorView = viewStub.inflate()).setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
        else {
            this.mIndicatorView = viewGroup.findViewById(2131296589);
        }
        this.mIndicatorView.setOnTouchListener((View$OnTouchListener)this);
        this.mOpacityReductionTask = new OpacityReductionTask(this.mIndicatorView);
        this.mBrightness = (SemiAutoSeekBarView)this.mIndicatorView.findViewById(2131296593);
        this.mColor = (SemiAutoSeekBarView)this.mIndicatorView.findViewById(2131296594);
        this.mReset = (ImageView)this.mIndicatorView.findViewById(2131296590);
        this.mExpand = (ImageView)this.mIndicatorView.findViewById(2131296588);
        this.mBrightness.hide();
        this.mColor.hide();
        this.mReset.setOnClickListener((View$OnClickListener)this);
        this.mExpand.setOnClickListener((View$OnClickListener)this);
        this.mBrightness.setSeekBarResource(2131231536);
        this.mColor.setSeekBarResource(2131231538);
        this.mBrightness.setTextForAccessibility(2131690173);
        this.mColor.setTextForAccessibility(2131690174);
        this.mBrightness.setMinimum(0);
        this.mBrightness.setMaximum(100);
        this.mColor.setMinimum(0);
        this.mColor.setMaximum(100);
        this.setDefaultPosition();
        this.mBrightness.setAscending(false);
        this.mColor.setAscending(false);
        this.mBrightness.setOnSemiAutoSeekBarChangeListener((SemiAutoSeekBarView.OnSemiAutoSeekBarChangeListener)this);
        this.mColor.setOnSemiAutoSeekBarChangeListener((SemiAutoSeekBarView.OnSemiAutoSeekBarChangeListener)this);
    }
    
    private void setDefaultPosition() {
        this.mBrightness.moveToCenterProgress();
        this.mColor.moveToCenterProgress();
    }
    
    private void show(final boolean b, final boolean b2) {
        if (!this.mIndicatorView.isShown()) {
            this.mIndicatorView.setVisibility(0);
        }
        if (!this.mIsTracking) {
            this.mBrightness.show(b);
            if (b2) {
                this.mColor.show(b);
            }
            else if (this.mColor.getVisibility() != 0) {
                this.mExpand.setVisibility(0);
                this.mExpand.setOnClickListener((View$OnClickListener)this);
            }
            if (this.mReset.getVisibility() != 0) {
                this.mReset.setVisibility(0);
                this.mReset.setOnClickListener((View$OnClickListener)this);
            }
        }
    }
    
    public void disable() {
        this.hide();
        this.setDefaultPosition();
        this.mOpacityReductionTask.stop(false);
        if (this.mListener != null) {
            this.mListener.onSemiAutoDisabled();
        }
    }
    
    public void enable() {
        this.mIndicatorView.setAlpha(1.0f);
        this.show(true, this.mIsExpanded);
        this.mOpacityReductionTask.start();
        if (this.mListener != null) {
            this.mListener.onSemiAutoEnabled();
        }
    }
    
    public void onClick(final View view) {
        final int id = view.getId();
        if (id != 2131296588) {
            if (id != 2131296590) {
                throw new IllegalArgumentException();
            }
            if (this.mListener != null) {
                this.mListener.onSemiAutoReset();
            }
        }
        else if (this.mColor != null && this.mColor.getVisibility() != 0) {
            this.mColor.show(true);
            this.mExpand.setVisibility(4);
            this.mIsExpanded = true;
        }
        this.mOpacityReductionTask.start();
    }
    
    @Override
    public void onProgressChanged(final SemiAutoSeekBarView semiAutoSeekBarView, final int n, final boolean b) {
        switch (semiAutoSeekBarView.getId()) {
            default: {
                throw new IllegalArgumentException();
            }
            case 2131296594: {
                if (this.mListener != null) {
                    this.mListener.onAmberBlueColorChanged(n);
                    break;
                }
                break;
            }
            case 2131296593: {
                if (this.mListener != null) {
                    this.mListener.onBrightnessChanged(n);
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void onStartTrackingTouch(final SemiAutoSeekBarView semiAutoSeekBarView, final int n) {
        this.mIsTracking = true;
        this.mOpacityReductionTask.stop(true);
        if (this.mListener != null) {
            this.mListener.onSemiAutoControlStarted();
        }
    }
    
    @Override
    public void onStopTrackingTouch(final SemiAutoSeekBarView semiAutoSeekBarView, final int n) {
        this.mIsTracking = false;
        this.mOpacityReductionTask.start();
        if (this.mListener != null) {
            this.mListener.onSemiAutoControlStopped();
        }
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final boolean b = this.mReset.getVisibility() == 0;
        if (b) {
            this.mIndicatorView.getParent().requestDisallowInterceptTouchEvent(true);
        }
        final int action = motionEvent.getAction();
        if (action == 1 || action == 3) {
            this.mIndicatorView.getParent().requestDisallowInterceptTouchEvent(false);
        }
        return b;
    }
    
    public void release() {
        this.setOnSemiAutoChangeListener(null);
    }
    
    public void setExpanded(final boolean mIsExpanded) {
        this.mIsExpanded = mIsExpanded;
    }
    
    public void setOnSemiAutoChangeListener(final OnSemiAutoChangeListener mListener) {
        this.mListener = mListener;
    }
    
    public void setOrientation(final int n) {
        this.mReset.setRotation(RotationUtil.getAngle(n));
    }
    
    public void setVisibility(final int visibility) {
        if (this.mIndicatorView != null) {
            this.mIndicatorView.setVisibility(visibility);
        }
    }
    
    public interface OnSemiAutoChangeListener
    {
        void onAmberBlueColorChanged(final int p0);
        
        void onBrightnessChanged(final int p0);
        
        void onSemiAutoControlStarted();
        
        void onSemiAutoControlStopped();
        
        void onSemiAutoDisabled();
        
        void onSemiAutoEnabled();
        
        void onSemiAutoReset();
    }
    
    private static final class OpacityReductionTask implements Choreographer$FrameCallback
    {
        private static final long DELAY = 3000L;
        private static final float DELTA = -0.5f;
        private static final long DURATION = 300L;
        private static final float SOURCE_ALPHA = 1.0f;
        private static final float TARGET_ALPHA = 0.5f;
        private final Choreographer mChoreographer;
        private boolean mStarted;
        private long mStartedTime;
        private final WeakReference<View> mTargetView;
        
        OpacityReductionTask(final View referent) {
            this.mTargetView = new WeakReference<View>(referent);
            this.mChoreographer = Choreographer.getInstance();
        }
        
        public void doFrame(long n) {
            final View view = this.mTargetView.get();
            if (this.mStarted && view != null) {
                n = SystemClock.uptimeMillis() - this.mStartedTime;
                if (n <= 3000L) {
                    n = 0L;
                }
                else if ((n -= 3000L) > 300L) {
                    n = 300L;
                }
                view.setAlpha(1.0f + -0.5f * n / 300.0f);
                if (n < 300L) {
                    this.mChoreographer.postFrameCallback((Choreographer$FrameCallback)this);
                }
            }
        }
        
        public void start() {
            if (this.mStarted) {
                this.stop(true);
            }
            this.mStarted = true;
            this.mStartedTime = SystemClock.uptimeMillis();
            this.mChoreographer.postFrameCallbackDelayed((Choreographer$FrameCallback)this, 3000L);
        }
        
        public void stop(final boolean b) {
            this.mStarted = false;
            this.mChoreographer.removeFrameCallback((Choreographer$FrameCallback)this);
            if (b) {
                final View view = this.mTargetView.get();
                if (view != null) {
                    view.setAlpha(1.0f);
                }
            }
        }
    }
}
