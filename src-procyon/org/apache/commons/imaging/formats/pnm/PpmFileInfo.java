// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.pnm;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;

class PpmFileInfo extends FileInfo
{
    private final int bytesPerSample;
    private final int max;
    private final float scale;
    
    public PpmFileInfo(final int n, final int n2, final boolean b, final int max) throws ImageReadException {
        super(n, n2, b);
        if (max <= 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("PPM maxVal ");
            sb.append(max);
            sb.append(" is out of range [1;65535]");
            throw new ImageReadException(sb.toString());
        }
        if (max <= 255) {
            this.scale = 255.0f;
            this.bytesPerSample = 1;
        }
        else {
            if (max > 65535) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("PPM maxVal ");
                sb2.append(max);
                sb2.append(" is out of range [1;65535]");
                throw new ImageReadException(sb2.toString());
            }
            this.scale = 65535.0f;
            this.bytesPerSample = 2;
        }
        this.max = max;
    }
    
    @Override
    public int getBitDepth() {
        return this.max;
    }
    
    @Override
    public ImageInfo.ColorType getColorType() {
        return ImageInfo.ColorType.RGB;
    }
    
    @Override
    public ImageFormat getImageType() {
        return ImageFormats.PPM;
    }
    
    @Override
    public String getImageTypeDescription() {
        return "PPM: portable pixmap file format";
    }
    
    @Override
    public String getMIMEType() {
        return "image/x-portable-pixmap";
    }
    
    @Override
    public int getNumComponents() {
        return 3;
    }
    
    @Override
    public int getRGB(final InputStream inputStream) throws IOException {
        return (FileInfo.scaleSample(FileInfo.readSample(inputStream, this.bytesPerSample), this.scale, this.max) & 0xFF) << 0 | ((FileInfo.scaleSample(FileInfo.readSample(inputStream, this.bytesPerSample), this.scale, this.max) & 0xFF) << 16 | 0xFF000000 | (FileInfo.scaleSample(FileInfo.readSample(inputStream, this.bytesPerSample), this.scale, this.max) & 0xFF) << 8);
    }
    
    @Override
    public int getRGB(final WhiteSpaceReader whiteSpaceReader) throws IOException {
        return (FileInfo.scaleSample(Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()), this.scale, this.max) & 0xFF) << 0 | ((FileInfo.scaleSample(Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()), this.scale, this.max) & 0xFF) << 16 | 0xFF000000 | (FileInfo.scaleSample(Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()), this.scale, this.max) & 0xFF) << 8);
    }
    
    @Override
    public boolean hasAlpha() {
        return false;
    }
}
