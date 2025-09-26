// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.mediasaving;

import com.sonyericsson.android.camera.util.CamLog;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;
import java.nio.charset.Charset;
import android.location.Location;

public class ExifFactory
{
    private static byte[] APP1_HEADER;
    private static int APP1_LENGTH = 0;
    private static byte[] EXIF_IFD;
    private static byte[] FIRST_IFD;
    private static byte[] GPS_IFD;
    private static final int MAKER_NAME_LIMITATION = 14;
    public static final String TAG = "ExifFactory";
    private static byte[] TIFF_HEADER;
    private static byte[] ZERO_IFD;
    private static byte[] ZERO_IFD_INT;
    
    static {
        ExifFactory.APP1_HEADER = new byte[] { -1, -31, 3, 27, 69, 120, 105, 102, 0, 0 };
        ExifFactory.TIFF_HEADER = new byte[] { 77, 77, 0, 42, 0, 0, 0, 8 };
        ExifFactory.ZERO_IFD = new byte[] { 0, 10, 1, 15, 0, 2, 0, 0, 0, 0, 0, 0, 0, -122, 1, 16, 0, 2, 0, 0, 0, 30, 0, 0, 0, -108, 1, 18, 0, 3, 0, 0, 0, 1, 0, 6, 0, 0, 1, 26, 0, 5, 0, 0, 0, 1, 0, 0, 0, -78, 1, 27, 0, 5, 0, 0, 0, 1, 0, 0, 0, -70, 1, 40, 0, 3, 0, 0, 0, 1, 0, 2, 0, 0, 1, 50, 0, 2, 0, 0, 0, 20, 0, 0, 0, -62, 2, 19, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, -121, 105, 0, 4, 0, 0, 0, 1, 0, 0, 0, -42, -120, 37, 0, 4, 0, 0, 0, 1, 0, 0, 1, -102, 0, 0, 2, -86, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 72, 0, 0, 0, 1, 0, 0, 0, 72, 0, 0, 0, 1, 50, 48, 49, 49, 58, 48, 49, 58, 50, 51, 32, 49, 50, 58, 51, 52, 58, 53, 54, 0 };
        ExifFactory.EXIF_IFD = new byte[] { 0, 9, -112, 0, 0, 7, 0, 0, 0, 4, 48, 50, 50, 48, -112, 3, 0, 2, 0, 0, 0, 20, 0, 0, 1, 84, -112, 4, 0, 2, 0, 0, 0, 20, 0, 0, 1, 104, -111, 1, 0, 7, 0, 0, 0, 4, 1, 2, 3, 0, -96, 0, 0, 7, 0, 0, 0, 4, 48, 49, 48, 48, -96, 1, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, -96, 2, 0, 4, 0, 0, 0, 1, 0, 0, 12, -64, -96, 3, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, -96, 5, 0, 4, 0, 0, 0, 1, 0, 0, 1, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 48, 49, 49, 58, 48, 49, 58, 50, 51, 32, 49, 50, 58, 51, 52, 58, 53, 54, 0, 50, 48, 49, 49, 58, 48, 49, 58, 50, 51, 32, 49, 50, 58, 51, 52, 58, 53, 54, 0 };
        ExifFactory.ZERO_IFD_INT = new byte[] { 0, 2, 0, 1, 0, 2, 0, 0, 0, 4, 82, 57, 56, 0, 0, 2, 0, 7, 0, 0, 0, 4, 48, 49, 48, 48, 0, 0, 0, 0 };
        ExifFactory.GPS_IFD = new byte[] { 0, 12, 0, 0, 0, 1, 0, 0, 0, 4, 2, 2, 0, 0, 0, 1, 0, 2, 0, 0, 0, 2, 78, 0, 0, 0, 0, 2, 0, 5, 0, 0, 0, 3, 0, 0, 2, 48, 0, 3, 0, 2, 0, 0, 0, 2, 69, 0, 0, 0, 0, 4, 0, 5, 0, 0, 0, 3, 0, 0, 2, 72, 0, 5, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 6, 0, 5, 0, 0, 0, 1, 0, 0, 2, 96, 0, 7, 0, 5, 0, 0, 0, 3, 0, 0, 2, 104, 0, 9, 0, 2, 0, 0, 0, 2, 65, 0, 0, 0, 0, 18, 0, 2, 0, 0, 0, 7, 0, 0, 2, -128, 0, 27, 0, 7, 0, 0, 0, 0, 0, 0, 2, -118, 0, 29, 0, 2, 0, 0, 0, 11, 0, 0, 2, -98, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 87, 71, 83, 45, 56, 52, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 48, 49, 49, 58, 48, 49, 58, 50, 51, 0, 0 };
        ExifFactory.FIRST_IFD = new byte[] { 0, 7, 1, 3, 0, 3, 0, 0, 0, 1, 0, 6, 0, 0, 1, 18, 0, 3, 0, 0, 0, 1, 0, 6, 0, 0, 1, 26, 0, 5, 0, 0, 0, 1, 0, 0, 3, 4, 1, 27, 0, 5, 0, 0, 0, 1, 0, 0, 3, 12, 1, 40, 0, 3, 0, 0, 0, 1, 0, 2, 0, 0, 2, 1, 0, 4, 0, 0, 0, 1, 0, 0, 3, 20, 2, 2, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 72, 0, 0, 0, 1, 0, 0, 0, 72, 0, 0, 0, 1 };
        ExifFactory.APP1_LENGTH = ExifFactory.APP1_HEADER.length + ExifFactory.TIFF_HEADER.length + ExifFactory.ZERO_IFD.length + ExifFactory.EXIF_IFD.length + ExifFactory.ZERO_IFD_INT.length + ExifFactory.GPS_IFD.length + ExifFactory.FIRST_IFD.length;
    }
    
