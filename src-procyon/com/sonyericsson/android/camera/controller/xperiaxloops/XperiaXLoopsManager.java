// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.controller.xperiaxloops;

import android.support.annotation.UiThread;
import android.content.ServiceConnection;
import android.content.Intent;
import com.sonymobile.xperiaxloops.IXperiaXLoopsServiceCallback;
import android.os.RemoteException;
import com.sonyericsson.android.camera.util.CamLog;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import com.sonymobile.xperiaxloops.IXperiaXLoopsService;
import android.content.Context;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007*\u0002\t\f\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0011\u001a\u00020\u0012H\u0007J\b\u0010\u0013\u001a\u00020\u0012H\u0007J\u0010\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0006H\u0002J\b\u0010\u0016\u001a\u00020\u0012H\u0002J\b\u0010\u0017\u001a\u00020\u0012H\u0002R\u0011\u0010\u0005\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0007R\u0010\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\nR\u0010\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019" }, d2 = { "Lcom/sonyericsson/android/camera/controller/xperiaxloops/XperiaXLoopsManager;", "", "mContext", "Landroid/content/Context;", "(Landroid/content/Context;)V", "isConnected", "", "()Z", "mCallback", "com/sonyericsson/android/camera/controller/xperiaxloops/XperiaXLoopsManager$mCallback$1", "Lcom/sonyericsson/android/camera/controller/xperiaxloops/XperiaXLoopsManager$mCallback$1;", "mConnection", "com/sonyericsson/android/camera/controller/xperiaxloops/XperiaXLoopsManager$mConnection$1", "Lcom/sonyericsson/android/camera/controller/xperiaxloops/XperiaXLoopsManager$mConnection$1;", "mIsConnectionRequested", "mService", "Lcom/sonymobile/xperiaxloops/IXperiaXLoopsService;", "connect", "", "disconnect", "notifyShowLoopsByApps", "isShown", "registerCallback", "unregisterCallback", "Companion", "SemcCameraUI_release" }, k = 1, mv = { 1, 1, 11 })
public final class XperiaXLoopsManager
{
    public static final Companion Companion;
    private static final String LOOPS_SERVICE_CLASS_NAME = "com.sonymobile.xperiaxloops.XperiaXLoopsService";
    private static final String LOOPS_SERVICE_PACKAGE_NAME = "com.sonymobile.xperiaxloops";
    private static final int TYPE_CAMERA = 2;
    private final XperiaXLoopsManager$mCallback.XperiaXLoopsManager$mCallback$1 mCallback;
    private final XperiaXLoopsManager$mConnection.XperiaXLoopsManager$mConnection$1 mConnection;
    private final Context mContext;
    private boolean mIsConnectionRequested;
    private IXperiaXLoopsService mService;
    
    static {
        Companion = new Companion(null);
    }
    
    public XperiaXLoopsManager(@NotNull final Context mContext) {
        Intrinsics.checkParameterIsNotNull(mContext, "mContext");
        this.mContext = mContext;
        this.mCallback = new XperiaXLoopsManager$mCallback.XperiaXLoopsManager$mCallback$1();
        this.mConnection = new XperiaXLoopsManager$mConnection.XperiaXLoopsManager$mConnection$1(this);
    }
    
    private final void notifyShowLoopsByApps(final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("notifyShowLoopsByApps: ");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        try {
            final IXperiaXLoopsService mService = this.mService;
            if (mService != null) {
                mService.notifyShowLoopsByApps(b, XperiaXLoopsManager.TYPE_CAMERA);
            }
        }
        catch (final RemoteException ex) {
            CamLog.e(ex.toString());
        }
    }
    
    private final void registerCallback() {
        if (CamLog.VERBOSE) {
            CamLog.d("registerCallback");
        }
        try {
            final IXperiaXLoopsService mService = this.mService;
            if (mService != null) {
                mService.registerCallback(XperiaXLoopsManager.TYPE_CAMERA, (IXperiaXLoopsServiceCallback)this.mCallback);
            }
        }
        catch (final RemoteException ex) {
            CamLog.e(ex.toString());
        }
    }
    
    private final void unregisterCallback() {
        if (CamLog.VERBOSE) {
            CamLog.d("unregisterCallback");
        }
        try {
            final IXperiaXLoopsService mService = this.mService;
            if (mService != null) {
                mService.unregisterCallback(XperiaXLoopsManager.TYPE_CAMERA, (IXperiaXLoopsServiceCallback)this.mCallback);
            }
        }
        catch (final RemoteException ex) {
            CamLog.e(ex.toString());
        }
    }
    
    @UiThread
    public final void connect() {
        if (CamLog.DEBUG) {
            CamLog.d("connect");
        }
        final Intent intent = new Intent();
        intent.setClassName(XperiaXLoopsManager.LOOPS_SERVICE_PACKAGE_NAME, XperiaXLoopsManager.LOOPS_SERVICE_CLASS_NAME);
        this.mContext.bindService(intent, (ServiceConnection)this.mConnection, 1);
        this.mIsConnectionRequested = true;
    }
    
    @UiThread
    public final void disconnect() {
        if (CamLog.DEBUG) {
            CamLog.d("disconnect");
        }
        if (this.isConnected()) {
            this.notifyShowLoopsByApps(false);
            this.unregisterCallback();
            this.mContext.unbindService((ServiceConnection)this.mConnection);
            this.mService = null;
        }
        this.mIsConnectionRequested = false;
    }
    
    public final boolean isConnected() {
        return this.mService != null;
    }
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082D¢\u0006\u0002\n\u0000¨\u0006\b" }, d2 = { "Lcom/sonyericsson/android/camera/controller/xperiaxloops/XperiaXLoopsManager$Companion;", "", "()V", "LOOPS_SERVICE_CLASS_NAME", "", "LOOPS_SERVICE_PACKAGE_NAME", "TYPE_CAMERA", "", "SemcCameraUI_release" }, k = 1, mv = { 1, 1, 11 })
    public static final class Companion
    {
        private Companion() {
        }
    }
}
