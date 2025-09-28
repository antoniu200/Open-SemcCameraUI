package com.sonymobile.cameracommon.font;

import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;
import java.io.File;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FontUtil {
    public static final String TAG = "FontUtil";

    public enum RobotoFontType {
        THIN("/system/fonts/Roboto-Thin.ttf"),
        THIN_ITALIC("/system/fonts/Roboto-ThinItalic.ttf"),
        LIGHT("/system/fonts/Roboto-Light.ttf"),
        LIGHT_ITALIC("/system/fonts/Roboto-LightItalic.ttf"),
        REGULAR("/system/fonts/Roboto-Regular.ttf"),
        REGULAR_ITALIC("/system/fonts/Roboto-Italic.ttf"),
        MEDIUM("/system/fonts/Roboto-Medium.ttf"),
        MEDIUM_ITALIC("/system/fonts/Roboto-MediumItalic.ttf"),
        BLACK("/system/fonts/Roboto-Black.ttf"),
        BLACK_ITALIC("/system/fonts/Roboto-BlackItalic.ttf"),
        BOLD("/system/fonts/Roboto-Bold.ttf"),
        BOLD_ITALIC("/system/fonts/Roboto-BoldItalic.ttf"),
        CONDENSED_LIGHT("/system/fonts/RobotoCondensed-Light.ttf"),
        CONDENSED_LIGHT_ITALIC("/system/fonts/RobotoCondensed-LightItalic.ttf"),
        CONDENSED("/system/fonts/RobotoCondensed-Regular.ttf"),
        CONDENSED_ITALIC("/system/fonts/RobotoCondensed-Italic.ttf"),
        CONDENSED_BOLD("/system/fonts/RobotoCondensed-Bold.ttf"),
        CONDENSED_BOLD_ITALIC("/system/fonts/RobotoCondensed-BoldItalic.ttf");

        private final String mPath;

        RobotoFontType(String str) {
            this.mPath = str;
        }
    }

    public static Typeface createTypeface(RobotoFontType robotoFontType) {
        if (robotoFontType == null || !new File(robotoFontType.mPath).exists()) {
            return null;
        }
        return Typeface.createFromFile(robotoFontType.mPath);
    }

    public static boolean setRobotoFont(TextView textView, RobotoFontType robotoFontType) {
        Typeface typefaceCreateTypeface;
        if (textView == null || robotoFontType == null || (typefaceCreateTypeface = createTypeface(robotoFontType)) == null) {
            return false;
        }
        textView.setTypeface(typefaceCreateTypeface);
        return true;
    }

    public static boolean setRobotoFont(Button button, RobotoFontType robotoFontType) {
        Typeface typefaceCreateTypeface;
        if (button == null || robotoFontType == null || (typefaceCreateTypeface = createTypeface(robotoFontType)) == null) {
            return false;
        }
        button.setTypeface(typefaceCreateTypeface);
        return true;
    }

    public static boolean setBold(TextView textView) {
        if (textView == null) {
            return false;
        }
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        return true;
    }

    public static boolean setBold(Button button) {
        if (button == null) {
            return false;
        }
        button.setTypeface(Typeface.DEFAULT_BOLD);
        return true;
    }

    public static boolean setDefault(TextView textView) {
        if (textView == null) {
            return false;
        }
        textView.setTypeface(Typeface.DEFAULT);
        return true;
    }

    public static boolean setDefault(Button button) {
        if (button == null) {
            return false;
        }
        button.setTypeface(Typeface.DEFAULT);
        return true;
    }
}
