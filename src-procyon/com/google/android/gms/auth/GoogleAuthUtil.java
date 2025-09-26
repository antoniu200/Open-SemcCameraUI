// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth;

import java.net.URISyntaxException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.auth.firstparty.shared.zzd;
import android.os.SystemClock;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.text.TextUtils;
import android.os.Parcelable;
import android.content.Intent;
import android.accounts.Account;
import java.util.List;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.internal.zzau;
import java.io.IOException;
import android.content.ServiceConnection;
import com.google.android.gms.common.internal.zzl;
import com.google.android.gms.common.zza;
import android.os.Bundle;
import com.google.android.gms.common.internal.zzx;
import android.content.Context;
import android.os.Build$VERSION;
import android.content.ComponentName;

public final class GoogleAuthUtil
{
    public static final int CHANGE_TYPE_ACCOUNT_ADDED = 1;
    public static final int CHANGE_TYPE_ACCOUNT_REMOVED = 2;
    public static final int CHANGE_TYPE_ACCOUNT_RENAMED_FROM = 3;
    public static final int CHANGE_TYPE_ACCOUNT_RENAMED_TO = 4;
    public static final String GOOGLE_ACCOUNT_TYPE = "com.google";
    public static final String KEY_ANDROID_PACKAGE_NAME;
    public static final String KEY_CALLER_UID;
    public static final String KEY_REQUEST_ACTIONS = "request_visible_actions";
    @Deprecated
    public static final String KEY_REQUEST_VISIBLE_ACTIVITIES = "request_visible_actions";
    public static final String KEY_SUPPRESS_PROGRESS_SCREEN = "suppressProgressScreen";
    private static final ComponentName zzRw;
    private static final ComponentName zzRx;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        KEY_CALLER_UID = "callerUid";
        final int sdk_INT2 = Build$VERSION.SDK_INT;
        KEY_ANDROID_PACKAGE_NAME = "androidPackageName";
        zzRw = new ComponentName("com.google.android.gms", "com.google.android.gms.auth.GetToken");
        zzRx = new ComponentName("com.google.android.gms", "com.google.android.gms.recovery.RecoveryService");
    }
    
    private GoogleAuthUtil() {
    }
    
    public static void clearToken(Context context, String string) throws GooglePlayServicesAvailabilityException, GoogleAuthException, IOException {
        final Context applicationContext = context.getApplicationContext();
        zzx.zzcj("Calling this from your main thread can lead to deadlock");
        zzaa(applicationContext);
        final Bundle bundle = new Bundle();
        final String packageName = context.getApplicationInfo().packageName;
        bundle.putString("clientPackageName", packageName);
        if (!bundle.containsKey(GoogleAuthUtil.KEY_ANDROID_PACKAGE_NAME)) {
            bundle.putString(GoogleAuthUtil.KEY_ANDROID_PACKAGE_NAME, packageName);
        }
        context = (Context)new zza();
        final zzl zzal = zzl.zzal(applicationContext);
        if (!zzal.zza(GoogleAuthUtil.zzRw, (ServiceConnection)context, "GoogleAuthUtil")) {
            throw new IOException("Could not bind to service with the given context.");
        }
        try {
            try {
                final Bundle zza = zzau.zza.zza(((zza)context).zzno()).zza(string, bundle);
                string = zza.getString("Error");
                if (!zza.getBoolean("booleanResult")) {
                    throw new GoogleAuthException(string);
                }
                zzal.zzb(GoogleAuthUtil.zzRw, (ServiceConnection)context, "GoogleAuthUtil");
                return;
            }
            finally {}
        }
        catch (final InterruptedException ex) {
            throw new GoogleAuthException("Interrupted");
        }
        catch (final RemoteException ex2) {
            Log.i("GoogleAuthUtil", "GMS remote exception ", (Throwable)ex2);
            throw new IOException("remote exception");
        }
        zzal.zzb(GoogleAuthUtil.zzRw, (ServiceConnection)context, "GoogleAuthUtil");
    }
    
    public static List<AccountChangeEvent> getAccountChangeEvents(Context context, final int eventIndex, final String accountName) throws GoogleAuthException, IOException {
        zzx.zzh(accountName, "accountName must be provided");
        zzx.zzcj("Calling this from your main thread can lead to deadlock");
        final Context applicationContext = context.getApplicationContext();
        zzaa(applicationContext);
        context = (Context)new zza();
        final zzl zzal = zzl.zzal(applicationContext);
        if (!zzal.zza(GoogleAuthUtil.zzRw, (ServiceConnection)context, "GoogleAuthUtil")) {
            throw new IOException("Could not bind to service with the given context.");
        }
        try {
            try {
                final List<AccountChangeEvent> events = zzau.zza.zza(((zza)context).zzno()).zza(new AccountChangeEventsRequest().setAccountName(accountName).setEventIndex(eventIndex)).getEvents();
                zzal.zzb(GoogleAuthUtil.zzRw, (ServiceConnection)context, "GoogleAuthUtil");
                return events;
            }
            finally {}
        }
        catch (final InterruptedException ex) {
            throw new GoogleAuthException("Interrupted");
        }
        catch (final RemoteException ex2) {
            Log.i("GoogleAuthUtil", "GMS remote exception ", (Throwable)ex2);
            throw new IOException("remote exception");
        }
        zzal.zzb(GoogleAuthUtil.zzRw, (ServiceConnection)context, "GoogleAuthUtil");
    }
    
    public static String getAccountId(final Context context, final String s) throws GoogleAuthException, IOException {
        zzx.zzh(s, "accountName must be provided");
        zzx.zzcj("Calling this from your main thread can lead to deadlock");
        zzaa(context.getApplicationContext());
        return getToken(context, s, "^^_account_id_^^", new Bundle());
    }
    
    public static String getToken(final Context context, final Account account, final String s) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return getToken(context, account, s, new Bundle());
    }
    
    public static String getToken(final Context context, final Account account, final String s, final Bundle bundle) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return zza(context, account, s, bundle).getToken();
    }
    
    @Deprecated
    public static String getToken(final Context context, final String s, final String s2) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return getToken(context, new Account(s, "com.google"), s2);
    }
    
    @Deprecated
    public static String getToken(final Context context, final String s, final String s2, final Bundle bundle) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return getToken(context, new Account(s, "com.google"), s2, bundle);
    }
    
    public static String getTokenWithNotification(final Context context, final Account account, final String s, final Bundle bundle) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        return zzb(context, account, s, bundle).getToken();
    }
    
    public static String getTokenWithNotification(final Context context, final Account account, final String s, final Bundle bundle, final Intent intent) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        zzi(intent);
        Bundle bundle2 = bundle;
        if (bundle == null) {
            bundle2 = new Bundle();
        }
        bundle2.putParcelable("callback_intent", (Parcelable)intent);
        bundle2.putBoolean("handle_notification", true);
        return zzc(context, account, s, bundle2).getToken();
    }
    
    public static String getTokenWithNotification(final Context context, final Account account, final String s, Bundle o, final String s2, final Bundle bundle) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        if (TextUtils.isEmpty((CharSequence)s2)) {
            throw new IllegalArgumentException("Authority cannot be empty or null.");
        }
        Object o2;
        if ((o2 = o) == null) {
            o2 = new Bundle();
        }
        if ((o = bundle) == null) {
            o = new Bundle();
        }
        ContentResolver.validateSyncExtrasBundle((Bundle)o);
        ((Bundle)o2).putString("authority", s2);
        ((Bundle)o2).putBundle("sync_extras", (Bundle)o);
        ((Bundle)o2).putBoolean("handle_notification", true);
        return zzc(context, account, s, (Bundle)o2).getToken();
    }
    
    @Deprecated
    public static String getTokenWithNotification(final Context context, final String s, final String s2, final Bundle bundle) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        return getTokenWithNotification(context, new Account(s, "com.google"), s2, bundle);
    }
    
    @Deprecated
    public static String getTokenWithNotification(final Context context, final String s, final String s2, final Bundle bundle, final Intent intent) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        return getTokenWithNotification(context, new Account(s, "com.google"), s2, bundle, intent);
    }
    
    @Deprecated
    public static String getTokenWithNotification(final Context context, final String s, final String s2, final Bundle bundle, final String s3, final Bundle bundle2) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        return getTokenWithNotification(context, new Account(s, "com.google"), s2, bundle, s3, bundle2);
    }
    
    @Deprecated
    public static void invalidateToken(final Context context, final String s) {
        AccountManager.get(context).invalidateAuthToken("com.google", s);
    }
    
    public static TokenData zza(Context context, final Account account, final String s, Bundle bundle) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        final Context applicationContext = context.getApplicationContext();
        zzx.zzcj("Calling this from your main thread can lead to deadlock");
        zzaa(applicationContext);
        if (bundle == null) {
            bundle = new Bundle();
        }
        else {
            bundle = new Bundle(bundle);
        }
        final String packageName = context.getApplicationInfo().packageName;
        bundle.putString("clientPackageName", packageName);
        if (TextUtils.isEmpty((CharSequence)bundle.getString(GoogleAuthUtil.KEY_ANDROID_PACKAGE_NAME))) {
            bundle.putString(GoogleAuthUtil.KEY_ANDROID_PACKAGE_NAME, packageName);
        }
        bundle.putLong("service_connection_start_time_millis", SystemClock.elapsedRealtime());
        context = (Context)new zza();
        final zzl zzal = zzl.zzal(applicationContext);
        if (!zzal.zza(GoogleAuthUtil.zzRw, (ServiceConnection)context, "GoogleAuthUtil")) {
            throw new IOException("Could not bind to service with the given context.");
        }
        try {
            final Bundle zza = zzau.zza.zza(((zza)context).zzno()).zza(account, s, bundle);
            if (zza == null) {
                Log.w("GoogleAuthUtil", "Binder call returned null.");
                throw new GoogleAuthException("ServiceUnavailable");
            }
            final TokenData zza2 = TokenData.zza(zza, "tokenDetails");
            if (zza2 != null) {
                zzal.zzb(GoogleAuthUtil.zzRw, (ServiceConnection)context, "GoogleAuthUtil");
                return zza2;
            }
            final String string = zza.getString("Error");
            final Intent intent = (Intent)zza.getParcelable("userRecoveryIntent");
            final zzd zzbE = zzd.zzbE(string);
            if (zzd.zza(zzbE)) {
                throw new UserRecoverableAuthException(string, intent);
            }
            if (zzd.zzc(zzbE)) {
                throw new IOException(string);
            }
            throw new GoogleAuthException(string);
        }
        catch (final InterruptedException ex) {
            throw new GoogleAuthException("Interrupted");
        }
        catch (final RemoteException ex2) {
            Log.i("GoogleAuthUtil", "GMS remote exception ", (Throwable)ex2);
            throw new IOException("remote exception");
        }
        zzal.zzb(GoogleAuthUtil.zzRw, (ServiceConnection)context, "GoogleAuthUtil");
    }
    
    private static void zzaa(final Context context) throws GoogleAuthException {
        try {
            GooglePlayServicesUtil.zzaa(context);
        }
        catch (final GooglePlayServicesNotAvailableException ex) {
            throw new GoogleAuthException(ex.getMessage());
        }
        catch (final GooglePlayServicesRepairableException ex2) {
            throw new GooglePlayServicesAvailabilityException(ex2.getConnectionStatusCode(), ex2.getMessage(), ex2.getIntent());
        }
    }
    
    public static TokenData zzb(final Context context, final Account account, final String s, final Bundle bundle) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        Bundle bundle2 = bundle;
        if (bundle == null) {
            bundle2 = new Bundle();
        }
        bundle2.putBoolean("handle_notification", true);
        return zzc(context, account, s, bundle2);
    }
    
    private static TokenData zzc(final Context context, final Account account, final String s, final Bundle bundle) throws IOException, GoogleAuthException {
        Bundle bundle2 = bundle;
        if (bundle == null) {
            bundle2 = new Bundle();
        }
        try {
            final TokenData zza = zza(context, account, s, bundle2);
            GooglePlayServicesUtil.zzac(context);
            return zza;
        }
        catch (final UserRecoverableAuthException ex) {
            GooglePlayServicesUtil.zzac(context);
            throw new UserRecoverableNotifiedException("User intervention required. Notification has been pushed.");
        }
        catch (final GooglePlayServicesAvailabilityException ex2) {
            GooglePlayServicesUtil.showErrorNotification(ex2.getConnectionStatusCode(), context);
            throw new UserRecoverableNotifiedException("User intervention required. Notification has been pushed.");
        }
    }
    
    private static void zzi(final Intent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("Callback cannot be null.");
        }
        final String uri = intent.toUri(1);
        try {
            Intent.parseUri(uri, 1);
        }
        catch (final URISyntaxException ex) {
            throw new IllegalArgumentException("Parameter callback contains invalid data. It must be serializable using toUri() and parseUri().");
        }
    }
}
