// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.font;

import android.widget.TextView;
import android.widget.Button;
import java.io.File;
import android.graphics.Typeface;

public class FontUtil
{
    public static final String TAG = "FontUtil";
    
    public static Typeface createTypeface(final RobotoFontType robotoFontType) {
        Typeface fromFile;
        if (robotoFontType != null && new File(robotoFontType.mPath).exists()) {
            fromFile = Typeface.createFromFile(robotoFontType.mPath);
        }
        else {
            fromFile = null;
        }
        return fromFile;
    }
    
    public static boolean setBold(final Button button) {
        if (button == null) {
            return false;
        }
        button.setTypeface(Typeface.DEFAULT_BOLD);
        return true;
    }
    
    public static boolean setBold(final TextView textView) {
        if (textView == null) {
            return false;
        }
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        return true;
    }
    
    public static boolean setDefault(final Button button) {
        if (button == null) {
            return false;
        }
        button.setTypeface(Typeface.DEFAULT);
        return true;
    }
    
    public static boolean setDefault(final TextView textView) {
        if (textView == null) {
            return false;
        }
        textView.setTypeface(Typeface.DEFAULT);
        return true;
    }
    
    public static boolean setRobotoFont(final Button button, final RobotoFontType robotoFontType) {
        if (button == null || robotoFontType == null) {
            return false;
        }
        final Typeface typeface = createTypeface(robotoFontType);
        if (typeface != null) {
            button.setTypeface(typeface);
            return true;
        }
        return false;
    }
    
    public static boolean setRobotoFont(final TextView textView, final RobotoFontType robotoFontType) {
        if (textView == null || robotoFontType == null) {
            return false;
        }
        final Typeface typeface = createTypeface(robotoFontType);
        if (typeface != null) {
            textView.setTypeface(typeface);
            return true;
        }
        return false;
    }
    
    public enum RobotoFontType
    {
        private static final RobotoFontType[] $VALUES;
        
        BLACK("/system/fonts/Roboto-Black.ttf"), 
        BLACK_ITALIC("/system/fonts/Roboto-BlackItalic.ttf"), 
        BOLD("/system/fonts/Roboto-Bold.ttf"), 
        BOLD_ITALIC("/system/fonts/Roboto-BoldItalic.ttf"), 
        CONDENSED("/system/fonts/RobotoCondensed-Regular.ttf"), 
        CONDENSED_BOLD("/system/fonts/RobotoCondensed-Bold.ttf"), 
        CONDENSED_BOLD_ITALIC("/system/fonts/RobotoCondensed-BoldItalic.ttf"), 
        CONDENSED_ITALIC("/system/fonts/RobotoCondensed-Italic.ttf"), 
        CONDENSED_LIGHT("/system/fonts/RobotoCondensed-Light.ttf"), 
        CONDENSED_LIGHT_ITALIC("/system/fonts/RobotoCondensed-LightItalic.ttf"), 
        LIGHT("/system/fonts/Roboto-Light.ttf"), 
        LIGHT_ITALIC("/system/fonts/Roboto-LightItalic.ttf"), 
        MEDIUM("/system/fonts/Roboto-Medium.ttf"), 
        MEDIUM_ITALIC("/system/fonts/Roboto-MediumItalic.ttf"), 
        REGULAR("/system/fonts/Roboto-Regular.ttf"), 
        REGULAR_ITALIC("/system/fonts/Roboto-Italic.ttf"), 
        THIN("/system/fonts/Roboto-Thin.ttf"), 
        THIN_ITALIC("/system/fonts/Roboto-ThinItalic.ttf");
        
        private final String mPath;
        
        static {
            $VALUES = new RobotoFontType[] { RobotoFontType.THIN, RobotoFontType.THIN_ITALIC, RobotoFontType.LIGHT, RobotoFontType.LIGHT_ITALIC, RobotoFontType.REGULAR, RobotoFontType.REGULAR_ITALIC, RobotoFontType.MEDIUM, RobotoFontType.MEDIUM_ITALIC, RobotoFontType.BLACK, RobotoFontType.BLACK_ITALIC, RobotoFontType.BOLD, RobotoFontType.BOLD_ITALIC, RobotoFontType.CONDENSED_LIGHT, RobotoFontType.CONDENSED_LIGHT_ITALIC, RobotoFontType.CONDENSED, RobotoFontType.CONDENSED_ITALIC, RobotoFontType.CONDENSED_BOLD, RobotoFontType.CONDENSED_BOLD_ITALIC };
        }
        
        private RobotoFontType(final String mPath) {
            this.mPath = mPath;
        }
    }
}
