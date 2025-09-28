package org.apache.commons.imaging;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ImageWriteException extends ImagingException {
    private static final long serialVersionUID = -1;

    public ImageWriteException(String str) {
        super(str);
    }

    public ImageWriteException(String str, Throwable th) {
        super(str, th);
    }

    public ImageWriteException(String str, Object obj) {
        super(str + ": " + obj + " (" + getType(obj) + ")");
    }

    private static String getType(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof Object[]) {
            return "[Object[]: " + ((Object[]) obj).length + "]";
        }
        if (obj instanceof char[]) {
            return "[char[]: " + ((char[]) obj).length + "]";
        }
        if (obj instanceof byte[]) {
            return "[byte[]: " + ((byte[]) obj).length + "]";
        }
        if (obj instanceof short[]) {
            return "[short[]: " + ((short[]) obj).length + "]";
        }
        if (obj instanceof int[]) {
            return "[int[]: " + ((int[]) obj).length + "]";
        }
        if (obj instanceof long[]) {
            return "[long[]: " + ((long[]) obj).length + "]";
        }
        if (obj instanceof float[]) {
            return "[float[]: " + ((float[]) obj).length + "]";
        }
        if (obj instanceof double[]) {
            return "[double[]: " + ((double[]) obj).length + "]";
        }
        if (obj instanceof boolean[]) {
            return "[boolean[]: " + ((boolean[]) obj).length + "]";
        }
        return obj.getClass().getName();
    }
}
