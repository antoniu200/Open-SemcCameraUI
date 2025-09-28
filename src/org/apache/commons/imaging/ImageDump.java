package org.apache.commons.imaging;

import android.support.v4.os.EnvironmentCompat;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.image.BufferedImage;
import org.apache.commons.imaging.icc.IccProfileParser;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ImageDump {
    private String colorSpaceTypeToName(ColorSpace colorSpace) {
        int type = colorSpace.getType();
        if (type == 5) {
            return "TYPE_RGB";
        }
        if (type == 9) {
            return "TYPE_CMYK";
        }
        switch (type) {
            case 1000:
                return "CS_sRGB";
            case 1001:
                return "CS_CIEXYZ";
            case 1002:
                return "CS_PYCC";
            case 1003:
                return "CS_GRAY";
            case 1004:
                return "CS_LINEAR_RGB";
            default:
                return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }

    public void dumpColorSpace(String str, ColorSpace colorSpace) {
        System.out.println(str + ": type: " + colorSpace.getType() + " (" + colorSpaceTypeToName(colorSpace) + ")");
        if (!(colorSpace instanceof ICC_ColorSpace)) {
            System.out.println(str + ": Unknown ColorSpace: " + colorSpace.getClass().getName());
            return;
        }
        new IccProfileParser().getICCProfileInfo(((ICC_ColorSpace) colorSpace).getProfile().getData()).dump(str);
    }

    public void dump(BufferedImage bufferedImage) {
        dump("", bufferedImage);
    }

    public void dump(String str, BufferedImage bufferedImage) {
        System.out.println(str + ": dump");
        dumpColorSpace(str, bufferedImage.getColorModel().getColorSpace());
        dumpBIProps(str, bufferedImage);
    }

    public void dumpBIProps(String str, BufferedImage bufferedImage) {
        String[] propertyNames = bufferedImage.getPropertyNames();
        if (propertyNames == null) {
            System.out.println(str + ": no props");
            return;
        }
        for (String str2 : propertyNames) {
            System.out.println(str + ": " + str2 + ": " + bufferedImage.getProperty(str2));
        }
    }
}
