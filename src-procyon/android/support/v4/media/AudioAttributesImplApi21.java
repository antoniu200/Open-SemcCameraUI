// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.media;

import android.support.annotation.NonNull;
import android.os.Parcelable;
import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import android.os.Build$VERSION;
import android.os.Bundle;
import android.media.AudioAttributes;
import java.lang.reflect.Method;
import android.annotation.TargetApi;

@TargetApi(21)
class AudioAttributesImplApi21 implements AudioAttributesImpl
{
    private static final String TAG = "AudioAttributesCompat21";
    static Method sAudioAttributesToLegacyStreamType;
    AudioAttributes mAudioAttributes;
    int mLegacyStreamType;
    
    AudioAttributesImplApi21() {
        this.mLegacyStreamType = -1;
    }
    
    AudioAttributesImplApi21(final AudioAttributes audioAttributes) {
        this(audioAttributes, -1);
    }
    
    AudioAttributesImplApi21(final AudioAttributes mAudioAttributes, final int mLegacyStreamType) {
        this.mLegacyStreamType = -1;
        this.mAudioAttributes = mAudioAttributes;
        this.mLegacyStreamType = mLegacyStreamType;
    }
    
    public static AudioAttributesImpl fromBundle(final Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        final AudioAttributes audioAttributes = (AudioAttributes)bundle.getParcelable("android.support.v4.media.audio_attrs.FRAMEWORKS");
        if (audioAttributes == null) {
            return null;
        }
        return new AudioAttributesImplApi21(audioAttributes, bundle.getInt("android.support.v4.media.audio_attrs.LEGACY_STREAM_TYPE", -1));
    }
    
    static Method getAudioAttributesToLegacyStreamTypeMethod() {
        try {
            if (AudioAttributesImplApi21.sAudioAttributesToLegacyStreamType == null) {
                AudioAttributesImplApi21.sAudioAttributesToLegacyStreamType = AudioAttributes.class.getMethod("toLegacyStreamType", AudioAttributes.class);
            }
            return AudioAttributesImplApi21.sAudioAttributesToLegacyStreamType;
        }
        catch (final NoSuchMethodException ex) {
            return null;
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof AudioAttributesImplApi21 && this.mAudioAttributes.equals((Object)((AudioAttributesImplApi21)o).mAudioAttributes);
    }
    
    @Override
    public Object getAudioAttributes() {
        return this.mAudioAttributes;
    }
    
    @Override
    public int getContentType() {
        return this.mAudioAttributes.getContentType();
    }
    
    @Override
    public int getFlags() {
        return this.mAudioAttributes.getFlags();
    }
    
    @Override
    public int getLegacyStreamType() {
        if (this.mLegacyStreamType != -1) {
            return this.mLegacyStreamType;
        }
        final Method audioAttributesToLegacyStreamTypeMethod = getAudioAttributesToLegacyStreamTypeMethod();
        if (audioAttributesToLegacyStreamTypeMethod == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("No AudioAttributes#toLegacyStreamType() on API: ");
            sb.append(Build$VERSION.SDK_INT);
            Log.w("AudioAttributesCompat21", sb.toString());
            return -1;
        }
        try {
            return (int)audioAttributesToLegacyStreamTypeMethod.invoke(null, this.mAudioAttributes);
        }
        catch (final InvocationTargetException | IllegalAccessException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("getLegacyStreamType() failed on API: ");
            sb2.append(Build$VERSION.SDK_INT);
            Log.w("AudioAttributesCompat21", sb2.toString(), (Throwable)ex);
            return -1;
        }
    }
    
    @Override
    public int getRawLegacyStreamType() {
        return this.mLegacyStreamType;
    }
    
    @Override
    public int getUsage() {
        return this.mAudioAttributes.getUsage();
    }
    
    @Override
    public int getVolumeControlStream() {
        if (Build$VERSION.SDK_INT >= 26) {
            return this.mAudioAttributes.getVolumeControlStream();
        }
        return AudioAttributesCompat.toVolumeStreamType(true, this.getFlags(), this.getUsage());
    }
    
    @Override
    public int hashCode() {
        return this.mAudioAttributes.hashCode();
    }
    
    @NonNull
    @Override
    public Bundle toBundle() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable("android.support.v4.media.audio_attrs.FRAMEWORKS", (Parcelable)this.mAudioAttributes);
        if (this.mLegacyStreamType != -1) {
            bundle.putInt("android.support.v4.media.audio_attrs.LEGACY_STREAM_TYPE", this.mLegacyStreamType);
        }
        return bundle;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AudioAttributesCompat: audioattributes=");
        sb.append(this.mAudioAttributes);
        return sb.toString();
    }
}
