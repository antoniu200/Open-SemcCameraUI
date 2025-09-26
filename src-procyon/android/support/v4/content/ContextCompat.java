// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.content;

import android.view.WindowManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.app.WallpaperManager;
import android.os.Vibrator;
import android.hardware.usb.UsbManager;
import android.app.UiModeManager;
import android.view.textservice.TextServicesManager;
import android.telephony.TelephonyManager;
import android.os.storage.StorageManager;
import android.hardware.SensorManager;
import android.app.SearchManager;
import android.os.PowerManager;
import android.app.NotificationManager;
import android.nfc.NfcManager;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.app.KeyguardManager;
import android.view.inputmethod.InputMethodManager;
import android.os.DropBoxManager;
import android.app.DownloadManager;
import android.app.admin.DevicePolicyManager;
import android.net.ConnectivityManager;
import android.content.ClipboardManager;
import android.media.AudioManager;
import android.app.AlarmManager;
import android.app.ActivityManager;
import android.accounts.AccountManager;
import android.accessibilityservice.AccessibilityService;
import android.net.nsd.NsdManager;
import android.media.MediaRouter;
import android.hardware.input.InputManager;
import android.os.UserManager;
import android.hardware.display.DisplayManager;
import android.bluetooth.BluetoothManager;
import android.print.PrintManager;
import android.hardware.ConsumerIrManager;
import android.view.accessibility.CaptioningManager;
import android.app.AppOpsManager;
import android.media.tv.TvInputManager;
import android.telecom.TelecomManager;
import android.content.RestrictionsManager;
import android.media.session.MediaSessionManager;
import android.media.projection.MediaProjectionManager;
import android.content.pm.LauncherApps;
import android.app.job.JobScheduler;
import android.hardware.camera2.CameraManager;
import android.os.BatteryManager;
import android.appwidget.AppWidgetManager;
import android.app.usage.UsageStatsManager;
import android.telephony.SubscriptionManager;
import java.util.HashMap;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.support.annotation.Nullable;
import android.os.Build$VERSION;
import android.os.Process;
import android.support.annotation.NonNull;
import android.content.Context;
import java.io.File;
import android.util.TypedValue;

public class ContextCompat
{
    private static final String TAG = "ContextCompat";
    private static final Object sLock;
    private static TypedValue sTempValue;
    
    static {
        sLock = new Object();
    }
    
    protected ContextCompat() {
    }
    
    private static File buildPath(final File file, final String... array) {
        final int length = array.length;
        int i = 0;
        File parent = file;
        while (i < length) {
            final String s = array[i];
            File file2;
            if (parent == null) {
                file2 = new File(s);
            }
            else {
                file2 = parent;
                if (s != null) {
                    file2 = new File(parent, s);
                }
            }
            ++i;
            parent = file2;
        }
        return parent;
    }
    
    public static int checkSelfPermission(@NonNull final Context context, @NonNull final String s) {
        if (s == null) {
            throw new IllegalArgumentException("permission is null");
        }
        return context.checkPermission(s, Process.myPid(), Process.myUid());
    }
    
    @Nullable
    public static Context createDeviceProtectedStorageContext(@NonNull final Context context) {
        if (Build$VERSION.SDK_INT >= 24) {
            return context.createDeviceProtectedStorageContext();
        }
        return null;
    }
    
    private static File createFilesDir(final File file) {
        synchronized (ContextCompat.class) {
            if (file.exists() || file.mkdirs()) {
                return file;
            }
            if (file.exists()) {
                return file;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to create files subdir ");
            sb.append(file.getPath());
            Log.w("ContextCompat", sb.toString());
            return null;
        }
    }
    
    public static File getCodeCacheDir(@NonNull final Context context) {
        if (Build$VERSION.SDK_INT >= 21) {
            return context.getCodeCacheDir();
        }
        return createFilesDir(new File(context.getApplicationInfo().dataDir, "code_cache"));
    }
    
    @ColorInt
    public static int getColor(@NonNull final Context context, @ColorRes final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            return context.getColor(n);
        }
        return context.getResources().getColor(n);
    }
    
