package org.apache.commons.imaging.formats.png.chunks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PngChunkIccp extends PngChunk {
    private final byte[] compressedProfile;
    public final int compressionMethod;
    public final String profileName;
    private final byte[] uncompressedProfile;

    public byte[] getUncompressedProfile() {
        return this.uncompressedProfile;
    }

    public PngChunkIccp(int i, int i2, int i3, byte[] bArr) throws IOException, ImageReadException {
        super(i, i2, i3, bArr);
        int iFindNull = BinaryFunctions.findNull(bArr);
        if (iFindNull < 0) {
            throw new ImageReadException("PngChunkIccp: No Profile Name");
        }
        byte[] bArr2 = new byte[iFindNull];
        System.arraycopy(bArr, 0, bArr2, 0, iFindNull);
        this.profileName = new String(bArr2, "ISO-8859-1");
        int i4 = iFindNull + 1;
        this.compressionMethod = bArr[i4];
        int i5 = i4 + 1;
        int length = bArr.length - i5;
        this.compressedProfile = new byte[length];
        System.arraycopy(bArr, i5, this.compressedProfile, 0, length);
        if (getDebug()) {
            System.out.println("ProfileName: " + this.profileName);
            System.out.println("ProfileName.length(): " + this.profileName.length());
            System.out.println("CompressionMethod: " + this.compressionMethod);
            System.out.println("CompressedProfileLength: " + length);
            System.out.println("bytes.length: " + bArr.length);
        }
        this.uncompressedProfile = BinaryFunctions.getStreamBytes(new InflaterInputStream(new ByteArrayInputStream(this.compressedProfile)));
        if (getDebug()) {
            System.out.println("UncompressedProfile: " + Integer.toString(bArr.length));
        }
    }
}
