package org.apache.commons.imaging.common;

import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface ImageMetadata {

    public interface ImageMetadataItem {
        String toString();

        String toString(String str);
    }

    List<? extends ImageMetadataItem> getItems();

    String toString(String str);
}
