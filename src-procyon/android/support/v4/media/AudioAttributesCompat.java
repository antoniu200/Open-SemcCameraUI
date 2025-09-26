// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.media;

import android.util.Log;
import android.media.AudioAttributes$Builder;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import android.support.annotation.Nullable;
import android.media.AudioAttributes;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.os.Build$VERSION;
import android.os.Bundle;
import android.util.SparseIntArray;
import androidx.versionedparcelable.VersionedParcelable;

public class AudioAttributesCompat implements VersionedParcelable
{
    static final String AUDIO_ATTRIBUTES_CONTENT_TYPE = "android.support.v4.media.audio_attrs.CONTENT_TYPE";
    static final String AUDIO_ATTRIBUTES_FLAGS = "android.support.v4.media.audio_attrs.FLAGS";
    static final String AUDIO_ATTRIBUTES_FRAMEWORKS = "android.support.v4.media.audio_attrs.FRAMEWORKS";
    static final String AUDIO_ATTRIBUTES_LEGACY_STREAM_TYPE = "android.support.v4.media.audio_attrs.LEGACY_STREAM_TYPE";
    static final String AUDIO_ATTRIBUTES_USAGE = "android.support.v4.media.audio_attrs.USAGE";
    public static final int CONTENT_TYPE_MOVIE = 3;
    public static final int CONTENT_TYPE_MUSIC = 2;
    public static final int CONTENT_TYPE_SONIFICATION = 4;
    public static final int CONTENT_TYPE_SPEECH = 1;
    public static final int CONTENT_TYPE_UNKNOWN = 0;
    static final int FLAG_ALL = 1023;
    static final int FLAG_ALL_PUBLIC = 273;
    public static final int FLAG_AUDIBILITY_ENFORCED = 1;
    static final int FLAG_BEACON = 8;
    static final int FLAG_BYPASS_INTERRUPTION_POLICY = 64;
    static final int FLAG_BYPASS_MUTE = 128;
    static final int FLAG_DEEP_BUFFER = 512;
    public static final int FLAG_HW_AV_SYNC = 16;
    static final int FLAG_HW_HOTWORD = 32;
    static final int FLAG_LOW_LATENCY = 256;
    static final int FLAG_SCO = 4;
    static final int FLAG_SECURE = 2;
    static final int INVALID_STREAM_TYPE = -1;
    private static final int[] SDK_USAGES;
    private static final int SUPPRESSIBLE_CALL = 2;
    private static final int SUPPRESSIBLE_NOTIFICATION = 1;
    private static final SparseIntArray SUPPRESSIBLE_USAGES;
    private static final String TAG = "AudioAttributesCompat";
    public static final int USAGE_ALARM = 4;
    public static final int USAGE_ASSISTANCE_ACCESSIBILITY = 11;
    public static final int USAGE_ASSISTANCE_NAVIGATION_GUIDANCE = 12;
    public static final int USAGE_ASSISTANCE_SONIFICATION = 13;
    public static final int USAGE_ASSISTANT = 16;
    public static final int USAGE_GAME = 14;
    public static final int USAGE_MEDIA = 1;
    public static final int USAGE_NOTIFICATION = 5;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_DELAYED = 9;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_INSTANT = 8;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_REQUEST = 7;
    public static final int USAGE_NOTIFICATION_EVENT = 10;
    public static final int USAGE_NOTIFICATION_RINGTONE = 6;
    public static final int USAGE_UNKNOWN = 0;
    private static final int USAGE_VIRTUAL_SOURCE = 15;
    public static final int USAGE_VOICE_COMMUNICATION = 2;
    public static final int USAGE_VOICE_COMMUNICATION_SIGNALLING = 3;
    static boolean sForceLegacyBehavior;
    AudioAttributesImpl mImpl;
    
