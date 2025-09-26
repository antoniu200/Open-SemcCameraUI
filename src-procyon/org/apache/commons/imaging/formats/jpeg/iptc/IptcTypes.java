// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.iptc;

public enum IptcTypes implements IptcType
{
    private static final IptcTypes[] $VALUES;
    
    ACTION_ADVISED(42, "Action Advised"), 
    AUDIO_DURATION(153, "Audio Duration"), 
    AUDIO_OUTCUE(154, "Audio Outcue"), 
    AUDIO_SAMPLING_RATE(151, "Audio Sampling Rate"), 
    AUDIO_SAMPLING_RESOLUTION(152, "Audio Sampling Resolution"), 
    AUDIO_TYPE(150, "Audio Type"), 
    BYLINE(80, "By-line"), 
    BYLINE_TITLE(85, "By-line Title"), 
    CAPTION_ABSTRACT(120, "Caption/Abstract"), 
    CATEGORY(15, "Category"), 
    CITY(90, "City"), 
    CONTACT(118, "Contact"), 
    CONTENT_LOCATION_CODE(26, "Content Location Code"), 
    CONTENT_LOCATION_NAME(27, "Content Location Name"), 
    COPYRIGHT_NOTICE(116, "Copyright Notice"), 
    COUNTRY_PRIMARY_LOCATION_CODE(100, "Country/Primary Location Code"), 
    COUNTRY_PRIMARY_LOCATION_NAME(101, "Country/Primary Location Name"), 
    CREDIT(110, "Credit"), 
    DATE_CREATED(55, "Date Created"), 
    DIGITAL_CREATION_DATE(62, "Digital Creation Date"), 
    DIGITAL_CREATION_TIME(63, "Digital Creation Time"), 
    EDITORIAL_UPDATE(8, "Editorial Update"), 
    EDIT_STATUS(7, "Edit Status"), 
    EXPIRATION_DATE(37, "Expiration Date"), 
    EXPIRATION_TIME(38, "Expiration Time"), 
    FIXTURE_IDENTIFIER(22, "Fixture Identifier"), 
    HEADLINE(105, "Headline"), 
    IMAGE_ORIENTATION(131, "Image Orientation"), 
    IMAGE_TYPE(130, "ImageType"), 
    KEYWORDS(25, "Keywords"), 
    LANGUAGE_IDENTIFIER(135, "Language Identifier"), 
    OBJECT_ATTRIBUTE_REFERENCE(4, "Object Attribute Reference"), 
    OBJECT_CYCLE(75, "Object Cycle"), 
    OBJECT_DATA_PREVIEW_DATA(202, "Object Data Preview Data"), 
    OBJECT_DATA_PREVIEW_FILE_FORMAT(200, "Object Data Preview, File Format"), 
    OBJECT_DATA_PREVIEW_FILE_FORMAT_VERSION(201, "Object Data Preview, File Format Version"), 
    OBJECT_NAME(5, "Object Name"), 
    OBJECT_TYPE_REFERENCE(3, "Object Type Reference"), 
    ORIGINAL_TRANSMISSION_REFERENCE(103, "Original Transmission, Reference"), 
    ORIGINATING_PROGRAM(65, "Originating Program"), 
    PROGRAM_VERSION(70, "Program Version"), 
    PROVINCE_STATE(95, "Province/State"), 
    RASTERIZED_CAPTION(125, "Rasterized Caption"), 
    RECORD_VERSION(0, "Record Version"), 
    REFERENCE_DATE(47, "Reference Date"), 
    REFERENCE_NUMBER(50, "Reference Number"), 
    REFERENCE_SERVICE(45, "Reference Service"), 
    RELEASE_DATE(30, "Release Date"), 
    RELEASE_TIME(35, "Release Time"), 
    SOURCE(115, "Source"), 
    SPECIAL_INSTRUCTIONS(40, "Special Instructions"), 
    SUBJECT_REFERENCE(12, "Subject Reference"), 
    SUBLOCATION(92, "Sublocation"), 
    SUPPLEMENTAL_CATEGORY(20, "Supplemental Category"), 
    TIME_CREATED(60, "Time Created"), 
    URGENCY(10, "Urgency"), 
    WRITER_EDITOR(122, "Writer/Editor");
    
