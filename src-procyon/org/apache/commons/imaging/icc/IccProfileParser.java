// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.icc;

import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import java.io.File;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceArray;
import java.awt.color.ICC_Profile;
import java.io.IOException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.util.Debug;
import java.io.InputStream;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.BinaryFileParser;

public class IccProfileParser extends BinaryFileParser
{
    public IccProfileParser() {
        this.setByteOrder(ByteOrder.BIG_ENDIAN);
    }
    
    private IccTagType getIccTagType(final int n) {
        for (final IccTagTypes iccTagTypes : IccTagTypes.values()) {
            if (iccTagTypes.getSignature() == n) {
                return iccTagTypes;
            }
        }
        return null;
    }
    
    private IccProfileInfo readICCProfileInfo(final InputStream inputStream) {
        final CachingInputStream cachingInputStream = new CachingInputStream(inputStream);
        if (this.getDebug()) {
            Debug.debug();
        }
        try {
            final int read4Bytes = BinaryFunctions.read4Bytes("ProfileSize", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            final int read4Bytes2 = BinaryFunctions.read4Bytes("Signature", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            if (this.getDebug()) {
                BinaryFunctions.printCharQuad("CMMTypeSignature", read4Bytes2);
            }
            final int read4Bytes3 = BinaryFunctions.read4Bytes("ProfileVersion", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            final int read4Bytes4 = BinaryFunctions.read4Bytes("ProfileDeviceClassSignature", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            if (this.getDebug()) {
                BinaryFunctions.printCharQuad("ProfileDeviceClassSignature", read4Bytes4);
            }
            final int read4Bytes5 = BinaryFunctions.read4Bytes("ColorSpace", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            if (this.getDebug()) {
                BinaryFunctions.printCharQuad("ColorSpace", read4Bytes5);
            }
            final int read4Bytes6 = BinaryFunctions.read4Bytes("ProfileConnectionSpace", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            if (this.getDebug()) {
                BinaryFunctions.printCharQuad("ProfileConnectionSpace", read4Bytes6);
            }
            BinaryFunctions.skipBytes(cachingInputStream, 12L, "Not a Valid ICC Profile");
            final int read4Bytes7 = BinaryFunctions.read4Bytes("ProfileFileSignature", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            if (this.getDebug()) {
                BinaryFunctions.printCharQuad("ProfileFileSignature", read4Bytes7);
            }
            final int read4Bytes8 = BinaryFunctions.read4Bytes("PrimaryPlatformSignature", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            if (this.getDebug()) {
                BinaryFunctions.printCharQuad("PrimaryPlatformSignature", read4Bytes8);
            }
            final int read4Bytes9 = BinaryFunctions.read4Bytes("VariousFlags", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            if (this.getDebug()) {
                BinaryFunctions.printCharQuad("VariousFlags", read4Bytes7);
            }
            final int read4Bytes10 = BinaryFunctions.read4Bytes("DeviceManufacturer", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            if (this.getDebug()) {
                BinaryFunctions.printCharQuad("DeviceManufacturer", read4Bytes10);
            }
            final int read4Bytes11 = BinaryFunctions.read4Bytes("DeviceModel", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            if (this.getDebug()) {
                BinaryFunctions.printCharQuad("DeviceModel", read4Bytes11);
            }
            BinaryFunctions.skipBytes(cachingInputStream, 8L, "Not a Valid ICC Profile");
            final int read4Bytes12 = BinaryFunctions.read4Bytes("RenderingIntent", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            if (this.getDebug()) {
                BinaryFunctions.printCharQuad("RenderingIntent", read4Bytes12);
            }
            BinaryFunctions.skipBytes(cachingInputStream, 12L, "Not a Valid ICC Profile");
            final int read4Bytes13 = BinaryFunctions.read4Bytes("ProfileCreatorSignature", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            if (this.getDebug()) {
                BinaryFunctions.printCharQuad("ProfileCreatorSignature", read4Bytes13);
            }
            BinaryFunctions.skipBytes(cachingInputStream, 16L, "Not a Valid ICC Profile");
            BinaryFunctions.skipBytes(cachingInputStream, 28L, "Not a Valid ICC Profile");
            final int read4Bytes14 = BinaryFunctions.read4Bytes("TagCount", cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
            final IccTag[] array = new IccTag[read4Bytes14];
            for (int i = 0; i < read4Bytes14; ++i) {
                final StringBuilder sb = new StringBuilder();
                sb.append("TagSignature[");
                sb.append(i);
                sb.append("]");
                final int read4Bytes15 = BinaryFunctions.read4Bytes(sb.toString(), cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("OffsetToData[");
                sb2.append(i);
                sb2.append("]");
                final int read4Bytes16 = BinaryFunctions.read4Bytes(sb2.toString(), cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder());
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("ElementSize[");
                sb3.append(i);
                sb3.append("]");
                array[i] = new IccTag(read4Bytes15, read4Bytes16, BinaryFunctions.read4Bytes(sb3.toString(), cachingInputStream, "Not a Valid ICC Profile", this.getByteOrder()), this.getIccTagType(read4Bytes15));
            }
            while (cachingInputStream.read() >= 0) {}
            final byte[] cache = cachingInputStream.getCache();
            if (cache.length < read4Bytes) {
                throw new IOException("Couldn't read ICC Profile.");
            }
            final IccProfileInfo iccProfileInfo = new IccProfileInfo(cache, read4Bytes, read4Bytes2, read4Bytes3, read4Bytes4, read4Bytes5, read4Bytes6, read4Bytes7, read4Bytes8, read4Bytes9, read4Bytes10, read4Bytes11, read4Bytes12, read4Bytes13, null, array);
            if (this.getDebug()) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("issRGB: ");
                sb4.append(iccProfileInfo.issRGB());
                Debug.debug(sb4.toString());
            }
            return iccProfileInfo;
        }
        catch (final Exception ex) {
            Debug.debug(ex);
            return null;
        }
    }
    
    public IccProfileInfo getICCProfileInfo(final ICC_Profile icc_Profile) {
        if (icc_Profile == null) {
            return null;
        }
        return this.getICCProfileInfo(new ByteSourceArray(icc_Profile.getData()));
    }
    
    public IccProfileInfo getICCProfileInfo(final File file) {
        if (file == null) {
            return null;
        }
        return this.getICCProfileInfo(new ByteSourceFile(file));
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
    
    public IccProfileInfo getICCProfileInfo(final byte[] array) {
        if (array == null) {
            return null;
        }
        return this.getICCProfileInfo(new ByteSourceArray(array));
    }
    
    public boolean issRGB(final ICC_Profile icc_Profile) throws IOException {
        return this.issRGB(new ByteSourceArray(icc_Profile.getData()));
    }
    
    public boolean issRGB(final File file) throws IOException {
        return this.issRGB(new ByteSourceFile(file));
    }
    
    public boolean issRGB(final ByteSource byteSource) throws IOException {
        if (this.getDebug()) {
            Debug.debug();
        }
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                BinaryFunctions.read4Bytes("ProfileSize", inputStream, "Not a Valid ICC Profile", this.getByteOrder());
                BinaryFunctions.skipBytes(inputStream, 20L);
                BinaryFunctions.skipBytes(inputStream, 12L, "Not a Valid ICC Profile");
                BinaryFunctions.skipBytes(inputStream, 12L);
                final int read4Bytes = BinaryFunctions.read4Bytes("ProfileFileSignature", inputStream, "Not a Valid ICC Profile", this.getByteOrder());
                if (this.getDebug()) {
                    BinaryFunctions.printCharQuad("DeviceManufacturer", read4Bytes);
                }
                final int read4Bytes2 = BinaryFunctions.read4Bytes("DeviceModel", inputStream, "Not a Valid ICC Profile", this.getByteOrder());
                if (this.getDebug()) {
                    BinaryFunctions.printCharQuad("DeviceModel", read4Bytes2);
                }
                final boolean b = read4Bytes == 1229275936 && read4Bytes2 == 1934772034;
                IoUtils.closeQuietly(true, inputStream);
                return b;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    public boolean issRGB(final byte[] array) throws IOException {
        return this.issRGB(new ByteSourceArray(array));
    }
}
