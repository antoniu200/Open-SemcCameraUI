// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.icns;

import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.List;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;

final class IcnsDecoder
{
    private static final int[] PALETTE_4BPP;
    private static final int[] PALETTE_8BPP;
    
    static {
        PALETTE_4BPP = new int[] { -1, -199931, -39934, -2291706, -915324, -12189531, -16777004, -16602134, -14698732, -16751599, -11129851, -7311046, -4144960, -8355712, -12566464, -16777216 };
        PALETTE_8BPP = $d2j$hex$22e02f5b$decode_I("ffffffffccffffff99ffffff66ffffff33ffffff00ffffffffccffffccccffff99ccffff66ccffff33ccffff00ccffffff99ffffcc99ffff9999ffff6699ffff3399ffff0099ffffff66ffffcc66ffff9966ffff6666ffff3366ffff0066ffffff33ffffcc33ffff9933ffff6633ffff3333ffff0033ffffff00ffffcc00ffff9900ffff6600ffff3300ffff0000ffffffffccffccffccff99ffccff66ffccff33ffccff00ffccffffccccffccccccff99ccccff66ccccff33ccccff00ccccffff99ccffcc99ccff9999ccff6699ccff3399ccff0099ccffff66ccffcc66ccff9966ccff6666ccff3366ccff0066ccffff33ccffcc33ccff9933ccff6633ccff3333ccff0033ccffff00ccffcc00ccff9900ccff6600ccff3300ccff0000ccffffff99ffccff99ff99ff99ff66ff99ff33ff99ff00ff99ffffcc99ffcccc99ff99cc99ff66cc99ff33cc99ff00cc99ffff9999ffcc9999ff999999ff669999ff339999ff009999ffff6699ffcc6699ff996699ff666699ff336699ff006699ffff3399ffcc3399ff993399ff663399ff333399ff003399ffff0099ffcc0099ff990099ff660099ff330099ff000099ffffff66ffccff66ff99ff66ff66ff66ff33ff66ff00ff66ffffcc66ffcccc66ff99cc66ff66cc66ff33cc66ff00cc66ffff9966ffcc9966ff999966ff669966ff339966ff009966ffff6666ffcc6666ff996666ff666666ff336666ff006666ffff3366ffcc3366ff993366ff663366ff333366ff003366ffff0066ffcc0066ff990066ff660066ff330066ff000066ffffff33ffccff33ff99ff33ff66ff33ff33ff33ff00ff33ffffcc33ffcccc33ff99cc33ff66cc33ff33cc33ff00cc33ffff9933ffcc9933ff999933ff669933ff339933ff009933ffff6633ffcc6633ff996633ff666633ff336633ff006633ffff3333ffcc3333ff993333ff663333ff333333ff003333ffff0033ffcc0033ff990033ff660033ff330033ff000033ffffff00ffccff00ff99ff00ff66ff00ff33ff00ff00ff00ffffcc00ffcccc00ff99cc00ff66cc00ff33cc00ff00cc00ffff9900ffcc9900ff999900ff669900ff339900ff009900ffff6600ffcc6600ff996600ff666600ff336600ff006600ffff3300ffcc3300ff993300ff663300ff333300ff003300ffff0000ffcc0000ff990000ff660000ff330000ff0000eeff0000ddff0000bbff0000aaff000088ff000077ff000055ff000044ff000022ff000011ff00ee00ff00dd00ff00bb00ff00aa00ff008800ff007700ff005500ff004400ff002200ff001100ffee0000ffdd0000ffbb0000ffaa0000ff880000ff770000ff550000ff440000ff220000ff110000ffeeeeeeffddddddffbbbbbbffaaaaaaff888888ff777777ff555555ff444444ff222222ff111111ff000000ff");
    }
    
    private IcnsDecoder() {
    }
    
    private static void apply1BPPMask(final byte[] array, final ImageBuilder imageBuilder) throws ImageReadException {
        int n = (imageBuilder.getWidth() * imageBuilder.getHeight() + 7) / 8;
        if (array.length >= 2 * n) {
            int i = 0;
            int n3;
            int n2 = n3 = 0;
            while (i < imageBuilder.getHeight()) {
                int n4 = n;
                final int n5 = 0;
                int n6 = n3;
                int n7 = n2;
                int n10;
                int n12;
                for (int j = n5; j < imageBuilder.getWidth(); ++j, n7 = n12, n4 = n10) {
                    final int n8 = 255;
                    int n9 = n7;
                    n10 = n4;
                    if (n7 == 0) {
                        n6 = (0xFF & array[n4]);
                        n10 = n4 + 1;
                        n9 = 8;
                    }
                    int n11;
                    if ((n6 & 0x80) != 0x0) {
                        n11 = n8;
                    }
                    else {
                        n11 = 0;
                    }
                    n6 <<= 1;
                    n12 = n9 - 1;
                    imageBuilder.setRGB(j, i, n11 << 24 | (0xFFFFFF & imageBuilder.getRGB(j, i)));
                }
                ++i;
                final int n13 = n4;
                final int n14 = n6;
                n = n13;
                n2 = n7;
                n3 = n14;
            }
            return;
        }
        throw new ImageReadException("1 BPP mask underrun parsing ICNS file");
    }
    
