// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.apps.lens.library.base;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.android.apps.gsa.search.shared.service.proto.nano.LensServiceEventData;
import com.google.protobuf.nano.Extension;
import com.google.android.apps.gsa.search.shared.service.proto.nano.LensServiceEvent;
import com.google.android.apps.gsa.search.shared.service.proto.nano.ServiceEventProto;
import com.google.android.apps.gsa.publicsearch.SystemParcelableWrapper;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.protobuf.nano.MessageNano;
import com.google.android.apps.gsa.search.shared.service.proto.nano.LensServiceClientEventData;
import com.google.android.apps.gsa.search.shared.service.proto.nano.LensServiceClientEvent;
import com.google.android.apps.gsa.search.shared.service.proto.nano.ClientEventProto;
import com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSession;
import android.support.annotation.Nullable;
import com.google.android.apps.gsa.publicsearch.IPublicSearchService;
import android.support.annotation.NonNull;
import android.content.Context;
import android.content.ServiceConnection;
import com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSessionCallback;

public class LensServiceBridge extends Stub implements ServiceConnection
{
    private static final String BIND_INTENT_ACTION = "com.google.android.apps.gsa.publicsearch.IPublicSearchService";
    private static final boolean DEBUG = false;
    private static final String LENS_CLIENT_SESSION_TYPE = "LENS_SERVICE_SESSION";
    private static final String TAG = "LensServiceBridge";
    private static final int TARGET_SERVICE_API_VERSION = 1;
    @NonNull
    private final Context context;
    @Nullable
    private IPublicSearchService lensService;
    @Nullable
    private volatile IPublicSearchServiceSession lensServiceSession;
    private int serviceApiVersion;
    
    public LensServiceBridge(@NonNull final Context context) {
        this.context = context;
    }
    
    private void beginLensSession() {
        if (this.lensService == null) {
            return;
        }
        final ClientEventProto setEventId = new ClientEventProto().setEventId(348);
        setEventId.setExtension(LensServiceClientEvent.lensServiceClientEventData, new LensServiceClientEventData().setTargetServiceApiVersion(1));
        try {
            this.lensServiceSession = this.lensService.beginSession("LENS_SERVICE_SESSION", this, MessageNano.toByteArray(setEventId));
        }
        catch (final RemoteException | SecurityException ex) {
            Log.e("LensServiceBridge", "Unable to begin Lens service session.", (Throwable)ex);
        }
    }
    
    private void endLensSession() {
        if (this.lensService != null && this.lensServiceSession != null) {
            final ClientEventProto setEventId = new ClientEventProto().setEventId(345);
            try {
                this.lensServiceSession.onGenericClientEvent(MessageNano.toByteArray(setEventId));
            }
            catch (final RemoteException | SecurityException ex) {
                Log.e("LensServiceBridge", "Unable to end Lens service session.", (Throwable)ex);
            }
        }
    }
    
    private void ensureOnMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("This should be running on the main thread.");
        }
    }
    
    private boolean isLensSessionReady() {
        final int serviceApiVersion = this.serviceApiVersion;
        return this.lensService != null && this.lensServiceSession != null && serviceApiVersion > 0;
    }
    
    public boolean bindService() {
        this.ensureOnMainThread();
        final Intent intent = new Intent("com.google.android.apps.gsa.publicsearch.IPublicSearchService");
        intent.setPackage("com.google.android.googlequicksearchbox");
        try {
            if (!this.context.bindService(intent, (ServiceConnection)this, 65)) {
                Log.e("LensServiceBridge", "Unable to bind Lens service.");
                return false;
            }
            return true;
        }
        catch (final SecurityException ex) {
            Log.i("LensServiceBridge", "Unable to bind Lens service due to security exception. Maybe the service is not available yet.");
            return false;
        }
    }
    
    public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
        this.ensureOnMainThread();
        Log.i("LensServiceBridge", "Lens service connected.");
        this.lensService = IPublicSearchService.Stub.asInterface(binder);
        this.beginLensSession();
    }
    
    public void onServiceDisconnected(final ComponentName componentName) {
        this.ensureOnMainThread();
        Log.w("LensServiceBridge", "Lens service disconnected.");
    }
    
    public void onServiceEvent(final byte[] array, final SystemParcelableWrapper systemParcelableWrapper) {
        try {
            final ServiceEventProto from = ServiceEventProto.parseFrom(array);
            if (from.getEventId() == 240 && from.hasExtension(LensServiceEvent.lensServiceEventData)) {
                this.serviceApiVersion = from.getExtension(LensServiceEvent.lensServiceEventData).getServiceApiVersion();
            }
        }
        catch (final InvalidProtocolBufferNanoException ex) {
            Log.e("LensServiceBridge", "Unable to parse the protobuf.", (Throwable)ex);
        }
    }
    
    public boolean prewarmLensActivity() {
        this.ensureOnMainThread();
        if (!this.isLensSessionReady()) {
            Log.i("LensServiceBridge", "Lens session is not ready for prewarm.");
            return false;
        }
        final ClientEventProto setEventId = new ClientEventProto().setEventId(347);
        try {
            this.lensServiceSession.onGenericClientEvent(MessageNano.toByteArray(setEventId));
            return true;
        }
        catch (final RemoteException | SecurityException ex) {
            Log.e("LensServiceBridge", "Unable to send prewarm signal.", (Throwable)ex);
            return false;
        }
    }
    
    public void unbindService() {
        this.ensureOnMainThread();
        this.endLensSession();
        this.context.unbindService((ServiceConnection)this);
        this.lensService = null;
        this.lensServiceSession = null;
        this.serviceApiVersion = 0;
    }
}
