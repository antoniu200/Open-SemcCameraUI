package org.apache.commons.imaging.formats.tiff.write;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.util.Debug;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class TiffOutputSet {
    private static final String NEWLINE = System.getProperty("line.separator");
    public final ByteOrder byteOrder;
    private final List<TiffOutputDirectory> directories;

    public TiffOutputSet() {
        this(TiffConstants.DEFAULT_TIFF_BYTE_ORDER);
    }

    public TiffOutputSet(ByteOrder byteOrder) {
        this.directories = new ArrayList();
        this.byteOrder = byteOrder;
    }

    protected List<TiffOutputItem> getOutputItems(TiffOutputSummary tiffOutputSummary) throws ImageWriteException {
        ArrayList arrayList = new ArrayList();
        Iterator<TiffOutputDirectory> it = this.directories.iterator();
        while (it.hasNext()) {
            arrayList.addAll(it.next().getOutputItems(tiffOutputSummary));
        }
        return arrayList;
    }

    public void addDirectory(TiffOutputDirectory tiffOutputDirectory) throws ImageWriteException {
        if (findDirectory(tiffOutputDirectory.type) != null) {
            throw new ImageWriteException("Output set already contains a directory of that type.");
        }
        this.directories.add(tiffOutputDirectory);
    }

    public List<TiffOutputDirectory> getDirectories() {
        return new ArrayList(this.directories);
    }

    public TiffOutputDirectory getRootDirectory() {
        return findDirectory(0);
    }

    public TiffOutputDirectory getExifDirectory() {
        return findDirectory(-2);
    }

    public TiffOutputDirectory getOrCreateRootDirectory() throws ImageWriteException {
        TiffOutputDirectory tiffOutputDirectoryFindDirectory = findDirectory(0);
        return tiffOutputDirectoryFindDirectory != null ? tiffOutputDirectoryFindDirectory : addRootDirectory();
    }

    public TiffOutputDirectory getOrCreateExifDirectory() throws ImageWriteException {
        getOrCreateRootDirectory();
        TiffOutputDirectory tiffOutputDirectoryFindDirectory = findDirectory(-2);
        return tiffOutputDirectoryFindDirectory != null ? tiffOutputDirectoryFindDirectory : addExifDirectory();
    }

    public TiffOutputDirectory getOrCreateGPSDirectory() throws ImageWriteException {
        getOrCreateExifDirectory();
        TiffOutputDirectory tiffOutputDirectoryFindDirectory = findDirectory(-3);
        return tiffOutputDirectoryFindDirectory != null ? tiffOutputDirectoryFindDirectory : addGPSDirectory();
    }

    public TiffOutputDirectory getGPSDirectory() {
        return findDirectory(-3);
    }

    public TiffOutputDirectory getInteroperabilityDirectory() {
        return findDirectory(-4);
    }

    public TiffOutputDirectory findDirectory(int i) {
        for (TiffOutputDirectory tiffOutputDirectory : this.directories) {
            if (tiffOutputDirectory.type == i) {
                return tiffOutputDirectory;
            }
        }
        return null;
    }

    public void setGPSInDegrees(double d, double d2) throws ImageWriteException {
        TiffOutputDirectory orCreateGPSDirectory = getOrCreateGPSDirectory();
        orCreateGPSDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_VERSION_ID);
        orCreateGPSDirectory.add(GpsTagConstants.GPS_TAG_GPS_VERSION_ID, GpsTagConstants.GPS_VERSION);
        String str = d < 0.0d ? "W" : "E";
        double dAbs = Math.abs(d);
        String str2 = d2 < 0.0d ? "S" : "N";
        double dAbs2 = Math.abs(d2);
        orCreateGPSDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
        orCreateGPSDirectory.add(GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF, str);
        orCreateGPSDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
        orCreateGPSDirectory.add(GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF, str2);
        orCreateGPSDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
        orCreateGPSDirectory.add(GpsTagConstants.GPS_TAG_GPS_LONGITUDE, RationalNumber.valueOf((long) dAbs), RationalNumber.valueOf((long) r3), RationalNumber.valueOf((((dAbs % 1.0d) * 60.0d) % 1.0d) * 60.0d));
        orCreateGPSDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_LATITUDE);
        orCreateGPSDirectory.add(GpsTagConstants.GPS_TAG_GPS_LATITUDE, RationalNumber.valueOf((long) dAbs2), RationalNumber.valueOf((long) r6), RationalNumber.valueOf((((dAbs2 % 1.0d) * 60.0d) % 1.0d) * 60.0d));
    }

    public void removeField(TagInfo tagInfo) {
        removeField(tagInfo.tag);
    }

    public void removeField(int i) {
        Iterator<TiffOutputDirectory> it = this.directories.iterator();
        while (it.hasNext()) {
            it.next().removeField(i);
        }
    }

    public TiffOutputField findField(TagInfo tagInfo) {
        return findField(tagInfo.tag);
    }

    public TiffOutputField findField(int i) {
        Iterator<TiffOutputDirectory> it = this.directories.iterator();
        while (it.hasNext()) {
            TiffOutputField tiffOutputFieldFindField = it.next().findField(i);
            if (tiffOutputFieldFindField != null) {
                return tiffOutputFieldFindField;
            }
        }
        return null;
    }

    public TiffOutputDirectory addRootDirectory() throws ImageWriteException {
        TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(0, this.byteOrder);
        addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }

    public TiffOutputDirectory addExifDirectory() throws ImageWriteException {
        TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(-2, this.byteOrder);
        addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }

    public TiffOutputDirectory addGPSDirectory() throws ImageWriteException {
        TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(-3, this.byteOrder);
        addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }

    public TiffOutputDirectory addInteroperabilityDirectory() throws ImageWriteException {
        getOrCreateExifDirectory();
        TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(-4, this.byteOrder);
        addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }

    public String toString() {
        return toString(null);
    }

    public String toString(String str) {
        if (str == null) {
            str = "";
        }
        StringBuilder sb = new StringBuilder(39);
        sb.append(str);
        sb.append("TiffOutputSet {");
        sb.append(NEWLINE);
        sb.append(str);
        sb.append("byteOrder: ");
        sb.append(this.byteOrder);
        sb.append(NEWLINE);
        for (int i = 0; i < this.directories.size(); i++) {
            TiffOutputDirectory tiffOutputDirectory = this.directories.get(i);
            sb.append(String.format("%s\tdirectory %d: %s (%d)%n", str, Integer.valueOf(i), tiffOutputDirectory.description(), Integer.valueOf(tiffOutputDirectory.type)));
            for (TiffOutputField tiffOutputField : tiffOutputDirectory.getFields()) {
                sb.append(str);
                sb.append("\t\tfield " + i + ": " + tiffOutputField.tagInfo);
                sb.append(NEWLINE);
            }
        }
        sb.append(str);
        sb.append('}');
        sb.append(NEWLINE);
        return sb.toString();
    }

    public void dump() {
        Debug.debug(toString());
    }
}
