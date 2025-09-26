package com.sonyericsson.cameracommon.focusview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class Rectangle extends RelativeLayout implements GestureDetector.OnGestureListener {
    public static final String TAG = "Rectangles";
    private GestureDetector mGestureDetector;
    private RectangleOnTouchListener mRectangleOnTouchListener;

    public interface RectangleOnTouchListener {
        void onRectTouchCancel(View view, MotionEvent motionEvent);

        void onRectTouchDown(View view, MotionEvent motionEvent);

        void onRectTouchLongPress(View view, MotionEvent motionEvent);

        void onRectTouchUp(View view, MotionEvent motionEvent);
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public Rectangle(Context context) {
        super(context);
    }

    public Rectangle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Rectangle(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    public synchronized boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        if (CamLog.VERBOSE) {
            CamLog.d("onTouchEvent: action: " + motionEvent.getAction() + ", (x, y): " + motionEvent.getRawX() + ", " + motionEvent.getRawY());
        }
        super.onTouchEvent(motionEvent);
        getGestureDetector().onTouchEvent(motionEvent);
        z = this.mRectangleOnTouchListener != null;
        switch (motionEvent.getAction()) {
            case 0:
                if (this.mRectangleOnTouchListener != null) {
                    this.mRectangleOnTouchListener.onRectTouchDown(this, motionEvent);
                    break;
                }
                break;
            case 1:
                if (this.mRectangleOnTouchListener != null) {
                    if (motionEvent.getX() < 0.0f || motionEvent.getX() > getHeight() - 1 || motionEvent.getY() < 0.0f || motionEvent.getY() > getWidth() - 1) {
                        this.mRectangleOnTouchListener.onRectTouchCancel(this, motionEvent);
                        break;
                    } else {
                        this.mRectangleOnTouchListener.onRectTouchUp(this, motionEvent);
                        break;
                    }
                }
                break;
        }
        return z;
    }

    public synchronized void setRectangleOnTouchListener(RectangleOnTouchListener rectangleOnTouchListener) {
        this.mRectangleOnTouchListener = rectangleOnTouchListener;
    }

    public void changeChildBackgroundResource(int i) {
        View viewFindViewById = findViewById(R.id.rect_image);
        if (viewFindViewById.getVisibility() != 8) {
            viewFindViewById.setBackgroundResource(i);
        }
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public synchronized void onLongPress(MotionEvent motionEvent) {
        if (this.mRectangleOnTouchListener != null) {
            this.mRectangleOnTouchListener.onRectTouchLongPress(this, motionEvent);
        }
    }

    private GestureDetector getGestureDetector() {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new GestureDetector(getContext(), this);
        }
        return this.mGestureDetector;
    }
}
