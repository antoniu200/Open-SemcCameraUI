// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.app.SharedElementCallback$OnSharedElementsReadyListener;
import java.util.Map;
import java.util.List;
import android.content.Context;
import android.os.Parcelable;
import android.graphics.RectF;
import android.graphics.Matrix;
import android.support.annotation.RequiresApi;
import android.content.IntentSender$SendIntentException;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.support.annotation.IdRes;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntRange;
import android.support.v13.view.DragAndDropPermissionsCompat;
import android.view.DragEvent;
import android.support.annotation.Nullable;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.RestrictTo;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.app.Activity;
import android.support.v4.content.ContextCompat;

public class ActivityCompat extends ContextCompat
{
    private static PermissionCompatDelegate sDelegate;
    
    protected ActivityCompat() {
    }
    
    public static void finishAffinity(@NonNull final Activity activity) {
        if (Build$VERSION.SDK_INT >= 16) {
            activity.finishAffinity();
        }
        else {
            activity.finish();
        }
    }
    
    public static void finishAfterTransition(@NonNull final Activity activity) {
        if (Build$VERSION.SDK_INT >= 21) {
            activity.finishAfterTransition();
        }
        else {
            activity.finish();
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static PermissionCompatDelegate getPermissionCompatDelegate() {
        return ActivityCompat.sDelegate;
    }
    
    @Nullable
    public static Uri getReferrer(@NonNull final Activity activity) {
        if (Build$VERSION.SDK_INT >= 22) {
            return activity.getReferrer();
        }
        final Intent intent = activity.getIntent();
        final Uri uri = (Uri)intent.getParcelableExtra("android.intent.extra.REFERRER");
        if (uri != null) {
            return uri;
        }
        final String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        if (stringExtra != null) {
            return Uri.parse(stringExtra);
        }
        return null;
    }
    
    @Deprecated
    public static boolean invalidateOptionsMenu(final Activity activity) {
        activity.invalidateOptionsMenu();
        return true;
    }
    
    public static void postponeEnterTransition(@NonNull final Activity activity) {
        if (Build$VERSION.SDK_INT >= 21) {
            activity.postponeEnterTransition();
        }
    }
    
    @Nullable
    public static DragAndDropPermissionsCompat requestDragAndDropPermissions(final Activity activity, final DragEvent dragEvent) {
        return DragAndDropPermissionsCompat.request(activity, dragEvent);
    }
    
    public static void requestPermissions(@NonNull final Activity activity, @NonNull final String[] array, @IntRange(from = 0L) final int n) {
        if (ActivityCompat.sDelegate != null && ActivityCompat.sDelegate.requestPermissions(activity, array, n)) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            if (activity instanceof RequestPermissionsRequestCodeValidator) {
                ((RequestPermissionsRequestCodeValidator)activity).validateRequestPermissionsRequestCode(n);
            }
            activity.requestPermissions(array, n);
        }
        else if (activity instanceof OnRequestPermissionsResultCallback) {
            new Handler(Looper.getMainLooper()).post((Runnable)new Runnable(array, activity, n) {
                final Activity val$activity;
                final String[] val$permissions;
                final int val$requestCode;
                
                @Override
                public void run() {
                    final int[] array = new int[this.val$permissions.length];
                    final PackageManager packageManager = this.val$activity.getPackageManager();
                    final String packageName = this.val$activity.getPackageName();
                    for (int length = this.val$permissions.length, i = 0; i < length; ++i) {
                        array[i] = packageManager.checkPermission(this.val$permissions[i], packageName);
                    }
                    ((OnRequestPermissionsResultCallback)this.val$activity).onRequestPermissionsResult(this.val$requestCode, this.val$permissions, array);
                }
            });
        }
    }
    
    @NonNull
    public static <T extends View> T requireViewById(@NonNull final Activity activity, @IdRes final int n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return (T)activity.requireViewById(n);
        }
        final View viewById = activity.findViewById(n);
        if (viewById == null) {
            throw new IllegalArgumentException("ID does not reference a View inside this Activity");
        }
        return (T)viewById;
    }
    
    public static void setEnterSharedElementCallback(@NonNull final Activity activity, @Nullable final SharedElementCallback sharedElementCallback) {
        if (Build$VERSION.SDK_INT >= 21) {
            SharedElementCallback21Impl enterSharedElementCallback;
            if (sharedElementCallback != null) {
                enterSharedElementCallback = new SharedElementCallback21Impl(sharedElementCallback);
            }
            else {
                enterSharedElementCallback = null;
            }
            activity.setEnterSharedElementCallback((android.app.SharedElementCallback)enterSharedElementCallback);
        }
    }
    
