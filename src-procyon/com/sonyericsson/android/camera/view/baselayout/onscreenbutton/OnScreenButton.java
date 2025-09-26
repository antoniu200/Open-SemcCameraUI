// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout.onscreenbutton;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import android.widget.FrameLayout$LayoutParams;
import android.widget.ImageView$ScaleType;
import android.view.View;
import android.view.View$OnLongClickListener;
import android.view.View$OnClickListener;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.content.Context;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class OnScreenButton extends FrameLayout implements OnItemUpdatedListener
{
    private static final int DISABLED_FILTER = 2131099706;
    public static final OnScreenButtonListener EMPTY_LISTENER;
    public static final Resource EMPTY_RESOURCE;
    private static final String TAG = "OnScreenButton";
    private final ImageView mIcon;
    private boolean mIsCanceled;
    private boolean mIsRotatable;
    private boolean mIsTouched;
    private Item mItem;
    private OnScreenButtonListener mListener;
    private int mOrientation;
    private Resource mResource;
    private int mStaticOrientation;
    
    static {
        EMPTY_LISTENER = new OnScreenButtonListener() {
            @Override
            public void onCancel(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
            }
            
            @Override
            public void onDown(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
            }
            
            @Override
            public void onLongPress(final OnScreenButton onScreenButton) {
            }
            
            @Override
            public void onMove(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
            }
            
            @Override
            public void onUp(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
            }
        };
        EMPTY_RESOURCE = new Resource(-1, -1, -1, -1, null);
    }
    
    public OnScreenButton(final Context context) {
        super(context);
        this.mStaticOrientation = 0;
        this.mIsRotatable = true;
        this.mIcon = new ImageView(context);
        this.init();
    }
    
    public OnScreenButton(final Context context, final AttributeSet set) {
        super(context, set);
        this.mStaticOrientation = 0;
        this.mIsRotatable = true;
        this.mIcon = new ImageView(context);
        this.init();
    }
    
    private boolean contains(final MotionEvent motionEvent) {
        return this.getGlobalVisibleRect(new Rect()) && (motionEvent.getX() >= 0.0f && motionEvent.getX() <= this.getHeight() - 1 && motionEvent.getY() >= 0.0f && motionEvent.getY() <= this.getWidth() - 1);
    }
    
    private Animation createIconAnimation() {
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator((Interpolator)new OvershootInterpolator());
        scaleAnimation.setDuration(250L);
        scaleAnimation.setStartOffset(50L);
        return (Animation)scaleAnimation;
    }
    
    private void init() {
        this.mOrientation = 2;
        this.mResource = OnScreenButton.EMPTY_RESOURCE;
        this.mListener = OnScreenButton.EMPTY_LISTENER;
        this.mItem = null;
        this.mIsTouched = false;
        this.setFocusable(this.mIsCanceled = false);
        this.setFocusableInTouchMode(false);
        this.setOnClickListener((View$OnClickListener)null);
        this.setClickable(false);
        this.setOnLongClickListener((View$OnLongClickListener)new LongClickListener());
        this.setSoundEffectsEnabled(false);
        this.addView((View)this.mIcon);
        this.mIcon.setScaleType(ImageView$ScaleType.CENTER);
        this.mIcon.getLayoutParams().width = -2;
        this.mIcon.getLayoutParams().height = -2;
        ((FrameLayout$LayoutParams)this.mIcon.getLayoutParams()).gravity = 17;
    }
    
    private void sendStartupPerformanceDataForReadyForUse() {
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
    }
    
    private void update() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("update orientation:");
            sb.append(this.mOrientation);
            CamLog.d(sb.toString());
        }
        if (this.mResource.getIconResource(this.mOrientation) != -1) {
            this.mIcon.setImageResource(this.mResource.getIconResource(this.mOrientation));
            this.mIcon.requestLayout();
            if (this.mResource.shouldRotateByView()) {
                this.mIcon.setRotation(RotationUtil.getAngle(this.mOrientation));
            }
            else {
                this.mIcon.setRotation(0.0f);
            }
        }
        else {
            this.mIcon.setImageDrawable((Drawable)null);
        }
        if (this.mResource.getBackgroundResource() != -1) {
            this.setBackgroundResource(this.mResource.getBackgroundResource());
        }
        else {
            this.setBackground((Drawable)null);
        }
        this.setContentDescription((CharSequence)this.mResource.getDescription(this.getContext()));
        if (this.isEnabled()) {
            this.mIcon.clearColorFilter();
        }
        else {
            this.mIcon.setColorFilter(2131099706);
        }
    }
    
    public void changeRotatability(final int n, final boolean mIsRotatable) {
        if (!(this.mIsRotatable = mIsRotatable)) {
            this.mStaticOrientation = n;
        }
        this.setUiOrientation(n);
    }
    
    public void clearTouched() {
        this.mIsTouched = false;
    }
    
    protected void dispatchDraw(final Canvas canvas) {
        super.dispatchDraw(canvas);
        this.sendStartupPerformanceDataForReadyForUse();
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.VIDEO_RECORDING_STOP_READY_FOR_USE);
    }
    
    public boolean isTouched() {
        return this.mIsTouched;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        if (CamLog.VERBOSE) {
            CamLog.d("onTouchEvent()");
        }
        if (this.mListener == OnScreenButton.EMPTY_LISTENER) {
            return false;
        }
        switch (motionEvent.getAction()) {
            case 3: {
                if (CamLog.VERBOSE) {
                    CamLog.d("  event:CANCEL");
                }
                this.setPressed(false);
                this.mIsTouched = false;
                this.mIsCanceled = false;
                this.mListener.onCancel(this, motionEvent);
                break;
            }
            case 2: {
                if (this.contains(motionEvent)) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  event:MOVE");
                    }
                    this.mListener.onMove(this, motionEvent);
                    break;
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("  event:MOVE to outside of CaptureButton");
                }
                this.setPressed(false);
                this.mIsTouched = false;
                this.mIsCanceled = false;
                motionEvent.setAction(3);
                this.mListener.onCancel(this, motionEvent);
                break;
            }
            case 1: {
                if (this.contains(motionEvent) && this.mIsTouched && !this.mIsCanceled && this.hasWindowFocus()) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  event:UP");
                    }
                    this.setPressed(false);
                    this.mIsTouched = true;
                    if (this.isEnabled()) {
                        this.playSoundEffect(0);
                        this.mListener.onUp(this, motionEvent);
                    }
                    else {
                        motionEvent.setAction(3);
                        this.mListener.onCancel(this, motionEvent);
                    }
                }
                else {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  event:UP outside of CaptureButton");
                    }
                    this.setPressed(false);
                    this.mIsTouched = false;
                    motionEvent.setAction(3);
                    this.mListener.onCancel(this, motionEvent);
                }
                this.mIsCanceled = false;
                break;
            }
            case 0: {
                if (CamLog.VERBOSE) {
                    CamLog.d("  event:DOWN");
                }
                this.setPressed(true);
                this.mIsTouched = true;
                this.mIsCanceled = false;
                this.mListener.onDown(this, motionEvent);
                break;
            }
        }
        return true;
    }
    
    public void onUpdated(final Item item) {
        final boolean b = this.mResource.getIconResource(this.mOrientation) != item.getResource().getIconResource(this.mOrientation);
        this.setSoundEffectsEnabled(item.isSoundEffectsEnabled());
        this.setEnabled(item.isEnabled());
        this.set(item.getResource());
        if (this.isShown() && b) {
            this.mIcon.startAnimation(this.createIconAnimation());
        }
    }
    
    @Deprecated
    public void set(final Resource mResource) {
        if (mResource == this.mResource) {
            return;
        }
        if (mResource == null) {
            this.mResource = OnScreenButton.EMPTY_RESOURCE;
        }
        else {
            this.mResource = mResource;
        }
        this.update();
    }
    
    public void setItem(final Item mItem) {
        if (mItem == this.mItem) {
            return;
        }
        if (mItem != null && mItem.equals(this.mItem)) {
            return;
        }
        if (this.mItem != null) {
            this.setPressed(false);
            this.mIsCanceled = true;
            this.mListener.onCancel(this, null);
            this.mItem.removeOnUpdatedListener(this);
        }
        this.mItem = mItem;
        if (this.mItem != null) {
            this.setListener(this.mItem.getOnScreenButtonListener());
            this.setSoundEffectsEnabled(this.mItem.isSoundEffectsEnabled());
            this.setEnabled(this.mItem.isEnabled());
            this.set(this.mItem.getResource());
            this.setPressed(false);
            this.mItem.addOnUpdatedListener(this);
        }
        else {
            this.setListener(null);
            this.setSoundEffectsEnabled(true);
            this.setEnabled(true);
            this.setSelected(false);
            this.set(null);
        }
    }
    
    @Deprecated
    public void setListener(final OnScreenButtonListener mListener) {
        if (mListener == null) {
            this.mListener = OnScreenButton.EMPTY_LISTENER;
        }
        else {
            this.mListener = mListener;
        }
    }
    
    public void setUiOrientation(final int mOrientation) {
        if (mOrientation == this.mOrientation) {
            return;
        }
        if (this.mIsRotatable) {
            this.mOrientation = mOrientation;
        }
        else {
            this.mOrientation = this.mStaticOrientation;
        }
        this.update();
    }
    
    public void setVisibility(final int visibility) {
        if (this.getVisibility() == visibility) {
            return;
        }
        super.setVisibility(visibility);
    }
    
    private class LongClickListener implements View$OnLongClickListener
    {
        final OnScreenButton this$0;
        
        private LongClickListener(final OnScreenButton this$0) {
            this.this$0 = this$0;
        }
        
        public boolean onLongClick(final View view) {
            if (CamLog.VERBOSE) {
                CamLog.d("onLongClick()");
            }
            if (this.this$0.mListener == OnScreenButton.EMPTY_LISTENER) {
                return false;
            }
            this.this$0.mListener.onLongPress(this.this$0);
            return false;
        }
    }
    
    public static class Resource
    {
        public static final int NONE = -1;
        final int mBackground;
        final int mDescription;
        final int mIcon;
        final int mIconPortrait;
        final String mText;
        
        public Resource(final int mIcon, final int mIconPortrait, final int mBackground, final int mDescription, final String mText) {
            this.mIcon = mIcon;
            this.mIconPortrait = mIconPortrait;
            this.mBackground = mBackground;
            this.mDescription = mDescription;
            this.mText = mText;
        }
        
        int getBackgroundResource() {
            return this.mBackground;
        }
        
        String getDescription(final Context context) {
            if (this.mDescription != -1) {
                return context.getResources().getString(this.mDescription);
            }
            if (this.mText != null) {
                return this.mText;
            }
            return "";
        }
        
        int getIconResource(final int n) {
            if (n == 2) {
                return this.mIcon;
            }
            if (this.shouldRotateByView()) {
                return this.mIcon;
            }
            return this.mIconPortrait;
        }
        
        boolean shouldRotateByView() {
            return this.mIconPortrait == -1;
        }
    }
}
