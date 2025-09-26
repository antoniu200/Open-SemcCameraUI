package org.apache.commons.imaging.formats.pnm;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class PamFileInfo extends FileInfo {
    private final int bytesPerSample;
    private final int depth;
    private final boolean hasAlpha;
    private final int maxval;
    private final float scale;
    private final TupleReader tupleReader;

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public String getImageTypeDescription() {
        return "PAM: portable arbitrary map file format";
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public String getMIMEType() {
        return "image/x-portable-arbitrary-map";
    }

    PamFileInfo(int i, int i2, int i3, int i4, String str) throws ImageReadException {
        super(i, i2, true);
        this.depth = i3;
        this.maxval = i4;
        if (i4 <= 0) {
            throw new ImageReadException("PAM maxVal " + i4 + " is out of range [1;65535]");
        }
        if (i4 <= 255) {
            this.scale = 255.0f;
            this.bytesPerSample = 1;
        } else if (i4 <= 65535) {
            this.scale = 65535.0f;
            this.bytesPerSample = 2;
        } else {
            throw new ImageReadException("PAM maxVal " + i4 + " is out of range [1;65535]");
        }
        this.hasAlpha = str.endsWith("_ALPHA");
        if ("BLACKANDWHITE".equals(str) || "BLACKANDWHITE_ALPHA".equals(str)) {
            this.tupleReader = new GrayscaleTupleReader(ImageInfo.ColorType.BW);
            return;
        }
        if ("GRAYSCALE".equals(str) || "GRAYSCALE_ALPHA".equals(str)) {
            this.tupleReader = new GrayscaleTupleReader(ImageInfo.ColorType.GRAYSCALE);
            return;
        }
        if ("RGB".equals(str) || "RGB_ALPHA".equals(str)) {
            this.tupleReader = new ColorTupleReader();
            return;
        }
        throw new ImageReadException("Unknown PAM tupletype '" + str + "'");
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public boolean hasAlpha() {
        return this.hasAlpha;
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public int getNumComponents() {
        return this.depth;
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public int getBitDepth() {
        return this.maxval;
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public ImageFormat getImageType() {
        return ImageFormats.PAM;
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public ImageInfo.ColorType getColorType() {
        return this.tupleReader.getColorType();
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public int getRGB(WhiteSpaceReader whiteSpaceReader) throws IOException {
        throw new UnsupportedOperationException("PAM files are only ever binary");
    }

    @Override // org.apache.commons.imaging.formats.pnm.FileInfo
    public int getRGB(InputStream inputStream) throws IOException {
        return this.tupleReader.getRGB(inputStream);
    }

    private abstract class TupleReader {
        public abstract ImageInfo.ColorType getColorType();

        public abstract int getRGB(InputStream inputStream) throws IOException;

        private TupleReader() {
        }
    }

    private class GrayscaleTupleReader extends TupleReader {
        private final ImageInfo.ColorType colorType;

        public GrayscaleTupleReader(ImageInfo.ColorType colorType) {
            super();
            this.colorType = colorType;
        }

        @Override // org.apache.commons.imaging.formats.pnm.PamFileInfo.TupleReader
        public ImageInfo.ColorType getColorType() {
            return this.colorType;
        }

        @Override // org.apache.commons.imaging.formats.pnm.PamFileInfo.TupleReader
        public int getRGB(InputStream inputStream) throws IOException {
            int iScaleSample = FileInfo.scaleSample(FileInfo.readSample(inputStream, PamFileInfo.this.bytesPerSample), PamFileInfo.this.scale, PamFileInfo.this.maxval);
            int iScaleSample2 = PamFileInfo.this.hasAlpha ? FileInfo.scaleSample(FileInfo.readSample(inputStream, PamFileInfo.this.bytesPerSample), PamFileInfo.this.scale, PamFileInfo.this.maxval) : 255;
            int i = 255 & iScaleSample;
            return ((iScaleSample2 & 255) << 24) | (i << 16) | (i << 8) | (i << 0);
        }
    }

    private class ColorTupleReader extends TupleReader {
        private ColorTupleReader() {
            super();
        }

        @Override // org.apache.commons.imaging.formats.pnm.PamFileInfo.TupleReader
        public ImageInfo.ColorType getColorType() {
            return ImageInfo.ColorType.RGB;
        }

        @Override // org.apache.commons.imaging.formats.pnm.PamFileInfo.TupleReader
        public int getRGB(InputStream inputStream) throws IOException {
            int sample = FileInfo.readSample(inputStream, PamFileInfo.this.bytesPerSample);
            int sample2 = FileInfo.readSample(inputStream, PamFileInfo.this.bytesPerSample);
            int sample3 = FileInfo.readSample(inputStream, PamFileInfo.this.bytesPerSample);
            return (((PamFileInfo.this.hasAlpha ? FileInfo.scaleSample(FileInfo.readSample(inputStream, PamFileInfo.this.bytesPerSample), PamFileInfo.this.scale, PamFileInfo.this.maxval) : 255) & 255) << 24) | ((255 & FileInfo.scaleSample(sample, PamFileInfo.this.scale, PamFileInfo.this.maxval)) << 16) | ((255 & FileInfo.scaleSample(sample2, PamFileInfo.this.scale, PamFileInfo.this.maxval)) << 8) | ((255 & FileInfo.scaleSample(sample3, PamFileInfo.this.scale, PamFileInfo.this.maxval)) << 0);
        }
    }
}
