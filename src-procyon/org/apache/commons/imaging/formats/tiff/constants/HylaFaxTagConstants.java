// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Collections;
import java.util.Arrays;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.List;

public final class HylaFaxTagConstants
{
    public static final List<TagInfo> ALL_HYLAFAX_TAGS;
    public static final TagInfoAscii EXIF_TAG_FAX_DCS;
    public static final TagInfoLong EXIF_TAG_FAX_RECV_PARAMS;
    public static final TagInfoLong EXIF_TAG_FAX_RECV_TIME;
    public static final TagInfoAscii EXIF_TAG_FAX_SUB_ADDRESS;
    
    static {
        EXIF_TAG_FAX_RECV_PARAMS = new TagInfoLong("FaxRecvParams", 34908, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_FAX_SUB_ADDRESS = new TagInfoAscii("FaxSubAddress", 34909, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_FAX_RECV_TIME = new TagInfoLong("FaxRecvTime", 34910, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_FAX_DCS = new TagInfoAscii("FaxDCS", 34911, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        ALL_HYLAFAX_TAGS = Collections.unmodifiableList((List<? extends TagInfo>)Arrays.asList(HylaFaxTagConstants.EXIF_TAG_FAX_RECV_PARAMS, HylaFaxTagConstants.EXIF_TAG_FAX_SUB_ADDRESS, HylaFaxTagConstants.EXIF_TAG_FAX_RECV_TIME, HylaFaxTagConstants.EXIF_TAG_FAX_DCS));
    }
    
    private HylaFaxTagConstants() {
    }
}
