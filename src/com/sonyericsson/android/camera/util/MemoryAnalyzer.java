package com.sonyericsson.android.camera.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import com.sonyericsson.android.camera.view.modeselector.CameraCommonProviderConstants;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MemoryAnalyzer {
    public static final String TAG = "MemoryAnalyzer";

    public static synchronized void logMemoryInfo(Context context, String str, String str2) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(CameraCommonProviderConstants.CapturingModeColumns.ACTIVITY);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        if (CamLog.VERBOSE) {
            CamLog.d("System Memory Info : availMem = " + memoryInfo.availMem);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("System Memory Info : threshold = " + memoryInfo.threshold);
        }
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses != null) {
            int i = 0;
            while (true) {
                if (i >= runningAppProcesses.size()) {
                    break;
                }
                if (runningAppProcesses.get(i).processName.equalsIgnoreCase("com.sonyericsson.android.camera")) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("Info : Find Camera Process !!!");
                    }
                    if (CamLog.VERBOSE) {
                        CamLog.d("Process Info : pid = " + runningAppProcesses.get(i).pid);
                    }
                    if (CamLog.VERBOSE) {
                        CamLog.d("Process Info : uid = " + runningAppProcesses.get(i).uid);
                    }
                    if (CamLog.VERBOSE) {
                        CamLog.d("Process Info : processName = " + runningAppProcesses.get(i).processName);
                    }
                } else {
                    i++;
                }
            }
            if (i == runningAppProcesses.size()) {
                if (CamLog.VERBOSE) {
                    CamLog.d("Info : Can not find Camera Process Info");
                }
            } else {
                Debug.MemoryInfo memoryInfo2 = new Debug.MemoryInfo();
                Debug.getMemoryInfo(memoryInfo2);
                if (CamLog.VERBOSE) {
                    CamLog.d("Proc Memory Info : dalvikPrivateDirty   = " + memoryInfo2.dalvikPrivateDirty);
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("Proc Memory Info : dalvikPss            = " + memoryInfo2.dalvikPss);
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("Proc Memory Info : dalvikSharedDirty    = " + memoryInfo2.dalvikSharedDirty);
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("Proc Memory Info : nativePrivateDirty   = " + memoryInfo2.nativePrivateDirty);
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("Proc Memory Info : nativePss            = " + memoryInfo2.nativePss);
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("Proc Memory Info : nativeSharedDirty    = " + memoryInfo2.nativeSharedDirty);
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("Proc Memory Info : otherPrivateDirty    = " + memoryInfo2.otherPrivateDirty);
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("Proc Memory Info : otherPss             = " + memoryInfo2.otherPss);
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("Proc Memory Info : otherSharedDirty     = " + memoryInfo2.otherSharedDirty);
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("Proc Memory Info : getTotalPrivateDirty = " + memoryInfo2.getTotalPrivateDirty());
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("Proc Memory Info : getTotalPss          = " + memoryInfo2.getTotalPss());
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("Proc Memory Info : getTotalSharedDirty  = " + memoryInfo2.getTotalSharedDirty());
                }
            }
        } else if (CamLog.VERBOSE) {
            CamLog.d("Cannot get Running App Processes.");
        }
    }
}
