// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.focusview;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Rect;
import android.view.ViewGroup$LayoutParams;
import com.sonyericsson.android.camera.util.CamLog;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.RelativeLayout;

public class TaggedRectangle extends RelativeLayout implements RectangleOnTouchListener
{
    public static final int FACEDETECT_CAPTURE = 1;
    public static final int FACERECOGNITION_REVIEW = 2;
    private static final int GAUGE_DIR_BOTTOM = 2;
    private static final int GAUGE_DIR_LEFT = 0;
    private static final int GAUGE_DIR_TOP = 1;
    public static final int OBJECT_TRACKING = 3;
    private static final int RECT_SIZE_LIFE_TIME_MILLIS = 300;
    public static final int SMILE_DETECTION_CAPTURE = 0;
    private static final int SMILE_GAUGE_NUMBER = 3;
    public static final String TAG = "TaggedRectangle";
    private int mCurrentType;
    private String mFaceUuid;
    private boolean mIsAbleToTouch;
    private boolean mIsLockedBySelfTimer;
    private boolean mIsUpdate;
    private long mLastSizeUpdatedTimestamp;
    private int mRectImageHeight;
    private int mRectImageWidth;
    private Rectangle mRectangle;
    private RectangleOnTouchListener mRectangleOnTouchListener;
    private SmileGauge[] mSmileGauges;
    
    public TaggedRectangle(final Context context) {
        super(context);
        this.mIsUpdate = false;
        this.mLastSizeUpdatedTimestamp = 0L;
        this.mIsAbleToTouch = true;
        this.mIsLockedBySelfTimer = false;
    }
    
    public TaggedRectangle(final Context context, final AttributeSet set) {
        super(context, set);
        this.mIsUpdate = false;
        this.mLastSizeUpdatedTimestamp = 0L;
        this.mIsAbleToTouch = true;
        this.mIsLockedBySelfTimer = false;
    }
    
    public TaggedRectangle(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mIsUpdate = false;
        this.mLastSizeUpdatedTimestamp = 0L;
        this.mIsAbleToTouch = true;
        this.mIsLockedBySelfTimer = false;
    }
    
    private boolean isRectSizeAlreadyInvalid() {
        return 300L < System.currentTimeMillis() - this.mLastSizeUpdatedTimestamp;
    }
    
    private boolean isSmileGaugeVisible() {
        return this.mSmileGauges[0].getVisibility() == 0 || this.mSmileGauges[1].getVisibility() == 0 || this.mSmileGauges[2].getVisibility() == 0;
    }
    
    private void prepare() {
        if (CamLog.VERBOSE) {
            CamLog.d("prepare() is called.");
        }
        this.mRectangle = (Rectangle)this.findViewById(2131296527);
        (this.mSmileGauges = new SmileGauge[3])[0] = (SmileGauge)this.findViewById(2131296615);
        this.mSmileGauges[1] = (SmileGauge)this.findViewById(2131296618);
        this.mSmileGauges[2] = (SmileGauge)this.findViewById(2131296613);
    }
    
    private void setRectCenter(final int n, final int n2) {
        int n3 = 0;
        int n4 = 0;
        switch (this.mCurrentType) {
            default: {
                n3 = this.mRectangle.getWidth() / 2;
                n4 = this.mRectangle.getHeight() / 2;
                break;
            }
            case 0:
            case 1: {
                n3 = this.getWidth() / 2;
                n4 = this.getHeight() / 2;
                break;
            }
        }
        this.scrollTo(-n + n3, -n2 + n4);
    }
    
