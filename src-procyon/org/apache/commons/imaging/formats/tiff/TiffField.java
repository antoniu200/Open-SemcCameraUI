// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff;

import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import java.nio.ByteOrder;

public class TiffField
{
    private final ByteOrder byteOrder;
    private final long count;
    private final int directoryType;
    private final FieldType fieldType;
    private final long offset;
    private final int sortHint;
    private final int tag;
    private final TagInfo tagInfo;
    private final byte[] value;
    
    public TiffField(final int tag, final int directoryType, final FieldType fieldType, final long count, final long offset, final byte[] value, final ByteOrder byteOrder, final int sortHint) {
        this.tag = tag;
        this.directoryType = directoryType;
        this.fieldType = fieldType;
        this.count = count;
        this.offset = offset;
        this.value = value;
        this.byteOrder = byteOrder;
        this.sortHint = sortHint;
        this.tagInfo = TiffTags.getTag(directoryType, tag);
    }
    
    private String getValueDescription(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return o.toString();
        }
        if (o instanceof String) {
            final StringBuilder sb = new StringBuilder();
            sb.append("'");
            sb.append(o.toString().trim());
            sb.append("'");
            return sb.toString();
        }
        if (o instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH).format((Date)o);
        }
        final boolean b = o instanceof Object[];
        final int n = 0;
        final int n2 = 0;
        final int n3 = 0;
        int i = 0;
        final int n4 = 0;
        final int n5 = 0;
        final int n6 = 0;
        final int n7 = 0;
        if (b) {
            final Object[] array = (Object[])o;
            final StringBuilder sb2 = new StringBuilder();
            for (int j = n7; j < array.length; ++j) {
                final Object o2 = array[j];
                if (j > 50) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("... (");
                    sb3.append(array.length);
                    sb3.append(")");
                    sb2.append(sb3.toString());
                    break;
                }
                if (j > 0) {
                    sb2.append(", ");
                }
                sb2.append(o2.toString());
            }
            return sb2.toString();
        }
        if (o instanceof short[]) {
            final short[] array2 = (short[])o;
            final StringBuilder sb4 = new StringBuilder();
            for (int k = n; k < array2.length; ++k) {
                final short s = array2[k];
                if (k > 50) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("... (");
                    sb5.append(array2.length);
                    sb5.append(")");
                    sb4.append(sb5.toString());
                    break;
                }
                if (k > 0) {
                    sb4.append(", ");
                }
                sb4.append(Short.toString(s));
            }
            return sb4.toString();
        }
        if (o instanceof int[]) {
            final int[] array3 = (int[])o;
            final StringBuilder sb6 = new StringBuilder();
            for (int l = n2; l < array3.length; ++l) {
                final int m = array3[l];
                if (l > 50) {
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append("... (");
                    sb7.append(array3.length);
                    sb7.append(")");
                    sb6.append(sb7.toString());
                    break;
                }
                if (l > 0) {
                    sb6.append(", ");
                }
                sb6.append(Integer.toString(m));
            }
            return sb6.toString();
        }
        if (o instanceof long[]) {
            final long[] array4 = (long[])o;
            final StringBuilder sb8 = new StringBuilder();
            for (int n8 = n3; n8 < array4.length; ++n8) {
                final long i2 = array4[n8];
                if (n8 > 50) {
                    final StringBuilder sb9 = new StringBuilder();
                    sb9.append("... (");
                    sb9.append(array4.length);
                    sb9.append(")");
                    sb8.append(sb9.toString());
                    break;
                }
                if (n8 > 0) {
                    sb8.append(", ");
                }
                sb8.append(Long.toString(i2));
            }
            return sb8.toString();
        }
        if (o instanceof double[]) {
            final double[] array5 = (double[])o;
            final StringBuilder sb10 = new StringBuilder();
            while (i < array5.length) {
                final double d = array5[i];
                if (i > 50) {
                    final StringBuilder sb11 = new StringBuilder();
                    sb11.append("... (");
                    sb11.append(array5.length);
                    sb11.append(")");
                    sb10.append(sb11.toString());
                    break;
                }
                if (i > 0) {
                    sb10.append(", ");
                }
                sb10.append(Double.toString(d));
                ++i;
            }
            return sb10.toString();
        }
        if (o instanceof byte[]) {
            final byte[] array6 = (byte[])o;
            final StringBuilder sb12 = new StringBuilder();
            for (int n9 = n4; n9 < array6.length; ++n9) {
                final byte b2 = array6[n9];
                if (n9 > 50) {
                    final StringBuilder sb13 = new StringBuilder();
                    sb13.append("... (");
                    sb13.append(array6.length);
                    sb13.append(")");
                    sb12.append(sb13.toString());
                    break;
                }
                if (n9 > 0) {
                    sb12.append(", ");
                }
                sb12.append(Byte.toString(b2));
            }
            return sb12.toString();
        }
        if (o instanceof char[]) {
            final char[] array7 = (char[])o;
            final StringBuilder sb14 = new StringBuilder();
            for (int n10 = n5; n10 < array7.length; ++n10) {
                final char c = array7[n10];
                if (n10 > 50) {
                    final StringBuilder sb15 = new StringBuilder();
                    sb15.append("... (");
                    sb15.append(array7.length);
                    sb15.append(")");
                    sb14.append(sb15.toString());
                    break;
                }
                if (n10 > 0) {
                    sb14.append(", ");
                }
                sb14.append(Character.toString(c));
            }
            return sb14.toString();
        }
        if (o instanceof float[]) {
            final float[] array8 = (float[])o;
            final StringBuilder sb16 = new StringBuilder();
            for (int n11 = n6; n11 < array8.length; ++n11) {
                final float f = array8[n11];
                if (n11 > 50) {
                    final StringBuilder sb17 = new StringBuilder();
                    sb17.append("... (");
                    sb17.append(array8.length);
                    sb17.append(")");
                    sb16.append(sb17.toString());
                    break;
                }
                if (n11 > 0) {
                    sb16.append(", ");
                }
                sb16.append(Float.toString(f));
            }
            return sb16.toString();
        }
        final StringBuilder sb18 = new StringBuilder();
        sb18.append("Unknown: ");
        sb18.append(o.getClass().getName());
        return sb18.toString();
    }
    
    public void dump() {
        final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
        this.dump(printWriter);
        printWriter.flush();
    }
    
    public void dump(final PrintWriter printWriter) {
        this.dump(printWriter, null);
    }
    
    public void dump(final PrintWriter printWriter, final String str) {
        if (str != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(": ");
            printWriter.print(sb.toString());
        }
        printWriter.println(this.toString());
        printWriter.flush();
    }
    
    public byte[] getByteArrayValue() {
        return BinaryFunctions.head(this.value, this.getBytesLength());
    }
    
    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }
    
    public int getBytesLength() {
        return (int)this.count * this.fieldType.getSize();
    }
    
    public long getCount() {
        return this.count;
    }
    
    public String getDescriptionWithoutValue() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getTag());
        sb.append(" (0x");
        sb.append(Integer.toHexString(this.getTag()));
        sb.append(": ");
        sb.append(this.getTagInfo().name);
        sb.append("): ");
        return sb.toString();
    }
    
    public int getDirectoryType() {
        return this.directoryType;
    }
    
    public double[] getDoubleArrayValue() throws ImageReadException {
        final Object value = this.getValue();
        final boolean b = value instanceof Number;
        final int n = 0;
        int i = 0;
        final int n2 = 0;
        final int n3 = 0;
        if (b) {
            return new double[] { ((Number)value).doubleValue() };
        }
        if (value instanceof Number[]) {
            final Number[] array = (Number[])value;
            final double[] array2 = new double[array.length];
            for (int j = n3; j < array.length; ++j) {
                array2[j] = array[j].doubleValue();
            }
            return array2;
        }
        if (value instanceof short[]) {
            final short[] array3 = (short[])value;
            final double[] array4 = new double[array3.length];
            for (int k = n; k < array3.length; ++k) {
                array4[k] = array3[k];
            }
            return array4;
        }
        if (value instanceof int[]) {
            final int[] array5 = (int[])value;
            final double[] array6 = new double[array5.length];
            while (i < array5.length) {
                array6[i] = array5[i];
                ++i;
            }
            return array6;
        }
        if (value instanceof float[]) {
            final float[] array7 = (float[])value;
            final double[] array8 = new double[array7.length];
            for (int l = n2; l < array7.length; ++l) {
                array8[l] = array7[l];
            }
            return array8;
        }
        if (value instanceof double[]) {
            final double[] array9 = (double[])value;
            final double[] array10 = new double[array9.length];
            System.arraycopy(array9, 0, array10, 0, array9.length);
            return array10;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown value: ");
        sb.append(value);
        sb.append(" for: ");
        sb.append(this.getTagInfo().getDescription());
        throw new ImageReadException(sb.toString());
    }
    
    public double getDoubleValue() throws ImageReadException {
        final Object value = this.getValue();
        if (value == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Missing value: ");
            sb.append(this.getTagInfo().getDescription());
            throw new ImageReadException(sb.toString());
        }
        return ((Number)value).doubleValue();
    }
    
    public FieldType getFieldType() {
        return this.fieldType;
    }
    
    public String getFieldTypeName() {
        return this.getFieldType().getName();
    }
    
    public int[] getIntArrayValue() throws ImageReadException {
        final Object value = this.getValue();
        final boolean b = value instanceof Number;
        int i = 0;
        final int n = 0;
        if (b) {
            return new int[] { ((Number)value).intValue() };
        }
        if (value instanceof Number[]) {
            final Number[] array = (Number[])value;
            final int[] array2 = new int[array.length];
            for (int j = n; j < array.length; ++j) {
                array2[j] = array[j].intValue();
            }
            return array2;
        }
        if (value instanceof short[]) {
            final short[] array3 = (short[])value;
            final int[] array4 = new int[array3.length];
            while (i < array3.length) {
                array4[i] = (0xFFFF & array3[i]);
                ++i;
            }
            return array4;
        }
        if (value instanceof int[]) {
            final int[] array5 = (int[])value;
            final int[] array6 = new int[array5.length];
            System.arraycopy(array5, 0, array6, 0, array5.length);
            return array6;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown value: ");
        sb.append(value);
        sb.append(" for: ");
        sb.append(this.getTagInfo().getDescription());
        throw new ImageReadException(sb.toString());
    }
    
    public int getIntValue() throws ImageReadException {
        final Object value = this.getValue();
        if (value == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Missing value: ");
            sb.append(this.getTagInfo().getDescription());
            throw new ImageReadException(sb.toString());
        }
        return ((Number)value).intValue();
    }
    
    public int getIntValueOrArraySum() throws ImageReadException {
        final Object value = this.getValue();
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        final boolean b = value instanceof Number[];
        int i = 0;
        final int n = 0;
        final int n2 = 0;
        if (b) {
            final Number[] array = (Number[])value;
            final int length = array.length;
            final int n3 = 0;
            int j = n2;
            int n4 = n3;
            while (j < length) {
                n4 += array[j].intValue();
                ++j;
            }
            return n4;
        }
        if (value instanceof short[]) {
            final short[] array2 = (short[])value;
            final int length2 = array2.length;
            int n5 = 0;
            while (i < length2) {
                n5 += array2[i];
                ++i;
            }
            return n5;
        }
        if (value instanceof int[]) {
            final int[] array3 = (int[])value;
            final int length3 = array3.length;
            int n6 = 0;
            for (int k = n; k < length3; ++k) {
                n6 += array3[k];
            }
            return n6;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown value: ");
        sb.append(value);
        sb.append(" for: ");
        sb.append(this.getTagInfo().getDescription());
        throw new ImageReadException(sb.toString());
    }
    
    public int getOffset() {
        return (int)this.offset;
    }
    
    public TiffElement getOversizeValueElement() {
        if (this.isLocalValue()) {
            return null;
        }
        return new OversizeValueElement(this.getOffset(), this.value.length);
    }
    
    public int getSortHint() {
        return this.sortHint;
    }
    
    public String getStringValue() throws ImageReadException {
        final Object value = this.getValue();
        if (value == null) {
            return null;
        }
        if (!(value instanceof String)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected String value(");
            sb.append(this.getTagInfo().getDescription());
            sb.append("): ");
            sb.append(value);
            throw new ImageReadException(sb.toString());
        }
        return (String)value;
    }
    
    public int getTag() {
        return this.tag;
    }
    
    public TagInfo getTagInfo() {
        return this.tagInfo;
    }
    
    public String getTagName() {
        if (this.getTagInfo() == TiffTagConstants.TIFF_TAG_UNKNOWN) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getTagInfo().name);
            sb.append(" (0x");
            sb.append(Integer.toHexString(this.getTag()));
            sb.append(")");
            return sb.toString();
        }
        return this.getTagInfo().name;
    }
    
    public Object getValue() throws ImageReadException {
        return this.getTagInfo().getValue(this);
    }
    
    public String getValueDescription() {
        try {
            return this.getValueDescription(this.getValue());
        }
        catch (final ImageReadException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid value: ");
            sb.append(ex.getMessage());
            return sb.toString();
        }
    }
    
    public boolean isLocalValue() {
        return this.count * this.fieldType.getSize() <= 4L;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.getTag());
        sb2.append(" (0x");
        sb2.append(Integer.toHexString(this.getTag()));
        sb2.append(": ");
        sb2.append(this.getTagInfo().name);
        sb2.append("): ");
        sb.append(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(this.getValueDescription());
        sb3.append(" (");
        sb3.append(this.getCount());
        sb3.append(" ");
        sb3.append(this.getFieldType().getName());
        sb3.append(")");
        sb.append(sb3.toString());
        return sb.toString();
    }
    
    public final class OversizeValueElement extends TiffElement
    {
        final TiffField this$0;
        
        public OversizeValueElement(final TiffField this$0, final int n, final int n2) {
            this.this$0 = this$0;
            super(n, n2);
        }
        
        @Override
        public String getElementDescription(final boolean b) {
            if (b) {
                return null;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("OversizeValueElement, tag: ");
            sb.append(this.this$0.getTagInfo().name);
            sb.append(", fieldType: ");
            sb.append(this.this$0.getFieldType().getName());
            return sb.toString();
        }
    }
}