    private static void checkArguments(final byte[] array, final ExifOption exifOption) {
        if (array == null || exifOption == null || exifOption.mModel == null || exifOption.mDateTime == null || exifOption.mThumbnailData == null) {
            throw new IllegalArgumentException("can not null");
        }
        if (exifOption.mGPSOption != null && !exifOption.mGPSOption.hasAltitude()) {
            exifOption.mGPSOption.setAltitude(0.0);
        }
        if (exifOption.mThumbnailData.length < exifOption.mThumbnailDataLength) {
            throw new IllegalArgumentException("thumbnail data length too big");
        }
        if (array.length < ExifFactory.APP1_LENGTH + exifOption.mThumbnailDataLength) {
            throw new IllegalArgumentException("buffer too short");
        }
        if (exifOption.mModel.length() == 0 || exifOption.mDateTime.length() < "YYYY:MM:DD hh:mm:ss".length()) {
            throw new IllegalArgumentException("model or datetime too short");
        }
        if (exifOption.mModel.length() < 30 && exifOption.mDateTime.length() <= "YYYY:MM:DD hh:mm:ss".length()) {
            return;
        }
        throw new IllegalArgumentException("model or datetime too long");
    }
    
    private static void fillNullValue(final byte[] array, final int n, final int n2) {
        for (int i = 0; i < n2; ++i) {
            array[n + i] = 0;
        }
    }
    
    public static int generate(final byte[] array, final ExifOption exifOption) {
        checkArguments(array, exifOption);
        final int writeTemplate = writeTemplate(array);
        updateMake(array, exifOption.mMake);
        updateModel(array, exifOption.mModel);
        updateOrientation(array, exifOption.mOrientation);
        updateDateTime(array, exifOption.mDateTime);
        updatePixelXDimension(array, exifOption.mPixelXDimension);
        updatePixelYDimension(array, exifOption.mPixelYDimension);
        updateGpsFields(array, exifOption.mGPSOption);
        updateJpegInterchangeFormatLength(array, exifOption.mThumbnailDataLength);
        System.arraycopy(exifOption.mThumbnailData, 0, array, writeTemplate, (int)exifOption.mThumbnailDataLength);
        final int n = (int)(writeTemplate + exifOption.mThumbnailDataLength);
        updateExifSize(array, n - 2);
        return n;
    }
    
    public static int getLength() {
        return ExifFactory.APP1_LENGTH;
    }
    
