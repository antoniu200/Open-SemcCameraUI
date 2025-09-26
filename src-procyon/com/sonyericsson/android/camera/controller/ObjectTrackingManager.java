// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.controller;

import android.os.Message;
import android.os.Handler;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.android.camera.util.CamLog;
import android.graphics.Rect;
import com.sonyericsson.android.camera.view.ViewFinder;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;

public class ObjectTrackingManager
{
    public static final String TAG = "ObjectTrackingManager";
    private final CameraDeviceHandler mCameraDeviceHandler;
    private State mCurrentState;
    private final ObjectTrackingHandler mHandler;
    private ObjectTrackingCallback mObjectTrackingCallback;
    private final StateMachine mStateMachine;
    private final ViewFinder mViewFinder;
    
    public ObjectTrackingManager(final ViewFinder mViewFinder, final CameraDeviceHandler mCameraDeviceHandler, final StateMachine mStateMachine) {
        this.mCurrentState = State.STOPPED;
        this.mHandler = new ObjectTrackingHandler();
        this.mViewFinder = mViewFinder;
        this.mCameraDeviceHandler = mCameraDeviceHandler;
        this.mStateMachine = mStateMachine;
    }
    
    public static final void preload() {
    }
    
    private void startTracking(final Rect obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("startTracking E: ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_OBJECT_TRACKING_STARTED, new Object[0]);
        this.mObjectTrackingCallback = new ObjectTrackingCallback();
        this.mCameraDeviceHandler.startObjectTracking(PositionConverter.getInstance().convertFromViewToActiveArray(obj), this.mObjectTrackingCallback);
        this.mCurrentState = State.IDLE;
        if (CamLog.VERBOSE) {
            CamLog.d("startTracking X");
        }
    }
    
    public void start(final Rect obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("start() called: ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        if (obj != null) {
            switch (ObjectTrackingManager$1.$SwitchMap$com$sonyericsson$android$camera$controller$ObjectTrackingManager$State[this.mCurrentState.ordinal()]) {
                case 2:
                case 3:
                case 4: {
                    this.stop();
                }
                case 1: {
                    this.startTracking(obj);
                    break;
                }
            }
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
    
    private class ObjectTrackingCallback implements CameraParameters.ObjectTrackingCallback
    {
        final ObjectTrackingManager this$0;
        
        private ObjectTrackingCallback(final ObjectTrackingManager this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onObjectTracked(final ObjectTrackingResult objectTrackingResult) {
            if (this != this.this$0.mObjectTrackingCallback) {
                return;
            }
            if (objectTrackingResult == null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("onObjectTracked: result is null.");
                }
                return;
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onObjectTracked: lost: ");
                sb.append(objectTrackingResult.mIsLost);
                sb.append(", rect: ");
                sb.append(objectTrackingResult.mRectOfTrackedObject);
                CamLog.d(sb.toString());
            }
            switch (ObjectTrackingManager$1.$SwitchMap$com$sonyericsson$android$camera$controller$ObjectTrackingManager$State[this.this$0.mCurrentState.ordinal()]) {
                case 4: {
                    if (objectTrackingResult.mIsLost) {
                        this.this$0.mCurrentState = State.LOST;
                        break;
                    }
                    break;
                }
                case 3: {
                    if (objectTrackingResult.mIsLost) {
                        if (CamLog.VERBOSE) {
                            CamLog.d("onObjectTracked: ignore lost");
                        }
                        return;
                    }
                    this.this$0.mCurrentState = State.TRACKING;
                    break;
                }
                case 2: {
                    if (objectTrackingResult.mIsLost) {
                        this.this$0.mCurrentState = State.LOST;
                        break;
                    }
                    this.this$0.mCurrentState = State.TRACKING;
                    break;
                }
                case 1: {
                    return;
                }
            }
            if (objectTrackingResult.mIsLost) {
                this.this$0.mHandler.startTimeoutCount();
                this.this$0.mStateMachine.onObjectLost(objectTrackingResult);
            }
            else {
                this.this$0.mHandler.stopTimeoutCount();
                if (objectTrackingResult.mRectOfTrackedObject.isEmpty()) {
                    this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
                }
                else {
                    this.this$0.mStateMachine.onObjectTracked(objectTrackingResult);
                }
            }
        }
    }
    
    private class ObjectTrackingHandler extends Handler
    {
        private static final int MSG_TIMEOUT_INVISIBLE = 2;
        private static final int MSG_TIMEOUT_LOST = 1;
        private static final int TIMEOUT_INVISIBLE_MILLIS = 500;
        private static final int TIMEOUT_LOST_MILLIS = 3000;
        final ObjectTrackingManager this$0;
        
        private ObjectTrackingHandler(final ObjectTrackingManager this$0) {
            this.this$0 = this$0;
        }
        
        public void handleMessage(final Message message) {
            if (!this.this$0.mCameraDeviceHandler.isObjectTrackingRunning()) {
                return;
            }
            boolean b = true;
            if (this.this$0.mStateMachine.getCurrentCaptureState() == StateMachine.CaptureState.STATE_PHOTO_AF_DONE) {
                b = false;
            }
            switch (message.what) {
                case 2: {
                    if (b) {
                        this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_OBJECT_TRACKING_TIMEOUT, new Object[0]);
                        break;
                    }
                    break;
                }
                case 1: {
                    this.this$0.stop();
                    if (b) {
                        this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
                    }
                    this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CLEAR_FOCUS, new Object[0]);
                    break;
                }
            }
        }
        
        public void startTimeoutCount() {
            this.sendEmptyMessageDelayed(1, 3000L);
            this.sendEmptyMessageDelayed(2, 500L);
        }
        
        public void stopTimeoutCount() {
            this.removeMessages(1);
            this.removeMessages(2);
        }
    }
    
    private enum State
    {
        private static final State[] $VALUES;
        
        IDLE, 
        LOST, 
        STOPPED, 
        TRACKING;
        
        static {
            $VALUES = new State[] { State.STOPPED, State.IDLE, State.TRACKING, State.LOST };
        }
    }
}
