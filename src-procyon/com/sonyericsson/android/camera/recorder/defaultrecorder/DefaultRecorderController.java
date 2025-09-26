// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.defaultrecorder;

import android.util.Pair;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.CountDownLatch;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.recorder.RecorderParameters;
import android.os.Handler;
import com.sonyericsson.android.camera.recorder.RecorderController;
import com.sonyericsson.android.camera.recorder.RecorderInterface;
import com.sonyericsson.android.camera.device.CameraActionSound;
import android.content.Context;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;
import com.sonyericsson.android.camera.recorder.utility.Accessor;

public class DefaultRecorderController extends BaseRecorderController
{
    private static final long STOP_PROCESS_INTERVAL_MILLISECONDS = 100L;
    private static final boolean TRACE = true;
    private final Accessor<BypassCamera> mBypassCamera;
    private final CallbackLock mPrepareVideoRecordingCallbackLock;
    private final CallbackLock mStartVideoRecordingCallbackLock;
    private final CallbackLock mStopVideoRecordingCallbackLock;
    
    public DefaultRecorderController(final Context context, final Accessor<CameraActionSound> accessor, final Accessor<BypassCamera> mBypassCamera, final RecorderInterface recorderInterface, final RecorderListener recorderListener, final long n, final Handler handler, final int n2, final Handler handler2, final boolean b, final boolean b2, final boolean b3, final boolean b4, final boolean b5) {
        super(context, accessor, recorderInterface, handler, recorderListener, n, n2, handler2, b, b2, b3, b4);
        trace("DefaultRecorderController() E");
        this.mBypassCamera = mBypassCamera;
        this.mPrepareVideoRecordingCallbackLock = new CallbackLock();
        this.mStartVideoRecordingCallbackLock = new CallbackLock();
        this.mStopVideoRecordingCallbackLock = new CallbackLock();
        if (b5) {
            this.disableAdjustRecordingTimeByRecorderNotification();
        }
        trace("DefaultRecorderController() X");
    }
    
    private BypassCamera.DataSpace convertDataSpace(final RecorderParameters.DataSpace dataSpace) {
        return new BypassCamera.DataSpace(dataSpace.standard, dataSpace.transfer, dataSpace.range);
    }
    
    private static void trace(final String s) {
        CamLog.d(s);
    }
    
    protected BypassCamera getBypassCamera() {
        return this.mBypassCamera.get();
    }
    
    protected boolean prepareBypassCamera(final RecorderParameters recorderParameters) {
        trace("prepareBypassCamera() E");
        final CountDownLatch requestLatch = this.mPrepareVideoRecordingCallbackLock.requestLatch();
        try {
            try {
                this.getBypassCamera().requestPrepareVideoRecording(this.getRecorder().getSurface(), new BypassCamera.RecordingParameters(this.convertDataSpace(recorderParameters.dataSpace())));
                requestLatch.await();
                this.mPrepareVideoRecordingCallbackLock.release();
                trace("prepareBypassCamera() X");
                return true;
            }
            finally {}
        }
        catch (final RuntimeException | InterruptedException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("prepareBypassCamera() X failed : ");
            sb.append(((Throwable)ex).getMessage());
            trace(sb.toString());
            this.mPrepareVideoRecordingCallbackLock.release();
            return false;
        }
        this.mPrepareVideoRecordingCallbackLock.release();
    }
    
    protected boolean prepareCallBack() {
        trace("prepareCallBack() E");
        if (this.getBypassCamera() == null) {
            trace("prepareCallBack() X failed.");
            return false;
        }
        this.getBypassCamera().setVideoCallbacks((BypassCamera.PrepareVideoRecordingCallback)new PrepareVideoRecordingCallbackImpl(this.mPrepareVideoRecordingCallbackLock), (BypassCamera.StartVideoRecordingCallback)new StartVideoRecordingCallbackImpl(this.mStartVideoRecordingCallbackLock), (BypassCamera.StopVideoRecordingCallback)new StopVideoRecordingCallbackImpl(this.mStopVideoRecordingCallbackLock));
        trace("prepareCallBack() X");
        return true;
    }
    
    @Override
    protected boolean prepareInternal(final RecorderParameters recorderParameters) {
        trace("prepareInternal() E");
        if (!this.prepareCallBack()) {
            trace("prepareInternal() X failed");
            return false;
        }
        if (!super.prepareInternal(recorderParameters)) {
            trace("prepareInternal() X failed");
            return false;
        }
        if (!this.prepareBypassCamera(recorderParameters)) {
            trace("prepareInternal() X failed");
            return false;
        }
        trace("prepareInternal() X");
        return true;
    }
    
    protected boolean startBypassCamera() {
        trace("startBypassCamera() E");
        final CountDownLatch requestLatch = this.mStartVideoRecordingCallbackLock.requestLatch();
        try {
            try {
                this.getBypassCamera().requestStartVideoRecording();
                requestLatch.await();
                this.mStartVideoRecordingCallbackLock.release();
                trace("startBypassCamera() X");
                return true;
            }
            finally {}
        }
        catch (final RuntimeException | InterruptedException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("startBypassCamera() X failed : ");
            sb.append(((Throwable)ex).getMessage());
            trace(sb.toString());
            this.mStartVideoRecordingCallbackLock.release();
            return false;
        }
        this.mStartVideoRecordingCallbackLock.release();
    }
    
