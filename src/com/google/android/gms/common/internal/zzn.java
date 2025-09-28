package com.google.android.gms.common.internal;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.sonyericsson.android.camera.view.modeselector.CameraCommonProviderConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzn {
    private static final Uri zzagi = Uri.parse("http://plus.google.com/");
    private static final Uri zzagj = zzagi.buildUpon().appendPath("circles").appendPath("find").build();

    public static Intent zzco(String str) {
        Uri uriFromParts = Uri.fromParts(CameraCommonProviderConstants.CapturingModeColumns.PACKAGE, str, null);
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(uriFromParts);
        return intent;
    }

    public static Intent zzpo() {
        Intent intent = new Intent("com.google.android.clockwork.home.UPDATE_ANDROID_WEAR_ACTION");
        intent.setPackage("com.google.android.wearable.app");
        return intent;
    }

    private static Uri zzv(String str, String str2) {
        Uri.Builder builderAppendQueryParameter = Uri.parse("market://details").buildUpon().appendQueryParameter("id", str);
        if (!TextUtils.isEmpty(str2)) {
            builderAppendQueryParameter.appendQueryParameter("pcampaignid", str2);
        }
        return builderAppendQueryParameter.build();
    }

    public static Intent zzw(String str, String str2) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(zzv(str, str2));
        intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE);
        intent.addFlags(524288);
        return intent;
    }
}
