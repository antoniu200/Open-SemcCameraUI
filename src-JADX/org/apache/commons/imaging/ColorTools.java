package org.apache.commons.imaging;

import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ColorTools {
    private int countBitsInMask(int i) {
        int i2 = 0;
        while (i != 0) {
            i2 += i & 1;
            i >>>= 1;
        }
        return i2;
    }

    public BufferedImage correctImage(BufferedImage bufferedImage, File file) throws IOException, ImageReadException {
        ICC_Profile iCCProfile = Imaging.getICCProfile(file);
        return iCCProfile == null ? bufferedImage : convertFromColorSpace(bufferedImage, new ICC_ColorSpace(iCCProfile));
    }

    public BufferedImage relabelColorSpace(BufferedImage bufferedImage, ICC_Profile iCC_Profile) throws ImagingOpException {
        return relabelColorSpace(bufferedImage, (ColorSpace) new ICC_ColorSpace(iCC_Profile));
    }

    public BufferedImage relabelColorSpace(BufferedImage bufferedImage, ColorSpace colorSpace) throws ImagingOpException {
        return relabelColorSpace(bufferedImage, deriveColorModel(bufferedImage, colorSpace));
    }

    public BufferedImage relabelColorSpace(BufferedImage bufferedImage, ColorModel colorModel) throws ImagingOpException {
        return new BufferedImage(colorModel, bufferedImage.getRaster(), false, (Hashtable) null);
    }

    public ColorModel deriveColorModel(BufferedImage bufferedImage, ColorSpace colorSpace) throws ImagingOpException {
        return deriveColorModel(bufferedImage, colorSpace, false);
    }

    public ColorModel deriveColorModel(BufferedImage bufferedImage, ColorSpace colorSpace, boolean z) throws ImagingOpException {
        return deriveColorModel(bufferedImage.getColorModel(), colorSpace, z);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.awt.image.ImagingOpException */
    public ColorModel deriveColorModel(ColorModel colorModel, ColorSpace colorSpace, boolean z) throws ImagingOpException {
        if (colorModel instanceof ComponentColorModel) {
            ComponentColorModel componentColorModel = (ComponentColorModel) colorModel;
            if (z) {
                return new ComponentColorModel(colorSpace, false, false, 1, componentColorModel.getTransferType());
            }
            return new ComponentColorModel(colorSpace, componentColorModel.hasAlpha(), componentColorModel.isAlphaPremultiplied(), componentColorModel.getTransparency(), componentColorModel.getTransferType());
        }
        if (colorModel instanceof DirectColorModel) {
            DirectColorModel directColorModel = (DirectColorModel) colorModel;
            return new DirectColorModel(colorSpace, countBitsInMask(directColorModel.getRedMask() | directColorModel.getGreenMask() | directColorModel.getBlueMask() | directColorModel.getAlphaMask()), directColorModel.getRedMask(), directColorModel.getGreenMask(), directColorModel.getBlueMask(), directColorModel.getAlphaMask(), directColorModel.isAlphaPremultiplied(), directColorModel.getTransferType());
        }
        throw new ImagingOpException("Could not clone unknown ColorModel Type.");
    }

    public BufferedImage convertToColorSpace(BufferedImage bufferedImage, ColorSpace colorSpace) {
        ColorSpace colorSpace2 = bufferedImage.getColorModel().getColorSpace();
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        return relabelColorSpace(new ColorConvertOp(colorSpace2, colorSpace, renderingHints).filter(bufferedImage, (BufferedImage) null), colorSpace);
    }

    public BufferedImage convertTosRGB(BufferedImage bufferedImage) {
        return convertToColorSpace(bufferedImage, ColorModel.getRGBdefault().getColorSpace());
    }

    protected BufferedImage convertFromColorSpace(BufferedImage bufferedImage, ColorSpace colorSpace) {
        return convertBetweenColorSpaces(bufferedImage, colorSpace, ColorModel.getRGBdefault().getColorSpace());
    }

    public BufferedImage convertBetweenICCProfiles(BufferedImage bufferedImage, ICC_Profile iCC_Profile, ICC_Profile iCC_Profile2) {
        return convertBetweenColorSpaces(bufferedImage, new ICC_ColorSpace(iCC_Profile), new ICC_ColorSpace(iCC_Profile2));
    }

    public BufferedImage convertToICCProfile(BufferedImage bufferedImage, ICC_Profile iCC_Profile) {
        return convertToColorSpace(bufferedImage, new ICC_ColorSpace(iCC_Profile));
    }

    public BufferedImage convertBetweenColorSpacesX2(BufferedImage bufferedImage, ColorSpace colorSpace, ColorSpace colorSpace2) throws ImagingOpException {
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        BufferedImage bufferedImageRelabelColorSpace = relabelColorSpace(bufferedImage, colorSpace);
        ColorConvertOp colorConvertOp = new ColorConvertOp(colorSpace, colorSpace2, renderingHints);
        return relabelColorSpace(colorConvertOp.filter(relabelColorSpace(colorConvertOp.filter(bufferedImageRelabelColorSpace, (BufferedImage) null), colorSpace), (BufferedImage) null), colorSpace2);
    }

    public BufferedImage convertBetweenColorSpaces(BufferedImage bufferedImage, ColorSpace colorSpace, ColorSpace colorSpace2) {
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        return relabelColorSpace(new ColorConvertOp(colorSpace, colorSpace2, renderingHints).filter(relabelColorSpace(bufferedImage, colorSpace), (BufferedImage) null), colorSpace2);
    }
}
