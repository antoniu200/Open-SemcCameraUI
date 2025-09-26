// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.interaction;

import android.graphics.PointF;
import com.sonyericsson.android.camera.util.CamLog;
import android.graphics.Point;
import android.view.MotionEvent;
import android.graphics.Rect;
import android.os.Handler;
import android.view.ViewConfiguration;
import android.view.View;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector$OnGestureListener;

public class TouchActionTranslator implements TouchStopDetectorListener, ScaleAndRotateDetectorListener, GestureDetector$OnGestureListener
{
    private static final TouchActionListener NULL_LISTENER;
    public static final String TAG = "TouchActionTranslator";
    private GestureDetector mAndroidGestureDetector;
    private TouchActionListener mClientListener;
    private Context mContext;
    private InteractionState mCurrentInteractionState;
    private TouchScaleAndRotateDetector mDoubleTouchScaleAndRotateDetector;
    private boolean mIsAllTouchEventInTargetArea;
    private final int mMargin;
    private TouchMoveAndStopDetector mSingleTouchMoveAndStopDetector;
    private View mTargetView;
    private final int mTouchSlop;
    
    static {
        NULL_LISTENER = (TouchActionListener)new NullInteractionListener();
    }
    
    public TouchActionTranslator(final Context context, final View view) {
        this(context, view, 0);
    }
    
    public TouchActionTranslator(final Context context, final View view, final int n) {
        this(context, view, n, ViewConfiguration.get(context).getScaledTouchSlop());
    }
    
    public TouchActionTranslator(final Context mContext, final View mTargetView, final int mMargin, final int mTouchSlop) {
        this.mIsAllTouchEventInTargetArea = true;
        this.mClientListener = TouchActionTranslator.NULL_LISTENER;
        this.mCurrentInteractionState = (InteractionState)new Idle();
        this.mContext = mContext;
        this.mTargetView = mTargetView;
        this.mMargin = mMargin;
        this.mTouchSlop = mTouchSlop;
        this.setInteractionListener(null);
        (this.mDoubleTouchScaleAndRotateDetector = new TouchScaleAndRotateDetector()).setScaleAndRotateDetectorListener((TouchScaleAndRotateDetector.ScaleAndRotateDetectorListener)this);
    }
    
    private void changeTo(final InteractionState mCurrentInteractionState) {
        synchronized (this) {
            this.mCurrentInteractionState = mCurrentInteractionState;
        }
    }
    
    private GestureDetector getAndroidGestureDetector() {
        if (this.mAndroidGestureDetector == null) {
            this.mAndroidGestureDetector = new GestureDetector(this.mContext, (GestureDetector$OnGestureListener)this, new Handler(), true);
        }
        return this.mAndroidGestureDetector;
    }
    
    private TouchMoveAndStopDetector getSingleTouchMoveAndStopDetector() {
        if (this.mSingleTouchMoveAndStopDetector == null) {
            (this.mSingleTouchMoveAndStopDetector = new TouchMoveAndStopDetector(this.mTouchSlop)).setTouchStopDetectorListener((TouchMoveAndStopDetector.TouchStopDetectorListener)this);
        }
        return this.mSingleTouchMoveAndStopDetector;
    }
    
    private boolean hitTest(final View view, final int n, final int n2, final int n3) {
        return new Rect(n, n, view.getWidth() - n, view.getHeight() - n).contains(n2, n3);
    }
    
    public void cancel() {
        this.changeTo((InteractionState)new Idle());
    }
    
    @Override
    public void onDoubleTouchRotateDetected(final float n, final float n2) {
        synchronized (this) {
            this.mCurrentInteractionState.handleTouchRotateEvent(n, n2);
        }
    }
    
    @Override
    public void onDoubleTouchScaleDetected(final float n, final float n2, final float n3) {
        synchronized (this) {
            this.mCurrentInteractionState.handleTouchScaleEvent(n, n2, n3);
        }
    }
    
    public boolean onDown(final MotionEvent motionEvent) {
        monitorenter(this);
        monitorexit(this);
        return true;
    }
    
