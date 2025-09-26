// 
// Decompiled by Procyon v0.6.0
// 

package androidx.versionedparcelable;

import android.os.Parcelable;
import java.io.OutputStream;
import java.io.InputStream;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ParcelUtils
{
    private ParcelUtils() {
    }
    
    public static <T extends VersionedParcelable> T fromInputStream(final InputStream inputStream) {
        return new VersionedParcelStream(inputStream, null).readVersionedParcelable();
    }
    
    public static <T extends VersionedParcelable> T fromParcelable(final Parcelable parcelable) {
        if (!(parcelable instanceof ParcelImpl)) {
            throw new IllegalArgumentException("Invalid parcel");
        }
        return ((ParcelImpl)parcelable).getVersionedParcel();
    }
    
    public static void toOutputStream(final VersionedParcelable versionedParcelable, final OutputStream outputStream) {
        final VersionedParcelStream versionedParcelStream = new VersionedParcelStream(null, outputStream);
        versionedParcelStream.writeVersionedParcelable(versionedParcelable);
        versionedParcelStream.closeField();
    }
    
    public static Parcelable toParcelable(final VersionedParcelable versionedParcelable) {
        return (Parcelable)new ParcelImpl(versionedParcelable);
    }
}
