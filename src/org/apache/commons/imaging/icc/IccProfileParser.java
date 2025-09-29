package org.apache.commons.imaging.icc;

import java.awt.color.ICC_Profile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.BinaryFileParser;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceArray;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import org.apache.commons.imaging.util.Debug;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class IccProfileParser extends BinaryFileParser {
    public IccProfileParser() {
        setByteOrder(ByteOrder.BIG_ENDIAN);
    }

    public IccProfileInfo getICCProfileInfo(ICC_Profile iCC_Profile) {
        if (iCC_Profile == null) {
            return null;
        }
        return getICCProfileInfo(new ByteSourceArray(iCC_Profile.getData()));
    }

    public IccProfileInfo getICCProfileInfo(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return getICCProfileInfo(new ByteSourceArray(bArr));
    }

    public IccProfileInfo getICCProfileInfo(File file) {
        if (file == null) {
            return null;
        }
        return getICCProfileInfo(new ByteSourceFile(file));
    }

    public IccProfileInfo getICCProfileInfo(final ByteSource byteSource) {
        final InputStream inputStream = null;
        InputStream inputStream4 = null;
        Label_0171: {
            Object inputStream2 = null;
            InputStream inputStream3;
            try {
                inputStream2 = byteSource.getInputStream();
                try {
                    try {
                        final IccProfileInfo iccProfileInfo = this.readICCProfileInfo((InputStream)inputStream2);
                        if (iccProfileInfo == null) {
                            if (inputStream2 != null) {
                                try {
                                    ((InputStream)inputStream2).close();
                                }
                                catch (final Exception ex) {
                                    Debug.debug(ex);
                                }
                            }
                            return null;
                        }
                        ((InputStream)inputStream2).close();
                        final IccTag[] tags = iccProfileInfo.getTags();
                        for (int length = tags.length, i = 0; i < length; ++i) {
                            inputStream2 = tags[i];
                            ((IccTag)inputStream2).setData(byteSource.getBlock(((IccTag)inputStream2).offset, ((IccTag)inputStream2).length));
                        }
                        return iccProfileInfo;
                    }
                    finally {}
                }
                catch (final Exception ex2) {}
            }
            catch (final Exception inputStream2) {
                inputStream3 = null;
            }
            finally {
                inputStream4 = inputStream;
                break Label_0171;
            }
            Debug.debug((Throwable)inputStream2);
            if (inputStream3 != null) {
                try {
                    inputStream3.close();
                }
                catch (final Exception ex3) {
                    Debug.debug(ex3);
                }
            }
            if (this.getDebug()) {
                Debug.debug();
            }
            return null;
        }
        if (inputStream4 != null) {
            try {
                inputStream4.close();
            }
            catch (final Exception ex4) {
                Debug.debug(ex4);
            }
        }
    }

    private IccProfileInfo readICCProfileInfo(InputStream inputStream) throws IOException {
        CachingInputStream cachingInputStream = new CachingInputStream(inputStream);
        if (getDebug()) {
            Debug.debug();
        }
        try {
            int i = BinaryFunctions.read4Bytes("ProfileSize", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            int i2 = BinaryFunctions.read4Bytes("Signature", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("CMMTypeSignature", i2);
            }
            int i3 = BinaryFunctions.read4Bytes("ProfileVersion", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            int i4 = BinaryFunctions.read4Bytes("ProfileDeviceClassSignature", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("ProfileDeviceClassSignature", i4);
            }
            int i5 = BinaryFunctions.read4Bytes("ColorSpace", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("ColorSpace", i5);
            }
            int i6 = BinaryFunctions.read4Bytes("ProfileConnectionSpace", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("ProfileConnectionSpace", i6);
            }
            BinaryFunctions.skipBytes(cachingInputStream, 12L, "Not a Valid ICC Profile");
            int i7 = BinaryFunctions.read4Bytes("ProfileFileSignature", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("ProfileFileSignature", i7);
            }
            int i8 = BinaryFunctions.read4Bytes("PrimaryPlatformSignature", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("PrimaryPlatformSignature", i8);
            }
            int i9 = BinaryFunctions.read4Bytes("VariousFlags", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("VariousFlags", i7);
            }
            int i10 = BinaryFunctions.read4Bytes("DeviceManufacturer", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("DeviceManufacturer", i10);
            }
            int i11 = BinaryFunctions.read4Bytes("DeviceModel", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("DeviceModel", i11);
            }
            BinaryFunctions.skipBytes(cachingInputStream, 8L, "Not a Valid ICC Profile");
            int i12 = BinaryFunctions.read4Bytes("RenderingIntent", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("RenderingIntent", i12);
            }
            BinaryFunctions.skipBytes(cachingInputStream, 12L, "Not a Valid ICC Profile");
            int i13 = BinaryFunctions.read4Bytes("ProfileCreatorSignature", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("ProfileCreatorSignature", i13);
            }
            BinaryFunctions.skipBytes(cachingInputStream, 16L, "Not a Valid ICC Profile");
            BinaryFunctions.skipBytes(cachingInputStream, 28L, "Not a Valid ICC Profile");
            int i14 = BinaryFunctions.read4Bytes("TagCount", cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
            IccTag[] iccTagArr = new IccTag[i14];
            int i15 = 0;
            while (i15 < i14) {
                int i16 = i14;
                StringBuilder sb = new StringBuilder();
                int i17 = i13;
                sb.append("TagSignature[");
                sb.append(i15);
                sb.append("]");
                int i18 = i10;
                int i19 = BinaryFunctions.read4Bytes(sb.toString(), cachingInputStream, "Not a Valid ICC Profile", getByteOrder());
                iccTagArr[i15] = new IccTag(i19, BinaryFunctions.read4Bytes("OffsetToData[" + i15 + "]", cachingInputStream, "Not a Valid ICC Profile", getByteOrder()), BinaryFunctions.read4Bytes("ElementSize[" + i15 + "]", cachingInputStream, "Not a Valid ICC Profile", getByteOrder()), getIccTagType(i19));
                i15++;
                i14 = i16;
                i13 = i17;
                i10 = i18;
                i9 = i9;
                i8 = i8;
            }
            int i20 = i10;
            int i21 = i13;
            int i22 = i8;
            int i23 = i9;
            while (cachingInputStream.read() >= 0) {
            }
            byte[] cache = cachingInputStream.getCache();
            if (cache.length < i) {
                throw new IOException("Couldn't read ICC Profile.");
            }
            IccProfileInfo iccProfileInfo = new IccProfileInfo(cache, i, i2, i3, i4, i5, i6, i7, i22, i23, i20, i11, i12, i21, null, iccTagArr);
            if (getDebug()) {
                Debug.debug("issRGB: " + iccProfileInfo.issRGB());
            }
            return iccProfileInfo;
        } catch (Exception e) {
            Debug.debug(e);
            return null;
        }
    }

    private IccTagType getIccTagType(int i) {
        for (IccTagTypes iccTagTypes : IccTagTypes.values()) {
            if (iccTagTypes.getSignature() == i) {
                return iccTagTypes;
            }
        }
        return null;
    }

    public boolean issRGB(ICC_Profile iCC_Profile) throws IOException {
        return issRGB(new ByteSourceArray(iCC_Profile.getData()));
    }

    public boolean issRGB(byte[] bArr) throws IOException {
        return issRGB(new ByteSourceArray(bArr));
    }

    public boolean issRGB(File file) throws IOException {
        return issRGB(new ByteSourceFile(file));
    }

    public boolean issRGB(ByteSource byteSource) throws Throwable {
        InputStream inputStream;
        if (getDebug()) {
            Debug.debug();
        }
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            BinaryFunctions.read4Bytes("ProfileSize", inputStream, "Not a Valid ICC Profile", getByteOrder());
            BinaryFunctions.skipBytes(inputStream, 20L);
            BinaryFunctions.skipBytes(inputStream, 12L, "Not a Valid ICC Profile");
            BinaryFunctions.skipBytes(inputStream, 12L);
            int i = BinaryFunctions.read4Bytes("ProfileFileSignature", inputStream, "Not a Valid ICC Profile", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("DeviceManufacturer", i);
            }
            int i2 = BinaryFunctions.read4Bytes("DeviceModel", inputStream, "Not a Valid ICC Profile", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("DeviceModel", i2);
            }
            boolean z = i == 1229275936 && i2 == 1934772034;
            IoUtils.closeQuietly(true, inputStream);
            return z;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }
}
