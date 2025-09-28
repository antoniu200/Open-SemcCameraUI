package org.apache.commons.imaging.formats.tiff;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TiffField {
    private final ByteOrder byteOrder;
    private final long count;
    private final int directoryType;
    private final FieldType fieldType;
    private final long offset;
    private final int sortHint;
    private final int tag;
    private final TagInfo tagInfo;
    private final byte[] value;

    public TiffField(int i, int i2, FieldType fieldType, long j, long j2, byte[] bArr, ByteOrder byteOrder, int i3) {
        this.tag = i;
        this.directoryType = i2;
        this.fieldType = fieldType;
        this.count = j;
        this.offset = j2;
        this.value = bArr;
        this.byteOrder = byteOrder;
        this.sortHint = i3;
        this.tagInfo = TiffTags.getTag(i2, i);
    }

    public int getDirectoryType() {
        return this.directoryType;
    }

    public TagInfo getTagInfo() {
        return this.tagInfo;
    }

    public int getTag() {
        return this.tag;
    }

    public FieldType getFieldType() {
        return this.fieldType;
    }

    public long getCount() {
        return this.count;
    }

    public int getOffset() {
        return (int) this.offset;
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public int getSortHint() {
        return this.sortHint;
    }

    public boolean isLocalValue() {
        return this.count * ((long) this.fieldType.getSize()) <= 4;
    }

    public int getBytesLength() {
        return ((int) this.count) * this.fieldType.getSize();
    }

    public byte[] getByteArrayValue() {
        return BinaryFunctions.head(this.value, getBytesLength());
    }

    public final class OversizeValueElement extends TiffElement {
        public OversizeValueElement(int i, int i2) {
            super(i, i2);
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffElement
        public String getElementDescription(boolean z) {
            if (z) {
                return null;
            }
            return "OversizeValueElement, tag: " + TiffField.this.getTagInfo().name + ", fieldType: " + TiffField.this.getFieldType().getName();
        }
    }

    public TiffElement getOversizeValueElement() {
        if (isLocalValue()) {
            return null;
        }
        return new OversizeValueElement(getOffset(), this.value.length);
    }

    public String getValueDescription() {
        try {
            return getValueDescription(getValue());
        } catch (ImageReadException e) {
            return "Invalid value: " + e.getMessage();
        }
    }

    private String getValueDescription(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return obj.toString();
        }
        if (obj instanceof String) {
            return "'" + obj.toString().trim() + "'";
        }
        if (obj instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH).format((Date) obj);
        }
        int i = 0;
        if (obj instanceof Object[]) {
            Object[] objArr = (Object[]) obj;
            StringBuilder sb = new StringBuilder();
            while (true) {
                if (i >= objArr.length) {
                    break;
                }
                Object obj2 = objArr[i];
                if (i > 50) {
                    sb.append("... (" + objArr.length + ")");
                    break;
                }
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(obj2.toString());
                i++;
            }
            return sb.toString();
        }
        if (obj instanceof short[]) {
            short[] sArr = (short[]) obj;
            StringBuilder sb2 = new StringBuilder();
            while (true) {
                if (i >= sArr.length) {
                    break;
                }
                short s = sArr[i];
                if (i > 50) {
                    sb2.append("... (" + sArr.length + ")");
                    break;
                }
                if (i > 0) {
                    sb2.append(", ");
                }
                sb2.append(Short.toString(s));
                i++;
            }
            return sb2.toString();
        }
        if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            StringBuilder sb3 = new StringBuilder();
            while (true) {
                if (i >= iArr.length) {
                    break;
                }
                int i2 = iArr[i];
                if (i > 50) {
                    sb3.append("... (" + iArr.length + ")");
                    break;
                }
                if (i > 0) {
                    sb3.append(", ");
                }
                sb3.append(Integer.toString(i2));
                i++;
            }
            return sb3.toString();
        }
        if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            StringBuilder sb4 = new StringBuilder();
            while (true) {
                if (i >= jArr.length) {
                    break;
                }
                long j = jArr[i];
                if (i > 50) {
                    sb4.append("... (" + jArr.length + ")");
                    break;
                }
                if (i > 0) {
                    sb4.append(", ");
                }
                sb4.append(Long.toString(j));
                i++;
            }
            return sb4.toString();
        }
        if (obj instanceof double[]) {
            double[] dArr = (double[]) obj;
            StringBuilder sb5 = new StringBuilder();
            while (true) {
                if (i >= dArr.length) {
                    break;
                }
                double d = dArr[i];
                if (i > 50) {
                    sb5.append("... (" + dArr.length + ")");
                    break;
                }
                if (i > 0) {
                    sb5.append(", ");
                }
                sb5.append(Double.toString(d));
                i++;
            }
            return sb5.toString();
        }
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            StringBuilder sb6 = new StringBuilder();
            while (true) {
                if (i >= bArr.length) {
                    break;
                }
                byte b = bArr[i];
                if (i > 50) {
                    sb6.append("... (" + bArr.length + ")");
                    break;
                }
                if (i > 0) {
                    sb6.append(", ");
                }
                sb6.append(Byte.toString(b));
                i++;
            }
            return sb6.toString();
        }
        if (obj instanceof char[]) {
            char[] cArr = (char[]) obj;
            StringBuilder sb7 = new StringBuilder();
            while (true) {
                if (i >= cArr.length) {
                    break;
                }
                char c = cArr[i];
                if (i > 50) {
                    sb7.append("... (" + cArr.length + ")");
                    break;
                }
                if (i > 0) {
                    sb7.append(", ");
                }
                sb7.append(Character.toString(c));
                i++;
            }
            return sb7.toString();
        }
        if (obj instanceof float[]) {
            float[] fArr = (float[]) obj;
            StringBuilder sb8 = new StringBuilder();
            while (true) {
                if (i >= fArr.length) {
                    break;
                }
                float f = fArr[i];
                if (i > 50) {
                    sb8.append("... (" + fArr.length + ")");
                    break;
                }
                if (i > 0) {
                    sb8.append(", ");
                }
                sb8.append(Float.toString(f));
                i++;
            }
            return sb8.toString();
        }
        return "Unknown: " + obj.getClass().getName();
    }

    public void dump() {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
        dump(printWriter);
        printWriter.flush();
    }

    public void dump(PrintWriter printWriter) {
        dump(printWriter, null);
    }

    public void dump(PrintWriter printWriter, String str) {
        if (str != null) {
            printWriter.print(str + ": ");
        }
        printWriter.println(toString());
        printWriter.flush();
    }

    public String getDescriptionWithoutValue() {
        return getTag() + " (0x" + Integer.toHexString(getTag()) + ": " + getTagInfo().name + "): ";
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTag() + " (0x" + Integer.toHexString(getTag()) + ": " + getTagInfo().name + "): ");
        sb.append(getValueDescription() + " (" + getCount() + " " + getFieldType().getName() + ")");
        return sb.toString();
    }

    public String getTagName() {
        if (getTagInfo() == TiffTagConstants.TIFF_TAG_UNKNOWN) {
            return getTagInfo().name + " (0x" + Integer.toHexString(getTag()) + ")";
        }
        return getTagInfo().name;
    }

    public String getFieldTypeName() {
        return getFieldType().getName();
    }

    public Object getValue() throws ImageReadException {
        return getTagInfo().getValue(this);
    }

    public String getStringValue() throws ImageReadException {
        Object value = getValue();
        if (value == null) {
            return null;
        }
        if (!(value instanceof String)) {
            throw new ImageReadException("Expected String value(" + getTagInfo().getDescription() + "): " + value);
        }
        return (String) value;
    }

    public int[] getIntArrayValue() throws ImageReadException {
        Object value = getValue();
        int i = 0;
        if (value instanceof Number) {
            return new int[]{((Number) value).intValue()};
        }
        if (value instanceof Number[]) {
            Number[] numberArr = (Number[]) value;
            int[] iArr = new int[numberArr.length];
            while (i < numberArr.length) {
                iArr[i] = numberArr[i].intValue();
                i++;
            }
            return iArr;
        }
        if (value instanceof short[]) {
            short[] sArr = (short[]) value;
            int[] iArr2 = new int[sArr.length];
            while (i < sArr.length) {
                iArr2[i] = 65535 & sArr[i];
                i++;
            }
            return iArr2;
        }
        if (value instanceof int[]) {
            int[] iArr3 = (int[]) value;
            int[] iArr4 = new int[iArr3.length];
            System.arraycopy(iArr3, 0, iArr4, 0, iArr3.length);
            return iArr4;
        }
        throw new ImageReadException("Unknown value: " + value + " for: " + getTagInfo().getDescription());
    }

    public double[] getDoubleArrayValue() throws ImageReadException {
        Object value = getValue();
        int i = 0;
        if (value instanceof Number) {
            return new double[]{((Number) value).doubleValue()};
        }
        if (value instanceof Number[]) {
            Number[] numberArr = (Number[]) value;
            double[] dArr = new double[numberArr.length];
            while (i < numberArr.length) {
                dArr[i] = numberArr[i].doubleValue();
                i++;
            }
            return dArr;
        }
        if (value instanceof short[]) {
            short[] sArr = (short[]) value;
            double[] dArr2 = new double[sArr.length];
            while (i < sArr.length) {
                dArr2[i] = sArr[i];
                i++;
            }
            return dArr2;
        }
        if (value instanceof int[]) {
            int[] iArr = (int[]) value;
            double[] dArr3 = new double[iArr.length];
            while (i < iArr.length) {
                dArr3[i] = iArr[i];
                i++;
            }
            return dArr3;
        }
        if (value instanceof float[]) {
            float[] fArr = (float[]) value;
            double[] dArr4 = new double[fArr.length];
            while (i < fArr.length) {
                dArr4[i] = fArr[i];
                i++;
            }
            return dArr4;
        }
        if (value instanceof double[]) {
            double[] dArr5 = (double[]) value;
            double[] dArr6 = new double[dArr5.length];
            System.arraycopy(dArr5, 0, dArr6, 0, dArr5.length);
            return dArr6;
        }
        throw new ImageReadException("Unknown value: " + value + " for: " + getTagInfo().getDescription());
    }

    public int getIntValueOrArraySum() throws ImageReadException {
        Object value = getValue();
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        int i = 0;
        if (value instanceof Number[]) {
            Number[] numberArr = (Number[]) value;
            int length = numberArr.length;
            int iIntValue = 0;
            while (i < length) {
                iIntValue += numberArr[i].intValue();
                i++;
            }
            return iIntValue;
        }
        if (value instanceof short[]) {
            short[] sArr = (short[]) value;
            int length2 = sArr.length;
            int i2 = 0;
            while (i < length2) {
                i2 += sArr[i];
                i++;
            }
            return i2;
        }
        if (value instanceof int[]) {
            int[] iArr = (int[]) value;
            int length3 = iArr.length;
            int i3 = 0;
            while (i < length3) {
                i3 += iArr[i];
                i++;
            }
            return i3;
        }
        throw new ImageReadException("Unknown value: " + value + " for: " + getTagInfo().getDescription());
    }

    public int getIntValue() throws ImageReadException {
        Object value = getValue();
        if (value == null) {
            throw new ImageReadException("Missing value: " + getTagInfo().getDescription());
        }
        return ((Number) value).intValue();
    }

    public double getDoubleValue() throws ImageReadException {
        Object value = getValue();
        if (value == null) {
            throw new ImageReadException("Missing value: " + getTagInfo().getDescription());
        }
        return ((Number) value).doubleValue();
    }
}