    public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
        synchronized (this) {
            this.mClientListener.onFling(motionEvent, motionEvent2, n, n2);
            return true;
        }
    }
    
    public void onLongPress(final MotionEvent motionEvent) {
        synchronized (this) {
            this.mClientListener.onLongPress(motionEvent);
        }
    }
    
    public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
        monitorenter(this);
        monitorexit(this);
        return true;
    }
    
    public void onShowPress(final MotionEvent motionEvent) {
        synchronized (this) {
            this.mClientListener.onShowPress(motionEvent);
        }
    }
    
    public boolean onSingleTapUp(final MotionEvent motionEvent) {
        synchronized (this) {
            this.mClientListener.onSingleTapUp(motionEvent);
            return true;
        }
    }
    
    @Override
    public void onSingleTouchMoveDetected(final Point point, final Point point2, final Point point3) {
        synchronized (this) {
            this.mCurrentInteractionState.handleSingleTouchMoveEvent(point, point2, point3);
        }
    }
    
    @Override
    public void onSingleTouchStopDetected(final Point point, final Point point2, final Point point3) {
        synchronized (this) {
            this.mCurrentInteractionState.handleSingleTouchStopEvent(point, point2, point3);
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        synchronized (this) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onTouchEvent() getActionMasked:");
                sb.append(motionEvent.getActionMasked());
                sb.append(", getPointerCount:");
                sb.append(motionEvent.getPointerCount());
                CamLog.d(sb.toString());
            }
            if (this.mTargetView != null) {
                for (int i = 0; i < motionEvent.getPointerCount(); ++i) {
                    if (!this.hitTest(this.mTargetView, this.mMargin, (int)motionEvent.getX(i), (int)motionEvent.getY(i))) {
                        this.mIsAllTouchEventInTargetArea = false;
                        break;
                    }
                    this.mIsAllTouchEventInTargetArea = true;
                }
            }
            switch (motionEvent.getActionMasked()) {
                case 2: {
                    if (1 == motionEvent.getPointerCount()) {
                        this.getSingleTouchMoveAndStopDetector().updateCurrentPosition((int)motionEvent.getX(), (int)motionEvent.getY());
                        break;
                    }
                    break;
                }
                case 1:
                case 3: {
                    this.getSingleTouchMoveAndStopDetector().stopTouchStopDetection();
                    break;
                }
                case 0: {
                    this.getSingleTouchMoveAndStopDetector().startTouchStopDetection((int)motionEvent.getX(), (int)motionEvent.getY());
                    break;
                }
            }
            if (motionEvent.getActionMasked() == 1 && !this.mIsAllTouchEventInTargetArea) {
                this.cancel();
            }
            else {
                this.mCurrentInteractionState.handleMotionEvent(motionEvent);
            }
            this.getAndroidGestureDetector().onTouchEvent(motionEvent);
            return this.mIsAllTouchEventInTargetArea;
        }
    }
    
    public void release() {
        synchronized (this) {
            this.mContext = null;
            this.mTargetView = null;
            if (this.mSingleTouchMoveAndStopDetector != null) {
                this.mSingleTouchMoveAndStopDetector.release();
                this.mSingleTouchMoveAndStopDetector = null;
            }
            this.mDoubleTouchScaleAndRotateDetector.release();
            this.mDoubleTouchScaleAndRotateDetector = null;
            this.mAndroidGestureDetector = null;
            this.mClientListener = TouchActionTranslator.NULL_LISTENER;
        }
    }
    
    public void setInteractionListener(final TouchActionListener mClientListener) {
        if (mClientListener != null) {
            this.mClientListener = mClientListener;
        }
        else {
            this.mClientListener = TouchActionTranslator.NULL_LISTENER;
        }
    }
    
    private class DoubleDown implements InteractionState
    {
        final TouchActionTranslator this$0;
        
        private DoubleDown(final TouchActionTranslator this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void handleMotionEvent(final MotionEvent motionEvent) {
            final int actionMasked = motionEvent.getActionMasked();
            int n = 0;
            switch (actionMasked) {
                default: {
                    return;
                }
                case 6: {
                    if (motionEvent.getPointerCount() == 1) {
                        return;
                    }
                    final int actionIndex = motionEvent.getActionIndex();
                    if (actionIndex == 0) {
                        n = 1;
                    }
                    this.this$0.mClientListener.onSingleReleasedInDouble(new Point((int)motionEvent.getX(actionIndex), (int)motionEvent.getY(actionIndex)), new Point((int)motionEvent.getX(n), (int)motionEvent.getY(n)));
                    this.this$0.getSingleTouchMoveAndStopDetector().updateCurrentAndLastPosition((int)motionEvent.getX(n), (int)motionEvent.getY(n));
                    this.this$0.changeTo((InteractionState)new SingleMove());
                    return;
                }
                case 5: {
                    if (motionEvent.getPointerCount() >= 3) {
                        this.this$0.changeTo((InteractionState)new OverTriple());
                    }
                    return;
                }
                case 3: {
                    this.this$0.mClientListener.onDoubleCanceled();
                    this.this$0.changeTo((InteractionState)new Idle());
                    return;
                }
                case 2: {
                    if (motionEvent.getPointerCount() != 2) {
                        return;
                    }
                    final Point point = new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0));
                    final Point point2 = new Point((int)motionEvent.getX(1), (int)motionEvent.getY(1));
                    this.this$0.mClientListener.onDoubleMoved(point, point2);
                    this.this$0.changeTo((InteractionState)this.this$0.new DoubleMove(point, point2));
                }
            }
        }
        
        @Override
        public void handleSingleTouchMoveEvent(final Point point, final Point point2, final Point point3) {
        }
        
        @Override
        public void handleSingleTouchStopEvent(final Point point, final Point point2, final Point point3) {
        }
        
        @Override
        public void handleTouchRotateEvent(final float n, final float n2) {
        }
        
        @Override
        public void handleTouchScaleEvent(final float n, final float n2, final float n3) {
        }
    }
    
    private interface InteractionState
    {
        void handleMotionEvent(final MotionEvent p0);
        
        void handleSingleTouchMoveEvent(final Point p0, final Point p1, final Point p2);
        
        void handleSingleTouchStopEvent(final Point p0, final Point p1, final Point p2);
        
        void handleTouchRotateEvent(final float p0, final float p1);
        
        void handleTouchScaleEvent(final float p0, final float p1, final float p2);
    }
    
    private class DoubleMove implements InteractionState
    {
        final TouchActionTranslator this$0;
        
        DoubleMove(final TouchActionTranslator this$0, final Point point, final Point point2) {
            this.this$0 = this$0;
            this$0.mDoubleTouchScaleAndRotateDetector.startScaleAndRotateDetection(new PointF(point), new PointF(point2));
        }
        
        @Override
        public void handleMotionEvent(final MotionEvent motionEvent) {
            final int actionMasked = motionEvent.getActionMasked();
            int n = 0;
            switch (actionMasked) {
                default: {
                    return;
                }
                case 6: {
                    this.this$0.mDoubleTouchScaleAndRotateDetector.stopScaleAndRotateDetection();
                    final int actionIndex = motionEvent.getActionIndex();
                    if (actionIndex == 0) {
                        n = 1;
                    }
                    this.this$0.mClientListener.onSingleReleasedInDouble(new Point((int)motionEvent.getX(actionIndex), (int)motionEvent.getY(actionIndex)), new Point((int)motionEvent.getX(n), (int)motionEvent.getY(n)));
                    this.this$0.getSingleTouchMoveAndStopDetector().updateCurrentAndLastPosition((int)motionEvent.getX(n), (int)motionEvent.getY(n));
                    this.this$0.changeTo((InteractionState)new SingleMove());
                    return;
                }
                case 5: {
                    this.this$0.mDoubleTouchScaleAndRotateDetector.stopScaleAndRotateDetection();
                    this.this$0.changeTo((InteractionState)new OverTriple());
                    return;
                }
                case 3: {
                    this.this$0.mClientListener.onDoubleCanceled();
                    this.this$0.changeTo((InteractionState)new Idle());
                    return;
                }
                case 2: {
                    if (motionEvent.getPointerCount() != 2) {
                        return;
                    }
                    this.this$0.mDoubleTouchScaleAndRotateDetector.updateCurrentPosition(new PointF(motionEvent.getX(0), motionEvent.getY(0)), new PointF(motionEvent.getX(1), motionEvent.getY(1)));
                    this.this$0.mClientListener.onDoubleMoved(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)), new Point((int)motionEvent.getX(1), (int)motionEvent.getY(1)));
                }
            }
        }
        
        @Override
        public void handleSingleTouchMoveEvent(final Point point, final Point point2, final Point point3) {
        }
        
        @Override
        public void handleSingleTouchStopEvent(final Point point, final Point point2, final Point point3) {
        }
        
        @Override
        public void handleTouchRotateEvent(final float n, final float n2) {
            this.this$0.mClientListener.onDoubleRotated(n, n2);
        }
        
        @Override
        public void handleTouchScaleEvent(final float n, final float n2, final float n3) {
            this.this$0.mClientListener.onDoubleScaled(n, n2, n3);
        }
    }
    
    private class Idle implements InteractionState
    {
        final TouchActionTranslator this$0;
        
        private Idle(final TouchActionTranslator this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void handleMotionEvent(final MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 0) {
                return;
            }
            this.this$0.mClientListener.onSingleTouched(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)));
            this.this$0.changeTo((InteractionState)new SingleDown());
        }
        
        @Override
        public void handleSingleTouchMoveEvent(final Point point, final Point point2, final Point point3) {
        }
        
        @Override
        public void handleSingleTouchStopEvent(final Point point, final Point point2, final Point point3) {
        }
        
        @Override
        public void handleTouchRotateEvent(final float n, final float n2) {
        }
        
        @Override
        public void handleTouchScaleEvent(final float n, final float n2, final float n3) {
        }
    }
    
    private static final class NullInteractionListener implements TouchActionListener
    {
        @Override
        public void onDoubleCanceled() {
        }
        
        @Override
        public void onDoubleMoved(final Point point, final Point point2) {
        }
        
        @Override
        public void onDoubleRotated(final float n, final float n2) {
        }
        
        @Override
        public void onDoubleScaled(final float n, final float n2, final float n3) {
        }
        
        @Override
        public void onDoubleTouched(final Point point, final Point point2) {
        }
        
        @Override
        public void onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
        }
        
        @Override
        public void onLongPress(final MotionEvent motionEvent) {
        }
        
        @Override
        public void onOverTripleCanceled() {
        }
        
        @Override
        public void onShowPress(final MotionEvent motionEvent) {
        }
        
        @Override
        public void onSingleCanceled() {
        }
        
        @Override
        public void onSingleMoved(final Point point, final Point point2, final Point point3) {
        }
        
        @Override
        public void onSingleReleased(final Point point) {
        }
        
        @Override
        public void onSingleReleasedInDouble(final Point point, final Point point2) {
        }
        
        @Override
        public void onSingleStopped(final Point point, final Point point2, final Point point3) {
        }
        
        @Override
        public void onSingleTapUp(final MotionEvent motionEvent) {
        }
        
        @Override
        public void onSingleTouched(final Point point) {
        }
    }
    
    private class OverTriple implements InteractionState
    {
        final TouchActionTranslator this$0;
        
        private OverTriple(final TouchActionTranslator this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void handleMotionEvent(final MotionEvent motionEvent) {
            final int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 3) {
                this.this$0.mClientListener.onOverTripleCanceled();
                this.this$0.changeTo((InteractionState)new Idle());
                return;
            }
            if (actionMasked != 6) {
                return;
            }
            if (motionEvent.getPointerCount() == 3) {
                this.this$0.changeTo((InteractionState)new DoubleDown());
            }
        }
        
        @Override
        public void handleSingleTouchMoveEvent(final Point point, final Point point2, final Point point3) {
        }
        
        @Override
        public void handleSingleTouchStopEvent(final Point point, final Point point2, final Point point3) {
        }
        
        @Override
        public void handleTouchRotateEvent(final float n, final float n2) {
        }
        
        @Override
        public void handleTouchScaleEvent(final float n, final float n2, final float n3) {
        }
    }
    
    private class SingleDown implements InteractionState
    {
        final TouchActionTranslator this$0;
        
        private SingleDown(final TouchActionTranslator this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void handleMotionEvent(final MotionEvent motionEvent) {
            final int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 5) {
                switch (actionMasked) {
                    default: {
                        return;
                    }
                    case 3: {
                        this.this$0.mClientListener.onSingleCanceled();
                        this.this$0.changeTo((InteractionState)new Idle());
                        return;
                    }
                    case 2: {
                        this.this$0.changeTo((InteractionState)new SingleMove());
                        return;
                    }
                    case 1: {
                        this.this$0.mClientListener.onSingleReleased(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)));
                        this.this$0.changeTo((InteractionState)new Idle());
                    }
                }
            }
            else {
                if (motionEvent.getPointerCount() == 1) {
                    return;
                }
                this.this$0.mClientListener.onDoubleTouched(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)), new Point((int)motionEvent.getX(1), (int)motionEvent.getY(1)));
                this.this$0.changeTo((InteractionState)new DoubleDown());
            }
        }
        
        @Override
        public void handleSingleTouchMoveEvent(final Point point, final Point point2, final Point point3) {
        }
        
        @Override
        public void handleSingleTouchStopEvent(final Point point, final Point point2, final Point point3) {
        }
        
        @Override
        public void handleTouchRotateEvent(final float n, final float n2) {
        }
        
        @Override
        public void handleTouchScaleEvent(final float n, final float n2, final float n3) {
        }
    }
    
    private class SingleMove implements InteractionState
    {
        final TouchActionTranslator this$0;
        
        private SingleMove(final TouchActionTranslator this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void handleMotionEvent(final MotionEvent motionEvent) {
            final int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 1) {
                this.this$0.mClientListener.onSingleReleased(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)));
                this.this$0.changeTo((InteractionState)new Idle());
                return;
            }
            if (actionMasked == 3) {
                this.this$0.mClientListener.onSingleCanceled();
                this.this$0.changeTo((InteractionState)new Idle());
                return;
            }
            if (actionMasked != 5) {
                return;
            }
            if (motionEvent.getPointerCount() == 1) {
                return;
            }
            this.this$0.mClientListener.onDoubleTouched(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)), new Point((int)motionEvent.getX(1), (int)motionEvent.getY(1)));
            this.this$0.changeTo((InteractionState)new DoubleDown());
        }
        
        @Override
        public void handleSingleTouchMoveEvent(final Point point, final Point point2, final Point point3) {
            this.this$0.mClientListener.onSingleMoved(point, point2, point3);
        }
        
        @Override
        public void handleSingleTouchStopEvent(final Point point, final Point point2, final Point point3) {
            this.this$0.mClientListener.onSingleStopped(point, point2, point3);
            this.this$0.changeTo((InteractionState)new SingleStop());
        }
        
        @Override
        public void handleTouchRotateEvent(final float n, final float n2) {
        }
        
        @Override
        public void handleTouchScaleEvent(final float n, final float n2, final float n3) {
        }
    }
    
    private class SingleStop implements InteractionState
    {
        final TouchActionTranslator this$0;
        
        private SingleStop(final TouchActionTranslator this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void handleMotionEvent(final MotionEvent motionEvent) {
            final int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 1) {
                this.this$0.mClientListener.onSingleReleased(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)));
                this.this$0.changeTo((InteractionState)new Idle());
                return;
            }
            if (actionMasked == 3) {
                this.this$0.mClientListener.onSingleCanceled();
                this.this$0.changeTo((InteractionState)new Idle());
                return;
            }
            if (actionMasked != 5) {
                return;
            }
            if (motionEvent.getPointerCount() == 1) {
                return;
            }
            this.this$0.mClientListener.onDoubleTouched(new Point((int)motionEvent.getX(0), (int)motionEvent.getY(0)), new Point((int)motionEvent.getX(1), (int)motionEvent.getY(1)));
            this.this$0.changeTo((InteractionState)new DoubleDown());
        }
        
        @Override
        public void handleSingleTouchMoveEvent(final Point point, final Point point2, final Point point3) {
            this.this$0.mClientListener.onSingleMoved(point, point2, point3);
            this.this$0.changeTo((InteractionState)new SingleMove());
        }
        
        @Override
        public void handleSingleTouchStopEvent(final Point point, final Point point2, final Point point3) {
        }
        
        @Override
        public void handleTouchRotateEvent(final float n, final float n2) {
        }
        
        @Override
        public void handleTouchScaleEvent(final float n, final float n2, final float n3) {
        }
    }
    
    public interface TouchActionListener
    {
        void onDoubleCanceled();
        
        void onDoubleMoved(final Point p0, final Point p1);
        
        void onDoubleRotated(final float p0, final float p1);
        
        void onDoubleScaled(final float p0, final float p1, final float p2);
        
        void onDoubleTouched(final Point p0, final Point p1);
        
        void onFling(final MotionEvent p0, final MotionEvent p1, final float p2, final float p3);
        
        void onLongPress(final MotionEvent p0);
        
        void onOverTripleCanceled();
        
        void onShowPress(final MotionEvent p0);
        
        void onSingleCanceled();
        
        void onSingleMoved(final Point p0, final Point p1, final Point p2);
        
        void onSingleReleased(final Point p0);
        
        void onSingleReleasedInDouble(final Point p0, final Point p1);
        
        void onSingleStopped(final Point p0, final Point p1, final Point p2);
        
        void onSingleTapUp(final MotionEvent p0);
        
        void onSingleTouched(final Point p0);
    }
}
