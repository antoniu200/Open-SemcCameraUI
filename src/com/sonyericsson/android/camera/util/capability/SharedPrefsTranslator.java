package com.sonyericsson.android.camera.util.capability;

import android.graphics.Rect;
import android.util.Range;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class SharedPrefsTranslator {
    public static final String CONNECTOR_AT = "@";
    public static final String CONNECTOR_CROSS = "x";
    public static final String CONNECTOR_SLASH = "/";
    public static final String DELIMITER = ";";
    public static final String TAG = "SharedPrefsTranslator";

    SharedPrefsTranslator() {
    }

    public static final String fromRect(Rect rect) {
        if (rect == null) {
            return "";
        }
        return rect.width() + CONNECTOR_CROSS + rect.height();
    }

    public static final String fromStringList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(list.size());
        for (String str : list) {
            sb.append(DELIMITER);
            sb.append(str);
        }
        return sb.toString();
    }

    public static final String fromRectList(List<Rect> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(list.size());
        for (Rect rect : list) {
            sb.append(DELIMITER);
            sb.append(rect.width());
            sb.append(CONNECTOR_CROSS);
            sb.append(rect.height());
        }
        return sb.toString();
    }

    public static final String fromVideoConfigurationList(List<VideoConfiguration> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(list.size());
        for (VideoConfiguration videoConfiguration : list) {
            sb.append(DELIMITER);
            sb.append(videoConfiguration.mWidth);
            sb.append(CONNECTOR_CROSS);
            sb.append(videoConfiguration.mHeight);
            if (videoConfiguration.mFrameNum != 0) {
                sb.append("@");
                sb.append(videoConfiguration.mFrameNum);
            }
            sb.append(CONNECTOR_SLASH);
            sb.append(videoConfiguration.mFps);
        }
        return sb.toString();
    }

    public static final String fromIntegerRange(Range<Integer> range) {
        if (range == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String strValueOf = String.valueOf(range.getLower());
        String strValueOf2 = String.valueOf(range.getUpper());
        sb.append(strValueOf);
        sb.append(CONNECTOR_CROSS);
        sb.append(strValueOf2);
        return sb.toString();
    }

    public static final String fromIntArrayList(List<int[]> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(list.size());
        for (int[] iArr : list) {
            if (iArr.length == 2) {
                sb.append(DELIMITER);
                String strValueOf = String.valueOf(iArr[0]);
                String strValueOf2 = String.valueOf(iArr[1]);
                sb.append(strValueOf);
                sb.append(CONNECTOR_CROSS);
                sb.append(strValueOf2);
            }
        }
        return sb.toString();
    }

    public static final Rect getRect(String str) {
        Rect rect = new Rect();
        if (str != null) {
            String[] strArrSplit = str.split(CONNECTOR_CROSS);
            if (strArrSplit.length == 2) {
                try {
                    rect.right = Integer.parseInt(strArrSplit[0]);
                    rect.bottom = Integer.parseInt(strArrSplit[1]);
                } catch (NumberFormatException e) {
                    if (CamLog.VERBOSE) {
                        CamLog.w("getRect failed: " + str, e);
                    }
                    rect.setEmpty();
                }
            }
        }
        return rect;
    }

    public static final List<String> getStringList(String str) {
        ArrayList arrayList = new ArrayList();
        if (str != null && !str.isEmpty()) {
            String[] strArrSplit = str.split(DELIMITER);
            try {
                if (Integer.parseInt(strArrSplit[0]) > 0) {
                    for (String str2 : strArrSplit) {
                        arrayList.add(str2);
                    }
                }
            } catch (NumberFormatException e) {
                if (CamLog.VERBOSE) {
                    CamLog.w("getStringList failed: " + str, e);
                }
                arrayList.clear();
            }
        }
        return arrayList;
    }

    public static final List<Rect> getRectList(String str) {
        ArrayList arrayList = new ArrayList();
        if (str != null && !str.isEmpty()) {
            String[] strArrSplit = str.split(DELIMITER);
            try {
                if (Integer.parseInt(strArrSplit[0]) > 0) {
                    for (String str2 : strArrSplit) {
                        arrayList.add(getRect(str2));
                    }
                }
            } catch (NumberFormatException e) {
                if (CamLog.VERBOSE) {
                    CamLog.w("getRectList failed: " + str, e);
                }
                arrayList.clear();
            }
        }
        return arrayList;
    }

    public static final List<VideoConfiguration> getVideoConfigurationList(String str) {
        ArrayList arrayList = new ArrayList();
        if (str == null || str.isEmpty()) {
            return arrayList;
        }
        String[] strArrSplit = str.split(DELIMITER, 2);
        return strArrSplit.length == 2 ? VideoConfiguration.parse(strArrSplit[1], DELIMITER) : arrayList;
    }

    public static final int[] getIntArray(String str) {
        int[] iArr = new int[2];
        if (str == null) {
            return iArr;
        }
        String[] strArrSplit = str.split(CONNECTOR_CROSS);
        if (strArrSplit.length != 2) {
            return iArr;
        }
        try {
            iArr[0] = Integer.parseInt(strArrSplit[0]);
            iArr[1] = Integer.parseInt(strArrSplit[1]);
            return iArr;
        } catch (NumberFormatException e) {
            if (CamLog.VERBOSE) {
                CamLog.w("getIntArray failed: " + str, e);
            }
            return new int[0];
        }
    }

    public static final List<int[]> getIntArrayList(String str) {
        ArrayList arrayList = new ArrayList();
        if (str != null && !str.isEmpty()) {
            String[] strArrSplit = str.split(DELIMITER);
            try {
                if (Integer.parseInt(strArrSplit[0]) > 0) {
                    for (String str2 : strArrSplit) {
                        arrayList.add(getIntArray(str2));
                    }
                }
            } catch (NumberFormatException e) {
                if (CamLog.VERBOSE) {
                    CamLog.w("getIntArrayList failed: " + str, e);
                }
                arrayList.clear();
            }
        }
        return arrayList;
    }

    public static final Range<Integer> getIntegerRange(String str) {
        Range<Integer> range = new Range<>(0, 0);
        if (str == null) {
            return range;
        }
        String[] strArrSplit = str.split(CONNECTOR_CROSS);
        if (strArrSplit.length != 2) {
            return range;
        }
        try {
            return new Range<>(Integer.valueOf(Integer.parseInt(strArrSplit[0])), Integer.valueOf(Integer.parseInt(strArrSplit[1])));
        } catch (NumberFormatException e) {
            if (CamLog.VERBOSE) {
                CamLog.w("getIntArray failed: " + str, e);
            }
            return new Range<>(0, 0);
        }
    }
}
