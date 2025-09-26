// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.media;

import java.util.Iterator;
import java.util.ArrayList;
import android.os.Parcel;
import android.support.v4.media.session.MediaSessionCompat;
import android.media.browse.MediaBrowser$MediaItem;
import java.util.List;
import android.service.media.MediaBrowserService;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.service.media.MediaBrowserService$Result;
import java.lang.reflect.Field;
import android.support.annotation.RequiresApi;

@RequiresApi(26)
class MediaBrowserServiceCompatApi26
{
    private static final String TAG = "MBSCompatApi26";
    static Field sResultFlags;
    
    static {
        try {
            (MediaBrowserServiceCompatApi26.sResultFlags = MediaBrowserService$Result.class.getDeclaredField("mFlags")).setAccessible(true);
        }
        catch (final NoSuchFieldException ex) {
            Log.w("MBSCompatApi26", (Throwable)ex);
        }
    }
    
    private MediaBrowserServiceCompatApi26() {
    }
    
    public static Object createService(final Context context, final ServiceCompatProxy serviceCompatProxy) {
        return new MediaBrowserServiceAdaptor(context, serviceCompatProxy);
    }
    
    public static Bundle getBrowserRootHints(final Object o) {
        return ((MediaBrowserService)o).getBrowserRootHints();
    }
    
    public static void notifyChildrenChanged(final Object o, final String s, final Bundle bundle) {
        ((MediaBrowserService)o).notifyChildrenChanged(s, bundle);
    }
    
    static class MediaBrowserServiceAdaptor extends MediaBrowserServiceCompatApi23.MediaBrowserServiceAdaptor
    {
        MediaBrowserServiceAdaptor(final Context context, final MediaBrowserServiceCompatApi26.ServiceCompatProxy serviceCompatProxy) {
            super(context, serviceCompatProxy);
        }
        
        public void onLoadChildren(final String s, final MediaBrowserService$Result<List<MediaBrowser$MediaItem>> mediaBrowserService$Result, final Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            ((MediaBrowserServiceCompatApi26.ServiceCompatProxy)this.mServiceProxy).onLoadChildren(s, new MediaBrowserServiceCompatApi26.ResultWrapper(mediaBrowserService$Result), bundle);
        }
    }
    
    static class ResultWrapper
    {
        MediaBrowserService$Result mResultObj;
        
        ResultWrapper(final MediaBrowserService$Result mResultObj) {
            this.mResultObj = mResultObj;
        }
        
        public void detach() {
            this.mResultObj.detach();
        }
        
        List<MediaBrowser$MediaItem> parcelListToItemList(final List<Parcel> list) {
            if (list == null) {
                return null;
            }
            final ArrayList list2 = new ArrayList();
            for (final Parcel parcel : list) {
                parcel.setDataPosition(0);
                list2.add(MediaBrowser$MediaItem.CREATOR.createFromParcel(parcel));
                parcel.recycle();
            }
            return list2;
        }
        
        public void sendResult(final List<Parcel> list, final int i) {
            try {
                MediaBrowserServiceCompatApi26.sResultFlags.setInt(this.mResultObj, i);
            }
            catch (final IllegalAccessException ex) {
                Log.w("MBSCompatApi26", (Throwable)ex);
            }
            this.mResultObj.sendResult((Object)this.parcelListToItemList(list));
        }
    }
    
    public interface ServiceCompatProxy extends MediaBrowserServiceCompatApi23.ServiceCompatProxy
    {
        void onLoadChildren(final String p0, final MediaBrowserServiceCompatApi26.ResultWrapper p1, final Bundle p2);
    }
}
