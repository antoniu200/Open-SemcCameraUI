// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.content.Context;
import android.content.ComponentName;
import android.os.Build$VERSION;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.app.Activity;

public final class NavUtils
{
    public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
    private static final String TAG = "NavUtils";
    
    private NavUtils() {
    }
    
    @Nullable
    public static Intent getParentActivityIntent(@NonNull final Activity activity) {
        if (Build$VERSION.SDK_INT >= 16) {
            final Intent parentActivityIntent = activity.getParentActivityIntent();
            if (parentActivityIntent != null) {
                return parentActivityIntent;
            }
        }
        final String parentActivityName = getParentActivityName(activity);
        if (parentActivityName == null) {
            return null;
        }
        final ComponentName component = new ComponentName((Context)activity, parentActivityName);
        try {
            Intent intent;
            if (getParentActivityName((Context)activity, component) == null) {
                intent = Intent.makeMainActivity(component);
            }
            else {
                intent = new Intent().setComponent(component);
            }
            return intent;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getParentActivityIntent: bad parentActivityName '");
            sb.append(parentActivityName);
            sb.append("' in manifest");
            Log.e("NavUtils", sb.toString());
            return null;
        }
    }
    
    @Nullable
    public static Intent getParentActivityIntent(@NonNull final Context context, @NonNull ComponentName component) throws PackageManager$NameNotFoundException {
        final String parentActivityName = getParentActivityName(context, component);
        if (parentActivityName == null) {
            return null;
        }
        component = new ComponentName(component.getPackageName(), parentActivityName);
        Intent intent;
        if (getParentActivityName(context, component) == null) {
            intent = Intent.makeMainActivity(component);
        }
        else {
            intent = new Intent().setComponent(component);
        }
        return intent;
    }
    
    @Nullable
    public static Intent getParentActivityIntent(@NonNull final Context context, @NonNull final Class<?> clazz) throws PackageManager$NameNotFoundException {
        final String parentActivityName = getParentActivityName(context, new ComponentName(context, (Class)clazz));
        if (parentActivityName == null) {
            return null;
        }
        final ComponentName component = new ComponentName(context, parentActivityName);
        Intent intent;
        if (getParentActivityName(context, component) == null) {
            intent = Intent.makeMainActivity(component);
        }
        else {
            intent = new Intent().setComponent(component);
        }
        return intent;
    }
    
    @Nullable
    public static String getParentActivityName(@NonNull final Activity activity) {
        try {
            return getParentActivityName((Context)activity, activity.getComponentName());
        }
        catch (final PackageManager$NameNotFoundException cause) {
            throw new IllegalArgumentException((Throwable)cause);
        }
    }
    
    @Nullable
    public static String getParentActivityName(@NonNull final Context context, @NonNull final ComponentName componentName) throws PackageManager$NameNotFoundException {
        final ActivityInfo activityInfo = context.getPackageManager().getActivityInfo(componentName, 128);
        if (Build$VERSION.SDK_INT >= 16) {
            final String parentActivityName = activityInfo.parentActivityName;
            if (parentActivityName != null) {
                return parentActivityName;
            }
        }
        if (activityInfo.metaData == null) {
            return null;
        }
        final String string = activityInfo.metaData.getString("android.support.PARENT_ACTIVITY");
        if (string == null) {
            return null;
        }
        String string2 = string;
        if (string.charAt(0) == '.') {
            final StringBuilder sb = new StringBuilder();
            sb.append(context.getPackageName());
            sb.append(string);
            string2 = sb.toString();
        }
        return string2;
    }
    
    public static void navigateUpFromSameTask(@NonNull final Activity activity) {
        final Intent parentActivityIntent = getParentActivityIntent(activity);
        if (parentActivityIntent == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Activity ");
            sb.append(activity.getClass().getSimpleName());
            sb.append(" does not have a parent activity name specified.");
            sb.append(" (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data> ");
            sb.append(" element in your manifest?)");
            throw new IllegalArgumentException(sb.toString());
        }
        navigateUpTo(activity, parentActivityIntent);
    }
    
    public static void navigateUpTo(@NonNull final Activity activity, @NonNull final Intent intent) {
        if (Build$VERSION.SDK_INT >= 16) {
            activity.navigateUpTo(intent);
        }
        else {
            intent.addFlags(67108864);
            activity.startActivity(intent);
            activity.finish();
        }
    }
    
    public static boolean shouldUpRecreateTask(@NonNull final Activity activity, @NonNull final Intent intent) {
        if (Build$VERSION.SDK_INT >= 16) {
            return activity.shouldUpRecreateTask(intent);
        }
        final String action = activity.getIntent().getAction();
        return action != null && !action.equals("android.intent.action.MAIN");
    }
}
