package org.apache.commons.imaging.icc;

import android.support.v4.os.EnvironmentCompat;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class IccTag {
    private byte[] data;
    private int dataTypeSignature;
    public final IccTagType fIccTagType;
    private IccTagDataType itdt;
    public final int length;
    public final int offset;
    public final int signature;

    public IccTag(int i, int i2, int i3, IccTagType iccTagType) {
        this.signature = i;
        this.offset = i2;
        this.length = i3;
        this.fIccTagType = iccTagType;
    }

    public void setData(byte[] bArr) throws Throwable {
        ByteArrayInputStream byteArrayInputStream;
        this.data = bArr;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bArr);
        } catch (Throwable th) {
            th = th;
            byteArrayInputStream = null;
        }
        try {
            this.dataTypeSignature = BinaryFunctions.read4Bytes("data type signature", byteArrayInputStream, "ICC: corrupt tag data", ByteOrder.BIG_ENDIAN);
            this.itdt = getIccTagDataType(this.dataTypeSignature);
            IoUtils.closeQuietly(true, byteArrayInputStream);
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, byteArrayInputStream);
            throw th;
        }
    }

    private IccTagDataType getIccTagDataType(int i) {
        for (IccTagDataTypes iccTagDataTypes : IccTagDataTypes.values()) {
            if (iccTagDataTypes.getSignature() == i) {
                return iccTagDataTypes;
            }
        }
        return null;
    }

    public void dump(String str) throws IOException, ImageReadException {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
        dump(printWriter, str);
        printWriter.flush();
    }

    public void dump(PrintWriter printWriter, String str) throws IOException, ImageReadException {
        printWriter.println(str + "tag signature: " + Integer.toHexString(this.signature) + " (" + new String(new byte[]{(byte) ((this.signature >> 24) & 255), (byte) ((this.signature >> 16) & 255), (byte) ((this.signature >> 8) & 255), (byte) ((this.signature >> 0) & 255)}, "US-ASCII") + ")");
        if (this.data == null) {
            printWriter.println(str + "data: " + Arrays.toString(this.data));
        } else {
            printWriter.println(str + "data: " + this.data.length);
            printWriter.println(str + "data type signature: " + Integer.toHexString(this.dataTypeSignature) + " (" + new String(new byte[]{(byte) ((this.dataTypeSignature >> 24) & 255), (byte) ((this.dataTypeSignature >> 16) & 255), (byte) ((this.dataTypeSignature >> 8) & 255), (byte) ((this.dataTypeSignature >> 0) & 255)}, "US-ASCII") + ")");
            if (this.itdt == null) {
                printWriter.println(str + "IccTagType : " + EnvironmentCompat.MEDIA_UNKNOWN);
            } else {
                printWriter.println(str + "IccTagType : " + this.itdt.getName());
                this.itdt.dump(str, this.data);
            }
        }
        printWriter.println("");
        printWriter.flush();
    }
}
