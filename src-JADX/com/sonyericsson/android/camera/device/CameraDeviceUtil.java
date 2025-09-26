package com.sonyericsson.android.camera.device;

import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class CameraDeviceUtil {
    public static final String TAG = "CameraDeviceUtil";
    private static final Comparator<int[]> mSupportedFpsComparator = new Comparator<int[]>() { // from class: com.sonyericsson.android.camera.device.CameraDeviceUtil.1
        @Override // java.util.Comparator
        public int compare(int[] iArr, int[] iArr2) {
            int i = iArr[1];
            int i2 = iArr[0];
            int i3 = iArr[1];
            int i4 = iArr[0];
            if (i > i3) {
                return 1;
            }
            if (i < i3) {
                return -1;
            }
            if (i2 < i4) {
                return 1;
            }
            return i2 > i4 ? -1 : 0;
        }
    };

    CameraDeviceUtil() {
    }

    static int[] computePreviewFpsRange(CameraInfo.CameraId cameraId, int i, List<int[]> list) {
        if (CamLog.VERBOSE) {
            CamLog.d("computePreviewFpsRange: " + i);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("computePreviewFpsRange: the number of supported values: " + list.size());
        }
        if (list.size() == 1) {
            int i2 = list.get(0)[1];
            int i3 = list.get(0)[0];
            if (CamLog.VERBOSE) {
                CamLog.d("Max fps: " + i2 + ", Min fps: " + i3);
            }
            if (i2 > 0) {
                return new int[]{i3, i2};
            }
            return new int[0];
        }
        int maxPreviewFps = PlatformCapability.getMaxPreviewFps(cameraId);
        if (i <= maxPreviewFps) {
            maxPreviewFps = i;
        } else if (CamLog.VERBOSE) {
            CamLog.d("targetFps over the all supported fps, shrink target to supported");
        }
        return getFpsRange(maxPreviewFps, list);
    }

    private static int[] getFpsRange(int i, List<int[]> list) {
        int i2;
        int i3;
        Collections.sort(list, mSupportedFpsComparator);
        Iterator<int[]> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                i2 = 0;
                i3 = 0;
                break;
            }
            int[] next = it.next();
            i3 = next[1];
            i2 = next[0];
            if (CamLog.VERBOSE) {
                CamLog.d("getFpsRange: Supported frame rate: " + i2 + ", " + i3);
                StringBuilder sb = new StringBuilder();
                sb.append("getFpsRange: current candidate max fps: ");
                sb.append(0);
                CamLog.d(sb.toString());
            }
            if (i <= i3) {
                if (i >= i2) {
                    break;
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("targetFps under the supported min frame");
                }
            } else if (CamLog.VERBOSE) {
                CamLog.d("targetFps over the supported max frame");
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Max: " + i3 + ", Min: " + i2);
        }
        if (i3 > 0) {
            return new int[]{i2, i3};
        }
        return new int[0];
    }
}
