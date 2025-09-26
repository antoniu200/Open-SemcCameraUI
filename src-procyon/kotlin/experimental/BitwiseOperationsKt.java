// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.experimental;

import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0010\n\u0000\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\n\n\u0002\b\u0004\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0000\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0004\u001a\u00020\u0003*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0005\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0006\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f¨\u0006\u0007" }, d2 = { "and", "", "other", "", "inv", "or", "xor", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
public final class BitwiseOperationsKt
{
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte and(final byte b, final byte b2) {
        return (byte)(b & b2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short and(final short n, final short n2) {
        return (short)(n & n2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte inv(final byte b) {
        return (byte)~b;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short inv(final short n) {
        return (short)~n;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte or(final byte b, final byte b2) {
        return (byte)(b | b2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short or(final short n, final short n2) {
        return (short)(n | n2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte xor(final byte b, final byte b2) {
        return (byte)(b ^ b2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short xor(final short n, final short n2) {
        return (short)(n ^ n2);
    }
}
