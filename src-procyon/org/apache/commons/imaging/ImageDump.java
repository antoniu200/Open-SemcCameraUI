// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging;

import org.apache.commons.imaging.icc.IccProfileParser;
import java.awt.color.ICC_ColorSpace;
import java.io.PrintStream;
import java.awt.image.BufferedImage;
import java.awt.color.ColorSpace;

public class ImageDump
{
    private String colorSpaceTypeToName(final ColorSpace colorSpace) {
        final int type = colorSpace.getType();
        if (type == 5) {
            return "TYPE_RGB";
        }
        if (type == 9) {
            return "TYPE_CMYK";
        }
        switch (type) {
            default: {
                return "unknown";
            }
            case 1004: {
                return "CS_LINEAR_RGB";
            }
            case 1003: {
                return "CS_GRAY";
            }
            case 1002: {
                return "CS_PYCC";
            }
            case 1001: {
                return "CS_CIEXYZ";
            }
            case 1000: {
                return "CS_sRGB";
            }
        }
    }
    
    public void dump(final BufferedImage bufferedImage) {
        this.dump("", bufferedImage);
    }
    
    public void dump(final String str, final BufferedImage bufferedImage) {
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(": ");
        sb.append("dump");
        out.println(sb.toString());
        this.dumpColorSpace(str, bufferedImage.getColorModel().getColorSpace());
        this.dumpBIProps(str, bufferedImage);
    }
    
    public void dumpBIProps(final String s, final BufferedImage bufferedImage) {
        final String[] propertyNames = bufferedImage.getPropertyNames();
        if (propertyNames == null) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(": no props");
            out.println(sb.toString());
            return;
        }
        for (final String s2 : propertyNames) {
            final PrintStream out2 = System.out;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(": ");
            sb2.append(s2);
            sb2.append(": ");
            sb2.append(bufferedImage.getProperty(s2));
            out2.println(sb2.toString());
        }
    }
    
    public void dumpColorSpace(final String s, final ColorSpace colorSpace) {
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(": ");
        sb.append("type: ");
        sb.append(colorSpace.getType());
        sb.append(" (");
        sb.append(this.colorSpaceTypeToName(colorSpace));
        sb.append(")");
        out.println(sb.toString());
        if (!(colorSpace instanceof ICC_ColorSpace)) {
            final PrintStream out2 = System.out;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(": ");
            sb2.append("Unknown ColorSpace: ");
            sb2.append(colorSpace.getClass().getName());
            out2.println(sb2.toString());
            return;
        }
        new IccProfileParser().getICCProfileInfo(((ICC_ColorSpace)colorSpace).getProfile().getData()).dump(s);
    }
}
