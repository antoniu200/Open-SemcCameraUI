package org.apache.commons.imaging.palette;

import org.apache.commons.imaging.ImageWriteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface Palette {
    int getEntry(int i);

    int getPaletteIndex(int i) throws ImageWriteException;

    int length();
}
