// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.decoder;

final class Block
{
    final int height;
    final int[] samples;
    final int width;
    
    Block(final int width, final int height) {
        this.samples = new int[width * height];
        this.width = width;
        this.height = height;
    }
}
