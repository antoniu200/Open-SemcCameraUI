package com.sonyericsson.cameracommon.mediasaving.takenstatus;

import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TakenStatusVideo {
    public static final String TAG = "TakenStatusVideo";
    public long mDuration;
    public final long maxDurationMills;
    public final long maxFileSizeBytes;

    public TakenStatusVideo(long j, long j2) {
        this.mDuration = 0L;
        this.maxDurationMills = j;
        this.maxFileSizeBytes = j2;
    }

    public TakenStatusVideo(TakenStatusVideo takenStatusVideo) {
        this.mDuration = 0L;
        this.mDuration = takenStatusVideo.mDuration;
        this.maxDurationMills = takenStatusVideo.maxDurationMills;
        this.maxFileSizeBytes = takenStatusVideo.maxFileSizeBytes;
    }

    public void log() {
        if (CamLog.VERBOSE) {
            CamLog.d("Duration         : " + this.mDuration);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("MaxDurationMills : " + this.maxDurationMills);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("MaxFileSizeBytes : " + this.maxFileSizeBytes);
        }
    }
}
