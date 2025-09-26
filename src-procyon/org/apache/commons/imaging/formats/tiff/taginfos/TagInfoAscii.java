// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.taginfos;

import java.io.UnsupportedEncodingException;
import org.apache.commons.imaging.ImageWriteException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;

public class TagInfoAscii extends TagInfo
{
    public TagInfoAscii(final String s, final int n, final int n2, final TiffDirectoryType tiffDirectoryType) {
        super(s, n, FieldType.ASCII, n2, tiffDirectoryType);
    }
    
    public byte[] encodeValue(final ByteOrder byteOrder, final String... array) throws ImageWriteException {
        return FieldType.ASCII.writeData(array, byteOrder);
    }
    
    public String[] getValue(ByteOrder byteOrder, final byte[] array) {
        final int n = 0;
        int i = 0;
        int n2 = 0;
        while (i < array.length - 1) {
            int n3 = n2;
            if (array[i] == 0) {
                n3 = n2 + 1;
            }
            ++i;
            n2 = n3;
        }
        byteOrder = (ByteOrder)(Object)new String[n2 + 1];
        byteOrder[0] = "";
        final int n4 = 0;
        int n5 = 0;
        int j = n;
        int n6 = n4;
        while (j < array.length) {
            int n7 = n6;
            int n8 = n5;
            if (array[j] == 0) {
                try {
                    final String s = new String(array, n6, j - n6, "UTF-8");
                    final int n9 = n5 + 1;
                    byteOrder[n5] = s;
                    n5 = n9;
                }
                catch (final UnsupportedEncodingException ex) {}
                n7 = j + 1;
                n8 = n5;
            }
            ++j;
            n6 = n7;
            n5 = n8;
        }
        if (n6 >= array.length) {
            return (String[])(Object)byteOrder;
        }
        try {
            byteOrder[n5] = new String(array, n6, array.length - n6, "UTF-8");
            return (String[])(Object)byteOrder;
        }
        catch (final UnsupportedEncodingException ex2) {
            return (String[])(Object)byteOrder;
        }
    }
}
