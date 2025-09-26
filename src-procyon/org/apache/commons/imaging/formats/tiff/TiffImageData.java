// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff;

import org.apache.commons.imaging.formats.tiff.datareaders.DataReaderTiled;
import org.apache.commons.imaging.formats.tiff.datareaders.DataReaderStrips;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import org.apache.commons.imaging.ImageReadException;
import java.io.IOException;
import org.apache.commons.imaging.formats.tiff.datareaders.DataReader;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter;

public abstract class TiffImageData
{
    public abstract DataReader getDataReader(final TiffDirectory p0, final PhotometricInterpreter p1, final int p2, final int[] p3, final int p4, final int p5, final int p6, final int p7, final int p8, final ByteOrder p9) throws IOException, ImageReadException;
    
    public abstract TiffElement.DataElement[] getImageData();
    
    public abstract boolean stripsNotTiles();
    
    public static class ByteSourceData extends Data
    {
        ByteSourceFile byteSourceFile;
        
        public ByteSourceData(final long n, final int n2, final ByteSourceFile byteSourceFile) {
            super(n, n2, new byte[0]);
            this.byteSourceFile = byteSourceFile;
        }
        
        @Override
        public byte[] getData() {
            try {
                return this.byteSourceFile.getBlock(this.offset, this.length);
            }
            catch (final IOException ex) {
                return new byte[0];
            }
        }
        
        @Override
        public String getElementDescription(final boolean b) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tiff image data: ");
            sb.append(((DataElement)this).getDataLength());
            sb.append(" bytes");
            return sb.toString();
        }
    }
    
    public static class Data extends DataElement
    {
        public Data(final long n, final int n2, final byte[] array) {
            super(n, n2, array);
        }
        
        @Override
        public String getElementDescription(final boolean b) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tiff image data: ");
            sb.append(((DataElement)this).getDataLength());
            sb.append(" bytes");
            return sb.toString();
        }
    }
    
    public static class Strips extends TiffImageData
    {
        public final int rowsPerStrip;
        private final TiffElement.DataElement[] strips;
        
        public Strips(final TiffElement.DataElement[] strips, final int rowsPerStrip) {
            this.strips = strips;
            this.rowsPerStrip = rowsPerStrip;
        }
        
        @Override
        public DataReader getDataReader(final TiffDirectory tiffDirectory, final PhotometricInterpreter photometricInterpreter, final int n, final int[] array, final int n2, final int n3, final int n4, final int n5, final int n6, final ByteOrder byteOrder) throws IOException, ImageReadException {
            return new DataReaderStrips(tiffDirectory, photometricInterpreter, n, array, n2, n3, n4, n5, n6, byteOrder, this.rowsPerStrip, this);
        }
        
        public TiffElement.DataElement getImageData(final int n) {
            return this.strips[n];
        }
        
        @Override
        public TiffElement.DataElement[] getImageData() {
            return this.strips;
        }
        
        public int getImageDataLength() {
            return this.strips.length;
        }
        
        @Override
        public boolean stripsNotTiles() {
            return true;
        }
    }
    
    public static class Tiles extends TiffImageData
    {
        private final int tileLength;
        private final int tileWidth;
        public final TiffElement.DataElement[] tiles;
        
        public Tiles(final TiffElement.DataElement[] tiles, final int tileWidth, final int tileLength) {
            this.tiles = tiles;
            this.tileWidth = tileWidth;
            this.tileLength = tileLength;
        }
        
        @Override
        public DataReader getDataReader(final TiffDirectory tiffDirectory, final PhotometricInterpreter photometricInterpreter, final int n, final int[] array, final int n2, final int n3, final int n4, final int n5, final int n6, final ByteOrder byteOrder) throws IOException, ImageReadException {
            return new DataReaderTiled(tiffDirectory, photometricInterpreter, this.tileWidth, this.tileLength, n, array, n2, n3, n4, n5, n6, byteOrder, this);
        }
        
        @Override
        public TiffElement.DataElement[] getImageData() {
            return this.tiles;
        }
        
        public int getTileHeight() {
            return this.tileLength;
        }
        
        public int getTileWidth() {
            return this.tileWidth;
        }
        
        @Override
        public boolean stripsNotTiles() {
            return false;
        }
    }
}
