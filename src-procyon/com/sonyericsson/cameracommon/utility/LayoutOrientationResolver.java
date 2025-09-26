// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import android.util.Size;
import android.graphics.Rect;
import android.graphics.Point;

public class LayoutOrientationResolver
{
    public static final String TAG = "LayoutOrientationResolver";
    private static final LayoutOrientationResolver sInstance;
    private LayoutOrientationType mLayoutOrientation;
    
    static {
        sInstance = new LayoutOrientationResolver();
    }
    
    private LayoutOrientationResolver() {
        this.mLayoutOrientation = LayoutOrientationType.PORTRAIT;
    }
    
    public static LayoutOrientationResolver getInstance() {
        return LayoutOrientationResolver.sInstance;
    }
    
    public int getConfigurationOrientation() {
        return 1;
    }
    
    public LayoutOrientationType getOrientation() {
        return this.mLayoutOrientation;
    }
    
    public Point getPointAccordingToLayoutOrientation(Point point) {
        if (point == null) {
            return point;
        }
        if (this.mLayoutOrientation == LayoutOrientationType.PORTRAIT) {
            point = new Point(point.y, point.x);
        }
        else {
            point = new Point(point);
        }
        return point;
    }
    
    public Rect getRectAccordingToLayoutOrientation(Rect rect) {
        if (rect == null) {
            return null;
        }
        if (this.mLayoutOrientation == LayoutOrientationType.PORTRAIT) {
            rect = new Rect(0, 0, rect.height(), rect.width());
        }
        else {
            rect = new Rect(rect);
        }
        return rect;
    }
    
    public Size getSizeAccordingToLayoutOrientation(final Size size) {
        int n;
        int n2;
        if (this.mLayoutOrientation == LayoutOrientationType.PORTRAIT) {
            n = size.getHeight();
            n2 = size.getWidth();
        }
        else {
            n = size.getWidth();
            n2 = size.getHeight();
        }
        return new Size(n, n2);
    }
    
    public enum LayoutOrientationType
    {
        private static final LayoutOrientationType[] $VALUES;
        
        BEHIND, 
        LANDSCAPE, 
        PORTRAIT;
        
        static {
            $VALUES = new LayoutOrientationType[] { LayoutOrientationType.PORTRAIT, LayoutOrientationType.LANDSCAPE, LayoutOrientationType.BEHIND };
        }
    }
}
