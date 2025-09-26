// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

class PngCrc
{
    private final long[] crc_table;
    private boolean crc_table_computed;
    
    PngCrc() {
        this.crc_table = new long[256];
    }
    
    private void make_crc_table() {
        for (int i = 0; i < 256; ++i) {
            long n = i;
            for (int j = 0; j < 8; ++j) {
                if ((0x1L & n) != 0x0L) {
                    n = (n >> 1 ^ 0xEDB88320L);
                }
                else {
                    n >>= 1;
                }
            }
            this.crc_table[i] = n;
        }
        this.crc_table_computed = true;
    }
    
    private long update_crc(long n, final byte[] array) {
        if (!this.crc_table_computed) {
            this.make_crc_table();
        }
        for (int i = 0; i < array.length; ++i) {
            n = (n >> 8 ^ this.crc_table[(int)(((long)array[i] ^ n) & 0xFFL)]);
        }
        return n;
    }
    
    public final long continue_partial_crc(final long n, final byte[] array, final int n2) {
        return this.update_crc(n, array);
    }
    
    public final int crc(final byte[] array, final int n) {
        return (int)(this.update_crc(4294967295L, array) ^ 0xFFFFFFFFL);
    }
    
    public final long finish_partial_crc(final long n) {
        return n ^ 0xFFFFFFFFL;
    }
    
    public final long start_partial_crc(final byte[] array, final int n) {
        return this.update_crc(4294967295L, array);
    }
}
