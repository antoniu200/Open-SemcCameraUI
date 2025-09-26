// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.media;

import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.content.ComponentName;
import android.provider.Settings$Secure;
import android.support.annotation.NonNull;
import android.content.Context;
import android.content.ContentResolver;

class MediaSessionManagerImplBase implements MediaSessionManagerImpl
{
    private static final boolean DEBUG;
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String PERMISSION_MEDIA_CONTENT_CONTROL = "android.permission.MEDIA_CONTENT_CONTROL";
    private static final String PERMISSION_STATUS_BAR_SERVICE = "android.permission.STATUS_BAR_SERVICE";
    private static final String TAG = "MediaSessionManager";
    ContentResolver mContentResolver;
    Context mContext;
    
    static {
        DEBUG = MediaSessionManager.DEBUG;
    }
    
    MediaSessionManagerImplBase(final Context mContext) {
        this.mContext = mContext;
        this.mContentResolver = this.mContext.getContentResolver();
    }
    
    private boolean isPermissionGranted(final RemoteUserInfoImpl remoteUserInfoImpl, final String s) {
        final int pid = remoteUserInfoImpl.getPid();
        final boolean b = false;
        boolean b2 = false;
        if (pid < 0) {
            if (this.mContext.getPackageManager().checkPermission(s, remoteUserInfoImpl.getPackageName()) == 0) {
                b2 = true;
            }
            return b2;
        }
        boolean b3 = b;
        if (this.mContext.checkPermission(s, remoteUserInfoImpl.getPid(), remoteUserInfoImpl.getUid()) == 0) {
            b3 = true;
        }
        return b3;
    }
    
    @Override
    public Context getContext() {
        return this.mContext;
    }
    
    boolean isEnabledNotificationListener(@NonNull final RemoteUserInfoImpl remoteUserInfoImpl) {
        final String string = Settings$Secure.getString(this.mContentResolver, "enabled_notification_listeners");
        if (string != null) {
            final String[] split = string.split(":");
            for (int i = 0; i < split.length; ++i) {
                final ComponentName unflattenFromString = ComponentName.unflattenFromString(split[i]);
                if (unflattenFromString != null && unflattenFromString.getPackageName().equals(remoteUserInfoImpl.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean isTrustedForMediaControl(@NonNull final RemoteUserInfoImpl remoteUserInfoImpl) {
        boolean b = false;
        try {
            if (this.mContext.getPackageManager().getApplicationInfo(remoteUserInfoImpl.getPackageName(), 0).uid != remoteUserInfoImpl.getUid()) {
                if (MediaSessionManagerImplBase.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Package name ");
                    sb.append(remoteUserInfoImpl.getPackageName());
                    sb.append(" doesn't match with the uid ");
                    sb.append(remoteUserInfoImpl.getUid());
                    Log.d("MediaSessionManager", sb.toString());
                }
                return false;
            }
            if (this.isPermissionGranted(remoteUserInfoImpl, "android.permission.STATUS_BAR_SERVICE") || this.isPermissionGranted(remoteUserInfoImpl, "android.permission.MEDIA_CONTENT_CONTROL") || remoteUserInfoImpl.getUid() == 1000 || this.isEnabledNotificationListener(remoteUserInfoImpl)) {
                b = true;
            }
            return b;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            if (MediaSessionManagerImplBase.DEBUG) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Package ");
                sb2.append(remoteUserInfoImpl.getPackageName());
                sb2.append(" doesn't exist");
                Log.d("MediaSessionManager", sb2.toString());
            }
            return false;
        }
    }
    
    static class RemoteUserInfoImplBase implements RemoteUserInfoImpl
    {
        private String mPackageName;
        private int mPid;
        private int mUid;
        
        RemoteUserInfoImplBase(final String mPackageName, final int mPid, final int mUid) {
            this.mPackageName = mPackageName;
            this.mPid = mPid;
            this.mUid = mUid;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (!(o instanceof RemoteUserInfoImplBase)) {
                return false;
            }
            final RemoteUserInfoImplBase remoteUserInfoImplBase = (RemoteUserInfoImplBase)o;
            if (!TextUtils.equals((CharSequence)this.mPackageName, (CharSequence)remoteUserInfoImplBase.mPackageName) || this.mPid != remoteUserInfoImplBase.mPid || this.mUid != remoteUserInfoImplBase.mUid) {
                b = false;
            }
            return b;
        }
        
        @Override
        public String getPackageName() {
            return this.mPackageName;
        }
        
        @Override
        public int getPid() {
            return this.mPid;
        }
        
        @Override
        public int getUid() {
            return this.mUid;
        }
        
        @Override
        public int hashCode() {
            return ObjectsCompat.hash(this.mPackageName, this.mPid, this.mUid);
        }
    }
}
