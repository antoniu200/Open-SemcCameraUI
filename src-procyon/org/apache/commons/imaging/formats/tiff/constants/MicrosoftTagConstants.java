// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Collections;
import java.util.Arrays;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoXpString;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.List;

public final class MicrosoftTagConstants
{
    public static final List<TagInfo> ALL_MICROSOFT_TAGS;
    public static final TagInfoShort EXIF_TAG_RATING;
    public static final TagInfoShort EXIF_TAG_RATING_PERCENT;
    public static final TagInfoXpString EXIF_TAG_XPAUTHOR;
    public static final TagInfoXpString EXIF_TAG_XPCOMMENT;
    public static final TagInfoXpString EXIF_TAG_XPKEYWORDS;
    public static final TagInfoXpString EXIF_TAG_XPSUBJECT;
    public static final TagInfoXpString EXIF_TAG_XPTITLE;
    
    static {
        EXIF_TAG_RATING = new TagInfoShort("Rating", 18246, 1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
        EXIF_TAG_RATING_PERCENT = new TagInfoShort("RatingPercent", 18249, 1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
        EXIF_TAG_XPTITLE = new TagInfoXpString("XPTitle", 40091, -1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
        EXIF_TAG_XPCOMMENT = new TagInfoXpString("XPComment", 40092, -1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
        EXIF_TAG_XPAUTHOR = new TagInfoXpString("XPAuthor", 40093, -1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
        EXIF_TAG_XPKEYWORDS = new TagInfoXpString("XPKeywords", 40094, -1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
        EXIF_TAG_XPSUBJECT = new TagInfoXpString("XPSubject", 40095, -1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
        ALL_MICROSOFT_TAGS = Collections.unmodifiableList((List<? extends TagInfo>)Arrays.asList(MicrosoftTagConstants.EXIF_TAG_RATING, MicrosoftTagConstants.EXIF_TAG_RATING_PERCENT, MicrosoftTagConstants.EXIF_TAG_XPTITLE, MicrosoftTagConstants.EXIF_TAG_XPCOMMENT, MicrosoftTagConstants.EXIF_TAG_XPAUTHOR, MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS, MicrosoftTagConstants.EXIF_TAG_XPSUBJECT));
    }
    
    private MicrosoftTagConstants() {
    }
}
