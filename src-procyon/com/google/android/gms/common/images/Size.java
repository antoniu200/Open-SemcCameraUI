// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.images;

public final class Size
{
    private final int zznQ;
    private final int zznR;
    
    public Size(final int zznQ, final int zznR) {
        this.zznQ = zznQ;
        this.zznR = zznR;
    }
    
    public static Size parseSize(final String s) throws NumberFormatException {
        if (s == null) {
            throw new IllegalArgumentException("string must not be null");
        }
        int endIndex;
        if ((endIndex = s.indexOf(42)) < 0) {
            endIndex = s.indexOf(120);
        }
        if (endIndex < 0) {
            throw zzch(s);
        }
        try {
            return new Size(Integer.parseInt(s.substring(0, endIndex)), Integer.parseInt(s.substring(endIndex + 1)));
        }
        catch (final NumberFormatException ex) {
            throw zzch(s);
        }
    }
    
    private static NumberFormatException zzch(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid Size: \"");
        sb.append(str);
        sb.append("\"");
        throw new NumberFormatException(sb.toString());
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        boolean b2 = b;
        if (o instanceof Size) {
            final Size size = (Size)o;
            b2 = b;
            if (this.zznQ == size.zznQ) {
                b2 = b;
                if (this.zznR == size.zznR) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    public int getHeight() {
        return this.zznR;
    }
    
    public int getWidth() {
        return this.zznQ;
    }
    
    @Override
    public int hashCode() {
        return (this.zznQ >>> 16 | this.zznQ << 16) ^ this.zznR;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.zznQ);
        sb.append("x");
        sb.append(this.zznR);
        return sb.toString();
    }
}
