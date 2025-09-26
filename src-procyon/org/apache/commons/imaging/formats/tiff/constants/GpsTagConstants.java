// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Collections;
import java.util.Arrays;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoGpsText;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.List;

public final class GpsTagConstants
{
    public static final List<TagInfo> ALL_GPS_TAGS;
    public static final TagInfoRational GPS_TAG_GPS_ALTITUDE;
    public static final TagInfoByte GPS_TAG_GPS_ALTITUDE_REF;
    public static final int GPS_TAG_GPS_ALTITUDE_REF_VALUE_ABOVE_SEA_LEVEL = 0;
    public static final int GPS_TAG_GPS_ALTITUDE_REF_VALUE_BELOW_SEA_LEVEL = 1;
    public static final TagInfoGpsText GPS_TAG_GPS_AREA_INFORMATION;
    public static final TagInfoAscii GPS_TAG_GPS_DATE_STAMP;
    public static final TagInfoRational GPS_TAG_GPS_DEST_BEARING;
    public static final TagInfoAscii GPS_TAG_GPS_DEST_BEARING_REF;
    public static final String GPS_TAG_GPS_DEST_BEARING_REF_VALUE_MAGNETIC_NORTH = "M";
    public static final String GPS_TAG_GPS_DEST_BEARING_REF_VALUE_TRUE_NORTH = "T";
    public static final TagInfoRational GPS_TAG_GPS_DEST_DISTANCE;
    public static final TagInfoAscii GPS_TAG_GPS_DEST_DISTANCE_REF;
    public static final String GPS_TAG_GPS_DEST_DISTANCE_REF_VALUE_KILOMETERS = "K";
    public static final String GPS_TAG_GPS_DEST_DISTANCE_REF_VALUE_MILES = "M";
    public static final String GPS_TAG_GPS_DEST_DISTANCE_REF_VALUE_NAUTICAL_MILES = "N";
    public static final TagInfoRational GPS_TAG_GPS_DEST_LATITUDE;
    public static final TagInfoAscii GPS_TAG_GPS_DEST_LATITUDE_REF;
    public static final String GPS_TAG_GPS_DEST_LATITUDE_REF_VALUE_NORTH = "N";
    public static final String GPS_TAG_GPS_DEST_LATITUDE_REF_VALUE_SOUTH = "S";
    public static final TagInfoRational GPS_TAG_GPS_DEST_LONGITUDE;
    public static final TagInfoAscii GPS_TAG_GPS_DEST_LONGITUDE_REF;
    public static final String GPS_TAG_GPS_DEST_LONGITUDE_REF_VALUE_EAST = "E";
    public static final String GPS_TAG_GPS_DEST_LONGITUDE_REF_VALUE_WEST = "W";
    public static final TagInfoShort GPS_TAG_GPS_DIFFERENTIAL;
    public static final int GPS_TAG_GPS_DIFFERENTIAL_VALUE_DIFFERENTIAL_CORRECTED = 1;
    public static final int GPS_TAG_GPS_DIFFERENTIAL_VALUE_NO_CORRECTION = 0;
    public static final TagInfoRational GPS_TAG_GPS_DOP;
    public static final TagInfoRational GPS_TAG_GPS_IMG_DIRECTION;
    public static final TagInfoAscii GPS_TAG_GPS_IMG_DIRECTION_REF;
    public static final String GPS_TAG_GPS_IMG_DIRECTION_REF_VALUE_MAGNETIC_NORTH = "M";
    public static final String GPS_TAG_GPS_IMG_DIRECTION_REF_VALUE_TRUE_NORTH = "T";
    public static final TagInfoRational GPS_TAG_GPS_LATITUDE;
    public static final TagInfoAscii GPS_TAG_GPS_LATITUDE_REF;
    public static final String GPS_TAG_GPS_LATITUDE_REF_VALUE_NORTH = "N";
    public static final String GPS_TAG_GPS_LATITUDE_REF_VALUE_SOUTH = "S";
    public static final TagInfoRational GPS_TAG_GPS_LONGITUDE;
    public static final TagInfoAscii GPS_TAG_GPS_LONGITUDE_REF;
    public static final String GPS_TAG_GPS_LONGITUDE_REF_VALUE_EAST = "E";
    public static final String GPS_TAG_GPS_LONGITUDE_REF_VALUE_WEST = "W";
    public static final TagInfoAscii GPS_TAG_GPS_MAP_DATUM;
    public static final TagInfoAscii GPS_TAG_GPS_MEASURE_MODE;
    public static final int GPS_TAG_GPS_MEASURE_MODE_VALUE_2_DIMENSIONAL_MEASUREMENT = 2;
    public static final int GPS_TAG_GPS_MEASURE_MODE_VALUE_3_DIMENSIONAL_MEASUREMENT = 3;
    public static final TagInfoGpsText GPS_TAG_GPS_PROCESSING_METHOD;
    public static final TagInfoAscii GPS_TAG_GPS_SATELLITES;
    public static final TagInfoRational GPS_TAG_GPS_SPEED;
    public static final TagInfoAscii GPS_TAG_GPS_SPEED_REF;
    public static final String GPS_TAG_GPS_SPEED_REF_VALUE_KMPH = "K";
    public static final String GPS_TAG_GPS_SPEED_REF_VALUE_KNOTS = "N";
    public static final String GPS_TAG_GPS_SPEED_REF_VALUE_MPH = "M";
    public static final TagInfoAscii GPS_TAG_GPS_STATUS;
    public static final String GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_INTEROPERABILITY = "V";
    public static final String GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_IN_PROGRESS = "A";
    public static final TagInfoRational GPS_TAG_GPS_TIME_STAMP;
    public static final TagInfoRational GPS_TAG_GPS_TRACK;
    public static final TagInfoAscii GPS_TAG_GPS_TRACK_REF;
    public static final String GPS_TAG_GPS_TRACK_REF_VALUE_MAGNETIC_NORTH = "M";
    public static final String GPS_TAG_GPS_TRACK_REF_VALUE_TRUE_NORTH = "T";
    public static final TagInfoByte GPS_TAG_GPS_VERSION_ID;
    public static final byte[] GPS_VERSION;
    
