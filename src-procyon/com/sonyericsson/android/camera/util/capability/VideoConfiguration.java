// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import java.util.ArrayList;
import java.util.List;

public class VideoConfiguration
{
    private static final String DELIMITER_AT = "@";
    private static final String DELIMITER_COMMA = ",";
    private static final String DELIMITER_CROSS = "x";
    private static final String DELIMITER_SLASH = "/";
    public static final String TAG = "VideoConfiguration";
    public int mFps;
    public int mFrameNum;
    public int mHeight;
    public int mWidth;
    
    public VideoConfiguration(final int mWidth, final int mHeight, final int mFrameNum, final int mFps) {
        this.mWidth = 0;
        this.mHeight = 0;
        this.mFrameNum = 0;
        this.mFps = 0;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.mFrameNum = mFrameNum;
        this.mFps = mFps;
    }
    
    public static List<VideoConfiguration> parse(final String s) {
        return parse(s, ",");
    }
    
    public static List<VideoConfiguration> parse(final String s, final String regex) {
        if (s != null && regex != null) {
            final ArrayList list = new ArrayList();
            final String[] split = s.split(regex);
            for (int length = split.length, i = 0; i < length; ++i) {
                final String[] split2 = split[i].split("/", 2);
                if (split2.length != 2) {
                    return null;
                }
                final int int1 = Integer.parseInt(split2[1]);
                final String[] split3 = split2[0].split("@");
                int int2;
                if (split3.length != 2) {
                    int2 = 0;
                }
                else {
                    int2 = Integer.parseInt(split3[1]);
                }
                final String[] split4 = split3[0].split("x");
                if (split4.length != 2) {
                    return null;
                }
                list.add(new VideoConfiguration(Integer.parseInt(split4[0]), Integer.parseInt(split4[1]), int2, int1));
            }
            return list;
        }
        return null;
    }
}
