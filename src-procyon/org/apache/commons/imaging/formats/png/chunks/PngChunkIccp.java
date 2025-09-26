// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.chunks;

import java.io.IOException;
import java.io.PrintStream;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;
import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;

public class PngChunkIccp extends PngChunk
{
    private final byte[] compressedProfile;
    public final int compressionMethod;
    public final String profileName;
    private final byte[] uncompressedProfile;
    
    public PngChunkIccp(int i, int n, final int n2, final byte[] array) throws ImageReadException, IOException {
        super(i, n, n2, array);
        i = BinaryFunctions.findNull(array);
        if (i < 0) {
            throw new ImageReadException("PngChunkIccp: No Profile Name");
        }
        final byte[] bytes = new byte[i];
        System.arraycopy(array, 0, bytes, 0, i);
        this.profileName = new String(bytes, "ISO-8859-1");
        n = i + 1;
        this.compressionMethod = array[n];
        i = array.length;
        ++n;
        i -= n;
        System.arraycopy(array, n, this.compressedProfile = new byte[i], 0, i);
        if (this.getDebug()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("ProfileName: ");
            sb.append(this.profileName);
            out.println(sb.toString());
            final PrintStream out2 = System.out;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("ProfileName.length(): ");
            sb2.append(this.profileName.length());
            out2.println(sb2.toString());
            final PrintStream out3 = System.out;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("CompressionMethod: ");
            sb3.append(this.compressionMethod);
            out3.println(sb3.toString());
            final PrintStream out4 = System.out;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("CompressedProfileLength: ");
            sb4.append(i);
            out4.println(sb4.toString());
            final PrintStream out5 = System.out;
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("bytes.length: ");
            sb5.append(array.length);
            out5.println(sb5.toString());
        }
        this.uncompressedProfile = BinaryFunctions.getStreamBytes(new InflaterInputStream(new ByteArrayInputStream(this.compressedProfile)));
        if (this.getDebug()) {
            final PrintStream out6 = System.out;
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("UncompressedProfile: ");
            sb6.append(Integer.toString(array.length));
            out6.println(sb6.toString());
        }
    }
    
    public byte[] getUncompressedProfile() {
        return this.uncompressedProfile;
    }
}