    public static void setExitSharedElementCallback(@NonNull final Activity activity, @Nullable final SharedElementCallback sharedElementCallback) {
        if (Build$VERSION.SDK_INT >= 21) {
            SharedElementCallback21Impl exitSharedElementCallback;
            if (sharedElementCallback != null) {
                exitSharedElementCallback = new SharedElementCallback21Impl(sharedElementCallback);
            }
            else {
                exitSharedElementCallback = null;
            }
            activity.setExitSharedElementCallback((android.app.SharedElementCallback)exitSharedElementCallback);
        }
    }
    
    public static void setPermissionCompatDelegate(@Nullable final PermissionCompatDelegate sDelegate) {
        ActivityCompat.sDelegate = sDelegate;
    }
    
    public static boolean shouldShowRequestPermissionRationale(@NonNull final Activity activity, @NonNull final String s) {
        return Build$VERSION.SDK_INT >= 23 && activity.shouldShowRequestPermissionRationale(s);
    }
    
    public static void startActivityForResult(@NonNull final Activity activity, @NonNull final Intent intent, final int n, @Nullable final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 16) {
            activity.startActivityForResult(intent, n, bundle);
        }
        else {
            activity.startActivityForResult(intent, n);
        }
    }
    
    public static void startIntentSenderForResult(@NonNull final Activity activity, @NonNull final IntentSender intentSender, final int n, @Nullable final Intent intent, final int n2, final int n3, final int n4, @Nullable final Bundle bundle) throws IntentSender$SendIntentException {
        if (Build$VERSION.SDK_INT >= 16) {
            activity.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4, bundle);
        }
        else {
            activity.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4);
        }
    }
    
    public static void startPostponedEnterTransition(@NonNull final Activity activity) {
        if (Build$VERSION.SDK_INT >= 21) {
            activity.startPostponedEnterTransition();
        }
    }
    
    public interface OnRequestPermissionsResultCallback
    {
        void onRequestPermissionsResult(final int p0, @NonNull final String[] p1, @NonNull final int[] p2);
    }
    
    public interface PermissionCompatDelegate
    {
        boolean onActivityResult(@NonNull final Activity p0, @IntRange(from = 0L) final int p1, final int p2, @Nullable final Intent p3);
        
        boolean requestPermissions(@NonNull final Activity p0, @NonNull final String[] p1, @IntRange(from = 0L) final int p2);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public interface RequestPermissionsRequestCodeValidator
    {
        void validateRequestPermissionsRequestCode(final int p0);
    }
    
    @RequiresApi(21)
    private static class SharedElementCallback21Impl extends android.app.SharedElementCallback
    {
        private final SharedElementCallback mCallback;
        
        SharedElementCallback21Impl(final SharedElementCallback mCallback) {
            this.mCallback = mCallback;
        }
        
        public Parcelable onCaptureSharedElementSnapshot(final View view, final Matrix matrix, final RectF rectF) {
            return this.mCallback.onCaptureSharedElementSnapshot(view, matrix, rectF);
        }
        
        public View onCreateSnapshotView(final Context context, final Parcelable parcelable) {
            return this.mCallback.onCreateSnapshotView(context, parcelable);
        }
        
        public void onMapSharedElements(final List<String> list, final Map<String, View> map) {
            this.mCallback.onMapSharedElements(list, map);
        }
        
        public void onRejectSharedElements(final List<View> list) {
            this.mCallback.onRejectSharedElements(list);
        }
        
        public void onSharedElementEnd(final List<String> list, final List<View> list2, final List<View> list3) {
            this.mCallback.onSharedElementEnd(list, list2, list3);
        }
        
        public void onSharedElementStart(final List<String> list, final List<View> list2, final List<View> list3) {
            this.mCallback.onSharedElementStart(list, list2, list3);
        }
        
        @RequiresApi(23)
        public void onSharedElementsArrived(final List<String> list, final List<View> list2, final SharedElementCallback$OnSharedElementsReadyListener sharedElementCallback$OnSharedElementsReadyListener) {
            this.mCallback.onSharedElementsArrived(list, list2, (SharedElementCallback.OnSharedElementsReadyListener)new SharedElementCallback.OnSharedElementsReadyListener(this, sharedElementCallback$OnSharedElementsReadyListener) {
                final SharedElementCallback21Impl this$0;
                final SharedElementCallback$OnSharedElementsReadyListener val$listener;
                
                @Override
                public void onSharedElementsReady() {
                    this.val$listener.onSharedElementsReady();
                }
            });
        }
    }
}
