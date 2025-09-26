package com.sonyericsson.cameracommon.utility;

import android.graphics.Rect;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.focusview.FaceInformationList;
import com.sonyericsson.cameracommon.focusview.NamedFace;
import com.sonyericsson.cameracommon.focusview.TaggedRectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FaceDetectUtil {
    public static final String TAG = "FaceDetectUtil";

    private static class DistanceMapItem {
        private final int mArrayIndex;
        private final int mDistance;

        public DistanceMapItem(int i, int i2) {
            this.mArrayIndex = i;
            this.mDistance = i2;
        }

        public int getArrayIndex() {
            return this.mArrayIndex;
        }

        public int getDistance() {
            return this.mDistance;
        }
    }

    public static void dumpDistanceMapList(List<DistanceMapItem> list) {
        if (CamLog.VERBOSE) {
            CamLog.d("dumpDistanceMapList");
            for (DistanceMapItem distanceMapItem : list) {
                CamLog.d("item.arrayIndex = " + distanceMapItem.getArrayIndex() + " item.distance = " + distanceMapItem.getDistance());
            }
        }
    }

    private static class DistanceComparator implements Comparator<DistanceMapItem> {
        private DistanceComparator() {
        }

        @Override // java.util.Comparator
        public int compare(DistanceMapItem distanceMapItem, DistanceMapItem distanceMapItem2) {
            return distanceMapItem.getDistance() - distanceMapItem2.getDistance();
        }
    }

    private static List<DistanceMapItem> createSortedDistanceList(CameraParameters.FaceDetectionResult faceDetectionResult, Rect rect) {
        if (faceDetectionResult == null || faceDetectionResult.extFaceList == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        Iterator<CameraParameters.ExtFace> it = faceDetectionResult.extFaceList.iterator();
        while (it.hasNext()) {
            arrayList.add(new DistanceMapItem(i, computeClosesDistance(it.next().rect, rect)));
            i++;
        }
        Collections.sort(arrayList, new DistanceComparator());
        return arrayList;
    }

    private static int computeClosesDistance(Rect rect, Rect rect2) {
        if (CamLog.VERBOSE) {
            CamLog.d("computeClosesDistance: centerPosition = " + rect2 + " faceRect = " + rect);
        }
        Rect rectConvertFromActiveArrayToView = PositionConverter.getInstance().convertFromActiveArrayToView(rect);
        int iCenterX = rect2.centerX() - rectConvertFromActiveArrayToView.centerX();
        int iCenterY = rect2.centerY() - rectConvertFromActiveArrayToView.centerY();
        if (CamLog.VERBOSE) {
            CamLog.d("converted faceRect = " + rectConvertFromActiveArrayToView);
        }
        if (iCenterX < 0) {
            iCenterX *= -1;
        }
        if (iCenterY < 0) {
            iCenterY *= -1;
        }
        return iCenterX + iCenterY;
    }

    public static void logFaceDetectionResult(CameraParameters.FaceDetectionResult faceDetectionResult) {
        if (faceDetectionResult == null) {
            CamLog.v("onFaceDetection: result is null");
            return;
        }
        CamLog.v("onFaceDetection: Number of faces: " + faceDetectionResult.extFaceList.size());
        CamLog.v("onFaceDetection: Selected index : " + faceDetectionResult.indexOfSelectedFace);
        if (faceDetectionResult.extFaceList.isEmpty()) {
            return;
        }
        int i = 0;
        for (CameraParameters.ExtFace extFace : faceDetectionResult.extFaceList) {
            CamLog.v((((("ExtFACE[" + i + "]") + " face = " + extFace + " ") + " face.id = " + extFace.id + " ") + " face.rect = " + extFace.rect + " ") + " SmileScore = " + extFace.smileScore + " ");
            i++;
        }
    }

    public static TaggedRectangle overwriteTaggedRectangle(HashMap<String, TaggedRectangle> map, String str, FaceInformationList faceInformationList) {
        String key;
        TaggedRectangle value;
        Iterator<Map.Entry<String, TaggedRectangle>> it = map.entrySet().iterator();
        while (true) {
            key = null;
            if (!it.hasNext()) {
                value = null;
                break;
            }
            Map.Entry<String, TaggedRectangle> next = it.next();
            key = next.getKey();
            boolean z = false;
            Iterator<NamedFace> it2 = faceInformationList.getNamedFaceList().iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                if (key.equals(it2.next().mUuid)) {
                    z = true;
                    break;
                }
            }
            if (!z) {
                value = next.getValue();
                break;
            }
        }
        if (value != null) {
            map.remove(key);
            map.put(str, value);
        }
        return value;
    }

    public static FaceInformationList getFaceInformationList(CameraParameters.FaceDetectionResult faceDetectionResult, Rect rect, String str) {
        if (CamLog.VERBOSE) {
            CamLog.d("getFaceInformationSortList centerPosition = " + rect);
        }
        if (faceDetectionResult == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("getFaceInformationListt faceDetectResultList is null");
            }
            return null;
        }
        List<DistanceMapItem> listCreateSortedDistanceList = createSortedDistanceList(faceDetectionResult, rect);
        if (listCreateSortedDistanceList == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("createSortedDistanceList() return null");
            }
            return null;
        }
        FaceInformationList faceInformationList = new FaceInformationList();
        faceInformationList.setUserTouchUuid(str);
        Iterator<DistanceMapItem> it = listCreateSortedDistanceList.iterator();
        while (it.hasNext()) {
            CameraParameters.ExtFace extFace = faceDetectionResult.extFaceList.get(it.next().getArrayIndex());
            faceInformationList.addNamedFace(new NamedFace(null, String.valueOf(extFace.id), extFace.rect, extFace.smileScore));
        }
        logFaceDetectionResult(faceDetectionResult);
        return faceInformationList;
    }

    public static Boolean hasValidFaceId(CameraParameters.FaceDetectionResult faceDetectionResult) {
        Boolean bool = Boolean.TRUE;
        Iterator<CameraParameters.ExtFace> it = faceDetectionResult.extFaceList.iterator();
        while (it.hasNext()) {
            if (it.next().id == -1) {
                if (CamLog.VERBOSE) {
                    CamLog.d("FaceDetection ID is not supported.");
                }
                return Boolean.FALSE;
            }
        }
        return bool;
    }

    public static CameraParameters.FaceDetectionResult setUuidFaceDetectionResult(CameraParameters.FaceDetectionResult faceDetectionResult) {
        Iterator<CameraParameters.ExtFace> it = faceDetectionResult.extFaceList.iterator();
        int i = 0;
        while (it.hasNext()) {
            it.next().id = i;
            i++;
        }
        return faceDetectionResult;
    }

    public static boolean isValidFaceDetectionResult(CameraParameters.FaceDetectionResult faceDetectionResult) {
        return faceDetectionResult != null && faceDetectionResult.extFaceList.size() > faceDetectionResult.indexOfSelectedFace && faceDetectionResult.indexOfSelectedFace >= 0;
    }
}
