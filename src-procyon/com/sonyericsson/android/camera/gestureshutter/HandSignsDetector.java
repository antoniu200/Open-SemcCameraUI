// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.gestureshutter;

import com.sonyericsson.android.camera.CameraActivity;
import android.graphics.Rect;
import java.nio.ByteBuffer;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.BackgroundWorker;
import android.os.Handler;
import com.sonyericsson.android.camera.device.ImageRetriever;

public class HandSignsDetector implements HandSignsDetectorInterface
{
    private static final int DEBUG_FPS_CALCULATE_INTERVAL_MILLIS = 3000;
    private static final int DETECT_FRAME_RATE = 10;
    private static final int MAX_DETECT_FRAME_HEIGHT = 480;
    private static final int MAX_DETECT_FRAME_WIDTH = 640;
    private static final float NV21_BUFFER_SIZE_MULTIPLIER = 1.5f;
    public static final String TAG = "HandSignsDetector";
    private DetectRunnable mCurrentDetect;
    private final DetectContext mDetectContext;
    private final FpsLimiter mFpsLimiter;
    private final Runnable mGetFrameTask;
    private ImageRetriever.OnImageRetrieverCallback mImageCallback;
    private ImageRetriever mImageRetriever;
    private boolean mIsStarted;
    private final DetectResultListener mListener;
    private HandSignsNativeWrapper mNativeWrapper;
    private final Handler mResultScheduler;
    private int mRoll;
    private BackgroundWorker mWorker;
    
    public HandSignsDetector(final DetectResultListener mListener, final Handler mResultScheduler) {
        this.mIsStarted = false;
        this.mDetectContext = new DetectContext();
        this.mFpsLimiter = new FpsLimiter(10);
        this.mGetFrameTask = new Runnable() {
            final HandSignsDetector this$0;
            
            @Override
            public void run() {
                synchronized (this.this$0.mDetectContext) {
                    if (!this.this$0.mIsStarted) {
                        return;
                    }
                    this.this$0.mImageRetriever.registerPreviewStreamingCallback(this.this$0.mImageCallback, this.this$0.mWorker.getHandler());
                    if (CamLog.VERBOSE) {
                        CamLog.d("Get frame requested");
                    }
                }
            }
        };
        this.mImageCallback = new ImageRetriever.OnImageRetrieverCallback() {
            final HandSignsDetector this$0;
            
            @Override
            public void onRetrieved(final ByteBuffer byteBuffer, final int n, final Rect rect) {
                synchronized (this.this$0.mDetectContext) {
                    if (!this.this$0.mIsStarted) {
                        return;
                    }
                    if (byteBuffer != null && n == 17) {
                        this.this$0.mImageRetriever.unregisterPreviewStreamingCallback((ImageRetriever.OnImageRetrieverCallback)this);
                        this.this$0.postDetect(rect.width(), rect.height(), byteBuffer);
                    }
                }
            }
        };
        this.mListener = mListener;
        this.mNativeWrapper = new HandSignsNativeWrapper();
        this.mWorker = new BackgroundWorker("HandSignsDetector");
        this.mResultScheduler = mResultScheduler;
    }
    
    private void postDetect(final int n, final int n2, final ByteBuffer byteBuffer) {
        this.mCurrentDetect = new DetectRunnable(n, n2, byteBuffer);
        this.mWorker.getHandler().post((Runnable)this.mCurrentDetect);
        if (CamLog.VERBOSE) {
            CamLog.d("detection posted");
        }
    }
    
