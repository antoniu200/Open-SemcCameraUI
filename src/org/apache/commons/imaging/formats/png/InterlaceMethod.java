package org.apache.commons.imaging.formats.png;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum InterlaceMethod {
    NONE(false),
    ADAM7(true);

    private final boolean progressive;

    InterlaceMethod(boolean z) {
        this.progressive = z;
    }

    public boolean isProgressive() {
        return this.progressive;
    }
}
