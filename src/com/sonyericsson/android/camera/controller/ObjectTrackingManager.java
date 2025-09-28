package com.sonyericsson.android.camera.controller;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import com.sonyericsson.android.camera.controller.StateMachine;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.MaxVideoSize;
import com.sonyericsson.android.camera.view.ViewFinder;
import com.sonyericsson.cameracommon.utility.PositionConverter;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ObjectTrackingManager {
    public static final String TAG = "ObjectTrackingManager";
    private final CameraDeviceHandler mCameraDeviceHandler;
    private State mCurrentState = State.STOPPED;
    private final ObjectTrackingHandler mHandler = new ObjectTrackingHandler();
    private ObjectTrackingCallback mObjectTrackingCallback;
    private final StateMachine mStateMachine;
    private final ViewFinder mViewFinder;

    private enum State {
        STOPPED,
        IDLE,
        TRACKING,
        LOST
    }

    public static final void preload() {
    }

    private class ObjectTrackingCallback implements CameraParameters.ObjectTrackingCallback {
        private ObjectTrackingCallback() {
        }

        @Override // com.sonyericsson.android.camera.device.CameraParameters.ObjectTrackingCallback
        public void onObjectTracked(CameraParameters.ObjectTrackingResult objectTrackingResult) {
            if (this != ObjectTrackingManager.this.mObjectTrackingCallback) {
                return;
            }
            if (objectTrackingResult == null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("onObjectTracked: result is null.");
                    return;
                }
                return;
            }
            if (CamLog.VERBOSE) {
                CamLog.d("onObjectTracked: lost: " + objectTrackingResult.mIsLost + ", rect: " + objectTrackingResult.mRectOfTrackedObject);
            }
            switch (ObjectTrackingManager.this.mCurrentState) {
                case STOPPED:
                    return;
                case IDLE:
                    if (objectTrackingResult.mIsLost) {
                        ObjectTrackingManager.this.mCurrentState = State.LOST;
                        break;
                    } else {
                        ObjectTrackingManager.this.mCurrentState = State.TRACKING;
                        break;
                    }
                case LOST:
                    if (!objectTrackingResult.mIsLost) {
                        ObjectTrackingManager.this.mCurrentState = State.TRACKING;
                        break;
                    } else {
                        if (CamLog.VERBOSE) {
                            CamLog.d("onObjectTracked: ignore lost");
                            return;
                        }
                        return;
                    }
                case TRACKING:
                    if (objectTrackingResult.mIsLost) {
                        ObjectTrackingManager.this.mCurrentState = State.LOST;
                        break;
                    }
                    break;
            }
            if (objectTrackingResult.mIsLost) {
                ObjectTrackingManager.this.mHandler.startTimeoutCount();
                ObjectTrackingManager.this.mStateMachine.onObjectLost(objectTrackingResult);
                return;
            }
            ObjectTrackingManager.this.mHandler.stopTimeoutCount();
            if (objectTrackingResult.mRectOfTrackedObject.isEmpty()) {
                ObjectTrackingManager.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
            } else {
                ObjectTrackingManager.this.mStateMachine.onObjectTracked(objectTrackingResult);
            }
        }
    }

    public ObjectTrackingManager(ViewFinder viewFinder, CameraDeviceHandler cameraDeviceHandler, StateMachine stateMachine) {
        this.mViewFinder = viewFinder;
        this.mCameraDeviceHandler = cameraDeviceHandler;
        this.mStateMachine = stateMachine;
    }

    public void start(Rect rect) {
        if (CamLog.VERBOSE) {
            CamLog.d("start() called: " + rect);
        }
        if (rect != null) {
            switch (this.mCurrentState) {
                case STOPPED:
                    break;
                case IDLE:
                case LOST:
                case TRACKING:
                    stop();
                    break;
                default:
                    return;
            }
            startTracking(rect);
        }
    }

    private void startTracking(Rect rect) {
        if (CamLog.VERBOSE) {
            CamLog.d("startTracking E: " + rect);
        }
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_OBJECT_TRACKING_STARTED, new Object[0]);
        this.mObjectTrackingCallback = new ObjectTrackingCallback();
        this.mCameraDeviceHandler.startObjectTracking(PositionConverter.getInstance().convertFromViewToActiveArray(rect), this.mObjectTrackingCallback);
        this.mCurrentState = State.IDLE;
        if (CamLog.VERBOSE) {
            CamLog.d("startTracking X");
        }
    }

    public void stop() {
        if (CamLog.VERBOSE) {
            CamLog.d("stop() E");
        }
        this.mCameraDeviceHandler.stopObjectTracking();
        this.mHandler.stopTimeoutCount();
        this.mCurrentState = State.STOPPED;
        this.mObjectTrackingCallback = null;
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_OBJECT_TRACKING_STOP, new Object[0]);
        if (CamLog.VERBOSE) {
            CamLog.d("stop() X");
        }
    }

    private class ObjectTrackingHandler extends Handler {
        private static final int MSG_TIMEOUT_INVISIBLE = 2;
        private static final int MSG_TIMEOUT_LOST = 1;
        private static final int TIMEOUT_INVISIBLE_MILLIS = 500;
        private static final int TIMEOUT_LOST_MILLIS = 3000;

        private ObjectTrackingHandler() {
        }

        public void startTimeoutCount() {
            sendEmptyMessageDelayed(1, MaxVideoSize.GUARANTEED_MIN_DURATION_IN_MILLIS);
            sendEmptyMessageDelayed(2, 500L);
        }

        public void stopTimeoutCount() {
            removeMessages(1);
            removeMessages(2);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (ObjectTrackingManager.this.mCameraDeviceHandler.isObjectTrackingRunning()) {
                boolean z = ObjectTrackingManager.this.mStateMachine.getCurrentCaptureState() != StateMachine.CaptureState.STATE_PHOTO_AF_DONE;
                switch (message.what) {
                    case 1:
                        ObjectTrackingManager.this.stop();
                        if (z) {
                            ObjectTrackingManager.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
                        }
                        ObjectTrackingManager.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CLEAR_FOCUS, new Object[0]);
                        break;
                    case 2:
                        if (z) {
                            ObjectTrackingManager.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_OBJECT_TRACKING_TIMEOUT, new Object[0]);
                            break;
                        }
                        break;
                }
            }
        }
    }
}
