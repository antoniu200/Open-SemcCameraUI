// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class BinaryConstant implements Cloneable
{
    private final byte[] value;
    
    public BinaryConstant(final byte[] array) {
        this.value = array.clone();
    }
    
    public BinaryConstant clone() throws CloneNotSupportedException {
        return (BinaryConstant)super.clone();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && o instanceof BinaryConstant && this.equals(((BinaryConstant)o).value);
    }
    
    public boolean equals(final byte[] a2) {
        return Arrays.equals(this.value, a2);
    }
    
    public boolean equals(final byte[] array, final int n, final int n2) {
        if (this.value.length != n2) {
            return false;
        }
        for (int i = 0; i < n2; ++i) {
            if (this.value[i] != array[n + i]) {
                return false;
            }
        }
        return true;
    }
    
    public byte get(final int n) {
        return this.value[n];
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }
    
    public int size() {
        return this.value.length;
    }
    
    public byte[] toByteArray() {
        return this.value.clone();
    }
    
    public void writeTo(final OutputStream outputStream) throws IOException {
        final byte[] value = this.value;
        for (int length = value.length, i = 0; i < length; ++i) {
            outputStream.write(value[i]);
        }
    }
}
