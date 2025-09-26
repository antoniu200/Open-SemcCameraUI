// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.album.fastview;

import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.support.annotation.Nullable;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.content.ComponentName;
import android.support.annotation.NonNull;
import java.lang.reflect.Method;
import android.content.Context;
import android.content.ServiceConnection;

public class FastViewManager
{
    private static final String LOG_TAG = "FastViewManager";
    private final Object mBitmapManagerClassInstance;
    private final ServiceConnection mConnection;
    private final Context mContext;
    private final Method mGetBitmapMethod;
    private OnPrewarmedListener mOnPrewarmedListener;
    private IFastViewService mService;
    
    public FastViewManager(@NonNull final Context mContext) throws FastViewUnavailableException {
        this.mConnection = (ServiceConnection)new ServiceConnection() {
            final FastViewManager this$0;
            
            public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                this.this$0.mService = IFastViewService.Stub.asInterface(binder);
                if (this.this$0.mOnPrewarmedListener != null) {
                    this.this$0.mOnPrewarmedListener.onPrewarmed();
                }
            }
            
            public void onServiceDisconnected(final ComponentName componentName) {
            }
        };
        this.mContext = mContext;
        try {
            final Class<?> class1 = ReflectionUtil.getClass(PackageManagerUtil.getApkPath(mContext, "com.sonyericsson.album"), this.getClassName(mContext));
            this.mGetBitmapMethod = ReflectionUtil.getMethod(class1, this.getMethodName(mContext), Context.class, Uri.class);
            this.mBitmapManagerClassInstance = class1.newInstance();
        }
        catch (final Exception ex) {
            throw new FastViewUnavailableException();
        }
    }
    
    private String getClassName(@NonNull final Context context) {
        return PackageManagerUtil.getMetadataString(context, "com.sonyericsson.album", "com.sonyericsson.album.fastview.class");
    }
    
    private String getMethodName(@NonNull final Context context) {
        return PackageManagerUtil.getMetadataString(context, "com.sonyericsson.album", "com.sonyericsson.album.fastview.method");
    }
    
    public void cooldown() {
        if (this.mService != null) {
            this.mContext.unbindService(this.mConnection);
        }
        this.mService = null;
    }
    
    public Bitmap getBitmap(final Uri uri) {
        Object invoke;
        try {
            invoke = this.mGetBitmapMethod.invoke(this.mBitmapManagerClassInstance, this.mContext, uri);
        }
        catch (final Exception ex) {
            invoke = null;
        }
        if (invoke instanceof Bitmap) {
            return (Bitmap)invoke;
        }
        return null;
    }
    
    public void prepare(@Nullable final Uri uri) {
        if (this.mService != null && uri != null) {
            try {
                this.mService.prepare(uri);
            }
            catch (final RemoteException ex) {
                Log.d("FastViewManager", "Not in pre-warmed state.");
            }
        }
    }
    
    public void prewarm() {
        final Intent intent = new Intent();
        intent.setClassName("com.sonyericsson.album", "com.sonyericsson.album.fastview.FastViewService");
        this.mContext.bindService(intent, this.mConnection, 1);
    }
    
    public void setOnPrewarmedListener(@Nullable final OnPrewarmedListener mOnPrewarmedListener) {
        this.mOnPrewarmedListener = mOnPrewarmedListener;
    }
    
    public interface OnPrewarmedListener
    {
        void onPrewarmed();
    }
}
