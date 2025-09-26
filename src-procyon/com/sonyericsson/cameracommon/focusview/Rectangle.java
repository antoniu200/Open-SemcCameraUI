// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.focusview;

import com.sonyericsson.android.camera.util.CamLog;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector$OnGestureListener;
import android.widget.RelativeLayout;

public class Rectangle extends RelativeLayout implements GestureDetector$OnGestureListener
{
    public static final String TAG = "Rectangles";
    private GestureDetector mGestureDetector;
    private RectangleOnTouchListener mRectangleOnTouchListener;
    
    public Rectangle(final Context context) {
        super(context);
    }
    
    public Rectangle(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public Rectangle(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    private GestureDetector getGestureDetector() {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new GestureDetector(this.getContext(), (GestureDetector$OnGestureListener)this);
        }
        return this.mGestureDetector;
    }
    
    public void changeChildBackgroundResource(final int backgroundResource) {
        final View viewById = this.findViewById(2131296528);
        if (viewById.getVisibility() != 8) {
            viewById.setBackgroundResource(backgroundResource);
        }
    }
    
    public boolean onDown(final MotionEvent motionEvent) {
        return false;
    }
    
    public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
        return false;
    }
    
    public void onLongPress(final MotionEvent motionEvent) {
        synchronized (this) {
            if (this.mRectangleOnTouchListener != null) {
                this.mRectangleOnTouchListener.onRectTouchLongPress((View)this, motionEvent);
            }
        }
    }
    
    public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
        return false;
    }
    
    public void onShowPress(final MotionEvent motionEvent) {
    }
    
    public boolean onSingleTapUp(final MotionEvent motionEvent) {
        return false;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        synchronized (this) {
            final boolean verbose = CamLog.VERBOSE;
            boolean b = false;
            if (verbose) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onTouchEvent: action: ");
                sb.append(motionEvent.getAction());
                sb.append(", (x, y): ");
                sb.append(motionEvent.getRawX());
                sb.append(", ");
                sb.append(motionEvent.getRawY());
                CamLog.d(sb.toString());
            }
            super.onTouchEvent(motionEvent);
            this.getGestureDetector().onTouchEvent(motionEvent);
            if (this.mRectangleOnTouchListener != null) {
                b = true;
            }
            switch (motionEvent.getAction()) {
                case 1: {
                    if (this.mRectangleOnTouchListener == null) {
                        break;
                    }
                    if (motionEvent.getX() >= 0.0f && motionEvent.getX() <= this.getHeight() - 1 && motionEvent.getY() >= 0.0f && motionEvent.getY() <= this.getWidth() - 1) {
                        this.mRectangleOnTouchListener.onRectTouchUp((View)this, motionEvent);
                        break;
                    }
                    this.mRectangleOnTouchListener.onRectTouchCancel((View)this, motionEvent);
                    break;
                }
                case 0: {
                    if (this.mRectangleOnTouchListener != null) {
                        this.mRectangleOnTouchListener.onRectTouchDown((View)this, motionEvent);
                        break;
                    }
                    break;
                }
            }
            return b;
        }
    }
    
    public void setRectangleOnTouchListener(final RectangleOnTouchListener mRectangleOnTouchListener) {
        synchronized (this) {
            this.mRectangleOnTouchListener = mRectangleOnTouchListener;
        }
    }
    
    public interface RectangleOnTouchListener
    {
        void onRectTouchCancel(final View p0, final MotionEvent p1);
        
        void onRectTouchDown(final View p0, final MotionEvent p1);
        
        void onRectTouchLongPress(final View p0, final MotionEvent p1);
        
        void onRectTouchUp(final View p0, final MotionEvent p1);
    }
}