    static {
        GPS_TAG_GPS_VERSION_ID = new TagInfoByte("GPSVersionID", 0, 4, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_VERSION = new byte[] { 2, 3, 0, 0 };
        GPS_TAG_GPS_LATITUDE_REF = new TagInfoAscii("GPSLatitudeRef", 1, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_LATITUDE = new TagInfoRational("GPSLatitude", 2, 3, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_LONGITUDE_REF = new TagInfoAscii("GPSLongitudeRef", 3, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_LONGITUDE = new TagInfoRational("GPSLongitude", 4, 3, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_ALTITUDE_REF = new TagInfoByte("GPSAltitudeRef", 5, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_ALTITUDE = new TagInfoRational("GPSAltitude", 6, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_TIME_STAMP = new TagInfoRational("GPSTimeStamp", 7, 3, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_SATELLITES = new TagInfoAscii("GPSSatellites", 8, -1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_STATUS = new TagInfoAscii("GPSStatus", 9, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_MEASURE_MODE = new TagInfoAscii("GPSMeasureMode", 10, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DOP = new TagInfoRational("GPSDOP", 11, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_SPEED_REF = new TagInfoAscii("GPSSpeedRef", 12, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_SPEED = new TagInfoRational("GPSSpeed", 13, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_TRACK_REF = new TagInfoAscii("GPSTrackRef", 14, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_TRACK = new TagInfoRational("GPSTrack", 15, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_IMG_DIRECTION_REF = new TagInfoAscii("GPSImgDirectionRef", 16, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_IMG_DIRECTION = new TagInfoRational("GPSImgDirection", 17, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_MAP_DATUM = new TagInfoAscii("GPSMapDatum", 18, -1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_LATITUDE_REF = new TagInfoAscii("GPSDestLatitudeRef", 19, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_LATITUDE = new TagInfoRational("GPSDestLatitude", 20, 3, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_LONGITUDE_REF = new TagInfoAscii("GPSDestLongitudeRef", 21, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_LONGITUDE = new TagInfoRational("GPSDestLongitude", 22, 3, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_BEARING_REF = new TagInfoAscii("GPSDestBearingRef", 23, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_BEARING = new TagInfoRational("GPSDestBearing", 24, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_DISTANCE_REF = new TagInfoAscii("GPSDestDistanceRef", 25, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_DISTANCE = new TagInfoRational("GPSDestDistance", 26, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_PROCESSING_METHOD = new TagInfoGpsText("GPSProcessingMethod", 27, -1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_AREA_INFORMATION = new TagInfoGpsText("GPSAreaInformation", 28, -1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DATE_STAMP = new TagInfoAscii("GPSDateStamp", 29, 11, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DIFFERENTIAL = new TagInfoShort("GPSDifferential", 30, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
        ALL_GPS_TAGS = Collections.unmodifiableList((List<? extends TagInfo>)Arrays.asList(GpsTagConstants.GPS_TAG_GPS_VERSION_ID, GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF, GpsTagConstants.GPS_TAG_GPS_LATITUDE, GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF, GpsTagConstants.GPS_TAG_GPS_LONGITUDE, GpsTagConstants.GPS_TAG_GPS_ALTITUDE_REF, GpsTagConstants.GPS_TAG_GPS_ALTITUDE, GpsTagConstants.GPS_TAG_GPS_TIME_STAMP, GpsTagConstants.GPS_TAG_GPS_SATELLITES, GpsTagConstants.GPS_TAG_GPS_STATUS, GpsTagConstants.GPS_TAG_GPS_MEASURE_MODE, GpsTagConstants.GPS_TAG_GPS_DOP, GpsTagConstants.GPS_TAG_GPS_SPEED_REF, GpsTagConstants.GPS_TAG_GPS_SPEED, GpsTagConstants.GPS_TAG_GPS_TRACK_REF, GpsTagConstants.GPS_TAG_GPS_TRACK, GpsTagConstants.GPS_TAG_GPS_IMG_DIRECTION_REF, GpsTagConstants.GPS_TAG_GPS_IMG_DIRECTION, GpsTagConstants.GPS_TAG_GPS_MAP_DATUM, GpsTagConstants.GPS_TAG_GPS_DEST_LATITUDE_REF, GpsTagConstants.GPS_TAG_GPS_DEST_LATITUDE, GpsTagConstants.GPS_TAG_GPS_DEST_LONGITUDE_REF, GpsTagConstants.GPS_TAG_GPS_DEST_LONGITUDE, GpsTagConstants.GPS_TAG_GPS_DEST_BEARING_REF, GpsTagConstants.GPS_TAG_GPS_DEST_BEARING, GpsTagConstants.GPS_TAG_GPS_DEST_DISTANCE_REF, GpsTagConstants.GPS_TAG_GPS_DEST_DISTANCE, GpsTagConstants.GPS_TAG_GPS_PROCESSING_METHOD, GpsTagConstants.GPS_TAG_GPS_AREA_INFORMATION, GpsTagConstants.GPS_TAG_GPS_DATE_STAMP, GpsTagConstants.GPS_TAG_GPS_DIFFERENTIAL));
    }
    
    private GpsTagConstants() {
    }
}
