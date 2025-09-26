// 
// Decompiled by Procyon v0.6.0
// 

package androidx.media;

import androidx.versionedparcelable.VersionedParcelable;
import android.support.v4.media.AudioAttributesCompat;
import androidx.versionedparcelable.VersionedParcel;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY })
public final class AudioAttributesCompatParcelizer
{
    public static AudioAttributesCompat read(final VersionedParcel versionedParcel) {
        final AudioAttributesCompat audioAttributesCompat = new AudioAttributesCompat();
        audioAttributesCompat.mImpl = versionedParcel.readVersionedParcelable(audioAttributesCompat.mImpl, 1);
        return audioAttributesCompat;
    }
    
    public static void write(final AudioAttributesCompat audioAttributesCompat, final VersionedParcel versionedParcel) {
        versionedParcel.setSerializationFlags(false, false);
        versionedParcel.writeVersionedParcelable(audioAttributesCompat.mImpl, 1);
    }
}
