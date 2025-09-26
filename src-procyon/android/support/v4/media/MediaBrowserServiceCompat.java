// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.media;

import android.os.Binder;
import java.util.Collection;
import android.os.Message;
import android.text.TextUtils;
import android.service.media.MediaBrowserService;
import android.os.Parcel;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.app.BundleCompat;
import android.os.Handler;
import android.os.Messenger;
import android.support.annotation.RequiresApi;
import java.util.HashMap;
import android.os.IBinder$DeathRecipient;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.os.ResultReceiver;
import android.os.Build$VERSION;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.content.Context;
import java.util.Collections;
import java.util.Iterator;
import android.support.v4.util.Pair;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.media.session.MediaSessionCompat;
import android.os.IBinder;
import android.support.v4.util.ArrayMap;
import android.support.annotation.RestrictTo;
import android.app.Service;

public abstract class MediaBrowserServiceCompat extends Service
{
    static final boolean DEBUG;
    private static final float EPSILON = 1.0E-5f;
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String KEY_MEDIA_ITEM = "media_item";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String KEY_SEARCH_RESULTS = "search_results";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final int RESULT_ERROR = -1;
    static final int RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED = 2;
    static final int RESULT_FLAG_ON_SEARCH_NOT_IMPLEMENTED = 4;
    static final int RESULT_FLAG_OPTION_NOT_HANDLED = 1;
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final int RESULT_OK = 0;
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final int RESULT_PROGRESS_UPDATE = 1;
    public static final String SERVICE_INTERFACE = "android.media.browse.MediaBrowserService";
    static final String TAG = "MBServiceCompat";
    final ArrayMap<IBinder, ConnectionRecord> mConnections;
    ConnectionRecord mCurConnection;
    final ServiceHandler mHandler;
    private MediaBrowserServiceImpl mImpl;
    MediaSessionCompat.Token mSession;
    
    static {
        DEBUG = Log.isLoggable("MBServiceCompat", 3);
    }
    
    public MediaBrowserServiceCompat() {
        this.mConnections = new ArrayMap<IBinder, ConnectionRecord>();
        this.mHandler = new ServiceHandler();
    }
    
    void addSubscription(final String s, final ConnectionRecord mCurConnection, final IBinder binder, final Bundle bundle) {
        List value;
        if ((value = mCurConnection.subscriptions.get(s)) == null) {
            value = new ArrayList();
        }
        for (final Pair pair : value) {
            if (binder == pair.first && MediaBrowserCompatUtils.areSameOptions(bundle, (Bundle)pair.second)) {
                return;
            }
        }
        value.add(new Pair(binder, bundle));
        mCurConnection.subscriptions.put(s, value);
        this.performLoadChildren(s, mCurConnection, bundle, null);
        this.mCurConnection = mCurConnection;
        this.onSubscribe(s, bundle);
        this.mCurConnection = null;
    }
    
