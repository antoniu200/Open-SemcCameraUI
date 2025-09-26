// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.icc;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import java.io.PrintStream;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.nio.ByteOrder;
import java.io.ByteArrayInputStream;

public enum IccTagDataTypes implements IccTagDataType
{
    private static final IccTagDataTypes[] $VALUES;
    
    DATA_TYPE("dataType", 1684108385) {
        @Override
        public void dump(final String s, final byte[] buf) throws ImageReadException, IOException {
            Closeable closeable;
            try {
                final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                try {
                    BinaryFunctions.read4Bytes("type_signature", byteArrayInputStream, "ICC: corrupt tag data", ByteOrder.BIG_ENDIAN);
                    IoUtils.closeQuietly(true, byteArrayInputStream);
                    return;
                }
                finally {}
            }
            finally {
                closeable = null;
            }
            IoUtils.closeQuietly(false, closeable);
        }
    }, 
    DESC_TYPE("descType", 1684370275) {
        @Override
        public void dump(final String str, final byte[] array) throws ImageReadException, IOException {
            Closeable closeable;
            try {
                final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
                try {
                    BinaryFunctions.read4Bytes("type_signature", byteArrayInputStream, "ICC: corrupt tag data", ByteOrder.BIG_ENDIAN);
                    BinaryFunctions.read4Bytes("ignore", byteArrayInputStream, "ICC: corrupt tag data", ByteOrder.BIG_ENDIAN);
                    final String str2 = new String(array, 12, BinaryFunctions.read4Bytes("stringLength", byteArrayInputStream, "ICC: corrupt tag data", ByteOrder.BIG_ENDIAN) - 1, "US-ASCII");
                    final PrintStream out = System.out;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append("s: '");
                    sb.append(str2);
                    sb.append("'");
                    out.println(sb.toString());
                    IoUtils.closeQuietly(true, byteArrayInputStream);
                    return;
                }
                finally {}
            }
            finally {
                closeable = null;
            }
            IoUtils.closeQuietly(false, closeable);
        }
    }, 
    MULTI_LOCALIZED_UNICODE_TYPE("multiLocalizedUnicodeType", 1835824483) {
        @Override
        public void dump(final String s, final byte[] buf) throws ImageReadException, IOException {
            Closeable closeable;
            try {
                final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                try {
                    BinaryFunctions.read4Bytes("type_signature", byteArrayInputStream, "ICC: corrupt tag data", ByteOrder.BIG_ENDIAN);
                    IoUtils.closeQuietly(true, byteArrayInputStream);
                    return;
                }
                finally {}
            }
            finally {
                closeable = null;
            }
            IoUtils.closeQuietly(false, closeable);
        }
    }, 
    SIGNATURE_TYPE("signatureType", 1936287520) {
        @Override
        public void dump(final String str, final byte[] buf) throws ImageReadException, IOException {
            Closeable closeable;
            try {
                final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                try {
                    BinaryFunctions.read4Bytes("type_signature", byteArrayInputStream, "ICC: corrupt tag data", ByteOrder.BIG_ENDIAN);
                    BinaryFunctions.read4Bytes("ignore", byteArrayInputStream, "ICC: corrupt tag data", ByteOrder.BIG_ENDIAN);
                    final int read4Bytes = BinaryFunctions.read4Bytes("thesignature ", byteArrayInputStream, "ICC: corrupt tag data", ByteOrder.BIG_ENDIAN);
                    final PrintStream out = System.out;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append("thesignature: ");
                    sb.append(Integer.toHexString(read4Bytes));
                    sb.append(" (");
                    sb.append(new String(new byte[] { (byte)(read4Bytes >> 24 & 0xFF), (byte)(read4Bytes >> 16 & 0xFF), (byte)(read4Bytes >> 8 & 0xFF), (byte)(read4Bytes >> 0 & 0xFF) }, "US-ASCII"));
                    sb.append(")");
                    out.println(sb.toString());
                    IoUtils.closeQuietly(true, byteArrayInputStream);
                    return;
                }
                finally {}
            }
            finally {
                closeable = null;
            }
            IoUtils.closeQuietly(false, closeable);
        }
    }, 
    TEXT_TYPE("textType", 1952807028) {
        @Override
        public void dump(final String str, final byte[] array) throws ImageReadException, IOException {
            Closeable closeable;
            try {
                final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
                try {
                    BinaryFunctions.read4Bytes("type_signature", byteArrayInputStream, "ICC: corrupt tag data", ByteOrder.BIG_ENDIAN);
                    BinaryFunctions.read4Bytes("ignore", byteArrayInputStream, "ICC: corrupt tag data", ByteOrder.BIG_ENDIAN);
                    final String str2 = new String(array, 8, array.length - 8, "US-ASCII");
                    final PrintStream out = System.out;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append("s: '");
                    sb.append(str2);
                    sb.append("'");
                    out.println(sb.toString());
                    IoUtils.closeQuietly(true, byteArrayInputStream);
                    return;
                }
                finally {}
            }
            finally {
                closeable = null;
            }
            IoUtils.closeQuietly(false, closeable);
        }
    };
    
    public final String name;
    public final int signature;
    
    static {
        $VALUES = new IccTagDataTypes[] { IccTagDataTypes.DESC_TYPE, IccTagDataTypes.DATA_TYPE, IccTagDataTypes.MULTI_LOCALIZED_UNICODE_TYPE, IccTagDataTypes.SIGNATURE_TYPE, IccTagDataTypes.TEXT_TYPE };
    }
    
    private IccTagDataTypes(final String name2, final int signature) {
        this.name = name2;
        this.signature = signature;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public int getSignature() {
        return this.signature;
    }
}
