package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import com.google.android.gms.R;
import com.google.android.gms.internal.zzmq;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzg {
    public static String zzc(Context context, int i, String str) {
        Resources resources = context.getResources();
        if (i == 5) {
            return resources.getString(R.string.common_google_play_services_invalid_account_text);
        }
        if (i == 7) {
            return resources.getString(R.string.common_google_play_services_network_error_text);
        }
        if (i == 9) {
            return resources.getString(R.string.common_google_play_services_unsupported_text, str);
        }
        if (i == 42) {
            return resources.getString(R.string.common_android_wear_update_text, str);
        }
        switch (i) {
            case 1:
                return zzmq.zzb(resources) ? resources.getString(R.string.common_google_play_services_install_text_tablet, str) : resources.getString(R.string.common_google_play_services_install_text_phone, str);
            case 2:
                return resources.getString(R.string.common_google_play_services_update_text, str);
            case 3:
                return resources.getString(R.string.common_google_play_services_enable_text, str);
            default:
                switch (i) {
                    case 16:
                        return resources.getString(R.string.common_google_play_services_api_unavailable_text, str);
                    case 17:
                        return resources.getString(R.string.common_google_play_services_sign_in_failed_text);
                    case 18:
                        return resources.getString(R.string.common_google_play_services_updating_text, str);
                    default:
                        return resources.getString(R.string.common_google_play_services_unknown_issue);
                }
        }
    }

    public static String zzd(Context context, int i, String str) {
        Resources resources = context.getResources();
        if (i == 5) {
            return resources.getString(R.string.common_google_play_services_invalid_account_text);
        }
        if (i == 7) {
            return resources.getString(R.string.common_google_play_services_network_error_text);
        }
        if (i == 9) {
            return resources.getString(R.string.common_google_play_services_unsupported_text, str);
        }
        if (i == 42) {
            return resources.getString(R.string.common_android_wear_notification_needs_update_text, str);
        }
        switch (i) {
            case 1:
                return zzmq.zzb(resources) ? resources.getString(R.string.common_google_play_services_install_text_tablet, str) : resources.getString(R.string.common_google_play_services_install_text_phone, str);
            case 2:
                return resources.getString(R.string.common_google_play_services_update_text, str);
            case 3:
                return resources.getString(R.string.common_google_play_services_enable_text, str);
            default:
                switch (i) {
                    case 16:
                        return resources.getString(R.string.common_google_play_services_api_unavailable_text, str);
                    case 17:
                        return resources.getString(R.string.common_google_play_services_sign_in_failed_text);
                    case 18:
                        return resources.getString(R.string.common_google_play_services_updating_text, str);
                    default:
                        return resources.getString(R.string.common_google_play_services_unknown_issue);
                }
        }
    }

    public static final String zzg(Context context, int i) {
        int i2;
        String str;
        String str2;
        Resources resources = context.getResources();
        if (i != 42) {
            switch (i) {
                case 1:
                    i2 = R.string.common_google_play_services_install_title;
                    break;
                case 2:
                    i2 = R.string.common_google_play_services_update_title;
                    break;
                case 3:
                    i2 = R.string.common_google_play_services_enable_title;
                    break;
                case 4:
                case 6:
                    return null;
                case 5:
                    Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                    i2 = R.string.common_google_play_services_invalid_account_title;
                    break;
                case 7:
                    Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                    i2 = R.string.common_google_play_services_network_error_title;
                    break;
                case 8:
                    str = "GoogleApiAvailability";
                    str2 = "Internal error occurred. Please see logs for detailed information";
                    Log.e(str, str2);
                    return null;
                case 9:
                    Log.e("GoogleApiAvailability", "Google Play services is invalid. Cannot recover.");
                    i2 = R.string.common_google_play_services_unsupported_title;
                    break;
                case 10:
                    str = "GoogleApiAvailability";
                    str2 = "Developer error occurred. Please see logs for detailed information";
                    Log.e(str, str2);
                    return null;
                case 11:
                    str = "GoogleApiAvailability";
                    str2 = "The application is not licensed to the user.";
                    Log.e(str, str2);
                    return null;
                default:
                    switch (i) {
                        case 16:
                            str = "GoogleApiAvailability";
                            str2 = "One of the API components you attempted to connect to is not available.";
                            Log.e(str, str2);
                            return null;
                        case 17:
                            Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                            i2 = R.string.common_google_play_services_sign_in_failed_title;
                            break;
                        case 18:
                            i2 = R.string.common_google_play_services_updating_title;
                            break;
                        default:
                            str = "GoogleApiAvailability";
                            str2 = "Unexpected error code " + i;
                            Log.e(str, str2);
                            return null;
                    }
            }
        } else {
            i2 = R.string.common_android_wear_update_title;
        }
        return resources.getString(i2);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0019  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String zzh(android.content.Context r1, int r2) {
        /*
            android.content.res.Resources r1 = r1.getResources()
            r0 = 42
            if (r2 == r0) goto L19
            switch(r2) {
                case 1: goto L16;
                case 2: goto L19;
                case 3: goto L13;
                default: goto Lb;
            }
        Lb:
            r2 = 17039370(0x104000a, float:2.42446E-38)
        Le:
            java.lang.String r1 = r1.getString(r2)
            return r1
        L13:
            int r2 = com.google.android.gms.R.string.common_google_play_services_enable_button
            goto Le
        L16:
            int r2 = com.google.android.gms.R.string.common_google_play_services_install_button
            goto Le
        L19:
            int r2 = com.google.android.gms.R.string.common_google_play_services_update_button
            goto Le
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzg.zzh(android.content.Context, int):java.lang.String");
    }

    public static final String zzi(Context context, int i) {
        int i2;
        String str;
        String str2;
        Resources resources = context.getResources();
        if (i != 42) {
            switch (i) {
                case 1:
                    i2 = R.string.common_google_play_services_install_title;
                    break;
                case 2:
                    i2 = R.string.common_google_play_services_update_title;
                    break;
                case 3:
                    i2 = R.string.common_google_play_services_enable_title;
                    break;
                case 4:
                case 6:
                    return null;
                case 5:
                    Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                    i2 = R.string.common_google_play_services_invalid_account_title;
                    break;
                case 7:
                    Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                    i2 = R.string.common_google_play_services_network_error_title;
                    break;
                case 8:
                    str = "GoogleApiAvailability";
                    str2 = "Internal error occurred. Please see logs for detailed information";
                    Log.e(str, str2);
                    return null;
                case 9:
                    Log.e("GoogleApiAvailability", "Google Play services is invalid. Cannot recover.");
                    i2 = R.string.common_google_play_services_unsupported_title;
                    break;
                case 10:
                    str = "GoogleApiAvailability";
                    str2 = "Developer error occurred. Please see logs for detailed information";
                    Log.e(str, str2);
                    return null;
                case 11:
                    str = "GoogleApiAvailability";
                    str2 = "The application is not licensed to the user.";
                    Log.e(str, str2);
                    return null;
                default:
                    switch (i) {
                        case 16:
                            str = "GoogleApiAvailability";
                            str2 = "One of the API components you attempted to connect to is not available.";
                            Log.e(str, str2);
                            return null;
                        case 17:
                            Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                            i2 = R.string.common_google_play_services_sign_in_failed_title;
                            break;
                        case 18:
                            i2 = R.string.common_google_play_services_updating_title;
                            break;
                        default:
                            str = "GoogleApiAvailability";
                            str2 = "Unexpected error code " + i;
                            Log.e(str, str2);
                            return null;
                    }
            }
        } else {
            i2 = R.string.common_android_wear_update_title;
        }
        return resources.getString(i2);
    }
}
