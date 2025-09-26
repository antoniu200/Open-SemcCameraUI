package com.sonyericsson.android.camera.debug;

import android.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import kotlin.Metadata;
import org.jetbrains.annotations.Nullable;

/* compiled from: DebugMenuActivity.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0014¨\u0006\u0007"}, d2 = {"Lcom/sonyericsson/android/camera/debug/DebugMenuActivity;", "Landroid/support/v4/app/FragmentActivity;", "()V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "SemcCameraUI_release"}, k = 1, mv = {1, 1, 11})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class DebugMenuActivity extends FragmentActivity {
    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DebugParameterUtils.INSTANCE.isUserBuild()) {
            finish();
        } else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new DebugMenuFragment(), null).commit();
        }
    }
}
