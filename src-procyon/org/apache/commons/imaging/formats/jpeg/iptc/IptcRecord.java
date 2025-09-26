// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.iptc;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;

public class IptcRecord
{
    public static final Comparator<IptcRecord> COMPARATOR;
    private final byte[] bytes;
    public final IptcType iptcType;
    public final String value;
    
    static {
        COMPARATOR = new Comparator<IptcRecord>() {
            @Override
            public int compare(final IptcRecord iptcRecord, final IptcRecord iptcRecord2) {
                return iptcRecord.iptcType.getType() - iptcRecord2.iptcType.getType();
            }
        };
    }
    
    public IptcRecord(final IptcType iptcType, final String value) {
        this.iptcType = iptcType;
        byte[] bytes;
        try {
            bytes = value.getBytes("ISO-8859-1");
        }
        catch (final UnsupportedEncodingException ex) {
            bytes = null;
        }
        this.bytes = bytes;
        this.value = value;
    }
    
    public IptcRecord(final IptcType iptcType, final byte[] bytes, final String value) {
        this.iptcType = iptcType;
        this.bytes = bytes;
        this.value = value;
    }
    
    public String getIptcTypeName() {
        return this.iptcType.getName();
    }
    
    public byte[] getRawBytes() {
        return this.bytes.clone();
    }
    
    public String getValue() {
        return this.value;
    }
}
