// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.controller.album;

import android.content.ActivityNotFoundException;
import java.util.List;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import android.content.Intent;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.contentsview.PredictiveCaptureStoreInfo;
import android.net.Uri;
import android.app.Activity;

public final class AlbumLauncher
{
    public static final String EXTRA_BURST_BUCKETID = "burst_bucketId";
    public static final String TAG = "AlbumLauncher";
    
    public static void launchAlbum(final Activity activity, final Uri uri, final String s, final int n, final boolean b) {
        launchAlbum(activity, uri, s, n, b, true);
    }
    
    public static void launchAlbum(final Activity activity, final Uri uri, final String s, final int n, final boolean b, final boolean b2) {
        launchAlbum(activity, uri, s, n, b, b2, null);
    }
    
    public static void launchAlbum(final Activity activity, final Uri obj, final String str, final int i, final boolean b, final boolean b2, final PredictiveCaptureStoreInfo obj2) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("launchAlbum(");
            sb.append(obj);
            sb.append(", ");
            sb.append(str);
            sb.append(", ");
            sb.append(i);
            sb.append(", ");
            sb.append(b);
            sb.append(", ");
            sb.append(b2);
            sb.append(", ");
            sb.append(obj2);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        final Intent intent = new Intent("com.android.camera.action.REVIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        if (MimeType.fromText(str) == MimeType.MPO) {
            intent.setDataAndType(obj, MimeType.PHOTO.mText);
        }
        else {
            intent.setDataAndType(obj, str);
        }
        final CommonUtility.DefaultGallerySetting defaultGallery = CommonUtility.getDefaultGallery(activity.getApplicationContext(), obj, str);
        if (CamLog.DEBUG) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("launchAlbum defaultGallery ");
            sb2.append(defaultGallery);
            CamLog.d(sb2.toString());
        }
        Label_0325: {
            switch (AlbumLauncher$1.$SwitchMap$com$sonyericsson$cameracommon$utility$CommonUtility$DefaultGallerySetting[defaultGallery.ordinal()]) {
                default: {
                    break Label_0325;
                }
                case 1:
                case 2: {
                    if (b) {
                        intent.putExtra("burst_bucketId", i);
                    }
                    if (b2) {
                        intent.putExtra("com.sonyericsson.album.intent.extra.FAST_VIEW_MODE", true);
                    }
                    if (obj2 != null) {
                        intent.putExtra("com.sonymobile.album.intent.extra.PREDICTIVE_CAPTURE_COUNT", obj2.getCaptureNum());
                    }
                    break Label_0325;
                }
                case 3: {
                    if (CommonUtility.isActivityAvailable(activity.getApplicationContext(), intent)) {
                        activity.startActivityForResult(intent, 8);
                    }
                    else {
                        launchReviewApp(activity, obj, str, 8);
                    }
                    ResearchUtil.getInstance().setViewerLaunched();
                }
            }
        }
    }
    
    public static void launchAlbumSecure(final Activity activity, final List<Uri> obj, final List<String> obj2, final PredictiveCaptureStoreInfo obj3, final long[] obj4) {
        if (obj.size() != 0 && obj2.size() != 0) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("launchAlbumSecure(");
                sb.append(obj);
                sb.append(", ");
                sb.append(obj2);
                sb.append(", ");
                sb.append(obj4);
                sb.append(", ");
                sb.append(obj3);
                sb.append(")");
                CamLog.d(sb.toString());
            }
            final Intent intent = new Intent("com.android.camera.action.REVIEW");
            intent.setDataAndType((Uri)obj.get(0), (String)obj2.get(0));
            intent.addCategory("android.intent.category.DEFAULT");
            switch (AlbumLauncher$1.$SwitchMap$com$sonyericsson$cameracommon$utility$CommonUtility$DefaultGallerySetting[CommonUtility.getDefaultGallery(activity.getApplicationContext(), obj.get(0), obj2.get(0)).ordinal()]) {
                case 3: {
                    intent.putExtra("com.google.android.apps.photos.api.secure_mode_ids", obj4);
                    intent.putExtra("com.google.android.apps.photos.api.secure_mode", true);
                    break;
                }
                case 2: {
                    intent.putExtra("com.sonyericsson.album.intent.extra.FAST_VIEW_MODE", true);
                    if (obj3 != null) {
                        intent.putExtra("com.sonymobile.album.intent.extra.PREDICTIVE_CAPTURE_COUNT", obj3.getCaptureNum());
                    }
                    intent.putExtra("com.google.android.apps.photos.api.secure_mode_ids", obj4);
                    intent.putExtra("com.google.android.apps.photos.api.secure_mode", true);
                    break;
                }
                case 1: {
                    intent.putExtra("com.sonyericsson.album.intent.extra.FAST_VIEW_MODE", true);
                    if (obj3 != null) {
                        intent.putExtra("com.sonymobile.album.intent.extra.PREDICTIVE_CAPTURE_COUNT", obj3.getCaptureNum());
                        break;
                    }
                    break;
                }
            }
            if (CommonUtility.isActivityAvailable(activity.getApplicationContext(), intent)) {
                activity.startActivityForResult(intent, 9);
            }
            else {
                launchReviewApp(activity, obj.get(0), obj2.get(0), 9);
            }
            ResearchUtil.getInstance().setViewerLaunched();
            return;
        }
        throw new RuntimeException("album image uri is empty.");
    }
    
    private static void launchReviewApp(final Activity activity, final Uri uri, final String s, final int n) {
        final Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, s);
        try {
            activity.startActivityForResult(intent, n);
        }
        catch (final ActivityNotFoundException ex) {
            if (CamLog.DEBUG) {
                CamLog.d("There is no activity which accepts action:Intent.ACTION_VIEW");
            }
        }
    }
    
    private enum MimeType
    {
        private static final MimeType[] $VALUES;
        
        MP4("video/mp4"), 
        MPO("image/mpo"), 
        PHOTO("image/jpeg"), 
        THREEGPP("video/3gpp"), 
        UNKOWN("");
        
        final String mText;
        
        static {
            $VALUES = new MimeType[] { MimeType.PHOTO, MimeType.MPO, MimeType.MP4, MimeType.THREEGPP, MimeType.UNKOWN };
        }
        
        private MimeType(final String mText) {
            this.mText = mText;
        }
        
        static MimeType fromText(final String anObject) {
            for (final MimeType mimeType : values()) {
                if (mimeType.mText.equals(anObject)) {
                    return mimeType;
                }
            }
            return MimeType.UNKOWN;
        }
    }
}
