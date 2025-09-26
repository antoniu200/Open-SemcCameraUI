package com.sonyericsson.android.camera.gestureshutter;

import android.graphics.Rect;
import android.os.Handler;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.device.ImageRetriever;
import com.sonyericsson.android.camera.gestureshutter.HandSignsDetectorInterface;
import com.sonyericsson.android.camera.gestureshutter.HandSignsNativeWrapper;
import com.sonyericsson.android.camera.util.BackgroundWorker;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.MaxVideoSize;
import java.nio.ByteBuffer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HandSignsDetector implements HandSignsDetectorInterface {
    private static final int DEBUG_FPS_CALCULATE_INTERVAL_MILLIS = 3000;
    private static final int DETECT_FRAME_RATE = 10;
    private static final int MAX_DETECT_FRAME_HEIGHT = 480;
    private static final int MAX_DETECT_FRAME_WIDTH = 640;
    private static final float NV21_BUFFER_SIZE_MULTIPLIER = 1.5f;
    public static final String TAG = "HandSignsDetector";
    private DetectRunnable mCurrentDetect;
    private ImageRetriever mImageRetriever;
    private final HandSignsDetectorInterface.DetectResultListener mListener;
    private final Handler mResultScheduler;
    private int mRoll;
    private boolean mIsStarted = false;
    private final DetectContext mDetectContext = new DetectContext();
    private final FpsLimiter mFpsLimiter = new FpsLimiter(10);
    private final Runnable mGetFrameTask = new Runnable() { // from class: com.sonyericsson.android.camera.gestureshutter.HandSignsDetector.1
        @Override // java.lang.Runnable
        public void run() {
            synchronized (HandSignsDetector.this.mDetectContext) {
                if (HandSignsDetector.this.mIsStarted) {
                    HandSignsDetector.this.mImageRetriever.registerPreviewStreamingCallback(HandSignsDetector.this.mImageCallback, HandSignsDetector.this.mWorker.getHandler());
                    if (CamLog.VERBOSE) {
                        CamLog.d("Get frame requested");
                    }
                }
            }
        }
    };
    private ImageRetriever.OnImageRetrieverCallback mImageCallback = new ImageRetriever.OnImageRetrieverCallback() { // from class: com.sonyericsson.android.camera.gestureshutter.HandSignsDetector.2
        @Override // com.sonyericsson.android.camera.device.ImageRetriever.OnImageRetrieverCallback
        public void onRetrieved(ByteBuffer byteBuffer, int i, Rect rect) {
            synchronized (HandSignsDetector.this.mDetectContext) {
                if (HandSignsDetector.this.mIsStarted) {
                    if (byteBuffer != null && i == 17) {
                        HandSignsDetector.this.mImageRetriever.unregisterPreviewStreamingCallback(this);
                        HandSignsDetector.this.postDetect(rect.width(), rect.height(), byteBuffer);
                    }
                }
            }
        }
    };
    private HandSignsNativeWrapper mNativeWrapper = new HandSignsNativeWrapper();
    private BackgroundWorker mWorker = new BackgroundWorker(TAG);

    private static class FpsLimiter {
        private final int mExpectedInterval;
        private long mFrameStartTimeStamp = 0;
        private int mFrames = 0;
        private long mFpsDetectStartTime = 0;

        FpsLimiter(int i) {
            if (i > 0) {
                this.mExpectedInterval = 1000 / i;
            } else {
                this.mExpectedInterval = 0;
            }
        }

        long hit() {
            if (this.mExpectedInterval == 0) {
                return 0L;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (CamLog.VERBOSE) {
                logFps(jCurrentTimeMillis);
            }
            long j = jCurrentTimeMillis - this.mFrameStartTimeStamp;
            long j2 = j < ((long) this.mExpectedInterval) ? this.mExpectedInterval - j : 0L;
            this.mFrameStartTimeStamp = jCurrentTimeMillis + j2;
            return j2;
        }

        private void logFps(long j) {
            if (this.mFpsDetectStartTime == 0) {
                this.mFpsDetectStartTime = j;
            } else if (j - this.mFpsDetectStartTime >= MaxVideoSize.GUARANTEED_MIN_DURATION_IN_MILLIS) {
                CamLog.d("Detect FPS = " + ((this.mFrames * 1000.0f) / (j - this.mFpsDetectStartTime)));
                this.mFpsDetectStartTime = j;
                this.mFrames = 0;
            }
            this.mFrames++;
        }
    }

    private static class DetectContext {
        private int mDetectHeight;
        private int mDetectWidth;
        private byte[] mFrame;
        private boolean mIsInitialized;
        private HandSignsNativeWrapper.ShrinkRatio mShrinkRatio;

        private DetectContext() {
        }

        void initialize(int i, int i2) {
            int i3 = 0;
            do {
                if (i <= 640 && i2 <= 480) {
                    break;
                }
                i /= 2;
                i2 /= 2;
                i3++;
            } while (i3 != HandSignsNativeWrapper.ShrinkRatio.values().length - 1);
            if (this.mDetectWidth != i || this.mDetectHeight != i2) {
                this.mDetectWidth = i;
                this.mDetectHeight = i2;
                this.mFrame = null;
            }
            if (this.mFrame == null) {
                this.mFrame = new byte[(int) (this.mDetectWidth * this.mDetectHeight * HandSignsDetector.NV21_BUFFER_SIZE_MULTIPLIER)];
            }
            this.mShrinkRatio = HandSignsNativeWrapper.ShrinkRatio.values()[i3];
            this.mIsInitialized = true;
        }

        boolean isInitialized() {
            return this.mIsInitialized;
        }

        byte[] getFrame() {
            return this.mFrame;
        }

        int getDetectWidth() {
            return this.mDetectWidth;
        }

        int getDetectHeight() {
            return this.mDetectHeight;
        }

        HandSignsNativeWrapper.ShrinkRatio getShrinkRatio() {
            return this.mShrinkRatio;
        }

        void reset() {
            this.mShrinkRatio = null;
            this.mIsInitialized = false;
        }
    }

    public HandSignsDetector(HandSignsDetectorInterface.DetectResultListener detectResultListener, Handler handler) {
        this.mListener = detectResultListener;
        this.mResultScheduler = handler;
    }

    @Override // com.sonyericsson.android.camera.gestureshutter.HandSignsDetectorInterface
    public int getDetectWidth() {
        if (this.mDetectContext.isInitialized()) {
            return this.mDetectContext.getDetectWidth();
        }
        return 0;
    }

    @Override // com.sonyericsson.android.camera.gestureshutter.HandSignsDetectorInterface
    public int getDetectHeight() {
        if (this.mDetectContext.isInitialized()) {
            return this.mDetectContext.getDetectHeight();
        }
        return 0;
    }

    @Override // com.sonyericsson.android.camera.gestureshutter.HandSignsDetectorInterface
    public void startDetect(ImageRetriever imageRetriever) {
        this.mImageRetriever = imageRetriever;
        this.mDetectContext.reset();
        this.mIsStarted = true;
        postGetFrame();
    }

    @Override // com.sonyericsson.android.camera.gestureshutter.HandSignsDetectorInterface
    public void stopDetect() {
        if (this.mCurrentDetect != null) {
            this.mWorker.getHandler().removeCallbacks(this.mCurrentDetect);
            this.mCurrentDetect = null;
        }
        this.mWorker.getHandler().removeCallbacks(this.mGetFrameTask);
        synchronized (this.mDetectContext) {
            this.mImageRetriever.unregisterPreviewStreamingCallback(this.mImageCallback);
            this.mImageRetriever = null;
            this.mIsStarted = false;
        }
    }

    @Override // com.sonyericsson.android.camera.gestureshutter.HandSignsDetectorInterface
    public void setLayoutOrientation(CameraActivity.LayoutOrientation layoutOrientation) {
        switch (layoutOrientation) {
            case Portrait:
                this.mRoll = 270;
                break;
            case Landscape:
                this.mRoll = 0;
                break;
            case ReversePortrait:
                this.mRoll = 90;
                break;
            case ReverseLandscape:
                this.mRoll = 180;
                break;
            default:
                throw new RuntimeException("Abnormal orientation: " + layoutOrientation);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Rotation updated to:" + this.mRoll);
        }
    }

    @Override // com.sonyericsson.android.camera.gestureshutter.HandSignsDetectorInterface
    public boolean isStarted() {
        return this.mIsStarted;
    }

    @Override // com.sonyericsson.android.camera.gestureshutter.HandSignsDetectorInterface
    public void release() {
        try {
            this.mWorker.quit();
        } catch (InterruptedException unused) {
        }
        this.mNativeWrapper.release();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postGetFrame() {
        long jHit = this.mFpsLimiter.hit();
        this.mWorker.getHandler().postDelayed(this.mGetFrameTask, jHit);
        if (CamLog.VERBOSE) {
            CamLog.d("Get frame posted with delay: " + jHit);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postDetect(int i, int i2, ByteBuffer byteBuffer) {
        this.mCurrentDetect = new DetectRunnable(i, i2, byteBuffer);
        this.mWorker.getHandler().post(this.mCurrentDetect);
        if (CamLog.VERBOSE) {
            CamLog.d("detection posted");
        }
    }

    static class DetectResult implements HandSignsDetectorInterface.DetectResultInterface {
        public static final int AHS_STATUS_CLICKDOWN = 2097152;
        public static final int AHS_STATUS_CLICKUP = 4194304;
        public static final int AHS_STATUS_NONE = 0;
        public static final int AHS_STATUS_PALM = 16;
        private Rect mArea = new Rect();
        private HandSignsDetectorInterface.DetectResultInterface.HandStatus mStatus;

        DetectResult() {
        }

        public void setAreaAndStatus(int i, int i2, int i3, int i4, int i5) {
            this.mArea.left = i;
            this.mArea.right = i3;
            this.mArea.top = i2;
            this.mArea.bottom = i4;
            this.mStatus = i5 == 16 ? HandSignsDetectorInterface.DetectResultInterface.HandStatus.PALM : HandSignsDetectorInterface.DetectResultInterface.HandStatus.NONE;
        }

        @Override // com.sonyericsson.android.camera.gestureshutter.HandSignsDetectorInterface.DetectResultInterface
        public Rect getArea() {
            return this.mArea;
        }

        @Override // com.sonyericsson.android.camera.gestureshutter.HandSignsDetectorInterface.DetectResultInterface
        public HandSignsDetectorInterface.DetectResultInterface.HandStatus getStatus() {
            return this.mStatus;
        }

        public String toString() {
            return getStatus() + " - " + getArea();
        }
    }

    private class DetectRunnable implements Runnable {
        private int height;
        private ByteBuffer mYuvBuffer;
        private int width;

        public DetectRunnable(int i, int i2, ByteBuffer byteBuffer) {
            this.width = i;
            this.height = i2;
            this.mYuvBuffer = byteBuffer;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (HandSignsDetector.this.mIsStarted) {
                if (CamLog.VERBOSE) {
                    CamLog.d("Starting detection");
                }
                if (!HandSignsDetector.this.mDetectContext.isInitialized()) {
                    HandSignsDetector.this.mDetectContext.initialize(this.width, this.height);
                }
                byte[] bArr = new byte[this.mYuvBuffer.remaining()];
                this.mYuvBuffer.get(bArr);
                HandSignsNativeWrapper unused = HandSignsDetector.this.mNativeWrapper;
                HandSignsNativeWrapper.shrinkYvu420Sp(bArr, this.width, this.height, HandSignsDetector.this.mDetectContext.getFrame(), HandSignsDetector.this.mDetectContext.getShrinkRatio());
                final DetectResult detectResult = new DetectResult();
                HandSignsDetector.this.mNativeWrapper.detect(HandSignsDetector.this.mDetectContext.getDetectWidth(), HandSignsDetector.this.mDetectContext.getDetectHeight(), HandSignsDetector.this.mDetectContext.getFrame(), HandSignsDetector.this.mRoll, detectResult);
                if (CamLog.VERBOSE) {
                    CamLog.d("Detect result: " + detectResult);
                }
                if (HandSignsDetector.this.mListener != null) {
                    if (HandSignsDetector.this.mResultScheduler != null) {
                        HandSignsDetector.this.mResultScheduler.post(new Runnable() { // from class: com.sonyericsson.android.camera.gestureshutter.HandSignsDetector.DetectRunnable.1
                            @Override // java.lang.Runnable
                            public void run() {
                                HandSignsDetector.this.mListener.onDetectResult(detectResult);
                            }
                        });
                    } else {
                        HandSignsDetector.this.mListener.onDetectResult(detectResult);
                    }
                }
                HandSignsDetector.this.postGetFrame();
            }
        }
    }
}
