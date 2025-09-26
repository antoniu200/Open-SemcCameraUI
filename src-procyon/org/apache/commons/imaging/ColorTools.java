// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging;

import java.util.Hashtable;
import java.awt.image.DirectColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ImagingOpException;
import java.io.IOException;
import java.io.File;
import java.awt.image.ColorModel;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.ColorConvertOp;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

public class ColorTools
{
    private int countBitsInMask(int i) {
        int n = 0;
        while (i != 0) {
            n += (i & 0x1);
            i >>>= 1;
        }
        return n;
    }
    
    public BufferedImage convertBetweenColorSpaces(final BufferedImage bufferedImage, final ColorSpace srcCspace, final ColorSpace dstCspace) {
        final RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        return this.relabelColorSpace(new ColorConvertOp(srcCspace, dstCspace, hints).filter(this.relabelColorSpace(bufferedImage, srcCspace), null), dstCspace);
    }
    
    public BufferedImage convertBetweenColorSpacesX2(BufferedImage relabelColorSpace, final ColorSpace srcCspace, final ColorSpace dstCspace) {
        final RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        relabelColorSpace = this.relabelColorSpace(relabelColorSpace, srcCspace);
        final ColorConvertOp colorConvertOp = new ColorConvertOp(srcCspace, dstCspace, hints);
        return this.relabelColorSpace(colorConvertOp.filter(this.relabelColorSpace(colorConvertOp.filter(relabelColorSpace, null), srcCspace), null), dstCspace);
    }
    
    public BufferedImage convertBetweenICCProfiles(final BufferedImage bufferedImage, final ICC_Profile profile, final ICC_Profile profile2) {
        return this.convertBetweenColorSpaces(bufferedImage, new ICC_ColorSpace(profile), new ICC_ColorSpace(profile2));
    }
    
    protected BufferedImage convertFromColorSpace(final BufferedImage bufferedImage, final ColorSpace colorSpace) {
        return this.convertBetweenColorSpaces(bufferedImage, colorSpace, ColorModel.getRGBdefault().getColorSpace());
    }
    
    public BufferedImage convertToColorSpace(final BufferedImage src, final ColorSpace dstCspace) {
        final ColorSpace colorSpace = src.getColorModel().getColorSpace();
        final RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        return this.relabelColorSpace(new ColorConvertOp(colorSpace, dstCspace, hints).filter(src, null), dstCspace);
    }
    
    public BufferedImage convertToICCProfile(final BufferedImage bufferedImage, final ICC_Profile profile) {
        return this.convertToColorSpace(bufferedImage, new ICC_ColorSpace(profile));
    }
    
    public BufferedImage convertTosRGB(final BufferedImage bufferedImage) {
        return this.convertToColorSpace(bufferedImage, ColorModel.getRGBdefault().getColorSpace());
    }
    
    public BufferedImage correctImage(final BufferedImage bufferedImage, final File file) throws ImageReadException, IOException {
        final ICC_Profile iccProfile = Imaging.getICCProfile(file);
        if (iccProfile == null) {
            return bufferedImage;
        }
        return this.convertFromColorSpace(bufferedImage, new ICC_ColorSpace(iccProfile));
    }
    
    public ColorModel deriveColorModel(final BufferedImage bufferedImage, final ColorSpace colorSpace) throws ImagingOpException {
        return this.deriveColorModel(bufferedImage, colorSpace, false);
    }
    
    public ColorModel deriveColorModel(final BufferedImage bufferedImage, final ColorSpace colorSpace, final boolean b) throws ImagingOpException {
        return this.deriveColorModel(bufferedImage.getColorModel(), colorSpace, b);
    }
    
    public ColorModel deriveColorModel(final ColorModel colorModel, final ColorSpace space, final boolean b) throws ImagingOpException {
        if (colorModel instanceof ComponentColorModel) {
            final ComponentColorModel componentColorModel = (ComponentColorModel)colorModel;
            if (b) {
                return new ComponentColorModel(space, false, false, 1, componentColorModel.getTransferType());
            }
            return new ComponentColorModel(space, componentColorModel.hasAlpha(), componentColorModel.isAlphaPremultiplied(), componentColorModel.getTransparency(), componentColorModel.getTransferType());
        }
        else {
            if (colorModel instanceof DirectColorModel) {
                final DirectColorModel directColorModel = (DirectColorModel)colorModel;
                return new DirectColorModel(space, this.countBitsInMask(directColorModel.getRedMask() | directColorModel.getGreenMask() | directColorModel.getBlueMask() | directColorModel.getAlphaMask()), directColorModel.getRedMask(), directColorModel.getGreenMask(), directColorModel.getBlueMask(), directColorModel.getAlphaMask(), directColorModel.isAlphaPremultiplied(), directColorModel.getTransferType());
            }
            throw new ImagingOpException("Could not clone unknown ColorModel Type.");
        }
    }
    
    public BufferedImage relabelColorSpace(final BufferedImage bufferedImage, final ColorSpace colorSpace) throws ImagingOpException {
        return this.relabelColorSpace(bufferedImage, this.deriveColorModel(bufferedImage, colorSpace));
    }
    
    public BufferedImage relabelColorSpace(final BufferedImage bufferedImage, final ICC_Profile profile) throws ImagingOpException {
        return this.relabelColorSpace(bufferedImage, new ICC_ColorSpace(profile));
    }
    
    public BufferedImage relabelColorSpace(final BufferedImage bufferedImage, final ColorModel cm) throws ImagingOpException {
        return new BufferedImage(cm, bufferedImage.getRaster(), false, null);
    }
}
