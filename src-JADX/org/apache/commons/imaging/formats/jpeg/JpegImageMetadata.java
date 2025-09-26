package org.apache.commons.imaging.formats.jpeg;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageData;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.util.Debug;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class JpegImageMetadata implements ImageMetadata {
    private static final String NEWLINE = System.getProperty("line.separator");
    private final TiffImageMetadata exif;
    private final JpegPhotoshopMetadata photoshop;

    public JpegImageMetadata(JpegPhotoshopMetadata jpegPhotoshopMetadata, TiffImageMetadata tiffImageMetadata) {
        this.photoshop = jpegPhotoshopMetadata;
        this.exif = tiffImageMetadata;
    }

    public TiffImageMetadata getExif() {
        return this.exif;
    }

    public JpegPhotoshopMetadata getPhotoshop() {
        return this.photoshop;
    }

    public TiffField findEXIFValue(TagInfo tagInfo) {
        try {
            if (this.exif != null) {
                return this.exif.findField(tagInfo);
            }
            return null;
        } catch (ImageReadException unused) {
            return null;
        }
    }

    public TiffField findEXIFValueWithExactMatch(TagInfo tagInfo) {
        try {
            if (this.exif != null) {
                return this.exif.findField(tagInfo, true);
            }
            return null;
        } catch (ImageReadException unused) {
            return null;
        }
    }

    public Dimension getEXIFThumbnailSize() throws IOException, ImageReadException {
        byte[] eXIFThumbnailData = getEXIFThumbnailData();
        if (eXIFThumbnailData != null) {
            return Imaging.getImageSize(eXIFThumbnailData);
        }
        return null;
    }

    public byte[] getEXIFThumbnailData() throws IOException, ImageReadException {
        if (this.exif == null) {
            return null;
        }
        Iterator<? extends ImageMetadata.ImageMetadataItem> it = this.exif.getDirectories().iterator();
        while (it.hasNext()) {
            TiffImageMetadata.Directory directory = (TiffImageMetadata.Directory) it.next();
            byte[] data = directory.getJpegImageData() != null ? directory.getJpegImageData().getData() : null;
            if (data != null) {
                return data;
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x005b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0010 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.awt.image.BufferedImage getEXIFThumbnail() throws java.io.IOException, org.apache.commons.imaging.ImageReadException {
        /*
            r3 = this;
            org.apache.commons.imaging.formats.tiff.TiffImageMetadata r0 = r3.exif
            r1 = 0
            if (r0 != 0) goto L6
            return r1
        L6:
            org.apache.commons.imaging.formats.tiff.TiffImageMetadata r3 = r3.exif
            java.util.List r3 = r3.getDirectories()
            java.util.Iterator r3 = r3.iterator()
        L10:
            boolean r0 = r3.hasNext()
            if (r0 == 0) goto L5c
            java.lang.Object r0 = r3.next()
            org.apache.commons.imaging.common.ImageMetadata$ImageMetadataItem r0 = (org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem) r0
            org.apache.commons.imaging.formats.tiff.TiffImageMetadata$Directory r0 = (org.apache.commons.imaging.formats.tiff.TiffImageMetadata.Directory) r0
            java.awt.image.BufferedImage r2 = r0.getThumbnail()
            if (r2 == 0) goto L25
            return r2
        L25:
            org.apache.commons.imaging.formats.tiff.JpegImageData r0 = r0.getJpegImageData()
            if (r0 == 0) goto L10
            byte[] r2 = r0.getData()     // Catch: java.lang.Throwable -> L34 java.io.IOException -> L42 org.apache.commons.imaging.ImagingException -> L4c
            java.awt.image.BufferedImage r2 = org.apache.commons.imaging.Imaging.getBufferedImage(r2)     // Catch: java.lang.Throwable -> L34 java.io.IOException -> L42 org.apache.commons.imaging.ImagingException -> L4c
            goto L59
        L34:
            r3 = move-exception
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream
            byte[] r0 = r0.getData()
            r1.<init>(r0)
            javax.imageio.ImageIO.read(r1)
            throw r3
        L42:
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream
            byte[] r0 = r0.getData()
            r2.<init>(r0)
            goto L55
        L4c:
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream
            byte[] r0 = r0.getData()
            r2.<init>(r0)
        L55:
            java.awt.image.BufferedImage r2 = javax.imageio.ImageIO.read(r2)
        L59:
            if (r2 == 0) goto L10
            return r2
        L5c:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.imaging.formats.jpeg.JpegImageMetadata.getEXIFThumbnail():java.awt.image.BufferedImage");
    }

    public TiffImageData getRawImageData() {
        if (this.exif == null) {
            return null;
        }
        Iterator<? extends ImageMetadata.ImageMetadataItem> it = this.exif.getDirectories().iterator();
        while (it.hasNext()) {
            TiffImageData tiffImageData = ((TiffImageMetadata.Directory) it.next()).getTiffImageData();
            if (tiffImageData != null) {
                return tiffImageData;
            }
        }
        return null;
    }

    @Override // org.apache.commons.imaging.common.ImageMetadata
    public List<ImageMetadata.ImageMetadataItem> getItems() {
        ArrayList arrayList = new ArrayList();
        if (this.exif != null) {
            arrayList.addAll(this.exif.getItems());
        }
        if (this.photoshop != null) {
            arrayList.addAll(this.photoshop.getItems());
        }
        return arrayList;
    }

    public String toString() {
        return toString(null);
    }

    @Override // org.apache.commons.imaging.common.ImageMetadata
    public String toString(String str) {
        if (str == null) {
            str = "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        if (this.exif == null) {
            sb.append("No Exif metadata.");
        } else {
            sb.append("Exif metadata:");
            sb.append(NEWLINE);
            sb.append(this.exif.toString("\t"));
        }
        sb.append(NEWLINE);
        sb.append(str);
        if (this.photoshop == null) {
            sb.append("No Photoshop (IPTC) metadata.");
        } else {
            sb.append("Photoshop (IPTC) metadata:");
            sb.append(NEWLINE);
            sb.append(this.photoshop.toString("\t"));
        }
        return sb.toString();
    }

    public void dump() {
        Debug.debug(toString());
    }
}
