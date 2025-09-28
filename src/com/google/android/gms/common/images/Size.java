package com.google.android.gms.common.images;

import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class Size {
    private final int zznQ;
    private final int zznR;

    public Size(int i, int i2) {
        this.zznQ = i;
        this.zznR = i2;
    }

    public static Size parseSize(String str) throws NumberFormatException {
        if (str == null) {
            throw new IllegalArgumentException("string must not be null");
        }
        int iIndexOf = str.indexOf(42);
        if (iIndexOf < 0) {
            iIndexOf = str.indexOf(120);
        }
        if (iIndexOf < 0) {
            throw zzch(str);
        }
        try {
            return new Size(Integer.parseInt(str.substring(0, iIndexOf)), Integer.parseInt(str.substring(iIndexOf + 1)));
        } catch (NumberFormatException unused) {
            throw zzch(str);
        }
    }

    private static NumberFormatException zzch(String str) {
        throw new NumberFormatException("Invalid Size: \"" + str + "\"");
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Size)) {
            return false;
        }
        Size size = (Size) obj;
        return this.zznQ == size.zznQ && this.zznR == size.zznR;
    }

    public int getHeight() {
        return this.zznR;
    }

    public int getWidth() {
        return this.zznQ;
    }

    public int hashCode() {
        return ((this.zznQ >>> 16) | (this.zznQ << 16)) ^ this.zznR;
    }

    public String toString() {
        return this.zznQ + SharedPrefsTranslator.CONNECTOR_CROSS + this.zznR;
    }
}
