package com.sonyericsson.android.camera.view.overlaycontrol.semiauto;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.SystemClock;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.overlaycontrol.semiauto.SemiAutoSeekBarView;
import com.sonyericsson.cameracommon.utility.ResourceUtil;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import java.lang.ref.WeakReference;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SemiAutoControlView implements SemiAutoSeekBarView.OnSemiAutoSeekBarChangeListener, View.OnClickListener, View.OnTouchListener {
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
    private OnSemiAutoChangeListener mListener;
    private OpacityReductionTask mOpacityReductionTask;
    private ImageView mReset;
    private boolean mIsTracking = false;
    private boolean mIsExpanded = false;

    public interface OnSemiAutoChangeListener {
        void onAmberBlueColorChanged(int i);

        void onBrightnessChanged(int i);

        void onSemiAutoControlStarted();

        void onSemiAutoControlStopped();

        void onSemiAutoDisabled();

        void onSemiAutoEnabled();

        void onSemiAutoReset();
    }

    public SemiAutoControlView(ViewGroup viewGroup, LayoutDependencyResolver.ScreenAspect screenAspect) throws Resources.NotFoundException {
        init(viewGroup, screenAspect);
    }

    private void init(ViewGroup viewGroup, LayoutDependencyResolver.ScreenAspect screenAspect) throws Resources.NotFoundException {
        Context context = viewGroup.getContext();
        ViewStub viewStub = (ViewStub) viewGroup.findViewById(R.id.semi_auto_container_stub);
        if (viewStub != null) {
            Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize(context);
            int iMin = Math.min(viewFinderSize.width(), viewFinderSize.height());
            int iMax = Math.max(viewFinderSize.width(), viewFinderSize.height());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -1);
            layoutParams.gravity = 5;
            layoutParams.setMargins(0, 0, (iMax - ((iMin * 4) / 3)) - (screenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE ? ResourceUtil.getDimensionPixelSize(context, context.getPackageName(), R.dimen.left_icon_area_height) : 0), 0);
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.seek_bar_view_seek_area_max_height);
            if (iMin > dimensionPixelSize) {
                layoutParams.height = dimensionPixelSize;
                layoutParams.gravity |= 16;
            }
            this.mIndicatorView = viewStub.inflate();
            this.mIndicatorView.setLayoutParams(layoutParams);
        } else {
            this.mIndicatorView = viewGroup.findViewById(R.id.semi_auto_indicator_view);
        }
        this.mIndicatorView.setOnTouchListener(this);
        this.mOpacityReductionTask = new OpacityReductionTask(this.mIndicatorView);
        this.mBrightness = (SemiAutoSeekBarView) this.mIndicatorView.findViewById(2131296593);
        this.mColor = (SemiAutoSeekBarView) this.mIndicatorView.findViewById(2131296594);
        this.mReset = (ImageView) this.mIndicatorView.findViewById(2131296590);
        this.mExpand = (ImageView) this.mIndicatorView.findViewById(2131296588);
        this.mBrightness.hide();
        this.mColor.hide();
        this.mReset.setOnClickListener(this);
        this.mExpand.setOnClickListener(this);
        this.mBrightness.setSeekBarResource(R.drawable.semi_auto_brightness_slider_track_background);
        this.mColor.setSeekBarResource(R.drawable.semi_auto_color_slider_track_background);
        this.mBrightness.setTextForAccessibility(R.string.cam_strings_superior_auto_brightness_txt);
        this.mColor.setTextForAccessibility(R.string.cam_strings_superior_auto_color_txt);
        this.mBrightness.setMinimum(0);
        this.mBrightness.setMaximum(100);
        this.mColor.setMinimum(0);
        this.mColor.setMaximum(100);
        setDefaultPosition();
        this.mBrightness.setAscending(false);
        this.mColor.setAscending(false);
        this.mBrightness.setOnSemiAutoSeekBarChangeListener(this);
        this.mColor.setOnSemiAutoSeekBarChangeListener(this);
    }

    private void setDefaultPosition() {
        this.mBrightness.moveToCenterProgress();
        this.mColor.moveToCenterProgress();
    }

    public void release() {
        setOnSemiAutoChangeListener(null);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.semiauto.SemiAutoSeekBarView.OnSemiAutoSeekBarChangeListener
    public void onProgressChanged(SemiAutoSeekBarView semiAutoSeekBarView, int i, boolean z) {
        switch (semiAutoSeekBarView.getId()) {
            case 2131296593:
                if (this.mListener != null) {
                    this.mListener.onBrightnessChanged(i);
                    return;
                }
                return;
            case 2131296594:
                if (this.mListener != null) {
                    this.mListener.onAmberBlueColorChanged(i);
                    return;
                }
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.semiauto.SemiAutoSeekBarView.OnSemiAutoSeekBarChangeListener
    public void onStartTrackingTouch(SemiAutoSeekBarView semiAutoSeekBarView, int i) {
        this.mIsTracking = true;
        this.mOpacityReductionTask.stop(true);
        if (this.mListener != null) {
            this.mListener.onSemiAutoControlStarted();
        }
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.semiauto.SemiAutoSeekBarView.OnSemiAutoSeekBarChangeListener
    public void onStopTrackingTouch(SemiAutoSeekBarView semiAutoSeekBarView, int i) {
        this.mIsTracking = false;
        this.mOpacityReductionTask.start();
        if (this.mListener != null) {
            this.mListener.onSemiAutoControlStopped();
        }
    }

    public void setOnSemiAutoChangeListener(OnSemiAutoChangeListener onSemiAutoChangeListener) {
        this.mListener = onSemiAutoChangeListener;
    }

    public void setExpanded(boolean z) {
        this.mIsExpanded = z;
    }

    public void enable() {
        this.mIndicatorView.setAlpha(1.0f);
        show(true, this.mIsExpanded);
        this.mOpacityReductionTask.start();
        if (this.mListener != null) {
            this.mListener.onSemiAutoEnabled();
        }
    }

    public void disable() {
        hide();
        setDefaultPosition();
        this.mOpacityReductionTask.stop(false);
        if (this.mListener != null) {
            this.mListener.onSemiAutoDisabled();
        }
    }

    public void setVisibility(int i) {
        if (this.mIndicatorView != null) {
            this.mIndicatorView.setVisibility(i);
        }
    }

    private void hide() {
        this.mBrightness.hide();
        this.mColor.hide();
        this.mReset.setVisibility(4);
        this.mReset.setOnClickListener(null);
        this.mExpand.setVisibility(4);
        this.mExpand.setOnClickListener(null);
    }

    private void show(boolean z, boolean z2) {
        if (!this.mIndicatorView.isShown()) {
            this.mIndicatorView.setVisibility(0);
        }
        if (this.mIsTracking) {
            return;
        }
        this.mBrightness.show(z);
        if (z2) {
            this.mColor.show(z);
        } else if (this.mColor.getVisibility() != 0) {
            this.mExpand.setVisibility(0);
            this.mExpand.setOnClickListener(this);
        }
        if (this.mReset.getVisibility() != 0) {
            this.mReset.setVisibility(0);
            this.mReset.setOnClickListener(this);
        }
    }

    public void setOrientation(int i) {
        this.mReset.setRotation(RotationUtil.getAngle(i));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id != 2131296588) {
            if (id == 2131296590) {
                if (this.mListener != null) {
                    this.mListener.onSemiAutoReset();
                }
            } else {
                throw new IllegalArgumentException();
            }
        } else if (this.mColor != null && this.mColor.getVisibility() != 0) {
            this.mColor.show(true);
            this.mExpand.setVisibility(4);
            this.mIsExpanded = true;
        }
        this.mOpacityReductionTask.start();
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean z = this.mReset.getVisibility() == 0;
        if (z) {
            this.mIndicatorView.getParent().requestDisallowInterceptTouchEvent(true);
        }
        int action = motionEvent.getAction();
        if (action == 1 || action == 3) {
            this.mIndicatorView.getParent().requestDisallowInterceptTouchEvent(false);
        }
        return z;
    }

    private static final class OpacityReductionTask implements Choreographer.FrameCallback {
        private static final long DELAY = 3000;
        private static final float DELTA = -0.5f;
        private static final long DURATION = 300;
        private static final float SOURCE_ALPHA = 1.0f;
        private static final float TARGET_ALPHA = 0.5f;
        private final Choreographer mChoreographer = Choreographer.getInstance();
        private boolean mStarted;
        private long mStartedTime;
        private final WeakReference<View> mTargetView;

        OpacityReductionTask(View view) {
            this.mTargetView = new WeakReference<>(view);
        }

        @Override // android.view.Choreographer.FrameCallback
        public void doFrame(long j) {
            long j2;
            View view = this.mTargetView.get();
            if (!this.mStarted || view == null) {
                return;
            }
            long jUptimeMillis = SystemClock.uptimeMillis() - this.mStartedTime;
            if (jUptimeMillis <= 3000) {
                j2 = 0;
            } else {
                j2 = jUptimeMillis - 3000;
                if (j2 > DURATION) {
                    j2 = 300;
                }
            }
            view.setAlpha(SOURCE_ALPHA + ((DELTA * j2) / 300.0f));
            if (j2 < DURATION) {
                this.mChoreographer.postFrameCallback(this);
            }
        }

        public void start() {
            if (this.mStarted) {
                stop(true);
            }
            this.mStarted = true;
            this.mStartedTime = SystemClock.uptimeMillis();
            this.mChoreographer.postFrameCallbackDelayed(this, 3000L);
        }

        public void stop(boolean z) {
            View view;
            this.mStarted = false;
            this.mChoreographer.removeFrameCallback(this);
            if (!z || (view = this.mTargetView.get()) == null) {
                return;
            }
            view.setAlpha(SOURCE_ALPHA);
        }
    }
}
