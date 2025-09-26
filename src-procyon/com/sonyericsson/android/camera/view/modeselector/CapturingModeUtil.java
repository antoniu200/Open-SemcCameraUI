// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import java.util.Iterator;
import java.util.Collection;
import android.content.ComponentName;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.Intent;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class CapturingModeUtil
{
    public static final String ACTION_GET_MORE_APPLICATION = "com.sonymobile.cameracommon.action.GET_MORE_APPLICATION";
    public static final String ACTION_REGISTER_MODE = "com.sonymobile.cameracommon.action.REGISTER_MODE";
    public static final String ACTION_REQUEST_REGISTER = "com.sonymobile.camera.addon.action.REQUEST_REGISTER";
    private static final String ART_FILTER_PACKAGE_NAME = "com.sonyericsson.android.addoncamera.artfilter";
    private static final String AR_EFFECT_MODE_NAME = "AR Effect";
    private static final String AR_EFFECT_PACKAGE_NAME = "com.sonymobile.androidapp.cameraaddon.areffect";
    private static final String CAMERA3D_PACKAGE_NAME = "com.sonyericsson.android.camera3d";
    public static final String CAMERA_ACTIVITY = "com.sonyericsson.android.camera.CameraActivity";
    public static final String CAMERA_ADDON_PERMISSION_NAME = "com.sonymobile.permission.CAMERA_ADDON";
    public static final String CAMERA_COMMON_PACKAGE_NAME = "com.sonymobile.cameracommon";
    public static final String CAMERA_UI_PACKAGE_NAME = "com.sonyericsson.android.camera";
    private static final String[][] DEFAULT_SORT_ORDER_LIST;
    public static final String EXTRA_CALLING_CLASS_NAME = "extra-calling-class-name";
    public static final String EXTRA_CALLING_PACKAGE_NAME = "extra-calling-package-name";
    public static final String EXTRA_CAPTURING_MODE = "com.sonymobile.camera.addon.intent.extra.CAPTURING_MODE";
    private static final String FACE_IN_PACKAGE_NAME = "com.sonymobile.android.addoncamera.dual";
    private static final String FAST_CAPTURING_ACTIVITY = "com.sonyericsson.android.camera.fastcapturing.FastCapturingActivity";
    private static final int INVALID = -1;
    public static final List<String> MODE_WHITE_LIST;
    private static final String[][] ONESHOT_DEFAULT_SORT_ORDER_LIST;
    private static final String SOUND_PHOTO_PACKAGE_NAME = "com.sonymobile.android.addoncamera.soundphoto";
    private static final String STICKER_CREATOR_PACKAGE_NAME = "com.sonymobile.androidapp.cameraaddon.stickercreator";
    private static final String STYLE_PORTRAIT_PACKAGE_NAME = "com.sonymobile.android.addoncamera.styleportrait";
    private static final String SUPER_VIDEO_PACKAGE_NAME = "com.sonymobile.android.addoncamera.supervideo";
    static final String TAG = "CapturingModeUtil";
    private static final String TIME_SHIFT_PACKAGE_NAME = "com.sonymobile.android.addoncamera.timeshift";
    
    static {
        MODE_WHITE_LIST = new ArrayList<String>() {
            {
                this.add("capturing_mode_soundphoto");
                this.add("capturing_mode_single_effect");
                this.add("capturing_mode_sweep_panorama");
                this.add("GOOGLE_LENS");
                this.add("PORTRAIT_SELFIE");
                this.add("DUAL_BACKGROUND_DEFOCUS");
                this.add("DUAL_MONOCHROME");
            }
        };
        DEFAULT_SORT_ORDER_LIST = new String[][] { { "com.sonyericsson.android.camera", "SCENE_RECOGNITION" }, { "com.sonyericsson.android.camera", "NORMAL" }, { "com.sonymobile.androidapp.cameraaddon.areffect", "AR Effect" }, { "com.sonyericsson.android.addoncamera.artfilter", "capturing_mode_single_effect" }, { "com.sonyericsson.android.camera3d", "capturing_mode_sweep_panorama" }, { "com.sonymobile.android.addoncamera.soundphoto", "capturing_mode_soundphoto" }, { "com.sonymobile.androidapp.cameraaddon.stickercreator", "sticker_creator" }, { "com.sonymobile.android.addoncamera.supervideo", "HIGH_FRAME_RATE" }, { "com.sonymobile.android.addoncamera.styleportrait", "capturing_mode_self_portrait" }, { "com.sonymobile.android.addoncamera.dual", "capturing_mode_dual" }, { "com.sonymobile.android.addoncamera.supervideo", "FOUR_K_UHD" } };
        ONESHOT_DEFAULT_SORT_ORDER_LIST = new String[][] { { "com.sonyericsson.android.camera", "SCENE_RECOGNITION" }, { "com.sonyericsson.android.camera", "NORMAL" }, { "com.sonyericsson.android.addoncamera.artfilter", "capture_mode_single_effect" } };
    }
    
    private CapturingModeUtil() {
    }
    
    public static String filteringPrevActivity(final String s) {
        if (s.equals("com.sonyericsson.android.camera.fastcapturing.FastCapturingActivity")) {
            return "com.sonyericsson.android.camera.CameraActivity";
        }
        return s;
    }
    
    public static String filteringPrevName(final String s) {
        if (s.equals("FAST_CAPTURING_CAMERA")) {
            return "SCENE_RECOGNITION";
        }
        return s;
    }
    
    private static int getIndexOf(final CapturingMode capturingMode, final String[][] array) {
        for (int i = 0; i < array.length; ++i) {
            if (capturingMode.is(array[i][0], array[i][1])) {
                return i;
            }
        }
        return -1;
    }
    
    private static int getSortOrder(final CapturingMode capturingMode, final String[][]... array) {
        final int length = array.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            final String[][] array2 = array[i];
            final int index = getIndexOf(capturingMode, array2);
            if (index >= 0) {
                return index + n;
            }
            n += array2.length;
            ++i;
        }
        return -1;
    }
    
    public static boolean hasDefaultSortOrder(final CapturingMode capturingMode) {
        boolean b = true;
        if (-1 == getSortOrder(capturingMode, new String[][][] { CapturingModeUtil.DEFAULT_SORT_ORDER_LIST })) {
            b = false;
        }
        return b;
    }
    
    public static boolean isActivityAvailable(final Context context, final Intent obj) {
        final ComponentName resolveActivity = obj.resolveActivity(context.getPackageManager());
        boolean b = false;
        if (resolveActivity != null) {
            b = true;
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("isActivityAvailable: ");
            sb.append(false);
            sb.append(" : ");
            sb.append(obj);
            CamLog.w(sb.toString());
        }
        return b;
    }
    
    public static void requestRegisterMode(final Context context) {
        startCameraCommonService(context, new Intent("com.sonymobile.cameracommon.action.REGISTER_MODE"));
    }
    
    private static <T extends CapturingMode> List<T> sort(final List<T> list, final String[][]... array) {
        final ArrayList list2 = new ArrayList();
        for (final String[][] array2 : array) {
            for (int j = 0; j < array2.length; ++j) {
                list2.add(null);
            }
        }
        final ArrayList<CapturingMode> c = new ArrayList<CapturingMode>();
        for (final CapturingMode capturingMode : list) {
            final int sortOrder = getSortOrder(capturingMode, array);
            if (sortOrder != -1) {
                list2.set(sortOrder, capturingMode);
            }
            else {
                c.add(capturingMode);
            }
        }
        final ArrayList list3 = new ArrayList();
        for (final CapturingMode e : list2) {
            if (e != null) {
                list3.add(e);
            }
        }
        list3.addAll(c);
        return list3;
    }
    
    public static <T extends CapturingMode> List<T> sortCapturingMode(final List<T> list) {
        return sort(list, new String[][][] { CapturingModeUtil.DEFAULT_SORT_ORDER_LIST });
    }
    
    public static <T extends CapturingMode> List<T> sortOneshotCapturingMode(final List<T> list) {
        return sort(list, new String[][][] { CapturingModeUtil.ONESHOT_DEFAULT_SORT_ORDER_LIST });
    }
    
    public static void startCameraCommonService(final Context context, final Intent intent) {
        intent.setPackage("com.sonymobile.cameracommon");
        intent.putExtra("extra-calling-package-name", context.getApplicationInfo().packageName);
        intent.putExtra("extra-calling-class-name", context.getClass().getName());
        context.startService(intent);
    }
    
    public interface CapturingMode
    {
        boolean is(final String p0, final String p1);
    }
}
