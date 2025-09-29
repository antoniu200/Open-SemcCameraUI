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

    public BufferedImage getEXIFThumbnail() throws ImageReadException, IOException {
        if (this.exif == null) {
            return null;
        }
        for (final TiffImageMetadata.Directory directory : this.exif.getDirectories()) {
            final BufferedImage thumbnail = directory.getThumbnail();
            if (thumbnail != null) {
                return thumbnail;
            }
            final JpegImageData jpegImageData = directory.getJpegImageData();
            if (jpegImageData == null) {
                continue;
            }
            Object read = null;
            Label_0127: {
                try {
                    Imaging.getBufferedImage(((TiffElement.DataElement)jpegImageData).getData());
                    break Label_0127;
                }
                catch (final IOException ex) {
                    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(((TiffElement.DataElement)jpegImageData).getData());
                }
                catch (final ImagingException ex2) {
                    read = new ByteArrayInputStream(((TiffElement.DataElement)jpegImageData).getData());
                }
                finally {
                    ImageIO.read(new ByteArrayInputStream(((TiffElement.DataElement)jpegImageData).getData()));
                }
                read = ImageIO.read((InputStream)read);
            }
            if (read != null) {
                return (BufferedImage)read;
            }
        }
        return null;
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
