// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.fieldtypes;

import org.apache.commons.imaging.ImageWriteException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.TiffField;
import java.util.Iterator;
import org.apache.commons.imaging.ImageReadException;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public abstract class FieldType
{
    public static final List<FieldType> ANY;
    public static final FieldTypeAscii ASCII;
    public static final List<FieldType> ASCII_OR_BYTE;
    public static final List<FieldType> ASCII_OR_RATIONAL;
    public static final FieldTypeByte BYTE;
    public static final List<FieldType> BYTE_OR_SHORT;
    public static final FieldTypeDouble DOUBLE;
    public static final FieldTypeFloat FLOAT;
    public static final FieldTypeLong IFD;
    public static final FieldTypeLong LONG;
    public static final List<FieldType> LONG_OR_IFD;
    public static final List<FieldType> LONG_OR_SHORT;
    public static final FieldTypeRational RATIONAL;
    public static final FieldTypeByte SBYTE;
    public static final FieldTypeShort SHORT;
    public static final List<FieldType> SHORT_OR_LONG;
    public static final List<FieldType> SHORT_OR_LONG_OR_RATIONAL;
    public static final List<FieldType> SHORT_OR_RATIONAL;
    public static final FieldTypeLong SLONG;
    public static final FieldTypeRational SRATIONAL;
    public static final FieldTypeShort SSHORT;
    public static final FieldTypeByte UNDEFINED;
    private final int elementSize;
    private final String name;
    private final int type;
    
    static {
        BYTE = new FieldTypeByte(1, "Byte");
        ASCII = new FieldTypeAscii(2, "ASCII");
        SHORT = new FieldTypeShort(3, "Short");
        LONG = new FieldTypeLong(4, "Long");
        RATIONAL = new FieldTypeRational(5, "Rational");
        SBYTE = new FieldTypeByte(6, "SByte");
        UNDEFINED = new FieldTypeByte(7, "Undefined");
        SSHORT = new FieldTypeShort(8, "SShort");
        SLONG = new FieldTypeLong(9, "SLong");
        SRATIONAL = new FieldTypeRational(10, "SRational");
        FLOAT = new FieldTypeFloat(11, "Float");
        DOUBLE = new FieldTypeDouble(12, "Double");
        IFD = new FieldTypeLong(13, "IFD");
        ANY = Collections.unmodifiableList((List<? extends FieldType>)Arrays.asList(FieldType.BYTE, FieldType.ASCII, FieldType.SHORT, FieldType.LONG, FieldType.RATIONAL, FieldType.SBYTE, FieldType.UNDEFINED, FieldType.SSHORT, FieldType.SLONG, FieldType.SRATIONAL, FieldType.FLOAT, FieldType.DOUBLE, FieldType.IFD));
        SHORT_OR_LONG = Collections.unmodifiableList((List<? extends FieldType>)Arrays.asList(FieldType.SHORT, FieldType.LONG));
        SHORT_OR_RATIONAL = Collections.unmodifiableList((List<? extends FieldType>)Arrays.asList(FieldType.SHORT, FieldType.RATIONAL));
        SHORT_OR_LONG_OR_RATIONAL = Collections.unmodifiableList((List<? extends FieldType>)Arrays.asList(FieldType.SHORT, FieldType.LONG, FieldType.RATIONAL));
        LONG_OR_SHORT = Collections.unmodifiableList((List<? extends FieldType>)Arrays.asList(FieldType.SHORT, FieldType.LONG));
        BYTE_OR_SHORT = Collections.unmodifiableList((List<? extends FieldType>)Arrays.asList(FieldType.SHORT, FieldType.BYTE));
        LONG_OR_IFD = Collections.unmodifiableList((List<? extends FieldType>)Arrays.asList(FieldType.LONG, FieldType.IFD));
        ASCII_OR_RATIONAL = Collections.unmodifiableList((List<? extends FieldType>)Arrays.asList(FieldType.ASCII, FieldType.RATIONAL));
        ASCII_OR_BYTE = Collections.unmodifiableList((List<? extends FieldType>)Arrays.asList(FieldType.ASCII, FieldType.BYTE));
    }
    
    protected FieldType(final int type, final String name, final int elementSize) {
        this.type = type;
        this.name = name;
        this.elementSize = elementSize;
    }
    
    public static FieldType getFieldType(final int i) throws ImageReadException {
        for (final FieldType fieldType : FieldType.ANY) {
            if (fieldType.getType() == i) {
                return fieldType;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Field type ");
        sb.append(i);
        sb.append(" is unsupported");
        throw new ImageReadException(sb.toString());
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getSize() {
        return this.elementSize;
    }
    
    public int getType() {
        return this.type;
    }
    
    public abstract Object getValue(final TiffField p0);
    
    public abstract byte[] writeData(final Object p0, final ByteOrder p1) throws ImageWriteException;
}
