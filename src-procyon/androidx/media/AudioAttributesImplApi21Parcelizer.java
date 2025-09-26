// 
// Decompiled by Procyon v0.6.0
// 

package androidx.media;

import android.os.Parcelable;
import android.media.AudioAttributes;
import androidx.versionedparcelable.VersionedParcel;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY })
public final class AudioAttributesImplApi21Parcelizer
{
    public static AudioAttributesImplApi21 read(final VersionedParcel versionedParcel) {
        final AudioAttributesImplApi21 audioAttributesImplApi21 = new AudioAttributesImplApi21();
        audioAttributesImplApi21.mAudioAttributes = versionedParcel.readParcelable(audioAttributesImplApi21.mAudioAttributes, 1);
        audioAttributesImplApi21.mLegacyStreamType = versionedParcel.readInt(audioAttributesImplApi21.mLegacyStreamType, 2);
        return audioAttributesImplApi21;
    }
    
    public static void write(final AudioAttributesImplApi21 audioAttributesImplApi21, final VersionedParcel versionedParcel) {
        versionedParcel.setSerializationFlags(false, false);
        versionedParcel.writeParcelable((Parcelable)audioAttributesImplApi21.mAudioAttributes, 1);
        versionedParcel.writeInt(audioAttributesImplApi21.mLegacyStreamType, 2);
    }
}
