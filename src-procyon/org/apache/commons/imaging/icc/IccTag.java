// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.icc;

import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.nio.ByteOrder;
import java.io.ByteArrayInputStream;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import java.util.Arrays;
import java.io.PrintWriter;

public class IccTag
{
    private byte[] data;
    private int dataTypeSignature;
    public final IccTagType fIccTagType;
    private IccTagDataType itdt;
    public final int length;
    public final int offset;
    public final int signature;
    
    public IccTag(final int signature, final int offset, final int length, final IccTagType fIccTagType) {
        this.signature = signature;
        this.offset = offset;
        this.length = length;
        this.fIccTagType = fIccTagType;
    }
    
    private IccTagDataType getIccTagDataType(final int n) {
        for (final IccTagDataTypes iccTagDataTypes : IccTagDataTypes.values()) {
            if (iccTagDataTypes.getSignature() == n) {
                return iccTagDataTypes;
            }
        }
        return null;
    }
    
    public void dump(final PrintWriter printWriter, final String s) throws ImageReadException, IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("tag signature: ");
        sb.append(Integer.toHexString(this.signature));
        sb.append(" (");
        sb.append(new String(new byte[] { (byte)(this.signature >> 24 & 0xFF), (byte)(this.signature >> 16 & 0xFF), (byte)(this.signature >> 8 & 0xFF), (byte)(this.signature >> 0 & 0xFF) }, "US-ASCII"));
        sb.append(")");
        printWriter.println(sb.toString());
        if (this.data == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("data: ");
            sb2.append(Arrays.toString(this.data));
            printWriter.println(sb2.toString());
        }
        else {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append("data: ");
            sb3.append(this.data.length);
            printWriter.println(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(s);
            sb4.append("data type signature: ");
            sb4.append(Integer.toHexString(this.dataTypeSignature));
            sb4.append(" (");
            sb4.append(new String(new byte[] { (byte)(this.dataTypeSignature >> 24 & 0xFF), (byte)(this.dataTypeSignature >> 16 & 0xFF), (byte)(this.dataTypeSignature >> 8 & 0xFF), (byte)(this.dataTypeSignature >> 0 & 0xFF) }, "US-ASCII"));
            sb4.append(")");
            printWriter.println(sb4.toString());
            if (this.itdt == null) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(s);
                sb5.append("IccTagType : ");
                sb5.append("unknown");
                printWriter.println(sb5.toString());
            }
            else {
                final StringBuilder sb6 = new StringBuilder();
                sb6.append(s);
                sb6.append("IccTagType : ");
                sb6.append(this.itdt.getName());
                printWriter.println(sb6.toString());
                this.itdt.dump(s, this.data);
            }
        }
        printWriter.println("");
        printWriter.flush();
    }
    
    public void dump(final String s) throws ImageReadException, IOException {
        final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
        this.dump(printWriter, s);
        printWriter.flush();
    }
    
    public void setData(final byte[] array) throws IOException {
        this.data = array;
        Closeable closeable;
        try {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
            try {
                this.dataTypeSignature = BinaryFunctions.read4Bytes("data type signature", byteArrayInputStream, "ICC: corrupt tag data", ByteOrder.BIG_ENDIAN);
                this.itdt = this.getIccTagDataType(this.dataTypeSignature);
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
}
