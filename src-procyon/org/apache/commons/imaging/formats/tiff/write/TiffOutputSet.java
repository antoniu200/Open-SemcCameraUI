// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.write;

import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import java.util.Collection;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.Iterator;
import org.apache.commons.imaging.util.Debug;
import org.apache.commons.imaging.ImageWriteException;
import java.util.ArrayList;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import java.util.List;
import java.nio.ByteOrder;

public final class TiffOutputSet
{
    private static final String NEWLINE;
    public final ByteOrder byteOrder;
    private final List<TiffOutputDirectory> directories;
    
    static {
        NEWLINE = System.getProperty("line.separator");
    }
    
    public TiffOutputSet() {
        this(TiffConstants.DEFAULT_TIFF_BYTE_ORDER);
    }
    
    public TiffOutputSet(final ByteOrder byteOrder) {
        this.directories = new ArrayList<TiffOutputDirectory>();
        this.byteOrder = byteOrder;
    }
    
    public void addDirectory(final TiffOutputDirectory tiffOutputDirectory) throws ImageWriteException {
        if (this.findDirectory(tiffOutputDirectory.type) != null) {
            throw new ImageWriteException("Output set already contains a directory of that type.");
        }
        this.directories.add(tiffOutputDirectory);
    }
    
    public TiffOutputDirectory addExifDirectory() throws ImageWriteException {
        final TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(-2, this.byteOrder);
        this.addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }
    
    public TiffOutputDirectory addGPSDirectory() throws ImageWriteException {
        final TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(-3, this.byteOrder);
        this.addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }
    
    public TiffOutputDirectory addInteroperabilityDirectory() throws ImageWriteException {
        this.getOrCreateExifDirectory();
        final TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(-4, this.byteOrder);
        this.addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }
    
    public TiffOutputDirectory addRootDirectory() throws ImageWriteException {
        final TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(0, this.byteOrder);
        this.addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }
    
    public void dump() {
        Debug.debug(this.toString());
    }
    
    public TiffOutputDirectory findDirectory(final int n) {
        for (final TiffOutputDirectory tiffOutputDirectory : this.directories) {
            if (tiffOutputDirectory.type == n) {
                return tiffOutputDirectory;
            }
        }
        return null;
    }
    
    public TiffOutputField findField(final int n) {
        final Iterator<TiffOutputDirectory> iterator = this.directories.iterator();
        while (iterator.hasNext()) {
            final TiffOutputField field = iterator.next().findField(n);
            if (field != null) {
                return field;
            }
        }
        return null;
    }
    
    public TiffOutputField findField(final TagInfo tagInfo) {
        return this.findField(tagInfo.tag);
    }
    
    public List<TiffOutputDirectory> getDirectories() {
        return new ArrayList<TiffOutputDirectory>(this.directories);
    }
    
    public TiffOutputDirectory getExifDirectory() {
        return this.findDirectory(-2);
    }
    
    public TiffOutputDirectory getGPSDirectory() {
        return this.findDirectory(-3);
    }
    
    public TiffOutputDirectory getInteroperabilityDirectory() {
        return this.findDirectory(-4);
    }
    
    public TiffOutputDirectory getOrCreateExifDirectory() throws ImageWriteException {
        this.getOrCreateRootDirectory();
        final TiffOutputDirectory directory = this.findDirectory(-2);
        if (directory != null) {
            return directory;
        }
        return this.addExifDirectory();
    }
    
    public TiffOutputDirectory getOrCreateGPSDirectory() throws ImageWriteException {
        this.getOrCreateExifDirectory();
        final TiffOutputDirectory directory = this.findDirectory(-3);
        if (directory != null) {
            return directory;
        }
        return this.addGPSDirectory();
    }
    
    public TiffOutputDirectory getOrCreateRootDirectory() throws ImageWriteException {
        final TiffOutputDirectory directory = this.findDirectory(0);
        if (directory != null) {
            return directory;
        }
        return this.addRootDirectory();
    }
    