    @Nullable
    public static ColorStateList getColorStateList(@NonNull final Context context, @ColorRes final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            return context.getColorStateList(n);
        }
        return context.getResources().getColorStateList(n);
    }
    
    @Nullable
    public static File getDataDir(@NonNull final Context context) {
        if (Build$VERSION.SDK_INT >= 24) {
            return context.getDataDir();
        }
        final String dataDir = context.getApplicationInfo().dataDir;
        File file;
        if (dataDir != null) {
            file = new File(dataDir);
        }
        else {
            file = null;
        }
        return file;
    }
    
    @Nullable
    public static Drawable getDrawable(@NonNull final Context context, @DrawableRes int resourceId) {
        if (Build$VERSION.SDK_INT >= 21) {
            return context.getDrawable(resourceId);
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return context.getResources().getDrawable(resourceId);
        }
        synchronized (ContextCompat.sLock) {
            if (ContextCompat.sTempValue == null) {
                ContextCompat.sTempValue = new TypedValue();
            }
            context.getResources().getValue(resourceId, ContextCompat.sTempValue, true);
            resourceId = ContextCompat.sTempValue.resourceId;
            monitorexit(ContextCompat.sLock);
            return context.getResources().getDrawable(resourceId);
        }
    }
    
    @NonNull
    public static File[] getExternalCacheDirs(@NonNull final Context context) {
        if (Build$VERSION.SDK_INT >= 19) {
            return context.getExternalCacheDirs();
        }
        return new File[] { context.getExternalCacheDir() };
    }
    
    @NonNull
    public static File[] getExternalFilesDirs(@NonNull final Context context, @Nullable final String s) {
        if (Build$VERSION.SDK_INT >= 19) {
            return context.getExternalFilesDirs(s);
        }
        return new File[] { context.getExternalFilesDir(s) };
    }
    
    @Nullable
    public static File getNoBackupFilesDir(@NonNull final Context context) {
        if (Build$VERSION.SDK_INT >= 21) {
            return context.getNoBackupFilesDir();
        }
        return createFilesDir(new File(context.getApplicationInfo().dataDir, "no_backup"));
    }
    
    @NonNull
    public static File[] getObbDirs(@NonNull final Context context) {
        if (Build$VERSION.SDK_INT >= 19) {
            return context.getObbDirs();
        }
        return new File[] { context.getObbDir() };
    }
    
    @Nullable
    public static <T> T getSystemService(@NonNull final Context context, @NonNull final Class<T> clazz) {
        if (Build$VERSION.SDK_INT >= 23) {
            return (T)context.getSystemService((Class)clazz);
        }
        final String systemServiceName = getSystemServiceName(context, clazz);
        Object systemService;
        if (systemServiceName != null) {
            systemService = context.getSystemService(systemServiceName);
        }
        else {
            systemService = null;
        }
        return (T)systemService;
    }
    
    @Nullable
    public static String getSystemServiceName(@NonNull final Context context, @NonNull final Class<?> key) {
        if (Build$VERSION.SDK_INT >= 23) {
            return context.getSystemServiceName((Class)key);
        }
        return LegacyServiceMapHolder.SERVICES.get(key);
    }
    
    public static boolean isDeviceProtectedStorage(@NonNull final Context context) {
        return Build$VERSION.SDK_INT >= 24 && context.isDeviceProtectedStorage();
    }
    
    public static boolean startActivities(@NonNull final Context context, @NonNull final Intent[] array) {
        return startActivities(context, array, null);
    }
    
    public static boolean startActivities(@NonNull final Context context, @NonNull final Intent[] array, @Nullable final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 16) {
            context.startActivities(array, bundle);
        }
        else {
            context.startActivities(array);
        }
        return true;
    }
    
    public static void startActivity(@NonNull final Context context, @NonNull final Intent intent, @Nullable final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 16) {
            context.startActivity(intent, bundle);
        }
        else {
            context.startActivity(intent);
        }
    }
    
    public static void startForegroundService(@NonNull final Context context, @NonNull final Intent intent) {
        if (Build$VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
        }
        else {
            context.startService(intent);
        }
    }
    
    private static final class LegacyServiceMapHolder
    {
        static final HashMap<Class<?>, String> SERVICES;
        
        static {
            SERVICES = new HashMap<Class<?>, String>();
            if (Build$VERSION.SDK_INT > 22) {
                LegacyServiceMapHolder.SERVICES.put(SubscriptionManager.class, "telephony_subscription_service");
                LegacyServiceMapHolder.SERVICES.put(UsageStatsManager.class, "usagestats");
            }
            if (Build$VERSION.SDK_INT > 21) {
                LegacyServiceMapHolder.SERVICES.put(AppWidgetManager.class, "appwidget");
                LegacyServiceMapHolder.SERVICES.put(BatteryManager.class, "batterymanager");
                LegacyServiceMapHolder.SERVICES.put(CameraManager.class, "camera");
                LegacyServiceMapHolder.SERVICES.put(JobScheduler.class, "jobscheduler");
                LegacyServiceMapHolder.SERVICES.put(LauncherApps.class, "launcherapps");
                LegacyServiceMapHolder.SERVICES.put(MediaProjectionManager.class, "media_projection");
                LegacyServiceMapHolder.SERVICES.put(MediaSessionManager.class, "media_session");
                LegacyServiceMapHolder.SERVICES.put(RestrictionsManager.class, "restrictions");
                LegacyServiceMapHolder.SERVICES.put(TelecomManager.class, "telecom");
                LegacyServiceMapHolder.SERVICES.put(TvInputManager.class, "tv_input");
            }
            if (Build$VERSION.SDK_INT > 19) {
                LegacyServiceMapHolder.SERVICES.put(AppOpsManager.class, "appops");
                LegacyServiceMapHolder.SERVICES.put(CaptioningManager.class, "captioning");
                LegacyServiceMapHolder.SERVICES.put(ConsumerIrManager.class, "consumer_ir");
                LegacyServiceMapHolder.SERVICES.put(PrintManager.class, "print");
            }
            if (Build$VERSION.SDK_INT > 18) {
                LegacyServiceMapHolder.SERVICES.put(BluetoothManager.class, "bluetooth");
            }
            if (Build$VERSION.SDK_INT > 17) {
                LegacyServiceMapHolder.SERVICES.put(DisplayManager.class, "display");
                LegacyServiceMapHolder.SERVICES.put(UserManager.class, "user");
            }
            if (Build$VERSION.SDK_INT > 16) {
                LegacyServiceMapHolder.SERVICES.put(InputManager.class, "input");
                LegacyServiceMapHolder.SERVICES.put(MediaRouter.class, "media_router");
                LegacyServiceMapHolder.SERVICES.put(NsdManager.class, "servicediscovery");
            }
            LegacyServiceMapHolder.SERVICES.put(AccessibilityService.class, "accessibility");
            LegacyServiceMapHolder.SERVICES.put(AccountManager.class, "account");
            LegacyServiceMapHolder.SERVICES.put(ActivityManager.class, "activity");
            LegacyServiceMapHolder.SERVICES.put(AlarmManager.class, "alarm");
            LegacyServiceMapHolder.SERVICES.put(AudioManager.class, "audio");
            LegacyServiceMapHolder.SERVICES.put(ClipboardManager.class, "clipboard");
            LegacyServiceMapHolder.SERVICES.put(ConnectivityManager.class, "connectivity");
            LegacyServiceMapHolder.SERVICES.put(DevicePolicyManager.class, "device_policy");
            LegacyServiceMapHolder.SERVICES.put(DownloadManager.class, "download");
            LegacyServiceMapHolder.SERVICES.put(DropBoxManager.class, "dropbox");
            LegacyServiceMapHolder.SERVICES.put(InputMethodManager.class, "input_method");
            LegacyServiceMapHolder.SERVICES.put(KeyguardManager.class, "keyguard");
            LegacyServiceMapHolder.SERVICES.put(LayoutInflater.class, "layout_inflater");
            LegacyServiceMapHolder.SERVICES.put(LocationManager.class, "location");
            LegacyServiceMapHolder.SERVICES.put(NfcManager.class, "nfc");
            LegacyServiceMapHolder.SERVICES.put(NotificationManager.class, "notification");
            LegacyServiceMapHolder.SERVICES.put(PowerManager.class, "power");
            LegacyServiceMapHolder.SERVICES.put(SearchManager.class, "search");
            LegacyServiceMapHolder.SERVICES.put(SensorManager.class, "sensor");
            LegacyServiceMapHolder.SERVICES.put(StorageManager.class, "storage");
            LegacyServiceMapHolder.SERVICES.put(TelephonyManager.class, "phone");
            LegacyServiceMapHolder.SERVICES.put(TextServicesManager.class, "textservices");
            LegacyServiceMapHolder.SERVICES.put(UiModeManager.class, "uimode");
            LegacyServiceMapHolder.SERVICES.put(UsbManager.class, "usb");
            LegacyServiceMapHolder.SERVICES.put(Vibrator.class, "vibrator");
            LegacyServiceMapHolder.SERVICES.put(WallpaperManager.class, "wallpaper");
            LegacyServiceMapHolder.SERVICES.put(WifiP2pManager.class, "wifip2p");
            LegacyServiceMapHolder.SERVICES.put(WifiManager.class, "wifi");
            LegacyServiceMapHolder.SERVICES.put(WindowManager.class, "window");
        }
    }
}
