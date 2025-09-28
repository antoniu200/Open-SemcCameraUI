package com.sonyericsson.android.camera.recorder;

import android.location.Location;
import android.media.CamcorderProfile;
import android.net.Uri;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RecorderParameters {
    public static final long DEFAULT_MAX_FILE_SIZE = 256000000000L;
    private static final int INVALID_VALUE = -1;
    public static final String TAG = "RecorderParameters";
    private DataSpace mDataSpace;
    private boolean mIsHdr;
    private boolean mIsMicrophoneEnabled;
    private Location mLocation;
    private int mMaxDuration;
    private long mMaxFileSize;
    private int mOrientationHint;
    private final CamcorderProfile mProfile;
    private final Uri mUri;

    private boolean isValid(long j) {
        return j != -1;
    }

    public static class DataSpace {
        public final int range;
        public final int standard;
        public final int transfer;

        public DataSpace(int i, int i2, int i3) {
            this.standard = i;
            this.transfer = i2;
            this.range = i3;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            sb.append("standard:" + this.standard + ",");
            sb.append("transfer:" + this.transfer + ",");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("range:");
            sb2.append(this.range);
            sb.append(sb2.toString());
            sb.append(']');
            return sb.toString();
        }
    }

    private RecorderParameters(Uri uri, CamcorderProfile camcorderProfile) {
        this.mUri = uri;
        this.mProfile = camcorderProfile;
        this.mLocation = null;
        this.mOrientationHint = -1;
        this.mMaxFileSize = 256000000000L;
        this.mMaxDuration = -1;
        this.mIsMicrophoneEnabled = false;
        this.mIsHdr = false;
        this.mDataSpace = null;
    }

    public boolean hasLocation() {
        return this.mLocation != null;
    }

    public Location location() {
        return this.mLocation;
    }

    public boolean hasOrientationHint() {
        return isValid(this.mOrientationHint);
    }

    public int orientationHint() {
        return this.mOrientationHint;
    }

    public boolean hasMaxFileSize() {
        return isValid(this.mMaxFileSize);
    }

    public long maxFileSize() {
        return this.mMaxFileSize;
    }

    public boolean hasMaxDuration() {
        return isValid(this.mMaxDuration);
    }

    public int maxDuration() {
        return this.mMaxDuration;
    }

    public boolean isHdr() {
        return this.mIsHdr;
    }

    public DataSpace dataSpace() {
        return this.mDataSpace;
    }

    public CamcorderProfile profile() {
        return this.mProfile;
    }

    public boolean isMicrophoneEnabled() {
        return this.mIsMicrophoneEnabled;
    }

    public Uri outputUri() {
        return this.mUri;
    }

    public static class Builder {
        private final RecorderParameters mParameters;

        public Builder(Uri uri, CamcorderProfile camcorderProfile) {
            this.mParameters = new RecorderParameters(uri, camcorderProfile);
        }

        public Builder setLocation(Location location) {
            this.mParameters.mLocation = location;
            return this;
        }

        public Builder setOrientationHint(int i) {
            this.mParameters.mOrientationHint = i;
            return this;
        }

        public Builder setMaxFileSize(long j) {
            this.mParameters.mMaxFileSize = Math.min(j, 256000000000L);
            return this;
        }

        public Builder setMaxDuration(int i) {
            this.mParameters.mMaxDuration = i;
            return this;
        }

        public Builder setMicrophoneEnabled(boolean z) {
            this.mParameters.mIsMicrophoneEnabled = z;
            return this;
        }

        public Builder setDataSpace(DataSpace dataSpace) {
            this.mParameters.mDataSpace = dataSpace;
            return this;
        }

        public Builder setHdr(boolean z) {
            this.mParameters.mIsHdr = z;
            return this;
        }

        public RecorderParameters build() {
            return this.mParameters;
        }
    }

    public void dump() {
        if (CamLog.DEBUG) {
            CamLog.d(" uri:" + this.mUri);
            CamLog.d(" location:" + this.mLocation);
            CamLog.d(" orientationHint:" + this.mOrientationHint);
            CamLog.d(" maxFileSize:" + this.mMaxFileSize);
            CamLog.d(" maxDuration:" + this.mMaxDuration);
            CamLog.d(" profile:" + this.mProfile);
            CamLog.d(" isMicrophoneEnabled:" + this.mIsMicrophoneEnabled);
            CamLog.d(" isHdr:" + this.mIsHdr);
            CamLog.d(" dataSpace:" + this.mDataSpace);
        }
    }
}
