// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import java.util.Iterator;
import java.util.Collections;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.List;
import java.util.Comparator;

class CameraDeviceUtil
{
    public static final String TAG = "CameraDeviceUtil";
    private static final Comparator<int[]> mSupportedFpsComparator;
    
    static {
        mSupportedFpsComparator = new Comparator<int[]>() {
            @Override
            public int compare(final int[] array, final int[] array2) {
                final int n = array[1];
                final int n2 = array[0];
                final int n3 = array[1];
                final int n4 = array[0];
                if (n > n3) {
                    return 1;
                }
                if (n < n3) {
                    return -1;
                }
                if (n2 < n4) {
                    return 1;
                }
                if (n2 > n4) {
                    return -1;
                }
                return 0;
            }
        };
    }
    
    static int[] computePreviewFpsRange(final CameraInfo.CameraId cameraId, int n, final List<int[]> list) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("computePreviewFpsRange: ");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("computePreviewFpsRange: the number of supported values: ");
            sb2.append(list.size());
            CamLog.d(sb2.toString());
        }
        if (list.size() != 1) {
            final int maxPreviewFps = PlatformCapability.getMaxPreviewFps(cameraId);
            if (n > maxPreviewFps) {
                n = maxPreviewFps;
                if (CamLog.VERBOSE) {
                    CamLog.d("targetFps over the all supported fps, shrink target to supported");
                    n = maxPreviewFps;
                }
            }
            return getFpsRange(n, list);
        }
        final int i = ((int[])list.get(0))[1];
        n = ((int[])list.get(0))[0];
        if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Max fps: ");
            sb3.append(i);
            sb3.append(", Min fps: ");
            sb3.append(n);
            CamLog.d(sb3.toString());
        }
        if (i > 0) {
            return new int[] { n, i };
        }
        return new int[0];
    }
    
    private static int[] getFpsRange(final int n, final List<int[]> list) {
        Collections.sort((List<Object>)list, (Comparator<? super Object>)CameraDeviceUtil.mSupportedFpsComparator);
        while (true) {
            for (final int[] array : list) {
                final int n2 = array[1];
                final int i = array[0];
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("getFpsRange: Supported frame rate: ");
                    sb.append(i);
                    sb.append(", ");
                    sb.append(n2);
                    CamLog.d(sb.toString());
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("getFpsRange: current candidate max fps: ");
                    sb2.append(0);
                    CamLog.d(sb2.toString());
                }
                if (n > n2) {
                    if (!CamLog.VERBOSE) {
                        continue;
                    }
                    CamLog.d("targetFps over the supported max frame");
                }
                else {
                    final int j;
                    if (n < (j = i)) {
                        if (!CamLog.VERBOSE) {
                            continue;
                        }
                        CamLog.d("targetFps under the supported min frame");
                    }
                    else {
                        if (CamLog.VERBOSE) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("Max: ");
                            sb3.append(n2);
                            sb3.append(", Min: ");
                            sb3.append(j);
                            CamLog.d(sb3.toString());
                        }
                        if (n2 > 0) {
                            return new int[] { j, n2 };
                        }
                        return new int[0];
                    }
                }
            }
            int j = 0;
            final int n2 = 0;
            continue;
        }
    }
}