    private void postGetFrame() {
        final long hit = this.mFpsLimiter.hit();
        this.mWorker.getHandler().postDelayed(this.mGetFrameTask, hit);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Get frame posted with delay: ");
            sb.append(hit);
            CamLog.d(sb.toString());
        }
    }
    
    @Override
    public int getDetectHeight() {
        if (this.mDetectContext.isInitialized()) {
            return this.mDetectContext.getDetectHeight();
        }
        return 0;
    }
    
    @Override
    public int getDetectWidth() {
        if (this.mDetectContext.isInitialized()) {
            return this.mDetectContext.getDetectWidth();
        }
        return 0;
    }
    
    @Override
    public boolean isStarted() {
        return this.mIsStarted;
    }
    
    @Override
    public void release() {
        while (true) {
            try {
                this.mWorker.quit();
                this.mNativeWrapper.release();
            }
            catch (final InterruptedException ex) {
                continue;
            }
            break;
        }
    }
    
    @Override
    public void setLayoutOrientation(final CameraActivity.LayoutOrientation obj) {
        switch (HandSignsDetector$3.$SwitchMap$com$sonyericsson$android$camera$CameraActivity$LayoutOrientation[obj.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Abnormal orientation: ");
                sb.append(obj);
                throw new RuntimeException(sb.toString());
            }
            case 4: {
                this.mRoll = 180;
                break;
            }
            case 3: {
                this.mRoll = 90;
                break;
            }
            case 2: {
                this.mRoll = 0;
                break;
            }
            case 1: {
                this.mRoll = 270;
                break;
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Rotation updated to:");
            sb2.append(this.mRoll);
            CamLog.d(sb2.toString());
        }
    }
    
    @Override
    public void startDetect(final ImageRetriever mImageRetriever) {
        this.mImageRetriever = mImageRetriever;
        this.mDetectContext.reset();
        this.mIsStarted = true;
        this.postGetFrame();
    }
    
    @Override
    public void stopDetect() {
        if (this.mCurrentDetect != null) {
            this.mWorker.getHandler().removeCallbacks((Runnable)this.mCurrentDetect);
            this.mCurrentDetect = null;
        }
        this.mWorker.getHandler().removeCallbacks(this.mGetFrameTask);
        synchronized (this.mDetectContext) {
            this.mImageRetriever.unregisterPreviewStreamingCallback(this.mImageCallback);
            this.mImageRetriever = null;
            this.mIsStarted = false;
        }
    }
    
    private static class DetectContext
    {
        private int mDetectHeight;
        private int mDetectWidth;
        private byte[] mFrame;
        private boolean mIsInitialized;
        private HandSignsNativeWrapper.ShrinkRatio mShrinkRatio;
        
        int getDetectHeight() {
            return this.mDetectHeight;
        }
        
        int getDetectWidth() {
            return this.mDetectWidth;
        }
        
        byte[] getFrame() {
            return this.mFrame;
        }
        
        HandSignsNativeWrapper.ShrinkRatio getShrinkRatio() {
            return this.mShrinkRatio;
        }
        
        void initialize(int n, int n2) {
            int n3 = 0;
            int i;
            int mDetectWidth;
            int mDetectHeight;
            do {
                if (n <= 640) {
                    i = n3;
                    mDetectWidth = n;
                    if ((mDetectHeight = n2) <= 480) {
                        break;
                    }
                }
                mDetectWidth = n / 2;
                mDetectHeight = n2 / 2;
                i = ++n3;
                n = mDetectWidth;
                n2 = mDetectHeight;
            } while (i != HandSignsNativeWrapper.ShrinkRatio.values().length - 1);
            if (this.mDetectWidth != mDetectWidth || this.mDetectHeight != mDetectHeight) {
                this.mDetectWidth = mDetectWidth;
                this.mDetectHeight = mDetectHeight;
                this.mFrame = null;
            }
            if (this.mFrame == null) {
                this.mFrame = new byte[(int)(this.mDetectWidth * this.mDetectHeight * 1.5f)];
            }
            this.mShrinkRatio = HandSignsNativeWrapper.ShrinkRatio.values()[i];
            this.mIsInitialized = true;
        }
        
        boolean isInitialized() {
            return this.mIsInitialized;
        }
        
        void reset() {
            this.mShrinkRatio = null;
            this.mIsInitialized = false;
        }
    }
    
    static class DetectResult implements DetectResultInterface
    {
        public static final int AHS_STATUS_CLICKDOWN = 2097152;
        public static final int AHS_STATUS_CLICKUP = 4194304;
        public static final int AHS_STATUS_NONE = 0;
        public static final int AHS_STATUS_PALM = 16;
        private Rect mArea;
        private HandStatus mStatus;
        
        DetectResult() {
            this.mArea = new Rect();
        }
        
        @Override
        public Rect getArea() {
            return this.mArea;
        }
        
        @Override
        public HandStatus getStatus() {
            return this.mStatus;
        }
        
        public void setAreaAndStatus(final int left, final int top, final int right, final int bottom, final int n) {
            this.mArea.left = left;
            this.mArea.right = right;
            this.mArea.top = top;
            this.mArea.bottom = bottom;
            HandStatus mStatus;
            if (n == 16) {
                mStatus = HandStatus.PALM;
            }
            else {
                mStatus = HandStatus.NONE;
            }
            this.mStatus = mStatus;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getStatus());
            sb.append(" - ");
            sb.append(this.getArea());
            return sb.toString();
        }
    }
    
    private class DetectRunnable implements Runnable
    {
        private int height;
        private ByteBuffer mYuvBuffer;
        final HandSignsDetector this$0;
        private int width;
        
        public DetectRunnable(final HandSignsDetector this$0, final int width, final int height, final ByteBuffer mYuvBuffer) {
            this.this$0 = this$0;
            this.width = width;
            this.height = height;
            this.mYuvBuffer = mYuvBuffer;
        }
        
        @Override
        public void run() {
            if (!this.this$0.mIsStarted) {
                return;
            }
            if (CamLog.VERBOSE) {
                CamLog.d("Starting detection");
            }
            if (!this.this$0.mDetectContext.isInitialized()) {
                this.this$0.mDetectContext.initialize(this.width, this.height);
            }
            final byte[] dst = new byte[this.mYuvBuffer.remaining()];
            this.mYuvBuffer.get(dst);
            this.this$0.mNativeWrapper;
            HandSignsNativeWrapper.shrinkYvu420Sp(dst, this.width, this.height, this.this$0.mDetectContext.getFrame(), this.this$0.mDetectContext.getShrinkRatio());
            final DetectResult obj = new DetectResult();
            this.this$0.mNativeWrapper.detect(this.this$0.mDetectContext.getDetectWidth(), this.this$0.mDetectContext.getDetectHeight(), this.this$0.mDetectContext.getFrame(), this.this$0.mRoll, obj);
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Detect result: ");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            if (this.this$0.mListener != null) {
                if (this.this$0.mResultScheduler != null) {
                    this.this$0.mResultScheduler.post((Runnable)new Runnable(this, obj) {
                        final DetectRunnable this$1;
                        final DetectResult val$result;
                        
                        @Override
                        public void run() {
                            this.this$1.this$0.mListener.onDetectResult(this.val$result);
                        }
                    });
                }
                else {
                    this.this$0.mListener.onDetectResult(obj);
                }
            }
            this.this$0.postGetFrame();
        }
    }
    
    private static class FpsLimiter
    {
        private final int mExpectedInterval;
        private long mFpsDetectStartTime;
        private long mFrameStartTimeStamp;
        private int mFrames;
        
        FpsLimiter(final int n) {
            this.mFrameStartTimeStamp = 0L;
            this.mFrames = 0;
            this.mFpsDetectStartTime = 0L;
            if (n > 0) {
                this.mExpectedInterval = 1000 / n;
            }
            else {
                this.mExpectedInterval = 0;
            }
        }
        
        private void logFps(final long n) {
            if (this.mFpsDetectStartTime == 0L) {
                this.mFpsDetectStartTime = n;
            }
            else if (n - this.mFpsDetectStartTime >= 3000L) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Detect FPS = ");
                sb.append(this.mFrames * 1000.0f / (n - this.mFpsDetectStartTime));
                CamLog.d(sb.toString());
                this.mFpsDetectStartTime = n;
                this.mFrames = 0;
            }
            ++this.mFrames;
        }
        
        long hit() {
            final int mExpectedInterval = this.mExpectedInterval;
            long n = 0L;
            if (mExpectedInterval == 0) {
                return 0L;
            }
            final long currentTimeMillis = System.currentTimeMillis();
            if (CamLog.VERBOSE) {
                this.logFps(currentTimeMillis);
            }
            final long n2 = currentTimeMillis - this.mFrameStartTimeStamp;
            if (n2 < this.mExpectedInterval) {
                n = this.mExpectedInterval - n2;
            }
            this.mFrameStartTimeStamp = currentTimeMillis + n;
            return n;
        }
    }
}
