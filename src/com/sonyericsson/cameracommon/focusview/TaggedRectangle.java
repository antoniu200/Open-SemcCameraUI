package com.sonyericsson.cameracommon.focusview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.focusview.Rectangle;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TaggedRectangle extends RelativeLayout implements Rectangle.RectangleOnTouchListener {
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
    private Rectangle.RectangleOnTouchListener mRectangleOnTouchListener;
    private SmileGauge[] mSmileGauges;

    public TaggedRectangle(Context context) {
        super(context);
        this.mIsUpdate = false;
        this.mLastSizeUpdatedTimestamp = 0L;
        this.mIsAbleToTouch = true;
        this.mIsLockedBySelfTimer = false;
    }

    public TaggedRectangle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsUpdate = false;
        this.mLastSizeUpdatedTimestamp = 0L;
        this.mIsAbleToTouch = true;
        this.mIsLockedBySelfTimer = false;
    }

    public TaggedRectangle(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsUpdate = false;
        this.mLastSizeUpdatedTimestamp = 0L;
        this.mIsAbleToTouch = true;
        this.mIsLockedBySelfTimer = false;
    }

    private void prepare() {
        if (CamLog.VERBOSE) {
            CamLog.d("prepare() is called.");
        }
        this.mRectangle = (Rectangle) findViewById(R.id.rect);
        this.mSmileGauges = new SmileGauge[3];
        this.mSmileGauges[0] = (SmileGauge) findViewById(R.id.smile_gauge_left);
        this.mSmileGauges[1] = (SmileGauge) findViewById(R.id.smile_gauge_top);
        this.mSmileGauges[2] = (SmileGauge) findViewById(R.id.smile_gauge_bottom);
    }

    public void prepare(int i) {
        this.mCurrentType = i;
        prepare();
        switch (i) {
            case 0:
                this.mRectangle.setRectangleOnTouchListener(this);
                break;
            case 2:
                this.mRectangle.setVisibility(4);
                break;
        }
    }

    public void setRawPosition(Rect rect) {
        setRectSize(rect.width(), rect.height());
        setRectCenter(rect.centerX(), rect.centerY());
    }

    public void setRectPosition(int i, int i2, int i3, int i4) {
        if (isRectSizeAlreadyInvalid()) {
            setRectSize(i3, i4);
            setRectCenter(i, i2);
            this.mLastSizeUpdatedTimestamp = System.currentTimeMillis();
        }
    }

    public void setRectImageSize(int i, int i2, int i3, int i4) {
        ImageView imageView = (ImageView) this.mRectangle.findViewById(R.id.rect_image);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        this.mRectImageWidth = i3;
        this.mRectImageHeight = i4;
        if (layoutParams != null) {
            layoutParams.height = i4;
            layoutParams.width = i3;
            imageView.setLayoutParams(layoutParams);
            imageView.requestLayout();
        }
        setRectCenter(i, i2);
    }

    private void setRectSize(int i, int i2) {
        ViewGroup.LayoutParams layoutParams = this.mRectangle.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = i2;
            layoutParams.width = i;
            this.mRectangle.setLayoutParams(layoutParams);
        }
    }

    private boolean isRectSizeAlreadyInvalid() {
        return 300 < System.currentTimeMillis() - this.mLastSizeUpdatedTimestamp;
    }

    public void setSize(int i, int i2) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = i2;
        layoutParams.width = i;
        setLayoutParams(layoutParams);
    }

    private void setRectCenter(int i, int i2) {
        int width;
        int height;
        switch (this.mCurrentType) {
            case 0:
            case 1:
                width = getWidth() / 2;
                height = getHeight() / 2;
                break;
            default:
                width = this.mRectangle.getWidth() / 2;
                height = this.mRectangle.getHeight() / 2;
                break;
        }
        scrollTo((-i) + width, (-i2) + height);
    }

    public void moveRectTopLeft(int i, int i2) {
        scrollBy(-i, -i2);
    }

    public final void changeRectangleResource(int i) {
        this.mRectangle.changeChildBackgroundResource(i);
    }

    public void setRectangleOnTouchListener(Rectangle.RectangleOnTouchListener rectangleOnTouchListener) {
        this.mRectangleOnTouchListener = rectangleOnTouchListener;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        if (CamLog.VERBOSE) {
            CamLog.d("onDetachedFromWindow() is called.");
        }
        this.mRectangleOnTouchListener = null;
        this.mRectangle.setRectangleOnTouchListener(null);
        super.onDetachedFromWindow();
    }

    @Override // com.sonyericsson.cameracommon.focusview.Rectangle.RectangleOnTouchListener
    public void onRectTouchDown(View view, MotionEvent motionEvent) {
        if (this.mIsLockedBySelfTimer || this.mRectangleOnTouchListener == null || !this.mIsAbleToTouch) {
            return;
        }
        this.mRectangleOnTouchListener.onRectTouchDown(this, motionEvent);
    }

    @Override // com.sonyericsson.cameracommon.focusview.Rectangle.RectangleOnTouchListener
    public void onRectTouchUp(View view, MotionEvent motionEvent) {
        if (this.mIsLockedBySelfTimer || this.mRectangleOnTouchListener == null || !this.mIsAbleToTouch) {
            return;
        }
        this.mRectangleOnTouchListener.onRectTouchUp(this, motionEvent);
    }

    @Override // com.sonyericsson.cameracommon.focusview.Rectangle.RectangleOnTouchListener
    public void onRectTouchCancel(View view, MotionEvent motionEvent) {
        if (this.mIsLockedBySelfTimer || this.mRectangleOnTouchListener == null || !this.mIsAbleToTouch) {
            return;
        }
        this.mRectangleOnTouchListener.onRectTouchCancel(this, motionEvent);
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

    public int getRectangleHeight() {
        return this.mRectangle.getHeight();
    }

    public final void setSmileLevel(int i) {
        if (i < 0) {
            return;
        }
        for (SmileGauge smileGauge : this.mSmileGauges) {
            smileGauge.setSmileLevel(i);
        }
    }

    public void setSmileScore(int i) {
        this.mSmileGauges[0].setSmileScore(i);
        this.mSmileGauges[1].setSmileScore(i);
        this.mSmileGauges[2].setSmileScore(i);
    }

    private boolean isSmileGaugeVisible() {
        return this.mSmileGauges[0].getVisibility() == 0 || this.mSmileGauges[1].getVisibility() == 0 || this.mSmileGauges[2].getVisibility() == 0;
    }

    public void setSmileGaugeVisibility(int i) {
        this.mSmileGauges[0].setVisibility(i);
        this.mSmileGauges[1].setVisibility(i);
        this.mSmileGauges[2].setVisibility(i);
    }

    public void setSmileGaugesPosition(int i, int i2, int i3, int i4, int i5) {
        this.mSmileGauges[0].setPosition(i, i2, i3, i4, i5);
        this.mSmileGauges[1].setPosition(i, i2, i3, i4, i5);
        this.mSmileGauges[2].setPosition(i, i2, i3, i4, i5);
    }

    public Rect getFaceRect() {
        Rect rect = new Rect();
        this.mRectangle.getGlobalVisibleRect(rect);
        int[] iArr = new int[2];
        getLocationInWindow(iArr);
        rect.offset(-iArr[0], -iArr[1]);
        return rect;
    }

    public String getUuid() {
        return this.mFaceUuid;
    }

    private void setUuid(String str) {
        this.mFaceUuid = str;
    }

    public void update(String str, int i) {
        setUuid(str);
    }

    public void stopAnimation() {
        if (this.mRectangle.getAnimation() != null) {
            this.mRectangle.clearAnimation();
            this.mRectangle.setAnimation(null);
        }
    }

    public void startRectangleAnimation(int i) {
        this.mRectangle.startAnimation((AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.tagged_rectangle_show));
    }

    public void startRectanglePressAnimation() {
        this.mRectangle.startAnimation((AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.tagged_rectangle_press));
    }

    public void setRectOrientation(int i) {
        if (i == 1) {
            this.mRectangle.setRotation(-90.0f);
        } else {
            this.mRectangle.setRotation(0.0f);
        }
    }

    public void setUpdated() {
        this.mIsUpdate = true;
    }

    public void clearUpdated() {
        this.mIsUpdate = false;
    }

    public boolean isUpdate() {
        return this.mIsUpdate;
    }

    public void hide() {
        if (isSmileGaugeVisible()) {
            setSmileGaugeVisibility(4);
        }
        if (getVisibility() != 4) {
            ViewGroup.LayoutParams layoutParams = this.mRectangle.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.height = 0;
                layoutParams.width = 0;
                this.mRectangle.setLayoutParams(layoutParams);
            }
            setVisibility(4);
            this.mLastSizeUpdatedTimestamp = 0L;
        }
    }

    @Override // com.sonyericsson.cameracommon.focusview.Rectangle.RectangleOnTouchListener
    public void onRectTouchLongPress(View view, MotionEvent motionEvent) {
        if (this.mIsLockedBySelfTimer || this.mRectangleOnTouchListener == null || !this.mIsAbleToTouch) {
            return;
        }
        this.mRectangleOnTouchListener.onRectTouchLongPress(this, motionEvent);
    }

    public void setIsAbleToTouch(boolean z) {
        this.mIsAbleToTouch = z;
    }

    public void setLockedBySelfTimer(boolean z) {
        this.mIsLockedBySelfTimer = z;
    }

    public int getRectImageWidth() {
        return this.mRectImageWidth;
    }

    public int getRectImageHeight() {
        return this.mRectImageHeight;
    }
}
