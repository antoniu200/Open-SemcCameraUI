// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util;

import java.util.List;
import android.os.Debug;
import android.os.Debug$MemoryInfo;
import android.app.ActivityManager$RunningAppProcessInfo;
import android.app.ActivityManager$MemoryInfo;
import android.app.ActivityManager;
import android.content.Context;

public class MemoryAnalyzer
{
    public static final String TAG = "MemoryAnalyzer";
    
    public static void logMemoryInfo(final Context context, final String s, final String s2) {
        synchronized (MemoryAnalyzer.class) {
            final ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
            final ActivityManager$MemoryInfo activityManager$MemoryInfo = new ActivityManager$MemoryInfo();
            activityManager.getMemoryInfo(activityManager$MemoryInfo);
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("System Memory Info : availMem = ");
                sb.append(activityManager$MemoryInfo.availMem);
                CamLog.d(sb.toString());
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("System Memory Info : threshold = ");
                sb2.append(activityManager$MemoryInfo.threshold);
                CamLog.d(sb2.toString());
            }
            final List runningAppProcesses = activityManager.getRunningAppProcesses();
            if (runningAppProcesses != null) {
                int i = 0;
                while (i < runningAppProcesses.size()) {
                    if (((ActivityManager$RunningAppProcessInfo)runningAppProcesses.get(i)).processName.equalsIgnoreCase("com.sonyericsson.android.camera")) {
                        if (CamLog.VERBOSE) {
                            CamLog.d("Info : Find Camera Process !!!");
                        }
                        if (CamLog.VERBOSE) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("Process Info : pid = ");
                            sb3.append(runningAppProcesses.get(i).pid);
                            CamLog.d(sb3.toString());
                        }
                        if (CamLog.VERBOSE) {
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append("Process Info : uid = ");
                            sb4.append(runningAppProcesses.get(i).uid);
                            CamLog.d(sb4.toString());
                        }
                        if (CamLog.VERBOSE) {
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append("Process Info : processName = ");
                            sb5.append(runningAppProcesses.get(i).processName);
                            CamLog.d(sb5.toString());
                            break;
                        }
                        break;
                    }
                    else {
                        ++i;
                    }
                }
                if (i == runningAppProcesses.size()) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("Info : Can not find Camera Process Info");
                    }
                }
                else {
                    final Debug$MemoryInfo debug$MemoryInfo = new Debug$MemoryInfo();
                    Debug.getMemoryInfo(debug$MemoryInfo);
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("Proc Memory Info : dalvikPrivateDirty   = ");
                        sb6.append(debug$MemoryInfo.dalvikPrivateDirty);
                        CamLog.d(sb6.toString());
                    }
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("Proc Memory Info : dalvikPss            = ");
                        sb7.append(debug$MemoryInfo.dalvikPss);
                        CamLog.d(sb7.toString());
                    }
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb8 = new StringBuilder();
                        sb8.append("Proc Memory Info : dalvikSharedDirty    = ");
                        sb8.append(debug$MemoryInfo.dalvikSharedDirty);
                        CamLog.d(sb8.toString());
                    }
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb9 = new StringBuilder();
                        sb9.append("Proc Memory Info : nativePrivateDirty   = ");
                        sb9.append(debug$MemoryInfo.nativePrivateDirty);
                        CamLog.d(sb9.toString());
                    }
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb10 = new StringBuilder();
                        sb10.append("Proc Memory Info : nativePss            = ");
                        sb10.append(debug$MemoryInfo.nativePss);
                        CamLog.d(sb10.toString());
                    }
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb11 = new StringBuilder();
                        sb11.append("Proc Memory Info : nativeSharedDirty    = ");
                        sb11.append(debug$MemoryInfo.nativeSharedDirty);
                        CamLog.d(sb11.toString());
                    }
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb12 = new StringBuilder();
                        sb12.append("Proc Memory Info : otherPrivateDirty    = ");
                        sb12.append(debug$MemoryInfo.otherPrivateDirty);
                        CamLog.d(sb12.toString());
                    }
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb13 = new StringBuilder();
                        sb13.append("Proc Memory Info : otherPss             = ");
                        sb13.append(debug$MemoryInfo.otherPss);
                        CamLog.d(sb13.toString());
                    }
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb14 = new StringBuilder();
                        sb14.append("Proc Memory Info : otherSharedDirty     = ");
                        sb14.append(debug$MemoryInfo.otherSharedDirty);
                        CamLog.d(sb14.toString());
                    }
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb15 = new StringBuilder();
                        sb15.append("Proc Memory Info : getTotalPrivateDirty = ");
                        sb15.append(debug$MemoryInfo.getTotalPrivateDirty());
                        CamLog.d(sb15.toString());
                    }
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb16 = new StringBuilder();
                        sb16.append("Proc Memory Info : getTotalPss          = ");
                        sb16.append(debug$MemoryInfo.getTotalPss());
                        CamLog.d(sb16.toString());
                    }
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb17 = new StringBuilder();
                        sb17.append("Proc Memory Info : getTotalSharedDirty  = ");
                        sb17.append(debug$MemoryInfo.getTotalSharedDirty());
                        CamLog.d(sb17.toString());
                    }
                }
            }
            else if (CamLog.VERBOSE) {
                CamLog.d("Cannot get Running App Processes.");
            }
        }
    }
}
