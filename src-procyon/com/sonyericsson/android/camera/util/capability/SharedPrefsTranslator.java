// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import java.util.ArrayList;
import com.sonyericsson.android.camera.util.CamLog;
import android.graphics.Rect;
import android.util.Range;
import java.util.Iterator;
import java.util.List;

class SharedPrefsTranslator
{
    public static final String CONNECTOR_AT = "@";
    public static final String CONNECTOR_CROSS = "x";
    public static final String CONNECTOR_SLASH = "/";
    public static final String DELIMITER = ";";
    public static final String TAG = "SharedPrefsTranslator";
    
    public static final String fromIntArrayList(final List<int[]> list) {
        String string = "";
        if (list != null) {
            string = string;
            if (!list.isEmpty()) {
                final StringBuilder sb = new StringBuilder();
                sb.append(list.size());
                for (final int[] array : list) {
                    if (array.length == 2) {
                        sb.append(";");
                        final int i = array[0];
                        final int j = array[1];
                        sb.append(String.valueOf(i));
                        sb.append("x");
                        sb.append(String.valueOf(j));
                    }
                }
                string = sb.toString();
            }
        }
        return string;
    }
    
    public static final String fromIntegerRange(final Range<Integer> range) {
        String string = "";
        if (range != null) {
            final StringBuilder sb = new StringBuilder();
            final String value = String.valueOf(range.getLower());
            final String value2 = String.valueOf(range.getUpper());
            sb.append(value);
            sb.append("x");
            sb.append(value2);
            string = sb.toString();
        }
        return string;
    }
    
    public static final String fromRect(final Rect rect) {
        String string = "";
        if (rect != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(rect.width());
            sb.append("x");
            sb.append(rect.height());
            string = sb.toString();
        }
        return string;
    }
    
    public static final String fromRectList(final List<Rect> list) {
        String string = "";
        if (list != null) {
            string = string;
            if (!list.isEmpty()) {
                final StringBuilder sb = new StringBuilder();
                sb.append(list.size());
                for (final Rect rect : list) {
                    sb.append(";");
                    sb.append(rect.width());
                    sb.append("x");
                    sb.append(rect.height());
                }
                string = sb.toString();
            }
        }
        return string;
    }
    
    public static final String fromStringList(final List<String> list) {
        String string = "";
        if (list != null) {
            string = string;
            if (!list.isEmpty()) {
                final StringBuilder sb = new StringBuilder();
                sb.append(list.size());
                for (final String str : list) {
                    sb.append(";");
                    sb.append(str);
                }
                string = sb.toString();
            }
        }
        return string;
    }
    
    public static final String fromVideoConfigurationList(final List<VideoConfiguration> list) {
        String string = "";
        if (list != null) {
            string = string;
            if (!list.isEmpty()) {
                final StringBuilder sb = new StringBuilder();
                sb.append(list.size());
                for (final VideoConfiguration videoConfiguration : list) {
                    sb.append(";");
                    sb.append(videoConfiguration.mWidth);
                    sb.append("x");
                    sb.append(videoConfiguration.mHeight);
                    if (videoConfiguration.mFrameNum != 0) {
                        sb.append("@");
                        sb.append(videoConfiguration.mFrameNum);
                    }
                    sb.append("/");
                    sb.append(videoConfiguration.mFps);
                }
                string = sb.toString();
            }
        }
        return string;
    }
    
    public static final int[] getIntArray(final String str) {
        int[] array2;
        final int[] array = array2 = new int[2];
        if (str != null) {
            final String[] split = str.split("x");
            array2 = array;
            if (split.length == 2) {
                try {
                    array[0] = Integer.parseInt(split[0]);
                    array[1] = Integer.parseInt(split[1]);
                    array2 = array;
                }
                catch (final NumberFormatException ex) {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("getIntArray failed: ");
                        sb.append(str);
                        CamLog.w(sb.toString(), ex);
                    }
                    array2 = new int[0];
                }
            }
        }
        return array2;
    }
    
    public static final List<int[]> getIntArrayList(final String str) {
        final ArrayList list = new ArrayList();
        if (str != null && !str.isEmpty()) {
            final String[] split = str.split(";");
            int i = 0;
            try {
                if (Integer.parseInt(split[0]) > 0) {
                    while (i < split.length) {
                        list.add(getIntArray(split[i]));
                        ++i;
                    }
                }
            }
            catch (final NumberFormatException ex) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("getIntArrayList failed: ");
                    sb.append(str);
                    CamLog.w(sb.toString(), ex);
                }
                list.clear();
            }
        }
        return list;
    }
    
    public static final Range<Integer> getIntegerRange(final String str) {
        Range range = new Range((Comparable)0, (Comparable)0);
        if (str != null) {
            final String[] split = str.split("x");
            range = range;
            if (split.length == 2) {
                try {
                    range = new Range((Comparable)Integer.parseInt(split[0]), (Comparable)Integer.parseInt(split[1]));
                }
                catch (final NumberFormatException ex) {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("getIntArray failed: ");
                        sb.append(str);
                        CamLog.w(sb.toString(), ex);
                    }
                    range = new Range((Comparable)0, (Comparable)0);
                }
            }
        }
        return (Range<Integer>)range;
    }
    
    public static final Rect getRect(final String str) {
        final Rect rect = new Rect();
        if (str != null) {
            final String[] split = str.split("x");
            if (split.length == 2) {
                try {
                    rect.right = Integer.parseInt(split[0]);
                    rect.bottom = Integer.parseInt(split[1]);
                }
                catch (final NumberFormatException ex) {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("getRect failed: ");
                        sb.append(str);
                        CamLog.w(sb.toString(), ex);
                    }
                    rect.setEmpty();
                }
            }
        }
        return rect;
    }
    
    public static final List<Rect> getRectList(final String str) {
        final ArrayList list = new ArrayList();
        if (str != null && !str.isEmpty()) {
            final String[] split = str.split(";");
            int i = 0;
            try {
                if (Integer.parseInt(split[0]) > 0) {
                    while (i < split.length) {
                        list.add(getRect(split[i]));
                        ++i;
                    }
                }
            }
            catch (final NumberFormatException ex) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("getRectList failed: ");
                    sb.append(str);
                    CamLog.w(sb.toString(), ex);
                }
                list.clear();
            }
        }
        return list;
    }
    
    public static final List<String> getStringList(final String str) {
        final ArrayList list = new ArrayList();
        if (str != null && !str.isEmpty()) {
            final String[] split = str.split(";");
            int i = 0;
            try {
                if (Integer.parseInt(split[0]) > 0) {
                    while (i < split.length) {
                        list.add(split[i]);
                        ++i;
                    }
                }
            }
            catch (final NumberFormatException ex) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("getStringList failed: ");
                    sb.append(str);
                    CamLog.w(sb.toString(), ex);
                }
                list.clear();
            }
        }
        return list;
    }
    
    public static final List<VideoConfiguration> getVideoConfigurationList(final String s) {
        List<VideoConfiguration> parse;
        final ArrayList<VideoConfiguration> list = (ArrayList<VideoConfiguration>)(parse = new ArrayList<VideoConfiguration>());
        if (s != null) {
            parse = list;
            if (!s.isEmpty()) {
                final String[] split = s.split(";", 2);
                parse = list;
                if (split.length == 2) {
                    parse = VideoConfiguration.parse(split[1], ";");
                }
            }
        }
        return parse;
    }
}
