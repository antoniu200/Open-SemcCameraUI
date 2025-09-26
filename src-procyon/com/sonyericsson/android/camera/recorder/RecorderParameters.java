// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder;

import com.sonyericsson.android.camera.util.CamLog;
import android.net.Uri;
import android.media.CamcorderProfile;
import android.location.Location;

public class RecorderParameters
{
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
    
    private RecorderParameters(final Uri mUri, final CamcorderProfile mProfile) {
        this.mUri = mUri;
        this.mProfile = mProfile;
        this.mLocation = null;
        this.mOrientationHint = -1;
        this.mMaxFileSize = 256000000000L;
        this.mMaxDuration = -1;
        this.mIsMicrophoneEnabled = false;
        this.mIsHdr = false;
        this.mDataSpace = null;
    }
    
    private boolean isValid(final long n) {
        return n != -1L;
    }
    
    public DataSpace dataSpace() {
        return this.mDataSpace;
    }
    
    public void dump() {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append(" uri:");
            sb.append(this.mUri);
            CamLog.d(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(" location:");
            sb2.append(this.mLocation);
            CamLog.d(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(" orientationHint:");
            sb3.append(this.mOrientationHint);
            CamLog.d(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(" maxFileSize:");
            sb4.append(this.mMaxFileSize);
            CamLog.d(sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(" maxDuration:");
            sb5.append(this.mMaxDuration);
            CamLog.d(sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(" profile:");
            sb6.append(this.mProfile);
            CamLog.d(sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(" isMicrophoneEnabled:");
            sb7.append(this.mIsMicrophoneEnabled);
            CamLog.d(sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(" isHdr:");
            sb8.append(this.mIsHdr);
            CamLog.d(sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(" dataSpace:");
            sb9.append(this.mDataSpace);
            CamLog.d(sb9.toString());
        }
    }
    
    public boolean hasLocation() {
        return this.mLocation != null;
    }
    
    public boolean hasMaxDuration() {
        return this.isValid(this.mMaxDuration);
    }
    
    public boolean hasMaxFileSize() {
        return this.isValid(this.mMaxFileSize);
    }
    
    public boolean hasOrientationHint() {
        return this.isValid(this.mOrientationHint);
    }
    
    public boolean isHdr() {
        return this.mIsHdr;
    }
    
    public boolean isMicrophoneEnabled() {
        return this.mIsMicrophoneEnabled;
    }
    
    public Location location() {
        return this.mLocation;
    }
    
    public int maxDuration() {
        return this.mMaxDuration;
    }
    
    public long maxFileSize() {
        return this.mMaxFileSize;
    }
    
    public int orientationHint() {
        return this.mOrientationHint;
    }
    
    public Uri outputUri() {
        return this.mUri;
    }
    
    public CamcorderProfile profile() {
        return this.mProfile;
    }
    
    public static class Builder
    {
        private final RecorderParameters mParameters;
        
        public Builder(final Uri uri, final CamcorderProfile camcorderProfile) {
            this.mParameters = new RecorderParameters(uri, camcorderProfile, null);
        }
        
        public RecorderParameters build() {
            return this.mParameters;
        }
        
        public Builder setDataSpace(final DataSpace dataSpace) {
            this.mParameters.mDataSpace = dataSpace;
            return this;
        }
        
        public Builder setHdr(final boolean b) {
            this.mParameters.mIsHdr = b;
            return this;
        }
        
        public Builder setLocation(final Location location) {
            this.mParameters.mLocation = location;
            return this;
        }
        
        public Builder setMaxDuration(final int n) {
            this.mParameters.mMaxDuration = n;
            return this;
        }
        
        public Builder setMaxFileSize(final long a) {
            this.mParameters.mMaxFileSize = Math.min(a, 256000000000L);
            return this;
        }
        
        public Builder setMicrophoneEnabled(final boolean b) {
            this.mParameters.mIsMicrophoneEnabled = b;
            return this;
        }
        
        public Builder setOrientationHint(final int n) {
            this.mParameters.mOrientationHint = n;
            return this;
        }
    }
    
    public static class DataSpace
    {
        public final int range;
        public final int standard;
        public final int transfer;
        
        public DataSpace(final int standard, final int transfer, final int range) {
            this.standard = standard;
            this.transfer = transfer;
            this.range = range;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append('[');
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("standard:");
            sb2.append(this.standard);
            sb2.append(",");
            sb.append(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("transfer:");
            sb3.append(this.transfer);
            sb3.append(",");
            sb.append(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("range:");
            sb4.append(this.range);
            sb.append(sb4.toString());
            sb.append(']');
            return sb.toString();
        }
    }
}
