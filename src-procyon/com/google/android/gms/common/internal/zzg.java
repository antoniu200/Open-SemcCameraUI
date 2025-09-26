// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.util.Log;
import android.content.res.Resources;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.R;
import android.content.Context;

public final class zzg
{
    public static String zzc(final Context context, final int n, final String s) {
        final Resources resources = context.getResources();
        if (n == 5) {
            return resources.getString(R.string.common_google_play_services_invalid_account_text);
        }
        if (n == 7) {
            return resources.getString(R.string.common_google_play_services_network_error_text);
        }
        if (n == 9) {
            return resources.getString(R.string.common_google_play_services_unsupported_text, new Object[] { s });
        }
        if (n == 42) {
            return resources.getString(R.string.common_android_wear_update_text, new Object[] { s });
        }
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        return resources.getString(R.string.common_google_play_services_unknown_issue);
                    }
                    case 18: {
                        return resources.getString(R.string.common_google_play_services_updating_text, new Object[] { s });
                    }
                    case 17: {
                        return resources.getString(R.string.common_google_play_services_sign_in_failed_text);
                    }
                    case 16: {
                        return resources.getString(R.string.common_google_play_services_api_unavailable_text, new Object[] { s });
                    }
                }
                break;
            }
            case 3: {
                return resources.getString(R.string.common_google_play_services_enable_text, new Object[] { s });
            }
            case 2: {
                return resources.getString(R.string.common_google_play_services_update_text, new Object[] { s });
            }
            case 1: {
                if (zzmq.zzb(resources)) {
                    return resources.getString(R.string.common_google_play_services_install_text_tablet, new Object[] { s });
                }
                return resources.getString(R.string.common_google_play_services_install_text_phone, new Object[] { s });
            }
        }
    }
    
    public static String zzd(final Context context, final int n, final String s) {
        final Resources resources = context.getResources();
        if (n == 5) {
            return resources.getString(R.string.common_google_play_services_invalid_account_text);
        }
        if (n == 7) {
            return resources.getString(R.string.common_google_play_services_network_error_text);
        }
        if (n == 9) {
            return resources.getString(R.string.common_google_play_services_unsupported_text, new Object[] { s });
        }
        if (n == 42) {
            return resources.getString(R.string.common_android_wear_notification_needs_update_text, new Object[] { s });
        }
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        return resources.getString(R.string.common_google_play_services_unknown_issue);
                    }
                    case 18: {
                        return resources.getString(R.string.common_google_play_services_updating_text, new Object[] { s });
                    }
                    case 17: {
                        return resources.getString(R.string.common_google_play_services_sign_in_failed_text);
                    }
                    case 16: {
                        return resources.getString(R.string.common_google_play_services_api_unavailable_text, new Object[] { s });
                    }
                }
                break;
            }
            case 3: {
                return resources.getString(R.string.common_google_play_services_enable_text, new Object[] { s });
            }
            case 2: {
                return resources.getString(R.string.common_google_play_services_update_text, new Object[] { s });
            }
            case 1: {
                if (zzmq.zzb(resources)) {
                    return resources.getString(R.string.common_google_play_services_install_text_tablet, new Object[] { s });
                }
                return resources.getString(R.string.common_google_play_services_install_text_phone, new Object[] { s });
            }
        }
    }
    
    public static final String zzg(final Context context, int i) {
        final Resources resources = context.getResources();
        if (i != 42) {
            String string = null;
            Label_0126: {
                switch (i) {
                    default: {
                        switch (i) {
                            default: {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Unexpected error code ");
                                sb.append(i);
                                string = sb.toString();
                                break Label_0126;
                            }
                            case 18: {
                                i = R.string.common_google_play_services_updating_title;
                                return resources.getString(i);
                            }
                            case 17: {
                                Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                                i = R.string.common_google_play_services_sign_in_failed_title;
                                return resources.getString(i);
                            }
                            case 16: {
                                string = "One of the API components you attempted to connect to is not available.";
                                break Label_0126;
                            }
                        }
                        break;
                    }
                    case 11: {
                        string = "The application is not licensed to the user.";
                        break;
                    }
                    case 10: {
                        string = "Developer error occurred. Please see logs for detailed information";
                        break;
                    }
                    case 9: {
                        Log.e("GoogleApiAvailability", "Google Play services is invalid. Cannot recover.");
                        i = R.string.common_google_play_services_unsupported_title;
                        return resources.getString(i);
                    }
                    case 8: {
                        string = "Internal error occurred. Please see logs for detailed information";
                        break;
                    }
                    case 7: {
                        Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                        i = R.string.common_google_play_services_network_error_title;
                        return resources.getString(i);
                    }
                    case 5: {
                        Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                        i = R.string.common_google_play_services_invalid_account_title;
                        return resources.getString(i);
                    }
                    case 4:
                    case 6: {
                        return null;
                    }
                    case 3: {
                        i = R.string.common_google_play_services_enable_title;
                        return resources.getString(i);
                    }
                    case 2: {
                        i = R.string.common_google_play_services_update_title;
                        return resources.getString(i);
                    }
                    case 1: {
                        i = R.string.common_google_play_services_install_title;
                        return resources.getString(i);
                    }
                }
            }
            Log.e("GoogleApiAvailability", string);
            return null;
        }
        i = R.string.common_android_wear_update_title;
        return resources.getString(i);
    }
    
    public static String zzh(final Context context, int n) {
        final Resources resources = context.getResources();
        Label_0063: {
            if (n == 42) {
                break Label_0063;
            }
            switch (n) {
                default: {
                    n = 17039370;
                    break;
                }
                case 3: {
                    n = R.string.common_google_play_services_enable_button;
                    break;
                }
                case 1: {
                    n = R.string.common_google_play_services_install_button;
                    break;
                }
                case 2: {
                    break Label_0063;
                }
            }
            return resources.getString(n);
        }
        n = R.string.common_google_play_services_update_button;
        return resources.getString(n);
    }
    
    public static final String zzi(final Context context, int i) {
        final Resources resources = context.getResources();
        if (i != 42) {
            String string = null;
            Label_0126: {
                switch (i) {
                    default: {
                        switch (i) {
                            default: {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Unexpected error code ");
                                sb.append(i);
                                string = sb.toString();
                                break Label_0126;
                            }
                            case 18: {
                                i = R.string.common_google_play_services_updating_title;
                                return resources.getString(i);
                            }
                            case 17: {
                                Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                                i = R.string.common_google_play_services_sign_in_failed_title;
                                return resources.getString(i);
                            }
                            case 16: {
                                string = "One of the API components you attempted to connect to is not available.";
                                break Label_0126;
                            }
                        }
                        break;
                    }
                    case 11: {
                        string = "The application is not licensed to the user.";
                        break;
                    }
                    case 10: {
                        string = "Developer error occurred. Please see logs for detailed information";
                        break;
                    }
                    case 9: {
                        Log.e("GoogleApiAvailability", "Google Play services is invalid. Cannot recover.");
                        i = R.string.common_google_play_services_unsupported_title;
                        return resources.getString(i);
                    }
                    case 8: {
                        string = "Internal error occurred. Please see logs for detailed information";
                        break;
                    }
                    case 7: {
                        Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                        i = R.string.common_google_play_services_network_error_title;
                        return resources.getString(i);
                    }
                    case 5: {
                        Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                        i = R.string.common_google_play_services_invalid_account_title;
                        return resources.getString(i);
                    }
                    case 4:
                    case 6: {
                        return null;
                    }
                    case 3: {
                        i = R.string.common_google_play_services_enable_title;
                        return resources.getString(i);
                    }
                    case 2: {
                        i = R.string.common_google_play_services_update_title;
                        return resources.getString(i);
                    }
                    case 1: {
                        i = R.string.common_google_play_services_install_title;
                        return resources.getString(i);
                    }
                }
            }
            Log.e("GoogleApiAvailability", string);
            return null;
        }
        i = R.string.common_android_wear_update_title;
        return resources.getString(i);
    }
}
