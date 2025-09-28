package android.support.v4.media;

import android.content.Context;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.media.MediaSessionManagerImplApi28;
import android.support.v4.media.MediaSessionManagerImplBase;
import android.util.Log;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class MediaSessionManager {
    private static volatile MediaSessionManager sSessionManager;
    MediaSessionManagerImpl mImpl;
    static final String TAG = "MediaSessionManager";
    static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final Object sLock = new Object();

    interface MediaSessionManagerImpl {
        Context getContext();

        boolean isTrustedForMediaControl(RemoteUserInfoImpl remoteUserInfoImpl);
    }

    interface RemoteUserInfoImpl {
        String getPackageName();

        int getPid();

        int getUid();
    }

    @NonNull
    public static MediaSessionManager getSessionManager(@NonNull Context context) {
        MediaSessionManager mediaSessionManager = sSessionManager;
        if (mediaSessionManager == null) {
            synchronized (sLock) {
                mediaSessionManager = sSessionManager;
                if (mediaSessionManager == null) {
                    sSessionManager = new MediaSessionManager(context.getApplicationContext());
                    mediaSessionManager = sSessionManager;
                }
            }
        }
        return mediaSessionManager;
    }

    private MediaSessionManager(Context context) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.mImpl = new MediaSessionManagerImplApi28(context);
        } else if (Build.VERSION.SDK_INT >= 21) {
            this.mImpl = new MediaSessionManagerImplApi21(context);
        } else {
            this.mImpl = new MediaSessionManagerImplBase(context);
        }
    }

    public boolean isTrustedForMediaControl(@NonNull RemoteUserInfo remoteUserInfo) {
        if (remoteUserInfo == null) {
            throw new IllegalArgumentException("userInfo should not be null");
        }
        return this.mImpl.isTrustedForMediaControl(remoteUserInfo.mImpl);
    }

    Context getContext() {
        return this.mImpl.getContext();
    }

    public static final class RemoteUserInfo {
        public static final String LEGACY_CONTROLLER = "android.media.session.MediaController";
        RemoteUserInfoImpl mImpl;

        public RemoteUserInfo(@NonNull String str, int i, int i2) {
            if (Build.VERSION.SDK_INT >= 28) {
                this.mImpl = new MediaSessionManagerImplApi28.RemoteUserInfoImplApi28(str, i, i2);
            } else {
                this.mImpl = new MediaSessionManagerImplBase.RemoteUserInfoImplBase(str, i, i2);
            }
        }

        @RequiresApi(28)
        @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
        public RemoteUserInfo(MediaSessionManager.RemoteUserInfo remoteUserInfo) {
            this.mImpl = new MediaSessionManagerImplApi28.RemoteUserInfoImplApi28(remoteUserInfo);
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

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof RemoteUserInfo) {
                return this.mImpl.equals(((RemoteUserInfo) obj).mImpl);
            }
            return false;
        }

        public int hashCode() {
            return this.mImpl.hashCode();
        }
    }
}
