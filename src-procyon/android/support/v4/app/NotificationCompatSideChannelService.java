// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.os.RemoteException;
import android.os.Build$VERSION;
import android.os.IBinder;
import android.content.Intent;
import android.app.Notification;
import android.app.Service;

public abstract class NotificationCompatSideChannelService extends Service
{
    public abstract void cancel(final String p0, final int p1, final String p2);
    
    public abstract void cancelAll(final String p0);
    
    void checkPermission(final int i, final String s) {
        final String[] packagesForUid = this.getPackageManager().getPackagesForUid(i);
        for (int length = packagesForUid.length, j = 0; j < length; ++j) {
            if (packagesForUid[j].equals(s)) {
                return;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("NotificationSideChannelService: Uid ");
        sb.append(i);
        sb.append(" is not authorized for package ");
        sb.append(s);
        throw new SecurityException(sb.toString());
    }
    
    public abstract void notify(final String p0, final int p1, final String p2, final Notification p3);
    
    public IBinder onBind(final Intent intent) {
        if (!intent.getAction().equals("android.support.BIND_NOTIFICATION_SIDE_CHANNEL")) {
            return null;
        }
        if (Build$VERSION.SDK_INT > 19) {
            return null;
        }
        return (IBinder)new NotificationSideChannelStub();
    }
    
    private class NotificationSideChannelStub extends Stub
    {
        final NotificationCompatSideChannelService this$0;
        
        NotificationSideChannelStub(final NotificationCompatSideChannelService this$0) {
            this.this$0 = this$0;
        }
        
        public void cancel(final String s, final int n, final String s2) throws RemoteException {
            this.this$0.checkPermission(getCallingUid(), s);
            final long clearCallingIdentity = clearCallingIdentity();
            try {
                this.this$0.cancel(s, n, s2);
            }
            finally {
                restoreCallingIdentity(clearCallingIdentity);
            }
        }
        
        public void cancelAll(final String s) {
            this.this$0.checkPermission(getCallingUid(), s);
            final long clearCallingIdentity = clearCallingIdentity();
            try {
                this.this$0.cancelAll(s);
            }
            finally {
                restoreCallingIdentity(clearCallingIdentity);
            }
        }
        
        public void notify(final String s, final int n, final String s2, final Notification notification) throws RemoteException {
            this.this$0.checkPermission(getCallingUid(), s);
            final long clearCallingIdentity = clearCallingIdentity();
            try {
                this.this$0.notify(s, n, s2, notification);
            }
            finally {
                restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }
}
