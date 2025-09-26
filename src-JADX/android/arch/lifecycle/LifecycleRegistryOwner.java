package android.arch.lifecycle;

import android.support.annotation.NonNull;

@Deprecated
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface LifecycleRegistryOwner extends LifecycleOwner {
    @Override // android.arch.lifecycle.LifecycleOwner
    @NonNull
    LifecycleRegistry getLifecycle();
}
