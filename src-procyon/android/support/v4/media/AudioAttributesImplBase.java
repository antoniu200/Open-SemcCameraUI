// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.media;

import android.support.annotation.NonNull;
import java.util.Arrays;
import android.os.Bundle;

class AudioAttributesImplBase implements AudioAttributesImpl
{
    int mContentType;
    int mFlags;
    int mLegacyStream;
    int mUsage;
    
    AudioAttributesImplBase() {
        this.mUsage = 0;
        this.mContentType = 0;
        this.mFlags = 0;
        this.mLegacyStream = -1;
    }
    
    AudioAttributesImplBase(final int mContentType, final int mFlags, final int mUsage, final int mLegacyStream) {
        this.mUsage = 0;
        this.mContentType = 0;
        this.mFlags = 0;
        this.mLegacyStream = -1;
        this.mContentType = mContentType;
        this.mFlags = mFlags;
        this.mUsage = mUsage;
        this.mLegacyStream = mLegacyStream;
    }
    
    public static AudioAttributesImpl fromBundle(final Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        return new AudioAttributesImplBase(bundle.getInt("android.support.v4.media.audio_attrs.CONTENT_TYPE", 0), bundle.getInt("android.support.v4.media.audio_attrs.FLAGS", 0), bundle.getInt("android.support.v4.media.audio_attrs.USAGE", 0), bundle.getInt("android.support.v4.media.audio_attrs.LEGACY_STREAM_TYPE", -1));
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof AudioAttributesImplBase;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final AudioAttributesImplBase audioAttributesImplBase = (AudioAttributesImplBase)o;
        boolean b3 = b2;
        if (this.mContentType == audioAttributesImplBase.getContentType()) {
            b3 = b2;
            if (this.mFlags == audioAttributesImplBase.getFlags()) {
                b3 = b2;
                if (this.mUsage == audioAttributesImplBase.getUsage()) {
                    b3 = b2;
                    if (this.mLegacyStream == audioAttributesImplBase.mLegacyStream) {
                        b3 = true;
                    }
                }
            }
        }
        return b3;
    }
    
    @Override
    public Object getAudioAttributes() {
        return null;
    }
    
    @Override
    public int getContentType() {
        return this.mContentType;
    }
    
    @Override
    public int getFlags() {
        final int mFlags = this.mFlags;
        final int legacyStreamType = this.getLegacyStreamType();
        int n;
        if (legacyStreamType == 6) {
            n = (mFlags | 0x4);
        }
        else {
            n = mFlags;
            if (legacyStreamType == 7) {
                n = (mFlags | 0x1);
            }
        }
        return n & 0x111;
    }
    
    @Override
    public int getLegacyStreamType() {
        if (this.mLegacyStream != -1) {
            return this.mLegacyStream;
        }
        return AudioAttributesCompat.toVolumeStreamType(false, this.mFlags, this.mUsage);
    }
    
    @Override
    public int getRawLegacyStreamType() {
        return this.mLegacyStream;
    }
    
    @Override
    public int getUsage() {
        return this.mUsage;
    }
    
    @Override
    public int getVolumeControlStream() {
        return AudioAttributesCompat.toVolumeStreamType(true, this.mFlags, this.mUsage);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] { this.mContentType, this.mFlags, this.mUsage, this.mLegacyStream });
    }
    
    @NonNull
    @Override
    public Bundle toBundle() {
        final Bundle bundle = new Bundle();
        bundle.putInt("android.support.v4.media.audio_attrs.USAGE", this.mUsage);
        bundle.putInt("android.support.v4.media.audio_attrs.CONTENT_TYPE", this.mContentType);
        bundle.putInt("android.support.v4.media.audio_attrs.FLAGS", this.mFlags);
        if (this.mLegacyStream != -1) {
            bundle.putInt("android.support.v4.media.audio_attrs.LEGACY_STREAM_TYPE", this.mLegacyStream);
        }
        return bundle;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AudioAttributesCompat:");
        if (this.mLegacyStream != -1) {
            sb.append(" stream=");
            sb.append(this.mLegacyStream);
            sb.append(" derived");
        }
        sb.append(" usage=");
        sb.append(AudioAttributesCompat.usageToString(this.mUsage));
        sb.append(" content=");
        sb.append(this.mContentType);
        sb.append(" flags=0x");
        sb.append(Integer.toHexString(this.mFlags).toUpperCase());
        return sb.toString();
    }
}
