// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common.mylzw;

import java.io.IOException;
import java.io.InputStream;

public class BitsToByteInputStream extends InputStream
{
    private final int desiredDepth;
    private final MyBitInputStream is;
    
    public BitsToByteInputStream(final MyBitInputStream is, final int desiredDepth) {
        this.is = is;
        this.desiredDepth = desiredDepth;
    }
    
    @Override
    public int read() throws IOException {
        return this.readBits(8);
    }
    
    public int readBits(final int n) throws IOException {
        final int bits = this.is.readBits(n);
        int n2;
        if (n < this.desiredDepth) {
            n2 = bits << this.desiredDepth - n;
        }
        else {
            n2 = bits;
            if (n > this.desiredDepth) {
                n2 = bits >> n - this.desiredDepth;
            }
        }
        return n2;
    }
    
    public int[] readBitsArray(final int n, final int n2) throws IOException {
        final int[] array = new int[n2];
        for (int i = 0; i < n2; ++i) {
            array[i] = this.readBits(n);
        }
        return array;
    }
}