    static {
        (SUPPRESSIBLE_USAGES = new SparseIntArray()).put(5, 1);
        AudioAttributesCompat.SUPPRESSIBLE_USAGES.put(6, 2);
        AudioAttributesCompat.SUPPRESSIBLE_USAGES.put(7, 2);
        AudioAttributesCompat.SUPPRESSIBLE_USAGES.put(8, 1);
        AudioAttributesCompat.SUPPRESSIBLE_USAGES.put(9, 1);
        AudioAttributesCompat.SUPPRESSIBLE_USAGES.put(10, 1);
        SDK_USAGES = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16 };
    }
    
    AudioAttributesCompat() {
    }
    
    AudioAttributesCompat(final AudioAttributesImpl mImpl) {
        this.mImpl = mImpl;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static AudioAttributesCompat fromBundle(final Bundle bundle) {
        AudioAttributesImpl audioAttributesImpl;
        if (Build$VERSION.SDK_INT >= 21) {
            audioAttributesImpl = AudioAttributesImplApi21.fromBundle(bundle);
        }
        else {
            audioAttributesImpl = AudioAttributesImplBase.fromBundle(bundle);
        }
        AudioAttributesCompat audioAttributesCompat;
        if (audioAttributesImpl == null) {
            audioAttributesCompat = null;
        }
        else {
            audioAttributesCompat = new AudioAttributesCompat(audioAttributesImpl);
        }
        return audioAttributesCompat;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static void setForceLegacyBehavior(final boolean sForceLegacyBehavior) {
        AudioAttributesCompat.sForceLegacyBehavior = sForceLegacyBehavior;
    }
    
    static int toVolumeStreamType(final boolean b, int n, final int i) {
        final int n2 = 1;
        if ((n & 0x1) == 0x1) {
            if (b) {
                n = n2;
            }
            else {
                n = 7;
            }
            return n;
        }
        final int n3 = 0;
        final int n4 = 0;
        if ((n & 0x4) == 0x4) {
            if (b) {
                n = n4;
            }
            else {
                n = 6;
            }
            return n;
        }
        n = 3;
        switch (i) {
            default: {
                if (b) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown usage value ");
                    sb.append(i);
                    sb.append(" in audio attributes");
                    throw new IllegalArgumentException(sb.toString());
                }
                return 3;
            }
            case 13: {
                return 1;
            }
            case 11: {
                return 10;
            }
            case 6: {
                return 2;
            }
            case 5:
            case 7:
            case 8:
            case 9:
            case 10: {
                return 5;
            }
            case 4: {
                return 4;
            }
            case 3: {
                if (b) {
                    n = n3;
                }
                else {
                    n = 8;
                }
                return n;
            }
            case 2: {
                return 0;
            }
            case 1:
            case 12:
            case 14:
            case 16: {
                return 3;
            }
            case 0: {
                if (b) {
                    n = Integer.MIN_VALUE;
                }
                return n;
            }
        }
    }
    
    static int toVolumeStreamType(final boolean b, final AudioAttributesCompat audioAttributesCompat) {
        return toVolumeStreamType(b, audioAttributesCompat.getFlags(), audioAttributesCompat.getUsage());
    }
    
    static int usageForStreamType(final int n) {
        switch (n) {
            default: {
                return 0;
            }
            case 10: {
                return 11;
            }
            case 8: {
                return 3;
            }
            case 6: {
                return 2;
            }
            case 5: {
                return 5;
            }
            case 4: {
                return 4;
            }
            case 3: {
                return 1;
            }
            case 2: {
                return 6;
            }
            case 1:
            case 7: {
                return 13;
            }
            case 0: {
                return 2;
            }
        }
    }
    
    static String usageToString(final int i) {
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown usage ");
                sb.append(i);
                return sb.toString();
            }
            case 16: {
                return "USAGE_ASSISTANT";
            }
            case 14: {
                return "USAGE_GAME";
            }
            case 13: {
                return "USAGE_ASSISTANCE_SONIFICATION";
            }
            case 12: {
                return "USAGE_ASSISTANCE_NAVIGATION_GUIDANCE";
            }
            case 11: {
                return "USAGE_ASSISTANCE_ACCESSIBILITY";
            }
            case 10: {
                return "USAGE_NOTIFICATION_EVENT";
            }
            case 9: {
                return "USAGE_NOTIFICATION_COMMUNICATION_DELAYED";
            }
            case 8: {
                return "USAGE_NOTIFICATION_COMMUNICATION_INSTANT";
            }
            case 7: {
                return "USAGE_NOTIFICATION_COMMUNICATION_REQUEST";
            }
            case 6: {
                return "USAGE_NOTIFICATION_RINGTONE";
            }
            case 5: {
                return "USAGE_NOTIFICATION";
            }
            case 4: {
                return "USAGE_ALARM";
            }
            case 3: {
                return "USAGE_VOICE_COMMUNICATION_SIGNALLING";
            }
            case 2: {
                return "USAGE_VOICE_COMMUNICATION";
            }
            case 1: {
                return "USAGE_MEDIA";
            }
            case 0: {
                return "USAGE_UNKNOWN";
            }
        }
    }
    
    @Nullable
    public static AudioAttributesCompat wrap(@NonNull final Object o) {
        if (Build$VERSION.SDK_INT >= 21 && !AudioAttributesCompat.sForceLegacyBehavior) {
            final AudioAttributesImplApi21 mImpl = new AudioAttributesImplApi21((AudioAttributes)o);
            final AudioAttributesCompat audioAttributesCompat = new AudioAttributesCompat();
            audioAttributesCompat.mImpl = mImpl;
            return audioAttributesCompat;
        }
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof AudioAttributesCompat;
        boolean b2 = false;
        if (!b) {
            return false;
        }
        final AudioAttributesCompat audioAttributesCompat = (AudioAttributesCompat)o;
        if (this.mImpl == null) {
            if (audioAttributesCompat.mImpl == null) {
                b2 = true;
            }
            return b2;
        }
        return this.mImpl.equals(audioAttributesCompat.mImpl);
    }
    
    public int getContentType() {
        return this.mImpl.getContentType();
    }
    
    public int getFlags() {
        return this.mImpl.getFlags();
    }
    
    public int getLegacyStreamType() {
        return this.mImpl.getLegacyStreamType();
    }
    
    int getRawLegacyStreamType() {
        return this.mImpl.getRawLegacyStreamType();
    }
    
    public int getUsage() {
        return this.mImpl.getUsage();
    }
    
    public int getVolumeControlStream() {
        return this.mImpl.getVolumeControlStream();
    }
    
    @Override
    public int hashCode() {
        return this.mImpl.hashCode();
    }
    
    @NonNull
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public Bundle toBundle() {
        return this.mImpl.toBundle();
    }
    
    @Override
    public String toString() {
        return this.mImpl.toString();
    }
    
    @Nullable
    public Object unwrap() {
        return this.mImpl.getAudioAttributes();
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface AttributeContentType {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface AttributeUsage {
    }
    
    abstract static class AudioManagerHidden
    {
        public static final int STREAM_ACCESSIBILITY = 10;
        public static final int STREAM_BLUETOOTH_SCO = 6;
        public static final int STREAM_SYSTEM_ENFORCED = 7;
        public static final int STREAM_TTS = 9;
        
        private AudioManagerHidden() {
        }
    }
    
    public static class Builder
    {
        private int mContentType;
        private int mFlags;
        private int mLegacyStream;
        private int mUsage;
        
        public Builder() {
            this.mUsage = 0;
            this.mContentType = 0;
            this.mFlags = 0;
            this.mLegacyStream = -1;
        }
        
        public Builder(final AudioAttributesCompat audioAttributesCompat) {
            this.mUsage = 0;
            this.mContentType = 0;
            this.mFlags = 0;
            this.mLegacyStream = -1;
            this.mUsage = audioAttributesCompat.getUsage();
            this.mContentType = audioAttributesCompat.getContentType();
            this.mFlags = audioAttributesCompat.getFlags();
            this.mLegacyStream = audioAttributesCompat.getRawLegacyStreamType();
        }
        
        public AudioAttributesCompat build() {
            AudioAttributesImpl audioAttributesImpl;
            if (!AudioAttributesCompat.sForceLegacyBehavior && Build$VERSION.SDK_INT >= 21) {
                final AudioAttributes$Builder setUsage = new AudioAttributes$Builder().setContentType(this.mContentType).setFlags(this.mFlags).setUsage(this.mUsage);
                if (this.mLegacyStream != -1) {
                    setUsage.setLegacyStreamType(this.mLegacyStream);
                }
                audioAttributesImpl = new AudioAttributesImplApi21(setUsage.build(), this.mLegacyStream);
            }
            else {
                audioAttributesImpl = new AudioAttributesImplBase(this.mContentType, this.mFlags, this.mUsage, this.mLegacyStream);
            }
            return new AudioAttributesCompat(audioAttributesImpl);
        }
        
        public Builder setContentType(final int mContentType) {
            switch (mContentType) {
                default: {
                    this.mUsage = 0;
                    break;
                }
                case 0:
                case 1:
                case 2:
                case 3:
                case 4: {
                    this.mContentType = mContentType;
                    break;
                }
            }
            return this;
        }
        
        public Builder setFlags(final int n) {
            this.mFlags |= (n & 0x3FF);
            return this;
        }
        
        Builder setInternalLegacyStreamType(final int i) {
            switch (i) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid stream type ");
                    sb.append(i);
                    sb.append(" for AudioAttributesCompat");
                    Log.e("AudioAttributesCompat", sb.toString());
                    break;
                }
                case 10: {
                    this.mContentType = 1;
                    break;
                }
                case 9: {
                    this.mContentType = 4;
                    break;
                }
                case 8: {
                    this.mContentType = 4;
                    break;
                }
                case 6: {
                    this.mContentType = 1;
                    this.mFlags |= 0x4;
                    break;
                }
                case 5: {
                    this.mContentType = 4;
                    break;
                }
                case 4: {
                    this.mContentType = 4;
                    break;
                }
                case 3: {
                    this.mContentType = 2;
                    break;
                }
                case 2: {
                    this.mContentType = 4;
                    break;
                }
                case 7: {
                    this.mFlags |= 0x1;
                }
                case 1: {
                    this.mContentType = 4;
                    break;
                }
                case 0: {
                    this.mContentType = 1;
                    break;
                }
            }
            this.mUsage = AudioAttributesCompat.usageForStreamType(i);
            return this;
        }
        
        public Builder setLegacyStreamType(final int n) {
            if (n == 10) {
                throw new IllegalArgumentException("STREAM_ACCESSIBILITY is not a legacy stream type that was used for audio playback");
            }
            this.mLegacyStream = n;
            if (Build$VERSION.SDK_INT >= 21) {
                return this.setInternalLegacyStreamType(n);
            }
            return this;
        }
        
        public Builder setUsage(final int n) {
            switch (n) {
                default: {
                    this.mUsage = 0;
                    break;
                }
                case 16: {
                    if (!AudioAttributesCompat.sForceLegacyBehavior && Build$VERSION.SDK_INT > 25) {
                        this.mUsage = n;
                        break;
                    }
                    this.mUsage = 12;
                    break;
                }
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15: {
                    this.mUsage = n;
                    break;
                }
            }
            return this;
        }
    }
}
