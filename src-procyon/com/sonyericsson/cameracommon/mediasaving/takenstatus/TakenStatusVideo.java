// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving.takenstatus;

import com.sonyericsson.android.camera.util.CamLog;

public class TakenStatusVideo
{
    public static final String TAG = "TakenStatusVideo";
    public long mDuration;
    public final long maxDurationMills;
    public final long maxFileSizeBytes;
    
    public TakenStatusVideo(final long maxDurationMills, final long maxFileSizeBytes) {
        this.mDuration = 0L;
        this.maxDurationMills = maxDurationMills;
        this.maxFileSizeBytes = maxFileSizeBytes;
    }
    
    public TakenStatusVideo(final TakenStatusVideo takenStatusVideo) {
        this.mDuration = 0L;
        this.mDuration = takenStatusVideo.mDuration;
        this.maxDurationMills = takenStatusVideo.maxDurationMills;
        this.maxFileSizeBytes = takenStatusVideo.maxFileSizeBytes;
    }
    
    public void log() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Duration         : ");
            sb.append(this.mDuration);
            CamLog.d(sb.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("MaxDurationMills : ");
            sb2.append(this.maxDurationMills);
            CamLog.d(sb2.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("MaxFileSizeBytes : ");
            sb3.append(this.maxFileSizeBytes);
            CamLog.d(sb3.toString());
        }
    }
}
