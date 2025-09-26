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

class PamFileInfo extends FileInfo
{
    private final int bytesPerSample;
    private final int depth;
    private final boolean hasAlpha;
    private final int maxval;
    private final float scale;
    private final TupleReader tupleReader;
    
    PamFileInfo(final int n, final int n2, final int depth, final int i, final String str) throws ImageReadException {
        super(n, n2, true);
        this.depth = depth;
        this.maxval = i;
        if (i <= 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("PAM maxVal ");
            sb.append(i);
            sb.append(" is out of range [1;65535]");
            throw new ImageReadException(sb.toString());
        }
        if (i <= 255) {
            this.scale = 255.0f;
            this.bytesPerSample = 1;
        }
        else {
            if (i > 65535) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("PAM maxVal ");
                sb2.append(i);
                sb2.append(" is out of range [1;65535]");
                throw new ImageReadException(sb2.toString());
            }
            this.scale = 65535.0f;
            this.bytesPerSample = 2;
        }
        this.hasAlpha = str.endsWith("_ALPHA");
        if (!"BLACKANDWHITE".equals(str) && !"BLACKANDWHITE_ALPHA".equals(str)) {
            if (!"GRAYSCALE".equals(str) && !"GRAYSCALE_ALPHA".equals(str)) {
                if (!"RGB".equals(str) && !"RGB_ALPHA".equals(str)) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Unknown PAM tupletype '");
                    sb3.append(str);
                    sb3.append("'");
                    throw new ImageReadException(sb3.toString());
                }
                this.tupleReader = (TupleReader)new ColorTupleReader();
            }
            else {
                this.tupleReader = (TupleReader)new GrayscaleTupleReader(ImageInfo.ColorType.GRAYSCALE);
            }
        }
        else {
            this.tupleReader = (TupleReader)new GrayscaleTupleReader(ImageInfo.ColorType.BW);
        }
    }
    
    @Override
    public int getBitDepth() {
        return this.maxval;
    }
    
    @Override
    public ImageInfo.ColorType getColorType() {
        return this.tupleReader.getColorType();
    }
    
    @Override
    public ImageFormat getImageType() {
        return ImageFormats.PAM;
    }
    
    @Override
    public String getImageTypeDescription() {
        return "PAM: portable arbitrary map file format";
    }
    
    @Override
    public String getMIMEType() {
        return "image/x-portable-arbitrary-map";
    }
    
    @Override
    public int getNumComponents() {
        return this.depth;
    }
    
    @Override
    public int getRGB(final InputStream inputStream) throws IOException {
        return this.tupleReader.getRGB(inputStream);
    }
    
    @Override
    public int getRGB(final WhiteSpaceReader whiteSpaceReader) throws IOException {
        throw new UnsupportedOperationException("PAM files are only ever binary");
    }
    
    @Override
    public boolean hasAlpha() {
        return this.hasAlpha;
    }
    
    private class ColorTupleReader extends TupleReader
    {
        final PamFileInfo this$0;
        
        private ColorTupleReader(final PamFileInfo this$0) {
        }
        
        @Override
        public ImageInfo.ColorType getColorType() {
            return ImageInfo.ColorType.RGB;
        }
        
        @Override
        public int getRGB(final InputStream inputStream) throws IOException {
            final int sample = FileInfo.readSample(inputStream, this.this$0.bytesPerSample);
            final int sample2 = FileInfo.readSample(inputStream, this.this$0.bytesPerSample);
            final int sample3 = FileInfo.readSample(inputStream, this.this$0.bytesPerSample);
            final int scaleSample = FileInfo.scaleSample(sample, this.this$0.scale, this.this$0.maxval);
            final int scaleSample2 = FileInfo.scaleSample(sample2, this.this$0.scale, this.this$0.maxval);
            final int scaleSample3 = FileInfo.scaleSample(sample3, this.this$0.scale, this.this$0.maxval);
            int scaleSample4;
            if (this.this$0.hasAlpha) {
                scaleSample4 = FileInfo.scaleSample(FileInfo.readSample(inputStream, this.this$0.bytesPerSample), this.this$0.scale, this.this$0.maxval);
            }
            else {
                scaleSample4 = 255;
            }
            return (scaleSample4 & 0xFF) << 24 | (0xFF & scaleSample) << 16 | (0xFF & scaleSample2) << 8 | (0xFF & scaleSample3) << 0;
        }
    }
    
    private class GrayscaleTupleReader extends TupleReader
    {
        private final ImageInfo.ColorType colorType;
        final PamFileInfo this$0;
        
        public GrayscaleTupleReader(final PamFileInfo this$0, final ImageInfo.ColorType colorType) {
            this.colorType = colorType;
        }
        
        @Override
        public ImageInfo.ColorType getColorType() {
            return this.colorType;
        }
        
        @Override
        public int getRGB(final InputStream inputStream) throws IOException {
            final int scaleSample = FileInfo.scaleSample(FileInfo.readSample(inputStream, this.this$0.bytesPerSample), this.this$0.scale, this.this$0.maxval);
            int scaleSample2;
            if (this.this$0.hasAlpha) {
                scaleSample2 = FileInfo.scaleSample(FileInfo.readSample(inputStream, this.this$0.bytesPerSample), this.this$0.scale, this.this$0.maxval);
            }
            else {
                scaleSample2 = 255;
            }
            final int n = 0xFF & scaleSample;
            return (scaleSample2 & 0xFF) << 24 | n << 16 | n << 8 | n << 0;
        }
    }
    
    private abstract class TupleReader
    {
        final PamFileInfo this$0;
        
        private TupleReader(final PamFileInfo this$0) {
            this.this$0 = this$0;
        }
        
        public abstract ImageInfo.ColorType getColorType();
        
        public abstract int getRGB(final InputStream p0) throws IOException;
    }
}
