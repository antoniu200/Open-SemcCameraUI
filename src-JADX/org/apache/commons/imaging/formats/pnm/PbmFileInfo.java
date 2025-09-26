package org.apache.commons.imaging.formats.pnm;

import android.support.v4.view.ViewCompat;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class PbmFileInfo extends FileInfo {
    private int bitcache;
    private int bitsInCache;

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public int getBitDepth() {
        return 1;
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public String getImageTypeDescription() {
        return "PBM: portable bitmap fileformat";
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public String getMIMEType() {
        return "image/x-portable-bitmap";
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public int getNumComponents() {
        return 1;
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public boolean hasAlpha() {
        return false;
    }

    public PbmFileInfo(int i, int i2, boolean z) {
        super(i, i2, z);
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public ImageFormat getImageType() {
        return ImageFormats.PBM;
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public ImageInfo.ColorType getColorType() {
        return ImageInfo.ColorType.BW;
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    protected void newline() {
        this.bitcache = 0;
        this.bitsInCache = 0;
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public int getRGB(InputStream inputStream) throws IOException {
        if (this.bitsInCache < 1) {
            int i = inputStream.read();
            if (i < 0) {
                throw new IOException("PBM: Unexpected EOF");
            }
            this.bitcache = i & 255;
            this.bitsInCache += 8;
        }
        int i2 = (this.bitcache >> 7) & 1;
        this.bitcache <<= 1;
        this.bitsInCache--;
        if (i2 == 0) {
            return -1;
        }
        if (i2 == 1) {
            return ViewCompat.MEASURED_STATE_MASK;
        }
        throw new IOException("PBM: bad bit: " + i2);
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public int getRGB(WhiteSpaceReader whiteSpaceReader) throws NumberFormatException, IOException {
        int i = Integer.parseInt(whiteSpaceReader.readtoWhiteSpace());
        if (i == 0) {
            return ViewCompat.MEASURED_STATE_MASK;
        }
        if (i == 1) {
            return -1;
        }
        throw new IOException("PBM: bad bit: " + i);
    }
}
