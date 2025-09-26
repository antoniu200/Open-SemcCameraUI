// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.media.session;

import android.media.MediaMetadataEditor;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.media.session.MediaSession;
import android.media.Rating;
import android.media.RemoteControlClient$OnMetadataUpdateListener;
import android.os.Parcelable;
import android.os.IInterface;
import android.os.Binder;
import android.graphics.Bitmap;
import android.media.RemoteControlClient$MetadataEditor;
import android.os.RemoteException;
import android.media.RemoteControlClient;
import android.os.RemoteCallbackList;
import android.media.AudioManager;
import android.media.RemoteControlClient$OnPlaybackPositionUpdateListener;
import android.os.IBinder;
import android.os.BadParcelableException;
import android.support.v4.app.BundleCompat;
import android.support.annotation.RequiresApi;
import android.os.Message;
import android.os.Looper;
import android.support.v4.media.RatingCompat;
import android.net.Uri;
import android.view.ViewConfiguration;
import android.view.KeyEvent;
import android.os.ResultReceiver;
import android.support.v4.media.MediaDescriptionCompat;
import java.lang.ref.WeakReference;
import java.util.List;
import android.support.v4.media.VolumeProviderCompat;
import android.os.Handler;
import java.util.Iterator;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaSessionManager;
import android.os.SystemClock;
import android.support.v4.media.MediaMetadataCompat;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.content.Intent;
import android.util.Log;
import android.text.TextUtils;
import android.os.Bundle;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.os.Build$VERSION;
import android.content.Context;
import java.util.ArrayList;
import android.support.annotation.RestrictTo;

public class MediaSessionCompat
{
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_ARGUMENT_CAPTIONING_ENABLED = "android.support.v4.media.session.action.ARGUMENT_CAPTIONING_ENABLED";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_ARGUMENT_EXTRAS = "android.support.v4.media.session.action.ARGUMENT_EXTRAS";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_ARGUMENT_MEDIA_ID = "android.support.v4.media.session.action.ARGUMENT_MEDIA_ID";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_ARGUMENT_QUERY = "android.support.v4.media.session.action.ARGUMENT_QUERY";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_ARGUMENT_RATING = "android.support.v4.media.session.action.ARGUMENT_RATING";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_ARGUMENT_REPEAT_MODE = "android.support.v4.media.session.action.ARGUMENT_REPEAT_MODE";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_ARGUMENT_SHUFFLE_MODE = "android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_ARGUMENT_URI = "android.support.v4.media.session.action.ARGUMENT_URI";
    public static final String ACTION_FLAG_AS_INAPPROPRIATE = "android.support.v4.media.session.action.FLAG_AS_INAPPROPRIATE";
    public static final String ACTION_FOLLOW = "android.support.v4.media.session.action.FOLLOW";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_PLAY_FROM_URI = "android.support.v4.media.session.action.PLAY_FROM_URI";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_PREPARE = "android.support.v4.media.session.action.PREPARE";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_PREPARE_FROM_MEDIA_ID = "android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_PREPARE_FROM_SEARCH = "android.support.v4.media.session.action.PREPARE_FROM_SEARCH";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_PREPARE_FROM_URI = "android.support.v4.media.session.action.PREPARE_FROM_URI";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_SET_CAPTIONING_ENABLED = "android.support.v4.media.session.action.SET_CAPTIONING_ENABLED";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_SET_RATING = "android.support.v4.media.session.action.SET_RATING";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_SET_REPEAT_MODE = "android.support.v4.media.session.action.SET_REPEAT_MODE";
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String ACTION_SET_SHUFFLE_MODE = "android.support.v4.media.session.action.SET_SHUFFLE_MODE";
    public static final String ACTION_SKIP_AD = "android.support.v4.media.session.action.SKIP_AD";
    public static final String ACTION_UNFOLLOW = "android.support.v4.media.session.action.UNFOLLOW";
    public static final String ARGUMENT_MEDIA_ATTRIBUTE = "android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE";
    public static final String ARGUMENT_MEDIA_ATTRIBUTE_VALUE = "android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE_VALUE";
    private static final String DATA_CALLING_PACKAGE = "data_calling_pkg";
    private static final String DATA_CALLING_PID = "data_calling_pid";
    private static final String DATA_CALLING_UID = "data_calling_uid";
    private static final String DATA_EXTRAS = "data_extras";
    public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
    public static final int FLAG_HANDLES_QUEUE_COMMANDS = 4;
    public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final String KEY_EXTRA_BINDER = "android.support.v4.media.session.EXTRA_BINDER";
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final String KEY_SESSION_TOKEN2_BUNDLE = "android.support.v4.media.session.SESSION_TOKEN2_BUNDLE";
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final String KEY_TOKEN = "android.support.v4.media.session.TOKEN";
    private static final int MAX_BITMAP_SIZE_IN_DP = 320;
    public static final int MEDIA_ATTRIBUTE_ALBUM = 1;
    public static final int MEDIA_ATTRIBUTE_ARTIST = 0;
    public static final int MEDIA_ATTRIBUTE_PLAYLIST = 2;
    static final String TAG = "MediaSessionCompat";
    static int sMaxBitmapSize;
    private final ArrayList<OnActiveChangeListener> mActiveListeners;
    private final MediaControllerCompat mController;
    private final MediaSessionImpl mImpl;
    
    private MediaSessionCompat(final Context context, final MediaSessionImpl mImpl) {
        this.mActiveListeners = new ArrayList<OnActiveChangeListener>();
        this.mImpl = mImpl;
        if (Build$VERSION.SDK_INT >= 21 && !MediaSessionCompatApi21.hasCallback(mImpl.getMediaSession())) {
            this.setCallback((Callback)new Callback(this) {
                final MediaSessionCompat this$0;
            });
        }
        this.mController = new MediaControllerCompat(context, this);
    }
    
    public MediaSessionCompat(final Context context, final String s) {
        this(context, s, null, null);
    }
    
    public MediaSessionCompat(final Context context, final String s, final ComponentName componentName, final PendingIntent pendingIntent) {
        this(context, s, componentName, pendingIntent, null);
    }
    
    private MediaSessionCompat(final Context context, final String s, ComponentName mediaButtonReceiverComponent, final PendingIntent pendingIntent, final Bundle bundle) {
        this.mActiveListeners = new ArrayList<OnActiveChangeListener>();
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("tag must not be null or empty");
        }
        ComponentName component;
        if ((component = mediaButtonReceiverComponent) == null) {
            mediaButtonReceiverComponent = MediaButtonReceiver.getMediaButtonReceiverComponent(context);
            if ((component = mediaButtonReceiverComponent) == null) {
                Log.w("MediaSessionCompat", "Couldn't find a unique registered media button receiver in the given context.");
                component = mediaButtonReceiverComponent;
            }
        }
        PendingIntent broadcast = pendingIntent;
        if (component != null && (broadcast = pendingIntent) == null) {
            final Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
            intent.setComponent(component);
            broadcast = PendingIntent.getBroadcast(context, 0, intent, 0);
        }
        if (Build$VERSION.SDK_INT >= 28) {
            this.mImpl = (MediaSessionImpl)new MediaSessionImplApi28(context, s, bundle);
            this.setCallback((Callback)new Callback(this) {
                final MediaSessionCompat this$0;
            });
            this.mImpl.setMediaButtonReceiver(broadcast);
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            this.mImpl = (MediaSessionImpl)new MediaSessionImplApi21(context, s, bundle);
            this.setCallback((Callback)new Callback(this) {
                final MediaSessionCompat this$0;
            });
            this.mImpl.setMediaButtonReceiver(broadcast);
        }
        else if (Build$VERSION.SDK_INT >= 19) {
            this.mImpl = (MediaSessionImpl)new MediaSessionImplApi19(context, s, component, broadcast);
        }
        else if (Build$VERSION.SDK_INT >= 18) {
            this.mImpl = (MediaSessionImpl)new MediaSessionImplApi18(context, s, component, broadcast);
        }
        else {
            this.mImpl = (MediaSessionImpl)new MediaSessionImplBase(context, s, component, broadcast);
        }
        this.mController = new MediaControllerCompat(context, this);
        if (MediaSessionCompat.sMaxBitmapSize == 0) {
            MediaSessionCompat.sMaxBitmapSize = (int)(TypedValue.applyDimension(1, 320.0f, context.getResources().getDisplayMetrics()) + 0.5f);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public MediaSessionCompat(final Context context, final String s, final Bundle bundle) {
        this(context, s, null, null, bundle);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static void ensureClassLoader(@Nullable final Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(MediaSessionCompat.class.getClassLoader());
        }
    }
    
    public static MediaSessionCompat fromMediaSession(final Context context, final Object o) {
        if (context != null && o != null && Build$VERSION.SDK_INT >= 21) {
            return new MediaSessionCompat(context, (MediaSessionImpl)new MediaSessionImplApi21(o));
        }
        return null;
    }
    
    static PlaybackStateCompat getStateWithUpdatedPosition(final PlaybackStateCompat playbackStateCompat, final MediaMetadataCompat mediaMetadataCompat) {
        if (playbackStateCompat != null) {
            final long position = playbackStateCompat.getPosition();
            final long n = -1L;
            if (position != -1L) {
                if (playbackStateCompat.getState() == 3 || playbackStateCompat.getState() == 4 || playbackStateCompat.getState() == 5) {
                    final long lastPositionUpdateTime = playbackStateCompat.getLastPositionUpdateTime();
                    if (lastPositionUpdateTime > 0L) {
                        final long elapsedRealtime = SystemClock.elapsedRealtime();
                        final long n2 = (long)(playbackStateCompat.getPlaybackSpeed() * (elapsedRealtime - lastPositionUpdateTime)) + playbackStateCompat.getPosition();
                        long long1 = n;
                        if (mediaMetadataCompat != null) {
                            long1 = n;
                            if (mediaMetadataCompat.containsKey("android.media.metadata.DURATION")) {
                                long1 = mediaMetadataCompat.getLong("android.media.metadata.DURATION");
                            }
                        }
                        if (long1 < 0L || n2 <= long1) {
                            if (n2 < 0L) {
                                long1 = 0L;
                            }
                            else {
                                long1 = n2;
                            }
                        }
                        return new PlaybackStateCompat.Builder(playbackStateCompat).setState(playbackStateCompat.getState(), long1, playbackStateCompat.getPlaybackSpeed(), elapsedRealtime).build();
                    }
                }
                return playbackStateCompat;
            }
        }
        return playbackStateCompat;
    }
    
    public void addOnActiveChangeListener(final OnActiveChangeListener e) {
        if (e == null) {
            throw new IllegalArgumentException("Listener may not be null");
        }
        this.mActiveListeners.add(e);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public String getCallingPackage() {
        return this.mImpl.getCallingPackage();
    }
    
    public MediaControllerCompat getController() {
        return this.mController;
    }
    
    @NonNull
    public final MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
        return this.mImpl.getCurrentControllerInfo();
    }
    
    public Object getMediaSession() {
        return this.mImpl.getMediaSession();
    }
    
    public Object getRemoteControlClient() {
        return this.mImpl.getRemoteControlClient();
    }
    
    public Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }
    
    public boolean isActive() {
        return this.mImpl.isActive();
    }
    
    public void release() {
        this.mImpl.release();
    }
    
    public void removeOnActiveChangeListener(final OnActiveChangeListener o) {
        if (o == null) {
            throw new IllegalArgumentException("Listener may not be null");
        }
        this.mActiveListeners.remove(o);
    }
    