    private static void apply8BPPMask(final byte[] array, final ImageBuilder imageBuilder) {
        for (int i = 0; i < imageBuilder.getHeight(); ++i) {
            for (int j = 0; j < imageBuilder.getWidth(); ++j) {
                imageBuilder.setRGB(j, i, (0xFF & array[imageBuilder.getWidth() * i + j]) << 24 | (0xFFFFFF & imageBuilder.getRGB(j, i)));
            }
        }
    }
    
    private static void decode1BPPImage(final IcnsType icnsType, final byte[] array, final ImageBuilder imageBuilder) {
        final int n = 0;
        final int n2 = 0;
        int n4;
        int n3 = n4 = n2;
        int n5 = n2;
        for (int i = n; i < icnsType.getHeight(); ++i) {
            int n7;
            int n8;
            int n11;
            for (int j = 0; j < icnsType.getWidth(); ++j, n11 = n7 << 1, n4 = n8 - 1, n3 = n11) {
                if (n4 == 0) {
                    final int n6 = n5 + 1;
                    n7 = (0xFF & array[n5]);
                    n8 = 8;
                    n5 = n6;
                }
                else {
                    final int n9 = n4;
                    n7 = n3;
                    n8 = n9;
                }
                int n10;
                if ((n7 & 0x80) != 0x0) {
                    n10 = -16777216;
                }
                else {
                    n10 = -1;
                }
                imageBuilder.setRGB(j, i, n10);
            }
        }
    }
    
    private static void decode32BPPImage(final IcnsType icnsType, final byte[] array, final ImageBuilder imageBuilder) {
        for (int i = 0; i < icnsType.getHeight(); ++i) {
            for (int j = 0; j < icnsType.getWidth(); ++j) {
                imageBuilder.setRGB(j, i, 0xFF000000 | (array[(icnsType.getWidth() * i + j) * 4 + 1] & 0xFF) << 16 | (array[(icnsType.getWidth() * i + j) * 4 + 2] & 0xFF) << 8 | (array[4 * (icnsType.getWidth() * i + j) + 3] & 0xFF));
            }
        }
    }
    
    private static void decode4BPPImage(final IcnsType icnsType, final byte[] array, final ImageBuilder imageBuilder) {
        int i = 0;
        int n2;
        int n = n2 = 0;
        while (i < icnsType.getHeight()) {
            for (int j = 0; j < icnsType.getWidth(); ++j) {
                int n3;
                if (n2 == 0) {
                    n3 = (0xF & array[n] >> 4);
                }
                else {
                    n3 = (0xF & array[n]);
                    ++n;
                }
                n2 ^= 0x1;
                imageBuilder.setRGB(j, i, IcnsDecoder.PALETTE_4BPP[n3]);
            }
            ++i;
        }
    }
    
    private static void decode8BPPImage(final IcnsType icnsType, final byte[] array, final ImageBuilder imageBuilder) {
        for (int i = 0; i < icnsType.getHeight(); ++i) {
            for (int j = 0; j < icnsType.getWidth(); ++j) {
                imageBuilder.setRGB(j, i, IcnsDecoder.PALETTE_8BPP[0xFF & array[icnsType.getWidth() * i + j]]);
            }
        }
    }
    