    @Override
    protected boolean startInternal() throws TimeoutException {
        trace("startInternal() E");
        if (!this.startBypassCamera()) {
            trace("startInternal() X failed");
            return false;
        }
        if (!this.startRecorder()) {
            trace("startInternal() X failed");
            return false;
        }
        trace("startInternal() X");
        return true;
    }
    
    protected boolean startRecorder() {
        boolean startInternal;
        try {
            startInternal = super.startInternal();
        }
        catch (final TimeoutException cause) {
            if (CamLog.DEBUG) {
                throw new RuntimeException(cause);
            }
            this.notifyError();
            startInternal = false;
        }
        return startInternal;
    }
    
    protected Pair<Boolean, CountDownLatch> stopBypassCamera() {
        trace("stopBypassCamera() E");
        final CountDownLatch requestLatch = this.mStopVideoRecordingCallbackLock.requestLatch();
        try {
            try {
                this.getBypassCamera().requestStopVideoRecording();
                final Pair create = Pair.create((Object)true, (Object)requestLatch);
                trace("stopBypassCamera() X");
                return (Pair<Boolean, CountDownLatch>)create;
            }
            finally {}
        }
        catch (final RuntimeException ex) {
            final Pair create2 = Pair.create((Object)false, (Object)null);
            trace("stopBypassCamera() X");
            return (Pair<Boolean, CountDownLatch>)create2;
        }
        trace("stopBypassCamera() X");
    }
    
    @Override
    protected boolean stopInternal() {
        trace("stopInternal() E");
        final RecorderInterface recorder;
        Label_0201: {
            if (this.mIsCameraErrorDetected) {
                if (!super.stopInternal()) {
                    trace("stopInternal() X failed");
                    return false;
                }
                break Label_0201;
            }
            recorder = this.getRecorder();
            try {
                try {
                    this.waitUntilFirstVideoFrameWritten();
                    this.getReferenceClock().stop();
                    if (recorder.isAsyncStopSupported()) {
                        recorder.stopAsync();
                    }
                    else {
                        recorder.stop();
                    }
                    try {
                        Thread.sleep(100L);
                    }
                    catch (final InterruptedException ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("sleep interrupted : ");
                        sb.append(ex.getMessage());
                        trace(sb.toString());
                    }
                    final Pair<Boolean, CountDownLatch> stopBypassCamera = this.stopBypassCamera();
                    if (!(boolean)stopBypassCamera.first) {
                        recorder.reset();
                        return false;
                    }
                    final CountDownLatch countDownLatch = (CountDownLatch)stopBypassCamera.second;
                    if (recorder.isAsyncStopSupported()) {
                        recorder.waitUntilStopCompleted();
                    }
                    recorder.reset();
                    if (countDownLatch != null) {
                        try {
                            countDownLatch.await();
                            this.mStopVideoRecordingCallbackLock.release();
                        }
                        catch (final InterruptedException recorder) {
                            CamLog.e("stopBypassCameraLatch.await() interrupted");
                        }
                    }
                    trace("stopInternal() X");
                    return true;
                }
                finally {}
            }
            catch (final RuntimeException ex2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("stopInternal() X failed : ");
                sb2.append(ex2.getMessage());
                CamLog.e(sb2.toString());
                recorder.reset();
                return false;
            }
        }
        recorder.reset();
    }
    
    public static class CallbackLock
    {
        private CountDownLatch mLatch;
        
        public CallbackLock() {
            this.mLatch = null;
        }
        
        public void release() {
            synchronized (this) {
                this.mLatch = null;
            }
        }
        
        public CountDownLatch requestLatch() {
            synchronized (this) {
                if (this.mLatch != null) {
                    CamLog.e("requestLock() Lock object already exists.");
                }
                else {
                    this.mLatch = new CountDownLatch(1);
                }
                return this.mLatch;
            }
        }
        
        public void unlock() {
            trace("unlock() E");
            synchronized (this) {
                if (this.mLatch == null) {
                    return;
                }
                this.mLatch.countDown();
                this.mLatch = null;
                monitorexit(this);
                trace("unlock() X");
            }
        }
    }
    
    private static class PrepareVideoRecordingCallbackImpl implements PrepareVideoRecordingCallback
    {
        private final CallbackLock mLock;
        
        public PrepareVideoRecordingCallbackImpl(final CallbackLock mLock) {
            this.mLock = mLock;
        }
        
        @Override
        public void onPrepareVideoRecordingDone() {
            trace("onPrepareVideoRecordingDone() E");
            this.mLock.unlock();
            trace("onPrepareVideoRecordingDone() X");
        }
    }
    
    private static class StartVideoRecordingCallbackImpl implements StartVideoRecordingCallback
    {
        private final CallbackLock mLock;
        
        public StartVideoRecordingCallbackImpl(final CallbackLock mLock) {
            this.mLock = mLock;
        }
        
        @Override
        public void onStartVideoRecordingDone() {
            trace("onStartVideoRecordingDone() E");
            this.mLock.unlock();
            trace("onStartVideoRecordingDone() X");
        }
    }
    
    private static class StopVideoRecordingCallbackImpl implements StopVideoRecordingCallback
    {
        private final CallbackLock mLock;
        
        public StopVideoRecordingCallbackImpl(final CallbackLock mLock) {
            this.mLock = mLock;
        }
        
        @Override
        public void onStopVideoRecordingDone() {
            trace("onStopVideoRecordingDone() E");
            this.mLock.unlock();
            trace("onStopVideoRecordingDone() X");
        }
    }
}