    public void sendSessionEvent(final String s, final Bundle bundle) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("event cannot be null or empty");
        }
        this.mImpl.sendSessionEvent(s, bundle);
    }
    
    public void setActive(final boolean active) {
        this.mImpl.setActive(active);
        final Iterator<OnActiveChangeListener> iterator = this.mActiveListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onActiveChanged();
        }
    }
    
    public void setCallback(final Callback callback) {
        this.setCallback(callback, null);
    }
    
    public void setCallback(final Callback callback, Handler handler) {
        if (callback == null) {
            this.mImpl.setCallback(null, null);
        }
        else {
            final MediaSessionImpl mImpl = this.mImpl;
            if (handler == null) {
                handler = new Handler();
            }
            mImpl.setCallback(callback, handler);
        }
    }
    
    public void setCaptioningEnabled(final boolean captioningEnabled) {
        this.mImpl.setCaptioningEnabled(captioningEnabled);
    }
    
    public void setExtras(final Bundle extras) {
        this.mImpl.setExtras(extras);
    }
    
    public void setFlags(final int flags) {
        this.mImpl.setFlags(flags);
    }
    
    public void setMediaButtonReceiver(final PendingIntent mediaButtonReceiver) {
        this.mImpl.setMediaButtonReceiver(mediaButtonReceiver);
    }
    
    public void setMetadata(final MediaMetadataCompat metadata) {
        this.mImpl.setMetadata(metadata);
    }
    
    public void setPlaybackState(final PlaybackStateCompat playbackState) {
        this.mImpl.setPlaybackState(playbackState);
    }
    
    public void setPlaybackToLocal(final int playbackToLocal) {
        this.mImpl.setPlaybackToLocal(playbackToLocal);
    }
    
    public void setPlaybackToRemote(final VolumeProviderCompat playbackToRemote) {
        if (playbackToRemote == null) {
            throw new IllegalArgumentException("volumeProvider may not be null!");
        }
        this.mImpl.setPlaybackToRemote(playbackToRemote);
    }
    
    public void setQueue(final List<QueueItem> queue) {
        this.mImpl.setQueue(queue);
    }
    
    public void setQueueTitle(final CharSequence queueTitle) {
        this.mImpl.setQueueTitle(queueTitle);
    }
    
    public void setRatingType(final int ratingType) {
        this.mImpl.setRatingType(ratingType);
    }
    
    public void setRepeatMode(final int repeatMode) {
        this.mImpl.setRepeatMode(repeatMode);
    }
    
    public void setSessionActivity(final PendingIntent sessionActivity) {
        this.mImpl.setSessionActivity(sessionActivity);
    }
    
    public void setShuffleMode(final int shuffleMode) {
        this.mImpl.setShuffleMode(shuffleMode);
    }
    
    public abstract static class Callback
    {
        private CallbackHandler mCallbackHandler;
        final Object mCallbackObj;
        private boolean mMediaPlayPauseKeyPending;
        WeakReference<MediaSessionImpl> mSessionImpl;
        
        public Callback() {
            this.mCallbackHandler = null;
            if (Build$VERSION.SDK_INT >= 24) {
                this.mCallbackObj = MediaSessionCompatApi24.createCallback((MediaSessionCompatApi24.Callback)new StubApi24());
            }
            else if (Build$VERSION.SDK_INT >= 23) {
                this.mCallbackObj = MediaSessionCompatApi23.createCallback((MediaSessionCompatApi23.Callback)new StubApi23());
            }
            else if (Build$VERSION.SDK_INT >= 21) {
                this.mCallbackObj = MediaSessionCompatApi21.createCallback((MediaSessionCompatApi21.Callback)new StubApi21());
            }
            else {
                this.mCallbackObj = null;
            }
        }
        
        void handleMediaPlayPauseKeySingleTapIfPending(final MediaSessionManager.RemoteUserInfo currentControllerInfo) {
            if (!this.mMediaPlayPauseKeyPending) {
                return;
            }
            boolean b = false;
            this.mMediaPlayPauseKeyPending = false;
            this.mCallbackHandler.removeMessages(1);
            final MediaSessionImpl mediaSessionImpl = this.mSessionImpl.get();
            if (mediaSessionImpl == null) {
                return;
            }
            final PlaybackStateCompat playbackState = mediaSessionImpl.getPlaybackState();
            long actions;
            if (playbackState == null) {
                actions = 0L;
            }
            else {
                actions = playbackState.getActions();
            }
            final boolean b2 = playbackState != null && playbackState.getState() == 3;
            final boolean b3 = (0x204L & actions) != 0x0L;
            if ((actions & 0x202L) != 0x0L) {
                b = true;
            }
            mediaSessionImpl.setCurrentControllerInfo(currentControllerInfo);
            if (b2 && b) {
                this.onPause();
            }
            else if (!b2 && b3) {
                this.onPlay();
            }
            mediaSessionImpl.setCurrentControllerInfo(null);
        }
        
        public void onAddQueueItem(final MediaDescriptionCompat mediaDescriptionCompat) {
        }
        
        public void onAddQueueItem(final MediaDescriptionCompat mediaDescriptionCompat, final int n) {
        }
        
        public void onCommand(final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
        }
        
        public void onCustomAction(final String s, final Bundle bundle) {
        }
        
        public void onFastForward() {
        }
        
        public boolean onMediaButtonEvent(final Intent intent) {
            if (Build$VERSION.SDK_INT >= 27) {
                return false;
            }
            final MediaSessionImpl mediaSessionImpl = this.mSessionImpl.get();
            if (mediaSessionImpl == null || this.mCallbackHandler == null) {
                return false;
            }
            final KeyEvent keyEvent = (KeyEvent)intent.getParcelableExtra("android.intent.extra.KEY_EVENT");
            if (keyEvent == null || keyEvent.getAction() != 0) {
                return false;
            }
            final MediaSessionManager.RemoteUserInfo currentControllerInfo = mediaSessionImpl.getCurrentControllerInfo();
            final int keyCode = keyEvent.getKeyCode();
            if (keyCode != 79 && keyCode != 85) {
                this.handleMediaPlayPauseKeySingleTapIfPending(currentControllerInfo);
                return false;
            }
            if (keyEvent.getRepeatCount() > 0) {
                this.handleMediaPlayPauseKeySingleTapIfPending(currentControllerInfo);
            }
            else if (this.mMediaPlayPauseKeyPending) {
                this.mCallbackHandler.removeMessages(1);
                this.mMediaPlayPauseKeyPending = false;
                final PlaybackStateCompat playbackState = mediaSessionImpl.getPlaybackState();
                long actions;
                if (playbackState == null) {
                    actions = 0L;
                }
                else {
                    actions = playbackState.getActions();
                }
                if ((actions & 0x20L) != 0x0L) {
                    this.onSkipToNext();
                }
            }
            else {
                this.mMediaPlayPauseKeyPending = true;
                this.mCallbackHandler.sendMessageDelayed(this.mCallbackHandler.obtainMessage(1, (Object)currentControllerInfo), (long)ViewConfiguration.getDoubleTapTimeout());
            }
            return true;
        }
        
        public void onPause() {
        }
        
        public void onPlay() {
        }
        
        public void onPlayFromMediaId(final String s, final Bundle bundle) {
        }
        
        public void onPlayFromSearch(final String s, final Bundle bundle) {
        }
        
        public void onPlayFromUri(final Uri uri, final Bundle bundle) {
        }
        
        public void onPrepare() {
        }
        
        public void onPrepareFromMediaId(final String s, final Bundle bundle) {
        }
        
        public void onPrepareFromSearch(final String s, final Bundle bundle) {
        }
        
        public void onPrepareFromUri(final Uri uri, final Bundle bundle) {
        }
        
        public void onRemoveQueueItem(final MediaDescriptionCompat mediaDescriptionCompat) {
        }
        
        @Deprecated
        public void onRemoveQueueItemAt(final int n) {
        }
        
        public void onRewind() {
        }
        
        public void onSeekTo(final long n) {
        }
        
        public void onSetCaptioningEnabled(final boolean b) {
        }
        
        public void onSetRating(final RatingCompat ratingCompat) {
        }
        
        public void onSetRating(final RatingCompat ratingCompat, final Bundle bundle) {
        }
        
        public void onSetRepeatMode(final int n) {
        }
        
        public void onSetShuffleMode(final int n) {
        }
        
        public void onSkipToNext() {
        }
        
        public void onSkipToPrevious() {
        }
        
        public void onSkipToQueueItem(final long n) {
        }
        
        public void onStop() {
        }
        
        void setSessionImpl(final MediaSessionImpl referent, final Handler handler) {
            this.mSessionImpl = new WeakReference<MediaSessionImpl>(referent);
            if (this.mCallbackHandler != null) {
                this.mCallbackHandler.removeCallbacksAndMessages((Object)null);
            }
            this.mCallbackHandler = new CallbackHandler(handler.getLooper());
        }
        
        private class CallbackHandler extends Handler
        {
            private static final int MSG_MEDIA_PLAY_PAUSE_KEY_DOUBLE_TAP_TIMEOUT = 1;
            final Callback this$0;
            
            CallbackHandler(final Callback this$0, final Looper looper) {
                this.this$0 = this$0;
                super(looper);
            }
            
            public void handleMessage(final Message message) {
                if (message.what == 1) {
                    this.this$0.handleMediaPlayPauseKeySingleTapIfPending((MediaSessionManager.RemoteUserInfo)message.obj);
                }
            }
        }
        
        @RequiresApi(21)
        private class StubApi21 implements MediaSessionCompatApi21.Callback
        {
            final MediaSessionCompat.Callback this$0;
            
            StubApi21(final MediaSessionCompat.Callback this$0) {
                this.this$0 = this$0;
            }
            
            @Override
            public void onCommand(final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
                try {
                    final boolean equals = s.equals("android.support.v4.media.session.command.GET_EXTRA_BINDER");
                    final MediaSessionCompat.QueueItem queueItem = null;
                    final IBinder binder = null;
                    if (equals) {
                        final MediaSessionImplApi21 mediaSessionImplApi21 = this.this$0.mSessionImpl.get();
                        if (mediaSessionImplApi21 != null) {
                            final Bundle bundle2 = new Bundle();
                            final Token sessionToken = mediaSessionImplApi21.getSessionToken();
                            final IMediaSession extraBinder = sessionToken.getExtraBinder();
                            IBinder binder2;
                            if (extraBinder == null) {
                                binder2 = binder;
                            }
                            else {
                                binder2 = extraBinder.asBinder();
                            }
                            BundleCompat.putBinder(bundle2, "android.support.v4.media.session.EXTRA_BINDER", binder2);
                            bundle2.putBundle("android.support.v4.media.session.SESSION_TOKEN2_BUNDLE", sessionToken.getSessionToken2Bundle());
                            resultReceiver.send(0, bundle2);
                        }
                    }
                    else if (s.equals("android.support.v4.media.session.command.ADD_QUEUE_ITEM")) {
                        this.this$0.onAddQueueItem((MediaDescriptionCompat)bundle.getParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION"));
                    }
                    else if (s.equals("android.support.v4.media.session.command.ADD_QUEUE_ITEM_AT")) {
                        this.this$0.onAddQueueItem((MediaDescriptionCompat)bundle.getParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION"), bundle.getInt("android.support.v4.media.session.command.ARGUMENT_INDEX"));
                    }
                    else if (s.equals("android.support.v4.media.session.command.REMOVE_QUEUE_ITEM")) {
                        this.this$0.onRemoveQueueItem((MediaDescriptionCompat)bundle.getParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION"));
                    }
                    else if (s.equals("android.support.v4.media.session.command.REMOVE_QUEUE_ITEM_AT")) {
                        final MediaSessionImplApi21 mediaSessionImplApi22 = this.this$0.mSessionImpl.get();
                        if (mediaSessionImplApi22 != null && mediaSessionImplApi22.mQueue != null) {
                            final int int1 = bundle.getInt("android.support.v4.media.session.command.ARGUMENT_INDEX", -1);
                            Object o = queueItem;
                            if (int1 >= 0) {
                                o = queueItem;
                                if (int1 < mediaSessionImplApi22.mQueue.size()) {
                                    o = mediaSessionImplApi22.mQueue.get(int1);
                                }
                            }
                            if (o != null) {
                                this.this$0.onRemoveQueueItem(((MediaSessionCompat.QueueItem)o).getDescription());
                            }
                        }
                    }
                    else {
                        this.this$0.onCommand(s, bundle, resultReceiver);
                    }
                }
                catch (final BadParcelableException ex) {
                    Log.e("MediaSessionCompat", "Could not unparcel the extra data.");
                }
            }
            
            @Override
            public void onCustomAction(String s, final Bundle bundle) {
                final Bundle bundle2 = bundle.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
                MediaSessionCompat.ensureClassLoader(bundle2);
                if (s.equals("android.support.v4.media.session.action.PLAY_FROM_URI")) {
                    this.this$0.onPlayFromUri((Uri)bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI"), bundle2);
                }
                else if (s.equals("android.support.v4.media.session.action.PREPARE")) {
                    this.this$0.onPrepare();
                }
                else if (s.equals("android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID")) {
                    s = bundle.getString("android.support.v4.media.session.action.ARGUMENT_MEDIA_ID");
                    this.this$0.onPrepareFromMediaId(s, bundle2);
                }
                else if (s.equals("android.support.v4.media.session.action.PREPARE_FROM_SEARCH")) {
                    s = bundle.getString("android.support.v4.media.session.action.ARGUMENT_QUERY");
                    this.this$0.onPrepareFromSearch(s, bundle2);
                }
                else if (s.equals("android.support.v4.media.session.action.PREPARE_FROM_URI")) {
                    this.this$0.onPrepareFromUri((Uri)bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI"), bundle2);
                }
                else if (s.equals("android.support.v4.media.session.action.SET_CAPTIONING_ENABLED")) {
                    this.this$0.onSetCaptioningEnabled(bundle.getBoolean("android.support.v4.media.session.action.ARGUMENT_CAPTIONING_ENABLED"));
                }
                else if (s.equals("android.support.v4.media.session.action.SET_REPEAT_MODE")) {
                    this.this$0.onSetRepeatMode(bundle.getInt("android.support.v4.media.session.action.ARGUMENT_REPEAT_MODE"));
                }
                else if (s.equals("android.support.v4.media.session.action.SET_SHUFFLE_MODE")) {
                    this.this$0.onSetShuffleMode(bundle.getInt("android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE"));
                }
                else if (s.equals("android.support.v4.media.session.action.SET_RATING")) {
                    this.this$0.onSetRating((RatingCompat)bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_RATING"), bundle2);
                }
                else {
                    this.this$0.onCustomAction(s, bundle);
                }
            }
            
            @Override
            public void onFastForward() {
                this.this$0.onFastForward();
            }
            
            @Override
            public boolean onMediaButtonEvent(final Intent intent) {
                return this.this$0.onMediaButtonEvent(intent);
            }
            
            @Override
            public void onPause() {
                this.this$0.onPause();
            }
            
            @Override
            public void onPlay() {
                this.this$0.onPlay();
            }
            
            @Override
            public void onPlayFromMediaId(final String s, final Bundle bundle) {
                this.this$0.onPlayFromMediaId(s, bundle);
            }
            
            @Override
            public void onPlayFromSearch(final String s, final Bundle bundle) {
                this.this$0.onPlayFromSearch(s, bundle);
            }
            
            @Override
            public void onRewind() {
                this.this$0.onRewind();
            }
            
            @Override
            public void onSeekTo(final long n) {
                this.this$0.onSeekTo(n);
            }
            
            @Override
            public void onSetRating(final Object o) {
                this.this$0.onSetRating(RatingCompat.fromRating(o));
            }
            
            @Override
            public void onSetRating(final Object o, final Bundle bundle) {
            }
            
            @Override
            public void onSkipToNext() {
                this.this$0.onSkipToNext();
            }
            
            @Override
            public void onSkipToPrevious() {
                this.this$0.onSkipToPrevious();
            }
            
            @Override
            public void onSkipToQueueItem(final long n) {
                this.this$0.onSkipToQueueItem(n);
            }
            
            @Override
            public void onStop() {
                this.this$0.onStop();
            }
        }
        
        @RequiresApi(23)
        private class StubApi23 extends StubApi21 implements MediaSessionCompatApi23.Callback
        {
            final MediaSessionCompat.Callback this$0;
            
            StubApi23(final MediaSessionCompat.Callback this$0) {
                this.this$0 = this$0.super();
            }
            
            @Override
            public void onPlayFromUri(final Uri uri, final Bundle bundle) {
                this.this$0.onPlayFromUri(uri, bundle);
            }
        }
        
        @RequiresApi(24)
        private class StubApi24 extends StubApi23 implements MediaSessionCompatApi24.Callback
        {
            final MediaSessionCompat.Callback this$0;
            
            StubApi24(final MediaSessionCompat.Callback this$0) {
                this.this$0 = this$0.super();
            }
            
            @Override
            public void onPrepare() {
                this.this$0.onPrepare();
            }
            
            @Override
            public void onPrepareFromMediaId(final String s, final Bundle bundle) {
                this.this$0.onPrepareFromMediaId(s, bundle);
            }
            
            @Override
            public void onPrepareFromSearch(final String s, final Bundle bundle) {
                this.this$0.onPrepareFromSearch(s, bundle);
            }
            
            @Override
            public void onPrepareFromUri(final Uri uri, final Bundle bundle) {
                this.this$0.onPrepareFromUri(uri, bundle);
            }
        }
    }
    
    interface MediaSessionImpl
    {
        String getCallingPackage();
        
        MediaSessionManager.RemoteUserInfo getCurrentControllerInfo();
        
        Object getMediaSession();
        
        PlaybackStateCompat getPlaybackState();
        
        Object getRemoteControlClient();
        
        Token getSessionToken();
        
        boolean isActive();
        
        void release();
        
        void sendSessionEvent(final String p0, final Bundle p1);
        
        void setActive(final boolean p0);
        
        void setCallback(final Callback p0, final Handler p1);
        
        void setCaptioningEnabled(final boolean p0);
        
        void setCurrentControllerInfo(final MediaSessionManager.RemoteUserInfo p0);
        
        void setExtras(final Bundle p0);
        
        void setFlags(final int p0);
        
        void setMediaButtonReceiver(final PendingIntent p0);
        
        void setMetadata(final MediaMetadataCompat p0);
        
        void setPlaybackState(final PlaybackStateCompat p0);
        
        void setPlaybackToLocal(final int p0);
        
        void setPlaybackToRemote(final VolumeProviderCompat p0);
        
        void setQueue(final List<QueueItem> p0);
        
        void setQueueTitle(final CharSequence p0);
        
        void setRatingType(final int p0);
        
        void setRepeatMode(final int p0);
        
        void setSessionActivity(final PendingIntent p0);
        
        void setShuffleMode(final int p0);
    }
    
    @RequiresApi(18)
    static class MediaSessionImplApi18 extends MediaSessionImplBase
    {
        private static boolean sIsMbrPendingIntentSupported = true;
        
        MediaSessionImplApi18(final Context context, final String s, final ComponentName componentName, final PendingIntent pendingIntent) {
            super(context, s, componentName, pendingIntent);
        }
        
        @Override
        int getRccTransportControlFlagsFromActions(final long n) {
            int rccTransportControlFlagsFromActions = super.getRccTransportControlFlagsFromActions(n);
            if ((n & 0x100L) != 0x0L) {
                rccTransportControlFlagsFromActions |= 0x100;
            }
            return rccTransportControlFlagsFromActions;
        }
        
        @Override
        void registerMediaButtonEventReceiver(final PendingIntent pendingIntent, final ComponentName componentName) {
            if (MediaSessionImplApi18.sIsMbrPendingIntentSupported) {
                try {
                    this.mAudioManager.registerMediaButtonEventReceiver(pendingIntent);
                }
                catch (final NullPointerException ex) {
                    Log.w("MediaSessionCompat", "Unable to register media button event receiver with PendingIntent, falling back to ComponentName.");
                    MediaSessionImplApi18.sIsMbrPendingIntentSupported = false;
                }
            }
            if (!MediaSessionImplApi18.sIsMbrPendingIntentSupported) {
                super.registerMediaButtonEventReceiver(pendingIntent, componentName);
            }
        }
        
        @Override
        public void setCallback(final Callback callback, final Handler handler) {
            super.setCallback(callback, handler);
            if (callback == null) {
                this.mRcc.setPlaybackPositionUpdateListener((RemoteControlClient$OnPlaybackPositionUpdateListener)null);
            }
            else {
                this.mRcc.setPlaybackPositionUpdateListener((RemoteControlClient$OnPlaybackPositionUpdateListener)new RemoteControlClient$OnPlaybackPositionUpdateListener(this) {
                    final MediaSessionImplApi18 this$0;
                    
                    public void onPlaybackPositionUpdate(final long l) {
                        ((MediaSessionImplBase)this.this$0).postToHandler(18, -1, -1, l, null);
                    }
                });
            }
        }
        
        @Override
        void setRccState(final PlaybackStateCompat playbackStateCompat) {
            final long position = playbackStateCompat.getPosition();
            final float playbackSpeed = playbackStateCompat.getPlaybackSpeed();
            final long lastPositionUpdateTime = playbackStateCompat.getLastPositionUpdateTime();
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            long n = position;
            if (playbackStateCompat.getState() == 3) {
                final long n2 = 0L;
                n = position;
                if (position > 0L) {
                    long n3 = n2;
                    if (lastPositionUpdateTime > 0L) {
                        final long n4 = n3 = elapsedRealtime - lastPositionUpdateTime;
                        if (playbackSpeed > 0.0f) {
                            n3 = n4;
                            if (playbackSpeed != 1.0f) {
                                n3 = (long)(n4 * playbackSpeed);
                            }
                        }
                    }
                    n = position + n3;
                }
            }
            this.mRcc.setPlaybackState(((MediaSessionImplBase)this).getRccStateFromState(playbackStateCompat.getState()), n, playbackSpeed);
        }
        
        @Override
        void unregisterMediaButtonEventReceiver(final PendingIntent pendingIntent, final ComponentName componentName) {
            if (MediaSessionImplApi18.sIsMbrPendingIntentSupported) {
                this.mAudioManager.unregisterMediaButtonEventReceiver(pendingIntent);
            }
            else {
                super.unregisterMediaButtonEventReceiver(pendingIntent, componentName);
            }
        }
    }
    
    static class MediaSessionImplBase implements MediaSessionImpl
    {
        static final int RCC_PLAYSTATE_NONE = 0;
        final AudioManager mAudioManager;
        volatile Callback mCallback;
        boolean mCaptioningEnabled;
        private final Context mContext;
        final RemoteCallbackList<IMediaControllerCallback> mControllerCallbacks;
        boolean mDestroyed;
        Bundle mExtras;
        int mFlags;
        private MessageHandler mHandler;
        boolean mIsActive;
        private boolean mIsMbrRegistered;
        private boolean mIsRccRegistered;
        int mLocalStream;
        final Object mLock;
        private final ComponentName mMediaButtonReceiverComponentName;
        private final PendingIntent mMediaButtonReceiverIntent;
        MediaMetadataCompat mMetadata;
        final String mPackageName;
        List<QueueItem> mQueue;
        CharSequence mQueueTitle;
        int mRatingType;
        final RemoteControlClient mRcc;
        private MediaSessionManager.RemoteUserInfo mRemoteUserInfo;
        int mRepeatMode;
        PendingIntent mSessionActivity;
        int mShuffleMode;
        PlaybackStateCompat mState;
        private final MediaSessionStub mStub;
        final String mTag;
        private final Token mToken;
        private VolumeProviderCompat.Callback mVolumeCallback;
        VolumeProviderCompat mVolumeProvider;
        int mVolumeType;
        
        public MediaSessionImplBase(final Context mContext, final String mTag, final ComponentName mMediaButtonReceiverComponentName, final PendingIntent mMediaButtonReceiverIntent) {
            this.mLock = new Object();
            this.mControllerCallbacks = (RemoteCallbackList<IMediaControllerCallback>)new RemoteCallbackList();
            this.mDestroyed = false;
            this.mIsActive = false;
            this.mIsMbrRegistered = false;
            this.mIsRccRegistered = false;
            this.mVolumeCallback = new VolumeProviderCompat.Callback() {
                final MediaSessionImplBase this$0;
                
                @Override
                public void onVolumeChanged(final VolumeProviderCompat volumeProviderCompat) {
                    if (this.this$0.mVolumeProvider != volumeProviderCompat) {
                        return;
                    }
                    this.this$0.sendVolumeInfoChanged(new ParcelableVolumeInfo(this.this$0.mVolumeType, this.this$0.mLocalStream, volumeProviderCompat.getVolumeControl(), volumeProviderCompat.getMaxVolume(), volumeProviderCompat.getCurrentVolume()));
                }
            };
            if (mMediaButtonReceiverComponentName == null) {
                throw new IllegalArgumentException("MediaButtonReceiver component may not be null.");
            }
            this.mContext = mContext;
            this.mPackageName = mContext.getPackageName();
            this.mAudioManager = (AudioManager)mContext.getSystemService("audio");
            this.mTag = mTag;
            this.mMediaButtonReceiverComponentName = mMediaButtonReceiverComponentName;
            this.mMediaButtonReceiverIntent = mMediaButtonReceiverIntent;
            this.mStub = new MediaSessionStub();
            this.mToken = new Token(this.mStub);
            this.mRatingType = 0;
            this.mVolumeType = 1;
            this.mLocalStream = 3;
            this.mRcc = new RemoteControlClient(mMediaButtonReceiverIntent);
        }
        
        private void sendCaptioningEnabled(final boolean b) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0039: {
                    if (n < 0) {
                        break Label_0039;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onCaptioningEnabledChanged(b);
                            --n;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendEvent(final String s, final Bundle bundle) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0036_Outer:
            while (true) {
                Label_0042: {
                    if (n < 0) {
                        break Label_0042;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onEvent(s, bundle);
                            --n;
                            continue Label_0036_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendExtras(final Bundle bundle) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0039: {
                    if (n < 0) {
                        break Label_0039;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onExtrasChanged(bundle);
                            --n;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendMetadata(final MediaMetadataCompat mediaMetadataCompat) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0039: {
                    if (n < 0) {
                        break Label_0039;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onMetadataChanged(mediaMetadataCompat);
                            --n;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendQueue(final List<QueueItem> list) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0039: {
                    if (n < 0) {
                        break Label_0039;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onQueueChanged(list);
                            --n;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendQueueTitle(final CharSequence charSequence) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0039: {
                    if (n < 0) {
                        break Label_0039;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onQueueTitleChanged(charSequence);
                            --n;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendRepeatMode(final int n) {
            int n2 = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0039: {
                    if (n2 < 0) {
                        break Label_0039;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n2);
                    while (true) {
                        try {
                            mediaControllerCallback.onRepeatModeChanged(n);
                            --n2;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendSessionDestroyed() {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0032_Outer:
            while (true) {
                Label_0038: {
                    if (n < 0) {
                        break Label_0038;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onSessionDestroyed();
                            --n;
                            continue Label_0032_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                            this.mControllerCallbacks.kill();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendShuffleMode(final int n) {
            int n2 = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0039: {
                    if (n2 < 0) {
                        break Label_0039;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n2);
                    while (true) {
                        try {
                            mediaControllerCallback.onShuffleModeChanged(n);
                            --n2;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendState(final PlaybackStateCompat playbackStateCompat) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0039: {
                    if (n < 0) {
                        break Label_0039;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onPlaybackStateChanged(playbackStateCompat);
                            --n;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        void adjustVolume(final int n, final int n2) {
            if (this.mVolumeType == 2) {
                if (this.mVolumeProvider != null) {
                    this.mVolumeProvider.onAdjustVolume(n);
                }
            }
            else {
                this.mAudioManager.adjustStreamVolume(this.mLocalStream, n, n2);
            }
        }
        
        RemoteControlClient$MetadataEditor buildRccMetadata(final Bundle bundle) {
            final RemoteControlClient$MetadataEditor editMetadata = this.mRcc.editMetadata(true);
            if (bundle == null) {
                return editMetadata;
            }
            if (bundle.containsKey("android.media.metadata.ART")) {
                final Bitmap bitmap = (Bitmap)bundle.getParcelable("android.media.metadata.ART");
                Bitmap copy;
                if ((copy = bitmap) != null) {
                    copy = bitmap.copy(bitmap.getConfig(), false);
                }
                editMetadata.putBitmap(100, copy);
            }
            else if (bundle.containsKey("android.media.metadata.ALBUM_ART")) {
                final Bitmap bitmap2 = (Bitmap)bundle.getParcelable("android.media.metadata.ALBUM_ART");
                Bitmap copy2;
                if ((copy2 = bitmap2) != null) {
                    copy2 = bitmap2.copy(bitmap2.getConfig(), false);
                }
                editMetadata.putBitmap(100, copy2);
            }
            if (bundle.containsKey("android.media.metadata.ALBUM")) {
                editMetadata.putString(1, bundle.getString("android.media.metadata.ALBUM"));
            }
            if (bundle.containsKey("android.media.metadata.ALBUM_ARTIST")) {
                editMetadata.putString(13, bundle.getString("android.media.metadata.ALBUM_ARTIST"));
            }
            if (bundle.containsKey("android.media.metadata.ARTIST")) {
                editMetadata.putString(2, bundle.getString("android.media.metadata.ARTIST"));
            }
            if (bundle.containsKey("android.media.metadata.AUTHOR")) {
                editMetadata.putString(3, bundle.getString("android.media.metadata.AUTHOR"));
            }
            if (bundle.containsKey("android.media.metadata.COMPILATION")) {
                editMetadata.putString(15, bundle.getString("android.media.metadata.COMPILATION"));
            }
            if (bundle.containsKey("android.media.metadata.COMPOSER")) {
                editMetadata.putString(4, bundle.getString("android.media.metadata.COMPOSER"));
            }
            if (bundle.containsKey("android.media.metadata.DATE")) {
                editMetadata.putString(5, bundle.getString("android.media.metadata.DATE"));
            }
            if (bundle.containsKey("android.media.metadata.DISC_NUMBER")) {
                editMetadata.putLong(14, bundle.getLong("android.media.metadata.DISC_NUMBER"));
            }
            if (bundle.containsKey("android.media.metadata.DURATION")) {
                editMetadata.putLong(9, bundle.getLong("android.media.metadata.DURATION"));
            }
            if (bundle.containsKey("android.media.metadata.GENRE")) {
                editMetadata.putString(6, bundle.getString("android.media.metadata.GENRE"));
            }
            if (bundle.containsKey("android.media.metadata.TITLE")) {
                editMetadata.putString(7, bundle.getString("android.media.metadata.TITLE"));
            }
            if (bundle.containsKey("android.media.metadata.TRACK_NUMBER")) {
                editMetadata.putLong(0, bundle.getLong("android.media.metadata.TRACK_NUMBER"));
            }
            if (bundle.containsKey("android.media.metadata.WRITER")) {
                editMetadata.putString(11, bundle.getString("android.media.metadata.WRITER"));
            }
            return editMetadata;
        }
        
        @Override
        public String getCallingPackage() {
            return null;
        }
        
        @Override
        public MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
            synchronized (this.mLock) {
                return this.mRemoteUserInfo;
            }
        }
        
        @Override
        public Object getMediaSession() {
            return null;
        }
        
        @Override
        public PlaybackStateCompat getPlaybackState() {
            synchronized (this.mLock) {
                return this.mState;
            }
        }
        
        int getRccStateFromState(final int n) {
            switch (n) {
                default: {
                    return -1;
                }
                case 10:
                case 11: {
                    return 6;
                }
                case 9: {
                    return 7;
                }
                case 7: {
                    return 9;
                }
                case 6:
                case 8: {
                    return 8;
                }
                case 5: {
                    return 5;
                }
                case 4: {
                    return 4;
                }
                case 3: {
                    return 3;
                }
                case 2: {
                    return 2;
                }
                case 1: {
                    return 1;
                }
                case 0: {
                    return 0;
                }
            }
        }
        
        int getRccTransportControlFlagsFromActions(final long n) {
            int n2;
            if ((0x1L & n) != 0x0L) {
                n2 = 32;
            }
            else {
                n2 = 0;
            }
            int n3 = n2;
            if ((0x2L & n) != 0x0L) {
                n3 = (n2 | 0x10);
            }
            int n4 = n3;
            if ((0x4L & n) != 0x0L) {
                n4 = (n3 | 0x4);
            }
            int n5 = n4;
            if ((0x8L & n) != 0x0L) {
                n5 = (n4 | 0x2);
            }
            int n6 = n5;
            if ((0x10L & n) != 0x0L) {
                n6 = (n5 | 0x1);
            }
            int n7 = n6;
            if ((0x20L & n) != 0x0L) {
                n7 = (n6 | 0x80);
            }
            int n8 = n7;
            if ((0x40L & n) != 0x0L) {
                n8 = (n7 | 0x40);
            }
            int n9 = n8;
            if ((n & 0x200L) != 0x0L) {
                n9 = (n8 | 0x8);
            }
            return n9;
        }
        
        @Override
        public Object getRemoteControlClient() {
            return null;
        }
        
        @Override
        public Token getSessionToken() {
            return this.mToken;
        }
        
        @Override
        public boolean isActive() {
            return this.mIsActive;
        }
        
        void postToHandler(final int n, final int n2, final int n3, final Object o, final Bundle bundle) {
            synchronized (this.mLock) {
                if (this.mHandler != null) {
                    final Message obtainMessage = this.mHandler.obtainMessage(n, n2, n3, o);
                    final Bundle data = new Bundle();
                    data.putString("data_calling_pkg", "android.media.session.MediaController");
                    data.putInt("data_calling_pid", Binder.getCallingPid());
                    data.putInt("data_calling_uid", Binder.getCallingUid());
                    if (bundle != null) {
                        data.putBundle("data_extras", bundle);
                    }
                    obtainMessage.setData(data);
                    obtainMessage.sendToTarget();
                }
            }
        }
        
        void registerMediaButtonEventReceiver(final PendingIntent pendingIntent, final ComponentName componentName) {
            this.mAudioManager.registerMediaButtonEventReceiver(componentName);
        }
        
        @Override
        public void release() {
            this.mIsActive = false;
            this.mDestroyed = true;
            this.update();
            this.sendSessionDestroyed();
        }
        
        @Override
        public void sendSessionEvent(final String s, final Bundle bundle) {
            this.sendEvent(s, bundle);
        }
        
        void sendVolumeInfoChanged(final ParcelableVolumeInfo parcelableVolumeInfo) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0039: {
                    if (n < 0) {
                        break Label_0039;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onVolumeInfoChanged(parcelableVolumeInfo);
                            --n;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        @Override
        public void setActive(final boolean mIsActive) {
            if (mIsActive == this.mIsActive) {
                return;
            }
            this.mIsActive = mIsActive;
            if (this.update()) {
                this.setMetadata(this.mMetadata);
                this.setPlaybackState(this.mState);
            }
        }
        
        @Override
        public void setCallback(final Callback mCallback, final Handler handler) {
            this.mCallback = mCallback;
            if (mCallback != null) {
                Handler handler2;
                if ((handler2 = handler) == null) {
                    handler2 = new Handler();
                }
                synchronized (this.mLock) {
                    if (this.mHandler != null) {
                        this.mHandler.removeCallbacksAndMessages((Object)null);
                    }
                    this.mHandler = new MessageHandler(handler2.getLooper());
                    this.mCallback.setSessionImpl(this, handler2);
                }
            }
        }
        
        @Override
        public void setCaptioningEnabled(final boolean mCaptioningEnabled) {
            if (this.mCaptioningEnabled != mCaptioningEnabled) {
                this.sendCaptioningEnabled(this.mCaptioningEnabled = mCaptioningEnabled);
            }
        }
        
        @Override
        public void setCurrentControllerInfo(final MediaSessionManager.RemoteUserInfo mRemoteUserInfo) {
            synchronized (this.mLock) {
                this.mRemoteUserInfo = mRemoteUserInfo;
            }
        }
        
        @Override
        public void setExtras(final Bundle mExtras) {
            this.sendExtras(this.mExtras = mExtras);
        }
        
        @Override
        public void setFlags(final int mFlags) {
            synchronized (this.mLock) {
                this.mFlags = mFlags;
                monitorexit(this.mLock);
                this.update();
            }
        }
        
        @Override
        public void setMediaButtonReceiver(final PendingIntent pendingIntent) {
        }
        
        @Override
        public void setMetadata(MediaMetadataCompat mediaMetadataCompat) {
            MediaMetadataCompat build = mediaMetadataCompat;
            if (mediaMetadataCompat != null) {
                build = new MediaMetadataCompat.Builder(mediaMetadataCompat, MediaSessionCompat.sMaxBitmapSize).build();
            }
            mediaMetadataCompat = (MediaMetadataCompat)this.mLock;
            synchronized (mediaMetadataCompat) {
                this.mMetadata = build;
                monitorexit(mediaMetadataCompat);
                this.sendMetadata(build);
                if (!this.mIsActive) {
                    return;
                }
                if (build == null) {
                    mediaMetadataCompat = null;
                }
                else {
                    mediaMetadataCompat = (MediaMetadataCompat)build.getBundle();
                }
                this.buildRccMetadata((Bundle)mediaMetadataCompat).apply();
            }
        }
        
        @Override
        public void setPlaybackState(final PlaybackStateCompat playbackStateCompat) {
            synchronized (this.mLock) {
                this.mState = playbackStateCompat;
                monitorexit(this.mLock);
                this.sendState(playbackStateCompat);
                if (!this.mIsActive) {
                    return;
                }
                if (playbackStateCompat == null) {
                    this.mRcc.setPlaybackState(0);
                    this.mRcc.setTransportControlFlags(0);
                }
                else {
                    this.setRccState(playbackStateCompat);
                    this.mRcc.setTransportControlFlags(this.getRccTransportControlFlagsFromActions(playbackStateCompat.getActions()));
                }
            }
        }
        
        @Override
        public void setPlaybackToLocal(final int mLocalStream) {
            if (this.mVolumeProvider != null) {
                this.mVolumeProvider.setCallback(null);
            }
            this.mLocalStream = mLocalStream;
            this.mVolumeType = 1;
            this.sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, 2, this.mAudioManager.getStreamMaxVolume(this.mLocalStream), this.mAudioManager.getStreamVolume(this.mLocalStream)));
        }
        
        @Override
        public void setPlaybackToRemote(final VolumeProviderCompat mVolumeProvider) {
            if (mVolumeProvider == null) {
                throw new IllegalArgumentException("volumeProvider may not be null");
            }
            if (this.mVolumeProvider != null) {
                this.mVolumeProvider.setCallback(null);
            }
            this.mVolumeType = 2;
            this.mVolumeProvider = mVolumeProvider;
            this.sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, this.mVolumeProvider.getVolumeControl(), this.mVolumeProvider.getMaxVolume(), this.mVolumeProvider.getCurrentVolume()));
            mVolumeProvider.setCallback(this.mVolumeCallback);
        }
        
        @Override
        public void setQueue(final List<QueueItem> mQueue) {
            this.sendQueue(this.mQueue = mQueue);
        }
        
        @Override
        public void setQueueTitle(final CharSequence mQueueTitle) {
            this.sendQueueTitle(this.mQueueTitle = mQueueTitle);
        }
        
        @Override
        public void setRatingType(final int mRatingType) {
            this.mRatingType = mRatingType;
        }
        
        void setRccState(final PlaybackStateCompat playbackStateCompat) {
            this.mRcc.setPlaybackState(this.getRccStateFromState(playbackStateCompat.getState()));
        }
        
        @Override
        public void setRepeatMode(final int mRepeatMode) {
            if (this.mRepeatMode != mRepeatMode) {
                this.sendRepeatMode(this.mRepeatMode = mRepeatMode);
            }
        }
        
        @Override
        public void setSessionActivity(final PendingIntent mSessionActivity) {
            synchronized (this.mLock) {
                this.mSessionActivity = mSessionActivity;
            }
        }
        
        @Override
        public void setShuffleMode(final int mShuffleMode) {
            if (this.mShuffleMode != mShuffleMode) {
                this.sendShuffleMode(this.mShuffleMode = mShuffleMode);
            }
        }
        
        void setVolumeTo(final int n, final int n2) {
            if (this.mVolumeType == 2) {
                if (this.mVolumeProvider != null) {
                    this.mVolumeProvider.onSetVolumeTo(n);
                }
            }
            else {
                this.mAudioManager.setStreamVolume(this.mLocalStream, n, n2);
            }
        }
        
        void unregisterMediaButtonEventReceiver(final PendingIntent pendingIntent, final ComponentName componentName) {
            this.mAudioManager.unregisterMediaButtonEventReceiver(componentName);
        }
        
        boolean update() {
            final boolean mIsActive = this.mIsActive;
            boolean b = true;
            if (mIsActive) {
                if (!this.mIsMbrRegistered && (this.mFlags & 0x1) != 0x0) {
                    this.registerMediaButtonEventReceiver(this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
                    this.mIsMbrRegistered = true;
                }
                else if (this.mIsMbrRegistered && (this.mFlags & 0x1) == 0x0) {
                    this.unregisterMediaButtonEventReceiver(this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
                    this.mIsMbrRegistered = false;
                }
                if (!this.mIsRccRegistered && (this.mFlags & 0x2) != 0x0) {
                    this.mAudioManager.registerRemoteControlClient(this.mRcc);
                    this.mIsRccRegistered = true;
                    return b;
                }
                if (this.mIsRccRegistered && (this.mFlags & 0x2) == 0x0) {
                    this.mRcc.setPlaybackState(0);
                    this.mAudioManager.unregisterRemoteControlClient(this.mRcc);
                    this.mIsRccRegistered = false;
                }
            }
            else {
                if (this.mIsMbrRegistered) {
                    this.unregisterMediaButtonEventReceiver(this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
                    this.mIsMbrRegistered = false;
                }
                if (this.mIsRccRegistered) {
                    this.mRcc.setPlaybackState(0);
                    this.mAudioManager.unregisterRemoteControlClient(this.mRcc);
                    this.mIsRccRegistered = false;
                }
            }
            b = false;
            return b;
        }
        
        private static final class Command
        {
            public final String command;
            public final Bundle extras;
            public final ResultReceiver stub;
            
            public Command(final String command, final Bundle extras, final ResultReceiver stub) {
                this.command = command;
                this.extras = extras;
                this.stub = stub;
            }
        }
        
        class MediaSessionStub extends Stub
        {
            final MediaSessionImplBase this$0;
            
            MediaSessionStub(final MediaSessionImplBase this$0) {
                this.this$0 = this$0;
            }
            
            public void addQueueItem(final MediaDescriptionCompat mediaDescriptionCompat) {
                this.postToHandler(25, mediaDescriptionCompat);
            }
            
            public void addQueueItemAt(final MediaDescriptionCompat mediaDescriptionCompat, final int n) {
                this.postToHandler(26, mediaDescriptionCompat, n);
            }
            
            public void adjustVolume(final int n, final int n2, final String s) {
                this.this$0.adjustVolume(n, n2);
            }
            
            public void fastForward() throws RemoteException {
                this.postToHandler(16);
            }
            
            public Bundle getExtras() {
                synchronized (this.this$0.mLock) {
                    return this.this$0.mExtras;
                }
            }
            
            public long getFlags() {
                synchronized (this.this$0.mLock) {
                    return this.this$0.mFlags;
                }
            }
            
            public PendingIntent getLaunchPendingIntent() {
                synchronized (this.this$0.mLock) {
                    return this.this$0.mSessionActivity;
                }
            }
            
            public MediaMetadataCompat getMetadata() {
                return this.this$0.mMetadata;
            }
            
            public String getPackageName() {
                return this.this$0.mPackageName;
            }
            
            public PlaybackStateCompat getPlaybackState() {
                synchronized (this.this$0.mLock) {
                    final PlaybackStateCompat mState = this.this$0.mState;
                    final MediaMetadataCompat mMetadata = this.this$0.mMetadata;
                    monitorexit(this.this$0.mLock);
                    return MediaSessionCompat.getStateWithUpdatedPosition(mState, mMetadata);
                }
            }
            
            public List<QueueItem> getQueue() {
                synchronized (this.this$0.mLock) {
                    return this.this$0.mQueue;
                }
            }
            
            public CharSequence getQueueTitle() {
                return this.this$0.mQueueTitle;
            }
            
            public int getRatingType() {
                return this.this$0.mRatingType;
            }
            
            public int getRepeatMode() {
                return this.this$0.mRepeatMode;
            }
            
            public int getShuffleMode() {
                return this.this$0.mShuffleMode;
            }
            
            public String getTag() {
                return this.this$0.mTag;
            }
            
            public ParcelableVolumeInfo getVolumeAttributes() {
                synchronized (this.this$0.mLock) {
                    final int mVolumeType = this.this$0.mVolumeType;
                    final int mLocalStream = this.this$0.mLocalStream;
                    final VolumeProviderCompat mVolumeProvider = this.this$0.mVolumeProvider;
                    int volumeControl;
                    int n;
                    int n2;
                    if (mVolumeType == 2) {
                        volumeControl = mVolumeProvider.getVolumeControl();
                        n = mVolumeProvider.getMaxVolume();
                        n2 = mVolumeProvider.getCurrentVolume();
                    }
                    else {
                        n = this.this$0.mAudioManager.getStreamMaxVolume(mLocalStream);
                        n2 = this.this$0.mAudioManager.getStreamVolume(mLocalStream);
                        volumeControl = 2;
                    }
                    monitorexit(this.this$0.mLock);
                    return new ParcelableVolumeInfo(mVolumeType, mLocalStream, volumeControl, n, n2);
                }
            }
            
            public boolean isCaptioningEnabled() {
                return this.this$0.mCaptioningEnabled;
            }
            
            public boolean isShuffleModeEnabledRemoved() {
                return false;
            }
            
            public boolean isTransportControlEnabled() {
                return (this.this$0.mFlags & 0x2) != 0x0;
            }
            
            public void next() throws RemoteException {
                this.postToHandler(14);
            }
            
            public void pause() throws RemoteException {
                this.postToHandler(12);
            }
            
            public void play() throws RemoteException {
                this.postToHandler(7);
            }
            
            public void playFromMediaId(final String s, final Bundle bundle) throws RemoteException {
                this.postToHandler(8, s, bundle);
            }
            
            public void playFromSearch(final String s, final Bundle bundle) throws RemoteException {
                this.postToHandler(9, s, bundle);
            }
            
            public void playFromUri(final Uri uri, final Bundle bundle) throws RemoteException {
                this.postToHandler(10, uri, bundle);
            }
            
            void postToHandler(final int n) {
                this.this$0.postToHandler(n, 0, 0, null, null);
            }
            
            void postToHandler(final int n, final int n2) {
                this.this$0.postToHandler(n, n2, 0, null, null);
            }
            
            void postToHandler(final int n, final Object o) {
                this.this$0.postToHandler(n, 0, 0, o, null);
            }
            
            void postToHandler(final int n, final Object o, final int n2) {
                this.this$0.postToHandler(n, n2, 0, o, null);
            }
            
            void postToHandler(final int n, final Object o, final Bundle bundle) {
                this.this$0.postToHandler(n, 0, 0, o, bundle);
            }
            
            public void prepare() throws RemoteException {
                this.postToHandler(3);
            }
            
            public void prepareFromMediaId(final String s, final Bundle bundle) throws RemoteException {
                this.postToHandler(4, s, bundle);
            }
            
            public void prepareFromSearch(final String s, final Bundle bundle) throws RemoteException {
                this.postToHandler(5, s, bundle);
            }
            
            public void prepareFromUri(final Uri uri, final Bundle bundle) throws RemoteException {
                this.postToHandler(6, uri, bundle);
            }
            
            public void previous() throws RemoteException {
                this.postToHandler(15);
            }
            
            public void rate(final RatingCompat ratingCompat) throws RemoteException {
                this.postToHandler(19, ratingCompat);
            }
            
            public void rateWithExtras(final RatingCompat ratingCompat, final Bundle bundle) throws RemoteException {
                this.postToHandler(31, ratingCompat, bundle);
            }
            
            public void registerCallbackListener(final IMediaControllerCallback mediaControllerCallback) {
                Label_0017: {
                    if (!this.this$0.mDestroyed) {
                        break Label_0017;
                    }
                    try {
                        mediaControllerCallback.onSessionDestroyed();
                        return;
                        this.this$0.mControllerCallbacks.register((IInterface)mediaControllerCallback, (Object)new MediaSessionManager.RemoteUserInfo("android.media.session.MediaController", getCallingPid(), getCallingUid()));
                    }
                    catch (final Exception ex) {}
                }
            }
            
            public void removeQueueItem(final MediaDescriptionCompat mediaDescriptionCompat) {
                this.postToHandler(27, mediaDescriptionCompat);
            }
            
            public void removeQueueItemAt(final int n) {
                this.postToHandler(28, n);
            }
            
            public void rewind() throws RemoteException {
                this.postToHandler(17);
            }
            
            public void seekTo(final long l) throws RemoteException {
                this.postToHandler(18, l);
            }
            
            public void sendCommand(final String s, final Bundle bundle, final ResultReceiverWrapper resultReceiverWrapper) {
                this.postToHandler(1, new Command(s, bundle, resultReceiverWrapper.mResultReceiver));
            }
            
            public void sendCustomAction(final String s, final Bundle bundle) throws RemoteException {
                this.postToHandler(20, s, bundle);
            }
            
            public boolean sendMediaButton(final KeyEvent keyEvent) {
                final int mFlags = this.this$0.mFlags;
                boolean b = true;
                if ((mFlags & 0x1) == 0x0) {
                    b = false;
                }
                if (b) {
                    this.postToHandler(21, keyEvent);
                }
                return b;
            }
            
            public void setCaptioningEnabled(final boolean b) throws RemoteException {
                this.postToHandler(29, b);
            }
            
            public void setRepeatMode(final int n) throws RemoteException {
                this.postToHandler(23, n);
            }
            
            public void setShuffleMode(final int n) throws RemoteException {
                this.postToHandler(30, n);
            }
            
            public void setShuffleModeEnabledRemoved(final boolean b) throws RemoteException {
            }
            
            public void setVolumeTo(final int n, final int n2, final String s) {
                this.this$0.setVolumeTo(n, n2);
            }
            
            public void skipToQueueItem(final long l) {
                this.postToHandler(11, l);
            }
            
            public void stop() throws RemoteException {
                this.postToHandler(13);
            }
            
            public void unregisterCallbackListener(final IMediaControllerCallback mediaControllerCallback) {
                this.this$0.mControllerCallbacks.unregister((IInterface)mediaControllerCallback);
            }
        }
        
        class MessageHandler extends Handler
        {
            private static final int KEYCODE_MEDIA_PAUSE = 127;
            private static final int KEYCODE_MEDIA_PLAY = 126;
            private static final int MSG_ADD_QUEUE_ITEM = 25;
            private static final int MSG_ADD_QUEUE_ITEM_AT = 26;
            private static final int MSG_ADJUST_VOLUME = 2;
            private static final int MSG_COMMAND = 1;
            private static final int MSG_CUSTOM_ACTION = 20;
            private static final int MSG_FAST_FORWARD = 16;
            private static final int MSG_MEDIA_BUTTON = 21;
            private static final int MSG_NEXT = 14;
            private static final int MSG_PAUSE = 12;
            private static final int MSG_PLAY = 7;
            private static final int MSG_PLAY_MEDIA_ID = 8;
            private static final int MSG_PLAY_SEARCH = 9;
            private static final int MSG_PLAY_URI = 10;
            private static final int MSG_PREPARE = 3;
            private static final int MSG_PREPARE_MEDIA_ID = 4;
            private static final int MSG_PREPARE_SEARCH = 5;
            private static final int MSG_PREPARE_URI = 6;
            private static final int MSG_PREVIOUS = 15;
            private static final int MSG_RATE = 19;
            private static final int MSG_RATE_EXTRA = 31;
            private static final int MSG_REMOVE_QUEUE_ITEM = 27;
            private static final int MSG_REMOVE_QUEUE_ITEM_AT = 28;
            private static final int MSG_REWIND = 17;
            private static final int MSG_SEEK_TO = 18;
            private static final int MSG_SET_CAPTIONING_ENABLED = 29;
            private static final int MSG_SET_REPEAT_MODE = 23;
            private static final int MSG_SET_SHUFFLE_MODE = 30;
            private static final int MSG_SET_VOLUME = 22;
            private static final int MSG_SKIP_TO_ITEM = 11;
            private static final int MSG_STOP = 13;
            final MediaSessionImplBase this$0;
            
            public MessageHandler(final MediaSessionImplBase this$0, final Looper looper) {
                this.this$0 = this$0;
                super(looper);
            }
            
            private void onMediaButtonEvent(final KeyEvent keyEvent, final Callback callback) {
                if (keyEvent != null && keyEvent.getAction() == 0) {
                    long actions;
                    if (this.this$0.mState == null) {
                        actions = 0L;
                    }
                    else {
                        actions = this.this$0.mState.getActions();
                    }
                    final int keyCode = keyEvent.getKeyCode();
                    if (keyCode != 79) {
                        switch (keyCode) {
                            default: {
                                switch (keyCode) {
                                    default: {
                                        return;
                                    }
                                    case 127: {
                                        if ((0x2L & actions) != 0x0L) {
                                            callback.onPause();
                                        }
                                        return;
                                    }
                                    case 126: {
                                        if ((0x4L & actions) != 0x0L) {
                                            callback.onPlay();
                                        }
                                        return;
                                    }
                                }
                                break;
                            }
                            case 90: {
                                if ((0x40L & actions) != 0x0L) {
                                    callback.onFastForward();
                                }
                                return;
                            }
                            case 89: {
                                if ((0x8L & actions) != 0x0L) {
                                    callback.onRewind();
                                }
                                return;
                            }
                            case 88: {
                                if ((0x10L & actions) != 0x0L) {
                                    callback.onSkipToPrevious();
                                }
                                return;
                            }
                            case 87: {
                                if ((0x20L & actions) != 0x0L) {
                                    callback.onSkipToNext();
                                }
                                return;
                            }
                            case 86: {
                                if ((0x1L & actions) != 0x0L) {
                                    callback.onStop();
                                }
                                return;
                            }
                            case 85: {
                                break;
                            }
                        }
                    }
                    Log.w("MediaSessionCompat", "KEYCODE_MEDIA_PLAY_PAUSE and KEYCODE_HEADSETHOOK are handled already");
                }
            }
            
            public void handleMessage(final Message message) {
                final Callback mCallback = this.this$0.mCallback;
                if (mCallback == null) {
                    return;
                }
                final Bundle data = message.getData();
                MediaSessionCompat.ensureClassLoader(data);
                this.this$0.setCurrentControllerInfo(new MediaSessionManager.RemoteUserInfo(data.getString("data_calling_pkg"), data.getInt("data_calling_pid"), data.getInt("data_calling_uid")));
                final Bundle bundle = data.getBundle("data_extras");
                MediaSessionCompat.ensureClassLoader(bundle);
                try {
                    switch (message.what) {
                        case 31: {
                            mCallback.onSetRating((RatingCompat)message.obj, bundle);
                            break;
                        }
                        case 30: {
                            mCallback.onSetShuffleMode(message.arg1);
                            break;
                        }
                        case 29: {
                            mCallback.onSetCaptioningEnabled((boolean)message.obj);
                            break;
                        }
                        case 28: {
                            if (this.this$0.mQueue == null) {
                                break;
                            }
                            QueueItem queueItem;
                            if (message.arg1 >= 0 && message.arg1 < this.this$0.mQueue.size()) {
                                queueItem = this.this$0.mQueue.get(message.arg1);
                            }
                            else {
                                queueItem = null;
                            }
                            if (queueItem != null) {
                                mCallback.onRemoveQueueItem(queueItem.getDescription());
                                break;
                            }
                            break;
                        }
                        case 27: {
                            mCallback.onRemoveQueueItem((MediaDescriptionCompat)message.obj);
                            break;
                        }
                        case 26: {
                            mCallback.onAddQueueItem((MediaDescriptionCompat)message.obj, message.arg1);
                            break;
                        }
                        case 25: {
                            mCallback.onAddQueueItem((MediaDescriptionCompat)message.obj);
                            break;
                        }
                        case 23: {
                            mCallback.onSetRepeatMode(message.arg1);
                            break;
                        }
                        case 22: {
                            this.this$0.setVolumeTo(message.arg1, 0);
                            break;
                        }
                        case 21: {
                            final KeyEvent keyEvent = (KeyEvent)message.obj;
                            final Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                            intent.putExtra("android.intent.extra.KEY_EVENT", (Parcelable)keyEvent);
                            if (!mCallback.onMediaButtonEvent(intent)) {
                                this.onMediaButtonEvent(keyEvent, mCallback);
                                break;
                            }
                            break;
                        }
                        case 20: {
                            mCallback.onCustomAction((String)message.obj, bundle);
                            break;
                        }
                        case 19: {
                            mCallback.onSetRating((RatingCompat)message.obj);
                            break;
                        }
                        case 18: {
                            mCallback.onSeekTo((long)message.obj);
                            break;
                        }
                        case 17: {
                            mCallback.onRewind();
                            break;
                        }
                        case 16: {
                            mCallback.onFastForward();
                            break;
                        }
                        case 15: {
                            mCallback.onSkipToPrevious();
                            break;
                        }
                        case 14: {
                            mCallback.onSkipToNext();
                            break;
                        }
                        case 13: {
                            mCallback.onStop();
                            break;
                        }
                        case 12: {
                            mCallback.onPause();
                            break;
                        }
                        case 11: {
                            mCallback.onSkipToQueueItem((long)message.obj);
                            break;
                        }
                        case 10: {
                            mCallback.onPlayFromUri((Uri)message.obj, bundle);
                            break;
                        }
                        case 9: {
                            mCallback.onPlayFromSearch((String)message.obj, bundle);
                            break;
                        }
                        case 8: {
                            mCallback.onPlayFromMediaId((String)message.obj, bundle);
                            break;
                        }
                        case 7: {
                            mCallback.onPlay();
                            break;
                        }
                        case 6: {
                            mCallback.onPrepareFromUri((Uri)message.obj, bundle);
                            break;
                        }
                        case 5: {
                            mCallback.onPrepareFromSearch((String)message.obj, bundle);
                            break;
                        }
                        case 4: {
                            mCallback.onPrepareFromMediaId((String)message.obj, bundle);
                            break;
                        }
                        case 3: {
                            mCallback.onPrepare();
                            break;
                        }
                        case 2: {
                            this.this$0.adjustVolume(message.arg1, 0);
                            break;
                        }
                        case 1: {
                            final Command command = (Command)message.obj;
                            mCallback.onCommand(command.command, command.extras, command.stub);
                            break;
                        }
                    }
                }
                finally {
                    this.this$0.setCurrentControllerInfo(null);
                }
            }
        }
    }
    
    @RequiresApi(19)
    static class MediaSessionImplApi19 extends MediaSessionImplApi18
    {
        MediaSessionImplApi19(final Context context, final String s, final ComponentName componentName, final PendingIntent pendingIntent) {
            super(context, s, componentName, pendingIntent);
        }
        
        @Override
        RemoteControlClient$MetadataEditor buildRccMetadata(final Bundle bundle) {
            final RemoteControlClient$MetadataEditor buildRccMetadata = super.buildRccMetadata(bundle);
            long actions;
            if (this.mState == null) {
                actions = 0L;
            }
            else {
                actions = this.mState.getActions();
            }
            if ((actions & 0x80L) != 0x0L) {
                buildRccMetadata.addEditableKey(268435457);
            }
            if (bundle == null) {
                return buildRccMetadata;
            }
            if (bundle.containsKey("android.media.metadata.YEAR")) {
                buildRccMetadata.putLong(8, bundle.getLong("android.media.metadata.YEAR"));
            }
            if (bundle.containsKey("android.media.metadata.RATING")) {
                ((MediaMetadataEditor)buildRccMetadata).putObject(101, (Object)bundle.getParcelable("android.media.metadata.RATING"));
            }
            if (bundle.containsKey("android.media.metadata.USER_RATING")) {
                ((MediaMetadataEditor)buildRccMetadata).putObject(268435457, (Object)bundle.getParcelable("android.media.metadata.USER_RATING"));
            }
            return buildRccMetadata;
        }
        
        @Override
        int getRccTransportControlFlagsFromActions(final long n) {
            int rccTransportControlFlagsFromActions = super.getRccTransportControlFlagsFromActions(n);
            if ((n & 0x80L) != 0x0L) {
                rccTransportControlFlagsFromActions |= 0x200;
            }
            return rccTransportControlFlagsFromActions;
        }
        
        @Override
        public void setCallback(final Callback callback, final Handler handler) {
            super.setCallback(callback, handler);
            if (callback == null) {
                this.mRcc.setMetadataUpdateListener((RemoteControlClient$OnMetadataUpdateListener)null);
            }
            else {
                this.mRcc.setMetadataUpdateListener((RemoteControlClient$OnMetadataUpdateListener)new RemoteControlClient$OnMetadataUpdateListener(this) {
                    final MediaSessionImplApi19 this$0;
                    
                    public void onMetadataUpdate(final int n, final Object o) {
                        if (n == 268435457 && o instanceof Rating) {
                            ((MediaSessionImplBase)this.this$0).postToHandler(19, -1, -1, RatingCompat.fromRating(o), null);
                        }
                    }
                });
            }
        }
    }
    
    @RequiresApi(21)
    static class MediaSessionImplApi21 implements MediaSessionImpl
    {
        boolean mCaptioningEnabled;
        boolean mDestroyed;
        final RemoteCallbackList<IMediaControllerCallback> mExtraControllerCallbacks;
        MediaMetadataCompat mMetadata;
        PlaybackStateCompat mPlaybackState;
        List<QueueItem> mQueue;
        int mRatingType;
        int mRepeatMode;
        final Object mSessionObj;
        int mShuffleMode;
        final Token mToken;
        
        MediaSessionImplApi21(final Context context, final String s, final Bundle bundle) {
            this.mDestroyed = false;
            this.mExtraControllerCallbacks = (RemoteCallbackList<IMediaControllerCallback>)new RemoteCallbackList();
            this.mSessionObj = MediaSessionCompatApi21.createSession(context, s);
            this.mToken = new Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj), new ExtraSession(), bundle);
        }
        
        MediaSessionImplApi21(final Object o) {
            this.mDestroyed = false;
            this.mExtraControllerCallbacks = (RemoteCallbackList<IMediaControllerCallback>)new RemoteCallbackList();
            this.mSessionObj = MediaSessionCompatApi21.verifySession(o);
            this.mToken = new Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj), new ExtraSession());
        }
        
        @Override
        public String getCallingPackage() {
            if (Build$VERSION.SDK_INT < 24) {
                return null;
            }
            return MediaSessionCompatApi24.getCallingPackage(this.mSessionObj);
        }
        
        @Override
        public MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
            return null;
        }
        
        @Override
        public Object getMediaSession() {
            return this.mSessionObj;
        }
        
        @Override
        public PlaybackStateCompat getPlaybackState() {
            return this.mPlaybackState;
        }
        
        @Override
        public Object getRemoteControlClient() {
            return null;
        }
        
        @Override
        public Token getSessionToken() {
            return this.mToken;
        }
        
        @Override
        public boolean isActive() {
            return MediaSessionCompatApi21.isActive(this.mSessionObj);
        }
        
        @Override
        public void release() {
            this.mDestroyed = true;
            MediaSessionCompatApi21.release(this.mSessionObj);
        }
        
        @Override
        public void sendSessionEvent(final String s, final Bundle bundle) {
        Label_0018_Outer:
            while (true) {
                if (Build$VERSION.SDK_INT >= 23) {
                    break Label_0057;
                }
                int n = this.mExtraControllerCallbacks.beginBroadcast() - 1;
            Label_0044_Outer:
                while (true) {
                    Label_0050: {
                        if (n < 0) {
                            break Label_0050;
                        }
                        final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(n);
                        while (true) {
                            try {
                                mediaControllerCallback.onEvent(s, bundle);
                                --n;
                                continue Label_0044_Outer;
                                MediaSessionCompatApi21.sendSessionEvent(this.mSessionObj, s, bundle);
                                return;
                                this.mExtraControllerCallbacks.finishBroadcast();
                                continue Label_0018_Outer;
                            }
                            catch (final RemoteException ex) {
                                continue;
                            }
                            break;
                        }
                    }
                    break;
                }
                break;
            }
        }
        
        @Override
        public void setActive(final boolean b) {
            MediaSessionCompatApi21.setActive(this.mSessionObj, b);
        }
        
        @Override
        public void setCallback(final Callback callback, final Handler handler) {
            final Object mSessionObj = this.mSessionObj;
            Object mCallbackObj;
            if (callback == null) {
                mCallbackObj = null;
            }
            else {
                mCallbackObj = callback.mCallbackObj;
            }
            MediaSessionCompatApi21.setCallback(mSessionObj, mCallbackObj, handler);
            if (callback != null) {
                callback.setSessionImpl(this, handler);
            }
        }
        
        @Override
        public void setCaptioningEnabled(final boolean mCaptioningEnabled) {
            if (this.mCaptioningEnabled == mCaptioningEnabled) {
                return;
            }
            this.mCaptioningEnabled = mCaptioningEnabled;
            int n = this.mExtraControllerCallbacks.beginBroadcast() - 1;
        Label_0046_Outer:
            while (true) {
                Label_0052: {
                    if (n < 0) {
                        break Label_0052;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onCaptioningEnabledChanged(mCaptioningEnabled);
                            --n;
                            continue Label_0046_Outer;
                            this.mExtraControllerCallbacks.finishBroadcast();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
                break;
            }
        }
        
        @Override
        public void setCurrentControllerInfo(final MediaSessionManager.RemoteUserInfo remoteUserInfo) {
        }
        
        @Override
        public void setExtras(final Bundle bundle) {
            MediaSessionCompatApi21.setExtras(this.mSessionObj, bundle);
        }
        
        @Override
        public void setFlags(final int n) {
            MediaSessionCompatApi21.setFlags(this.mSessionObj, n);
        }
        
        @Override
        public void setMediaButtonReceiver(final PendingIntent pendingIntent) {
            MediaSessionCompatApi21.setMediaButtonReceiver(this.mSessionObj, pendingIntent);
        }
        
        @Override
        public void setMetadata(final MediaMetadataCompat mMetadata) {
            this.mMetadata = mMetadata;
            final Object mSessionObj = this.mSessionObj;
            Object mediaMetadata;
            if (mMetadata == null) {
                mediaMetadata = null;
            }
            else {
                mediaMetadata = mMetadata.getMediaMetadata();
            }
            MediaSessionCompatApi21.setMetadata(mSessionObj, mediaMetadata);
        }
        
        @Override
        public void setPlaybackState(PlaybackStateCompat playbackState) {
            this.mPlaybackState = playbackState;
            int n = this.mExtraControllerCallbacks.beginBroadcast() - 1;
        Label_0038_Outer:
            while (true) {
                Label_0044: {
                    if (n < 0) {
                        break Label_0044;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onPlaybackStateChanged(playbackState);
                            --n;
                            continue Label_0038_Outer;
                            this.mExtraControllerCallbacks.finishBroadcast();
                            final Object mSessionObj = this.mSessionObj;
                            iftrue(Label_0065:)(playbackState != null);
                            playbackState = null;
                            Label_0070: {
                                break Label_0070;
                                Label_0065: {
                                    playbackState = (PlaybackStateCompat)playbackState.getPlaybackState();
                                }
                            }
                            MediaSessionCompatApi21.setPlaybackState(mSessionObj, playbackState);
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        @Override
        public void setPlaybackToLocal(final int n) {
            MediaSessionCompatApi21.setPlaybackToLocal(this.mSessionObj, n);
        }
        
        @Override
        public void setPlaybackToRemote(final VolumeProviderCompat volumeProviderCompat) {
            MediaSessionCompatApi21.setPlaybackToRemote(this.mSessionObj, volumeProviderCompat.getVolumeProvider());
        }
        
        @Override
        public void setQueue(final List<QueueItem> mQueue) {
            this.mQueue = mQueue;
            ArrayList list2;
            if (mQueue != null) {
                final ArrayList list = new ArrayList();
                final Iterator<QueueItem> iterator = mQueue.iterator();
                while (true) {
                    list2 = list;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    list.add(((QueueItem)iterator.next()).getQueueItem());
                }
            }
            else {
                list2 = null;
            }
            MediaSessionCompatApi21.setQueue(this.mSessionObj, list2);
        }
        
        @Override
        public void setQueueTitle(final CharSequence charSequence) {
            MediaSessionCompatApi21.setQueueTitle(this.mSessionObj, charSequence);
        }
        
        @Override
        public void setRatingType(final int mRatingType) {
            if (Build$VERSION.SDK_INT < 22) {
                this.mRatingType = mRatingType;
            }
            else {
                MediaSessionCompatApi22.setRatingType(this.mSessionObj, mRatingType);
            }
        }
        
        @Override
        public void setRepeatMode(final int mRepeatMode) {
            if (this.mRepeatMode == mRepeatMode) {
                return;
            }
            this.mRepeatMode = mRepeatMode;
            int n = this.mExtraControllerCallbacks.beginBroadcast() - 1;
        Label_0046_Outer:
            while (true) {
                Label_0052: {
                    if (n < 0) {
                        break Label_0052;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onRepeatModeChanged(mRepeatMode);
                            --n;
                            continue Label_0046_Outer;
                            this.mExtraControllerCallbacks.finishBroadcast();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
                break;
            }
        }
        
        @Override
        public void setSessionActivity(final PendingIntent pendingIntent) {
            MediaSessionCompatApi21.setSessionActivity(this.mSessionObj, pendingIntent);
        }
        
        @Override
        public void setShuffleMode(final int mShuffleMode) {
            if (this.mShuffleMode == mShuffleMode) {
                return;
            }
            this.mShuffleMode = mShuffleMode;
            int n = this.mExtraControllerCallbacks.beginBroadcast() - 1;
        Label_0046_Outer:
            while (true) {
                Label_0052: {
                    if (n < 0) {
                        break Label_0052;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onShuffleModeChanged(mShuffleMode);
                            --n;
                            continue Label_0046_Outer;
                            this.mExtraControllerCallbacks.finishBroadcast();
                        }
                        catch (final RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
                break;
            }
        }
        
        class ExtraSession extends Stub
        {
            final MediaSessionImplApi21 this$0;
            
            ExtraSession(final MediaSessionImplApi21 this$0) {
                this.this$0 = this$0;
            }
            
            public void addQueueItem(final MediaDescriptionCompat mediaDescriptionCompat) {
                throw new AssertionError();
            }
            
            public void addQueueItemAt(final MediaDescriptionCompat mediaDescriptionCompat, final int n) {
                throw new AssertionError();
            }
            
            public void adjustVolume(final int n, final int n2, final String s) {
                throw new AssertionError();
            }
            
            public void fastForward() throws RemoteException {
                throw new AssertionError();
            }
            
            public Bundle getExtras() {
                throw new AssertionError();
            }
            
            public long getFlags() {
                throw new AssertionError();
            }
            
            public PendingIntent getLaunchPendingIntent() {
                throw new AssertionError();
            }
            
            public MediaMetadataCompat getMetadata() {
                throw new AssertionError();
            }
            
            public String getPackageName() {
                throw new AssertionError();
            }
            
            public PlaybackStateCompat getPlaybackState() {
                return MediaSessionCompat.getStateWithUpdatedPosition(this.this$0.mPlaybackState, this.this$0.mMetadata);
            }
            
            public List<QueueItem> getQueue() {
                return null;
            }
            
            public CharSequence getQueueTitle() {
                throw new AssertionError();
            }
            
            public int getRatingType() {
                return this.this$0.mRatingType;
            }
            
            public int getRepeatMode() {
                return this.this$0.mRepeatMode;
            }
            
            public int getShuffleMode() {
                return this.this$0.mShuffleMode;
            }
            
            public String getTag() {
                throw new AssertionError();
            }
            
            public ParcelableVolumeInfo getVolumeAttributes() {
                throw new AssertionError();
            }
            
            public boolean isCaptioningEnabled() {
                return this.this$0.mCaptioningEnabled;
            }
            
            public boolean isShuffleModeEnabledRemoved() {
                return false;
            }
            
            public boolean isTransportControlEnabled() {
                throw new AssertionError();
            }
            
            public void next() throws RemoteException {
                throw new AssertionError();
            }
            
            public void pause() throws RemoteException {
                throw new AssertionError();
            }
            
            public void play() throws RemoteException {
                throw new AssertionError();
            }
            
            public void playFromMediaId(final String s, final Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }
            
            public void playFromSearch(final String s, final Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }
            
            public void playFromUri(final Uri uri, final Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }
            
            public void prepare() throws RemoteException {
                throw new AssertionError();
            }
            
            public void prepareFromMediaId(final String s, final Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }
            
            public void prepareFromSearch(final String s, final Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }
            
            public void prepareFromUri(final Uri uri, final Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }
            
            public void previous() throws RemoteException {
                throw new AssertionError();
            }
            
            public void rate(final RatingCompat ratingCompat) throws RemoteException {
                throw new AssertionError();
            }
            
            public void rateWithExtras(final RatingCompat ratingCompat, final Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }
            
            public void registerCallbackListener(final IMediaControllerCallback mediaControllerCallback) {
                if (!this.this$0.mDestroyed) {
                    String callingPackage;
                    if ((callingPackage = this.this$0.getCallingPackage()) == null) {
                        callingPackage = "android.media.session.MediaController";
                    }
                    this.this$0.mExtraControllerCallbacks.register((IInterface)mediaControllerCallback, (Object)new MediaSessionManager.RemoteUserInfo(callingPackage, getCallingPid(), getCallingUid()));
                }
            }
            
            public void removeQueueItem(final MediaDescriptionCompat mediaDescriptionCompat) {
                throw new AssertionError();
            }
            
            public void removeQueueItemAt(final int n) {
                throw new AssertionError();
            }
            
            public void rewind() throws RemoteException {
                throw new AssertionError();
            }
            
            public void seekTo(final long n) throws RemoteException {
                throw new AssertionError();
            }
            
            public void sendCommand(final String s, final Bundle bundle, final ResultReceiverWrapper resultReceiverWrapper) {
                throw new AssertionError();
            }
            
            public void sendCustomAction(final String s, final Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }
            
            public boolean sendMediaButton(final KeyEvent keyEvent) {
                throw new AssertionError();
            }
            
            public void setCaptioningEnabled(final boolean b) throws RemoteException {
                throw new AssertionError();
            }
            
            public void setRepeatMode(final int n) throws RemoteException {
                throw new AssertionError();
            }
            
            public void setShuffleMode(final int n) throws RemoteException {
                throw new AssertionError();
            }
            
            public void setShuffleModeEnabledRemoved(final boolean b) throws RemoteException {
            }
            
            public void setVolumeTo(final int n, final int n2, final String s) {
                throw new AssertionError();
            }
            
            public void skipToQueueItem(final long n) {
                throw new AssertionError();
            }
            
            public void stop() throws RemoteException {
                throw new AssertionError();
            }
            
            public void unregisterCallbackListener(final IMediaControllerCallback mediaControllerCallback) {
                this.this$0.mExtraControllerCallbacks.unregister((IInterface)mediaControllerCallback);
            }
        }
    }
    
    @RequiresApi(28)
    static class MediaSessionImplApi28 extends MediaSessionImplApi21
    {
        MediaSessionImplApi28(final Context context, final String s, final Bundle bundle) {
            super(context, s, bundle);
        }
        
        MediaSessionImplApi28(final Object o) {
            super(o);
        }
        
        @NonNull
        @Override
        public final MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
            return new MediaSessionManager.RemoteUserInfo(((MediaSession)this.mSessionObj).getCurrentControllerInfo());
        }
        
        @Override
        public void setCurrentControllerInfo(final MediaSessionManager.RemoteUserInfo remoteUserInfo) {
        }
    }
    
    public interface OnActiveChangeListener
    {
        void onActiveChanged();
    }
    
    public static final class QueueItem implements Parcelable
    {
        public static final Parcelable$Creator<QueueItem> CREATOR;
        public static final int UNKNOWN_ID = -1;
        private final MediaDescriptionCompat mDescription;
        private final long mId;
        private Object mItem;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<QueueItem>() {
                public QueueItem createFromParcel(final Parcel parcel) {
                    return new QueueItem(parcel);
                }
                
                public QueueItem[] newArray(final int n) {
                    return new QueueItem[n];
                }
            };
        }
        
        QueueItem(final Parcel parcel) {
            this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
            this.mId = parcel.readLong();
        }
        
        public QueueItem(final MediaDescriptionCompat mediaDescriptionCompat, final long n) {
            this(null, mediaDescriptionCompat, n);
        }
        
        private QueueItem(final Object mItem, final MediaDescriptionCompat mDescription, final long mId) {
            if (mDescription == null) {
                throw new IllegalArgumentException("Description cannot be null.");
            }
            if (mId == -1L) {
                throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            }
            this.mDescription = mDescription;
            this.mId = mId;
            this.mItem = mItem;
        }
        
        public static QueueItem fromQueueItem(final Object o) {
            if (o != null && Build$VERSION.SDK_INT >= 21) {
                return new QueueItem(o, MediaDescriptionCompat.fromMediaDescription(MediaSessionCompatApi21.QueueItem.getDescription(o)), MediaSessionCompatApi21.QueueItem.getQueueId(o));
            }
            return null;
        }
        
        public static List<QueueItem> fromQueueItemList(final List<?> list) {
            if (list != null && Build$VERSION.SDK_INT >= 21) {
                final ArrayList list2 = new ArrayList();
                final Iterator<?> iterator = list.iterator();
                while (iterator.hasNext()) {
                    list2.add(fromQueueItem(iterator.next()));
                }
                return list2;
            }
            return null;
        }
        
        public int describeContents() {
            return 0;
        }
        
        public MediaDescriptionCompat getDescription() {
            return this.mDescription;
        }
        
        public long getQueueId() {
            return this.mId;
        }
        
        public Object getQueueItem() {
            if (this.mItem == null && Build$VERSION.SDK_INT >= 21) {
                return this.mItem = MediaSessionCompatApi21.QueueItem.createItem(this.mDescription.getMediaDescription(), this.mId);
            }
            return this.mItem;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("MediaSession.QueueItem {Description=");
            sb.append(this.mDescription);
            sb.append(", Id=");
            sb.append(this.mId);
            sb.append(" }");
            return sb.toString();
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            this.mDescription.writeToParcel(parcel, n);
            parcel.writeLong(this.mId);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static final class ResultReceiverWrapper implements Parcelable
    {
        public static final Parcelable$Creator<ResultReceiverWrapper> CREATOR;
        ResultReceiver mResultReceiver;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<ResultReceiverWrapper>() {
                public ResultReceiverWrapper createFromParcel(final Parcel parcel) {
                    return new ResultReceiverWrapper(parcel);
                }
                
                public ResultReceiverWrapper[] newArray(final int n) {
                    return new ResultReceiverWrapper[n];
                }
            };
        }
        
        ResultReceiverWrapper(final Parcel parcel) {
            this.mResultReceiver = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(parcel);
        }
        
        public ResultReceiverWrapper(final ResultReceiver mResultReceiver) {
            this.mResultReceiver = mResultReceiver;
        }
        
        public int describeContents() {
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            this.mResultReceiver.writeToParcel(parcel, n);
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface SessionFlags {
    }
    
    public static final class Token implements Parcelable
    {
        public static final Parcelable$Creator<Token> CREATOR;
        private IMediaSession mExtraBinder;
        private final Object mInner;
        private Bundle mSessionToken2Bundle;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<Token>() {
                public Token createFromParcel(final Parcel parcel) {
                    Object o;
                    if (Build$VERSION.SDK_INT >= 21) {
                        o = parcel.readParcelable((ClassLoader)null);
                    }
                    else {
                        o = parcel.readStrongBinder();
                    }
                    return new Token(o);
                }
                
                public Token[] newArray(final int n) {
                    return new Token[n];
                }
            };
        }
        
        Token(final Object o) {
            this(o, null, null);
        }
        
        Token(final Object o, final IMediaSession mediaSession) {
            this(o, mediaSession, null);
        }
        
        Token(final Object mInner, final IMediaSession mExtraBinder, final Bundle mSessionToken2Bundle) {
            this.mInner = mInner;
            this.mExtraBinder = mExtraBinder;
            this.mSessionToken2Bundle = mSessionToken2Bundle;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public static Token fromBundle(final Bundle bundle) {
            final Token token = null;
            if (bundle == null) {
                return null;
            }
            final IMediaSession interface1 = IMediaSession.Stub.asInterface(BundleCompat.getBinder(bundle, "android.support.v4.media.session.EXTRA_BINDER"));
            final Bundle bundle2 = bundle.getBundle("android.support.v4.media.session.SESSION_TOKEN2_BUNDLE");
            final Token token2 = (Token)bundle.getParcelable("android.support.v4.media.session.TOKEN");
            Token token3;
            if (token2 == null) {
                token3 = token;
            }
            else {
                token3 = new Token(token2.mInner, interface1, bundle2);
            }
            return token3;
        }
        
        public static Token fromToken(final Object o) {
            return fromToken(o, null);
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public static Token fromToken(final Object o, final IMediaSession mediaSession) {
            if (o != null && Build$VERSION.SDK_INT >= 21) {
                return new Token(MediaSessionCompatApi21.verifyToken(o), mediaSession);
            }
            return null;
        }
        
        public int describeContents() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (!(o instanceof Token)) {
                return false;
            }
            final Token token = (Token)o;
            if (this.mInner == null) {
                if (token.mInner != null) {
                    b = false;
                }
                return b;
            }
            return token.mInner != null && this.mInner.equals(token.mInner);
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public IMediaSession getExtraBinder() {
            return this.mExtraBinder;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public Bundle getSessionToken2Bundle() {
            return this.mSessionToken2Bundle;
        }
        
        public Object getToken() {
            return this.mInner;
        }
        
        @Override
        public int hashCode() {
            if (this.mInner == null) {
                return 0;
            }
            return this.mInner.hashCode();
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public void setExtraBinder(final IMediaSession mExtraBinder) {
            this.mExtraBinder = mExtraBinder;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public void setSessionToken2Bundle(final Bundle mSessionToken2Bundle) {
            this.mSessionToken2Bundle = mSessionToken2Bundle;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public Bundle toBundle() {
            final Bundle bundle = new Bundle();
            bundle.putParcelable("android.support.v4.media.session.TOKEN", (Parcelable)this);
            if (this.mExtraBinder != null) {
                BundleCompat.putBinder(bundle, "android.support.v4.media.session.EXTRA_BINDER", this.mExtraBinder.asBinder());
            }
            if (this.mSessionToken2Bundle != null) {
                bundle.putBundle("android.support.v4.media.session.SESSION_TOKEN2_BUNDLE", this.mSessionToken2Bundle);
            }
            return bundle;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            if (Build$VERSION.SDK_INT >= 21) {
                parcel.writeParcelable((Parcelable)this.mInner, n);
            }
            else {
                parcel.writeStrongBinder((IBinder)this.mInner);
            }
        }
    }
}
