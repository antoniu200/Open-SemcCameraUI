// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import android.view.MotionEvent;
import com.sonyericsson.android.camera.util.CamLog;
import android.graphics.Point;
import android.view.View$OnTouchListener;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import com.sonyericsson.cameracommon.interaction.TouchActionTranslator;
import android.widget.ImageView;

public class CaptureArea extends ImageView implements TouchActionListener
{
    public static final String TAG = "CaptureArea";
    private boolean mIsForceTouchCanceled;
    private boolean mIsLongPressed;
    private boolean mIsTouched;
    private CaptureAreaStateListener mListener;
    private CaptureAreaTouchEventListener mTouchListener;
    private TouchActionTranslator mUserInteractionEngine;
    
    public CaptureArea(final Context context, final AttributeSet set) {
        super(context, set);
        this.mIsTouched = false;
        this.mIsLongPressed = false;
        this.mIsForceTouchCanceled = false;
        this.mUserInteractionEngine = null;
        this.mTouchListener = new CaptureAreaTouchEventListener();
        (this.mUserInteractionEngine = new TouchActionTranslator(context, (View)this, context.getResources().getDimensionPixelSize(2131165678))).setInteractionListener((TouchActionTranslator.TouchActionListener)this);
        this.setOnTouchListener((View$OnTouchListener)this.mTouchListener);
    }
    
    private Point convertPointCoordinatesFromThisViewToScreen(final Point point) {
        final int[] array = new int[2];
        this.getLocationOnScreen(array);
        return new Point(point.x + array[0], point.y + array[1]);
    }
    
    public void clearTouched() {
        this.mIsForceTouchCanceled = true;
    }
    
    public boolean isTouched() {
        return this.mIsTouched;
    }
    
    public void onDoubleCanceled() {
        if (CamLog.VERBOSE) {
            CamLog.d("onDoubleCanceled: ");
        }
        if (!this.mIsTouched) {
            return;
        }
        this.mIsTouched = false;
        this.mIsForceTouchCanceled = false;
        if (this.mListener != null) {
            this.mListener.onCaptureAreaCanceled();
        }
    }
    
    public void onDoubleMoved(final Point point, final Point point2) {
    }
    
    public void onDoubleRotated(final float n, final float n2) {
    }
    
    public void onDoubleScaled(final float n, final float n2, final float n3) {
        if (!this.mIsTouched) {
            return;
        }
        if (this.mListener != null) {
            this.mListener.onCaptureAreaScaled(n - n2);
        }
    }
    
    public void onDoubleTouched(final Point point, final Point point2) {
        if (this.mIsTouched && !this.mIsForceTouchCanceled) {
            if (this.mListener != null) {
                this.mListener.onCaptureAreaIsReadyToScale();
            }
        }
    }
    
    public void onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
    }
    
    public void onLongPress(final MotionEvent motionEvent) {
        this.mIsLongPressed = true;
        if (this.mListener != null) {
            this.mListener.onCaptureAreaLongPressed(this.convertPointCoordinatesFromThisViewToScreen(new Point((int)motionEvent.getX(), (int)motionEvent.getY())));
        }
    }
    
    public void onOverTripleCanceled() {
        if (CamLog.VERBOSE) {
            CamLog.d("onOverTripleCanceled: ");
        }
        if (!this.mIsTouched) {
            return;
        }
        this.mIsTouched = false;
        this.mIsForceTouchCanceled = false;
        if (this.mListener != null) {
            this.mListener.onCaptureAreaCanceled();
        }
    }
    
    public void onShowPress(final MotionEvent motionEvent) {
    }
    
    public void onSingleCanceled() {
        if (!this.mIsTouched) {
            return;
        }
        this.mIsTouched = false;
        this.mIsForceTouchCanceled = false;
        if (this.mListener != null) {
            this.mListener.onCaptureAreaCanceled();
        }
    }
    
    public void onSingleMoved(final Point point, final Point point2, final Point point3) {
        if (!this.mIsTouched) {
            return;
        }
        if (this.mListener != null) {
            if (!this.isTouched()) {
                this.mIsForceTouchCanceled = false;
                this.mListener.onCaptureAreaCanceled();
                return;
            }
            this.mListener.onCaptureAreaMoved();
        }
    }
    
    public void onSingleReleased(final Point point) {
        if (this.mIsForceTouchCanceled) {
            this.mIsTouched = false;
            this.mIsLongPressed = false;
            this.mIsForceTouchCanceled = false;
            if (this.mListener != null) {
                this.mListener.onCaptureAreaCanceled();
            }
            return;
        }
        if (!this.mIsTouched && !this.mIsLongPressed) {
            return;
        }
        this.mIsTouched = false;
        this.mIsLongPressed = false;
        if (this.mListener != null) {
            this.mListener.onCaptureAreaReleased(this.convertPointCoordinatesFromThisViewToScreen(point));
        }
    }
    
    public void onSingleReleasedInDouble(final Point point, final Point point2) {
    }
    
    public void onSingleStopped(final Point point, final Point point2, final Point point3) {
        if (!this.mIsTouched) {
            return;
        }
        if (this.mListener != null) {
            this.mListener.onCaptureAreaStopped();
        }
    }
    
    public void onSingleTapUp(final MotionEvent motionEvent) {
        if (!this.mIsTouched) {
            return;
        }
        if (this.mListener != null) {
            this.mListener.onCaptureAreaSingleTapUp(this.convertPointCoordinatesFromThisViewToScreen(new Point((int)motionEvent.getX(), (int)motionEvent.getY())));
        }
    }
    
    public void onSingleTouched(final Point point) {
        this.mIsTouched = true;
        this.mIsForceTouchCanceled = false;
        if (this.mListener != null) {
            this.mListener.onCaptureAreaTouched();
        }
    }
    
    public void release() {
        this.mUserInteractionEngine.setInteractionListener(null);
        this.mUserInteractionEngine.release();
        this.mUserInteractionEngine = null;
        this.setOnTouchListener((View$OnTouchListener)null);
    }
    
    public void setCaptureAreaStateListener(final CaptureAreaStateListener mListener) {
        this.mListener = mListener;
    }
    
    public interface CaptureAreaStateListener
    {
        void onCaptureAreaCanceled();
        
        void onCaptureAreaIsReadyToScale();
        
        void onCaptureAreaLongPressed(final Point p0);
        
        void onCaptureAreaMoved();
        
        void onCaptureAreaReleased(final Point p0);
        
        void onCaptureAreaScaled(final float p0);
        
        void onCaptureAreaSingleTapUp(final Point p0);
        
        void onCaptureAreaStopped();
        
        void onCaptureAreaTouched();
    }
    
    class CaptureAreaTouchEventListener implements View$OnTouchListener
    {
        final CaptureArea this$0;
        
        CaptureAreaTouchEventListener(final CaptureArea this$0) {
            this.this$0 = this$0;
        }
        
        public boolean onTouch(final View view, final MotionEvent motionEvent) {
            synchronized (this) {
                if (!this.this$0.mUserInteractionEngine.onTouchEvent(motionEvent)) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("touch event is out of target area");
                    }
                    this.this$0.mIsTouched = false;
                    if (this.this$0.mListener != null) {
                        this.this$0.mListener.onCaptureAreaCanceled();
                    }
                }
                return true;
            }
        }
    }
}
