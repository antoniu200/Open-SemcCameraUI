package org.apache.commons.imaging.formats.gif;

import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ImageContents {
    final List<GifBlock> blocks;
    final GifHeaderInfo gifHeaderInfo;
    final byte[] globalColorTable;

    ImageContents(GifHeaderInfo gifHeaderInfo, byte[] bArr, List<GifBlock> list) {
        this.gifHeaderInfo = gifHeaderInfo;
        this.globalColorTable = bArr;
        this.blocks = list;
    }
}
