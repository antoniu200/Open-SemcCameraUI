package com.sonyericsson.android.camera.util.capability;

import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class VideoConfiguration {
    private static final String DELIMITER_AT = "@";
    private static final String DELIMITER_COMMA = ",";
    private static final String DELIMITER_CROSS = "x";
    private static final String DELIMITER_SLASH = "/";
    public static final String TAG = "VideoConfiguration";
    public int mFps;
    public int mFrameNum;
    public int mHeight;
    public int mWidth;

    public VideoConfiguration(int i, int i2, int i3, int i4) {
        this.mWidth = 0;
        this.mHeight = 0;
        this.mFrameNum = 0;
        this.mFps = 0;
        this.mWidth = i;
        this.mHeight = i2;
        this.mFrameNum = i3;
        this.mFps = i4;
    }

    public static List<VideoConfiguration> parse(String str) {
        return parse(str, DELIMITER_COMMA);
    }

    public static List<VideoConfiguration> parse(String str, String str2) throws NumberFormatException {
        if (str == null || str2 == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str3 : str.split(str2)) {
            String[] strArrSplit = str3.split("/", 2);
            if (strArrSplit.length != 2) {
                return null;
            }
            int i = Integer.parseInt(strArrSplit[1]);
            String[] strArrSplit2 = strArrSplit[0].split("@");
            int i2 = strArrSplit2.length != 2 ? 0 : Integer.parseInt(strArrSplit2[1]);
            String[] strArrSplit3 = strArrSplit2[0].split("x");
            if (strArrSplit3.length != 2) {
                return null;
            }
            arrayList.add(new VideoConfiguration(Integer.parseInt(strArrSplit3[0]), Integer.parseInt(strArrSplit3[1]), i2, i));
        }
        return arrayList;
    }
}
