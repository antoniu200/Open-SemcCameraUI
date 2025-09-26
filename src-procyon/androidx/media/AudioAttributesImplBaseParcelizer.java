// 
// Decompiled by Procyon v0.6.0
// 

package androidx.media;

import androidx.versionedparcelable.VersionedParcel;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY })
public final class AudioAttributesImplBaseParcelizer
{
    public static AudioAttributesImplBase read(final VersionedParcel versionedParcel) {
        final AudioAttributesImplBase audioAttributesImplBase = new AudioAttributesImplBase();
        audioAttributesImplBase.mUsage = versionedParcel.readInt(audioAttributesImplBase.mUsage, 1);
        audioAttributesImplBase.mContentType = versionedParcel.readInt(audioAttributesImplBase.mContentType, 2);
        audioAttributesImplBase.mFlags = versionedParcel.readInt(audioAttributesImplBase.mFlags, 3);
        audioAttributesImplBase.mLegacyStream = versionedParcel.readInt(audioAttributesImplBase.mLegacyStream, 4);
        return audioAttributesImplBase;
    }
    
    public static void write(final AudioAttributesImplBase audioAttributesImplBase, final VersionedParcel versionedParcel) {
        versionedParcel.setSerializationFlags(false, false);
        versionedParcel.writeInt(audioAttributesImplBase.mUsage, 1);
        versionedParcel.writeInt(audioAttributesImplBase.mContentType, 2);
        versionedParcel.writeInt(audioAttributesImplBase.mFlags, 3);
        versionedParcel.writeInt(audioAttributesImplBase.mLegacyStream, 4);
    }
}
