// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg;

import org.apache.commons.imaging.formats.tiff.TiffElement;
import org.apache.commons.imaging.formats.tiff.TiffImageData;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.awt.Dimension;
import org.apache.commons.imaging.formats.tiff.JpegImageData;
import java.util.Iterator;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.apache.commons.imaging.ImagingException;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.Imaging;
import java.awt.image.BufferedImage;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.util.Debug;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata;

public class JpegImageMetadata implements ImageMetadata
{
    private static final String NEWLINE;
    private final TiffImageMetadata exif;
    private final JpegPhotoshopMetadata photoshop;
    
    static {
        NEWLINE = System.getProperty("line.separator");
    }
    
    public JpegImageMetadata(final JpegPhotoshopMetadata photoshop, final TiffImageMetadata exif) {
        this.photoshop = photoshop;
        this.exif = exif;
    }
    
    public void dump() {
        Debug.debug(this.toString());
    }
    
    public TiffField findEXIFValue(final TagInfo tagInfo) {
        try {
            TiffField field;
            if (this.exif != null) {
                field = this.exif.findField(tagInfo);
            }
            else {
                field = null;
            }
            return field;
        }
        catch (final ImageReadException ex) {
            return null;
        }
    }
    
    public TiffField findEXIFValueWithExactMatch(final TagInfo tagInfo) {
        try {
            TiffField field;
            if (this.exif != null) {
                field = this.exif.findField(tagInfo, true);
            }
            else {
                field = null;
            }
            return field;
        }
        catch (final ImageReadException ex) {
            return null;
        }
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
    
    public byte[] getEXIFThumbnailData() throws ImageReadException, IOException {
        if (this.exif == null) {
            return null;
        }
        for (final TiffImageMetadata.Directory directory : this.exif.getDirectories()) {
            byte[] data;
            if (directory.getJpegImageData() != null) {
                data = ((TiffElement.DataElement)directory.getJpegImageData()).getData();
            }
            else {
                data = null;
            }
            if (data != null) {
                return data;
            }
        }
        return null;
    }
    
    public Dimension getEXIFThumbnailSize() throws ImageReadException, IOException {
        final byte[] exifThumbnailData = this.getEXIFThumbnailData();
        if (exifThumbnailData != null) {
            return Imaging.getImageSize(exifThumbnailData);
        }
        return null;
    }
    
    public TiffImageMetadata getExif() {
        return this.exif;
    }
    
    @Override
    public List<ImageMetadataItem> getItems() {
        final ArrayList list = new ArrayList();
        if (this.exif != null) {
            list.addAll(this.exif.getItems());
        }
        if (this.photoshop != null) {
            list.addAll(this.photoshop.getItems());
        }
        return list;
    }
    
    public JpegPhotoshopMetadata getPhotoshop() {
        return this.photoshop;
    }
    
    public TiffImageData getRawImageData() {
        if (this.exif == null) {
            return null;
        }
        final Iterator<? extends ImageMetadataItem> iterator = this.exif.getDirectories().iterator();
        while (iterator.hasNext()) {
            final TiffImageData tiffImageData = ((TiffImageMetadata.Directory)iterator.next()).getTiffImageData();
            if (tiffImageData != null) {
                return tiffImageData;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return this.toString(null);
    }
    
    @Override
    public String toString(final String s) {
        String s2 = s;
        if (s == null) {
            s2 = "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s2);
        if (this.exif == null) {
            sb.append("No Exif metadata.");
        }
        else {
            sb.append("Exif metadata:");
            sb.append(JpegImageMetadata.NEWLINE);
            sb.append(this.exif.toString("\t"));
        }
        sb.append(JpegImageMetadata.NEWLINE);
        sb.append(s2);
        if (this.photoshop == null) {
            sb.append("No Photoshop (IPTC) metadata.");
        }
        else {
            sb.append("Photoshop (IPTC) metadata:");
            sb.append(JpegImageMetadata.NEWLINE);
            sb.append(this.photoshop.toString("\t"));
        }
        return sb.toString();
    }
}