    protected List<TiffOutputItem> getOutputItems(final TiffOutputSummary tiffOutputSummary) throws ImageWriteException {
        final ArrayList list = new ArrayList();
        final Iterator<TiffOutputDirectory> iterator = this.directories.iterator();
        while (iterator.hasNext()) {
            list.addAll(iterator.next().getOutputItems(tiffOutputSummary));
        }
        return list;
    }
    
    public TiffOutputDirectory getRootDirectory() {
        return this.findDirectory(0);
    }
    
    public void removeField(final int n) {
        final Iterator<TiffOutputDirectory> iterator = this.directories.iterator();
        while (iterator.hasNext()) {
            iterator.next().removeField(n);
        }
    }
    
    public void removeField(final TagInfo tagInfo) {
        this.removeField(tagInfo.tag);
    }
    
    public void setGPSInDegrees(double abs, double a) throws ImageWriteException {
        final TiffOutputDirectory orCreateGPSDirectory = this.getOrCreateGPSDirectory();
        orCreateGPSDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_VERSION_ID);
        orCreateGPSDirectory.add(GpsTagConstants.GPS_TAG_GPS_VERSION_ID, GpsTagConstants.GPS_VERSION);
        String s;
        if (abs < 0.0) {
            s = "W";
        }
        else {
            s = "E";
        }
        final double abs2 = Math.abs(abs);
        String s2;
        if (a < 0.0) {
            s2 = "S";
        }
        else {
            s2 = "N";
        }
        abs = Math.abs(a);
        orCreateGPSDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
        orCreateGPSDirectory.add(GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF, s);
        orCreateGPSDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
        orCreateGPSDirectory.add(GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF, s2);
        a = (double)(long)abs2;
        final double n = abs2 % 1.0 * 60.0;
        final double n2 = (double)(long)n;
        orCreateGPSDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
        orCreateGPSDirectory.add(GpsTagConstants.GPS_TAG_GPS_LONGITUDE, RationalNumber.valueOf(a), RationalNumber.valueOf(n2), RationalNumber.valueOf(n % 1.0 * 60.0));
        a = (double)(long)abs;
        final double n3 = abs % 1.0 * 60.0;
        abs = (double)(long)n3;
        orCreateGPSDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_LATITUDE);
        orCreateGPSDirectory.add(GpsTagConstants.GPS_TAG_GPS_LATITUDE, RationalNumber.valueOf(a), RationalNumber.valueOf(abs), RationalNumber.valueOf(n3 % 1.0 * 60.0));
    }
    
    @Override
    public String toString() {
        return this.toString(null);
    }
    
    public String toString(final String s) {
        String s2 = s;
        if (s == null) {
            s2 = "";
        }
        final StringBuilder sb = new StringBuilder(39);
        sb.append(s2);
        sb.append("TiffOutputSet {");
        sb.append(TiffOutputSet.NEWLINE);
        sb.append(s2);
        sb.append("byteOrder: ");
        sb.append(this.byteOrder);
        sb.append(TiffOutputSet.NEWLINE);
        for (int i = 0; i < this.directories.size(); ++i) {
            final TiffOutputDirectory tiffOutputDirectory = this.directories.get(i);
            sb.append(String.format("%s\tdirectory %d: %s (%d)%n", s2, i, tiffOutputDirectory.description(), tiffOutputDirectory.type));
            for (final TiffOutputField tiffOutputField : tiffOutputDirectory.getFields()) {
                sb.append(s2);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("\t\tfield ");
                sb2.append(i);
                sb2.append(": ");
                sb2.append(tiffOutputField.tagInfo);
                sb.append(sb2.toString());
                sb.append(TiffOutputSet.NEWLINE);
            }
        }
        sb.append(s2);
        sb.append('}');
        sb.append(TiffOutputSet.NEWLINE);
        return sb.toString();
    }
}