    private static void removeGpsInfoFromHeader(final byte[] array) {
        writeShortValue(array, ExifFactory.APP1_HEADER.length + 8, 9);
        fillNullValue(array, ExifFactory.APP1_HEADER.length + 118, 11);
        writeLongValue(array, ExifFactory.APP1_HEADER.length + 118, 682L);
        fillNullValue(array, ExifFactory.APP1_HEADER.length + 410, 272);
    }
    
    private static void updateDateTime(final byte[] array, final String s) {
        writeASCIIValue(array, ExifFactory.APP1_HEADER.length + 194, s);
        writeASCIIValue(array, ExifFactory.APP1_HEADER.length + 340, s);
        writeASCIIValue(array, ExifFactory.APP1_HEADER.length + 360, s);
    }
    
    private static void updateExifSize(final byte[] array, final int n) {
        writeShortValue(array, ExifFactory.APP1_HEADER.length - 8, n);
    }
    
    private static void updateGpsFields(final byte[] array, final Location location) {
        if (location != null && writeGpsInfoToHeader(array, location)) {
            return;
        }
        removeGpsInfoFromHeader(array);
    }
    
    private static void updateJpegInterchangeFormatLength(final byte[] array, final long n) {
        writeLongValue(array, ExifFactory.APP1_HEADER.length + 764, n);
    }
    
    private static void updateMake(final byte[] array, final String s) {
        String substring = s;
        if (s.length() > 14) {
            substring = s.substring(0, 14);
        }
        writeShortValue(array, ExifFactory.APP1_HEADER.length + 16, writeASCIIValue(array, ExifFactory.APP1_HEADER.length + 134, substring) + 1);
    }
    
    private static void updateModel(final byte[] array, final String s) {
        writeLongValue(array, ExifFactory.APP1_HEADER.length + 26, writeASCIIValue(array, ExifFactory.APP1_HEADER.length + 148, s) + 1);
    }
    
    private static void updateOrientation(final byte[] array, final int n) {
        writeShortValue(array, ExifFactory.APP1_HEADER.length + 42, n);
        writeShortValue(array, ExifFactory.APP1_HEADER.length + 704, n);
    }
    
    private static void updatePixelXDimension(final byte[] array, final long n) {
        writeLongValue(array, ExifFactory.APP1_HEADER.length + 308 - 12, n);
    }
    
    private static void updatePixelYDimension(final byte[] array, final long n) {
        writeLongValue(array, ExifFactory.APP1_HEADER.length + 320 - 12, n);
    }
    
    private static int writeASCIIValue(final byte[] array, final int n, final String s) {
        final byte[] bytes = s.getBytes(Charset.forName("US-ASCII"));
        System.arraycopy(bytes, 0, array, n, bytes.length);
        return bytes.length;
    }
    
    private static void writeByteValue(final byte[] array, final int n, final int n2) {
        array[n + 0] = (byte)n2;
    }
    