    private void setRectSize(final int width, final int height) {
        final ViewGroup$LayoutParams layoutParams = this.mRectangle.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = height;
            layoutParams.width = width;
            this.mRectangle.setLayoutParams(layoutParams);
        }
    }
    
    private void setUuid(final String mFaceUuid) {
        this.mFaceUuid = mFaceUuid;
    }
    
    public final void changeRectangleResource(final int n) {
        this.mRectangle.changeChildBackgroundResource(n);
    }
    
    public void clearUpdated() {
        this.mIsUpdate = false;
    }
    
    public Rect getFaceRect() {
        final Rect rect = new Rect();
        this.mRectangle.getGlobalVisibleRect(rect);
        final int[] array = new int[2];
        this.getLocationInWindow(array);
        rect.offset(-array[0], -array[1]);
        return rect;
    }
    
    public int getRectImageHeight() {
        return this.mRectImageHeight;
    }
    
    public int getRectImageWidth() {
        return this.mRectImageWidth;
    }
    
    public int getRectangleHeight() {
        return this.mRectangle.getHeight();
    }
    
    public int getRectangleLeft() {
        return this.mRectangle.getLeft();
    }
    
    public int getRectangleTop() {
        return this.mRectangle.getTop();
    }
    
    public int getRectangleWidth() {
        return this.mRectangle.getWidth();
    }
    
    public String getUuid() {
        return this.mFaceUuid;
    }
    
    public void hide() {
        if (this.isSmileGaugeVisible()) {
            this.setSmileGaugeVisibility(4);
        }
        if (this.getVisibility() != 4) {
            final ViewGroup$LayoutParams layoutParams = this.mRectangle.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.height = 0;
                layoutParams.width = 0;
                this.mRectangle.setLayoutParams(layoutParams);
            }
            this.setVisibility(4);
            this.mLastSizeUpdatedTimestamp = 0L;
        }
    }
    
    public boolean isUpdate() {
        return this.mIsUpdate;
    }
    
    public void moveRectTopLeft(final int n, final int n2) {
        this.scrollBy(-n, -n2);
    }
    
    protected void onDetachedFromWindow() {
        if (CamLog.VERBOSE) {
            CamLog.d("onDetachedFromWindow() is called.");
        }
        this.mRectangleOnTouchListener = null;
        this.mRectangle.setRectangleOnTouchListener(null);
        super.onDetachedFromWindow();
    }
    
    public void onRectTouchCancel(final View view, final MotionEvent motionEvent) {
        if (this.mIsLockedBySelfTimer) {
            return;
        }
        if (this.mRectangleOnTouchListener != null && this.mIsAbleToTouch) {
            this.mRectangleOnTouchListener.onRectTouchCancel((View)this, motionEvent);
        }
    }
    
    public void onRectTouchDown(final View view, final MotionEvent motionEvent) {
        if (this.mIsLockedBySelfTimer) {
            return;
        }
        if (this.mRectangleOnTouchListener != null && this.mIsAbleToTouch) {
            this.mRectangleOnTouchListener.onRectTouchDown((View)this, motionEvent);
        }
    }
    
    public void onRectTouchLongPress(final View view, final MotionEvent motionEvent) {
        if (this.mIsLockedBySelfTimer) {
            return;
        }
        if (this.mRectangleOnTouchListener != null && this.mIsAbleToTouch) {
            this.mRectangleOnTouchListener.onRectTouchLongPress((View)this, motionEvent);
        }
    }
    
    public void onRectTouchUp(final View view, final MotionEvent motionEvent) {
        if (this.mIsLockedBySelfTimer) {
            return;
        }
        if (this.mRectangleOnTouchListener != null && this.mIsAbleToTouch) {
            this.mRectangleOnTouchListener.onRectTouchUp((View)this, motionEvent);
        }
    }
    
    public void prepare(final int mCurrentType) {
        this.mCurrentType = mCurrentType;
        this.prepare();
        switch (mCurrentType) {
            default: {
                return;
            }
            case 1:
            case 3: {
                return;
            }
            case 2: {
                this.mRectangle.setVisibility(4);
                return;
            }
            case 0: {
                this.mRectangle.setRectangleOnTouchListener((Rectangle.RectangleOnTouchListener)this);
            }
        }
    }
    
    public void setIsAbleToTouch(final boolean mIsAbleToTouch) {
        this.mIsAbleToTouch = mIsAbleToTouch;
    }
    
    public void setLockedBySelfTimer(final boolean mIsLockedBySelfTimer) {
        this.mIsLockedBySelfTimer = mIsLockedBySelfTimer;
    }
    
    public void setRawPosition(final Rect rect) {
        this.setRectSize(rect.width(), rect.height());
        this.setRectCenter(rect.centerX(), rect.centerY());
    }
    
    public void setRectImageSize(final int n, final int n2, final int n3, final int n4) {
        final ImageView imageView = (ImageView)this.mRectangle.findViewById(2131296528);
        final ViewGroup$LayoutParams layoutParams = imageView.getLayoutParams();
        this.mRectImageWidth = n3;
        this.mRectImageHeight = n4;
        if (layoutParams != null) {
            layoutParams.height = n4;
            layoutParams.width = n3;
            imageView.setLayoutParams(layoutParams);
            imageView.requestLayout();
        }
        this.setRectCenter(n, n2);
    }
    
    public void setRectOrientation(final int n) {
        if (n == 1) {
            this.mRectangle.setRotation(-90.0f);
        }
        else {
            this.mRectangle.setRotation(0.0f);
        }
    }
    
    public void setRectPosition(final int n, final int n2, final int n3, final int n4) {
        if (this.isRectSizeAlreadyInvalid()) {
            this.setRectSize(n3, n4);
            this.setRectCenter(n, n2);
            this.mLastSizeUpdatedTimestamp = System.currentTimeMillis();
        }
    }
    
    public void setRectangleOnTouchListener(final RectangleOnTouchListener mRectangleOnTouchListener) {
        this.mRectangleOnTouchListener = mRectangleOnTouchListener;
    }
    
    public void setSize(final int width, final int height) {
        final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        this.setLayoutParams(layoutParams);
    }
    
    public void setSmileGaugeVisibility(final int visibility) {
        this.mSmileGauges[0].setVisibility(visibility);
        this.mSmileGauges[1].setVisibility(visibility);
        this.mSmileGauges[2].setVisibility(visibility);
    }
    
    public void setSmileGaugesPosition(final int n, final int n2, final int n3, final int n4, final int n5) {
        this.mSmileGauges[0].setPosition(n, n2, n3, n4, n5);
        this.mSmileGauges[1].setPosition(n, n2, n3, n4, n5);
        this.mSmileGauges[2].setPosition(n, n2, n3, n4, n5);
    }
    
    public final void setSmileLevel(final int smileLevel) {
        if (smileLevel < 0) {
            return;
        }
        final SmileGauge[] mSmileGauges = this.mSmileGauges;
        for (int length = mSmileGauges.length, i = 0; i < length; ++i) {
            mSmileGauges[i].setSmileLevel(smileLevel);
        }
    }
    
    public void setSmileScore(final int smileScore) {
        this.mSmileGauges[0].setSmileScore(smileScore);
        this.mSmileGauges[1].setSmileScore(smileScore);
        this.mSmileGauges[2].setSmileScore(smileScore);
    }
    
    public void setUpdated() {
        this.mIsUpdate = true;
    }
    
    public void startRectangleAnimation(final int n) {
        this.mRectangle.startAnimation((Animation)AnimationUtils.loadAnimation(this.getContext(), 2130772001));
    }
    
    public void startRectanglePressAnimation() {
        this.mRectangle.startAnimation((Animation)AnimationUtils.loadAnimation(this.getContext(), 2130772000));
    }
    
    public void stopAnimation() {
        if (this.mRectangle.getAnimation() != null) {
            this.mRectangle.clearAnimation();
            this.mRectangle.setAnimation((Animation)null);
        }
    }
    
    public void update(final String uuid, final int n) {
        this.setUuid(uuid);
    }
}
