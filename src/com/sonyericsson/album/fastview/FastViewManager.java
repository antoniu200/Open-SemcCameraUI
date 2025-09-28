package com.sonyericsson.album.fastview;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.sonyericsson.album.fastview.IFastViewService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FastViewManager {
    private static final String LOG_TAG = "FastViewManager";
    private final Object mBitmapManagerClassInstance;
    private final ServiceConnection mConnection = new ServiceConnection() { // from class: com.sonyericsson.album.fastview.FastViewManager.1
        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            FastViewManager.this.mService = IFastViewService.Stub.asInterface(iBinder);
            if (FastViewManager.this.mOnPrewarmedListener != null) {
                FastViewManager.this.mOnPrewarmedListener.onPrewarmed();
            }
        }
    };
    private final Context mContext;
    private final Method mGetBitmapMethod;
    private OnPrewarmedListener mOnPrewarmedListener;
    private IFastViewService mService;

    public interface OnPrewarmedListener {
        void onPrewarmed();
    }

    public FastViewManager(@NonNull Context context) throws FastViewUnavailableException {
        this.mContext = context;
        try {
            Class<?> cls = ReflectionUtil.getClass(PackageManagerUtil.getApkPath(context, "com.sonyericsson.album"), getClassName(context));
            this.mGetBitmapMethod = ReflectionUtil.getMethod(cls, getMethodName(context), Context.class, Uri.class);
            this.mBitmapManagerClassInstance = cls.newInstance();
        } catch (Exception unused) {
            throw new FastViewUnavailableException();
        }
    }

    public void prewarm() {
        android.content.Intent intent = new android.content.Intent();
        intent.setClassName("com.sonyericsson.album", "com.sonyericsson.album.fastview.FastViewService");
        this.mContext.bindService(intent, this.mConnection, 1);
    }

    public void setOnPrewarmedListener(@Nullable OnPrewarmedListener onPrewarmedListener) {
        this.mOnPrewarmedListener = onPrewarmedListener;
    }

    public void prepare(@Nullable Uri uri) {
        if (this.mService == null || uri == null) {
            return;
        }
        try {
            this.mService.prepare(uri);
        } catch (RemoteException unused) {
            Log.d(LOG_TAG, "Not in pre-warmed state.");
        }
    }

    public void cooldown() {
        if (this.mService != null) {
            this.mContext.unbindService(this.mConnection);
        }
        this.mService = null;
    }

    public Bitmap getBitmap(Uri uri) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object objInvoke;
        try {
            objInvoke = this.mGetBitmapMethod.invoke(this.mBitmapManagerClassInstance, this.mContext, uri);
        } catch (Exception unused) {
            objInvoke = null;
        }
        if (objInvoke instanceof Bitmap) {
            return (Bitmap) objInvoke;
        }
        return null;
    }

    private String getClassName(@NonNull Context context) {
        return PackageManagerUtil.getMetadataString(context, "com.sonyericsson.album", "com.sonyericsson.album.fastview.class");
    }

    private String getMethodName(@NonNull Context context) {
        return PackageManagerUtil.getMetadataString(context, "com.sonyericsson.album", "com.sonyericsson.album.fastview.method");
    }
}
