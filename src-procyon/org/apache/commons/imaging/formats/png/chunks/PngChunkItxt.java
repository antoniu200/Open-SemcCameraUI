// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.chunks;

import org.apache.commons.imaging.formats.png.PngText;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;
import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;

public class PngChunkItxt extends PngTextChunk
{
    public final String keyword;
    public final String languageTag;
    public final String text;
    public final String translatedKeyword;
    
    public PngChunkItxt(int null, int null2, int null3, final byte[] array) throws ImageReadException, IOException {
        super(null, null2, null3, array);
        null = BinaryFunctions.findNull(array);
        if (null < 0) {
            throw new ImageReadException("PNG iTXt chunk keyword is not terminated.");
        }
        this.keyword = new String(array, 0, null, "ISO-8859-1");
        null2 = ++null + 1;
        null = array[null];
        if (null != 0 && null != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("PNG iTXt chunk has invalid compression flag: ");
            sb.append(null);
            throw new ImageReadException(sb.toString());
        }
        if (null == 1) {
            null = 1;
        }
        else {
            null = 0;
        }
        null3 = null2 + 1;
        null2 = array[null2];
        if (null != 0 && null2 != 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("PNG iTXt chunk has unexpected compression method: ");
            sb2.append(null2);
            throw new ImageReadException(sb2.toString());
        }
        null2 = BinaryFunctions.findNull(array, null3);
        if (null2 < 0) {
            throw new ImageReadException("PNG iTXt chunk language tag is not terminated.");
        }
        this.languageTag = new String(array, null3, null2 - null3, "ISO-8859-1");
        ++null2;
        null3 = BinaryFunctions.findNull(array, null2);
        if (null3 < 0) {
            throw new ImageReadException("PNG iTXt chunk translated keyword is not terminated.");
        }
        this.translatedKeyword = new String(array, null2, null3 - null2, "utf-8");
        null2 = null3 + 1;
        if (null != 0) {
            null = array.length - null2;
            final byte[] buf = new byte[null];
            System.arraycopy(array, null2, buf, 0, null);
            this.text = new String(BinaryFunctions.getStreamBytes(new InflaterInputStream(new ByteArrayInputStream(buf))), "utf-8");
        }
        else {
            this.text = new String(array, null2, array.length - null2, "utf-8");
        }
    }
    
    @Override
    public PngText getContents() {
        return new PngText.Itxt(this.keyword, this.text, this.languageTag, this.translatedKeyword);
    }
    
    @Override
    public String getKeyword() {
        return this.keyword;
    }
    
    @Override
    public String getText() {
        return this.text;
    }
}
