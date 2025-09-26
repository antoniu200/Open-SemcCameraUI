// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.media;

import android.support.v4.util.ObjectsCompat;
import android.media.session.MediaSessionManager$RemoteUserInfo;
import android.content.Context;
import android.media.session.MediaSessionManager;
import android.support.annotation.RequiresApi;

@RequiresApi(28)
class MediaSessionManagerImplApi28 extends MediaSessionManagerImplApi21
{
    android.media.session.MediaSessionManager mObject;
    
    MediaSessionManagerImplApi28(final Context context) {
        super(context);
        this.mObject = (android.media.session.MediaSessionManager)context.getSystemService("media_session");
    }
    
    @Override
    public boolean isTrustedForMediaControl(final RemoteUserInfoImpl remoteUserInfoImpl) {
        return remoteUserInfoImpl instanceof RemoteUserInfoImplApi28 && this.mObject.isTrustedForMediaControl(((RemoteUserInfoImplApi28)remoteUserInfoImpl).mObject);
    }
    
    static final class RemoteUserInfoImplApi28 implements RemoteUserInfoImpl
    {
        final MediaSessionManager$RemoteUserInfo mObject;
        
        RemoteUserInfoImplApi28(final MediaSessionManager$RemoteUserInfo mObject) {
            this.mObject = mObject;
        }
        
        RemoteUserInfoImplApi28(final String s, final int n, final int n2) {
            this.mObject = new MediaSessionManager$RemoteUserInfo(s, n, n2);
        }
        
        @Override
        public boolean equals(final Object o) {
            return this == o || (o instanceof RemoteUserInfoImplApi28 && this.mObject.equals((Object)((RemoteUserInfoImplApi28)o).mObject));
        }
        
        @Override
        public String getPackageName() {
            return this.mObject.getPackageName();
        }
        
        @Override
        public int getPid() {
            return this.mObject.getPid();
        }
        
        @Override
        public int getUid() {
            return this.mObject.getUid();
        }
        
        @Override
        public int hashCode() {
            return ObjectsCompat.hash(this.mObject);
        }
    }
}