    public final String name;
    public final int type;
    
    static {
        $VALUES = new IptcTypes[] { IptcTypes.RECORD_VERSION, IptcTypes.OBJECT_TYPE_REFERENCE, IptcTypes.OBJECT_ATTRIBUTE_REFERENCE, IptcTypes.OBJECT_NAME, IptcTypes.EDIT_STATUS, IptcTypes.EDITORIAL_UPDATE, IptcTypes.URGENCY, IptcTypes.SUBJECT_REFERENCE, IptcTypes.CATEGORY, IptcTypes.SUPPLEMENTAL_CATEGORY, IptcTypes.FIXTURE_IDENTIFIER, IptcTypes.KEYWORDS, IptcTypes.CONTENT_LOCATION_CODE, IptcTypes.CONTENT_LOCATION_NAME, IptcTypes.RELEASE_DATE, IptcTypes.RELEASE_TIME, IptcTypes.EXPIRATION_DATE, IptcTypes.EXPIRATION_TIME, IptcTypes.SPECIAL_INSTRUCTIONS, IptcTypes.ACTION_ADVISED, IptcTypes.REFERENCE_SERVICE, IptcTypes.REFERENCE_DATE, IptcTypes.REFERENCE_NUMBER, IptcTypes.DATE_CREATED, IptcTypes.TIME_CREATED, IptcTypes.DIGITAL_CREATION_DATE, IptcTypes.DIGITAL_CREATION_TIME, IptcTypes.ORIGINATING_PROGRAM, IptcTypes.PROGRAM_VERSION, IptcTypes.OBJECT_CYCLE, IptcTypes.BYLINE, IptcTypes.BYLINE_TITLE, IptcTypes.CITY, IptcTypes.SUBLOCATION, IptcTypes.PROVINCE_STATE, IptcTypes.COUNTRY_PRIMARY_LOCATION_CODE, IptcTypes.COUNTRY_PRIMARY_LOCATION_NAME, IptcTypes.ORIGINAL_TRANSMISSION_REFERENCE, IptcTypes.HEADLINE, IptcTypes.CREDIT, IptcTypes.SOURCE, IptcTypes.COPYRIGHT_NOTICE, IptcTypes.CONTACT, IptcTypes.CAPTION_ABSTRACT, IptcTypes.WRITER_EDITOR, IptcTypes.RASTERIZED_CAPTION, IptcTypes.IMAGE_TYPE, IptcTypes.IMAGE_ORIENTATION, IptcTypes.LANGUAGE_IDENTIFIER, IptcTypes.AUDIO_TYPE, IptcTypes.AUDIO_SAMPLING_RATE, IptcTypes.AUDIO_SAMPLING_RESOLUTION, IptcTypes.AUDIO_DURATION, IptcTypes.AUDIO_OUTCUE, IptcTypes.OBJECT_DATA_PREVIEW_FILE_FORMAT, IptcTypes.OBJECT_DATA_PREVIEW_FILE_FORMAT_VERSION, IptcTypes.OBJECT_DATA_PREVIEW_DATA };
    }
    
    private IptcTypes(final int type, final String name2) {
        this.type = type;
        this.name = name2;
    }
    
    public static IptcType getUnknown(final int n) {
        return new IptcType(n) {
            final int val$type;
            
            @Override
            public String getName() {
                return "Unknown";
            }
            
            @Override
            public int getType() {
                return this.val$type;
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown (");
                sb.append(this.val$type);
                sb.append(")");
                return sb.toString();
            }
        };
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public int getType() {
        return this.type;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append(" (");
        sb.append(this.type);
        sb.append(")");
        return sb.toString();
    }
}
