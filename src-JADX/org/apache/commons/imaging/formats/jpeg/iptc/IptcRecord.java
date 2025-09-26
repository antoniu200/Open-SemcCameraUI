package org.apache.commons.imaging.formats.jpeg.iptc;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class IptcRecord {
    public static final Comparator<IptcRecord> COMPARATOR = new Comparator<IptcRecord>() { // from class: org.apache.commons.imaging.formats.jpeg.iptc.IptcRecord.1
        @Override // java.util.Comparator
        public int compare(IptcRecord iptcRecord, IptcRecord iptcRecord2) {
            return iptcRecord.iptcType.getType() - iptcRecord2.iptcType.getType();
        }
    };
    private final byte[] bytes;
    public final IptcType iptcType;
    public final String value;

    public IptcRecord(IptcType iptcType, byte[] bArr, String str) {
        this.iptcType = iptcType;
        this.bytes = bArr;
        this.value = str;
    }

    public IptcRecord(IptcType iptcType, String str) throws UnsupportedEncodingException {
        byte[] bytes;
        this.iptcType = iptcType;
        try {
            bytes = str.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException unused) {
            bytes = null;
        }
        this.bytes = bytes;
        this.value = str;
    }

    public byte[] getRawBytes() {
        return (byte[]) this.bytes.clone();
    }

    public String getValue() {
        return this.value;
    }

    public String getIptcTypeName() {
        return this.iptcType.getName();
    }
}
