// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.media;

import android.support.annotation.NonNull;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class MediaSessionManagerImplApi21 extends MediaSessionManagerImplBase
{
    MediaSessionManagerImplApi21(final Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }
    
    private boolean hasMediaControlPermission(@NonNull final RemoteUserInfoImpl remoteUserInfoImpl) {
        return this.getContext().checkPermission("android.permission.MEDIA_CONTENT_CONTROL", remoteUserInfoImpl.getPid(), remoteUserInfoImpl.getUid()) == 0;
    }
    
    @Override
    public boolean isTrustedForMediaControl(@NonNull final RemoteUserInfoImpl remoteUserInfoImpl) {
        return this.hasMediaControlPermission(remoteUserInfoImpl) || super.isTrustedForMediaControl(remoteUserInfoImpl);
    }
}
