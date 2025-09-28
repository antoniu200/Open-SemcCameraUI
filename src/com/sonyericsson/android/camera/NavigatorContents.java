package com.sonyericsson.android.camera;

import android.content.Context;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum NavigatorContents {
    SUPERIOR_AUTO(R.drawable.cam_core_application_navigator_superior_auto_icn, R.drawable.cam_core_application_navigator_superior_auto_large_icn, R.string.cam_strings_capturing_mode_auto_txt),
    VIDEO(R.drawable.cam_core_application_navigator_video_icn, R.drawable.cam_core_application_navigator_video_large_icn, R.string.cam_strings_application_name_video_txt);

    private final int mIconId;
    private final int mLargeIconId;
    private final int mTextId;

    NavigatorContents(int i, int i2, int i3) {
        this.mIconId = i;
        this.mLargeIconId = i2;
        this.mTextId = i3;
    }

    public int getIconId() {
        return this.mIconId;
    }

    public int getLargeIconId() {
        return this.mLargeIconId;
    }

    public int getTextId() {
        return this.mTextId;
    }

    public String getText(Context context) {
        return context.getResources().getString(this.mTextId).toUpperCase();
    }

    public boolean hasPrevious() {
        return indexOf(this) > 0;
    }

    public boolean hasNext() {
        return indexOf(this) < values().length - 1;
    }

    public NavigatorContents next() {
        return next(1);
    }

    public NavigatorContents previous() {
        return previous(1);
    }

    public NavigatorContents next(int i) {
        int iIndexOf = indexOf(this) + i;
        if (iIndexOf < values().length) {
            return values()[iIndexOf];
        }
        return values()[values().length - 1];
    }

    public NavigatorContents previous(int i) {
        int iIndexOf = indexOf(this) - i;
        if (iIndexOf >= 0) {
            return values()[iIndexOf];
        }
        return values()[0];
    }

    public static int indexOf(NavigatorContents navigatorContents) {
        for (int i = 0; i < values().length; i++) {
            if (navigatorContents.equals(values()[i])) {
                return i;
            }
        }
        throw new IllegalArgumentException(navigatorContents.name() + " is not NavigatorContents.");
    }

    public static NavigatorContents valueOf(CapturingMode capturingMode) {
        switch (capturingMode) {
        }
        return SUPERIOR_AUTO;
    }
}
