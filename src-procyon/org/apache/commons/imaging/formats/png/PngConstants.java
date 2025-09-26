// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

import org.apache.commons.imaging.common.BinaryConstant;

public final class PngConstants
{
    public static final int COMPRESSION_DEFLATE_INFLATE = 0;
    public static final byte COMPRESSION_TYPE_INFLATE_DEFLATE = 0;
    public static final byte FILTER_METHOD_ADAPTIVE = 0;
    public static final String PARAM_KEY_PNG_BIT_DEPTH = "PNG_BIT_DEPTH";
    public static final String PARAM_KEY_PNG_FORCE_INDEXED_COLOR = "PNG_FORCE_INDEXED_COLOR";
    public static final String PARAM_KEY_PNG_FORCE_TRUE_COLOR = "PNG_FORCE_TRUE_COLOR";
    public static final String PARAM_KEY_PNG_TEXT_CHUNKS = "PNG_TEXT_CHUNKS";
    public static final BinaryConstant PNG_SIGNATURE;
    public static final String XMP_KEYWORD = "XML:com.adobe.xmp";
    
    static {
        PNG_SIGNATURE = new BinaryConstant(new byte[] { -119, 80, 78, 71, 13, 10, 26, 10 });
    }
    
    private PngConstants() {
    }
}
