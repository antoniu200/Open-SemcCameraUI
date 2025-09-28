package org.apache.commons.imaging.formats.png;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PngImageInfo extends ImageInfo {
    private final List<PngText> textChunks;

    PngImageInfo(String str, int i, List<String> list, ImageFormat imageFormat, String str2, int i2, String str3, int i3, int i4, float f, int i5, float f2, int i6, boolean z, boolean z2, boolean z3, ImageInfo.ColorType colorType, ImageInfo.CompressionAlgorithm compressionAlgorithm, List<PngText> list2) {
        super(str, i, list, imageFormat, str2, i2, str3, i3, i4, f, i5, f2, i6, z, z2, z3, colorType, compressionAlgorithm);
        this.textChunks = list2;
    }

    public List<PngText> getTextChunks() {
        return new ArrayList(this.textChunks);
    }
}
