// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import java.util.Map;
import com.sonyericsson.cameracommon.focusview.TaggedRectangle;
import java.util.HashMap;
import com.sonyericsson.cameracommon.focusview.NamedFace;
import com.sonyericsson.cameracommon.focusview.FaceInformationList;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.CamLog;
import android.graphics.Rect;

public class FaceDetectUtil
{
    public static final String TAG = "FaceDetectUtil";
    
    private static int computeClosesDistance(Rect convertFromActiveArrayToView, final Rect obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("computeClosesDistance: centerPosition = ");
            sb.append(obj);
            sb.append(" faceRect = ");
            sb.append(convertFromActiveArrayToView);
            CamLog.d(sb.toString());
        }
        convertFromActiveArrayToView = PositionConverter.getInstance().convertFromActiveArrayToView(convertFromActiveArrayToView);
        final int n = obj.centerX() - convertFromActiveArrayToView.centerX();
        final int n2 = obj.centerY() - convertFromActiveArrayToView.centerY();
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("converted faceRect = ");
            sb2.append(convertFromActiveArrayToView);
            CamLog.d(sb2.toString());
        }
        int n3;
        if ((n3 = n) < 0) {
            n3 = n * -1;
        }
        int n4;
        if ((n4 = n2) < 0) {
            n4 = n2 * -1;
        }
        return n3 + n4;
    }
    
    private static List<DistanceMapItem> createSortedDistanceList(final CameraParameters.FaceDetectionResult faceDetectionResult, final Rect rect) {
        if (faceDetectionResult == null) {
            return null;
        }
        if (faceDetectionResult.extFaceList == null) {
            return null;
        }
        final ArrayList list = new ArrayList();
        int n = 0;
        final Iterator<CameraParameters.ExtFace> iterator = faceDetectionResult.extFaceList.iterator();
        while (iterator.hasNext()) {
            list.add(new DistanceMapItem(n, computeClosesDistance(iterator.next().rect, rect)));
            ++n;
        }
        Collections.sort((List<Object>)list, (Comparator<? super Object>)new DistanceComparator());
        return list;
    }
    
    public static void dumpDistanceMapList(final List<DistanceMapItem> list) {
        if (CamLog.VERBOSE) {
            CamLog.d("dumpDistanceMapList");
            for (final DistanceMapItem distanceMapItem : list) {
                final StringBuilder sb = new StringBuilder();
                sb.append("item.arrayIndex = ");
                sb.append(distanceMapItem.getArrayIndex());
                sb.append(" item.distance = ");
                sb.append(distanceMapItem.getDistance());
                CamLog.d(sb.toString());
            }
        }
    }
    
    public static FaceInformationList getFaceInformationList(final CameraParameters.FaceDetectionResult faceDetectionResult, final Rect obj, final String userTouchUuid) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getFaceInformationSortList centerPosition = ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        if (faceDetectionResult == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("getFaceInformationListt faceDetectResultList is null");
            }
            return null;
        }
        final List<DistanceMapItem> sortedDistanceList = createSortedDistanceList(faceDetectionResult, obj);
        if (sortedDistanceList == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("createSortedDistanceList() return null");
            }
            return null;
        }
        final FaceInformationList list = new FaceInformationList();
        list.setUserTouchUuid(userTouchUuid);
        final Iterator<DistanceMapItem> iterator = sortedDistanceList.iterator();
        while (iterator.hasNext()) {
            final CameraParameters.ExtFace extFace = faceDetectionResult.extFaceList.get(iterator.next().getArrayIndex());
            list.addNamedFace(new NamedFace(null, String.valueOf(extFace.id), extFace.rect, extFace.smileScore));
        }
        logFaceDetectionResult(faceDetectionResult);
        return list;
    }
    
    public static Boolean hasValidFaceId(final CameraParameters.FaceDetectionResult faceDetectionResult) {
        final Boolean true = Boolean.TRUE;
        final Iterator<CameraParameters.ExtFace> iterator = faceDetectionResult.extFaceList.iterator();
        do {
            final Boolean false = true;
            if (iterator.hasNext()) {
                continue;
            }
            return false;
        } while (iterator.next().id != -1);
        if (CamLog.VERBOSE) {
            CamLog.d("FaceDetection ID is not supported.");
        }
        return Boolean.FALSE;
    }
    
    public static boolean isValidFaceDetectionResult(final CameraParameters.FaceDetectionResult faceDetectionResult) {
        return faceDetectionResult != null && faceDetectionResult.extFaceList.size() > faceDetectionResult.indexOfSelectedFace && faceDetectionResult.indexOfSelectedFace >= 0;
    }
    
    public static void logFaceDetectionResult(final CameraParameters.FaceDetectionResult faceDetectionResult) {
        if (faceDetectionResult == null) {
            CamLog.v("onFaceDetection: result is null");
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("onFaceDetection: Number of faces: ");
        sb.append(faceDetectionResult.extFaceList.size());
        CamLog.v(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("onFaceDetection: Selected index : ");
        sb2.append(faceDetectionResult.indexOfSelectedFace);
        CamLog.v(sb2.toString());
        if (!faceDetectionResult.extFaceList.isEmpty()) {
            final Iterator<CameraParameters.ExtFace> iterator = faceDetectionResult.extFaceList.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                final CameraParameters.ExtFace obj = iterator.next();
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("ExtFACE[");
                sb3.append(i);
                sb3.append("]");
                final String string = sb3.toString();
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(string);
                sb4.append(" face = ");
                sb4.append(obj);
                sb4.append(" ");
                final String string2 = sb4.toString();
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(string2);
                sb5.append(" face.id = ");
                sb5.append(obj.id);
                sb5.append(" ");
                final String string3 = sb5.toString();
                final StringBuilder sb6 = new StringBuilder();
                sb6.append(string3);
                sb6.append(" face.rect = ");
                sb6.append(obj.rect);
                sb6.append(" ");
                final String string4 = sb6.toString();
                final StringBuilder sb7 = new StringBuilder();
                sb7.append(string4);
                sb7.append(" SmileScore = ");
                sb7.append(obj.smileScore);
                sb7.append(" ");
                CamLog.v(sb7.toString());
                ++i;
            }
        }
    }
    
    public static TaggedRectangle overwriteTaggedRectangle(final HashMap<String, TaggedRectangle> hashMap, final String key, final FaceInformationList list) {
        final Iterator<Map.Entry<String, TaggedRectangle>> iterator = hashMap.entrySet().iterator();
    Label_0102:
        while (true) {
            int i;
            Map.Entry<String, V> entry;
            do {
                final boolean hasNext = iterator.hasNext();
                String key2 = null;
                if (!hasNext) {
                    final TaggedRectangle value = null;
                    if (value != null) {
                        hashMap.remove(key2);
                        hashMap.put(key, value);
                    }
                    return value;
                }
                entry = iterator.next();
                key2 = entry.getKey();
                final int n = 0;
                final Iterator<NamedFace> iterator2 = list.getNamedFaceList().iterator();
                do {
                    i = n;
                    if (iterator2.hasNext()) {
                        continue;
                    }
                    continue Label_0102;
                } while (!key2.equals(iterator2.next().mUuid));
                i = 1;
            } while (i != 0);
            final TaggedRectangle value = (TaggedRectangle)entry.getValue();
            continue;
        }
    }
    
    public static CameraParameters.FaceDetectionResult setUuidFaceDetectionResult(final CameraParameters.FaceDetectionResult faceDetectionResult) {
        final Iterator<CameraParameters.ExtFace> iterator = faceDetectionResult.extFaceList.iterator();
        int id = 0;
        while (iterator.hasNext()) {
            iterator.next().id = id;
            ++id;
        }
        return faceDetectionResult;
    }
    
    private static class DistanceComparator implements Comparator<DistanceMapItem>
    {
        @Override
        public int compare(final DistanceMapItem distanceMapItem, final DistanceMapItem distanceMapItem2) {
            return distanceMapItem.getDistance() - distanceMapItem2.getDistance();
        }
    }
    
    private static class DistanceMapItem
    {
        private final int mArrayIndex;
        private final int mDistance;
        
        public DistanceMapItem(final int mArrayIndex, final int mDistance) {
            this.mArrayIndex = mArrayIndex;
            this.mDistance = mDistance;
        }
        
        public int getArrayIndex() {
            return this.mArrayIndex;
        }
        
        public int getDistance() {
            return this.mDistance;
        }
    }
}
