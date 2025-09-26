// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.pnm;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;

class PbmFileInfo extends FileInfo
{
    private int bitcache;
    private int bitsInCache;
    
    public PbmFileInfo(final int n, final int n2, final boolean b) {
        super(n, n2, b);
    }
    
    @Override
    public int getBitDepth() {
        return 1;
    }
    
    @Override
    public ImageInfo.ColorType getColorType() {
        return ImageInfo.ColorType.BW;
    }
    
    @Override
    public ImageFormat getImageType() {
        return ImageFormats.PBM;
    }
    
    @Override
    public String getImageTypeDescription() {
        return "PBM: portable bitmap fileformat";
    }
    
    @Override
    public String getMIMEType() {
        return "image/x-portable-bitmap";
    }
    
    @Override
    public int getNumComponents() {
        return 1;
    }
    
    @Override
    public int getRGB(final InputStream inputStream) throws IOException {
        if (this.bitsInCache < 1) {
            final int read = inputStream.read();
            if (read < 0) {
                throw new IOException("PBM: Unexpected EOF");
            }
            this.bitcache = (read & 0xFF);
            this.bitsInCache += 8;
        }
        final int i = this.bitcache >> 7 & 0x1;
        this.bitcache <<= 1;
        --this.bitsInCache;
        if (i == 0) {
            return -1;
        }
        if (i == 1) {
            return -16777216;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("PBM: bad bit: ");
        sb.append(i);
        throw new IOException(sb.toString());
    }
    
    @Override
    public int getRGB(final WhiteSpaceReader whiteSpaceReader) throws IOException {
        final int int1 = Integer.parseInt(whiteSpaceReader.readtoWhiteSpace());
        if (int1 == 0) {
            return -16777216;
        }
        if (int1 == 1) {
            return -1;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("PBM: bad bit: ");
        sb.append(int1);
        throw new IOException(sb.toString());
    }
    
    @Override
    public boolean hasAlpha() {
        return false;
    }
    
    @Override
    protected void newline() {
        this.bitcache = 0;
        this.bitsInCache = 0;
    }
}