    public static List<BufferedImage> decodeAllImages(final IcnsImageParser.IcnsElement[] array) throws ImageReadException {
        final ArrayList list = new ArrayList();
        for (final IcnsImageParser.IcnsElement icnsElement : array) {
            final IcnsType imageType = IcnsType.findImageType(icnsElement.type);
            if (imageType != null) {
                final IcnsImageParser.IcnsElement icnsElement2 = null;
                IcnsImageParser.IcnsElement icnsElement3;
                IcnsType find8BPPMaskType;
                if (imageType.hasMask()) {
                    icnsElement3 = icnsElement;
                    find8BPPMaskType = imageType;
                }
                else {
                    find8BPPMaskType = IcnsType.find8BPPMaskType(imageType);
                    IcnsImageParser.IcnsElement icnsElement4 = icnsElement2;
                    if (find8BPPMaskType != null) {
                        final int length2 = array.length;
                        int n = 0;
                        while (true) {
                            icnsElement4 = icnsElement2;
                            if (n >= length2) {
                                break;
                            }
                            icnsElement4 = array[n];
                            if (icnsElement4.type == find8BPPMaskType.getType()) {
                                break;
                            }
                            ++n;
                        }
                    }
                    if ((icnsElement3 = icnsElement4) == null) {
                        final IcnsType find1BPPMaskType = IcnsType.find1BPPMaskType(imageType);
                        icnsElement3 = icnsElement4;
                        if ((find8BPPMaskType = find1BPPMaskType) != null) {
                            final int length3 = array.length;
                            int n2 = 0;
                            while (true) {
                                icnsElement3 = icnsElement4;
                                find8BPPMaskType = find1BPPMaskType;
                                if (n2 >= length3) {
                                    break;
                                }
                                icnsElement3 = array[n2];
                                if (icnsElement3.type == find1BPPMaskType.getType()) {
                                    find8BPPMaskType = find1BPPMaskType;
                                    break;
                                }
                                ++n2;
                            }
                        }
                    }
                }
                if (imageType != IcnsType.ICNS_256x256_32BIT_ARGB_IMAGE) {
                    if (imageType != IcnsType.ICNS_512x512_32BIT_ARGB_IMAGE) {
                        byte[] array2;
                        if (icnsElement.data.length < (imageType.getWidth() * imageType.getHeight() * imageType.getBitsPerPixel() + 7) / 8) {
                            if (imageType.getBitsPerPixel() != 32) {
                                throw new ImageReadException("Short image data but not a 32 bit compressed type");
                            }
                            array2 = Rle24Compression.decompress(imageType.getWidth(), imageType.getHeight(), icnsElement.data);
                        }
                        else {
                            array2 = icnsElement.data;
                        }
                        final ImageBuilder imageBuilder = new ImageBuilder(imageType.getWidth(), imageType.getHeight(), true);
                        final int bitsPerPixel = imageType.getBitsPerPixel();
                        if (bitsPerPixel != 1) {
                            if (bitsPerPixel != 4) {
                                if (bitsPerPixel != 8) {
                                    if (bitsPerPixel != 32) {
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append("Unsupported bit depth ");
                                        sb.append(imageType.getBitsPerPixel());
                                        throw new ImageReadException(sb.toString());
                                    }
                                    decode32BPPImage(imageType, array2, imageBuilder);
                                }
                                else {
                                    decode8BPPImage(imageType, array2, imageBuilder);
                                }
                            }
                            else {
                                decode4BPPImage(imageType, array2, imageBuilder);
                            }
                        }
                        else {
                            decode1BPPImage(imageType, array2, imageBuilder);
                        }
                        if (icnsElement3 != null) {
                            if (find8BPPMaskType.getBitsPerPixel() == 1) {
                                apply1BPPMask(icnsElement3.data, imageBuilder);
                            }
                            else {
                                if (find8BPPMaskType.getBitsPerPixel() != 8) {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("Unsupport mask bit depth ");
                                    sb2.append(find8BPPMaskType.getBitsPerPixel());
                                    throw new ImageReadException(sb2.toString());
                                }
                                apply8BPPMask(icnsElement3.data, imageBuilder);
                            }
                        }
                        list.add(imageBuilder.getBufferedImage());
                    }
                }
            }
        }
        return list;
    }
    
    private static long[] $d2j$hex$22e02f5b$decode_J(final String src) {
        final byte[] d = $d2j$hex$22e02f5b$decode_B(src);
        final ByteBuffer b = ByteBuffer.wrap(d);
        b.order(ByteOrder.LITTLE_ENDIAN);
        final LongBuffer s = b.asLongBuffer();
        final long[] data = new long[d.length / 8];
        s.get(data);
        return data;
    }
    
    private static int[] $d2j$hex$22e02f5b$decode_I(final String src) {
        final byte[] d = $d2j$hex$22e02f5b$decode_B(src);
        final ByteBuffer b = ByteBuffer.wrap(d);
        b.order(ByteOrder.LITTLE_ENDIAN);
        final IntBuffer s = b.asIntBuffer();
        final int[] data = new int[d.length / 4];
        s.get(data);
        return data;
    }
    
    private static short[] $d2j$hex$22e02f5b$decode_S(final String src) {
        final byte[] d = $d2j$hex$22e02f5b$decode_B(src);
        final ByteBuffer b = ByteBuffer.wrap(d);
        b.order(ByteOrder.LITTLE_ENDIAN);
        final ShortBuffer s = b.asShortBuffer();
        final short[] data = new short[d.length / 2];
        s.get(data);
        return data;
    }
    
    private static byte[] $d2j$hex$22e02f5b$decode_B(final String src) {
        final char[] d = src.toCharArray();
        final byte[] ret = new byte[src.length() / 2];
        for (int i = 0; i < ret.length; ++i) {
            final char h = d[2 * i];
            final char l = d[2 * i + 1];
            int hh;
            if (h >= '0' && h <= '9') {
                hh = h - '0';
            }
            else if (h >= 'a' && h <= 'f') {
                hh = h - 'a' + 10;
            }
            else {
                if (h < 'A' || h > 'F') {
                    throw new RuntimeException();
                }
                hh = h - 'A' + 10;
            }
            int ll;
            if (l >= '0' && l <= '9') {
                ll = l - '0';
            }
            else if (l >= 'a' && l <= 'f') {
                ll = l - 'a' + 10;
            }
            else {
                if (l < 'A' || l > 'F') {
                    throw new RuntimeException();
                }
                ll = l - 'A' + 10;
            }
            ret[i] = (byte)(hh << 4 | ll);
        }
        return ret;
    }
}