    private static boolean writeGpsInfoToHeader(final byte[] array, final Location location) {
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(location.getTime());
        double latitude;
        final double n = latitude = location.getLatitude();
        if (n < 0.0) {
            writeASCIIValue(array, ExifFactory.APP1_HEADER.length + 432, "S");
            latitude = -n;
        }
        try {
            final String[] split = Location.convert(latitude, 2).split(":");
            try {
                writeRationalValue(array, ExifFactory.APP1_HEADER.length + 560, Long.parseLong(split[0]), 1L);
                writeRationalValue(array, ExifFactory.APP1_HEADER.length + 568, Long.parseLong(split[1]), 1L);
                writeRationalValue(array, ExifFactory.APP1_HEADER.length + 576, (long)(Float.parseFloat(split[2]) * 1000.0f), 1000L);
                double longitude;
                final double n2 = longitude = location.getLongitude();
                if (n2 < 0.0) {
                    writeASCIIValue(array, ExifFactory.APP1_HEADER.length + 456, "W");
                    longitude = -n2;
                }
                try {
                    final String[] split2 = Location.convert(longitude, 2).split(":");
                    try {
                        writeRationalValue(array, ExifFactory.APP1_HEADER.length + 584, Long.parseLong(split2[0]), 1L);
                        writeRationalValue(array, ExifFactory.APP1_HEADER.length + 592, Long.parseLong(split2[1]), 1L);
                        writeRationalValue(array, ExifFactory.APP1_HEADER.length + 600, (long)(Float.parseFloat(split2[2]) * 1000.0f), 1000L);
                        final double altitude = location.getAltitude();
                        if (altitude < 0.0) {
                            writeByteValue(array, ExifFactory.APP1_HEADER.length + 480, 1);
                        }
                        writeRationalValue(array, ExifFactory.APP1_HEADER.length + 608, (long)(altitude * 1000.0), 1000L);
                        instance.setTimeZone(TimeZone.getTimeZone("UTC"));
                        try {
                            writeRationalValue(array, ExifFactory.APP1_HEADER.length + 616, instance.get(11), 1L);
                            writeRationalValue(array, ExifFactory.APP1_HEADER.length + 624, instance.get(12) + 1, 1L);
                            writeRationalValue(array, ExifFactory.APP1_HEADER.length + 632, instance.get(13) * 1000L, 1000L);
                            writeASCIIValue(array, ExifFactory.APP1_HEADER.length + 670, String.format(Locale.US, "%04d:%02d:%02d", instance.get(1), instance.get(2) + 1, instance.get(5)));
                            return true;
                        }
                        catch (final IllegalArgumentException ex) {
                            if (CamLog.VERBOSE) {
                                CamLog.d("failed to get gpsDateStamp");
                            }
                            return false;
                        }
                    }
                    catch (final NumberFormatException ex2) {
                        if (CamLog.VERBOSE) {
                            CamLog.d("failed to get longitude value");
                        }
                        return false;
                    }
                }
                catch (final IllegalArgumentException ex3) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("failed to change longitude format");
                    }
                    return false;
                }
            }
            catch (final NumberFormatException ex4) {
                if (CamLog.VERBOSE) {
                    CamLog.d("failed to get latitude value");
                }
                return false;
            }
        }
        catch (final IllegalArgumentException ex5) {
            if (CamLog.VERBOSE) {
                CamLog.d("failed to change latitude format");
            }
            return false;
        }
    }
    
    private static void writeLongValue(final byte[] array, final int n, final long n2) {
        array[n + 0] = (byte)(n2 / 16777216L);
        array[n + 1] = (byte)(n2 / 65536L);
        array[n + 2] = (byte)(n2 / 256L);
        array[n + 3] = (byte)(n2 % 256L);
    }
    
    private static void writeRationalValue(final byte[] array, final int n, final long n2, final long n3) {
        writeLongValue(array, n + 0, n2);
        writeLongValue(array, n + 4, n3);
    }
    
    private static void writeShortValue(final byte[] array, final int n, final int n2) {
        array[n + 0] = (byte)(n2 / 256);
        array[n + 1] = (byte)(n2 % 256);
    }
    
    private static int writeTemplate(final byte[] array) {
        System.arraycopy(ExifFactory.APP1_HEADER, 0, array, 0, ExifFactory.APP1_HEADER.length);
        final int n = ExifFactory.APP1_HEADER.length + 0;
        System.arraycopy(ExifFactory.TIFF_HEADER, 0, array, n, ExifFactory.TIFF_HEADER.length);
        final int n2 = n + ExifFactory.TIFF_HEADER.length;
        System.arraycopy(ExifFactory.ZERO_IFD, 0, array, n2, ExifFactory.ZERO_IFD.length);
        final int n3 = n2 + ExifFactory.ZERO_IFD.length;
        System.arraycopy(ExifFactory.EXIF_IFD, 0, array, n3, ExifFactory.EXIF_IFD.length);
        final int n4 = n3 + ExifFactory.EXIF_IFD.length;
        System.arraycopy(ExifFactory.ZERO_IFD_INT, 0, array, n4, ExifFactory.ZERO_IFD_INT.length);
        final int n5 = n4 + ExifFactory.ZERO_IFD_INT.length;
        System.arraycopy(ExifFactory.GPS_IFD, 0, array, n5, ExifFactory.GPS_IFD.length);
        final int n6 = n5 + ExifFactory.GPS_IFD.length;
        System.arraycopy(ExifFactory.FIRST_IFD, 0, array, n6, ExifFactory.FIRST_IFD.length);
        return n6 + ExifFactory.FIRST_IFD.length;
    }
}