    List<MediaBrowserCompat.MediaItem> applyOptions(final List<MediaBrowserCompat.MediaItem> list, final Bundle bundle) {
        if (list == null) {
            return null;
        }
        final int int1 = bundle.getInt("android.media.browse.extra.PAGE", -1);
        final int int2 = bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        if (int1 == -1 && int2 == -1) {
            return list;
        }
        final int n = int2 * int1;
        final int n2 = n + int2;
        if (int1 >= 0 && int2 >= 1 && n < list.size()) {
            int size;
            if ((size = n2) > list.size()) {
                size = list.size();
            }
            return list.subList(n, size);
        }
        return Collections.emptyList();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public void attachToBaseContext(final Context context) {
        this.attachBaseContext(context);
    }
    
    public void dump(final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
    }
    
    public final Bundle getBrowserRootHints() {
        return this.mImpl.getBrowserRootHints();
    }
    
    @NonNull
    public final MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo() {
        return this.mImpl.getCurrentBrowserInfo();
    }
    
    @Nullable
    public MediaSessionCompat.Token getSessionToken() {
        return this.mSession;
    }
    
    boolean isValidPackage(final String anObject, int i) {
        if (anObject == null) {
            return false;
        }
        final String[] packagesForUid = this.getPackageManager().getPackagesForUid(i);
        int length;
        for (length = packagesForUid.length, i = 0; i < length; ++i) {
            if (packagesForUid[i].equals(anObject)) {
                return true;
            }
        }
        return false;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void notifyChildrenChanged(@NonNull final MediaSessionManager.RemoteUserInfo remoteUserInfo, @NonNull final String s, @NonNull final Bundle bundle) {
        if (remoteUserInfo == null) {
            throw new IllegalArgumentException("remoteUserInfo cannot be null in notifyChildrenChanged");
        }
        if (s == null) {
            throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
        }
        if (bundle == null) {
            throw new IllegalArgumentException("options cannot be null in notifyChildrenChanged");
        }
        this.mImpl.notifyChildrenChanged(remoteUserInfo, s, bundle);
    }
    
    public void notifyChildrenChanged(@NonNull final String s) {
        if (s == null) {
            throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
        }
        this.mImpl.notifyChildrenChanged(s, null);
    }
    
    public void notifyChildrenChanged(@NonNull final String s, @NonNull final Bundle bundle) {
        if (s == null) {
            throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
        }
        if (bundle == null) {
            throw new IllegalArgumentException("options cannot be null in notifyChildrenChanged");
        }
        this.mImpl.notifyChildrenChanged(s, bundle);
    }
    
    public IBinder onBind(final Intent intent) {
        return this.mImpl.onBind(intent);
    }
    
    public void onCreate() {
        super.onCreate();
        if (Build$VERSION.SDK_INT >= 28) {
            this.mImpl = (MediaBrowserServiceImpl)new MediaBrowserServiceImplApi28();
        }
        else if (Build$VERSION.SDK_INT >= 26) {
            this.mImpl = (MediaBrowserServiceImpl)new MediaBrowserServiceImplApi26();
        }
        else if (Build$VERSION.SDK_INT >= 23) {
            this.mImpl = (MediaBrowserServiceImpl)new MediaBrowserServiceImplApi23();
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            this.mImpl = (MediaBrowserServiceImpl)new MediaBrowserServiceImplApi21();
        }
        else {
            this.mImpl = (MediaBrowserServiceImpl)new MediaBrowserServiceImplBase();
        }
        this.mImpl.onCreate();
    }
    
    public void onCustomAction(@NonNull final String s, final Bundle bundle, @NonNull final Result<Bundle> result) {
        result.sendError(null);
    }
    
    @Nullable
    public abstract BrowserRoot onGetRoot(@NonNull final String p0, final int p1, @Nullable final Bundle p2);
    
    public abstract void onLoadChildren(@NonNull final String p0, @NonNull final Result<List<MediaBrowserCompat.MediaItem>> p1);
    
    public void onLoadChildren(@NonNull final String s, @NonNull final Result<List<MediaBrowserCompat.MediaItem>> result, @NonNull final Bundle bundle) {
        result.setFlags(1);
        this.onLoadChildren(s, result);
    }
    
    public void onLoadItem(final String s, @NonNull final Result<MediaBrowserCompat.MediaItem> result) {
        result.setFlags(2);
        result.sendResult(null);
    }
    
    public void onSearch(@NonNull final String s, final Bundle bundle, @NonNull final Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.setFlags(4);
        result.sendResult(null);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void onSubscribe(final String s, final Bundle bundle) {
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void onUnsubscribe(final String s) {
    }
    
    void performCustomAction(final String str, final Bundle obj, final ConnectionRecord mCurConnection, final ResultReceiver resultReceiver) {
        final Result<Bundle> result = new Result<Bundle>(this, str, resultReceiver) {
            final MediaBrowserServiceCompat this$0;
            final ResultReceiver val$receiver;
            
            @Override
            void onErrorSent(final Bundle bundle) {
                this.val$receiver.send(-1, bundle);
            }
            
            @Override
            void onProgressUpdateSent(final Bundle bundle) {
                this.val$receiver.send(1, bundle);
            }
            
            void onResultSent(final Bundle bundle) {
                this.val$receiver.send(0, bundle);
            }
        };
        this.mCurConnection = mCurConnection;
        this.onCustomAction(str, obj, (Result<Bundle>)result);
        this.mCurConnection = null;
        if (!((Result)result).isDone()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onCustomAction must call detach() or sendResult() or sendError() before returning for action=");
            sb.append(str);
            sb.append(" extras=");
            sb.append(obj);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    void performLoadChildren(final String str, final ConnectionRecord mCurConnection, final Bundle bundle, final Bundle bundle2) {
        final Result<List<MediaBrowserCompat.MediaItem>> result = new Result<List<MediaBrowserCompat.MediaItem>>(this, str, mCurConnection, str, bundle, bundle2) {
            final MediaBrowserServiceCompat this$0;
            final ConnectionRecord val$connection;
            final Bundle val$notifyChildrenChangedOptions;
            final String val$parentId;
            final Bundle val$subscribeOptions;
            
            void onResultSent(final List<MediaBrowserCompat.MediaItem> list) {
                if (this.this$0.mConnections.get(this.val$connection.callbacks.asBinder()) != this.val$connection) {
                    if (MediaBrowserServiceCompat.DEBUG) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Not sending onLoadChildren result for connection that has been disconnected. pkg=");
                        sb.append(this.val$connection.pkg);
                        sb.append(" id=");
                        sb.append(this.val$parentId);
                        Log.d("MBServiceCompat", sb.toString());
                    }
                    return;
                }
                List<MediaBrowserCompat.MediaItem> applyOptions = list;
                if ((((Result)this).getFlags() & 0x1) != 0x0) {
                    applyOptions = this.this$0.applyOptions(list, this.val$subscribeOptions);
                }
                try {
                    this.val$connection.callbacks.onLoadChildren(this.val$parentId, applyOptions, this.val$subscribeOptions, this.val$notifyChildrenChangedOptions);
                }
                catch (final RemoteException ex) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Calling onLoadChildren() failed for id=");
                    sb2.append(this.val$parentId);
                    sb2.append(" package=");
                    sb2.append(this.val$connection.pkg);
                    Log.w("MBServiceCompat", sb2.toString());
                }
            }
        };
        this.mCurConnection = mCurConnection;
        if (bundle == null) {
            this.onLoadChildren(str, (Result<List<MediaBrowserCompat.MediaItem>>)result);
        }
        else {
            this.onLoadChildren(str, (Result<List<MediaBrowserCompat.MediaItem>>)result, bundle);
        }
        this.mCurConnection = null;
        if (!((Result)result).isDone()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onLoadChildren must call detach() or sendResult() before returning for package=");
            sb.append(mCurConnection.pkg);
            sb.append(" id=");
            sb.append(str);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    void performLoadItem(final String str, final ConnectionRecord mCurConnection, final ResultReceiver resultReceiver) {
        final Result<MediaBrowserCompat.MediaItem> result = new Result<MediaBrowserCompat.MediaItem>(this, str, resultReceiver) {
            final MediaBrowserServiceCompat this$0;
            final ResultReceiver val$receiver;
            
            void onResultSent(final MediaBrowserCompat.MediaItem mediaItem) {
                if ((((Result)this).getFlags() & 0x2) != 0x0) {
                    this.val$receiver.send(-1, null);
                    return;
                }
                final Bundle bundle = new Bundle();
                bundle.putParcelable("media_item", (Parcelable)mediaItem);
                this.val$receiver.send(0, bundle);
            }
        };
        this.mCurConnection = mCurConnection;
        this.onLoadItem(str, (Result<MediaBrowserCompat.MediaItem>)result);
        this.mCurConnection = null;
        if (!((Result)result).isDone()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onLoadItem must call detach() or sendResult() before returning for id=");
            sb.append(str);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    void performSearch(final String str, final Bundle bundle, final ConnectionRecord mCurConnection, final ResultReceiver resultReceiver) {
        final Result<List<MediaBrowserCompat.MediaItem>> result = new Result<List<MediaBrowserCompat.MediaItem>>(this, str, resultReceiver) {
            final MediaBrowserServiceCompat this$0;
            final ResultReceiver val$receiver;
            
            void onResultSent(final List<MediaBrowserCompat.MediaItem> list) {
                if ((((Result)this).getFlags() & 0x4) == 0x0 && list != null) {
                    final Bundle bundle = new Bundle();
                    bundle.putParcelableArray("search_results", (Parcelable[])list.toArray((Parcelable[])new MediaBrowserCompat.MediaItem[0]));
                    this.val$receiver.send(0, bundle);
                    return;
                }
                this.val$receiver.send(-1, null);
            }
        };
        this.mCurConnection = mCurConnection;
        this.onSearch(str, bundle, (Result<List<MediaBrowserCompat.MediaItem>>)result);
        this.mCurConnection = null;
        if (!((Result)result).isDone()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onSearch must call detach() or sendResult() before returning for query=");
            sb.append(str);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    boolean removeSubscription(final String key, final ConnectionRecord connectionRecord, final IBinder binder) {
        boolean b = false;
        boolean b2 = false;
        while (true) {
            Label_0048: {
                if (binder != null) {
                    break Label_0048;
                }
                Label_0145: {
                    try {
                        if (connectionRecord.subscriptions.remove(key) != null) {
                            b2 = true;
                        }
                        this.mCurConnection = connectionRecord;
                        this.onUnsubscribe(key);
                        this.mCurConnection = null;
                        return b2;
                    }
                    finally {
                        break Label_0145;
                    }
                    break Label_0048;
                }
                this.mCurConnection = connectionRecord;
                this.onUnsubscribe(key);
                this.mCurConnection = null;
            }
            final List list = connectionRecord.subscriptions.get(key);
            if (list == null) {
                continue;
            }
            final Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                if (binder == ((Pair)iterator.next()).first) {
                    iterator.remove();
                    b = true;
                }
            }
            b2 = b;
            if (list.size() == 0) {
                connectionRecord.subscriptions.remove(key);
                b2 = b;
                continue;
            }
            continue;
        }
    }
    
    public void setSessionToken(final MediaSessionCompat.Token token) {
        if (token == null) {
            throw new IllegalArgumentException("Session token may not be null.");
        }
        if (this.mSession != null) {
            throw new IllegalStateException("The session token has already been set.");
        }
        this.mSession = token;
        this.mImpl.setSessionToken(token);
    }
    
    public static final class BrowserRoot
    {
        public static final String EXTRA_OFFLINE = "android.service.media.extra.OFFLINE";
        public static final String EXTRA_RECENT = "android.service.media.extra.RECENT";
        public static final String EXTRA_SUGGESTED = "android.service.media.extra.SUGGESTED";
        @Deprecated
        public static final String EXTRA_SUGGESTION_KEYWORDS = "android.service.media.extra.SUGGESTION_KEYWORDS";
        private final Bundle mExtras;
        private final String mRootId;
        
        public BrowserRoot(@NonNull final String mRootId, @Nullable final Bundle mExtras) {
            if (mRootId == null) {
                throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead.");
            }
            this.mRootId = mRootId;
            this.mExtras = mExtras;
        }
        
        public Bundle getExtras() {
            return this.mExtras;
        }
        
        public String getRootId() {
            return this.mRootId;
        }
    }
    
    private class ConnectionRecord implements IBinder$DeathRecipient
    {
        public final MediaSessionManager.RemoteUserInfo browserInfo;
        public final ServiceCallbacks callbacks;
        public final int pid;
        public final String pkg;
        public BrowserRoot root;
        public final Bundle rootHints;
        public final HashMap<String, List<Pair<IBinder, Bundle>>> subscriptions;
        final MediaBrowserServiceCompat this$0;
        public final int uid;
        
        ConnectionRecord(final MediaBrowserServiceCompat this$0, final String pkg, final int pid, final int uid, final Bundle rootHints, final ServiceCallbacks callbacks) {
            this.this$0 = this$0;
            this.subscriptions = new HashMap<String, List<Pair<IBinder, Bundle>>>();
            this.pkg = pkg;
            this.pid = pid;
            this.uid = uid;
            this.browserInfo = new MediaSessionManager.RemoteUserInfo(pkg, pid, uid);
            this.rootHints = rootHints;
            this.callbacks = callbacks;
        }
        
        public void binderDied() {
            this.this$0.mHandler.post((Runnable)new Runnable(this) {
                final ConnectionRecord this$1;
                
                @Override
                public void run() {
                    this.this$1.this$0.mConnections.remove(this.this$1.callbacks.asBinder());
                }
            });
        }
    }
    
    interface MediaBrowserServiceImpl
    {
        Bundle getBrowserRootHints();
        
        MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo();
        
        void notifyChildrenChanged(final MediaSessionManager.RemoteUserInfo p0, final String p1, final Bundle p2);
        
        void notifyChildrenChanged(final String p0, final Bundle p1);
        
        IBinder onBind(final Intent p0);
        
        void onCreate();
        
        void setSessionToken(final MediaSessionCompat.Token p0);
    }
    
    @RequiresApi(21)
    class MediaBrowserServiceImplApi21 implements MediaBrowserServiceImpl, ServiceCompatProxy
    {
        Messenger mMessenger;
        final List<Bundle> mRootExtrasList;
        Object mServiceObj;
        final MediaBrowserServiceCompat this$0;
        
        MediaBrowserServiceImplApi21(final MediaBrowserServiceCompat this$0) {
            this.this$0 = this$0;
            this.mRootExtrasList = new ArrayList<Bundle>();
        }
        
        @Override
        public Bundle getBrowserRootHints() {
            final Messenger mMessenger = this.mMessenger;
            Bundle bundle = null;
            if (mMessenger == null) {
                return null;
            }
            if (this.this$0.mCurConnection == null) {
                throw new IllegalStateException("This should be called inside of onGetRoot, onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
            }
            if (this.this$0.mCurConnection.rootHints != null) {
                bundle = new Bundle(this.this$0.mCurConnection.rootHints);
            }
            return bundle;
        }
        
        @Override
        public MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo() {
            if (this.this$0.mCurConnection == null) {
                throw new IllegalStateException("This should be called inside of onGetRoot, onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
            }
            return this.this$0.mCurConnection.browserInfo;
        }
        
        @Override
        public void notifyChildrenChanged(final MediaSessionManager.RemoteUserInfo remoteUserInfo, final String s, final Bundle bundle) {
            this.notifyChildrenChangedForCompat(remoteUserInfo, s, bundle);
        }
        
        @Override
        public void notifyChildrenChanged(final String s, final Bundle bundle) {
            this.notifyChildrenChangedForFramework(s, bundle);
            this.notifyChildrenChangedForCompat(s, bundle);
        }
        
        void notifyChildrenChangedForCompat(final MediaSessionManager.RemoteUserInfo remoteUserInfo, final String s, final Bundle bundle) {
            this.this$0.mHandler.post((Runnable)new Runnable(this, remoteUserInfo, s, bundle) {
                final MediaBrowserServiceImplApi21 this$1;
                final Bundle val$options;
                final String val$parentId;
                final MediaSessionManager.RemoteUserInfo val$remoteUserInfo;
                
                @Override
                public void run() {
                    for (int i = 0; i < this.this$1.this$0.mConnections.size(); ++i) {
                        final ConnectionRecord connectionRecord = this.this$1.this$0.mConnections.valueAt(i);
                        if (connectionRecord.browserInfo.equals(this.val$remoteUserInfo)) {
                            this.this$1.notifyChildrenChangedForCompatOnHandler(connectionRecord, this.val$parentId, this.val$options);
                        }
                    }
                }
            });
        }
        
        void notifyChildrenChangedForCompat(final String s, final Bundle bundle) {
            this.this$0.mHandler.post((Runnable)new Runnable(this, s, bundle) {
                final MediaBrowserServiceImplApi21 this$1;
                final Bundle val$options;
                final String val$parentId;
                
                @Override
                public void run() {
                    final Iterator<IBinder> iterator = this.this$1.this$0.mConnections.keySet().iterator();
                    while (iterator.hasNext()) {
                        this.this$1.notifyChildrenChangedForCompatOnHandler((ConnectionRecord)this.this$1.this$0.mConnections.get(iterator.next()), this.val$parentId, this.val$options);
                    }
                }
            });
        }
        
        void notifyChildrenChangedForCompatOnHandler(final ConnectionRecord connectionRecord, final String key, final Bundle bundle) {
            final List list = connectionRecord.subscriptions.get(key);
            if (list != null) {
                for (final Pair pair : list) {
                    if (MediaBrowserCompatUtils.hasDuplicatedItems(bundle, (Bundle)pair.second)) {
                        this.this$0.performLoadChildren(key, connectionRecord, (Bundle)pair.second, bundle);
                    }
                }
            }
        }
        
        void notifyChildrenChangedForFramework(final String s, final Bundle bundle) {
            MediaBrowserServiceCompatApi21.notifyChildrenChanged(this.mServiceObj, s);
        }
        
        @Override
        public IBinder onBind(final Intent intent) {
            return MediaBrowserServiceCompatApi21.onBind(this.mServiceObj, intent);
        }
        
        @Override
        public void onCreate() {
            MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj = MediaBrowserServiceCompatApi21.createService((Context)this.this$0, (MediaBrowserServiceCompatApi21.ServiceCompatProxy)this));
        }
        
        @Override
        public MediaBrowserServiceCompatApi21.BrowserRoot onGetRoot(final String s, final int n, final Bundle bundle) {
            Bundle bundle3;
            if (bundle != null && bundle.getInt("extra_client_version", 0) != 0) {
                bundle.remove("extra_client_version");
                this.mMessenger = new Messenger((Handler)this.this$0.mHandler);
                final Bundle bundle2 = new Bundle();
                bundle2.putInt("extra_service_version", 2);
                BundleCompat.putBinder(bundle2, "extra_messenger", this.mMessenger.getBinder());
                if (this.this$0.mSession != null) {
                    final IMediaSession extraBinder = this.this$0.mSession.getExtraBinder();
                    IBinder binder;
                    if (extraBinder == null) {
                        binder = null;
                    }
                    else {
                        binder = extraBinder.asBinder();
                    }
                    BundleCompat.putBinder(bundle2, "extra_session_binder", binder);
                    bundle3 = bundle2;
                }
                else {
                    this.mRootExtrasList.add(bundle2);
                    bundle3 = bundle2;
                }
            }
            else {
                bundle3 = null;
            }
            this.this$0.mCurConnection = this.this$0.new ConnectionRecord(s, -1, n, bundle, null);
            final MediaBrowserServiceCompat.BrowserRoot onGetRoot = this.this$0.onGetRoot(s, n, bundle);
            this.this$0.mCurConnection = null;
            if (onGetRoot == null) {
                return null;
            }
            Bundle extras;
            if (bundle3 == null) {
                extras = onGetRoot.getExtras();
            }
            else {
                extras = bundle3;
                if (onGetRoot.getExtras() != null) {
                    bundle3.putAll(onGetRoot.getExtras());
                    extras = bundle3;
                }
            }
            return new MediaBrowserServiceCompatApi21.BrowserRoot(onGetRoot.getRootId(), extras);
        }
        
        @Override
        public void onLoadChildren(final String s, final ResultWrapper<List<Parcel>> resultWrapper) {
            this.this$0.onLoadChildren(s, (Result<List<MediaBrowserCompat.MediaItem>>)new Result<List<MediaBrowserCompat.MediaItem>>(this, s, resultWrapper) {
                final MediaBrowserServiceImplApi21 this$1;
                final ResultWrapper val$resultWrapper;
                
                @Override
                public void detach() {
                    this.val$resultWrapper.detach();
                }
                
                void onResultSent(final List<MediaBrowserCompat.MediaItem> list) {
                    ArrayList list3;
                    if (list != null) {
                        final ArrayList list2 = new ArrayList();
                        final Iterator<MediaBrowserCompat.MediaItem> iterator = list.iterator();
                        while (true) {
                            list3 = list2;
                            if (!iterator.hasNext()) {
                                break;
                            }
                            final MediaBrowserCompat.MediaItem mediaItem = iterator.next();
                            final Parcel obtain = Parcel.obtain();
                            mediaItem.writeToParcel(obtain, 0);
                            list2.add(obtain);
                        }
                    }
                    else {
                        list3 = null;
                    }
                    this.val$resultWrapper.sendResult(list3);
                }
            });
        }
        
        @Override
        public void setSessionToken(final MediaSessionCompat.Token token) {
            this.this$0.mHandler.postOrRun(new Runnable(this, token) {
                final MediaBrowserServiceImplApi21 this$1;
                final MediaSessionCompat.Token val$token;
                
                @Override
                public void run() {
                    if (!this.this$1.mRootExtrasList.isEmpty()) {
                        final IMediaSession extraBinder = this.val$token.getExtraBinder();
                        if (extraBinder != null) {
                            final Iterator<Bundle> iterator = this.this$1.mRootExtrasList.iterator();
                            while (iterator.hasNext()) {
                                BundleCompat.putBinder(iterator.next(), "extra_session_binder", extraBinder.asBinder());
                            }
                        }
                        this.this$1.mRootExtrasList.clear();
                    }
                    MediaBrowserServiceCompatApi21.setSessionToken(this.this$1.mServiceObj, this.val$token.getToken());
                }
            });
        }
    }
    
    public static class Result<T>
    {
        private final Object mDebug;
        private boolean mDetachCalled;
        private int mFlags;
        private boolean mSendErrorCalled;
        private boolean mSendProgressUpdateCalled;
        private boolean mSendResultCalled;
        
        Result(final Object mDebug) {
            this.mDebug = mDebug;
        }
        
        private void checkExtraFields(final Bundle bundle) {
            if (bundle == null) {
                return;
            }
            if (bundle.containsKey("android.media.browse.extra.DOWNLOAD_PROGRESS")) {
                final float float1 = bundle.getFloat("android.media.browse.extra.DOWNLOAD_PROGRESS");
                if (float1 < -1.0E-5f || float1 > 1.00001f) {
                    throw new IllegalArgumentException("The value of the EXTRA_DOWNLOAD_PROGRESS field must be a float number within [0.0, 1.0].");
                }
            }
        }
        
        public void detach() {
            if (this.mDetachCalled) {
                final StringBuilder sb = new StringBuilder();
                sb.append("detach() called when detach() had already been called for: ");
                sb.append(this.mDebug);
                throw new IllegalStateException(sb.toString());
            }
            if (this.mSendResultCalled) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("detach() called when sendResult() had already been called for: ");
                sb2.append(this.mDebug);
                throw new IllegalStateException(sb2.toString());
            }
            if (this.mSendErrorCalled) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("detach() called when sendError() had already been called for: ");
                sb3.append(this.mDebug);
                throw new IllegalStateException(sb3.toString());
            }
            this.mDetachCalled = true;
        }
        
        int getFlags() {
            return this.mFlags;
        }
        
        boolean isDone() {
            return this.mDetachCalled || this.mSendResultCalled || this.mSendErrorCalled;
        }
        
        void onErrorSent(final Bundle bundle) {
            final StringBuilder sb = new StringBuilder();
            sb.append("It is not supported to send an error for ");
            sb.append(this.mDebug);
            throw new UnsupportedOperationException(sb.toString());
        }
        
        void onProgressUpdateSent(final Bundle bundle) {
            final StringBuilder sb = new StringBuilder();
            sb.append("It is not supported to send an interim update for ");
            sb.append(this.mDebug);
            throw new UnsupportedOperationException(sb.toString());
        }
        
        void onResultSent(final T t) {
        }
        
        public void sendError(final Bundle bundle) {
            if (!this.mSendResultCalled && !this.mSendErrorCalled) {
                this.mSendErrorCalled = true;
                this.onErrorSent(bundle);
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("sendError() called when either sendResult() or sendError() had already been called for: ");
            sb.append(this.mDebug);
            throw new IllegalStateException(sb.toString());
        }
        
        public void sendProgressUpdate(final Bundle bundle) {
            if (!this.mSendResultCalled && !this.mSendErrorCalled) {
                this.checkExtraFields(bundle);
                this.mSendProgressUpdateCalled = true;
                this.onProgressUpdateSent(bundle);
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("sendProgressUpdate() called when either sendResult() or sendError() had already been called for: ");
            sb.append(this.mDebug);
            throw new IllegalStateException(sb.toString());
        }
        
        public void sendResult(final T t) {
            if (!this.mSendResultCalled && !this.mSendErrorCalled) {
                this.mSendResultCalled = true;
                this.onResultSent(t);
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("sendResult() called when either sendResult() or sendError() had already been called for: ");
            sb.append(this.mDebug);
            throw new IllegalStateException(sb.toString());
        }
        
        void setFlags(final int mFlags) {
            this.mFlags = mFlags;
        }
    }
    
    @RequiresApi(23)
    class MediaBrowserServiceImplApi23 extends MediaBrowserServiceImplApi21 implements MediaBrowserServiceCompatApi23.ServiceCompatProxy
    {
        final MediaBrowserServiceCompat this$0;
        
        MediaBrowserServiceImplApi23(final MediaBrowserServiceCompat this$0) {
            this.this$0 = this$0.super();
        }
        
        @Override
        public void onCreate() {
            MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj = MediaBrowserServiceCompatApi23.createService((Context)this.this$0, (MediaBrowserServiceCompatApi23.ServiceCompatProxy)this));
        }
        
        @Override
        public void onLoadItem(final String s, final ResultWrapper<Parcel> resultWrapper) {
            this.this$0.onLoadItem(s, (Result<MediaBrowserCompat.MediaItem>)new Result<MediaBrowserCompat.MediaItem>(this, s, resultWrapper) {
                final MediaBrowserServiceImplApi23 this$1;
                final ResultWrapper val$resultWrapper;
                
                @Override
                public void detach() {
                    this.val$resultWrapper.detach();
                }
                
                void onResultSent(final MediaBrowserCompat.MediaItem mediaItem) {
                    if (mediaItem == null) {
                        this.val$resultWrapper.sendResult(null);
                    }
                    else {
                        final Parcel obtain = Parcel.obtain();
                        mediaItem.writeToParcel(obtain, 0);
                        this.val$resultWrapper.sendResult(obtain);
                    }
                }
            });
        }
    }
    
    @RequiresApi(26)
    class MediaBrowserServiceImplApi26 extends MediaBrowserServiceImplApi23 implements MediaBrowserServiceCompatApi26.ServiceCompatProxy
    {
        final MediaBrowserServiceCompat this$0;
        
        MediaBrowserServiceImplApi26(final MediaBrowserServiceCompat this$0) {
            this.this$0 = this$0.super();
        }
        
        @Override
        public Bundle getBrowserRootHints() {
            if (this.this$0.mCurConnection != null) {
                Bundle bundle;
                if (this.this$0.mCurConnection.rootHints == null) {
                    bundle = null;
                }
                else {
                    bundle = new Bundle(this.this$0.mCurConnection.rootHints);
                }
                return bundle;
            }
            return MediaBrowserServiceCompatApi26.getBrowserRootHints(this.mServiceObj);
        }
        
        @Override
        void notifyChildrenChangedForFramework(final String s, final Bundle bundle) {
            if (bundle != null) {
                MediaBrowserServiceCompatApi26.notifyChildrenChanged(this.mServiceObj, s, bundle);
            }
            else {
                super.notifyChildrenChangedForFramework(s, bundle);
            }
        }
        
        @Override
        public void onCreate() {
            MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj = MediaBrowserServiceCompatApi26.createService((Context)this.this$0, (MediaBrowserServiceCompatApi26.ServiceCompatProxy)this));
        }
        
        @Override
        public void onLoadChildren(final String s, final MediaBrowserServiceCompatApi26.ResultWrapper resultWrapper, final Bundle bundle) {
            this.this$0.onLoadChildren(s, (Result<List<MediaBrowserCompat.MediaItem>>)new Result<List<MediaBrowserCompat.MediaItem>>(this, s, resultWrapper) {
                final MediaBrowserServiceImplApi26 this$1;
                final MediaBrowserServiceCompatApi26.ResultWrapper val$resultWrapper;
                
                @Override
                public void detach() {
                    this.val$resultWrapper.detach();
                }
                
                void onResultSent(final List<MediaBrowserCompat.MediaItem> list) {
                    ArrayList list3;
                    if (list != null) {
                        final ArrayList list2 = new ArrayList();
                        final Iterator<MediaBrowserCompat.MediaItem> iterator = list.iterator();
                        while (true) {
                            list3 = list2;
                            if (!iterator.hasNext()) {
                                break;
                            }
                            final MediaBrowserCompat.MediaItem mediaItem = iterator.next();
                            final Parcel obtain = Parcel.obtain();
                            mediaItem.writeToParcel(obtain, 0);
                            list2.add(obtain);
                        }
                    }
                    else {
                        list3 = null;
                    }
                    this.val$resultWrapper.sendResult(list3, ((Result)this).getFlags());
                }
            }, bundle);
        }
    }
    
    @RequiresApi(28)
    class MediaBrowserServiceImplApi28 extends MediaBrowserServiceImplApi26
    {
        final MediaBrowserServiceCompat this$0;
        
        MediaBrowserServiceImplApi28(final MediaBrowserServiceCompat this$0) {
            this.this$0 = this$0.super();
        }
        
        @Override
        public MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo() {
            if (this.this$0.mCurConnection != null) {
                return this.this$0.mCurConnection.browserInfo;
            }
            return new MediaSessionManager.RemoteUserInfo(((MediaBrowserService)this.mServiceObj).getCurrentBrowserInfo());
        }
    }
    
    class MediaBrowserServiceImplBase implements MediaBrowserServiceImpl
    {
        private Messenger mMessenger;
        final MediaBrowserServiceCompat this$0;
        
        MediaBrowserServiceImplBase(final MediaBrowserServiceCompat this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public Bundle getBrowserRootHints() {
            if (this.this$0.mCurConnection == null) {
                throw new IllegalStateException("This should be called inside of onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
            }
            Bundle bundle;
            if (this.this$0.mCurConnection.rootHints == null) {
                bundle = null;
            }
            else {
                bundle = new Bundle(this.this$0.mCurConnection.rootHints);
            }
            return bundle;
        }
        
        @Override
        public MediaSessionManager.RemoteUserInfo getCurrentBrowserInfo() {
            if (this.this$0.mCurConnection == null) {
                throw new IllegalStateException("This should be called inside of onLoadChildren, onLoadItem, onSearch, or onCustomAction methods");
            }
            return this.this$0.mCurConnection.browserInfo;
        }
        
        @Override
        public void notifyChildrenChanged(@NonNull final MediaSessionManager.RemoteUserInfo remoteUserInfo, @NonNull final String s, final Bundle bundle) {
            this.this$0.mHandler.post((Runnable)new Runnable(this, remoteUserInfo, s, bundle) {
                final MediaBrowserServiceImplBase this$1;
                final Bundle val$options;
                final String val$parentId;
                final MediaSessionManager.RemoteUserInfo val$remoteUserInfo;
                
                @Override
                public void run() {
                    for (int i = 0; i < this.this$1.this$0.mConnections.size(); ++i) {
                        final ConnectionRecord connectionRecord = this.this$1.this$0.mConnections.valueAt(i);
                        if (connectionRecord.browserInfo.equals(this.val$remoteUserInfo)) {
                            this.this$1.notifyChildrenChangedOnHandler(connectionRecord, this.val$parentId, this.val$options);
                            break;
                        }
                    }
                }
            });
        }
        
        @Override
        public void notifyChildrenChanged(@NonNull final String s, final Bundle bundle) {
            this.this$0.mHandler.post((Runnable)new Runnable(this, s, bundle) {
                final MediaBrowserServiceImplBase this$1;
                final Bundle val$options;
                final String val$parentId;
                
                @Override
                public void run() {
                    final Iterator<IBinder> iterator = this.this$1.this$0.mConnections.keySet().iterator();
                    while (iterator.hasNext()) {
                        this.this$1.notifyChildrenChangedOnHandler((ConnectionRecord)this.this$1.this$0.mConnections.get(iterator.next()), this.val$parentId, this.val$options);
                    }
                }
            });
        }
        
        void notifyChildrenChangedOnHandler(final ConnectionRecord connectionRecord, final String key, final Bundle bundle) {
            final List list = connectionRecord.subscriptions.get(key);
            if (list != null) {
                for (final Pair pair : list) {
                    if (MediaBrowserCompatUtils.hasDuplicatedItems(bundle, (Bundle)pair.second)) {
                        this.this$0.performLoadChildren(key, connectionRecord, (Bundle)pair.second, bundle);
                    }
                }
            }
        }
        
        @Override
        public IBinder onBind(final Intent intent) {
            if ("android.media.browse.MediaBrowserService".equals(intent.getAction())) {
                return this.mMessenger.getBinder();
            }
            return null;
        }
        
        @Override
        public void onCreate() {
            this.mMessenger = new Messenger((Handler)this.this$0.mHandler);
        }
        
        @Override
        public void setSessionToken(final MediaSessionCompat.Token token) {
            this.this$0.mHandler.post((Runnable)new Runnable(this, token) {
                final MediaBrowserServiceImplBase this$1;
                final MediaSessionCompat.Token val$token;
                
                @Override
                public void run() {
                    final Iterator<ConnectionRecord> iterator = this.this$1.this$0.mConnections.values().iterator();
                    while (iterator.hasNext()) {
                        final ConnectionRecord connectionRecord = iterator.next();
                        try {
                            connectionRecord.callbacks.onConnect(connectionRecord.root.getRootId(), this.val$token, connectionRecord.root.getExtras());
                        }
                        catch (final RemoteException ex) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Connection for ");
                            sb.append(connectionRecord.pkg);
                            sb.append(" is no longer valid.");
                            Log.w("MBServiceCompat", sb.toString());
                            iterator.remove();
                        }
                    }
                }
            });
        }
    }
    
    private class ServiceBinderImpl
    {
        final MediaBrowserServiceCompat this$0;
        
        ServiceBinderImpl(final MediaBrowserServiceCompat this$0) {
            this.this$0 = this$0;
        }
        
        public void addSubscription(final String s, final IBinder binder, final Bundle bundle, final ServiceCallbacks serviceCallbacks) {
            this.this$0.mHandler.postOrRun(new Runnable(this, serviceCallbacks, s, binder, bundle) {
                final ServiceBinderImpl this$1;
                final ServiceCallbacks val$callbacks;
                final String val$id;
                final Bundle val$options;
                final IBinder val$token;
                
                @Override
                public void run() {
                    final ConnectionRecord connectionRecord = this.this$1.this$0.mConnections.get(this.val$callbacks.asBinder());
                    if (connectionRecord == null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("addSubscription for callback that isn't registered id=");
                        sb.append(this.val$id);
                        Log.w("MBServiceCompat", sb.toString());
                        return;
                    }
                    this.this$1.this$0.addSubscription(this.val$id, connectionRecord, this.val$token, this.val$options);
                }
            });
        }
        
        public void connect(final String str, final int n, final int i, final Bundle bundle, final ServiceCallbacks serviceCallbacks) {
            if (!this.this$0.isValidPackage(str, i)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Package/uid mismatch: uid=");
                sb.append(i);
                sb.append(" package=");
                sb.append(str);
                throw new IllegalArgumentException(sb.toString());
            }
            this.this$0.mHandler.postOrRun(new Runnable(this, serviceCallbacks, str, n, i, bundle) {
                final ServiceBinderImpl this$1;
                final ServiceCallbacks val$callbacks;
                final int val$pid;
                final String val$pkg;
                final Bundle val$rootHints;
                final int val$uid;
                
                @Override
                public void run() {
                    final IBinder binder = this.val$callbacks.asBinder();
                    this.this$1.this$0.mConnections.remove(binder);
                    final ConnectionRecord mCurConnection = this.this$1.this$0.new ConnectionRecord(this.val$pkg, this.val$pid, this.val$uid, this.val$rootHints, this.val$callbacks);
                    this.this$1.this$0.mCurConnection = mCurConnection;
                    mCurConnection.root = this.this$1.this$0.onGetRoot(this.val$pkg, this.val$uid, this.val$rootHints);
                    this.this$1.this$0.mCurConnection = null;
                    if (mCurConnection.root == null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("No root for client ");
                        sb.append(this.val$pkg);
                        sb.append(" from service ");
                        sb.append(this.getClass().getName());
                        Log.i("MBServiceCompat", sb.toString());
                        try {
                            this.val$callbacks.onConnectFailed();
                        }
                        catch (final RemoteException ex) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Calling onConnectFailed() failed. Ignoring. pkg=");
                            sb2.append(this.val$pkg);
                            Log.w("MBServiceCompat", sb2.toString());
                        }
                    }
                    else {
                        try {
                            this.this$1.this$0.mConnections.put(binder, mCurConnection);
                            binder.linkToDeath((IBinder$DeathRecipient)mCurConnection, 0);
                            if (this.this$1.this$0.mSession != null) {
                                this.val$callbacks.onConnect(mCurConnection.root.getRootId(), this.this$1.this$0.mSession, mCurConnection.root.getExtras());
                            }
                        }
                        catch (final RemoteException ex2) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("Calling onConnect() failed. Dropping client. pkg=");
                            sb3.append(this.val$pkg);
                            Log.w("MBServiceCompat", sb3.toString());
                            this.this$1.this$0.mConnections.remove(binder);
                        }
                    }
                }
            });
        }
        
        public void disconnect(final ServiceCallbacks serviceCallbacks) {
            this.this$0.mHandler.postOrRun(new Runnable(this, serviceCallbacks) {
                final ServiceBinderImpl this$1;
                final ServiceCallbacks val$callbacks;
                
                @Override
                public void run() {
                    final ConnectionRecord connectionRecord = this.this$1.this$0.mConnections.remove(this.val$callbacks.asBinder());
                    if (connectionRecord != null) {
                        connectionRecord.callbacks.asBinder().unlinkToDeath((IBinder$DeathRecipient)connectionRecord, 0);
                    }
                }
            });
        }
        
        public void getMediaItem(final String s, final ResultReceiver resultReceiver, final ServiceCallbacks serviceCallbacks) {
            if (!TextUtils.isEmpty((CharSequence)s) && resultReceiver != null) {
                this.this$0.mHandler.postOrRun(new Runnable(this, serviceCallbacks, s, resultReceiver) {
                    final ServiceBinderImpl this$1;
                    final ServiceCallbacks val$callbacks;
                    final String val$mediaId;
                    final ResultReceiver val$receiver;
                    
                    @Override
                    public void run() {
                        final ConnectionRecord connectionRecord = this.this$1.this$0.mConnections.get(this.val$callbacks.asBinder());
                        if (connectionRecord == null) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("getMediaItem for callback that isn't registered id=");
                            sb.append(this.val$mediaId);
                            Log.w("MBServiceCompat", sb.toString());
                            return;
                        }
                        this.this$1.this$0.performLoadItem(this.val$mediaId, connectionRecord, this.val$receiver);
                    }
                });
            }
        }
        
        public void registerCallbacks(final ServiceCallbacks serviceCallbacks, final String s, final int n, final int n2, final Bundle bundle) {
            this.this$0.mHandler.postOrRun(new Runnable(this, serviceCallbacks, s, n, n2, bundle) {
                final ServiceBinderImpl this$1;
                final ServiceCallbacks val$callbacks;
                final int val$pid;
                final String val$pkg;
                final Bundle val$rootHints;
                final int val$uid;
                
                @Override
                public void run() {
                    final IBinder binder = this.val$callbacks.asBinder();
                    this.this$1.this$0.mConnections.remove(binder);
                    final ConnectionRecord connectionRecord = this.this$1.this$0.new ConnectionRecord(this.val$pkg, this.val$pid, this.val$uid, this.val$rootHints, this.val$callbacks);
                    this.this$1.this$0.mConnections.put(binder, connectionRecord);
                    try {
                        binder.linkToDeath((IBinder$DeathRecipient)connectionRecord, 0);
                    }
                    catch (final RemoteException ex) {
                        Log.w("MBServiceCompat", "IBinder is already dead.");
                    }
                }
            });
        }
        
        public void removeSubscription(final String s, final IBinder binder, final ServiceCallbacks serviceCallbacks) {
            this.this$0.mHandler.postOrRun(new Runnable(this, serviceCallbacks, s, binder) {
                final ServiceBinderImpl this$1;
                final ServiceCallbacks val$callbacks;
                final String val$id;
                final IBinder val$token;
                
                @Override
                public void run() {
                    final ConnectionRecord connectionRecord = this.this$1.this$0.mConnections.get(this.val$callbacks.asBinder());
                    if (connectionRecord == null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("removeSubscription for callback that isn't registered id=");
                        sb.append(this.val$id);
                        Log.w("MBServiceCompat", sb.toString());
                        return;
                    }
                    if (!this.this$1.this$0.removeSubscription(this.val$id, connectionRecord, this.val$token)) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("removeSubscription called for ");
                        sb2.append(this.val$id);
                        sb2.append(" which is not subscribed");
                        Log.w("MBServiceCompat", sb2.toString());
                    }
                }
            });
        }
        
        public void search(final String s, final Bundle bundle, final ResultReceiver resultReceiver, final ServiceCallbacks serviceCallbacks) {
            if (!TextUtils.isEmpty((CharSequence)s) && resultReceiver != null) {
                this.this$0.mHandler.postOrRun(new Runnable(this, serviceCallbacks, s, bundle, resultReceiver) {
                    final ServiceBinderImpl this$1;
                    final ServiceCallbacks val$callbacks;
                    final Bundle val$extras;
                    final String val$query;
                    final ResultReceiver val$receiver;
                    
                    @Override
                    public void run() {
                        final ConnectionRecord connectionRecord = this.this$1.this$0.mConnections.get(this.val$callbacks.asBinder());
                        if (connectionRecord == null) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("search for callback that isn't registered query=");
                            sb.append(this.val$query);
                            Log.w("MBServiceCompat", sb.toString());
                            return;
                        }
                        this.this$1.this$0.performSearch(this.val$query, this.val$extras, connectionRecord, this.val$receiver);
                    }
                });
            }
        }
        
        public void sendCustomAction(final String s, final Bundle bundle, final ResultReceiver resultReceiver, final ServiceCallbacks serviceCallbacks) {
            if (!TextUtils.isEmpty((CharSequence)s) && resultReceiver != null) {
                this.this$0.mHandler.postOrRun(new Runnable(this, serviceCallbacks, s, bundle, resultReceiver) {
                    final ServiceBinderImpl this$1;
                    final String val$action;
                    final ServiceCallbacks val$callbacks;
                    final Bundle val$extras;
                    final ResultReceiver val$receiver;
                    
                    @Override
                    public void run() {
                        final ConnectionRecord connectionRecord = this.this$1.this$0.mConnections.get(this.val$callbacks.asBinder());
                        if (connectionRecord == null) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("sendCustomAction for callback that isn't registered action=");
                            sb.append(this.val$action);
                            sb.append(", extras=");
                            sb.append(this.val$extras);
                            Log.w("MBServiceCompat", sb.toString());
                            return;
                        }
                        this.this$1.this$0.performCustomAction(this.val$action, this.val$extras, connectionRecord, this.val$receiver);
                    }
                });
            }
        }
        
        public void unregisterCallbacks(final ServiceCallbacks serviceCallbacks) {
            this.this$0.mHandler.postOrRun(new Runnable(this, serviceCallbacks) {
                final ServiceBinderImpl this$1;
                final ServiceCallbacks val$callbacks;
                
                @Override
                public void run() {
                    final IBinder binder = this.val$callbacks.asBinder();
                    final ConnectionRecord connectionRecord = this.this$1.this$0.mConnections.remove(binder);
                    if (connectionRecord != null) {
                        binder.unlinkToDeath((IBinder$DeathRecipient)connectionRecord, 0);
                    }
                }
            });
        }
    }
    
    private interface ServiceCallbacks
    {
        IBinder asBinder();
        
        void onConnect(final String p0, final MediaSessionCompat.Token p1, final Bundle p2) throws RemoteException;
        
        void onConnectFailed() throws RemoteException;
        
        void onLoadChildren(final String p0, final List<MediaBrowserCompat.MediaItem> p1, final Bundle p2, final Bundle p3) throws RemoteException;
    }
    
    private static class ServiceCallbacksCompat implements ServiceCallbacks
    {
        final Messenger mCallbacks;
        
        ServiceCallbacksCompat(final Messenger mCallbacks) {
            this.mCallbacks = mCallbacks;
        }
        
        private void sendRequest(final int what, final Bundle data) throws RemoteException {
            final Message obtain = Message.obtain();
            obtain.what = what;
            obtain.arg1 = 2;
            obtain.setData(data);
            this.mCallbacks.send(obtain);
        }
        
        @Override
        public IBinder asBinder() {
            return this.mCallbacks.getBinder();
        }
        
        @Override
        public void onConnect(final String s, final MediaSessionCompat.Token token, final Bundle bundle) throws RemoteException {
            Bundle bundle2 = bundle;
            if (bundle == null) {
                bundle2 = new Bundle();
            }
            bundle2.putInt("extra_service_version", 2);
            final Bundle bundle3 = new Bundle();
            bundle3.putString("data_media_item_id", s);
            bundle3.putParcelable("data_media_session_token", (Parcelable)token);
            bundle3.putBundle("data_root_hints", bundle2);
            this.sendRequest(1, bundle3);
        }
        
        @Override
        public void onConnectFailed() throws RemoteException {
            this.sendRequest(2, null);
        }
        
        @Override
        public void onLoadChildren(final String s, final List<MediaBrowserCompat.MediaItem> c, final Bundle bundle, final Bundle bundle2) throws RemoteException {
            final Bundle bundle3 = new Bundle();
            bundle3.putString("data_media_item_id", s);
            bundle3.putBundle("data_options", bundle);
            bundle3.putBundle("data_notify_children_changed_options", bundle2);
            if (c != null) {
                ArrayList list;
                if (c instanceof ArrayList) {
                    list = (ArrayList)c;
                }
                else {
                    list = new ArrayList((Collection<? extends E>)c);
                }
                bundle3.putParcelableArrayList("data_media_item_list", list);
            }
            this.sendRequest(3, bundle3);
        }
    }
    
    private final class ServiceHandler extends Handler
    {
        private final ServiceBinderImpl mServiceBinderImpl;
        final MediaBrowserServiceCompat this$0;
        
        ServiceHandler(final MediaBrowserServiceCompat this$0) {
            this.this$0 = this$0;
            this.mServiceBinderImpl = this.this$0.new ServiceBinderImpl();
        }
        
        public void handleMessage(final Message obj) {
            final Bundle data = obj.getData();
            switch (obj.what) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unhandled message: ");
                    sb.append(obj);
                    sb.append("\n  Service version: ");
                    sb.append(2);
                    sb.append("\n  Client version: ");
                    sb.append(obj.arg1);
                    Log.w("MBServiceCompat", sb.toString());
                    break;
                }
                case 9: {
                    final Bundle bundle = data.getBundle("data_custom_action_extras");
                    MediaSessionCompat.ensureClassLoader(bundle);
                    this.mServiceBinderImpl.sendCustomAction(data.getString("data_custom_action"), bundle, (ResultReceiver)data.getParcelable("data_result_receiver"), new ServiceCallbacksCompat(obj.replyTo));
                    break;
                }
                case 8: {
                    final Bundle bundle2 = data.getBundle("data_search_extras");
                    MediaSessionCompat.ensureClassLoader(bundle2);
                    this.mServiceBinderImpl.search(data.getString("data_search_query"), bundle2, (ResultReceiver)data.getParcelable("data_result_receiver"), new ServiceCallbacksCompat(obj.replyTo));
                    break;
                }
                case 7: {
                    this.mServiceBinderImpl.unregisterCallbacks(new ServiceCallbacksCompat(obj.replyTo));
                    break;
                }
                case 6: {
                    final Bundle bundle3 = data.getBundle("data_root_hints");
                    MediaSessionCompat.ensureClassLoader(bundle3);
                    this.mServiceBinderImpl.registerCallbacks(new ServiceCallbacksCompat(obj.replyTo), data.getString("data_package_name"), data.getInt("data_calling_pid"), data.getInt("data_calling_uid"), bundle3);
                    break;
                }
                case 5: {
                    this.mServiceBinderImpl.getMediaItem(data.getString("data_media_item_id"), (ResultReceiver)data.getParcelable("data_result_receiver"), new ServiceCallbacksCompat(obj.replyTo));
                    break;
                }
                case 4: {
                    this.mServiceBinderImpl.removeSubscription(data.getString("data_media_item_id"), BundleCompat.getBinder(data, "data_callback_token"), new ServiceCallbacksCompat(obj.replyTo));
                    break;
                }
                case 3: {
                    final Bundle bundle4 = data.getBundle("data_options");
                    MediaSessionCompat.ensureClassLoader(bundle4);
                    this.mServiceBinderImpl.addSubscription(data.getString("data_media_item_id"), BundleCompat.getBinder(data, "data_callback_token"), bundle4, new ServiceCallbacksCompat(obj.replyTo));
                    break;
                }
                case 2: {
                    this.mServiceBinderImpl.disconnect(new ServiceCallbacksCompat(obj.replyTo));
                    break;
                }
                case 1: {
                    final Bundle bundle5 = data.getBundle("data_root_hints");
                    MediaSessionCompat.ensureClassLoader(bundle5);
                    this.mServiceBinderImpl.connect(data.getString("data_package_name"), data.getInt("data_calling_pid"), data.getInt("data_calling_uid"), bundle5, new ServiceCallbacksCompat(obj.replyTo));
                    break;
                }
            }
        }
        
        public void postOrRun(final Runnable runnable) {
            if (Thread.currentThread() == this.getLooper().getThread()) {
                runnable.run();
            }
            else {
                this.post(runnable);
            }
        }
        
        public boolean sendMessageAtTime(final Message message, final long n) {
            final Bundle data = message.getData();
            data.setClassLoader(MediaBrowserCompat.class.getClassLoader());
            data.putInt("data_calling_uid", Binder.getCallingUid());
            data.putInt("data_calling_pid", Binder.getCallingPid());
            return super.sendMessageAtTime(message, n);
        }
    }
}
