package com.sonyericsson.cameracommon.interaction;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.interaction.TouchMoveAndStopDetector;
import com.sonyericsson.cameracommon.interaction.TouchScaleAndRotateDetector;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TouchActionTranslator implements TouchMoveAndStopDetector.TouchStopDetectorListener, TouchScaleAndRotateDetector.ScaleAndRotateDetectorListener, GestureDetector.OnGestureListener {
    private static final TouchActionListener NULL_LISTENER = new NullInteractionListener();
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

    private interface InteractionState {
        void handleMotionEvent(MotionEvent motionEvent);

        void handleSingleTouchMoveEvent(Point point, Point point2, Point point3);

        void handleSingleTouchStopEvent(Point point, Point point2, Point point3);

        void handleTouchRotateEvent(float f, float f2);

        void handleTouchScaleEvent(float f, float f2, float f3);
    }

    public interface TouchActionListener {
        void onDoubleCanceled();

        void onDoubleMoved(Point point, Point point2);

        void onDoubleRotated(float f, float f2);

        void onDoubleScaled(float f, float f2, float f3);

        void onDoubleTouched(Point point, Point point2);

        void onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onLongPress(MotionEvent motionEvent);

        void onOverTripleCanceled();

        void onShowPress(MotionEvent motionEvent);

        void onSingleCanceled();

        void onSingleMoved(Point point, Point point2, Point point3);

        void onSingleReleased(Point point);

        void onSingleReleasedInDouble(Point point, Point point2);

        void onSingleStopped(Point point, Point point2, Point point3);

        void onSingleTapUp(MotionEvent motionEvent);

        void onSingleTouched(Point point);
    }

    public void setInteractionListener(TouchActionListener touchActionListener) {
        if (touchActionListener != null) {
            this.mClientListener = touchActionListener;
        } else {
            this.mClientListener = NULL_LISTENER;
        }
    }

    private static final class NullInteractionListener implements TouchActionListener {
        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onDoubleCanceled() {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onDoubleMoved(Point point, Point point2) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onDoubleRotated(float f, float f2) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onDoubleScaled(float f, float f2, float f3) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onDoubleTouched(Point point, Point point2) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onOverTripleCanceled() {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onSingleCanceled() {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onSingleMoved(Point point, Point point2, Point point3) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onSingleReleased(Point point) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onSingleReleasedInDouble(Point point, Point point2) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onSingleStopped(Point point, Point point2, Point point3) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onSingleTapUp(MotionEvent motionEvent) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.TouchActionListener
        public void onSingleTouched(Point point) {
        }

        private NullInteractionListener() {
        }
    }

    public TouchActionTranslator(Context context, View view) {
        this(context, view, 0);
    }

    public TouchActionTranslator(Context context, View view, int i) {
        this(context, view, i, ViewConfiguration.get(context).getScaledTouchSlop());
    }

    public TouchActionTranslator(Context context, View view, int i, int i2) {
        this.mIsAllTouchEventInTargetArea = true;
        this.mClientListener = NULL_LISTENER;
        this.mCurrentInteractionState = new Idle();
        this.mContext = context;
        this.mTargetView = view;
        this.mMargin = i;
        this.mTouchSlop = i2;
        setInteractionListener(null);
        this.mDoubleTouchScaleAndRotateDetector = new TouchScaleAndRotateDetector();
        this.mDoubleTouchScaleAndRotateDetector.setScaleAndRotateDetectorListener(this);
    }

    public synchronized void release() {
        this.mContext = null;
        this.mTargetView = null;
        if (this.mSingleTouchMoveAndStopDetector != null) {
            this.mSingleTouchMoveAndStopDetector.release();
            this.mSingleTouchMoveAndStopDetector = null;
        }
        this.mDoubleTouchScaleAndRotateDetector.release();
        this.mDoubleTouchScaleAndRotateDetector = null;
        this.mAndroidGestureDetector = null;
        this.mClientListener = NULL_LISTENER;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void changeTo(InteractionState interactionState) {
        this.mCurrentInteractionState = interactionState;
    }

    private class Idle implements InteractionState {
        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchRotateEvent(float f, float f2) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchScaleEvent(float f, float f2, float f3) {
        }

        private Idle() {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleMotionEvent(MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 0) {
                return;
            }
            TouchActionTranslator.this.mClientListener.onSingleTouched(new Point((int) motionEvent.getX(0), (int) motionEvent.getY(0)));
            TouchActionTranslator.this.changeTo(new SingleDown());
        }
    }

    private class SingleDown implements InteractionState {
        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchRotateEvent(float f, float f2) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchScaleEvent(float f, float f2, float f3) {
        }

        private SingleDown() {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleMotionEvent(MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 5) {
                switch (actionMasked) {
                    case 1:
                        TouchActionTranslator.this.mClientListener.onSingleReleased(new Point((int) motionEvent.getX(0), (int) motionEvent.getY(0)));
                        TouchActionTranslator.this.changeTo(new Idle());
                        break;
                    case 2:
                        TouchActionTranslator.this.changeTo(new SingleMove());
                        break;
                    case 3:
                        TouchActionTranslator.this.mClientListener.onSingleCanceled();
                        TouchActionTranslator.this.changeTo(new Idle());
                        break;
                }
                return;
            }
            if (motionEvent.getPointerCount() == 1) {
                return;
            }
            TouchActionTranslator.this.mClientListener.onDoubleTouched(new Point((int) motionEvent.getX(0), (int) motionEvent.getY(0)), new Point((int) motionEvent.getX(1), (int) motionEvent.getY(1)));
            TouchActionTranslator.this.changeTo(new DoubleDown());
        }
    }

    private class SingleMove implements InteractionState {
        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchRotateEvent(float f, float f2) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchScaleEvent(float f, float f2, float f3) {
        }

        private SingleMove() {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleMotionEvent(MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 1) {
                TouchActionTranslator.this.mClientListener.onSingleReleased(new Point((int) motionEvent.getX(0), (int) motionEvent.getY(0)));
                TouchActionTranslator.this.changeTo(new Idle());
            } else if (actionMasked == 3) {
                TouchActionTranslator.this.mClientListener.onSingleCanceled();
                TouchActionTranslator.this.changeTo(new Idle());
            } else if (actionMasked == 5 && motionEvent.getPointerCount() != 1) {
                TouchActionTranslator.this.mClientListener.onDoubleTouched(new Point((int) motionEvent.getX(0), (int) motionEvent.getY(0)), new Point((int) motionEvent.getX(1), (int) motionEvent.getY(1)));
                TouchActionTranslator.this.changeTo(new DoubleDown());
            }
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
            TouchActionTranslator.this.mClientListener.onSingleMoved(point, point2, point3);
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
            TouchActionTranslator.this.mClientListener.onSingleStopped(point, point2, point3);
            TouchActionTranslator.this.changeTo(new SingleStop());
        }
    }

    private class SingleStop implements InteractionState {
        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchRotateEvent(float f, float f2) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchScaleEvent(float f, float f2, float f3) {
        }

        private SingleStop() {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleMotionEvent(MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 1) {
                TouchActionTranslator.this.mClientListener.onSingleReleased(new Point((int) motionEvent.getX(0), (int) motionEvent.getY(0)));
                TouchActionTranslator.this.changeTo(new Idle());
            } else if (actionMasked == 3) {
                TouchActionTranslator.this.mClientListener.onSingleCanceled();
                TouchActionTranslator.this.changeTo(new Idle());
            } else if (actionMasked == 5 && motionEvent.getPointerCount() != 1) {
                TouchActionTranslator.this.mClientListener.onDoubleTouched(new Point((int) motionEvent.getX(0), (int) motionEvent.getY(0)), new Point((int) motionEvent.getX(1), (int) motionEvent.getY(1)));
                TouchActionTranslator.this.changeTo(new DoubleDown());
            }
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
            TouchActionTranslator.this.mClientListener.onSingleMoved(point, point2, point3);
            TouchActionTranslator.this.changeTo(new SingleMove());
        }
    }

    private class DoubleDown implements InteractionState {
        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchRotateEvent(float f, float f2) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchScaleEvent(float f, float f2, float f3) {
        }

        private DoubleDown() {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleMotionEvent(MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                case 2:
                    if (motionEvent.getPointerCount() == 2) {
                        Point point = new Point((int) motionEvent.getX(0), (int) motionEvent.getY(0));
                        Point point2 = new Point((int) motionEvent.getX(1), (int) motionEvent.getY(1));
                        TouchActionTranslator.this.mClientListener.onDoubleMoved(point, point2);
                        TouchActionTranslator.this.changeTo(TouchActionTranslator.this.new DoubleMove(point, point2));
                        break;
                    }
                    break;
                case 3:
                    TouchActionTranslator.this.mClientListener.onDoubleCanceled();
                    TouchActionTranslator.this.changeTo(new Idle());
                    break;
                case 5:
                    if (motionEvent.getPointerCount() >= 3) {
                        TouchActionTranslator.this.changeTo(new OverTriple());
                        break;
                    }
                    break;
                case 6:
                    if (motionEvent.getPointerCount() != 1) {
                        int actionIndex = motionEvent.getActionIndex();
                        int i = actionIndex == 0 ? 1 : 0;
                        TouchActionTranslator.this.mClientListener.onSingleReleasedInDouble(new Point((int) motionEvent.getX(actionIndex), (int) motionEvent.getY(actionIndex)), new Point((int) motionEvent.getX(i), (int) motionEvent.getY(i)));
                        TouchActionTranslator.this.getSingleTouchMoveAndStopDetector().updateCurrentAndLastPosition((int) motionEvent.getX(i), (int) motionEvent.getY(i));
                        TouchActionTranslator.this.changeTo(new SingleMove());
                        break;
                    }
                    break;
            }
        }
    }

    private class DoubleMove implements InteractionState {
        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
        }

        DoubleMove(Point point, Point point2) {
            TouchActionTranslator.this.mDoubleTouchScaleAndRotateDetector.startScaleAndRotateDetection(new PointF(point), new PointF(point2));
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleMotionEvent(MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                case 2:
                    if (motionEvent.getPointerCount() == 2) {
                        TouchActionTranslator.this.mDoubleTouchScaleAndRotateDetector.updateCurrentPosition(new PointF(motionEvent.getX(0), motionEvent.getY(0)), new PointF(motionEvent.getX(1), motionEvent.getY(1)));
                        TouchActionTranslator.this.mClientListener.onDoubleMoved(new Point((int) motionEvent.getX(0), (int) motionEvent.getY(0)), new Point((int) motionEvent.getX(1), (int) motionEvent.getY(1)));
                        break;
                    }
                    break;
                case 3:
                    TouchActionTranslator.this.mClientListener.onDoubleCanceled();
                    TouchActionTranslator.this.changeTo(new Idle());
                    break;
                case 5:
                    TouchActionTranslator.this.mDoubleTouchScaleAndRotateDetector.stopScaleAndRotateDetection();
                    TouchActionTranslator.this.changeTo(new OverTriple());
                    break;
                case 6:
                    TouchActionTranslator.this.mDoubleTouchScaleAndRotateDetector.stopScaleAndRotateDetection();
                    int actionIndex = motionEvent.getActionIndex();
                    int i = actionIndex == 0 ? 1 : 0;
                    TouchActionTranslator.this.mClientListener.onSingleReleasedInDouble(new Point((int) motionEvent.getX(actionIndex), (int) motionEvent.getY(actionIndex)), new Point((int) motionEvent.getX(i), (int) motionEvent.getY(i)));
                    TouchActionTranslator.this.getSingleTouchMoveAndStopDetector().updateCurrentAndLastPosition((int) motionEvent.getX(i), (int) motionEvent.getY(i));
                    TouchActionTranslator.this.changeTo(new SingleMove());
                    break;
            }
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchScaleEvent(float f, float f2, float f3) {
            TouchActionTranslator.this.mClientListener.onDoubleScaled(f, f2, f3);
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchRotateEvent(float f, float f2) {
            TouchActionTranslator.this.mClientListener.onDoubleRotated(f, f2);
        }
    }

    private class OverTriple implements InteractionState {
        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchMoveEvent(Point point, Point point2, Point point3) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleSingleTouchStopEvent(Point point, Point point2, Point point3) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchRotateEvent(float f, float f2) {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleTouchScaleEvent(float f, float f2, float f3) {
        }

        private OverTriple() {
        }

        @Override // com.sonyericsson.cameracommon.interaction.TouchActionTranslator.InteractionState
        public void handleMotionEvent(MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 3) {
                TouchActionTranslator.this.mClientListener.onOverTripleCanceled();
                TouchActionTranslator.this.changeTo(new Idle());
            } else if (actionMasked == 6 && motionEvent.getPointerCount() == 3) {
                TouchActionTranslator.this.changeTo(new DoubleDown());
            }
        }
    }

    public synchronized boolean onTouchEvent(MotionEvent motionEvent) {
        if (CamLog.VERBOSE) {
            CamLog.d("onTouchEvent() getActionMasked:" + motionEvent.getActionMasked() + ", getPointerCount:" + motionEvent.getPointerCount());
        }
        if (this.mTargetView != null) {
            int i = 0;
            while (true) {
                if (i < motionEvent.getPointerCount()) {
                    if (hitTest(this.mTargetView, this.mMargin, (int) motionEvent.getX(i), (int) motionEvent.getY(i))) {
                        this.mIsAllTouchEventInTargetArea = true;
                        i++;
                    } else {
                        this.mIsAllTouchEventInTargetArea = false;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        switch (motionEvent.getActionMasked()) {
            case 0:
                getSingleTouchMoveAndStopDetector().startTouchStopDetection((int) motionEvent.getX(), (int) motionEvent.getY());
                break;
            case 1:
            case 3:
                getSingleTouchMoveAndStopDetector().stopTouchStopDetection();
                break;
            case 2:
                if (1 == motionEvent.getPointerCount()) {
                    getSingleTouchMoveAndStopDetector().updateCurrentPosition((int) motionEvent.getX(), (int) motionEvent.getY());
                    break;
                }
                break;
        }
        if (motionEvent.getActionMasked() != 1 || this.mIsAllTouchEventInTargetArea) {
            this.mCurrentInteractionState.handleMotionEvent(motionEvent);
        } else {
            cancel();
        }
        getAndroidGestureDetector().onTouchEvent(motionEvent);
        return this.mIsAllTouchEventInTargetArea;
    }

    private boolean hitTest(View view, int i, int i2, int i3) {
        return new Rect(i, i, view.getWidth() - i, view.getHeight() - i).contains(i2, i3);
    }

    @Override // com.sonyericsson.cameracommon.interaction.TouchMoveAndStopDetector.TouchStopDetectorListener
    public synchronized void onSingleTouchMoveDetected(Point point, Point point2, Point point3) {
        this.mCurrentInteractionState.handleSingleTouchMoveEvent(point, point2, point3);
    }

    @Override // com.sonyericsson.cameracommon.interaction.TouchMoveAndStopDetector.TouchStopDetectorListener
    public synchronized void onSingleTouchStopDetected(Point point, Point point2, Point point3) {
        this.mCurrentInteractionState.handleSingleTouchStopEvent(point, point2, point3);
    }

    @Override // com.sonyericsson.cameracommon.interaction.TouchScaleAndRotateDetector.ScaleAndRotateDetectorListener
    public synchronized void onDoubleTouchScaleDetected(float f, float f2, float f3) {
        this.mCurrentInteractionState.handleTouchScaleEvent(f, f2, f3);
    }

    @Override // com.sonyericsson.cameracommon.interaction.TouchScaleAndRotateDetector.ScaleAndRotateDetectorListener
    public synchronized void onDoubleTouchRotateDetected(float f, float f2) {
        this.mCurrentInteractionState.handleTouchRotateEvent(f, f2);
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public synchronized boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public synchronized boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.mClientListener.onFling(motionEvent, motionEvent2, f, f2);
        return true;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public synchronized void onLongPress(MotionEvent motionEvent) {
        this.mClientListener.onLongPress(motionEvent);
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public synchronized boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return true;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public synchronized void onShowPress(MotionEvent motionEvent) {
        this.mClientListener.onShowPress(motionEvent);
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public synchronized boolean onSingleTapUp(MotionEvent motionEvent) {
        this.mClientListener.onSingleTapUp(motionEvent);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TouchMoveAndStopDetector getSingleTouchMoveAndStopDetector() {
        if (this.mSingleTouchMoveAndStopDetector == null) {
            this.mSingleTouchMoveAndStopDetector = new TouchMoveAndStopDetector(this.mTouchSlop);
            this.mSingleTouchMoveAndStopDetector.setTouchStopDetectorListener(this);
        }
        return this.mSingleTouchMoveAndStopDetector;
    }

    private GestureDetector getAndroidGestureDetector() {
        if (this.mAndroidGestureDetector == null) {
            this.mAndroidGestureDetector = new GestureDetector(this.mContext, this, new Handler(), true);
        }
        return this.mAndroidGestureDetector;
    }

    public void cancel() {
        changeTo(new Idle());
    }
}
