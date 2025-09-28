package com.sonyericsson.android.camera.view.baselayout.onscreenbutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonGroup;
import com.sonyericsson.cameracommon.utility.RotationUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class OnScreenButton extends FrameLayout implements OnScreenButtonGroup.OnItemUpdatedListener {
    private static final int DISABLED_FILTER = 2131099706;
    public static final OnScreenButtonListener EMPTY_LISTENER = new OnScreenButtonListener() { // from class: com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButton.1
        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onCancel(OnScreenButton onScreenButton, MotionEvent motionEvent) {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onDown(OnScreenButton onScreenButton, MotionEvent motionEvent) {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onLongPress(OnScreenButton onScreenButton) {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onMove(OnScreenButton onScreenButton, MotionEvent motionEvent) {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onUp(OnScreenButton onScreenButton, MotionEvent motionEvent) {
        }
    };
    public static final Resource EMPTY_RESOURCE = new Resource(-1, -1, -1, -1, null);
    private static final String TAG = "OnScreenButton";
    private final ImageView mIcon;
    private boolean mIsCanceled;
    private boolean mIsRotatable;
    private boolean mIsTouched;
    private OnScreenButtonGroup.Item mItem;
    private OnScreenButtonListener mListener;
    private int mOrientation;
    private Resource mResource;
    private int mStaticOrientation;

    public static class Resource {
        public static final int NONE = -1;
        final int mBackground;
        final int mDescription;
        final int mIcon;
        final int mIconPortrait;
        final String mText;

        public Resource(int i, int i2, int i3, int i4, String str) {
            this.mIcon = i;
            this.mIconPortrait = i2;
            this.mBackground = i3;
            this.mDescription = i4;
            this.mText = str;
        }

        boolean shouldRotateByView() {
            return this.mIconPortrait == -1;
        }

        int getIconResource(int i) {
            if (i == 2) {
                return this.mIcon;
            }
            if (shouldRotateByView()) {
                return this.mIcon;
            }
            return this.mIconPortrait;
        }

        int getBackgroundResource() {
            return this.mBackground;
        }

        String getDescription(Context context) {
            if (this.mDescription == -1) {
                return this.mText != null ? this.mText : "";
            }
            return context.getResources().getString(this.mDescription);
        }
    }

    private class LongClickListener implements View.OnLongClickListener {
        private LongClickListener() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            if (CamLog.VERBOSE) {
                CamLog.d("onLongClick()");
            }
            if (OnScreenButton.this.mListener == OnScreenButton.EMPTY_LISTENER) {
                return false;
            }
            OnScreenButton.this.mListener.onLongPress(OnScreenButton.this);
            return false;
        }
    }

    public OnScreenButton(Context context) {
        super(context);
        this.mStaticOrientation = 0;
        this.mIsRotatable = true;
        this.mIcon = new ImageView(context);
        init();
    }

    public OnScreenButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mStaticOrientation = 0;
        this.mIsRotatable = true;
        this.mIcon = new ImageView(context);
        init();
    }

    private void init() {
        this.mOrientation = 2;
        this.mResource = EMPTY_RESOURCE;
        this.mListener = EMPTY_LISTENER;
        this.mItem = null;
        this.mIsTouched = false;
        this.mIsCanceled = false;
        setFocusable(false);
        setFocusableInTouchMode(false);
        setOnClickListener(null);
        setClickable(false);
        setOnLongClickListener(new LongClickListener());
        setSoundEffectsEnabled(false);
        addView(this.mIcon);
        this.mIcon.setScaleType(ImageView.ScaleType.CENTER);
        this.mIcon.getLayoutParams().width = -2;
        this.mIcon.getLayoutParams().height = -2;
        ((FrameLayout.LayoutParams) this.mIcon.getLayoutParams()).gravity = 17;
    }

    public void setItem(OnScreenButtonGroup.Item item) {
        if (item == this.mItem) {
            return;
        }
        if (item == null || !item.equals(this.mItem)) {
            if (this.mItem != null) {
                setPressed(false);
                this.mIsCanceled = true;
                this.mListener.onCancel(this, null);
                this.mItem.removeOnUpdatedListener(this);
            }
            this.mItem = item;
            if (this.mItem != null) {
                setListener(this.mItem.getOnScreenButtonListener());
                setSoundEffectsEnabled(this.mItem.isSoundEffectsEnabled());
                setEnabled(this.mItem.isEnabled());
                set(this.mItem.getResource());
                setPressed(false);
                this.mItem.addOnUpdatedListener(this);
                return;
            }
            setListener(null);
            setSoundEffectsEnabled(true);
            setEnabled(true);
            setSelected(false);
            set(null);
        }
    }

    @Deprecated
    public void set(Resource resource) {
        if (resource == this.mResource) {
            return;
        }
        if (resource == null) {
            this.mResource = EMPTY_RESOURCE;
        } else {
            this.mResource = resource;
        }
        update();
    }

    @Deprecated
    public void setListener(OnScreenButtonListener onScreenButtonListener) {
        if (onScreenButtonListener == null) {
            this.mListener = EMPTY_LISTENER;
        } else {
            this.mListener = onScreenButtonListener;
        }
    }

    public void setUiOrientation(int i) {
        if (i == this.mOrientation) {
            return;
        }
        if (this.mIsRotatable) {
            this.mOrientation = i;
        } else {
            this.mOrientation = this.mStaticOrientation;
        }
        update();
    }

    public void clearTouched() {
        this.mIsTouched = false;
    }

    public boolean isTouched() {
        return this.mIsTouched;
    }

    private void update() {
        if (CamLog.VERBOSE) {
            CamLog.d("update orientation:" + this.mOrientation);
        }
        if (this.mResource.getIconResource(this.mOrientation) != -1) {
            this.mIcon.setImageResource(this.mResource.getIconResource(this.mOrientation));
            this.mIcon.requestLayout();
            if (this.mResource.shouldRotateByView()) {
                this.mIcon.setRotation(RotationUtil.getAngle(this.mOrientation));
            } else {
                this.mIcon.setRotation(0.0f);
            }
        } else {
            this.mIcon.setImageDrawable(null);
        }
        if (this.mResource.getBackgroundResource() != -1) {
            setBackgroundResource(this.mResource.getBackgroundResource());
        } else {
            setBackground(null);
        }
        setContentDescription(this.mResource.getDescription(getContext()));
        if (isEnabled()) {
            this.mIcon.clearColorFilter();
        } else {
            this.mIcon.setColorFilter(2131099706);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        if (CamLog.VERBOSE) {
            CamLog.d("onTouchEvent()");
        }
        if (this.mListener == EMPTY_LISTENER) {
            return false;
        }
        switch (motionEvent.getAction()) {
            case 0:
                if (CamLog.VERBOSE) {
                    CamLog.d("  event:DOWN");
                }
                setPressed(true);
                this.mIsTouched = true;
                this.mIsCanceled = false;
                this.mListener.onDown(this, motionEvent);
                return true;
            case 1:
                if (contains(motionEvent) && this.mIsTouched && !this.mIsCanceled && hasWindowFocus()) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  event:UP");
                    }
                    setPressed(false);
                    this.mIsTouched = true;
                    if (isEnabled()) {
                        playSoundEffect(0);
                        this.mListener.onUp(this, motionEvent);
                    } else {
                        motionEvent.setAction(3);
                        this.mListener.onCancel(this, motionEvent);
                    }
                } else {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  event:UP outside of CaptureButton");
                    }
                    setPressed(false);
                    this.mIsTouched = false;
                    motionEvent.setAction(3);
                    this.mListener.onCancel(this, motionEvent);
                }
                this.mIsCanceled = false;
                return true;
            case 2:
                if (contains(motionEvent)) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  event:MOVE");
                    }
                    this.mListener.onMove(this, motionEvent);
                } else {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  event:MOVE to outside of CaptureButton");
                    }
                    setPressed(false);
                    this.mIsTouched = false;
                    this.mIsCanceled = false;
                    motionEvent.setAction(3);
                    this.mListener.onCancel(this, motionEvent);
                }
                return true;
            case 3:
                if (CamLog.VERBOSE) {
                    CamLog.d("  event:CANCEL");
                }
                setPressed(false);
                this.mIsTouched = false;
                this.mIsCanceled = false;
                this.mListener.onCancel(this, motionEvent);
                return true;
            default:
                return true;
        }
    }

    private boolean contains(MotionEvent motionEvent) {
        return getGlobalVisibleRect(new Rect()) && motionEvent.getX() >= 0.0f && motionEvent.getX() <= ((float) (getHeight() - 1)) && motionEvent.getY() >= 0.0f && motionEvent.getY() <= ((float) (getWidth() - 1));
    }

    public void changeRotatability(int i, boolean z) {
        this.mIsRotatable = z;
        if (!z) {
            this.mStaticOrientation = i;
        }
        setUiOrientation(i);
    }

    @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonGroup.OnItemUpdatedListener
    public void onUpdated(OnScreenButtonGroup.Item item) {
        boolean z = this.mResource.getIconResource(this.mOrientation) != item.getResource().getIconResource(this.mOrientation);
        setSoundEffectsEnabled(item.isSoundEffectsEnabled());
        setEnabled(item.isEnabled());
        set(item.getResource());
        if (isShown() && z) {
            this.mIcon.startAnimation(createIconAnimation());
        }
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        if (getVisibility() == i) {
            return;
        }
        super.setVisibility(i);
    }

    private Animation createIconAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(new OvershootInterpolator());
        scaleAnimation.setDuration(250L);
        scaleAnimation.setStartOffset(50L);
        return scaleAnimation;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        sendStartupPerformanceDataForReadyForUse();
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.VIDEO_RECORDING_STOP_READY_FOR_USE);
    }

    private void sendStartupPerformanceDataForReadyForUse() {
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
    }
}
