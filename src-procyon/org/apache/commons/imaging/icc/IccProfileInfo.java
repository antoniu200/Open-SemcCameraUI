// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.icc;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import java.io.Writer;
import java.io.StringWriter;
import java.io.PrintWriter;

public class IccProfileInfo
{
    public final int cmmTypeSignature;
    public final int colorSpace;
    private final byte[] data;
    public final int deviceManufacturer;
    public final int deviceModel;
    public final int primaryPlatformSignature;
    public final int profileConnectionSpace;
    public final int profileCreatorSignature;
    public final int profileDeviceClassSignature;
    public final int profileFileSignature;
    private final byte[] profileId;
    public final int profileSize;
    public final int profileVersion;
    public final int renderingIntent;
    private final IccTag[] tags;
    public final int variousFlags;
    
    public IccProfileInfo(final byte[] data, final int profileSize, final int cmmTypeSignature, final int profileVersion, final int profileDeviceClassSignature, final int colorSpace, final int profileConnectionSpace, final int profileFileSignature, final int primaryPlatformSignature, final int variousFlags, final int deviceManufacturer, final int deviceModel, final int renderingIntent, final int profileCreatorSignature, final byte[] profileId, final IccTag[] tags) {
        this.data = data;
        this.profileSize = profileSize;
        this.cmmTypeSignature = cmmTypeSignature;
        this.profileVersion = profileVersion;
        this.profileDeviceClassSignature = profileDeviceClassSignature;
        this.colorSpace = colorSpace;
        this.profileConnectionSpace = profileConnectionSpace;
        this.profileFileSignature = profileFileSignature;
        this.primaryPlatformSignature = primaryPlatformSignature;
        this.variousFlags = variousFlags;
        this.deviceManufacturer = deviceManufacturer;
        this.deviceModel = deviceModel;
        this.renderingIntent = renderingIntent;
        this.profileCreatorSignature = profileCreatorSignature;
        this.profileId = profileId;
        this.tags = tags;
    }
    
    private void printCharQuad(final PrintWriter printWriter, final String str, final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(": '");
        sb.append((char)(n >> 24 & 0xFF));
        sb.append((char)(n >> 16 & 0xFF));
        sb.append((char)(n >> 8 & 0xFF));
        sb.append((char)(n >> 0 & 0xFF));
        sb.append("'");
        printWriter.println(sb.toString());
    }
    
    public void dump(final String s) {
        System.out.print(this.toString());
    }
    
    public byte[] getData() {
        return this.data;
    }
    
    public byte[] getProfileId() {
        return this.profileId;
    }
    
    public IccTag[] getTags() {
        return this.tags;
    }
    
    public boolean issRGB() {
        return this.deviceManufacturer == 1229275936 && this.deviceModel == 1934772034;
    }
    
    @Override
    public String toString() {
        try {
            return this.toString("");
        }
        catch (final Exception ex) {
            return "IccProfileInfo: Error";
        }
    }
    
    public String toString(final String s) throws ImageReadException, IOException {
        final StringWriter out = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(out);
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(": ");
        sb.append("data length: ");
        sb.append(this.data.length);
        printWriter.println(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append(": ");
        sb2.append("ProfileDeviceClassSignature");
        this.printCharQuad(printWriter, sb2.toString(), this.profileDeviceClassSignature);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append(": ");
        sb3.append("CMMTypeSignature");
        this.printCharQuad(printWriter, sb3.toString(), this.cmmTypeSignature);
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(s);
        sb4.append(": ");
        sb4.append("ProfileDeviceClassSignature");
        this.printCharQuad(printWriter, sb4.toString(), this.profileDeviceClassSignature);
        final StringBuilder sb5 = new StringBuilder();
        sb5.append(s);
        sb5.append(": ");
        sb5.append("ColorSpace");
        this.printCharQuad(printWriter, sb5.toString(), this.colorSpace);
        final StringBuilder sb6 = new StringBuilder();
        sb6.append(s);
        sb6.append(": ");
        sb6.append("ProfileConnectionSpace");
        this.printCharQuad(printWriter, sb6.toString(), this.profileConnectionSpace);
        final StringBuilder sb7 = new StringBuilder();
        sb7.append(s);
        sb7.append(": ");
        sb7.append("ProfileFileSignature");
        this.printCharQuad(printWriter, sb7.toString(), this.profileFileSignature);
        final StringBuilder sb8 = new StringBuilder();
        sb8.append(s);
        sb8.append(": ");
        sb8.append("PrimaryPlatformSignature");
        this.printCharQuad(printWriter, sb8.toString(), this.primaryPlatformSignature);
        final StringBuilder sb9 = new StringBuilder();
        sb9.append(s);
        sb9.append(": ");
        sb9.append("ProfileFileSignature");
        this.printCharQuad(printWriter, sb9.toString(), this.profileFileSignature);
        final StringBuilder sb10 = new StringBuilder();
        sb10.append(s);
        sb10.append(": ");
        sb10.append("DeviceManufacturer");
        this.printCharQuad(printWriter, sb10.toString(), this.deviceManufacturer);
        final StringBuilder sb11 = new StringBuilder();
        sb11.append(s);
        sb11.append(": ");
        sb11.append("DeviceModel");
        this.printCharQuad(printWriter, sb11.toString(), this.deviceModel);
        final StringBuilder sb12 = new StringBuilder();
        sb12.append(s);
        sb12.append(": ");
        sb12.append("RenderingIntent");
        this.printCharQuad(printWriter, sb12.toString(), this.renderingIntent);
        final StringBuilder sb13 = new StringBuilder();
        sb13.append(s);
        sb13.append(": ");
        sb13.append("ProfileCreatorSignature");
        this.printCharQuad(printWriter, sb13.toString(), this.profileCreatorSignature);
        for (int i = 0; i < this.tags.length; ++i) {
            final IccTag iccTag = this.tags[i];
            final StringBuilder sb14 = new StringBuilder();
            sb14.append("\t");
            sb14.append(i);
            sb14.append(": ");
            iccTag.dump(printWriter, sb14.toString());
        }
        final StringBuilder sb15 = new StringBuilder();
        sb15.append(s);
        sb15.append(": ");
        sb15.append("issRGB: ");
        sb15.append(this.issRGB());
        printWriter.println(sb15.toString());
        printWriter.flush();
        return out.getBuffer().toString();
    }
}
