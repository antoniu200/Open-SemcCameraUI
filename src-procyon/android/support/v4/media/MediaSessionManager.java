// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.media;

import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;
import android.media.session.MediaSessionManager$RemoteUserInfo;
import android.support.annotation.NonNull;
import android.os.Build$VERSION;
import android.content.Context;
import android.util.Log;

public final class MediaSessionManager
{
    static final boolean DEBUG;
    static final String TAG = "MediaSessionManager";
    private static final Object sLock;
    private static volatile MediaSessionManager sSessionManager;
    MediaSessionManagerImpl mImpl;
    
    static {
        DEBUG = Log.isLoggable("MediaSessionManager", 3);
        sLock = new Object();
    }
    
    private MediaSessionManager(final Context context) {
        if (Build$VERSION.SDK_INT >= 28) {
            this.mImpl = (MediaSessionManagerImpl)new MediaSessionManagerImplApi28(context);
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            this.mImpl = (MediaSessionManagerImpl)new MediaSessionManagerImplApi21(context);
        }
        else {
            this.mImpl = (MediaSessionManagerImpl)new MediaSessionManagerImplBase(context);
        }
    }
    
    @NonNull
    public static MediaSessionManager getSessionManager(@NonNull final Context context) {
        MediaSessionManager mediaSessionManager;
        if ((mediaSessionManager = MediaSessionManager.sSessionManager) == null) {
            synchronized (MediaSessionManager.sLock) {
                if ((mediaSessionManager = MediaSessionManager.sSessionManager) == null) {
                    mediaSessionManager = (MediaSessionManager.sSessionManager = new MediaSessionManager(context.getApplicationContext()));
                    mediaSessionManager = MediaSessionManager.sSessionManager;
                }
            }
        }
        return mediaSessionManager;
    }
    
    Context getContext() {
        return this.mImpl.getContext();
    }
    
    public boolean isTrustedForMediaControl(@NonNull final RemoteUserInfo remoteUserInfo) {
        if (remoteUserInfo == null) {
            throw new IllegalArgumentException("userInfo should not be null");
        }
        return this.mImpl.isTrustedForMediaControl(remoteUserInfo.mImpl);
    }
    
    interface MediaSessionManagerImpl
    {
        Context getContext();
        
        boolean isTrustedForMediaControl(final RemoteUserInfoImpl p0);
    }
    
    public static final class RemoteUserInfo
    {
        public static final String LEGACY_CONTROLLER = "android.media.session.MediaController";
        RemoteUserInfoImpl mImpl;
        
        @RequiresApi(28)
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public RemoteUserInfo(final MediaSessionManager$RemoteUserInfo mediaSessionManager$RemoteUserInfo) {
            this.mImpl = new MediaSessionManagerImplApi28.RemoteUserInfoImplApi28(mediaSessionManager$RemoteUserInfo);
        }
        
        public RemoteUserInfo(@NonNull final String s, final int n, final int n2) {
            if (Build$VERSION.SDK_INT >= 28) {
                this.mImpl = new MediaSessionManagerImplApi28.RemoteUserInfoImplApi28(s, n, n2);
            }
            else {
                this.mImpl = new MediaSessionManagerImplBase.RemoteUserInfoImplBase(s, n, n2);
            }
        }
        
        @Override
        public boolean equals(@Nullable final Object o) {
            return this == o || (o instanceof RemoteUserInfo && this.mImpl.equals(((RemoteUserInfo)o).mImpl));
        }
        
        @NonNull
        public String getPackageName() {
            return this.mImpl.getPackageName();
        }
        
        public int getPid() {
            return this.mImpl.getPid();
        }
        
        public int getUid() {
            return this.mImpl.getUid();
        }
        
        @Override
        public int hashCode() {
            return this.mImpl.hashCode();
        }
    }
    
    interface RemoteUserInfoImpl
    {
        String getPackageName();
        
        int getPid();
        
        int getUid();
    }
}
