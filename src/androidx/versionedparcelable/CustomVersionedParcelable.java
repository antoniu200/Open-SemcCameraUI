package androidx.versionedparcelable;

import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class CustomVersionedParcelable implements VersionedParcelable {
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void onPostParceling() {
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void onPreParceling(boolean z) {
    }
}
